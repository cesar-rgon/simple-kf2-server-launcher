![Logo](../images/kf2banner.png)

# Pre-requisitos
- Conexión a internet para descargar, actualizar y publicar un servidor de Killing Floor 2.
- Abrir los puertos necesarios en el router y firewall si quieres que el servidor sea visible en internet.

  | Puerto      | Por defecto | Protocolo | Qué controla esta opción                                                                 |
  |-------------|-------------|-----------|------------------------------------------------------------------------------------------|
  | Game Port   | 7777        | UDP       | Este es el puerto principal por donde envía conexiones el juego                          |
  | Query Port  | 27015       | UDP       | Este es el puerto usado para comunicarse con el Servidor Maestro de Steam                |
  | Web Admin   | 8080        | TCP       | Este puerto se usa para conectarse a la página WebAdmin del servidor (si fué habilitada) |
  | Steam Port  | 20560       | UDP       |                                                                                          |
  | NTP Port    | 123         | UDP       | Semanal solamente - Usado para determinar el evento semanal correctamente                |
  | Web Server  | 14320       | TCP       | Este puerto se usa para acceder a un servidor web embebido que aloja imágenes banner     |

- Instalar Oracle JDK 17 or más reciente (Java Development Kit) o bien OpenJDK para poder ejecutar el lanzador.
  - JDK puede ser descargado desde [aquí](https://www.oracle.com/java/technologies/downloads/).
  - OpenJDK puede ser descargado desde [aquí](https://openjdk.java.net/).

- Instalar Git en tu SO.
  - Windows: https://gitforwindows.org/
  - Linux: ```$ sudo apt install git```
---
Back to main page [here](../../LEEME.md).