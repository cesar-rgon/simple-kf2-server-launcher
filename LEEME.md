![Logo](src/main/resources/images/documentation/kf2banner.png)

_Documentation in english [here](README.md) (Documentación en inglés)_

---
( Elaborando el documento )

---
Aplicación para personalizar y lanzar fácilmente un servidor de Killing Floor 2 a través de una interfaz visual en lugar de editar ficheros batch o de configuración del servidor. Ha sido desarrollado en Java y la interfaz es compatible con los S.O. Windows y Linux.

*Ejemplo de configuración*

![Ejemplo de configuración](src/main/resources/images/documentation/screenshot00.png)

*Resultado después de unirse al servidor mediante el cliente de juego*

![In game](src/main/resources/images/documentation/screenshot-in-game.jpg)

### Índice
> 1. [Caracteristicas](#caracteristicas)
> 2. [Pre-requisitos](#pre-requisitos)
> 3. [Instalar y ejecutar el lanzador](#instalar-y-ejecutar-el-lanzador)
>   - [En Windows](#en-windows)
>   - [En Linux](#en-linux)
> 4. [Vista rapida](#vista-rapida)
>   - [Pagina principal](#pagina-principal)
>   - [Pagina WebAdmin](#pagina-webadmin)
>   - [Pagina instalar o actualizar](#pagina-instalar-o-actualizar)
>   - [Pagina mapas y mods](#pagina-mapas-y-mods)
>   - [Configuracion-Perfiles](#configuracion-perfiles)
>   - [Configuracion-Tipos de juegos](#configuracion-tipos-de-juegos)
>   - [Configuracion-Dificultades](#configuracion-dificultades)
>   - [Configuracion-Longitudes](#configuracion-longitudes)
>   - [Configuracion-Maximo jugadores](#configuracion-maximo-jugadores)
> 5. [Parametros por consola](#parametros-por-consola)
> 6. [Donacion](#donacion)

## Caracteristicas

```
Versión: 2.0 release candidate
S.O. soportados: Microsoft Windows y Linux (Ubuntu/Debian)
Autor: César Rodríguez González
Idiomas: Inglés, Español
```

- Instalar/actualizar el servidor a la última versión oficial o beta. 
- Lanzar el servidor con los parámetros especificados por el usuario en la página principal del lanzador.
- Unirse a la partida en curso del servidor.
- Abrir la página WebAdmin para administrar el servidor en ejecución.
- Añadir mapas personalizados y mods al servidor mediante el WorkShop de Steam.
- Eliminar mapas personalizados y mods del servidor.
- Administrar perfiles, tipos de juegos, dificultades, longitudes y máximo número de jugadores que pueden ser seleccionados para lanzar el servidor.
- Importar mapas oficiales, mapas personalizados y mods del servidor al lanzador.
- Importar perfiles de fichero al lanzador.
- Exportar perfiles del lanzador a fichero.
- Lanzar el servidor a través de consola sin necesidad de interación con la interfaz.
- Todas estas características están disponibles a través de la interfaz del lanzador en Windows y Linux.

## Pre-requisitos
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

## Instalar y ejecutar el lanzador
- Descargar la última versión del programa desde [aquí](https://github.com/cesar-rgon/simple-kf2-server-launcher/releases)
- Extraer el contenido del fichero zip a una carpeta local.

##### En Windows
- Ejecuta el fichero "SimpleKF2ServerLauncher.jar" haciendo doble click sobre el fichero, como método alternativo, escribe los siguientes comandos en una terminal.  

*Ejemplo*
```
C:\> cd SimpleKF2ServerLauncher-local-folder
C:\SimpleKF2ServerLauncher-local-folder> java -jar SimpleKF2ServerLauncher.jar
```
*NOTA: Reemplaza SimpleKF2ServerLauncher-local-folder por la carpeta local que quieras.*

##### En Linux
- Ejecuta el fichero "SimpleKF2ServerLauncher.jar" con los siguientes comandos en una terminal:

*Ejemplo*
```
$ cd SimpleKF2ServerLauncher-local-folder
$ java -jar SimpleKF2ServerLauncher.jar
```
*NOTA: Reemplaza SimpleKF2ServerLauncher-local-folder por la carpeta local que quieras.*

*NOTA: JRE debe ser la versión de Oracle (no OpenJDK disponible en los repositorios).*

## Vista rapida

#### Pagina principal
![Launcher screenshot](src/main/resources/images/documentation/screenshot01.png)


#### Pagina WebAdmin
![Launcher screenshot](src/main/resources/images/documentation/screenshot02.png)


#### Pagina instalar o actualizar
![Launcher screenshot](src/main/resources/images/documentation/screenshot03.png)

#### Pagina Mapas y Mods
![Launcher screenshot](src/main/resources/images/documentation/screenshot04.png)

![Launcher screenshot](src/main/resources/images/documentation/screenshot05.png)


#### Configuracion-Perfiles
![Launcher screenshot](src/main/resources/images/documentation/screenshot06.png)


#### Configuracion-Tipos de juegos
![Launcher screenshot](src/main/resources/images/documentation/screenshot07.png)


#### Configuracion-Dificultades
![Launcher screenshot](src/main/resources/images/documentation/screenshot08.png)


#### Configuracion-Longitudes
![Launcher screenshot](src/main/resources/images/documentation/screenshot09.png)


#### Configuracion-Maximo jugadores
![Launcher screenshot](src/main/resources/images/documentation/screenshot10.png)


## Parametros por consola


## Donacion
Si encontraste útil esta aplicación, te gustó y quieres contribuir, puedes donar la cantidad que estimes [aquí](https://www.paypal.me/cesarrgon).

![Paypal logo](src/main/resources/images/documentation/paypal-logo.png)

¡Muchas gracias!
