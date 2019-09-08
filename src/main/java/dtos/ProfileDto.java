package dtos;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

public class ProfileDto {

    private final StringProperty name;
    private final SelectDto language;
    private final GameTypeDto gametype;
    private final MapDto map;
    private final SelectDto difficulty;
    private final SelectDto length;
    private final SelectDto maxPlayers;
    private final String serverName;
    private final String serverPassword;
    private final Boolean webPage;
    private final String webPassword;
    private final Integer webPort;
    private final Integer gamePort;
    private final Integer queryPort;
    private final String yourClan;
    private final String yourWebLink;
    private final String urlImageServer;
    private final String welcomeMessage;
    private final String customParameters;
    private final List<MapDto> mapList;

    public ProfileDto(String name, SelectDto language, GameTypeDto gametype, MapDto map, SelectDto difficulty, SelectDto length, SelectDto maxPlayers,
                   String serverName, String serverPassword, Boolean webPage, String webPassword, Integer webPort, Integer gamePort, Integer queryPort,
                   String yourClan, String yourWebLink, String urlImageServer, String welcomeMessage, String customParameters, List<MapDto> mapList) {
        super();
        this.name = new SimpleStringProperty(name);
        this.language = language;
        this.gametype = gametype;
        this.map = map;
        this.difficulty = difficulty;
        this.length = length;
        this.maxPlayers = maxPlayers;
        this.serverName = serverName;
        this.serverPassword = serverPassword;
        this.webPage = webPage;
        this.webPassword = webPassword;
        this.webPort = webPort;
        this.gamePort = gamePort;
        this.queryPort = queryPort;
        this.yourClan = yourClan;
        this.yourWebLink = yourWebLink;
        this.urlImageServer = urlImageServer;
        this.welcomeMessage = welcomeMessage;
        this.customParameters = customParameters;
        this.mapList = mapList;
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public SelectDto getLanguage() {
        return language;
    }

    public GameTypeDto getGametype() {
        return gametype;
    }

    public MapDto getMap() {
        return map;
    }

    public SelectDto getDifficulty() {
        return difficulty;
    }

    public SelectDto getLength() {
        return length;
    }

    public SelectDto getMaxPlayers() {
        return maxPlayers;
    }

    public String getServerName() {
        return serverName;
    }

    public String getServerPassword() {
        return serverPassword;
    }

    public Boolean getWebPage() {
        return webPage;
    }

    public String getWebPassword() {
        return webPassword;
    }

    public Integer getWebPort() {
        return webPort;
    }

    public Integer getGamePort() {
        return gamePort;
    }

    public Integer getQueryPort() {
        return queryPort;
    }

    public String getYourClan() {
        return yourClan;
    }

    public String getYourWebLink() {
        return yourWebLink;
    }

    public String getUrlImageServer() {
        return urlImageServer;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public String getCustomParameters() {
        return customParameters;
    }

    public List<MapDto> getMapList() {
        return mapList;
    }

    @Override
    public String toString() {
        return getName();
    }
}
