package stories.maincontent;

import dtos.MapDto;
import dtos.ProfileDto;
import dtos.SelectDto;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import org.apache.commons.lang3.StringUtils;
import pojos.session.Session;
import utils.Utils;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainContentController implements Initializable {

    private final MainContentFacade facade;

    @FXML private ComboBox<ProfileDto> profileSelect;
    @FXML private ComboBox<SelectDto> languageSelect;
    @FXML private ComboBox<SelectDto> gameTypeSelect;
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
    @FXML private TextField customParameters;
    @FXML private CheckBox webPage;
    @FXML private TextField console;
    @FXML private WebView imageWebView;

    public MainContentController() {
        super();
        facade = new MainContentFacadeImpl();
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            ObservableList<ProfileDto> profileOptions = facade.listAllProfiles();
            profileSelect.setItems(profileOptions);
            if (!profileOptions.isEmpty()) {
                profileSelect.setValue(Session.getInstance().getActualProfile() != null? Session.getInstance().getActualProfile(): profileOptions.get(0));
            } else {
                profileSelect.setValue(null);
            }
            Session.getInstance().setActualProfile(profileSelect.getValue());
            languageSelect.setItems(facade.listAllLanguages());
            gameTypeSelect.setItems(facade.listAllGameTypes());
            mapSelect.setItems(facade.listDownloadedMaps());
            difficultySelect.setItems(facade.listAllDifficulties());
            lengthSelect.setItems(facade.listAllLengths());
            maxPlayersSelect.setItems(facade.listAllPlayers());
            console.setText(Session.getInstance().getConsole());
        } catch (SQLException e) {
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }

        if (profileSelect.getValue() != null) {
            loadActualProfile(profileSelect.getValue());
        }

        serverName.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue().getName();
                        if (!facade.updateProfileSetServerName(profileName, serverName.getText())) {
                            Utils.errorDialog("Error updating the profile information", "The server name value could not be saved!", null);
                        }
                    }
                } catch (SQLException e) {
                    Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
                }
            }
        });

        serverPassword.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue().getName();
                        if (!facade.updateProfileSetServerPassword(profileName, serverPassword.getText())) {
                            Utils.errorDialog("Error updating the profile information", "The server password value could not be saved!", null);
                        }
                    }
                } catch (Exception e) {
                    Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
                }
            }
        });

        webPassword.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue().getName();
                        if (!facade.updateProfileSetWebPassword(profileName, webPassword.getText())) {
                            Utils.errorDialog("Error updating the profile information", "The web password value could not be saved!", null);
                        }
                    }
                } catch (Exception e) {
                    Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
                }
            }
        });

        webPort.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue().getName();
                        if (StringUtils.isNotEmpty(webPort.getText())) {
                            if (!facade.updateProfileSetWebPort(profileName, Integer.parseInt(webPort.getText()))) {
                                Utils.errorDialog("Error updating the profile information", "The web port value could not be saved!", null);
                            }
                        } else {
                            if (!facade.updateProfileSetWebPort(profileName, null)) {
                                Utils.errorDialog("Error updating the profile information", "The web port value could not be saved!", null);
                            }
                        }
                    }
                } catch (Exception e) {
                    Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
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
                        String profileName = profileSelect.getValue().getName();
                        if (StringUtils.isNotEmpty(gamePort.getText())) {
                            if (!facade.updateProfileSetGamePort(profileName, Integer.parseInt(gamePort.getText()))) {
                                Utils.errorDialog("Error updating the profile information", "The game port value could not be saved!", null);
                            }
                        } else {
                            if (!facade.updateProfileSetGamePort(profileName, null)) {
                                Utils.errorDialog("Error updating the profile information", "The game port value could not be saved!", null);
                            }
                        }
                    }
                } catch (Exception e) {
                    Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
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
                        String profileName = profileSelect.getValue().getName();
                        if (StringUtils.isNotEmpty(queryPort.getText())) {
                            if (!facade.updateProfileSetQueryPort(profileName, Integer.parseInt(queryPort.getText()))) {
                                Utils.errorDialog("Error updating the profile information", "The query port value could not be saved!", null);
                            }
                        } else {
                            if (!facade.updateProfileSetQueryPort(profileName, null)) {
                                Utils.errorDialog("Error updating the profile information", "The query port value could not be saved!", null);
                            }
                        }
                    }
                } catch (Exception e) {
                    Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
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
                        String profileName = profileSelect.getValue().getName();
                        if (!facade.updateProfileSetYourClan(profileName, yourClan.getText())) {
                            Utils.errorDialog("Error updating the profile information", "The clan value could not be saved!", null);
                        }
                    }
                } catch (SQLException e) {
                    Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
                }
            }
        });

        yourWebLink.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue().getName();
                        if (!facade.updateProfileSetYourWebLink(profileName, yourWebLink.getText())) {
                            Utils.errorDialog("Error updating the profile information", "The web link value could not be saved!", null);
                        }
                    }
                } catch (SQLException e) {
                    Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
                }
            }
        });

        urlImageServer.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue().getName();
                        if (!facade.updateProfileSetUrlImageServer(profileName, urlImageServer.getText())) {
                            Utils.errorDialog("Error updating the profile information", "The image server link value could not be saved!", null);
                        }
                        if (StringUtils.isNotEmpty(urlImageServer.getText())) {
                            imageWebView.getEngine().load(urlImageServer.getText());
                            imageWebView.setVisible(true);
                        } else {
                            imageWebView.setVisible(false);
                            imageWebView.getEngine().load(null);
                        }
                    }
                } catch (SQLException e) {
                    Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
                }
            }
        });

        welcomeMessage.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue().getName();
                        if (!facade.updateProfileSetWelcomeMessage(profileName, welcomeMessage.getText())) {
                            Utils.errorDialog("Error updating the profile information", "The welcome message value could not be saved!", null);
                        }
                    }
                } catch (SQLException e) {
                    Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
                }
            }
        });

        customParameters.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        String profileName = profileSelect.getValue().getName();
                        if (!facade.updateProfileSetCustomParameters(profileName, customParameters.getText())) {
                            Utils.errorDialog("Error updating the profile information", "The custom parameters value could not be saved!", null);
                        }
                    }
                } catch (SQLException e) {
                    Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
                }
            }
        });
    }

    private void loadActualProfile(ProfileDto profile) {
        languageSelect.setValue(profile.getLanguage());
        gameTypeSelect.setValue(profile.getGametype());
        mapSelect.setValue(profile.getMap());
        difficultySelect.setValue(profile.getDifficulty());
        lengthSelect.setValue(profile.getLength());
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
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }


    @FXML
    private void profileOnAction() {
        String headerText = "Error loading the profile information";
        String contentText = "The profile could not be changed!";
        try {
            ProfileDto databaseProfile = facade.findProfileByName(profileSelect.getValue().getName());
            loadActualProfile(databaseProfile);
            Session.getInstance().setActualProfile(profileSelect.getValue());
        } catch (SQLException e) {
            Utils.errorDialog(headerText, contentText, e);
        }
    }

    @FXML
    private void gameTypeOnAction() {
        String headerText = "Error updating the profile information";
        String contentText = "The gametype value could not be saved!";
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                String gameTypeCode = gameTypeSelect.getValue().getKey();
                if (!facade.updateProfileSetGameType(profileName, gameTypeCode)) {
                    Utils.errorDialog(headerText, contentText, null);
                }
            }
        } catch (Exception e) {
            Utils.errorDialog(headerText, contentText, e);
        }
    }


    @FXML
    private void mapOnAction() {
        String headerText = "Error updating the profile information";
        String contentText = "The map value could not be saved!";
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                String mapCode = mapSelect.getValue().getKey();
                if (!facade.updateProfileSetMap(profileName, mapCode)) {
                    Utils.errorDialog(headerText, contentText, null);
                }
            }
        } catch (Exception e) {
            Utils.errorDialog(headerText, contentText, e);
        }
    }

    @FXML
    private void difficultyOnAction() {
        String headerText = "Error updating the profile information";
        String contentText = "The difficulty value could not be saved!";
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                String difficultyCode = difficultySelect.getValue().getKey();
                if (!facade.updateProfileSetDifficulty(profileName, difficultyCode)) {
                    Utils.errorDialog(headerText, contentText, null);
                }
            }
        } catch (Exception e) {
            Utils.errorDialog(headerText, contentText, e);
        }
    }

    @FXML
    private void lengthOnAction() {
        String headerText = "Error updating the profile information";
        String contentText = "The length value could not be saved!";
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                String lengthCode = lengthSelect.getValue().getKey();
                if (!facade.updateProfileSetLength(profileName, lengthCode)) {
                    Utils.errorDialog(headerText, contentText, null);
                }
            }
        } catch (Exception e) {
            Utils.errorDialog(headerText, contentText, e);
        }
    }

    @FXML
    private void maxPlayersOnAction() {
        String headerText = "Error updating the profile information";
        String contentText = "The max.players value could not be saved!";
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                String maxPlayersCode = maxPlayersSelect.getValue().getKey();
                if (!facade.updateProfileSetMaxPlayers(profileName, maxPlayersCode)) {
                    Utils.errorDialog(headerText, contentText, null);
                }
            }
        } catch (Exception e) {
            Utils.errorDialog(headerText, contentText, e);
        }
    }

    @FXML
    private void languageOnAction() {
        String headerText = "Error updating the profile information";
        String contentText = "The language value could not be saved!";
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                String languageCode = languageSelect.getValue().getKey();
                if (!facade.updateProfileSetLanguage(profileName, languageCode)) {
                    Utils.errorDialog(headerText, contentText, null);
                }
            }
        } catch (Exception e) {
            Utils.errorDialog(headerText, contentText, e);
        }
    }

    @FXML
    private void webPageOnAction() {
        String headerText = "Error updating the profile information";
        String contentText = "The web page value could not be saved!";
        try {
            if (profileSelect.getValue() != null) {
                String profileName = profileSelect.getValue().getName();
                if (!facade.updateProfileSetWebPage(profileName, webPage.isSelected())) {
                    Utils.errorDialog(headerText, contentText, null);
                }
            }
        } catch (Exception e) {
            Utils.errorDialog(headerText, contentText, e);
        }
    }

    @FXML
    private void runServerOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                ProfileDto databaseProfileDto = facade.findProfileByName(profileSelect.getValue().getName());
                profileSelect.setValue(databaseProfileDto);
            }
            console.setText(facade.runServer(profileSelect.getValue() != null ? profileSelect.getValue().getName(): null));
            Session.getInstance().setConsole(console.getText());
        } catch (SQLException e) {
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void joinServerOnAction() {
        try {
            if (profileSelect.getValue() != null) {
                ProfileDto databaseProfileDto = facade.findProfileByName(profileSelect.getValue().getName());
                profileSelect.setValue(databaseProfileDto);
            }
            facade.joinServer(profileSelect.getValue() != null ? profileSelect.getValue().getName(): null);
        } catch (SQLException e) {
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }
}
