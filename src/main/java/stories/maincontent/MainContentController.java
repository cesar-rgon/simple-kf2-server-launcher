package stories.maincontent;

import dtos.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.session.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainContentController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MainContentController.class);
    private final MainContentFacade facade;
    private final PropertyService propertyService;
    private String previousSelectedLanguageCode;
    private String installationFolder;

    @FXML private ComboBox<ProfileDto> profileSelect;
    @FXML private ComboBox<SelectDto> languageSelect;
    @FXML private ComboBox<GameTypeDto> gameTypeSelect;
    @FXML private ComboBox<AbstractMapDto> mapSelect;
    @FXML private ComboBox<SelectDto> difficultySelect;
    @FXML private ComboBox<SelectDto> lengthSelect;
    @FXML private ComboBox<SelectDto> maxPlayersSelect;
    @FXML private TextField serverName;
    @FXML private PasswordField serverPassword;
    @FXML private PasswordField webPassword;
    @FXML private TextField webPort;
    @FXML private TextField gamePort;
    @FXML private TextField queryPort;
    @FXML private TextField yourClan;
    @FXML private TextField yourWebLink;
    @FXML private TextField urlImageServer;
    @FXML private TextArea welcomeMessage;
    @FXML private TextArea customParameters;
    @FXML private CheckBox webPage;
    @FXML private WebView imageWebView;
    @FXML private Label profileLabel;
    @FXML private Label languageLabel;
    @FXML private Label gameTypeLabel;
    @FXML private Label mapLabel;
    @FXML private Label difficultyLabel;
    @FXML private Label lengthLabel;
    @FXML private Label maxPlayersLabel;
    @FXML private Label serverNameLabel;
    @FXML private Label serverPasswordLabel;
    @FXML private Label webPageLabel;
    @FXML private Label portsLabel;
    @FXML private Label clanLabel;
    @FXML private Label webLinkLabel;
    @FXML private Label urlImageLabel;
    @FXML private Label welcomeLabel;
    @FXML private Label customParametersLabel;
    @FXML private Button runServer;
    @FXML private Button joinServer;
    @FXML private ImageView profileImg;
    @FXML private ImageView languageImg;
    @FXML private ImageView gameTypeImg;
    @FXML private ImageView mapImg;
    @FXML private ImageView difficultyImg;
    @FXML private ImageView lengthImg;
    @FXML private ImageView maxPlayersImg;
    @FXML private ImageView serverNameImg;
    @FXML private ImageView serverPasswordImg;
    @FXML private ImageView webPageImg;
    @FXML private ImageView takeoverImg;
    @FXML private Label takeoverLabel;
    @FXML private CheckBox takeover;
    @FXML private ImageView portsImg;
    @FXML private ImageView clanImg;
    @FXML private ImageView webLinkImg;
    @FXML private ImageView customParametersImg;
    @FXML private ImageView thumbnailImg;
    @FXML private ImageView urlImageServerImg;
    @FXML private ImageView welcomeImg;
    @FXML private Tab basicParameters;
    @FXML private Tab advancedParameters;

    @FXML private ImageView teamCollisionImg;
    @FXML private Label teamCollisionLabel;
    @FXML private CheckBox teamCollision;

    @FXML private ImageView adminPauseImg;
    @FXML private Label adminPauseLabel;
    @FXML private CheckBox adminPause;

    @FXML private ImageView adminLoginImg;
    @FXML private Label adminLoginLabel;
    @FXML private CheckBox adminLogin;

    @FXML private ImageView mapVotingImg;
    @FXML private Label mapVotingLabel;
    @FXML private CheckBox mapVoting;

    @FXML private ImageView mapVotingTimeImg;
    @FXML private Label mapVotingTimeLabel;
    @FXML private TextField mapVotingTime;

    @FXML private ImageView kickVotingImg;
    @FXML private Label kickVotingLabel;
    @FXML private CheckBox kickVoting;

    @FXML private ImageView kickPercentageImg;
    @FXML private Label kickPercentageLabel;
    @FXML private TextField kickPercentage;

    @FXML private ImageView publicTextChatImg;
    @FXML private Label publicTextChatLabel;
    @FXML private CheckBox publicTextChat;

    @FXML private ImageView spectatorsChatImg;
    @FXML private Label spectatorsChatLabel;
    @FXML private CheckBox spectatorsChat;

    @FXML private ImageView voipImg;
    @FXML private Label voipLabel;
    @FXML private CheckBox voip;

    @FXML private ImageView chatLoggingImg;
    @FXML private Label chatLoggingLabel;
    @FXML private CheckBox chatLogging;

    @FXML private ImageView chatLoggingFileImg;
    @FXML private Label chatLoggingFileLabel;
    @FXML private TextField chatLoggingFile;
    @FXML private CheckBox chatLoggingFileTimestamp;

    @FXML private ImageView timeBetweenKicksImg;
    @FXML private Label timeBetweenKicksLabel;
    @FXML private TextField timeBetweenKicks;

    @FXML private ImageView maxIdleTimeImg;
    @FXML private Label maxIdleTimeLabel;
    @FXML private TextField maxIdleTime;

    @FXML private ImageView deadPlayersCanTalkImg;
    @FXML private Label deadPlayersCanTalkLabel;
    @FXML private CheckBox deadPlayersCanTalk;

    @FXML private ImageView readyUpDelayImg;
    @FXML private Label readyUpDelayLabel;
    @FXML private TextField readyUpDelay;

    @FXML private ImageView gameStartDelayImg;
    @FXML private Label gameStartDelayLabel;
    @FXML private TextField gameStartDelay;

    @FXML private ImageView maxSpectatorsImg;
    @FXML private Label maxSpectatorsLabel;
    @FXML private TextField maxSpectators;

    @FXML private ImageView mapObjetivesImg;
    @FXML private Label mapObjetivesLabel;
    @FXML private CheckBox mapObjetives;

    @FXML private ImageView pickupItemsImg;
    @FXML private Label pickupItemsLabel;
    @FXML private CheckBox pickupItems;

    @FXML private ImageView friendlyFirePercentageImg;
    @FXML private Label friendlyFirePercentageLabel;
    @FXML private TextField friendlyFirePercentage;


    public MainContentController() {
        super();
        facade = new MainContentFacadeImpl();
        propertyService = new PropertyServiceImpl();
    }


    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            installationFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.installationFolder");
            ObservableList<ProfileDto> profileOptions = facade.listAllProfiles();
            profileSelect.setItems(profileOptions);
            if (!profileOptions.isEmpty()) {
                profileSelect.setValue(Session.getInstance().getActualProfile() != null? Session.getInstance().getActualProfile(): facade.getLastSelectedProfile());
            } else {
                profileSelect.setValue(null);
                mapSelect.setItems(null);
            }
            Session.getInstance().setActualProfile(profileSelect.getValue());
            languageSelect.setItems(facade.listAllLanguages());
            gameTypeSelect.setItems(facade.listAllGameTypes());
            difficultySelect.setItems(facade.listAllDifficulties());
            lengthSelect.setItems(facade.listAllLengths());
            maxPlayersSelect.setItems(facade.listAllPlayers());

            if (profileSelect.getValue() != null) {
                profileOnAction();
            }

            loadLanguageTexts(languageSelect.getValue() != null? languageSelect.getValue().getKey(): "en");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }

        mapSelect.setCellFactory(new Callback<ListView<AbstractMapDto>, ListCell<AbstractMapDto>>() {
            @Override
            public ListCell<AbstractMapDto> call(ListView<AbstractMapDto> p) {
                ListCell<AbstractMapDto> listCell = new ListCell<AbstractMapDto>() {

                    private GridPane createMapGridPane(AbstractMapDto map) {
                        Label mapNameLabel = new Label(map.getKey());
                        mapNameLabel.setStyle("-fx-padding: 5;");
                        Label mapType;
                        String languageCode = languageSelect.getValue().getKey();
                        String officialText;
                        String customText;
                        try {
                            officialText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.official");
                            customText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.custom");
                        } catch (Exception e) {
                            officialText = "OFFICIAL";
                            customText = "CUSTOM";
                        }
                        if (map.isOfficial()) {
                            mapType = new Label(officialText);
                            mapType.setStyle("-fx-text-fill: plum; -fx-padding: 5;");
                        } else {
                            mapType = new Label(customText);
                            mapType.setStyle("-fx-text-fill: gold; -fx-padding: 5;");
                        }
                        Image image;
                        if (facade.isCorrectInstallationFolder(installationFolder) && StringUtils.isNotBlank(map.getUrlPhoto())) {
                            image = new Image("file:" + installationFolder + "/" + map.getUrlPhoto());
                        } else {
                            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/no-photo.png");
                            image = new Image(inputStream);
                        }
                        ImageView mapPreview = new ImageView(image);
                        mapPreview.setPreserveRatio(false);
                        mapPreview.setFitWidth(128);
                        mapPreview.setFitHeight(64);
                        GridPane gridpane = new GridPane();
                        gridpane.add(mapPreview, 1, 1);
                        gridpane.add(new Label(), 2, 1);
                        gridpane.add(mapNameLabel, 2, 2);
                        gridpane.add(mapType, 2, 3);
                        GridPane.setRowSpan(mapPreview, 3);
                        return gridpane;
                    }

                    @Override
                    protected void updateItem(AbstractMapDto map, boolean empty) {
                        super.updateItem(map, empty);
                        if (map != null) {
                            setGraphic(createMapGridPane(map));
                            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        }
                    }
                };
                return listCell;
            }
        });

        mapSelect.setButtonCell(new ListCell<AbstractMapDto>() {
            private GridPane createMapGridPane(AbstractMapDto map) {
                Label mapNameLabel = new Label(map.getKey());
                mapNameLabel.setStyle("-fx-font-weight: bold;");
                Image image;
                if (facade.isCorrectInstallationFolder(installationFolder) && StringUtils.isNotBlank(map.getUrlPhoto())) {
                    image = new Image("file:" + installationFolder + "/" + map.getUrlPhoto());
                } else {
                    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/no-photo.png");
                    image = new Image(inputStream);
                }
                ImageView mapPreview = new ImageView(image);
                mapPreview.setPreserveRatio(false);
                mapPreview.setFitWidth(256);
                mapPreview.setFitHeight(128);
                Label mapType;
                String languageCode = languageSelect.getValue().getKey();
                String officialText;
                String customText;
                try {
                    officialText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.official");
                    customText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.custom");
                } catch (Exception e) {
                    officialText = "OFFICIAL";
                    customText = "CUSTOM";
                }
                if (map.isOfficial()) {
                    mapType = new Label(officialText);
                    mapType.setStyle("-fx-text-fill: plum; -fx-padding: 3; -fx-border-color: plum; -fx-border-radius: 5;");
                } else {
                    mapType = new Label(customText);
                    mapType.setStyle("-fx-text-fill: gold; -fx-padding: 3; -fx-border-color: gold; -fx-border-radius: 5;");
                }
                GridPane gridpane = new GridPane();
                gridpane.add(mapPreview, 1, 1);
                gridpane.add(mapType, 1, 2);
                gridpane.add(mapNameLabel, 2, 2);
                GridPane.setColumnSpan(mapPreview, 2);
                GridPane.setHalignment(mapNameLabel, HPos.RIGHT);
                gridpane.setAlignment(Pos.CENTER);
                gridpane.setHgap(15);
                gridpane.setVgap(5);
                return gridpane;
            }

            @Override
            protected void updateItem(AbstractMapDto map, boolean empty) {
                super.updateItem(map, empty);
                if (map != null) {
                    setGraphic(createMapGridPane(map));
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                }
            }
        });

        serverName.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetServerName(profileName, serverName.getText())) {
                            logger.warn("The server name value could not be saved!: " + serverName.getText());
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.profileNotUpdated");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.serverNameNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                }
            }
        });

        serverPassword.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetServerPassword(profileName, serverPassword.getText())) {
                            logger.warn("The server password value could not be saved!: " + serverPassword.getText());
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.profileNotUpdated");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.serverPasswordNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                }
            }
        });

        webPassword.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetWebPassword(profileName, webPassword.getText())) {
                            logger.warn("The web password value could not be saved!: " + webPassword.getText());
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.profileNotUpdated");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.webPasswordNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                }
            }
        });

        webPort.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                Integer oldValue = null;
                try {
                    if (!newPropertyValue) {
                        oldValue = facade.findProfileDtoByName(profileSelect.getValue().getName()).getWebPort();
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetWebPort(profileName, StringUtils.isNotEmpty(webPort.getText()) ? Integer.parseInt(webPort.getText()): null)) {
                            String webPortValue = StringUtils.isNotEmpty(webPort.getText())? webPort.getText(): "";
                            logger.warn("The web port value could not be saved! " + webPortValue);
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.profileNotUpdated");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.webPortNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                    webPort.setText(oldValue != null? String.valueOf(oldValue): "");
                }
            }
        });

        gamePort.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                Integer oldValue = null;
                try {
                    if (!newPropertyValue) {
                        oldValue = facade.findProfileDtoByName(profileSelect.getValue().getName()).getGamePort();
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetGamePort(profileName, StringUtils.isNotEmpty(gamePort.getText()) ? Integer.parseInt(gamePort.getText()): null)) {
                            String gamePortValue = StringUtils.isNotEmpty(gamePort.getText())? gamePort.getText(): "";
                            logger.warn("The game port value could not be saved! " + gamePortValue);
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.profileNotUpdated");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.gamePortNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                    gamePort.setText(oldValue != null? String.valueOf(oldValue): "");
                }
            }
        });

        queryPort.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                Integer oldValue = null;
                try {
                    if (!newPropertyValue) {
                        oldValue = facade.findProfileDtoByName(profileSelect.getValue().getName()).getQueryPort();
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetQueryPort(profileName, StringUtils.isNotEmpty(queryPort.getText()) ? Integer.parseInt(queryPort.getText()): null)) {
                            String queryPortValue = StringUtils.isNotEmpty(queryPort.getText())? queryPort.getText(): "";
                            logger.warn("The query port value could not be saved! " + queryPortValue);
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.profileNotUpdated");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.queryPortNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                    queryPort.setText(oldValue != null? String.valueOf(oldValue): "");
                }
            }
        });

        yourClan.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetYourClan(profileName, yourClan.getText())) {
                            logger.warn("The clan value could not be saved!" + yourClan.getText());
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.profileNotUpdated");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.clanNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                }
            }
        });

        yourWebLink.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetYourWebLink(profileName, yourWebLink.getText())) {
                            logger.warn("The web link value could not be saved!: " + yourWebLink.getText());
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.profileNotUpdated");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.webLinkNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                }
            }
        });

        urlImageServer.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetUrlImageServer(profileName, urlImageServer.getText())) {
                            logger.warn("The image server link value could not be saved!" + urlImageServer.getText());
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.profileNotUpdated");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.imageServerLinkNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                        if (StringUtils.isNotEmpty(urlImageServer.getText())) {
                            imageWebView.getEngine().load(urlImageServer.getText());
                            imageWebView.setVisible(true);
                        } else {
                            imageWebView.setVisible(false);
                            imageWebView.getEngine().load(null);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                }
            }
        });

        welcomeMessage.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetWelcomeMessage(profileName, welcomeMessage.getText())) {
                            logger.warn("The welcome message value could not be saved!: " + welcomeMessage.getText());
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.profileNotUpdated");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.welcomeMessageNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                }
            }
        });

        customParameters.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetCustomParameters(profileName, customParameters.getText())) {
                            logger.warn("The custom parameters value could not be saved!: " + customParameters.getText());
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.profileNotUpdated");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.customParametersNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                }
            }
        });

        mapVotingTime.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                Double oldValue = null;
                try {
                    if (!newPropertyValue) {
                        oldValue = facade.findProfileDtoByName(profileSelect.getValue().getName()).getMapVotingTime();
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetMapVotingTime(profileName, StringUtils.isNotEmpty(mapVotingTime.getText()) ? Double.parseDouble(mapVotingTime.getText()): null)) {
                            String mapVotingTimeValue = StringUtils.isNotEmpty(mapVotingTime.getText())? mapVotingTime.getText(): "";
                            logger.warn("The map voting time value could not be saved! " + mapVotingTimeValue);
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.profileNotUpdated");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.mapVotingTimeNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                    mapVotingTime.setText(oldValue != null? String.valueOf(oldValue): "");
                }
            }
        });

        timeBetweenKicks.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                Double oldValue = null;
                try {
                    if (!newPropertyValue) {
                        oldValue = facade.findProfileDtoByName(profileSelect.getValue().getName()).getTimeBetweenKicks();
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetTimeBetweenKicks(profileName, StringUtils.isNotEmpty(timeBetweenKicks.getText()) ? Double.parseDouble(timeBetweenKicks.getText()): null)) {
                            String timeBetweenKicksValue = StringUtils.isNotEmpty(timeBetweenKicks.getText())? timeBetweenKicks.getText(): "";
                            logger.warn("The time between kick votes value could not be saved! " + timeBetweenKicksValue);
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.profileNotUpdated");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.timeBetweenKicksNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                    timeBetweenKicks.setText(oldValue != null? String.valueOf(oldValue): "");
                }
            }
        });

        kickPercentage.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                Double oldValue = null;
                try {
                    if (!newPropertyValue) {
                        oldValue = facade.findProfileDtoByName(profileSelect.getValue().getName()).getKickPercentage();
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetKickPercentage(profileName, StringUtils.isNotEmpty(kickPercentage.getText()) ? Double.parseDouble(kickPercentage.getText()): null)) {
                            String kickPercentageValue = StringUtils.isNotEmpty(kickPercentage.getText())? kickPercentage.getText(): "";
                            logger.warn("The kick percentage value could not be saved! " + kickPercentageValue);
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.profileNotUpdated");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.kickPercentageNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                    kickPercentage.setText(oldValue != null? String.valueOf(oldValue): "");
                }
            }
        });


        maxIdleTime.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                Double oldValue = null;
                try {
                    if (!newPropertyValue) {
                        oldValue = facade.findProfileDtoByName(profileSelect.getValue().getName()).getMaxIdleTime();
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetMaxIdleTime(profileName, StringUtils.isNotEmpty(maxIdleTime.getText()) ? Double.parseDouble(maxIdleTime.getText()): null)) {
                            String maxIdleTimeValue = StringUtils.isNotEmpty(maxIdleTime.getText())? maxIdleTime.getText(): "";
                            logger.warn("The maximum idle time value could not be saved! " + maxIdleTimeValue);
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.profileNotUpdated");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.maxIdleTimeNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                    maxIdleTime.setText(oldValue != null? String.valueOf(oldValue): "");
                }
            }
        });

        readyUpDelay.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                Integer oldValue = null;
                try {
                    if (!newPropertyValue) {
                        oldValue = facade.findProfileDtoByName(profileSelect.getValue().getName()).getReadyUpDelay();
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetReadyUpDelay(profileName, StringUtils.isNotEmpty(readyUpDelay.getText()) ? Integer.parseInt(readyUpDelay.getText()): null)) {
                            String readyUpDelayValue = StringUtils.isNotEmpty(readyUpDelay.getText())? readyUpDelay.getText(): "";
                            logger.warn("The ready up delay value could not be saved! " + readyUpDelayValue);
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.profileNotUpdated");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.readyUpDelayNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                    readyUpDelay.setText(oldValue != null? String.valueOf(oldValue): "");
                }
            }
        });

        chatLoggingFile.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetChatLoggingFile(profileName, chatLoggingFile.getText())) {
                            logger.warn("The chat logging file value could not be saved!: " + chatLoggingFile.getText());
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.profileNotUpdated");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.chatLoggingFileNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                }
            }
        });

        gameStartDelay.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                Integer oldValue = null;
                try {
                    if (!newPropertyValue) {
                        oldValue = facade.findProfileDtoByName(profileSelect.getValue().getName()).getGameStartDelay();
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetGameStartDelay(profileName, StringUtils.isNotEmpty(gameStartDelay.getText()) ? Integer.parseInt(gameStartDelay.getText()): null)) {
                            String gameStartDelayValue = StringUtils.isNotEmpty(gameStartDelay.getText())? gameStartDelay.getText(): "";
                            logger.warn("The game start delay value could not be saved! " + gameStartDelayValue);
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.profileNotUpdated");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.gameStartDelayNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                    gameStartDelay.setText(oldValue != null? String.valueOf(oldValue): "");
                }
            }
        });

        maxSpectators.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                Integer oldValue = null;
                try {
                    if (!newPropertyValue) {
                        oldValue = facade.findProfileDtoByName(profileSelect.getValue().getName()).getMaxSpectators();
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetMaxSpectators(profileName, StringUtils.isNotEmpty(maxSpectators.getText()) ? Integer.parseInt(maxSpectators.getText()): null)) {
                            String maxSpectatorsValue = StringUtils.isNotEmpty(maxSpectators.getText())? maxSpectators.getText(): "";
                            logger.warn("The maximum spectators value could not be saved! " + maxSpectatorsValue);
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.profileNotUpdated");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.maxSpectatorsNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                    maxSpectators.setText(oldValue != null? String.valueOf(oldValue): "");
                }
            }
        });

        friendlyFirePercentage.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                Double oldValue = null;
                try {
                    if (!newPropertyValue) {
                        oldValue = facade.findProfileDtoByName(profileSelect.getValue().getName()).getFriendlyFirePercentage();
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetFriendlyFirePercentage(profileName, StringUtils.isNotEmpty(friendlyFirePercentage.getText()) ? Double.parseDouble(friendlyFirePercentage.getText()): null)) {
                            String friendlyFirePercentageValue = StringUtils.isNotEmpty(friendlyFirePercentage.getText())? friendlyFirePercentage.getText(): "";
                            logger.warn("The friendly fire percentage value could not be saved! " + friendlyFirePercentageValue);
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.profileNotUpdated");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                    "prop.message.friendlyFirePercentageNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                    friendlyFirePercentage.setText(oldValue != null? String.valueOf(oldValue): "");
                }
            }
        });
    }

    private void loadTooltip(String languageCode, String propKey, ImageView img, Label label, ComboBox<?> combo) throws Exception {
        Tooltip tooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        Tooltip.install(img, tooltip);
        label.setTooltip(tooltip);
        combo.setTooltip(tooltip);
    }

    private void loadTooltip(String languageCode, String propKey, ImageView img, Label label, TextField textField) throws Exception {
        Tooltip tooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        Tooltip.install(img, tooltip);
        label.setTooltip(tooltip);
        textField.setTooltip(tooltip);
    }

    private void loadTooltip(String languageCode, String propKey, ImageView img, Label label, TextField[] textFieldArray) throws Exception {
        Tooltip tooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        Tooltip.install(img, tooltip);
        label.setTooltip(tooltip);
        for (int i=0; i<textFieldArray.length; i++) {
            textFieldArray[i].setTooltip(tooltip);
        }
    }

    private void loadTooltip(String languageCode, String propKey, ImageView img, Label label, CheckBox checkBox, TextField textField) throws Exception {
        Tooltip tooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        Tooltip.install(img, tooltip);
        label.setTooltip(tooltip);
        checkBox.setTooltip(tooltip);
        textField.setTooltip(tooltip);
    }

    private void loadTooltip(String languageCode, String propKey, ImageView img, Label label, CheckBox checkBox) throws Exception {
        Tooltip tooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        Tooltip.install(img, tooltip);
        label.setTooltip(tooltip);
        checkBox.setTooltip(tooltip);
    }

    private void loadTooltip(String languageCode, String propKey, ImageView img, Label label, TextArea textArea) throws Exception {
        Tooltip tooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        Tooltip.install(img, tooltip);
        label.setTooltip(tooltip);
        textArea.setTooltip(tooltip);
    }

    private void loadLanguageTexts(String languageCode) throws Exception {
        String profileLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.profile") + "*";
        profileLabel.setText(profileLabelText);
        loadTooltip(languageCode, "prop.tooltip.profile", profileImg, profileLabel, profileSelect);

        String basicParametersText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.basicParameters");
        basicParameters.setText(basicParametersText);

        String advancedParametersText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.advancedParameters");
        advancedParameters.setText(advancedParametersText);

        loadTooltip(languageCode, "prop.tooltip.language", languageImg, languageLabel, languageSelect);

        String gameTypeLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.gameType") + "*";
        gameTypeLabel.setText(gameTypeLabelText);
        loadTooltip(languageCode, "prop.tooltip.gameType", gameTypeImg, gameTypeLabel, gameTypeSelect);

        String mapLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.map") + "*";
        mapLabel.setText(mapLabelText);
        loadTooltip(languageCode, "prop.tooltip.map", mapImg, mapLabel, mapSelect);

        String difficultyLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.difficulty") + "*";
        difficultyLabel.setText(difficultyLabelText);
        loadTooltip(languageCode, "prop.tooltip.difficulty", difficultyImg, difficultyLabel, difficultySelect);

        String lengthLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.length") + "*";
        lengthLabel.setText(lengthLabelText);
        loadTooltip(languageCode, "prop.tooltip.length", lengthImg, lengthLabel, lengthSelect);

        String maxPlayersLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.maxPlayers") + "*";
        maxPlayersLabel.setText(maxPlayersLabelText);
        loadTooltip(languageCode, "prop.tooltip.maxPlayers", maxPlayersImg, maxPlayersLabel, maxPlayersSelect);

        String serverNameLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.serverName") + "*";
        serverNameLabel.setText(serverNameLabelText);
        loadTooltip(languageCode, "prop.tooltip.serverName", serverNameImg, serverNameLabel, serverName);

        String serverPasswordLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.serverPassword");
        serverPasswordLabel.setText(serverPasswordLabelText);
        loadTooltip(languageCode, "prop.tooltip.serverPassword", serverPasswordImg, serverPasswordLabel, serverPassword);

        String webPageLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.webPage");
        webPageLabel.setText(webPageLabelText);
        loadTooltip(languageCode, "prop.tooltip.webPage", webPageImg, webPageLabel, webPage, webPassword);

        String takeoverLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.takeover");
        takeoverLabel.setText(takeoverLabelText);
        String takeoverText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.takeoverEnable");
        takeover.setText(takeoverText);
        loadTooltip(languageCode, "prop.tooltip.takeover", takeoverImg, takeoverLabel, takeover);

        String clanLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.clan");
        clanLabel.setText(clanLabelText);
        loadTooltip(languageCode, "prop.tooltip.clan", clanImg, clanLabel, yourClan);

        String webLinkLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.webLink");
        webLinkLabel.setText(webLinkLabelText);
        loadTooltip(languageCode, "prop.tooltip.weblink", webLinkImg, webLinkLabel, yourWebLink);

        String urlImageLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.urlImage");
        urlImageLabel.setText(urlImageLabelText);
        loadTooltip(languageCode, "prop.tooltip.urlImage", urlImageServerImg, urlImageLabel, urlImageServer);

        String welcomeLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.welcome");
        welcomeLabel.setText(welcomeLabelText);
        loadTooltip(languageCode, "prop.tooltip.welcomeMessage", welcomeImg, welcomeLabel, welcomeMessage);

        String runServerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.runServer");
        runServer.setText(runServerText);
        runServer.setTooltip(new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.runServer")));

        String joinServerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.joinServer");
        joinServer.setText(joinServerText);
        joinServer.setTooltip(new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.joinServer")));

        Tooltip.install(thumbnailImg, new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.thumbnail")));

        // Advanced Parameters
        String portsLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.ports");
        portsLabel.setText(portsLabelText);
        TextField[] portsArray = {gamePort, queryPort, webPort};
        loadTooltip(languageCode, "prop.tooltip.ports", portsImg, portsLabel, portsArray);

        String customParametersLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.customParameters");
        customParametersLabel.setText(customParametersLabelText);
        loadTooltip(languageCode, "prop.tooltip.customParameters", customParametersImg, customParametersLabel, customParameters);

        String teamCollisionLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.teamCollision");
        String teamCollisionText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.teamCollisionEnable");
        teamCollisionLabel.setText(teamCollisionLabelText);
        teamCollision.setText(teamCollisionText);
        loadTooltip(languageCode, "prop.tooltip.teamCollision", teamCollisionImg, teamCollisionLabel, teamCollision);

        String adminPauseLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.adminPause");
        String adminPauseText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.adminPauseEnable");
        adminPauseLabel.setText(adminPauseLabelText);
        adminPause.setText(adminPauseText);
        loadTooltip(languageCode, "prop.tooltip.adminPause", adminPauseImg, adminPauseLabel, adminPause);

        String adminLoginLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.adminLogin");
        String adminLoginText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.adminLoginEnable");
        adminLoginLabel.setText(adminLoginLabelText);
        adminLogin.setText(adminLoginText);
        loadTooltip(languageCode, "prop.tooltip.adminLogin", adminLoginImg, adminLoginLabel, adminLogin);

        String mapVotingLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.mapVoting");
        String mapVotingText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.mapVotingEnable");
        mapVotingLabel.setText(mapVotingLabelText);
        mapVoting.setText(mapVotingText);
        loadTooltip(languageCode, "prop.tooltip.mapVoting", mapVotingImg, mapVotingLabel, mapVoting);

        String kickVotingLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.kickVoting");
        String kickVotingText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.kickVotingEnable");
        kickVotingLabel.setText(kickVotingLabelText);
        kickVoting.setText(kickVotingText);
        loadTooltip(languageCode, "prop.tooltip.kickVoting", kickVotingImg, kickVotingLabel, kickVoting);

        String publicTextChatLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.publicTextChat");
        String publicTextChatText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.publicTextChatEnable");
        publicTextChatLabel.setText(publicTextChatLabelText);
        publicTextChat.setText(publicTextChatText);
        loadTooltip(languageCode, "prop.tooltip.publicTextChat", publicTextChatImg, publicTextChatLabel, publicTextChat);

        String voipLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.voip");
        String voipText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.voipEnable");
        voipLabel.setText(voipLabelText);
        voip.setText(voipText);
        loadTooltip(languageCode, "prop.tooltip.voip", voipImg, voipLabel, voip);

        String chatLoggingLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.chatLogging");
        String chatLoggingText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.chatLoggingEnable");
        chatLoggingLabel.setText(chatLoggingLabelText);
        chatLogging.setText(chatLoggingText);
        loadTooltip(languageCode, "prop.tooltip.chatLogging", chatLoggingImg, chatLoggingLabel, chatLogging);

        String mapVotingTimeLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.mapVotingTime");
        mapVotingTimeLabel.setText(mapVotingTimeLabelText);
        loadTooltip(languageCode, "prop.tooltip.mapVotingTime", mapVotingTimeImg, mapVotingTimeLabel, mapVotingTime);

        String kickPercentageLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.kickPercentage");
        kickPercentageLabel.setText(kickPercentageLabelText);
        loadTooltip(languageCode, "prop.tooltip.kickPercentage", kickPercentageImg, kickPercentageLabel, kickPercentage);

        String spectatorsChatLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.spectatorsChat");
        String spectatorsChatText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.spectatorsChatEnable");
        spectatorsChatLabel.setText(spectatorsChatLabelText);
        spectatorsChat.setText(spectatorsChatText);
        loadTooltip(languageCode, "prop.tooltip.spectatorsChat", spectatorsChatImg, spectatorsChatLabel, spectatorsChat);

        String chatLoggingFileLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.chatLoggingFile");
        String chatLoggingFileTimestampText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.chatLoggingFileTimestamp");
        chatLoggingFileLabel.setText(chatLoggingFileLabelText);
        chatLoggingFileTimestamp.setText(chatLoggingFileTimestampText);
        loadTooltip(languageCode, "prop.tooltip.chatLoggingFile", chatLoggingFileImg, chatLoggingFileLabel, chatLoggingFileTimestamp, chatLoggingFile);

        String timeBetweenKicksLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.timeBetweenKicks");
        timeBetweenKicksLabel.setText(timeBetweenKicksLabelText);
        loadTooltip(languageCode, "prop.tooltip.timeBetweenKicks", timeBetweenKicksImg, timeBetweenKicksLabel, timeBetweenKicks);

        String maxIdleTimeLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.maxIdleTime");
        maxIdleTimeLabel.setText(maxIdleTimeLabelText);
        loadTooltip(languageCode, "prop.tooltip.maxIdleTime", maxIdleTimeImg, maxIdleTimeLabel, maxIdleTime);

        String deadPlayersCanTalkLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.deadPlayersCanTalk");
        String deadPlayersCanTalkText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.deadPlayersCanTalkEnable");
        deadPlayersCanTalkLabel.setText(deadPlayersCanTalkLabelText);
        deadPlayersCanTalk.setText(deadPlayersCanTalkText);
        loadTooltip(languageCode, "prop.tooltip.deadPlayersCanTalk", deadPlayersCanTalkImg, deadPlayersCanTalkLabel, deadPlayersCanTalk);

        String readyUpDelayLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.readyUpDelay");
        readyUpDelayLabel.setText(readyUpDelayLabelText);
        loadTooltip(languageCode, "prop.tooltip.readyUpDelay", readyUpDelayImg, readyUpDelayLabel, readyUpDelay);

        String gameStartDelayLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.gameStartDelay");
        gameStartDelayLabel.setText(gameStartDelayLabelText);
        loadTooltip(languageCode, "prop.tooltip.gameStartDelay", gameStartDelayImg, gameStartDelayLabel, gameStartDelay);

        String maxSpectatorsLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.maxSpectators");
        maxSpectatorsLabel.setText(maxSpectatorsLabelText);
        loadTooltip(languageCode, "prop.tooltip.maxSpectators", maxSpectatorsImg, maxSpectatorsLabel, maxSpectators);

        String mapObjetivesLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.mapObjetives");
        String mapObjetivesText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.mapObjetivesEnable");
        mapObjetivesLabel.setText(mapObjetivesLabelText);
        mapObjetives.setText(mapObjetivesText);
        loadTooltip(languageCode, "prop.tooltip.mapObjetives", mapObjetivesImg, mapObjetivesLabel, mapObjetives);

        String pickupItemsLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.pickupItems");
        String pickupItemsText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.pickupItemsEnable");
        pickupItemsLabel.setText(pickupItemsLabelText);
        pickupItems.setText(pickupItemsText);
        loadTooltip(languageCode, "prop.tooltip.pickupItems", pickupItemsImg, pickupItemsLabel, pickupItems);

        String friendlyFirePercentageLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.friendlyFirePercentage");
        friendlyFirePercentageLabel.setText(friendlyFirePercentageLabelText);
        loadTooltip(languageCode, "prop.tooltip.friendlyFirePercentage", friendlyFirePercentageImg, friendlyFirePercentageLabel, friendlyFirePercentage);

    }

    private void loadActualProfile(ProfileDto profile) {
        languageSelect.setValue(profile.getLanguage());
        previousSelectedLanguageCode = profile.getLanguage().getKey();
        gameTypeSelect.setValue(profile.getGametype());

        List<AbstractMapDto> officialMaps = profile.getMapList().stream()
                .filter(m -> m.isOfficial())
                .sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                .collect(Collectors.toList());

        List<AbstractMapDto> downloadedCustomMaps = profile.getMapList().stream()
                .filter(m -> !m.isOfficial())
                .filter(m -> ((CustomMapModDto) m).isDownloaded())
                .sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                .collect(Collectors.toList());

        List<AbstractMapDto> mapList = new ArrayList<AbstractMapDto>(officialMaps);
        mapList.addAll(downloadedCustomMaps);
        mapSelect.setItems(FXCollections.observableArrayList(mapList));
        if (profile.getMap() != null) {
            Optional<AbstractMapDto> selectedMapOpt = mapList.stream().filter(m -> m.getKey().equals(profile.getMap().getKey())).findFirst();
            if (selectedMapOpt.isPresent()) {
                mapSelect.getSelectionModel().select(mapList.indexOf(selectedMapOpt.get()));
            }
        }

        difficultySelect.setValue(profile.getDifficulty());
        difficultySelect.setDisable(gameTypeSelect.getValue() != null ? !gameTypeSelect.getValue().isDifficultyEnabled(): false);
        lengthSelect.setValue(profile.getLength());
        lengthSelect.setDisable(gameTypeSelect.getValue() != null ? !gameTypeSelect.getValue().isLengthEnabled(): false);
        maxPlayersSelect.setValue(profile.getMaxPlayers());

        serverName.setText(profile.getServerName());
        Integer webPortValue = profile.getWebPort();
        webPort.setText(webPortValue != null? String.valueOf(webPortValue): "");
        Integer gamePortValue = profile.getGamePort();
        gamePort.setText(gamePortValue != null? String.valueOf(gamePortValue): "");
        Integer queryPortValue = profile.getQueryPort();
        queryPort.setText(queryPortValue != null? String.valueOf(queryPortValue): "");
        yourClan.setText(profile.getYourClan());
        yourWebLink.setText(profile.getYourWebLink());
        urlImageServer.setText(profile.getUrlImageServer());
        welcomeMessage.setText(profile.getWelcomeMessage());
        customParameters.setText(profile.getCustomParameters());
        webPage.setSelected(profile.getWebPage() != null ? profile.getWebPage(): false);
        takeover.setSelected(profile.getTakeover() != null ? profile.getTakeover(): false);
        try {
            if (StringUtils.isNotEmpty(urlImageServer.getText())) {
                imageWebView.getEngine().load(urlImageServer.getText());
                imageWebView.setVisible(true);
            } else {
                imageWebView.setVisible(false);
                imageWebView.getEngine().load(null);
            }
            serverPassword.setText(Utils.decryptAES(profile.getServerPassword()));
            webPassword.setText(Utils.decryptAES(profile.getWebPassword()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
        teamCollision.setSelected(profile.getTeamCollision() != null ? profile.getTeamCollision(): false);
        adminPause.setSelected(profile.getAdminCanPause() != null ? profile.getAdminCanPause(): false);
        adminLogin.setSelected(profile.getAnnounceAdminLogin() != null ? profile.getAnnounceAdminLogin(): false);
        mapVoting.setSelected(profile.getMapVoting() != null ? profile.getMapVoting(): false);
        kickVoting.setSelected(profile.getKickVoting() != null ? profile.getKickVoting(): false);
        publicTextChat.setSelected(profile.getPublicTextChat() != null ? profile.getPublicTextChat(): false);
        spectatorsChat.setSelected(profile.getSpectatorsOnlyChatToOtherSpectators() != null ? profile.getSpectatorsOnlyChatToOtherSpectators(): false);
        voip.setSelected(profile.getVoip() != null ? profile.getVoip(): false);
        chatLogging.setSelected(profile.getChatLogging() != null ? profile.getChatLogging(): false);
        mapVotingTime.setText(profile.getMapVotingTime() != null? String.valueOf(profile.getMapVotingTime()): "");
        kickPercentage.setText(profile.getKickPercentage() != null? String.valueOf(profile.getKickPercentage()): "");
        chatLoggingFile.setText(profile.getChatLoggingFile());
        chatLoggingFileTimestamp.setSelected(profile.getChatLoggingFileTimestamp() != null ? profile.getChatLoggingFileTimestamp(): false);
        timeBetweenKicks.setText(profile.getTimeBetweenKicks() != null? String.valueOf(profile.getTimeBetweenKicks()): "");
        maxIdleTime.setText(profile.getMaxIdleTime() != null? String.valueOf(profile.getMaxIdleTime()): "");
        deadPlayersCanTalk.setSelected(profile.getDeadPlayersCanTalk() != null ? profile.getDeadPlayersCanTalk(): false);
        readyUpDelay.setText(profile.getReadyUpDelay() != null? String.valueOf(profile.getReadyUpDelay()): "");
        gameStartDelay.setText(profile.getGameStartDelay() != null? String.valueOf(profile.getGameStartDelay()): "");
        maxSpectators.setText(profile.getMaxSpectators() != null? String.valueOf(profile.getMaxSpectators()): "");
        mapObjetives.setSelected(profile.getMapObjetives() != null ? profile.getMapObjetives(): false);
        pickupItems.setSelected(profile.getPickupItems() != null ? profile.getPickupItems(): false);
        friendlyFirePercentage.setText(profile.getFriendlyFirePercentage() != null? String.valueOf(profile.getFriendlyFirePercentage()): "");
    }


    @FXML
    private void profileOnAction() {
        try {
            ProfileDto databaseProfile = facade.findProfileDtoByName(profileSelect.getValue().getName());
            loadActualProfile(databaseProfile);
            Session.getInstance().setActualProfile(profileSelect.getValue());
            propertyService.setProperty("properties/config.properties", "prop.config.lastSelectedProfile", profileSelect.getValue().getName());
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            if (!languageCode.equals(languageSelect.getValue().getKey())) {
                propertyService.setProperty("properties/config.properties", "prop.config.selectedLanguageCode", languageSelect.getValue().getKey());
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                        "prop.message.languageChanged");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                        "prop.message.applicationMustBeRestarted");
                Utils.infoDialog(headerText, contentText);
            }
        } catch (Exception e) {
            String headerText = "Error loading the profile information";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void gameTypeOnAction() {
        try {
            difficultySelect.setDisable(!gameTypeSelect.getValue().isDifficultyEnabled());
            lengthSelect.setDisable(!gameTypeSelect.getValue().isLengthEnabled());
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                String gameTypeCode = gameTypeSelect.getValue().getKey();
                if (!facade.updateProfileSetGameType(profileName, gameTypeCode)) {
                    logger.warn("The game type value could not be saved!: " + gameTypeCode);
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.gameTypeNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            String headerText = "The game type value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }


    @FXML
    private void mapOnAction() {
        try {
            if (profileSelect.getValue() != null && mapSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                String mapCode = mapSelect.getValue().getKey();
                if (!facade.updateProfileSetMap(profileName, mapCode)) {
                    logger.warn("The map value could not be saved!: " + mapCode);
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.mapNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
                ProfileDto databaseProfile = facade.findProfileDtoByName(profileSelect.getValue().getName());
                Session.getInstance().setActualProfile(databaseProfile);
            }
        } catch (Exception e) {
            String headerText = "The map value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void difficultyOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                String difficultyCode = difficultySelect.getValue().getKey();
                if (!facade.updateProfileSetDifficulty(profileName, difficultyCode)) {
                    logger.warn("The difficulty value could not be saved!: " + difficultyCode);
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.difficultyNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            String headerText = "The difficulty value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void lengthOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                String lengthCode = lengthSelect.getValue().getKey();
                if (!facade.updateProfileSetLength(profileName, lengthCode)) {
                    logger.warn("The length value could not be saved!: " + lengthCode);
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.lengthNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            String headerText = "The length value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void maxPlayersOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                String maxPlayersCode = maxPlayersSelect.getValue().getKey();
                if (!facade.updateProfileSetMaxPlayers(profileName, maxPlayersCode)) {
                    logger.warn("The max. players value could not be saved!: " + maxPlayersCode);
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.maxPlayersNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            String headerText = "The max. players value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void languageOnAction() {
        try {
            if (!languageSelect.getValue().getKey().equals(previousSelectedLanguageCode)) {
                if (profileSelect.getValue() != null) {
                    String profileName = profileSelect.getValue().getName();
                    String languageCode = languageSelect.getValue().getKey();
                    if (facade.updateProfileSetLanguage(profileName, languageCode)) {
                        propertyService.setProperty("properties/config.properties", "prop.config.selectedLanguageCode", languageCode);
                        String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                                "prop.message.languageChanged");
                        String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                                "prop.message.applicationMustBeRestarted");
                        Utils.infoDialog(headerText, contentText);
                    } else {
                        logger.warn("The language value could not be saved!: " + languageCode);
                        String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                "prop.message.profileNotUpdated");
                        String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                "prop.message.languageNotSaved");
                        Utils.warningDialog(headerText, contentText);
                    }
                }
                previousSelectedLanguageCode = languageSelect.getValue().getKey();
            }
        } catch (Exception e) {
            String headerText = "The language value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void webPageOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                if (!facade.updateProfileSetWebPage(profileName, webPage.isSelected())) {
                    logger.warn("The web page value could not be saved!: " + webPage.isSelected());
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.webPageNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            String headerText = "The web page value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void takeoverOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                if (!facade.updateProfileSetTakeover(profileName, takeover.isSelected())) {
                    logger.warn("The takeover value could not be saved!: " + takeover.isSelected());
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.takeoverNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            String headerText = "The takeover value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void runServerOnAction() {
        try {
            ObservableList<ProfileDto> allProfiles = facade.listAllProfiles();
            List<String> selectedProfileNameList = new ArrayList<String>();
            switch (allProfiles.size()) {
                case 0:
                    facade.runServer(null);
                    return;
                case 1:
                    selectedProfileNameList.add(allProfiles.get(0).getName());
                    profileSelect.setValue(allProfiles.get(0));
                    break;
                default:
                    String message = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.runServers");
                    selectedProfileNameList = facade.selectProfiles(message, profileSelect.getValue().getName());
            }

            facade.runExecutableFile();
            for (String profileName: selectedProfileNameList) {
                Session.getInstance().setConsole((StringUtils.isNotBlank(Session.getInstance().getConsole())? Session.getInstance().getConsole() + "\n\n" : "") +
                        "< " + new Date() + " - Run Server >\n" + facade.runServer(profileName));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void joinServerOnAction() {
        try {
            ObservableList<ProfileDto> allProfiles = facade.listAllProfiles();
            String selectedProfileName = null;
            switch (allProfiles.size()) {
                case 0:
                    facade.joinServer(null);
                    return;
                case 1:
                    selectedProfileName = allProfiles.get(0).getName();
                    profileSelect.setValue(allProfiles.get(0));
                    break;
                default:
                    String message = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.joinServer");
                    selectedProfileName = facade.selectProfile(message, profileSelect.getValue().getName());
            }
            if (StringUtils.isNotBlank(selectedProfileName)) {
                Session.getInstance().setConsole((StringUtils.isNotBlank(Session.getInstance().getConsole())? Session.getInstance().getConsole() + "\n\n": "") +
                        "< " + new Date() + " - Join Server >\n" + facade.joinServer(selectedProfileName));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void mapVotingOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                if (!facade.updateProfileSetMapVoting(profileName, mapVoting.isSelected())) {
                    logger.warn("The map voting value could not be saved!: " + mapVoting.isSelected());
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.mapVotingNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            String headerText = "The map voting value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void kickVotingOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                if (!facade.updateProfileSetKickVoting(profileName, kickVoting.isSelected())) {
                    logger.warn("The kick voting value could not be saved!: " + kickVoting.isSelected());
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.kickVotingNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            String headerText = "The kick voting value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void publicTextChatOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                if (!facade.updateProfileSetPublicTextChat(profileName, publicTextChat.isSelected())) {
                    logger.warn("The public text chat value could not be saved!: " + publicTextChat.isSelected());
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.publicTextChatNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            String headerText = "The public text chat value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void spectatorsChatOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                if (!facade.updateProfileSetSpectatorsChat(profileName, spectatorsChat.isSelected())) {
                    logger.warn("The spectators chat value could not be saved!: " + spectatorsChat.isSelected());
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.spectatorsChatNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            String headerText = "The spectators chat value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void voipOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                if (!facade.updateProfileSetVoip(profileName, voip.isSelected())) {
                    logger.warn("The VoIP value could not be saved!: " + voip.isSelected());
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.voipNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            String headerText = "The VoIP value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void teamCollisionOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                if (!facade.updateProfileSetTeamCollision(profileName, teamCollision.isSelected())) {
                    logger.warn("The team collision value could not be saved!: " + teamCollision.isSelected());
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.teamCollisionNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            String headerText = "The team collision value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void adminPauseOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                if (!facade.updateProfileSetAdminCanPause(profileName, adminPause.isSelected())) {
                    logger.warn("The admin can pause value could not be saved!: " + adminPause.isSelected());
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.adminCanPauseNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            String headerText = "The admin can pause value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void adminLoginOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                if (!facade.updateProfileSetAnnounceAdminLogin(profileName, adminLogin.isSelected())) {
                    logger.warn("The announce admin login value could not be saved!: " + adminLogin.isSelected());
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.adminLoginNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            String headerText = "The announce admin login value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void chatLoggingOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                if (!facade.updateProfileSetChatLogging(profileName, chatLogging.isSelected())) {
                    logger.warn("The chat logging value could not be saved!: " + chatLogging.isSelected());
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.chatLoggingNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            String headerText = "The chat logging value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void chatLoggingFileTimestampOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                if (!facade.updateProfileSetChatLoggingFileTimestamp(profileName, chatLoggingFileTimestamp.isSelected())) {
                    logger.warn("The chat logging file timestamp value could not be saved!: " + adminLogin.isSelected());
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.chatLoggingFileTimestampNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            String headerText = "The chat logging file timestamp value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void deadPlayersCanTalkOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                if (!facade.updateProfileSetDeadPlayersCanTalk(profileName, deadPlayersCanTalk.isSelected())) {
                    logger.warn("The dead players can talk value could not be saved!: " + deadPlayersCanTalk.isSelected());
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.deadPlayersCanTalkNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            String headerText = "The dead players can talk value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void mapObjetivesOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                if (!facade.updateProfileSetMapObjetives(profileName, mapObjetives.isSelected())) {
                    logger.warn("The map objetives value could not be saved!: " + mapObjetives.isSelected());
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.mapObjetivesNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            String headerText = "The map objetives value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void pickupItemsOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                if (!facade.updateProfileSetPickupItems(profileName, pickupItems.isSelected())) {
                    logger.warn("The pick up items value could not be saved!: " + pickupItems.isSelected());
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.pickupItemsNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            String headerText = "The pick up items value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }
}
