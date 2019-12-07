![Logo](../images/kf2banner.png)

# Pre-requisitos
- Conexión a internet para descargar, actualizar y publicar un servidor de Killing Floor 2.
- Abrir los puertos necesarios en el router y firewall si quieres que el servidor sea visible en internet.

  | Puerto      | Por defecto | Protocolo  | Qué controla esta opción                                                                  |
  |-------------|-------------|------------|-------------------------------------------------------------------------------------------|
  | Game Port   | 7777        | UDP        | Este es el puerto principal por donde envía conexiones el juego                           |
  | Query Port  | 27015       | UDP        | Este es el puerto usado para comunicarse con el Servidor Maestro de Steam                 |
  | Web Admin   | 8080        | TCP        | Este puerto se usa para conectarse a la página WebAdmin del servidor (si fué habilitada)  |
  | Steam Port  | 20560       | UDP        |                                                                                           |
  | NTP Port    | 123         | UDP        | Semanal solamente - Usado para determinar el evento semanal correctamente                 |

- Instalar Oracle JRE 8 (Java Runtime Environment) para poder ejecutar el lanzador. JRE puede ser descargado de [aquí](https://www.java.com/es/download/).

---
Back to main page [here](../../LEEME.md).