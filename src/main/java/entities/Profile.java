package entities;

import javax.persistence.*;

@Entity
@Table(name = "PROFILES")
public class Profile extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="ID", updatable=false, nullable=false)
    private Integer id;

    @Column(name="NAME", length=50, unique=true, nullable=false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ID_LANGUAGE", referencedColumnName="ID", nullable=false)
    private Language language;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ID_GAMETYPE", referencedColumnName="ID", nullable=false)
    private GameType gametype;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ID_MAP", referencedColumnName="ID", nullable=false)
    private Map map;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ID_DIFFICULTY", referencedColumnName="ID", nullable=false)
    private Difficulty difficulty;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ID_LENGTH", referencedColumnName="ID", nullable=false)
    private Length length;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ID_MAXPLAYERS", referencedColumnName="ID", nullable=false)
    private MaxPlayers maxPlayers;

    @Column(name="SERVER_NAME", length=100, nullable=false)
    private String serverName;

    @Column(name="SERVER_PASSWORD", length=100)
    private String serverPassword;

    @Column(name="WEB_PAGE")
    private Boolean webPage;

    @Column(name="WEB_PASSWORD", length=100)
    private String webPassword;

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

    public Profile() {
        super();
    }

    public Profile(String name, Language language, GameType gametype, Map map, Difficulty difficulty, Length length, MaxPlayers maxPlayers,
                   String serverName, Integer webPort, Integer gamePort, Integer queryPort) {
        super();
        this.name = name;
        this.language = language;
        this.gametype = gametype;
        this.map = map;
        this.difficulty = difficulty;
        this.length = length;
        this.maxPlayers = maxPlayers;
        this.serverName = serverName;
        this.webPort = webPort;
        this.gamePort = gamePort;
        this.queryPort = queryPort;
    }

    public Profile(String name, Language language, GameType gametype, Map map, Difficulty difficulty, Length length, MaxPlayers maxPlayers,
                   String serverName, String serverPassword, Boolean webPage, String webPassword, Integer webPort, Integer gamePort, Integer queryPort,
                   String yourClan, String yourWebLink, String urlImageServer, String welcomeMessage, String customParameters) {
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
}
