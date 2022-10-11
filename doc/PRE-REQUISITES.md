![Logo](images/kf2banner.png)

# Pre-requisites
- Internet connection to download, update and publish a Killing Floor 2 server.
- Open needed ports in your router and firewall if you want your server be visible on internet.

  | Port        | Default  | Protocol  | What this option controls                                                   |
  |-------------|----------|-----------|-----------------------------------------------------------------------------|
  | Game Port   | 7777     | UDP       | This is the main port the game will send connections over                   |
  | Query Port  | 27015    | UDP       | This port is used to communicate with the Steam Master Server               |
  | Web Admin   | 8080     | TCP       | This port is used to connect to your servers web admin page (if turned on)  |
  | Steam Port  | 20560    | UDP       |                                                                             |
  | NTP Port    | 123      | UDP       | Weekly Outbreak Only - Internet time lookup to determine correct Outbreak   |

- Install Oracle JDK 17 or a newer one (Java Development Kit) or OpenJDK in order to be able to execute the launcher.
  - JDK can be downloaded from [here](https://www.oracle.com/java/technologies/downloads/).
  - OpenJDK can be downloaded from [here](https://openjdk.java.net/).

---
Back to main page [here](../README.md).