const { createCanvas, loadImage, GlobalFonts } = require('@napi-rs/canvas');

// Tamaño del banner. Se renderiza al doble de resolución que el boceto
// aprobado (550x380 -> 1100x760) para que se vea nítido en Discord.
const WIDTH = 1100;
const HEIGHT = 760;

// @napi-rs/canvas no hace fallback automático de fuente por carácter (a
// diferencia de un navegador): si el texto contiene un emoji y la fuente
// activa no tiene ese glifo, no lo dibuja. Por eso los emojis se detectan
// aparte y se dibujan explícitamente con esta fuente.
const EMOJI_FONT_FAMILY = 'Noto Color Emoji';
let emojiFontAvailable = null;

function isEmojiFontAvailable() {
  if (emojiFontAvailable === null) {
    try {
      emojiFontAvailable = GlobalFonts.families.some(
        (f) => f.family.toLowerCase() === EMOJI_FONT_FAMILY.toLowerCase(),
      );
    } catch (err) {
      emojiFontAvailable = false;
    }
  }
  return emojiFontAvailable;
}

const EMOJI_RUN_REGEX =
  /(\p{Extended_Pictographic}(?:\uFE0F)?(?:\u200D\p{Extended_Pictographic}(?:\uFE0F)?)*|\p{Regional_Indicator}{2})/gu;

function splitEmojiSegments(text) {
  const segments = [];
  let lastIndex = 0;
  let match;
  EMOJI_RUN_REGEX.lastIndex = 0;
  while ((match = EMOJI_RUN_REGEX.exec(text)) !== null) {
    if (match.index > lastIndex) segments.push({ text: text.slice(lastIndex, match.index), emoji: false });
    segments.push({ text: match[0], emoji: true });
    lastIndex = match.index + match[0].length;
  }
  if (lastIndex < text.length) segments.push({ text: text.slice(lastIndex), emoji: false });
  return segments;
}

function measureMixed(ctx, text, normalFont, emojiFont, hasEmojiFont) {
  const segments = splitEmojiSegments(text);
  let total = 0;
  for (const seg of segments) {
    ctx.font = seg.emoji && hasEmojiFont ? emojiFont : normalFont;
    total += ctx.measureText(seg.text).width;
  }
  return total;
}

// Trunca un texto (con posibles emojis) para que quepa en maxWidth, SIN
// reducir el tamaño de fuente. Añade "…" si hace falta cortar.
function truncateToWidth(ctx, text, maxWidth, normalFont, emojiFont, hasEmojiFont) {
  if (measureMixed(ctx, text, normalFont, emojiFont, hasEmojiFont) <= maxWidth) return text;

  let result = text;
  while (result.length > 0) {
    result = result.slice(0, -1);
    const candidate = `${result}…`;
    if (measureMixed(ctx, candidate, normalFont, emojiFont, hasEmojiFont) <= maxWidth) {
      return candidate;
    }
  }
  return '…';
}

function drawMixedLine(ctx, text, x, y, normalFont, emojiFontSize) {
  const hasEmojiFont = isEmojiFontAvailable();
  const emojiFont = `${emojiFontSize}px "${EMOJI_FONT_FAMILY}"`;
  const segments = splitEmojiSegments(text);

  const prevAlign = ctx.textAlign;
  ctx.textAlign = 'left';
  let cursor = x;
  for (const seg of segments) {
    if (seg.emoji && !hasEmojiFont) continue;
    ctx.font = seg.emoji ? emojiFont : normalFont;
    ctx.fillText(seg.text, cursor, y);
    cursor += ctx.measureText(seg.text).width;
  }
  ctx.textAlign = prevAlign;
}

async function loadImageSafe(url) {
  if (!url) return null;
  try {
    return await loadImage(url);
  } catch (err) {
    console.error('No se pudo cargar la imagen', url, err.message);
    return null;
  }
}

function drawRoundedRectPath(ctx, x, y, w, h, r) {
  ctx.beginPath();
  ctx.moveTo(x + r, y);
  ctx.arcTo(x + w, y, x + w, y + h, r);
  ctx.arcTo(x + w, y + h, x, y + h, r);
  ctx.arcTo(x, y + h, x, y, r);
  ctx.arcTo(x, y, x + w, y, r);
  ctx.closePath();
}

function rgbToHex(r, g, b) {
  return `#${[r, g, b].map((v) => Math.max(0, Math.min(255, v)).toString(16).padStart(2, '0')).join('')}`;
}

function colorDistance(a, b) {
  return Math.sqrt((a.r - b.r) ** 2 + (a.g - b.g) ** 2 + (a.b - b.b) ** 2);
}

function sampleBlock(data, width, height, cx, cy, size) {
  let r = 0;
  let g = 0;
  let b = 0;
  let a = 0;
  let count = 0;

  for (let dy = 0; dy < size; dy++) {
    for (let dx = 0; dx < size; dx++) {
      const x = cx + dx;
      const y = cy + dy;
      if (x < 0 || x >= width || y < 0 || y >= height) continue;
      const idx = (y * width + x) * 4;
      r += data[idx];
      g += data[idx + 1];
      b += data[idx + 2];
      a += data[idx + 3];
      count++;
    }
  }

  if (count === 0) return { r: 0, g: 0, b: 0, a: 0 };
  return { r: r / count, g: g / count, b: b / count, a: a / count };
}

/**
 * Analiza las 4 esquinas de una imagen ya cargada para averiguar el color
 * de fondo "real" del PNG (útil para logos con transparencia o con un
 * fondo sólido cocido en el propio archivo).
 *
 * Devuelve un hex (p.ej. "#1a1b1f") si detecta un fondo sólido consistente
 * en las 4 esquinas, o `null` si el fondo es mayormente transparente o las
 * esquinas no coinciden entre sí (imagen sin un fondo uniforme claro, como
 * una foto).
 */
function detectImageBackgroundColor(image) {
  const size = 64;
  const canvas = createCanvas(size, size);
  const ctx = canvas.getContext('2d');
  ctx.drawImage(image, 0, 0, size, size);

  const { data } = ctx.getImageData(0, 0, size, size);
  const margin = 2; // se evita el borde exacto por el antialiasing
  const blockSize = 4;

  const corners = [
    sampleBlock(data, size, size, margin, margin, blockSize),
    sampleBlock(data, size, size, size - margin - blockSize, margin, blockSize),
    sampleBlock(data, size, size, margin, size - margin - blockSize, blockSize),
    sampleBlock(data, size, size, size - margin - blockSize, size - margin - blockSize, blockSize),
  ];

  const avgAlpha = corners.reduce((sum, c) => sum + c.a, 0) / corners.length;
  if (avgAlpha < 40) return null; // esquinas mayormente transparentes

  const reference = corners[0];
  const isConsistent = corners.every((c) => colorDistance(reference, c) < 30);
  if (!isConsistent) return null; // las esquinas no coinciden: no hay un fondo uniforme claro

  const avgR = Math.round(corners.reduce((sum, c) => sum + c.r, 0) / corners.length);
  const avgG = Math.round(corners.reduce((sum, c) => sum + c.g, 0) / corners.length);
  const avgB = Math.round(corners.reduce((sum, c) => sum + c.b, 0) / corners.length);

  return rgbToHex(avgR, avgG, avgB);
}

/**
 * Extrae los `count` colores dominantes de una imagen ya cargada
 * (@napi-rs/canvas Image), como array de strings hex (p.ej. "#5a1c22").
 * No depende de librerías externas: reduce la imagen a un tamaño pequeño,
 * agrupa píxeles en "cubos" de color similares, y se queda con los cubos
 * más frecuentes que sean suficientemente distintos entre sí.
 */
function extractDominantColors(image, count = 2) {
  const size = 48;
  const canvas = createCanvas(size, size);
  const ctx = canvas.getContext('2d');
  ctx.drawImage(image, 0, 0, size, size);

  const { data } = ctx.getImageData(0, 0, size, size);
  const BUCKET = 24;
  const buckets = new Map();

  for (let i = 0; i < data.length; i += 4) {
    const r = data[i];
    const g = data[i + 1];
    const b = data[i + 2];
    const a = data[i + 3];
    if (a < 200) continue; // ignora píxeles casi transparentes

    const brightness = (r + g + b) / 3;
    if (brightness > 235 || brightness < 15) continue; // ignora casi blanco/negro puro

    const key = [
      Math.round(r / BUCKET) * BUCKET,
      Math.round(g / BUCKET) * BUCKET,
      Math.round(b / BUCKET) * BUCKET,
    ].join(',');
    buckets.set(key, (buckets.get(key) || 0) + 1);
  }

  const sorted = [...buckets.entries()].sort((a, b) => b[1] - a[1]);
  const picked = [];

  for (const [key] of sorted) {
    const [r, g, b] = key.split(',').map(Number);
    const candidate = { r, g, b };
    const isDistinct = picked.every((c) => colorDistance(c, candidate) > 60);
    if (isDistinct) picked.push(candidate);
    if (picked.length === count) break;
  }

  while (picked.length < count) {
    picked.push(picked[0] ?? { r: 42, g: 43, b: 48 });
  }

  return picked.map(({ r, g, b }) => rgbToHex(r, g, b));
}

/**
 * Calcula los colores dominantes de una URL de imagen. Devuelve
 * [color1, color2] en hex, o null si la imagen no se pudo cargar.
 */
async function computeAccentColorsFromImage(imageUrl) {
  const image = await loadImageSafe(imageUrl);
  if (!image) return null;
  try {
    return extractDominantColors(image, 2);
  } catch (err) {
    console.error('No se pudieron extraer los colores dominantes:', err);
    return null;
  }
}

const DEFAULT_ACCENT_1 = '#2a2b30';
const DEFAULT_ACCENT_2 = '#3a3b42';
// Gris de fondo real del tema oscuro de Discord (zona de mensajes), usado
// como valor por defecto para el fondo de la tarjeta cuando no hay imagen
// de servidor configurada o no se le puede detectar un color de fondo.
const DISCORD_DEFAULT_BG = '#313338';

/**
 * Genera el banner de bienvenida como Buffer PNG.
 *
 * @param {object} opts
 * @param {string} opts.username - Nombre a mostrar (para el avatar/alt, no se imprime aparte)
 * @param {string} opts.avatarURL - URL del avatar del usuario
 * @param {string|null} opts.imageURL - URL de la imagen/logo configurada para el servidor
 * @param {string} opts.welcomeText - Texto ya con variables sustituidas (una sola línea)
 * @param {string|null} opts.accentColor1 - Color hex manual (o null para usar el automático)
 * @param {string|null} opts.accentColor2 - Color hex manual (o null para usar el automático)
 * @param {string|null} opts.autoColor1 - Color hex auto-extraído cacheado (fallback si no hay manual)
 * @param {string|null} opts.autoColor2 - Color hex auto-extraído cacheado (fallback si no hay manual)
 */
async function generateWelcomeBanner({
  avatarURL,
  imageURL,
  welcomeText,
  accentColor1,
  accentColor2,
  autoColor1,
  autoColor2,
}) {
  const color1 = accentColor1 || autoColor1 || DEFAULT_ACCENT_1;
  const color2 = accentColor2 || autoColor2 || DEFAULT_ACCENT_2;

  // Se carga la imagen del servidor ya aquí (antes de dibujar nada) para
  // poder detectar el color de fondo real del PNG (transparente o sólido)
  // y usarlo como fondo de toda la tarjeta (banda 3).
  const serverImage = await loadImageSafe(imageURL);
  let imageBgColor = DISCORD_DEFAULT_BG;
  if (serverImage) {
    try {
      imageBgColor = detectImageBackgroundColor(serverImage) ?? DISCORD_DEFAULT_BG;
    } catch (err) {
      console.error('No se pudo detectar el color de fondo de la imagen:', err);
    }
  }

  const canvas = createCanvas(WIDTH, HEIGHT);
  const ctx = canvas.getContext('2d');

  // ---- Fondo (banda 3): todo el fondo de la tarjeta usa el color
  // detectado en la imagen del servidor, o el gris real de Discord si no
  // hay imagen o no se le pudo detectar un fondo claro. ----
  drawRoundedRectPath(ctx, 0, 0, WIDTH, HEIGHT, 52);
  ctx.clip();

  ctx.fillStyle = imageBgColor;
  ctx.fillRect(0, 0, WIDTH, HEIGHT);

  // Bandas diagonales decorativas en la esquina inferior izquierda:
  //   Banda 2 (más ancha/exterior): color2, dominante/manual.
  //   Banda 1 (más estrecha/interior, junto a la esquina): color1, dominante/manual.
  ctx.globalAlpha = 0.85;
  ctx.fillStyle = color2;
  ctx.beginPath();
  ctx.moveTo(0, 760);
  ctx.lineTo(0, 360);
  ctx.lineTo(420, 760);
  ctx.closePath();
  ctx.fill();

  ctx.globalAlpha = 0.9;
  ctx.fillStyle = color1;
  ctx.beginPath();
  ctx.moveTo(0, 760);
  ctx.lineTo(0, 500);
  ctx.lineTo(260, 760);
  ctx.closePath();
  ctx.fill();

  // Banda 4 (esquina superior derecha): mismo color que la banda 1, opaca,
  // y dibujada ANTES que la imagen del servidor a propósito, para que
  // quede debajo de ella (tapada donde se solapen).
  ctx.globalAlpha = 1;
  ctx.fillStyle = color1;
  ctx.beginPath();
  ctx.moveTo(1100, 0);
  ctx.lineTo(1100, 300);
  ctx.lineTo(680, 0);
  ctx.closePath();
  ctx.fill();

  // ---- Imagen / logo del servidor ----
  const imgX = 360;
  const imgY = 55;
  const imgW = 720;
  const imgH = 675;
  const imgR = 28;

  ctx.save();
  drawRoundedRectPath(ctx, imgX, imgY, imgW, imgH, imgR);
  ctx.clip();

  if (serverImage) {
    // Encaja la imagen tipo "contain": la escala para que quepa ENTERA en
    // el hueco sin recortar nada, dejando margen en el eje que sobre.
    // OJO: no se rellena el hueco con un color sólido antes de dibujar la
    // imagen — si el PNG tiene transparencia real, debe dejar ver las
    // bandas/el fondo que hay debajo (bandas 1, 2 y 4), no taparlas.
    const scale = Math.min(imgW / serverImage.width, imgH / serverImage.height);
    const drawW = serverImage.width * scale;
    const drawH = serverImage.height * scale;
    const drawX = imgX + (imgW - drawW) / 2;
    const drawY = imgY + (imgH - drawH) / 2;
    ctx.drawImage(serverImage, drawX, drawY, drawW, drawH);
  } else {
    ctx.fillStyle = imageBgColor;
    ctx.fillRect(imgX, imgY, imgW, imgH);
    ctx.fillStyle = '#777777';
    ctx.font = '600 24px sans-serif';
    ctx.textAlign = 'center';
    ctx.fillText('Sin imagen configurada', imgX + imgW / 2, imgY + imgH / 2);
    ctx.font = '18px sans-serif';
    ctx.fillStyle = '#666666';
    ctx.fillText('/setwelcomeimage', imgX + imgW / 2, imgY + imgH / 2 + 32);
    ctx.textAlign = 'left';
  }
  ctx.restore();

  // ---- Avatar del usuario: centrado verticalmente, sin texto extra ----
  const avatarCx = 200;
  const avatarCy = 410;
  const avatarR = 144;

  ctx.beginPath();
  ctx.arc(avatarCx, avatarCy, avatarR, 0, Math.PI * 2);
  ctx.fillStyle = '#2a2d3d';
  ctx.fill();

  const avatar = await loadImageSafe(avatarURL);
  if (avatar) {
    ctx.save();
    ctx.beginPath();
    ctx.arc(avatarCx, avatarCy, avatarR, 0, Math.PI * 2);
    ctx.clip();
    ctx.drawImage(avatar, avatarCx - avatarR, avatarCy - avatarR, avatarR * 2, avatarR * 2);
    ctx.restore();
  } else {
    ctx.fillStyle = '#ffffff';
    ctx.globalAlpha = 0.85;
    ctx.beginPath();
    ctx.arc(avatarCx, avatarCy - 22, 38, 0, Math.PI * 2);
    ctx.fill();
    ctx.beginPath();
    ctx.moveTo(avatarCx - 52, avatarCy + 78);
    ctx.quadraticCurveTo(avatarCx, avatarCy + 10, avatarCx + 52, avatarCy + 78);
    ctx.lineTo(avatarCx + 52, avatarCy + 90);
    ctx.lineTo(avatarCx - 52, avatarCy + 90);
    ctx.closePath();
    ctx.fill();
    ctx.globalAlpha = 1;
  }

  // ---- Texto de bienvenida: una sola línea, se trunca si no cabe ----
  const textX = 68;
  const textY = 104;
  const font = 'bold 42px sans-serif';
  const emojiFontSize = 42;
  // El texto vive en la franja superior (y≈60-115) y la imagen empieza más
  // abajo (imgY=190), así que no comparten espacio vertical: el texto puede
  // usar casi todo el ancho de la tarjeta sin riesgo de tocar la imagen.
  const rightMargin = 68;
  const maxTextWidth = WIDTH - textX - rightMargin;

  ctx.fillStyle = '#ffffff';
  ctx.font = font;
  const finalText = truncateToWidth(ctx, welcomeText, maxTextWidth, font, `${emojiFontSize}px "${EMOJI_FONT_FAMILY}"`, isEmojiFontAvailable());
  drawMixedLine(ctx, finalText, textX, textY, font, emojiFontSize);

  return canvas.toBuffer('image/png');
}

module.exports = { generateWelcomeBanner, computeAccentColorsFromImage };
