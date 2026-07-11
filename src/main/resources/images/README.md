# Rasecbot

Bot de Discord con dos funciones principales:

1. **Canales de voz dinámicos**: crea automáticamente un canal de voz nuevo
   cuando alguien se conecta a un canal "creador" configurado, y lo borra
   cuando se queda vacío.
2. **Moderación anti-spam**: detecta y actúa contra usuarios que publican el
   mismo mensaje en varios canales en poco tiempo.
3. **Rol inicial automático**: asigna un rol concreto a cada nuevo miembro
   en cuanto entra al servidor.
4. **Tarjeta de bienvenida**: genera una imagen elegante con el avatar del
   usuario, el icono del servidor, un mensaje personalizable y la fecha/hora,
   al entrar un nuevo miembro.
5. **Mensaje de despedida**: aviso de texto plano cuando alguien abandona el
   servidor o es baneado (incluido el baneo automático por spam).
6. **Registro de eventos (logs)**: publica en un canal fijo todo lo relevante
   que ocurre en el servidor (mensajes editados/borrados, canales y roles
   creados/eliminados/modificados, entradas/salidas/baneos, cambios de
   apodo y roles, actividad de voz, cambios del propio servidor).
7. **Estado de streaming en el canal de voz**: detecta los avisos de un
   webhook (p.ej. de tu infraestructura de streaming en `raseconline.es`)
   y actualiza automáticamente el "estado" del canal de voz del streamer.

Funciona en **cualquier número de servidores** simultáneamente: basta con
invitar al bot a cada servidor con el mismo enlace.

## 1. Crear la aplicación en Discord

1. Ve a https://discord.com/developers/applications y pulsa **New Application**.
   Nómbrala `Rasecbot`.
2. En **Bot** → **Reset Token** → copia el token (`DISCORD_TOKEN`). No lo
   compartas ni lo subas a git.
3. En **Bot → Privileged Gateway Intents**, activa:
   - **MESSAGE CONTENT INTENT** (imprescindible para la detección de spam,
     ya que el bot necesita leer el contenido de los mensajes)
   - **SERVER MEMBERS INTENT** (imprescindible para detectar cuándo entra
     un nuevo miembro y poder asignarle el rol inicial)
4. En **OAuth2 → General**, copia el **Application ID** (`CLIENT_ID`).
5. En **OAuth2 → URL Generator**:
   - Scopes: `bot`, `applications.commands`
   - Bot Permissions:
     - `View Channels`
     - `Manage Channels`
     - `Move Members`
     - `Connect`
     - `Send Messages`
     - `Manage Messages` (para borrar los mensajes de spam)
     - `Read Message History`
     - `Ban Members` (para expulsar a los reincidentes)
     - `Manage Roles` (para asignar el rol inicial a nuevos miembros)
     - `Attach Files` (para publicar la tarjeta de bienvenida)
     - `Embed Links` (para publicar las entradas del registro de eventos)
     - `Establecer estado del canal de voz` / `Set Voice Channel Status`
       (para el estado "🔴 ONLINE..." al detectar el stream)
6. Abre la URL generada y repítelo **una vez por cada servidor** al que
   quieras añadir el bot.
7. Activa el **modo desarrollador** en Discord (Ajustes → Avanzado) y copia
   el ID de cada servidor (clic derecho sobre el servidor → **Copiar ID**).

> **Nota sobre "Aplicación privada"**: si tu bot es privado (Public Bot
> desactivado), ve a la pestaña **Installation** de tu aplicación y pon
> **Install Link** en `None`. Para invitarlo sigue usando la URL generada
> manualmente en el paso 5.

## 2. Varios servidores de Discord

El bot **no necesita ninguna configuración especial para funcionar en varios
servidores**: toda la configuración (canales creadores, canales dinámicos
activos, infracciones de spam) se guarda internamente separada por servidor
(`guildId`), así que simplemente invitándolo a cada servidor ya funciona ahí
de forma independiente.

La única razón para indicar los IDs de servidor en `.env` es para que los
**comandos slash** (`/setcreatechannel`, etc.) aparezcan **al instante** en
esos servidores en lugar de tardar hasta 1 hora (que es lo que tardan en
propagarse los comandos globales).

En `.env`:

```
GUILD_IDS=123456789012345678,987654321098765432
```

Puedes añadir tantos IDs como servidores tengas, separados por comas. Cada
vez que añadas un nuevo servidor, agrega su ID a `GUILD_IDS` y vuelve a
ejecutar `npm run deploy`.

## 3. Preparar el proyecto en local (opcional, para probar)

```bash
cd rasecbot
cp .env.example .env
# Edita .env: DISCORD_TOKEN, CLIENT_ID y GUILD_IDS
npm install
npm run deploy   # registra los comandos slash en todos los servidores listados
npm start        # arranca el bot
```

## 4. Desplegar en el NUC (Ubuntu Server 26.04)

### 4.1 Instalar Node.js (si no lo tienes)

```bash
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt-get install -y nodejs
node -v   # debe ser >= 18
```

### 4.2 Copiar el proyecto al NUC

Desde tu Mac, con el proyecto ya descargado localmente:

```bash
scp -r rasecbot cesar@192.168.7.2:/tmp/rasecbot
```

En el NUC:

```bash
sudo mkdir -p /opt/rasecbot
sudo cp -r /tmp/rasecbot/* /opt/rasecbot/
sudo cp /tmp/rasecbot/.env.example /opt/rasecbot/.env.example
cd /opt/rasecbot
sudo cp .env.example .env
sudo nano .env   # rellena DISCORD_TOKEN, CLIENT_ID, GUILD_IDS
sudo chown -R cesar:cesar /opt/rasecbot
npm install
npm run deploy   # registra los comandos slash (repetir cuando cambies GUILD_IDS o los comandos)
```

### 4.3 Instalar como servicio systemd

```bash
sudo cp /opt/rasecbot/rasecbot.service /etc/systemd/system/rasecbot.service
```

Revisa `/etc/systemd/system/rasecbot.service` y ajusta `User=` / `Group=` si
tu usuario en el NUC no se llama `cesar`.

```bash
sudo systemctl daemon-reload
sudo systemctl enable --now rasecbot
sudo systemctl status rasecbot
```

Logs en tiempo real:

```bash
journalctl -u rasecbot -f
```

## 5. Comandos disponibles

Todos son *slash commands* (escribe `/` en el chat). Los que modifican
configuración requieren el permiso **Gestionar servidor**.

- **`/setcreatechannel canal:"Crear Canal" prefijo:"Sala"`**
  Marca el canal de voz "Crear Canal" como creador. Quien se conecte
  generará automáticamente `Sala #1`, `Sala #2`, etc., y será movido dentro.

- **`/removecreatechannel canal:"Crear Canal"`**
  Deja de tratar ese canal como creador.

- **`/listcreatechannels`**
  Lista los canales creadores configurados y sus prefijos.

- **`/clearinfractions usuario:@alguien`**
  Reinicia el contador de infracciones de spam de un usuario (por si se
  aplicó por error, o quieres darle una segunda oportunidad).

- **`/setinitialrol rol:@Miembro`**
  Define el rol que se asignará automáticamente a cada nuevo miembro que
  entre al servidor.

- **`/removeinitialrol`**
  Deja de asignar un rol automáticamente a los nuevos miembros.

- **`/showinitialrol`**
  Muestra qué rol está configurado actualmente como rol inicial.

- **`/setwelcomechannel canal:#bienvenidas`**
  Define en qué canal de texto se publica la tarjeta de bienvenida.

- **`/setwelcomemessage mensaje:"..."`**
  Define el texto que aparece dentro de la tarjeta de bienvenida. Admite
  variables (ver sección 7).

- **`/disablewelcome`**
  Desactiva el envío de tarjetas de bienvenida (sin borrar el mensaje ni el
  canal guardados, por si quieres reactivarlo más tarde con
  `/setwelcomechannel`).

- **`/showwelcomeconfig`**
  Muestra el canal, mensaje, imagen y colores de bienvenida configurados.

- **`/setwelcomeimage url:...`**
  Define la imagen/logo del servidor que aparece en la tarjeta de
  bienvenida. Al guardarla, el bot extrae automáticamente 2 colores
  dominantes de esa imagen para las bandas decorativas.

- **`/setwelcomeaccentcolor color1:#5a1c22 color2:#5a1cab`**
  Fija a mano los 2 colores de las bandas, sobrescribiendo los extraídos
  automáticamente.

- **`/deletewelcomeaccentcolor`**
  Borra los colores manuales y vuelve a usar los extraídos automáticamente
  de la imagen (o el color neutro por defecto si no hay imagen configurada).

- **`/testwelcomebanner usuario:@alguien`**
  Genera una vista previa de la tarjeta de bienvenida con la configuración
  actual del servidor, visible solo para ti (no se publica en ningún
  canal) — útil para probar cambios sin esperar a que alguien se una de
  verdad.

- **`/setgoodbyechannel canal:#general`**
  Define en qué canal de texto se publica el mensaje de despedida.

- **`/setgoodbyemessage mensaje:"..."`**
  Define el texto plano que se envía cuando alguien se va o es baneado.
  Admite variables (ver sección 7).

- **`/disablegoodbye`**
  Desactiva el envío de mensajes de despedida.

- **`/showgoodbyeconfig`**
  Muestra el canal y el mensaje de despedida configurados actualmente.

- **`/setlogchannel canal:#logs`**
  Define en qué canal de texto se registran todos los eventos del servidor.

- **`/disablelogs`**
  Desactiva el registro de eventos.

- **`/showlogconfig`**
  Muestra el canal de logs configurado actualmente.

- **`/setstreamchannel canal:#🔴raseconline`**
  Define en qué canal de texto llegan los avisos del webhook (p.ej. "Rasec
  Online") que indican inicio/fin de transmisión.

- **`/setstreamuser usuario:@rasecfight`**
  Define a qué usuario vigilar el canal de voz para actualizar su estado.

- **`/disablestreamstatus`**
  Desactiva la detección de inicio/fin de transmisión.

- **`/showstreamstatusconfig`**
  Muestra el canal del webhook y el usuario vigilado configurados.

## 6. Moderación anti-spam

La detección usa **dos reglas independientes**; basta con que se cumpla
cualquiera de las dos para actuar:

**Regla 1 — Contenido repetido:** el mismo mensaje (tras normalizar) se
publica en al menos **3 canales distintos** en menos de **8 segundos**.

**Regla 2 — Ráfaga multicanal:** un mismo usuario escribe en al menos
**5 canales distintos** en menos de 8 segundos, **aunque el contenido varíe**
de un canal a otro. Esta regla existe porque las herramientas de raid suelen
variar el texto (enlaces con distinto parámetro de tracking, texto
aleatorio, menciones distintas) precisamente para evadir la Regla 1;
publicar en tantos canales tan rápido ya es, por sí solo, un patrón
imposible para un humano.

Ambos umbrales son ajustables al principio de `index.js`:

```javascript
const SPAM_WINDOW_MS = 8000;          // ventana de tiempo en milisegundos
const SPAM_MIN_CHANNELS = 3;          // Regla 1: canales con contenido idéntico
const SPAM_MIN_CHANNELS_BURST = 5;    // Regla 2: canales en ráfaga, cualquier contenido
```

**Normalización del contenido**, para que las variaciones típicas de evasión
no cuelen como "mensajes distintos":

- Los enlaces (`https://...`) se sustituyen por un marcador genérico, así
  que dos enlaces al mismo destino con distinto parámetro de tracking
  cuentan como el mismo mensaje.
- Las menciones de usuario (`<@id>`) se normalizan igual.
- Se eliminan caracteres invisibles (espacios de ancho cero y similares)
  usados a veces para "romper" la coincidencia exacta de texto.
- Los mensajes sin texto pero con adjuntos o embeds (raids de imágenes/GIFs)
  también se agrupan y cuentan para la Regla 1.

**Moderadores exentos:** cualquier miembro con permiso de **Administrador**
o **Gestionar mensajes** queda excluido de ambas reglas, para evitar falsos
positivos si un mod publica un aviso en varios canales seguidos.

**Qué ocurre al detectar spam (cualquiera de las dos reglas):**

1. Se borran automáticamente **todos** los mensajes de la ráfaga detectada
   (no solo los que coinciden en contenido), en todos los canales implicados.
2. **Primera vez** que le ocurre a ese usuario en el servidor:
   - Se le envía un **mensaje privado (MD)** avisando de que el spam no
     está permitido, que sus mensajes han sido borrados, y que la próxima
     vez será **baneado automáticamente**.
   - Queda registrado internamente (no se banea todavía).
3. **Siguiente vez** que reincide:
   - Se le envía un MD informando del baneo.
   - Se le **banea del servidor** con el motivo:
     `Expulsado por infringir varias veces la norma de que NO se permite spam`.

El historial de infracciones se guarda por servidor en `config.json`, así
que sobrevive a reinicios del bot. Un administrador puede reiniciarlo en
cualquier momento con `/clearinfractions`.

**Importante:** si el usuario tiene los mensajes directos cerrados, el aviso
o el mensaje de baneo simplemente no se entregan (se registra en los logs),
pero el borrado de mensajes y el baneo se ejecutan igualmente.

**Limitaciones conocidas:** esta detección es por usuario. Un ataque
coordinado con **varias cuentas distintas** publicando una sola vez cada una
(en vez de una cuenta publicando muchas veces) no se detecta con este
mecanismo, ya que cada cuenta por separado no supera los umbrales. Si sufres
ese tipo de ataque (típico de raids con cuentas nuevas/falsas), dímelo y
añado una regla adicional basada en antigüedad de cuenta y volumen de
mensajes nuevos en el servidor en conjunto.

## 7. Rol inicial automático

Con `/setinitialrol rol:@Miembro`, cada persona que se una al servidor a
partir de ese momento recibirá ese rol de forma automática e instantánea.

**Requisitos imprescindibles para que funcione:**

- El intent **SERVER MEMBERS INTENT** debe estar activado en el Developer
  Portal (paso 1 de esta guía).
- El bot necesita el permiso **Gestionar roles**.
- **El rol de Rasecbot debe estar por encima, en la jerarquía de roles del
  servidor** (Ajustes del servidor → Roles), del rol que quieres asignar.
  Discord no permite que un bot asigne un rol igual o superior al suyo
  propio, aunque tenga el permiso "Gestionar roles".

Si algo falla (rol borrado, jerarquía incorrecta, permisos insuficientes),
el bot no interrumpe la entrada del usuario: simplemente registra el error
en los logs (`journalctl -u rasecbot -f`) y continúa funcionando con
normalidad.

## 8. Tarjeta de bienvenida y mensaje de despedida

### Variables disponibles

Tanto `/setwelcomemessage` como `/setgoodbyemessage` admiten estas variables,
que se sustituyen automáticamente:

| Variable      | Se sustituye por                                    |
|---------------|------------------------------------------------------|
| `{usuario}`   | Nombre visible del usuario (apodo si tiene, si no su nombre de usuario) |
| `{servidor}`  | Nombre del servidor                                  |
| `{miembros}`  | Número total de miembros del servidor                |
| `{fecha}`     | Fecha actual (`dd/mm/aaaa`)                          |
| `{hora}`      | Hora actual (`hh:mm`)                                |

Ejemplo:

```
/setwelcomemessage mensaje:¡Bienvenido a {servidor}, {usuario}! Ya somos {miembros} miembros 🎉
/setgoodbyemessage mensaje:{usuario} nos dejó el {fecha} a las {hora}. ¡Hasta pronto!
```

### Saltos de línea

El campo de texto de un comando slash es de una sola línea (pulsar Enter
envía el comando), así que no se puede escribir un salto de línea real ahí.
Para forzar uno, escribe literalmente `\n` (barra invertida + n); el bot lo
convierte automáticamente en un salto de línea real en el **mensaje de
despedida** (que sí admite varias líneas, al ser un mensaje de texto
normal de Discord).

En el **mensaje de bienvenida** no aplica: la tarjeta muestra el texto en
una única línea fija (ver más abajo), así que cualquier `\n` que pongas ahí
simplemente se ignora a efectos visuales.

Si no defines ningún mensaje, se usan estos por defecto:

- Bienvenida: `Bienvenido {usuario} a`
- Despedida: `👋 {usuario} ha salido de {servidor}. ¡Hasta pronto!`

### Tarjeta de bienvenida

Al entrar un nuevo miembro (si has configurado `/setwelcomechannel`), el bot
genera una imagen con:

- El avatar del usuario (real, descargado de Discord), grande y centrado
  verticalmente a la izquierda
- Tu mensaje de bienvenida (con las variables sustituidas), en **una sola
  línea que puede ocupar casi todo el ancho de la tarjeta** — si aun así no
  cabe, se trunca con "…" (nunca se reduce el tamaño de letra)
- La imagen/logo que hayas configurado para el servidor con
  `/setwelcomeimage`, a la derecha, ajustada sin recortar nada (si la
  proporción no encaja exacta, se deja margen en vez de cortar la imagen)
- Cuatro elementos de color decorativos: el **fondo de toda la tarjeta**
  usa el color detectado en tu imagen del servidor (o el gris real de
  Discord, `#313338`, si no hay imagen o no tiene un fondo claro que
  detectar); dos bandas diagonales en la esquina inferior izquierda usan
  los 2 colores extraídos automáticamente o los que fijes con
  `/setwelcomeaccentcolor`; y una banda semi-transparente en la esquina
  superior derecha (mismo color que la banda más pequeña) se dibuja **por
  encima** del logo, solapándolo parcialmente sin ocultarlo del todo

La imagen se genera en el momento con `@napi-rs/canvas` y se envía como
archivo adjunto — no depende de ningún servicio externo.

#### Imagen del servidor y colores

- **`/setwelcomeimage url:...`** guarda la URL de la imagen/logo que
  aparecerá en la tarjeta, y en ese mismo momento el bot descarga la imagen
  y calcula sus 2 colores dominantes para las bandas decorativas (sin
  necesidad de ninguna librería externa: reduce la imagen y agrupa píxeles
  por similitud de color). Esos colores quedan guardados en `config.json`,
  así que no hace falta recalcularlos en cada bienvenida.
- Si prefieres elegir los colores tú mismo, usa
  **`/setwelcomeaccentcolor color1:#5a1c22 color2:#5a1cab`** — sobrescribe
  los automáticos. **`/deletewelcomeaccentcolor`** los borra y vuelve a los
  automáticos.
- Si no hay ninguna imagen configurada, la tarjeta muestra un hueco con el
  texto "Sin imagen configurada" en vez de fallar, y todo el fondo de la
  tarjeta usa el gris real de Discord (`#313338`).
- **Fondo de la tarjeta a juego con el logo**: cada vez que se genera la
  tarjeta, el bot analiza las 4 esquinas de la imagen configurada para
  detectar su color de fondo real, y lo usa como color de fondo de **toda
  la tarjeta** (no solo de una banda):
  - Si el PNG tiene **transparencia real** en las esquinas, usa el gris
    real de Discord (`#313338`).
  - Si el PNG tiene un **fondo sólido** (un color de relleno "cocido" en
    el propio archivo, en vez de transparencia), usa exactamente ese color.
  - Si las 4 esquinas no coinciden entre sí (p.ej. una foto sin fondo
    uniforme), también usa el gris de Discord, ya que no hay un único
    color de fondo claro que detectar.
  Esto hace que el fondo de la tarjeta se funda con el logo en vez de
  generar una costura de color contra él, sin tener que configurar nada
  a mano.
- **Banda superior derecha sobre el logo**: a diferencia de las otras dos
  bandas (que se dibujan antes que el logo, en la esquina opuesta), esta se
  dibuja **después**, semi-transparente (80% de opacidad), para que quede
  visiblemente superpuesta sobre la esquina del logo en vez de oculta
  detrás de él.
- **`/testwelcomebanner usuario:@alguien`** genera la tarjeta con la
  configuración actual y te la envía solo a ti (respuesta efímera), sin
  publicarla en ningún canal — la forma más rápida de comprobar cómo queda
  antes de que entre alguien de verdad.

### Mensaje de despedida

Es un mensaje de **texto plano** (sin tarjeta) que se envía tanto si el
usuario abandona el servidor voluntariamente como si es baneado —
incluyendo los baneos automáticos por spam descritos en la sección 6. El
bot detecta internamente ambos casos y evita enviar el mensaje duplicado
cuando alguien es baneado (evento que también dispara la salida del
servidor).

### Fuentes de texto en el NUC

`@napi-rs/canvas` usa las fuentes instaladas en el sistema (a través de
fontconfig). Ubuntu Server, al ser una instalación mínima, normalmente no
trae ninguna fuente. Instala estas antes de arrancar el bot:

```bash
sudo apt update
sudo apt install -y fonts-dejavu-core fonts-noto-color-emoji fontconfig
sudo fc-cache -f
```

Si el texto de la tarjeta se ve con una fuente "de sistema" fea o cuadros en
vez de letras, es señal de que falta `fonts-dejavu-core`.

**Emojis mostrados como un cuadrado (▯) en vez del icono real:** pasa por
dos motivos que hay que resolver juntos:

1. **Falta la fuente.** `DejaVu Sans` no tiene glifos de emoji. Instala
   `fonts-noto-color-emoji` (ver arriba) para que el sistema tenga de dónde
   sacar el icono.
2. **`@napi-rs/canvas` no hace fallback automático de fuente por carácter**,
   a diferencia de un navegador: si le pides que dibuje con `sans-serif` y
   el emoji no está en esa fuente, no lo busca en otra por su cuenta. Por
   eso el bot detecta los emojis dentro del texto y los dibuja
   explícitamente con la fuente `Noto Color Emoji` (ver `welcomeCard.js`,
   función `drawMixedLine`). Si esa fuente no está instalada en el sistema,
   el bot omite el emoji en vez de mostrar un cuadro, y avisa en los logs
   (`journalctl -u rasecbot -f`).

Tras instalar la fuente, refresca la caché (`sudo fc-cache -f`) y
**reinicia el bot** (`sudo systemctl restart rasecbot`) — el proceso
necesita arrancar de nuevo para detectar la fuente recién instalada.

## 9. Registro de eventos (logs)

Con `/setlogchannel canal:#logs`, el bot publica un embed en ese canal cada
vez que ocurre alguno de estos eventos en el servidor:

| Categoría | Eventos registrados |
|-----------|---------------------|
| Mensajes  | Eliminados, editados (muestra el texto antes/después) |
| Canales   | Creados, eliminados, renombrados o con el tema actualizado |
| Roles     | Creados, eliminados, renombrados |
| Miembros  | Entra, sale, baneado, desbaneado, cambio de apodo, cambio de roles |
| Voz       | Se conecta, se desconecta, cambia de canal de voz |
| Servidor  | Cambio de nombre, cambio de icono |

**Recomendación:** usa un canal dedicado y visible solo para
administradores/moderadores — con actividad de voz activa, el volumen de
mensajes puede ser considerable.

**Limitaciones a tener en cuenta:**

- **No indica quién realizó la acción** en eliminaciones de mensajes/canales
  ni en cambios de roles (por ejemplo, quién borró un mensaje ajeno o quién
  expulsó un canal). Para eso haría falta consultar el registro de
  auditoría de Discord (permiso "Ver registro de auditoría" + llamadas
  adicionales a la API), que no está implementado en esta versión. Si te
  hace falta esa información, dímelo y lo añado.
- Los mensajes eliminados o editados solo muestran el contenido "antes" si
  el bot ya tenía el mensaje en caché (normalmente los mensajes recientes sí
  lo están; mensajes muy antiguos o de antes de que el bot arrancara pueden
  aparecer como "contenido no disponible").
- No se registran cambios de permisos de canal (quién puede ver/escribir en
  cada canal), solo nombre y tema — hacerlo en detalle sería muy verboso.

## 10. Estado de streaming en el canal de voz

Con `/setstreamchannel` y `/setstreamuser` configurados, el bot vigila el
canal de texto donde llega tu webhook (p.ej. **Rasec Online**, en el canal
privado `🔴raseconline`) y reacciona a dos tipos de aviso:

| Mensaje del webhook contiene...   | Estado del canal de voz (si está conectado) |
|------------------------------------|----------------------------------------------|
| "está transmitiendo"               | **"🔴 ONLINE https://www.raseconline.es"**   |
| "ha dejado de transmitir"          | Se **borra** el estado (queda sin texto)     |

La detección de texto no distingue mayúsculas/tildes exactas (por ejemplo
"Está Transmitiendo" o "esta transmitiendo" también funcionan), así que no
hace falta que el texto del webhook sea exacto, solo que contenga esas
frases.

**Importante: la URL no sale como enlace clicable.** El estado de un canal
de voz es texto plano puro, igual que un apodo — no admite markdown, y
Discord no la auto-detecta como enlace ahí. Se verá literalmente el texto
`https://www.raseconline.es`, sin poder pulsarlo. No hay ninguna forma de
conseguir un enlace real y clicable en el estado de un canal de voz; si en
algún momento quieres un enlace pulsable de verdad, la única vía es un
mensaje en un canal de texto (Discord sí detecta URLs automáticamente ahí).

**Cómo funciona por dentro:** el "estado" de un canal de voz (esa etiqueta
que aparece bajo el nombre del canal, tipo "Jugando al ajedrez") es una
función relativamente nueva de Discord. discord.js todavía no trae un
método directo para cambiarla, así que el bot llama a la API REST de
Discord directamente (`PUT /channels/{id}/voice-status`).

**Requisitos:**

- El bot necesita el permiso **"Establecer estado del canal de voz"** en el
  servidor (o al menos en los canales de voz donde vaya a actuar).
- Si el usuario configurado (`/setstreamuser`) no está conectado a ningún
  canal de voz en el momento del aviso, el bot no hace nada (lo registra en
  los logs de la consola del proceso, no en el canal de logs de Discord).
- Solo se procesan mensajes que vengan realmente de un **webhook** (se
  descartan mensajes normales de usuarios o de otros bots), y únicamente en
  el canal configurado con `/setstreamchannel`.
- A diferencia de cambiar un apodo o un rol, esto **no está sujeto a la
  jerarquía de roles ni a la protección especial del owner del servidor**:
  es una propiedad del canal, no del usuario, así que funciona igual seas
  miembro normal, administrador, o el owner.

## 11. Notas técnicas

- La configuración completa (canales creadores, canales dinámicos activos,
  infracciones de spam) vive en `config.json`, organizada por `guildId`, y
  persiste entre reinicios.
- Los canales dinámicos heredan categoría, límite de usuarios, bitrate y
  permisos del canal creador correspondiente.
- La detección de spam se mantiene en memoria (no en disco) durante la
  ventana de tiempo configurada; solo el conteo de infracciones se persiste.
- Si cambias los comandos slash o añades servidores nuevos a `GUILD_IDS`,
  vuelve a ejecutar `npm run deploy`.
- Recuerda activar **MESSAGE CONTENT INTENT** en el Developer Portal o el
  bot no podrá leer el contenido de los mensajes y la moderación no funcionará.
- La generación de la tarjeta de bienvenida vive en `welcomeCard.js`, separado
  de `index.js`, por si en el futuro quieres cambiar el diseño (colores,
  disposición, textos) sin tocar la lógica del bot.
