package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PROFILES")
public class Profile extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID", updatable=false, nullable=false)
    private Integer id;

    @Column(name="NAME", length=50, unique=true, nullable=false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ID_LANGUAGE", referencedColumnName="ID", nullable=false)
    private Language language;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ID_GAMETYPE", referencedColumnName="ID")
    private GameType gametype;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ID_MAP", referencedColumnName="ID")
    private AbstractMap map;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ID_DIFFICULTY", referencedColumnName="ID")
    private Difficulty difficulty;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ID_LENGTH", referencedColumnName="ID")
    private Length length;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ID_MAXPLAYERS", referencedColumnName="ID")
    private MaxPlayers maxPlayers;

    @Column(name="SERVER_NAME", length=100, nullable=false)
    private String serverName;

    @Column(name="SERVER_PASSWORD", length=100)
    private String serverPassword;

    @Column(name="WEB_PAGE")
    private Boolean webPage;

    @Column(name="WEB_PASSWORD", length=100)
    private String webPassword;

    @Column(name="TAKEOVER")
    private Boolean takeover;

    @Column(name="WEB_PORT")
    private Integer webPort;

    @Column(name="GAME_PORT")
    private Integer gamePort;

    @Column(name="QUERY_PORT")
    private Integer queryPort;

    @Column(name="YOUR_CLAN", length=100)
    private String yourClan;

    @Column(name="YOUR_WEB_LINK", length=255)
    private String yourWebLink;

    @Column(name="URL_IMAGE_SERVER", length=255)
    private String urlImageServer;

    @Column(name="WELCOME_MESSAGE", length=500)
    private String welcomeMessage;

    @Column(name="CUSTOM_PARAMETERS", length=500)
    private String customParameters;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "PROFILES_MAPS",
            joinColumns = {@JoinColumn(name = "ID_PROFILE")},
            inverseJoinColumns = {@JoinColumn(name = "ID_MAP")}
    )
    private List<AbstractMap> mapList;

    @Column(name="TEAM_COLLISION")
    private Boolean teamCollision;

    @Column(name="ADMIN_CAN_PAUSE")
    private Boolean adminCanPause;

    @Column(name="ANNOUNCE_ADMIN_LOGIN")
    private Boolean announceAdminLogin;

    @Column(name="MAP_VOTING")
    private Boolean mapVoting;

    @Column(name="MAP_VOTING_TIME")
    private Double mapVotingTime;

    @Column(name="KICK_VOTING")
    private Boolean kickVoting;

    @Column(name="KICK_PERCENTAGE")
    private Double kickPercentage;

    @Column(name="PUBLIC_CHAT")
    private Boolean publicTextChat;

    @Column(name="SPECTATORS_ONLY_CHAT_OTHER_SPECTATORS")
    private Boolean spectatorsOnlyChatToOtherSpectators;

    @Column(name="VOIP")
    private Boolean voip;

    @Column(name="CHAT_LOGGING")
    private Boolean chatLogging;

    @Column(name="CHAT_LOGGING_FILE", length = 255)
    private String chatLoggingFile;

    @Column(name="CHAT_LOGGING_FILE_TIMESTAMP")
    private Boolean chatLoggingFileTimestamp;

    @Column(name="TIME_BETWEEN_KICK_VOTES")
    private Double timeBetweenKicks;

    @Column(name="MAX_IDLE_TIME")
    private Double maxIdleTime;

    @Column(name="DEAD_PLAYERS_CAN_TALK")
    private Boolean deadPlayersCanTalk;

    @Column(name="READY_UP_DELAY")
    private Integer readyUpDelay;

    @Column(name="GAME_START_DELAY")
    private Integer gameStartDelay;

    @Column(name="MAX_SPECTATORS")
    private Integer maxSpectators;

    @Column(name="MAP_OBJETIVES")
    private Boolean mapObjetives;

    @Column(name="PICK_UP_ITEMS")
    private Boolean pickupItems;

    @Column(name="FRIENDLY_FIRE_PERCENTAGE")
    private Double friendlyFirePercentage;


    public Profile() {
        super();
        this.mapList = new ArrayList<AbstractMap>();
    }

    public Profile(String name, Language language, GameType gametype, AbstractMap map, Difficulty difficulty, Length length, MaxPlayers maxPlayers,
                   String serverName, String serverPassword, Boolean webPage, String webPassword, Integer webPort, Integer gamePort, Integer queryPort,
                   String yourClan, String yourWebLink, String urlImageServer, String welcomeMessage, String customParameters, List<AbstractMap> mapList, Boolean takeover,
                   Boolean teamCollision, Boolean adminCanPause, Boolean announceAdminLogin, Boolean mapVoting, Double mapVotingTime,
                   Boolean kickVoting, Double kickPercentage, Boolean publicTextChat, Boolean spectatorsOnlyChatToOtherSpectators, Boolean voip,
                   Boolean chatLogging, String chatLoggingFile, Boolean chatLoggingFileTimestamp, Double timeBetweenKicks, Double maxIdleTime, Boolean deadPlayersCanTalk,
                   Integer readyUpDelay, Integer gameStartDelay, Integer maxSpectators, Boolean mapObjetives, Boolean pickupItems, Double friendlyFirePercentage) {

        super();
        this.name = name;
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
        this.chatLoggingFileTimestamp = chatLoggingFileTimestamp;
        this.timeBetweenKicks = timeBetweenKicks;
        this.maxIdleTime = maxIdleTime;
        this.deadPlayersCanTalk = deadPlayersCanTalk;
        this.readyUpDelay = readyUpDelay;
        this.gameStartDelay = gameStartDelay;
        this.maxSpectators = maxSpectators;
        this.mapObjetives = mapObjetives;
        this.pickupItems = pickupItems;
        this.friendlyFirePercentage = friendlyFirePercentage;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getCode() {
        return name;
    }

    @Override
    public void setCode(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void setDescription(String description) {
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public GameType getGametype() {
        return gametype;
    }

    public void setGametype(GameType gametype) {
        this.gametype = gametype;
    }

    public AbstractMap getMap() {
        return map;
    }

    public void setMap(AbstractMap map) {
        this.map = map;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Length getLength() {
        return length;
    }

    public void setLength(Length length) {
        this.length = length;
    }

    public MaxPlayers getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(MaxPlayers maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerPassword() {
        return serverPassword;
    }

    public void setServerPassword(String serverPassword) {
        this.serverPassword = serverPassword;
    }

    public Boolean getWebPage() {
        return webPage;
    }

    public void setWebPage(Boolean webPage) {
        this.webPage = webPage;
    }

    public String getWebPassword() {
        return webPassword;
    }

    public void setWebPassword(String webPassword) {
        this.webPassword = webPassword;
    }

    public Integer getWebPort() {
        return webPort;
    }

    public void setWebPort(Integer webPort) {
        this.webPort = webPort;
    }

    public Integer getGamePort() {
        return gamePort;
    }

    public void setGamePort(Integer gamePort) {
        this.gamePort = gamePort;
    }

    public Integer getQueryPort() {
        return queryPort;
    }

    public void setQueryPort(Integer queryPort) {
        this.queryPort = queryPort;
    }

    public String getYourClan() {
        return yourClan;
    }

    public void setYourClan(String yourClan) {
        this.yourClan = yourClan;
    }

    public String getYourWebLink() {
        return yourWebLink;
    }

    public void setYourWebLink(String yourWebLink) {
        this.yourWebLink = yourWebLink;
    }

    public String getUrlImageServer() {
        return urlImageServer;
    }

    public void setUrlImageServer(String urlImageServer) {
        this.urlImageServer = urlImageServer;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getCustomParameters() {
        return customParameters;
    }

    public void setCustomParameters(String customParameters) {
        this.customParameters = customParameters;
    }

    public List<AbstractMap> getMapList() {
        return mapList;
    }

    public void setMapList(List<AbstractMap> mapList) {
        this.mapList = mapList;
    }

    public Boolean getTakeover() {
        return takeover;
    }

    public void setTakeover(Boolean takeover) {
        this.takeover = takeover;
    }

    public Boolean getTeamCollision() {
        return teamCollision;
    }

    public void setTeamCollision(Boolean teamCollision) {
        this.teamCollision = teamCollision;
    }

    public Boolean getAdminCanPause() {
        return adminCanPause;
    }

    public void setAdminCanPause(Boolean adminCanPause) {
        this.adminCanPause = adminCanPause;
    }

    public Boolean getAnnounceAdminLogin() {
        return announceAdminLogin;
    }

    public void setAnnounceAdminLogin(Boolean announceAdminLogin) {
        this.announceAdminLogin = announceAdminLogin;
    }

    public Boolean getMapVoting() {
        return mapVoting;
    }

    public void setMapVoting(Boolean mapVoting) {
        this.mapVoting = mapVoting;
    }

    public Boolean getKickVoting() {
        return kickVoting;
    }

    public void setKickVoting(Boolean kickVoting) {
        this.kickVoting = kickVoting;
    }

    public Boolean getPublicTextChat() {
        return publicTextChat;
    }

    public void setPublicTextChat(Boolean publicTextChat) {
        this.publicTextChat = publicTextChat;
    }

    public Boolean getSpectatorsOnlyChatToOtherSpectators() {
        return spectatorsOnlyChatToOtherSpectators;
    }

    public void setSpectatorsOnlyChatToOtherSpectators(Boolean spectatorsOnlyChatToOtherSpectators) {
        this.spectatorsOnlyChatToOtherSpectators = spectatorsOnlyChatToOtherSpectators;
    }

    public Boolean getVoip() {
        return voip;
    }

    public void setVoip(Boolean voip) {
        this.voip = voip;
    }

    public Boolean getChatLogging() {
        return chatLogging;
    }

    public void setChatLogging(Boolean chatLogging) {
        this.chatLogging = chatLogging;
    }

    public String getChatLoggingFile() {
        return chatLoggingFile;
    }

    public void setChatLoggingFile(String chatLoggingFile) {
        this.chatLoggingFile = chatLoggingFile;
    }

    public Double getMapVotingTime() {
        return mapVotingTime;
    }

    public void setMapVotingTime(Double mapVotingTime) {
        this.mapVotingTime = mapVotingTime;
    }

    public Double getKickPercentage() {
        return kickPercentage;
    }

    public void setKickPercentage(Double kickPercentage) {
        this.kickPercentage = kickPercentage;
    }

    public Boolean getChatLoggingFileTimestamp() {
        return chatLoggingFileTimestamp;
    }

    public void setChatLoggingFileTimestamp(Boolean chatLoggingFileTimestamp) {
        this.chatLoggingFileTimestamp = chatLoggingFileTimestamp;
    }

    public Double getTimeBetweenKicks() {
        return timeBetweenKicks;
    }

    public void setTimeBetweenKicks(Double timeBetweenKicks) {
        this.timeBetweenKicks = timeBetweenKicks;
    }

    public Double getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(Double maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public Boolean getDeadPlayersCanTalk() {
        return deadPlayersCanTalk;
    }

    public void setDeadPlayersCanTalk(Boolean deadPlayersCanTalk) {
        this.deadPlayersCanTalk = deadPlayersCanTalk;
    }

    public Integer getReadyUpDelay() {
        return readyUpDelay;
    }

    public void setReadyUpDelay(Integer readyUpDelay) {
        this.readyUpDelay = readyUpDelay;
    }

    public Integer getGameStartDelay() {
        return gameStartDelay;
    }

    public void setGameStartDelay(Integer gameStartDelay) {
        this.gameStartDelay = gameStartDelay;
    }

    public Integer getMaxSpectators() {
        return maxSpectators;
    }

    public void setMaxSpectators(Integer maxSpectators) {
        this.maxSpectators = maxSpectators;
    }

    public Boolean getMapObjetives() {
        return mapObjetives;
    }

    public void setMapObjetives(Boolean mapObjetives) {
        this.mapObjetives = mapObjetives;
    }

    public Boolean getPickupItems() {
        return pickupItems;
    }

    public void setPickupItems(Boolean pickupItems) {
        this.pickupItems = pickupItems;
    }

    public Double getFriendlyFirePercentage() {
        return friendlyFirePercentage;
    }

    public void setFriendlyFirePercentage(Double friendlyFirePercentage) {
        this.friendlyFirePercentage = friendlyFirePercentage;
    }
}
