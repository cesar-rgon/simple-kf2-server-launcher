package stories.maincontent;

import dtos.GameTypeDto;
import dtos.MapDto;
import dtos.ProfileDto;
import dtos.SelectDto;
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
    @FXML private ComboBox<MapDto> mapSelect;
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

    @FXML private ImageView cheatProtectionImg;
    @FXML private Label cheatProtectionLabel;
    @FXML private CheckBox cheatProtection;

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
    @FXML private Button exploreLoggingFile;
    @FXML private TextField chatLoggingFile;


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

        mapSelect.setCellFactory(new Callback<ListView<MapDto>, ListCell<MapDto>>() {
            @Override
            public ListCell<MapDto> call(ListView<MapDto> p) {
                ListCell<MapDto> listCell = new ListCell<MapDto>() {

                    private GridPane createMapGridPane(MapDto map) {
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
                    protected void updateItem(MapDto map, boolean empty) {
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

        mapSelect.setButtonCell(new ListCell<MapDto>() {
            private GridPane createMapGridPane(MapDto map) {
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
            protected void updateItem(MapDto map, boolean empty) {
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
                try {
                    if (!newPropertyValue) {
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
                    Integer webPortValue = profileSelect.getValue().getWebPort();
                    webPort.setText(webPortValue != null? String.valueOf(webPortValue): "");
                }
            }
        });

        gamePort.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
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
                    Integer gamePortValue = profileSelect.getValue().getGamePort();
                    gamePort.setText(gamePortValue != null? String.valueOf(gamePortValue): "");
                }
            }
        });

        queryPort.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
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
                    Integer queryPortValue = profileSelect.getValue().getQueryPort();
                    queryPort.setText(queryPortValue != null? String.valueOf(queryPortValue): "");
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
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetMapVotingTime(profileName, StringUtils.isNotEmpty(mapVotingTime.getText()) ? Integer.parseInt(mapVotingTime.getText()): null)) {
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
                    Integer mapVotingTimeValue = profileSelect.getValue().getMapVotingTime();
                    mapVotingTime.setText(mapVotingTimeValue != null? String.valueOf(mapVotingTimeValue): "");
                }
            }
        });

        kickPercentage.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        if (!facade.updateProfileSetKickPercentage(profileName, StringUtils.isNotEmpty(kickPercentage.getText()) ? Integer.parseInt(kickPercentage.getText()): null)) {
                            String kickPercentageValue = StringUtils.isNotEmpty(kickPercentage.getText())? kickPercentage.getText(): "";
                            logger.warn("The map voting time value could not be saved! " + kickPercentageValue);
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
                    Integer kickPercentageValue = profileSelect.getValue().getKickPercentage();
                    kickPercentage.setText(kickPercentageValue != null? String.valueOf(kickPercentageValue): "");
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

    private void loadTooltip(String languageCode, String propKey, ImageView img, Label label, TextField textField, Button button) throws Exception {
        Tooltip tooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        Tooltip.install(img, tooltip);
        label.setTooltip(tooltip);
        textField.setTooltip(tooltip);
        button.setTooltip(tooltip);
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

        String cheatProtectionLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.cheatProtection");
        String cheatProtectionText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.cheatProtectionEnable");
        cheatProtectionLabel.setText(cheatProtectionLabelText);
        cheatProtection.setText(cheatProtectionText);
        loadTooltip(languageCode, "prop.tooltip.cheatProtection", cheatProtectionImg, cheatProtectionLabel, cheatProtection);

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
        String exploreLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.exploreFolder");
        chatLoggingFileLabel.setText(chatLoggingFileLabelText);
        exploreLoggingFile.setText(exploreLabelText);
        loadTooltip(languageCode, "prop.tooltip.chatLoggingFile", chatLoggingFileImg, chatLoggingFileLabel, chatLoggingFile, exploreLoggingFile);
    }

    private void loadActualProfile(ProfileDto profile) {
        languageSelect.setValue(profile.getLanguage());
        previousSelectedLanguageCode = profile.getLanguage().getKey();
        gameTypeSelect.setValue(profile.getGametype());

        List<MapDto> officialMaps = profile.getMapList().stream()
                .filter(m -> m.isOfficial())
                .sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                .collect(Collectors.toList());

        List<MapDto> downloadedCustomMaps = profile.getMapList().stream()
                .filter(m -> m.isDownloaded())
                .filter(m -> !m.isOfficial())
                .filter(m -> m.getMod() != null && !m.getMod())
                .sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                .collect(Collectors.toList());

        List<MapDto> mapList = new ArrayList<MapDto>(officialMaps);
        mapList.addAll(downloadedCustomMaps);
        mapSelect.setItems(FXCollections.observableArrayList(mapList));
        Optional<MapDto> selectedMapOpt = mapList.stream().filter(m -> m.getKey().equalsIgnoreCase(profile.getMap().getKey())).findFirst();
        if (selectedMapOpt.isPresent()) {
            mapSelect.getSelectionModel().select(mapList.indexOf(selectedMapOpt.get()));
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
        cheatProtection.setSelected(profile.getCheatProtection() != null ? profile.getCheatProtection(): false);
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
    }


    @FXML
    private void profileOnAction() {
        try {
            ProfileDto databaseProfile = facade.findProfileByName(profileSelect.getValue().getName());
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
                ProfileDto databaseProfile = facade.findProfileByName(profileSelect.getValue().getName());
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
    private void cheatProtectionOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                if (!facade.updateProfileSetCheatProtection(profileName, cheatProtection.isSelected())) {
                    logger.warn("The cheat protection value could not be saved!: " + cheatProtection.isSelected());
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.profileNotUpdated");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                            "prop.message.cheatProtectionNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            String headerText = "The cheat protection value could not be saved!";
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

}
