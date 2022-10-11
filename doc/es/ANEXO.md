![Logo](../images/kf2banner.png)

# Anexo

## Ejecutar un fichero antes de lanzar el servidor
Opcionalmente, el lanzador permite ejecutar un fichero después de pulsar el botón "Ejectar servidor".
Para habilitar esta funcionalidad, necesitas configurar los siguientes parámetros en el fichero properties/config.properties: 
```
prop.config.enableExecuteFileBeforeRunKF2Server=true
prop.config.fileToBeExecuted=C:\\ruta\\a\\mi\\fichero\\ejemplo.exe
```

Además, si necesitas ejecutar un fichero a través de la consola, por ejemplo ficheros batch o de scripts, entonces activa el siguiente parámetro:
```
prop.config.executeFileViaConsole=true
```

## Parametros por consola
El lanzador permite ejecutar servidores a través de parámetros de terminal sin interacción del usuario con la interfaz.

Los parámetros aceptados son: *Por ejemplo*:

```
java -jar SimpleKF2ServerLauncher.jar --pp platformName/profileName [platformName2/profileName2 ... ]
```
*NOTE: [ ] significa que estos parámetros son opcionales*

- Este comando ejecuta uno o varios servidores, uno por nombre de plataforma y perfil.
- Los nombres de plataforma/perfil deben estar separados por espacio en blanco.
- Los nombres de plataformas deben existir en la base de datos del lanzador. Los valores válidos son: Steam y Epic.
- Los nombres de perfiles deben existir en la base de datos del lanzador (sensible a mayúsculas).

---
Back to main page [here](../../LEEME.md).