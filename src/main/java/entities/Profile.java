package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PROFILES")
public class Profile extends CommonEntity {

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
    private Map map;

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
    private List<Map> mapList;

    @Column(name="CHEAT_PROTECTION")
    private Boolean cheatProtection;

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


    public Profile() {
        super();
        this.mapList = new ArrayList<Map>();
    }

    public Profile(String name, Language language, GameType gametype, Map map, Difficulty difficulty, Length length, MaxPlayers maxPlayers,
                   String serverName, String serverPassword, Boolean webPage, String webPassword, Integer webPort, Integer gamePort, Integer queryPort,
                   String yourClan, String yourWebLink, String urlImageServer, String welcomeMessage, String customParameters, List<Map> mapList, Boolean takeover,
                   Boolean cheatProtection, Boolean teamCollision, Boolean adminCanPause, Boolean announceAdminLogin, Boolean mapVoting, Double mapVotingTime,
                   Boolean kickVoting, Double kickPercentage, Boolean publicTextChat, Boolean spectatorsOnlyChatToOtherSpectators, Boolean voip,
                   Boolean chatLogging, String chatLoggingFile) {

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

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
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

    public List<Map> getMapList() {
        return mapList;
    }

    public void setMapList(List<Map> mapList) {
        this.mapList = mapList;
    }

    public Boolean getTakeover() {
        return takeover;
    }

    public void setTakeover(Boolean takeover) {
        this.takeover = takeover;
    }

    public Boolean getCheatProtection() {
        return cheatProtection;
    }

    public void setCheatProtection(Boolean cheatProtection) {
        this.cheatProtection = cheatProtection;
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
}
