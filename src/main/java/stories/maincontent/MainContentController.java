package stories.maincontent;

import dtos.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.enums.EnumPlatform;
import pojos.session.Session;
import stories.listvaluesmaincontent.ListValuesMainContentFacadeResult;
import stories.loadactualprofile.LoadActualProfileFacadeResult;
import utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainContentController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MainContentController.class);

    private MainContentManagerFacade facade;
    private ProfileDto actualProfile;
    private boolean userInteractLanguageSelect;

    @FXML private ComboBox<ProfileDto> profileSelect;
    @FXML private ComboBox<SelectDto> languageSelect;
    @FXML private ComboBox<GameTypeDto> gameTypeSelect;
    @FXML private ComboBox<PlatformProfileMapDto> platformProfileMapSelect;
    @FXML private ComboBox<SelectDto> difficultySelect;
    @FXML private ComboBox<SelectDto> lengthSelect;
    @FXML private ComboBox<SelectDto> maxPlayersSelect;
    @FXML private ComboBox<PlatformDto> platformSelect;
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
    @FXML private Label difficultyLabel;
    @FXML private Label lengthLabel;
    @FXML private Label maxPlayersLabel;
    @FXML private Label serverNameLabel;
    @FXML private Label serverPasswordLabel;
    @FXML private Label webPageLabel;
    @FXML private Label portsLabel;
    @FXML private Label clanLabel;
    @FXML private Label webLinkLabel;

    @FXML private Label welcomeLabel;
    @FXML private Label customParametersLabel;
    @FXML private Button runServer;
    @FXML private Button joinServer;
    @FXML private ImageView runServerImage;
    @FXML private ImageView joinServerImage;
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
    @FXML private ImageView noSelectedMapImage;

    @FXML private ImageView platformImg;
    @FXML private Label platformLabel;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private ImageView kf2logo;
    @FXML private ImageView kf2animated;

    public MainContentController() {
        facade = new MainContentManagerFacadeImpl();
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        progressIndicator.setVisible(true);
        userInteractLanguageSelect = true;

        Task<ListValuesMainContentFacadeResult> task = new Task<ListValuesMainContentFacadeResult>() {
            @Override
            protected ListValuesMainContentFacadeResult call() throws Exception {

                ListValuesMainContentFacadeResult result = facade.execute();

                actualProfile = profileSelect.getValue() != null ?
                        facade.findProfileDtoByName(
                                profileSelect.getValue().getName()
                        ):
                        Session.getInstance().getActualProfileName() != null ?
                                facade.findProfileDtoByName(
                                        Session.getInstance().getActualProfileName()
                                ):
                                result.getLastSelectedProfile();

                return result;
            }
        };

        task.setOnSucceeded(wse -> {
            try {
                platformSelect.setItems(task.getValue().getPlatformDtoList());
                languageSelect.setItems(task.getValue().getLanguageDtoList());
                if (actualProfile != null) {
                    languageSelect.setValue(actualProfile.getLanguage());
                }

                if (profileSelect.getValue() == null) {
                    File file = new File(System.getProperty("user.dir") + "/external-images/no-server-photo.png");
                    if (file.exists()) {
                        imageWebView.getEngine().load("file:" + System.getProperty("user.dir") + "/external-images/no-server-photo.png");
                    } else {
                        imageWebView.getEngine().load("file:" + getClass().getResource("/external-images/no-server-photo.png").getPath());
                    }
                }

                Session.getInstance().setActualProfileName(profileSelect.getValue() != null ? profileSelect.getValue().getName(): StringUtils.EMPTY);
                progressIndicator.setVisible(false);

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                Utils.errorDialog(e.getMessage(), e);
            }

        });

        task.setOnFailed(wse -> {
            progressIndicator.setVisible(false);
        });

        Thread thread = new Thread(task);
        thread.start();


        platformSelect.setCellFactory(new Callback<ListView<PlatformDto>, ListCell<PlatformDto>>() {
            @Override
            public ListCell<PlatformDto> call(ListView<PlatformDto> p) {
                ListCell<PlatformDto> listCell = new ListCell<PlatformDto>() {
                    private HBox createHBox(PlatformDto platform) {
                        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(platform.getLogoPath());
                        Image image = new Image(inputStream);

                        ImageView logo = new ImageView(image);

                        Label platformDescription = new Label(platform.getValue());
                        platformDescription.setStyle("-fx-padding: 15 0 0 10; -fx-text-fill: white;");

                        HBox contentPane = new HBox();
                        contentPane.getChildren().addAll(logo, platformDescription);

                        return contentPane;
                    }

                    @Override
                    protected void updateItem(PlatformDto platform, boolean empty) {
                        super.updateItem(platform, empty);
                        if (platform != null) {
                            setGraphic(createHBox(platform));
                            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        }
                    }
                };
                return listCell;
            }
        });

        platformSelect.setButtonCell(new ListCell<PlatformDto>() {
            private HBox createHBox(PlatformDto platform) {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(platform.getSmallLogoPath());
                Image image = new Image(inputStream);

                ImageView logo = new ImageView(image);

                Label platformDescription = new Label(platform.getValue());
                platformDescription.setStyle("-fx-padding: 6 0 0 10; -fx-text-fill: white;");

                HBox contentPane = new HBox();
                contentPane.getChildren().addAll(logo, platformDescription);

                return contentPane;
            }

            @Override
            protected void updateItem(PlatformDto platform, boolean empty) {
                super.updateItem(platform, empty);
                if (platform != null) {
                    setGraphic(createHBox(platform));
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                }
            }
        });

        platformProfileMapSelect.setCellFactory(new Callback<ListView<PlatformProfileMapDto>, ListCell<PlatformProfileMapDto>>() {
            @Override
            public ListCell<PlatformProfileMapDto> call(ListView<PlatformProfileMapDto> p) {
                ListCell<PlatformProfileMapDto> listCell = new ListCell<PlatformProfileMapDto>() {

                    private GridPane createMapGridPane(PlatformProfileMapDto platformProfileMapDto) {
                        Label aliasLabel = new Label(platformProfileMapDto.getAlias());
                        aliasLabel.setStyle("-fx-padding: 5;");
                        Label mapType;
                        String languageCode = languageSelect.getValue().getKey();
                        String officialText;
                        String customText;
                        try {
                            officialText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.official");
                            customText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.custom");
                        } catch (Exception e) {
                            officialText = "OFFICIAL";
                            customText = "CUSTOM";
                        }
                        if (platformProfileMapDto.getMapDto().isOfficial()) {
                            mapType = new Label(officialText);
                            mapType.setStyle("-fx-text-fill: plum; -fx-padding: 5;");
                        } else {
                            mapType = new Label(customText);
                            mapType.setStyle("-fx-text-fill: gold; -fx-padding: 5;");
                        }

                        ImageView mapPreview = new ImageView();
                        try {
                            Image image;
                            if (facade.isCorrectInstallationFolder(platformSelect.getValue().getKey()) &&
                                    StringUtils.isNotBlank(platformProfileMapDto.getUrlPhoto()) &&
                                    new File(platformSelect.getValue().getInstallationFolder() + "/" + platformProfileMapDto.getUrlPhoto()).exists()) {
                                image = new Image("file:" + platformSelect.getValue().getInstallationFolder() + "/" + platformProfileMapDto.getUrlPhoto());
                            } else {
                                File file = new File(System.getProperty("user.dir") + "/external-images/no-photo.png");
                                InputStream inputStream;
                                if (file.exists()) {
                                    inputStream = new FileInputStream(file.getAbsolutePath());
                                } else {
                                    inputStream = getClass().getClassLoader().getResourceAsStream("external-images/no-photo.png");
                                }
                                image = new Image(inputStream);
                            }

                            mapPreview = new ImageView(image);
                            mapPreview.setPreserveRatio(false);
                            mapPreview.setFitWidth(200);
                            mapPreview.setFitHeight(100);
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }

                        GridPane gridpane = new GridPane();
                        gridpane.add(mapPreview, 1, 1);
                        gridpane.add(new Label(), 2, 1);
                        gridpane.add(aliasLabel, 2, 2);
                        gridpane.add(mapType, 2, 3);
                        GridPane.setRowSpan(mapPreview, 3);
                        gridpane.setPadding(new Insets(5,0,5,0));
                        return gridpane;
                    }

                    @Override
                    protected void updateItem(PlatformProfileMapDto profileMapDto, boolean empty) {
                        super.updateItem(profileMapDto, empty);
                        if (profileMapDto != null) {
                            setGraphic(createMapGridPane(profileMapDto));
                            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        }
                    }
                };
                return listCell;
            }
        });

        platformProfileMapSelect.setButtonCell(new ListCell<PlatformProfileMapDto>() {
            private GridPane createMapGridPane(PlatformProfileMapDto platformProfileMapDto) {
                Label aliasLabel = new Label(platformProfileMapDto.getAlias());
                aliasLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                ImageView mapPreview = new ImageView();
                try {
                    Image image;
                    if (facade.isCorrectInstallationFolder(platformSelect.getValue().getKey()) &&
                            StringUtils.isNotBlank(platformProfileMapDto.getUrlPhoto()) &&
                            new File(platformSelect.getValue().getInstallationFolder() + "/" + platformProfileMapDto.getUrlPhoto()).exists()) {
                        image = new Image("file:" + platformSelect.getValue().getInstallationFolder() + "/" + platformProfileMapDto.getUrlPhoto());
                    } else {
                        File file = new File(System.getProperty("user.dir") + "/external-images/no-photo.png");
                        InputStream inputStream;
                        if (file.exists()) {
                            inputStream = new FileInputStream(file.getAbsolutePath());
                        } else {
                            inputStream = getClass().getClassLoader().getResourceAsStream("external-images/no-photo.png");
                        }
                        image = new Image(inputStream);
                    }
                    mapPreview = new ImageView(image);
                    mapPreview.setPreserveRatio(false);
                    mapPreview.setFitWidth(440);
                    mapPreview.setFitHeight(220);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }

                Label mapType;
                String languageCode = languageSelect.getValue().getKey();
                String officialText;
                String customText;
                try {
                    officialText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.official");
                    customText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.custom");
                } catch (Exception e) {
                    officialText = "OFFICIAL";
                    customText = "CUSTOM";
                }

                if (platformProfileMapDto.getMapDto().isOfficial()) {
                    mapType = new Label(officialText);
                    mapType.setStyle("-fx-text-fill: plum; -fx-padding: 5; -fx-border-color: plum; -fx-border-radius: 5;");
                } else {
                    mapType = new Label(customText);
                    mapType.setStyle("-fx-text-fill: gold; -fx-padding: 5; -fx-border-color: gold; -fx-border-radius: 5;");
                }

                ImageView darkPanel = new ImageView(new Image("images/darkPanel.png"));
                darkPanel.setPreserveRatio(false);
                darkPanel.setFitWidth(440);
                darkPanel.setFitHeight(40);
                darkPanel.setOpacity(0.7);

                GridPane gridpane = new GridPane();
                gridpane.add(mapPreview, 1, 1);
                GridPane.setHalignment(mapPreview, HPos.LEFT);
                GridPane.setMargin(mapPreview, new Insets(0,0,0,-5));
                gridpane.add(darkPanel, 1, 1);
                GridPane.setHalignment(darkPanel, HPos.LEFT);
                GridPane.setValignment(darkPanel, VPos.BOTTOM);
                GridPane.setMargin(darkPanel, new Insets(0,0,0,-5));
                gridpane.add(mapType, 1, 1);
                gridpane.add(aliasLabel, 1, 1);
                GridPane.setHalignment(mapType, HPos.LEFT);
                GridPane.setMargin(mapType, new Insets(0,0,6,5));
                GridPane.setValignment(mapType, VPos.BOTTOM);
                GridPane.setHalignment(aliasLabel, HPos.RIGHT);
                GridPane.setValignment(aliasLabel, VPos.BOTTOM);
                GridPane.setMargin(aliasLabel, new Insets(0,15,10,0));
                return gridpane;
            }

            @Override
            protected void updateItem(PlatformProfileMapDto platformProfileMapDto, boolean empty) {
                super.updateItem(platformProfileMapDto, empty);
                if (platformProfileMapDto != null) {
                    setGraphic(createMapGridPane(platformProfileMapDto));
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    noSelectedMapImage.setVisible(false);
                } else {
                    noSelectedMapImage.setVisible(true);
                }
            }
        });

        serverName.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                        facade.updateProfileSetServerName(profileName, serverName.getText());
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
                        facade.updateProfileSetServerPassword(profileName, serverPassword.getText());
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
                        facade.updateProfileSetWebPassword(profileName, webPassword.getText());
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
                        facade.updateProfileSetWebPort(profileName, StringUtils.isNotEmpty(webPort.getText()) ? Integer.parseInt(webPort.getText()): null);
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
                        facade.updateProfileSetGamePort(profileName, StringUtils.isNotEmpty(gamePort.getText()) ? Integer.parseInt(gamePort.getText()): null);
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
                        facade.updateProfileSetQueryPort(profileName, StringUtils.isNotEmpty(queryPort.getText()) ? Integer.parseInt(queryPort.getText()): null);
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
                        facade.updateProfileSetYourClan(profileName, yourClan.getText());
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
                        facade.updateProfileSetYourWebLink(profileName, yourWebLink.getText());
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                }
            }
        });

        kf2logo.setOnMouseClicked(new EventHandler<MouseEvent>() {
              @Override
              public void handle(MouseEvent mouseEvent) {
                  kf2animated.setVisible(true);
              }
        });

        kf2animated.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                kf2animated.setVisible(false);
            }
        });

        runServer.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                runServerImage.setStyle("-fx-effect: dropshadow(three-pass-box, #c15d11, 20, 0, 0, 0);");
                kf2animated.setVisible(true);
            }
        });
        runServer.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                runServerImage.setStyle("-fx-effect: none;");
            }
        });

        joinServer.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                joinServerImage.setStyle("-fx-effect: dropshadow(three-pass-box, #c15d11, 20, 0, 0, 0);");
                kf2animated.setVisible(true);
            }
        });
        joinServer.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                joinServerImage.setStyle("-fx-effect: none;");
            }
        });

        urlImageServer.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                imageWebView.setStyle("-fx-effect: dropshadow(three-pass-box, #c15d11, 60, 0, 0, 0);");
            }
        });
        urlImageServer.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                imageWebView.setStyle("-fx-effect: none;");
            }
        });

        urlImageServer.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): StringUtils.EMPTY;
                        facade.updateProfileSetUrlImageServer(profileName, urlImageServer.getText());
                        if (StringUtils.isNotEmpty(urlImageServer.getText())) {
                            imageWebView.getEngine().load(urlImageServer.getText());
                        } else {
                            File file = new File(System.getProperty("user.dir") + "/external-images/no-server-photo.png");
                            if (file.exists()) {
                                imageWebView.getEngine().load("file:" + System.getProperty("user.dir") + "/external-images/no-server-photo.png");
                            } else {
                                imageWebView.getEngine().load("file:" + getClass().getResource("/external-images/no-server-photo.png").getPath());
                            }
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
                        facade.updateProfileSetWelcomeMessage(profileName, welcomeMessage.getText());
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
                        facade.updateProfileSetCustomParameters(profileName, customParameters.getText());
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
                        facade.updateProfileSetMapVotingTime(profileName, StringUtils.isNotEmpty(mapVotingTime.getText()) ? Double.parseDouble(mapVotingTime.getText()): null);
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
                        facade.updateProfileSetTimeBetweenKicks(profileName, StringUtils.isNotEmpty(timeBetweenKicks.getText()) ? Double.parseDouble(timeBetweenKicks.getText()): null);
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
                        facade.updateProfileSetKickPercentage(profileName, StringUtils.isNotEmpty(kickPercentage.getText()) ? Double.parseDouble(kickPercentage.getText()): null);
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
                        facade.updateProfileSetMaxIdleTime(profileName, StringUtils.isNotEmpty(maxIdleTime.getText()) ? Double.parseDouble(maxIdleTime.getText()): null);
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
                        facade.updateProfileSetReadyUpDelay(profileName, StringUtils.isNotEmpty(readyUpDelay.getText()) ? Integer.parseInt(readyUpDelay.getText()): null);
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
                        facade.updateProfileSetChatLoggingFile(profileName, chatLoggingFile.getText());
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
                        facade.updateProfileSetGameStartDelay(profileName, StringUtils.isNotEmpty(gameStartDelay.getText()) ? Integer.parseInt(gameStartDelay.getText()): null);
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
                        facade.updateProfileSetMaxSpectators(profileName, StringUtils.isNotEmpty(maxSpectators.getText()) ? Integer.parseInt(maxSpectators.getText()): null);
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
                        facade.updateProfileSetFriendlyFirePercentage(profileName, StringUtils.isNotEmpty(friendlyFirePercentage.getText()) ? Double.parseDouble(friendlyFirePercentage.getText()): null);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                    friendlyFirePercentage.setText(oldValue != null? String.valueOf(oldValue): "");
                }
            }
        });
    }

    private void loadPlatformProfileMapSelect() throws Exception {
        LoadActualProfileFacadeResult result = facade.loadActualProfile(
                platformSelect.getValue().getKey(),
                profileSelect.getValue().getName()
        );
        platformProfileMapSelect.getItems().clear();
        platformProfileMapSelect.setItems(result.getFilteredMapDtoList());
    }

    private void loadLanguageTexts(String languageCode) throws Exception {

        String platformText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.platform");
        platformLabel.setText(platformText);
        Utils.loadTooltip(languageCode, "prop.tooltip.platform", platformImg, platformLabel, platformSelect);

        String profileLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.profile") + "*";
        profileLabel.setText(profileLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.profile", profileImg, profileLabel, profileSelect);

        String basicParametersText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.basicParameters");
        basicParameters.setText(basicParametersText);

        String advancedParametersText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.advancedParameters");
        advancedParameters.setText(advancedParametersText);

        Utils.loadTooltip(languageCode, "prop.tooltip.language", languageImg, languageLabel, languageSelect);

        String gameTypeLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.gameType") + "*";
        gameTypeLabel.setText(gameTypeLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.gameType", gameTypeImg, gameTypeLabel, gameTypeSelect);

        Utils.loadTooltip(languageCode, "prop.tooltip.map", mapImg, null, platformProfileMapSelect);

        String difficultyLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.difficulty") + "*";
        difficultyLabel.setText(difficultyLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.difficulty", difficultyImg, difficultyLabel, difficultySelect);

        String lengthLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.length") + "*";
        lengthLabel.setText(lengthLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.length", lengthImg, lengthLabel, lengthSelect);

        String maxPlayersLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.maxPlayers") + "*";
        maxPlayersLabel.setText(maxPlayersLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.maxPlayers", maxPlayersImg, maxPlayersLabel, maxPlayersSelect);

        String serverNameLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.serverName") + "*";
        serverNameLabel.setText(serverNameLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.serverName", serverNameImg, serverNameLabel, serverName);

        String serverPasswordLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.serverPassword");
        serverPasswordLabel.setText(serverPasswordLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.serverPassword", serverPasswordImg, serverPasswordLabel, serverPassword);

        String webPageLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.webPage");
        webPageLabel.setText(webPageLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.webPage", webPageImg, webPageLabel, webPage, webPassword);

        String takeoverLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.takeover");
        takeoverLabel.setText(takeoverLabelText);
        String takeoverText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.takeoverEnable");
        takeover.setText(takeoverText);
        Utils.loadTooltip(languageCode, "prop.tooltip.takeover", takeoverImg, takeoverLabel, takeover);

        String clanLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.clan");
        clanLabel.setText(clanLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.clan", clanImg, clanLabel, yourClan);

        String webLinkLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.webLink");
        webLinkLabel.setText(webLinkLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.weblink", webLinkImg, webLinkLabel, yourWebLink);

        String welcomeLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.welcome");
        welcomeLabel.setText(welcomeLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.welcomeMessage", welcomeImg, welcomeLabel, welcomeMessage);

        Double tooltipDuration = Double.parseDouble(
                facade.findPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
        );

        Tooltip urlImageServerTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.urlImage"));
        urlImageServerTooltip.setShowDuration(Duration.seconds(tooltipDuration));
        urlImageServer.setTooltip(urlImageServerTooltip);

        String runServerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.runServer");
        runServer.setText(runServerText);
        Tooltip runServerTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.runServer"));
        runServerTooltip.setShowDuration(Duration.seconds(tooltipDuration));
        runServer.setTooltip(runServerTooltip);

        String joinServerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.joinServer");
        joinServer.setText(joinServerText);
        Tooltip joinServerTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.joinServer"));
        joinServerTooltip.setShowDuration(Duration.seconds(tooltipDuration));
        joinServer.setTooltip(joinServerTooltip);

        Tooltip thumbnailTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.thumbnail"));
        thumbnailTooltip.setShowDuration(Duration.seconds(tooltipDuration));
        Tooltip.install(imageWebView,thumbnailTooltip);

        // Advanced Parameters
        String portsLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.ports");
        portsLabel.setText(portsLabelText);
        TextField[] portsArray = {gamePort, queryPort, webPort};
        Utils.loadTooltip(languageCode, "prop.tooltip.ports", portsImg, portsLabel, portsArray);

        String customParametersLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.customParameters");
        customParametersLabel.setText(customParametersLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.customParameters", customParametersImg, customParametersLabel, customParameters);

        String teamCollisionLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.teamCollision");
        String teamCollisionText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.teamCollisionEnable");
        teamCollisionLabel.setText(teamCollisionLabelText);
        teamCollision.setText(teamCollisionText);
        Utils.loadTooltip(languageCode, "prop.tooltip.teamCollision", teamCollisionImg, teamCollisionLabel, teamCollision);

        String adminPauseLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.adminPause");
        String adminPauseText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.adminPauseEnable");
        adminPauseLabel.setText(adminPauseLabelText);
        adminPause.setText(adminPauseText);
        Utils.loadTooltip(languageCode, "prop.tooltip.adminPause", adminPauseImg, adminPauseLabel, adminPause);

        String adminLoginLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.adminLogin");
        String adminLoginText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.adminLoginEnable");
        adminLoginLabel.setText(adminLoginLabelText);
        adminLogin.setText(adminLoginText);
        Utils.loadTooltip(languageCode, "prop.tooltip.adminLogin", adminLoginImg, adminLoginLabel, adminLogin);

        String mapVotingLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.mapVoting");
        String mapVotingText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.mapVotingEnable");
        mapVotingLabel.setText(mapVotingLabelText);
        mapVoting.setText(mapVotingText);
        Utils.loadTooltip(languageCode, "prop.tooltip.mapVoting", mapVotingImg, mapVotingLabel, mapVoting);

        String kickVotingLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.kickVoting");
        String kickVotingText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.kickVotingEnable");
        kickVotingLabel.setText(kickVotingLabelText);
        kickVoting.setText(kickVotingText);
        Utils.loadTooltip(languageCode, "prop.tooltip.kickVoting", kickVotingImg, kickVotingLabel, kickVoting);

        String publicTextChatLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.publicTextChat");
        String publicTextChatText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.publicTextChatEnable");
        publicTextChatLabel.setText(publicTextChatLabelText);
        publicTextChat.setText(publicTextChatText);
        Utils.loadTooltip(languageCode, "prop.tooltip.publicTextChat", publicTextChatImg, publicTextChatLabel, publicTextChat);

        String voipLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.voip");
        String voipText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.voipEnable");
        voipLabel.setText(voipLabelText);
        voip.setText(voipText);
        Utils.loadTooltip(languageCode, "prop.tooltip.voip", voipImg, voipLabel, voip);

        String chatLoggingLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.chatLogging");
        String chatLoggingText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.chatLoggingEnable");
        chatLoggingLabel.setText(chatLoggingLabelText);
        chatLogging.setText(chatLoggingText);
        Utils.loadTooltip(languageCode, "prop.tooltip.chatLogging", chatLoggingImg, chatLoggingLabel, chatLogging);

        String mapVotingTimeLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.mapVotingTime");
        mapVotingTimeLabel.setText(mapVotingTimeLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.mapVotingTime", mapVotingTimeImg, mapVotingTimeLabel, mapVotingTime);

        String kickPercentageLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.kickPercentage");
        kickPercentageLabel.setText(kickPercentageLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.kickPercentage", kickPercentageImg, kickPercentageLabel, kickPercentage);

        String spectatorsChatLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.spectatorsChat");
        String spectatorsChatText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.spectatorsChatEnable");
        spectatorsChatLabel.setText(spectatorsChatLabelText);
        spectatorsChat.setText(spectatorsChatText);
        Utils.loadTooltip(languageCode, "prop.tooltip.spectatorsChat", spectatorsChatImg, spectatorsChatLabel, spectatorsChat);

        String chatLoggingFileLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.chatLoggingFile");
        String chatLoggingFileTimestampText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.chatLoggingFileTimestamp");
        chatLoggingFileLabel.setText(chatLoggingFileLabelText);
        chatLoggingFileTimestamp.setText(chatLoggingFileTimestampText);
        Utils.loadTooltip(languageCode, "prop.tooltip.chatLoggingFile", chatLoggingFileImg, chatLoggingFileLabel, chatLoggingFileTimestamp, chatLoggingFile);

        String timeBetweenKicksLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.timeBetweenKicks");
        timeBetweenKicksLabel.setText(timeBetweenKicksLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.timeBetweenKicks", timeBetweenKicksImg, timeBetweenKicksLabel, timeBetweenKicks);

        String maxIdleTimeLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.maxIdleTime");
        maxIdleTimeLabel.setText(maxIdleTimeLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.maxIdleTime", maxIdleTimeImg, maxIdleTimeLabel, maxIdleTime);

        String deadPlayersCanTalkLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.deadPlayersCanTalk");
        String deadPlayersCanTalkText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.deadPlayersCanTalkEnable");
        deadPlayersCanTalkLabel.setText(deadPlayersCanTalkLabelText);
        deadPlayersCanTalk.setText(deadPlayersCanTalkText);
        Utils.loadTooltip(languageCode, "prop.tooltip.deadPlayersCanTalk", deadPlayersCanTalkImg, deadPlayersCanTalkLabel, deadPlayersCanTalk);

        String readyUpDelayLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.readyUpDelay");
        readyUpDelayLabel.setText(readyUpDelayLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.readyUpDelay", readyUpDelayImg, readyUpDelayLabel, readyUpDelay);

        String gameStartDelayLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.gameStartDelay");
        gameStartDelayLabel.setText(gameStartDelayLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.gameStartDelay", gameStartDelayImg, gameStartDelayLabel, gameStartDelay);

        String maxSpectatorsLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.maxSpectators");
        maxSpectatorsLabel.setText(maxSpectatorsLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.maxSpectators", maxSpectatorsImg, maxSpectatorsLabel, maxSpectators);

        String mapObjetivesLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.mapObjetives");
        String mapObjetivesText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.mapObjetivesEnable");
        mapObjetivesLabel.setText(mapObjetivesLabelText);
        mapObjetives.setText(mapObjetivesText);
        Utils.loadTooltip(languageCode, "prop.tooltip.mapObjetives", mapObjetivesImg, mapObjetivesLabel, mapObjetives);

        String pickupItemsLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.pickupItems");
        String pickupItemsText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.pickupItemsEnable");
        pickupItemsLabel.setText(pickupItemsLabelText);
        pickupItems.setText(pickupItemsText);
        Utils.loadTooltip(languageCode, "prop.tooltip.pickupItems", pickupItemsImg, pickupItemsLabel, pickupItems);

        String friendlyFirePercentageLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.friendlyFirePercentage");
        friendlyFirePercentageLabel.setText(friendlyFirePercentageLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.friendlyFirePercentage", friendlyFirePercentageImg, friendlyFirePercentageLabel, friendlyFirePercentage);

    }

    private void loadActualProfile() throws Exception {

        platformSelect.getSelectionModel().select(
                Session.getInstance().getPlatform() != null ?
                        EnumPlatform.getByName(Session.getInstance().getPlatform().getKey()).getIndex() : 0
        );

        LoadActualProfileFacadeResult result = facade.loadActualProfile(
                platformSelect.getValue().getKey(),
                profileSelect.getValue().getName()
        );

        if (!userInteractLanguageSelect) {
            languageSelect.getSelectionModel().select(
                    languageSelect.getItems().stream()
                            .map(SelectDto::getKey)
                            .collect(Collectors.toList())
                            .indexOf(
                                    result.getProfileDto().getLanguage().getKey()
                            )
            );
        }

        gameTypeSelect.getSelectionModel().select(
            gameTypeSelect.getItems().stream().map(GameTypeDto::getKey).collect(Collectors.toList()).indexOf(result.getProfileDto().getGametype().getKey())
        );
        difficultySelect.setDisable(gameTypeSelect.getValue() != null ? !gameTypeSelect.getValue().isDifficultyEnabled(): false);
        difficultySelect.getSelectionModel().select(
                difficultySelect.getItems().stream().map(SelectDto::getKey).collect(Collectors.toList()).indexOf(result.getProfileDto().getDifficulty().getKey())
        );
        lengthSelect.setDisable(gameTypeSelect.getValue() != null ? !gameTypeSelect.getValue().isLengthEnabled(): false);
        lengthSelect.getSelectionModel().select(
                lengthSelect.getItems().stream().map(SelectDto::getKey).collect(Collectors.toList()).indexOf(result.getProfileDto().getLength().getKey())
        );
        maxPlayersSelect.getSelectionModel().select(
                maxPlayersSelect.getItems().stream().map(SelectDto::getKey).collect(Collectors.toList()).indexOf(result.getProfileDto().getMaxPlayers().getKey())
        );

        if (result.getProfileDto().getMap() != null && result.getSelectedProfileMapOptional().isPresent()) {
            platformProfileMapSelect.getSelectionModel().select(result.getFilteredMapDtoList().indexOf(result.getSelectedProfileMapOptional().get()));
        }
        if (platformProfileMapSelect.getValue() != null) {
            noSelectedMapImage.setVisible(false);
        } else {
            noSelectedMapImage.setVisible(true);
        }

        serverName.setText(result.getProfileDto().getServerName());
        Integer webPortValue = result.getProfileDto().getWebPort();
        webPort.setText(webPortValue != null? String.valueOf(webPortValue): "");
        Integer gamePortValue = result.getProfileDto().getGamePort();
        gamePort.setText(gamePortValue != null? String.valueOf(gamePortValue): "");
        Integer queryPortValue = result.getProfileDto().getQueryPort();
        queryPort.setText(queryPortValue != null? String.valueOf(queryPortValue): "");
        yourClan.setText(result.getProfileDto().getYourClan());
        yourWebLink.setText(result.getProfileDto().getYourWebLink());
        urlImageServer.setText(result.getProfileDto().getUrlImageServer());
        welcomeMessage.setText(result.getProfileDto().getWelcomeMessage());
        customParameters.setText(result.getProfileDto().getCustomParameters());
        webPage.setSelected(result.getProfileDto().getWebPage() != null ? result.getProfileDto().getWebPage(): false);
        takeover.setSelected(result.getProfileDto().getTakeover() != null ? result.getProfileDto().getTakeover(): false);
        try {
            if (StringUtils.isNotEmpty(urlImageServer.getText())) {
                imageWebView.getEngine().load(urlImageServer.getText());
            } else {
                File file = new File(System.getProperty("user.dir") + "/external-images/no-server-photo.png");
                if (file.exists()) {
                    imageWebView.getEngine().load("file:" + System.getProperty("user.dir") + "/external-images/no-server-photo.png");
                } else {
                    imageWebView.getEngine().load("file:" + getClass().getResource("/external-images/no-server-photo.png").getPath());
                }
            }
            serverPassword.setText(Utils.decryptAES(result.getProfileDto().getServerPassword()));
            webPassword.setText(Utils.decryptAES(result.getProfileDto().getWebPassword()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
        teamCollision.setSelected(result.getProfileDto().getTeamCollision() != null ? result.getProfileDto().getTeamCollision(): false);
        adminPause.setSelected(result.getProfileDto().getAdminCanPause() != null ? result.getProfileDto().getAdminCanPause(): false);
        adminLogin.setSelected(result.getProfileDto().getAnnounceAdminLogin() != null ? result.getProfileDto().getAnnounceAdminLogin(): false);
        mapVoting.setSelected(result.getProfileDto().getMapVoting() != null ? result.getProfileDto().getMapVoting(): false);
        kickVoting.setSelected(result.getProfileDto().getKickVoting() != null ? result.getProfileDto().getKickVoting(): false);
        publicTextChat.setSelected(result.getProfileDto().getPublicTextChat() != null ? result.getProfileDto().getPublicTextChat(): false);
        spectatorsChat.setSelected(result.getProfileDto().getSpectatorsOnlyChatToOtherSpectators() != null ? result.getProfileDto().getSpectatorsOnlyChatToOtherSpectators(): false);
        voip.setSelected(result.getProfileDto().getVoip() != null ? result.getProfileDto().getVoip(): false);
        chatLogging.setSelected(result.getProfileDto().getChatLogging() != null ? result.getProfileDto().getChatLogging(): false);
        mapVotingTime.setText(result.getProfileDto().getMapVotingTime() != null? String.valueOf(result.getProfileDto().getMapVotingTime()): "");
        kickPercentage.setText(result.getProfileDto().getKickPercentage() != null? String.valueOf(result.getProfileDto().getKickPercentage()): "");
        chatLoggingFile.setText(result.getProfileDto().getChatLoggingFile());
        chatLoggingFileTimestamp.setSelected(result.getProfileDto().getChatLoggingFileTimestamp() != null ? result.getProfileDto().getChatLoggingFileTimestamp(): false);
        timeBetweenKicks.setText(result.getProfileDto().getTimeBetweenKicks() != null? String.valueOf(result.getProfileDto().getTimeBetweenKicks()): "");
        maxIdleTime.setText(result.getProfileDto().getMaxIdleTime() != null? String.valueOf(result.getProfileDto().getMaxIdleTime()): "");
        deadPlayersCanTalk.setSelected(result.getProfileDto().getDeadPlayersCanTalk() != null ? result.getProfileDto().getDeadPlayersCanTalk(): false);
        readyUpDelay.setText(result.getProfileDto().getReadyUpDelay() != null? String.valueOf(result.getProfileDto().getReadyUpDelay()): "");
        gameStartDelay.setText(result.getProfileDto().getGameStartDelay() != null? String.valueOf(result.getProfileDto().getGameStartDelay()): "");
        maxSpectators.setText(result.getProfileDto().getMaxSpectators() != null? String.valueOf(result.getProfileDto().getMaxSpectators()): "");
        mapObjetives.setSelected(result.getProfileDto().getMapObjetives() != null ? result.getProfileDto().getMapObjetives(): false);
        pickupItems.setSelected(result.getProfileDto().getPickupItems() != null ? result.getProfileDto().getPickupItems(): false);
        friendlyFirePercentage.setText(result.getProfileDto().getFriendlyFirePercentage() != null? String.valueOf(result.getProfileDto().getFriendlyFirePercentage()): "");

        Session.getInstance().setActualProfileName(result.getProfileDto().getName());
        if (Session.getInstance().getMapsProfile() == null || result.getProfileDto().getName().equals(Session.getInstance().getMapsProfile().getName())) {
            Session.getInstance().setMapsProfile(result.getProfileDto());
        }
    }

    @FXML
    private void profileOnMouseReleased(MouseEvent mouseEvent) {
        userInteractLanguageSelect = false;
    }


    @FXML
    private void profileOnAction() {
        Session.getInstance().setActualProfileName(profileSelect.getValue().getName());
        try {
            loadActualProfile();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void languageOnMouseReleased(MouseEvent mouseEvent) {
        userInteractLanguageSelect = true;
    }

    @FXML
    private void gameTypeOnAction() {
        try {
            difficultySelect.setDisable(gameTypeSelect.getValue() != null ? !gameTypeSelect.getValue().isDifficultyEnabled() : false);
            lengthSelect.setDisable(gameTypeSelect.getValue() != null ? !gameTypeSelect.getValue().isLengthEnabled() : false);
            if (profileSelect.getValue() != null && gameTypeSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                String gameTypeCode = gameTypeSelect.getValue().getKey();
                facade.updateProfileSetGameType(profileName, gameTypeCode);
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
            if (profileSelect.getValue() != null && platformProfileMapSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                String mapCode = platformProfileMapSelect.getValue().getMapDto().getKey();
                boolean isOfficial = platformProfileMapSelect.getValue().getMapDto().isOfficial();
                facade.updateProfileSetMap(profileName, mapCode, isOfficial);
                ProfileDto databaseProfile = facade.findProfileDtoByName(profileSelect.getValue().getName());
                Session.getInstance().setActualProfileName(databaseProfile.getName());
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
            if (profileSelect.getValue() != null && difficultySelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                String difficultyCode = difficultySelect.getValue().getKey();
                facade.updateProfileSetDifficulty(profileName, difficultyCode);
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
            if (profileSelect.getValue() != null && lengthSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                String lengthCode = lengthSelect.getValue().getKey();
                facade.updateProfileSetLength(profileName, lengthCode);
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
            if (profileSelect.getValue() != null && maxPlayersSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                String maxPlayersCode = maxPlayersSelect.getValue().getKey();
                facade.updateProfileSetMaxPlayers(profileName, maxPlayersCode);
            }
        } catch (Exception e) {
            String headerText = "The max. players value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void languageOnAction(ActionEvent event) {
        try {
            loadLanguageTexts(languageSelect.getValue() != null ? languageSelect.getValue().getKey() : "en");
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                String languageCode = languageSelect.getValue().getKey();
                facade.updateProfileSetLanguage(profileName, languageCode);
            }

            ListValuesMainContentFacadeResult listValuesMainContentFacadeResult = facade.execute();

            gameTypeSelect.getItems().clear();
            difficultySelect.getItems().clear();
            lengthSelect.getItems().clear();
            maxPlayersSelect.getItems().clear();

            gameTypeSelect.setItems(listValuesMainContentFacadeResult.getGameTypeDtoList());
            difficultySelect.setItems(listValuesMainContentFacadeResult.getDifficultyDtoList());
            lengthSelect.setItems(listValuesMainContentFacadeResult.getLengthDtoList());
            maxPlayersSelect.setItems(listValuesMainContentFacadeResult.getPlayerDtoList());

            if (userInteractLanguageSelect) {
                ProfileDto oldProfile = profileSelect.getValue();
                profileSelect.getItems().clear();
                profileSelect.setItems(listValuesMainContentFacadeResult.getProfileDtoList());

                profileSelect.getSelectionModel().select(
                        oldProfile != null ?
                            profileSelect.getItems().stream()
                                .map(ProfileDto::getName)
                                .collect(Collectors.toList())
                                .indexOf(
                                        oldProfile.getName()
                                ):
                        0
                );
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
                facade.updateProfileSetWebPage(profileName, webPage.isSelected());
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
                facade.updateProfileSetTakeover(profileName, takeover.isSelected());
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
            facade.runServers(
                    platformSelect.getValue().getKey(),
                    profileSelect.getValue().getName(),
                    languageSelect.getValue().getKey()
            );
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void joinServerOnAction() {
        try {
            facade.joinServer(
                    platformSelect.getValue().getKey(),
                    profileSelect.getValue().getName(),
                    languageSelect.getValue().getKey()
            );
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
                facade.updateProfileSetMapVoting(profileName, mapVoting.isSelected());
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
                facade.updateProfileSetKickVoting(profileName, kickVoting.isSelected());
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
                facade.updateProfileSetPublicTextChat(profileName, publicTextChat.isSelected());
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
                facade.updateProfileSetSpectatorsChat(profileName, spectatorsChat.isSelected());
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
                facade.updateProfileSetVoip(profileName, voip.isSelected());
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
                facade.updateProfileSetTeamCollision(profileName, teamCollision.isSelected());
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
                facade.updateProfileSetAdminCanPause(profileName, adminPause.isSelected());
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
                facade.updateProfileSetAnnounceAdminLogin(profileName, adminLogin.isSelected());
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
                facade.updateProfileSetChatLogging(profileName, chatLogging.isSelected());
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
                facade.updateProfileSetChatLoggingFileTimestamp(profileName, chatLoggingFileTimestamp.isSelected());
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
                facade.updateProfileSetDeadPlayersCanTalk(profileName, deadPlayersCanTalk.isSelected());
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
                facade.updateProfileSetMapObjetives(profileName, mapObjetives.isSelected());
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
                facade.updateProfileSetPickupItems(profileName, pickupItems.isSelected());
            }
        } catch (Exception e) {
            String headerText = "The pick up items value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    @FXML
    private void platformOnAction() {
        joinServerVisibility();
        try {
            if (profileSelect.getValue() != null) {
                loadPlatformProfileMapSelect();
            }
            Session.getInstance().setPlatform(platformSelect.getValue());
        } catch (Exception e) {
            String headerText = "The platform value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
        }
    }

    private void joinServerVisibility() {
        if (EnumPlatform.STEAM.name().equals(platformSelect.getValue().getKey())) {
            joinServer.setVisible(true);
            joinServerImage.setVisible(true);
        } else {
            // Epic game launcher
            joinServer.setVisible(false);
            joinServerImage.setVisible(false);
        }
    }
}
