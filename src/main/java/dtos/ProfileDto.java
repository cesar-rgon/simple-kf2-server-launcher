package dtos;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

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
    private final ObservableList<MapDto> mapList;
    private final Boolean takeover;
    private final Boolean cheatProtection;
    private final Boolean teamCollision;
    private final Boolean adminCanPause;
    private final Boolean announceAdminLogin;
    private final Boolean mapVoting;
    private final Integer mapVotingTime;
    private final Boolean kickVoting;
    private final Integer kickPercentage;
    private final Boolean publicTextChat;
    private final Boolean spectatorsOnlyChatToOtherSpectators;
    private final Boolean voip;
    private final Boolean chatLogging;
    private final String chatLoggingFile;


    public ProfileDto(String name, SelectDto language, GameTypeDto gametype, MapDto map, SelectDto difficulty, SelectDto length, SelectDto maxPlayers,
                      String serverName, String serverPassword, Boolean webPage, String webPassword, Integer webPort, Integer gamePort, Integer queryPort,
                      String yourClan, String yourWebLink, String urlImageServer, String welcomeMessage, String customParameters, ObservableList<MapDto> mapList,
                      Boolean takeover, Boolean cheatProtection, Boolean teamCollision, Boolean adminCanPause, Boolean announceAdminLogin, Boolean mapVoting,
                      Integer mapVotingTime, Boolean kickVoting, Integer kickPercentage, Boolean publicTextChat, Boolean spectatorsOnlyChatToOtherSpectators,
                      Boolean voip, Boolean chatLogging, String chatLoggingFile) {

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
        this.takeover = takeover;
        this.cheatProtection = cheatProtection;
        this.teamCollision = teamCollision;
        this.adminCanPause = adminCanPause;
        this.announceAdminLogin = announceAdminLogin;
        this.mapVoting = mapVoting;
        this.mapVotingTime = mapVotingTime;
        this.kickVoting = kickVoting;
        this.kickPercentage = kickPercentage;
        this.publicTextChat = publicTextChat;
        this.spectatorsOnlyChatToOtherSpectators = spectatorsOnlyChatToOtherSpectators;
        this.voip = voip;
        this.chatLogging = chatLogging;
        this.chatLoggingFile = chatLoggingFile;
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

    public ObservableList<MapDto> getMapList() {
        return mapList;
    }

    public Boolean getTakeover() {
        return takeover;
    }

    public Boolean getCheatProtection() {
        return cheatProtection;
    }

    public Boolean getTeamCollision() {
        return teamCollision;
    }

    public Boolean getAdminCanPause() {
        return adminCanPause;
    }

    public Boolean getAnnounceAdminLogin() {
        return announceAdminLogin;
    }

    public Boolean getMapVoting() {
        return mapVoting;
    }

    public Integer getMapVotingTime() {
        return mapVotingTime;
    }

    public Boolean getKickVoting() {
        return kickVoting;
    }

    public Integer getKickPercentage() {
        return kickPercentage;
    }

    public Boolean getPublicTextChat() {
        return publicTextChat;
    }

    public Boolean getSpectatorsOnlyChatToOtherSpectators() {
        return spectatorsOnlyChatToOtherSpectators;
    }

    public Boolean getVoip() {
        return voip;
    }

    public Boolean getChatLogging() {
        return chatLogging;
    }

    public String getChatLoggingFile() {
        return chatLoggingFile;
    }

    @Override
    public String toString() {
        return getName();
    }
}
