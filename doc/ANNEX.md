![Logo](images/kf2banner.png)

# Annex

## Execute a file before launch the server
Optionally, the launcher allows to execute a file after you press the "Run server" button.
To enable this functionality, you have to setup next parameters in properties/config.properties file 
```
prop.config.enableExecuteFileBeforeRunKF2Server=true
prop.config.fileToBeExecuted=C:\\path\\to\\my\\file\\example.exe
```

Furthermore, if you need to execute a file via console, for example batch or script files, then enable next parameter:
```
prop.config.executeFileViaConsole=true
```

## Console parameters
The launcher allows to execute servers through terminal parameters without user interaction with the interface.

Accepted parameters are: *For example*
```
java -jar SimpleKF2ServerLauncher.jar --pp platformName/profileName [platformName2/profileName2 ... ]
```
*NOTE: [ ] means that these parameters are optional*

- This command executes one or multiple servers, one per platformName and profileName.
- The plarform/profile names must be separated by whitespace.
- The platform names must exist in launcher's database. Valid values are: Steam and Epic.
- The profile names must exist in launcher's database (case sensitive).

---
Back to main page [here](../README.md).