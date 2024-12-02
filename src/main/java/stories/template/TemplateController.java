package stories.template;

import dtos.PlatformDto;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.ProfileToDisplay;
import pojos.enums.EnumPlatform;
import pojos.session.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import start.MainApplication;
import stories.profilesedition.ProfilesEditionManagerFacade;
import stories.profilesedition.ProfilesEditionManagerFacadeImpl;
import utils.Utils;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.List;
import java.util.*;

public class TemplateController implements Initializable {

    private static final Logger logger = LogManager.getLogger(TemplateController.class);
    private final PropertyService propertyService;
    private final TemplateManagerFacade facade;

    private String languageCode;

    @FXML private Menu mainPage;
    @FXML private Menu webAdmin;
    @FXML private Menu maps;
    @FXML private Menu installUpdateServer;
    @FXML private Menu help;
    @FXML private MenuItem about;
    @FXML private MenuItem documentation;
    @FXML private MenuItem github;
    @FXML private MenuItem releases;
    @FXML private MenuItem tips;
    @FXML private MenuItem checkForUpdates;
    @FXML private MenuItem donation;
    @FXML private MenuItem discord;

    public TemplateController() {
        super();
        this.facade = new TemplateManagerFacadeImpl();
        propertyService = new PropertyServiceImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");

            String mainPageTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.mainPage");
            mainPage.setGraphic(getLabelWithHandler(mainPageTitle, "/views/mainContent.fxml"));

            String webAdminTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.webAdmin");
            webAdmin.setGraphic(getLabelWithHandler(webAdminTitle, "/views/webAdmin.fxml"));

            String mapsTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.maps");
            maps.setGraphic(getLabelWithHandler(mapsTitle, "/views/maps.fxml"));

            String installUpdateTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.installUpdateServer");
            installUpdateServer.setText(installUpdateTitle);

            String helpTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.help");
            help.setText(helpTitle);

            String aboutTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.help.about");;
            about.setText(aboutTitle);

            String documentationTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.help.documentation");;
            documentation.setText(documentationTitle);

            String githubTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.help.github");
            github.setText(githubTitle);

            String releasesTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.help.releases");
            releases.setText(releasesTitle);

            String tipsTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.help.tips");
            tips.setText(tipsTitle);

            String checkForUpdatesTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.help.checkForUpdates");
            checkForUpdates.setText(checkForUpdatesTitle);

            String donationTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.help.donation");
            donation.setText(donationTitle);

            String discordTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.help.discord");
            discord.setText(discordTitle);

        } catch (Exception e) {
            String message = "Error setting menu titles";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    private Label getLabelWithHandler(String title, String fxmlFilePath) {
        Label menuLabel = new Label(title);
        menuLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                loadNewContent(title, fxmlFilePath);
            }
        });
        return menuLabel;
    }

    private void loadNewContent(String title, String fxmlFilePath) {
        try {
            GridPane templateContent = (GridPane)MainApplication.getTemplate().getNamespace().get("content");
            templateContent.getColumnConstraints().clear();
            templateContent.getRowConstraints().clear();
            templateContent.getChildren().clear();
            FXMLLoader content = new FXMLLoader(getClass().getResource(fxmlFilePath));
            content.setRoot(MainApplication.getTemplate().getNamespace().get("content"));
            content.load();

            String mainPageTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.mainPage");
            mainPage.setDisable(mainPageTitle.equals(title));

            String webAdminTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.webAdmin");
            webAdmin.setDisable(webAdminTitle.equals(title));

            String mapsTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.maps");
            maps.setDisable(mapsTitle.equals(title));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    private void loadNewContent(String fxmlFilePath) {
        try {
            GridPane templateContent = (GridPane) MainApplication.getTemplate().getNamespace().get("content");
            templateContent.getColumnConstraints().clear();
            templateContent.getRowConstraints().clear();
            templateContent.getChildren().clear();
            FXMLLoader content = new FXMLLoader(getClass().getResource(fxmlFilePath));
            content.setRoot(MainApplication.getTemplate().getNamespace().get("content"));
            content.load();

            mainPage.setDisable(false);
            webAdmin.setDisable(false);
            maps.setDisable(false);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void aboutMenuOnAction() {
        try {
            String versionText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.help.about.version");
            String developedText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.help.about.developed");
            String translatedToSpanishBy = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.help.about.translatedSpanish");
            String translatedToFrenchBy = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.help.about.translatedFrench");
            String translatedToRussianBy = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.help.about.translatedRussian");
            String applicationVersion = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationVersion");

            String headerText = versionText + ": " + applicationVersion;

            InputStream cesarRgonImageInputStream = getClass().getClassLoader().getResourceAsStream("images/cesar-rgon-photo.png");
            Image cesarRgonImage = new Image(cesarRgonImageInputStream);
            ImageView cesarRgonImageView = new ImageView(cesarRgonImage);
            cesarRgonImageView.setPreserveRatio(true);
            cesarRgonImageView.setFitWidth(128);

            Label developedLabel = new Label(developedText + " cesar-rgon");
            developedLabel.setPadding(new Insets(50,0,0,0));
            Label spanishTranslationLabel = new Label(translatedToSpanishBy + " cesar-rgon");
            spanishTranslationLabel.setPadding(new Insets(0,0,60,0));

            InputStream noxInputStream = getClass().getClassLoader().getResourceAsStream("images/-foG.Nox-photo.jpg");
            Image noxImage = new Image(noxInputStream);
            ImageView noxImageView = new ImageView(noxImage);
            noxImageView.setPreserveRatio(true);
            noxImageView.setFitWidth(128);

            InputStream dreadmore404InputStream = getClass().getClassLoader().getResourceAsStream("images/dreadmor404-photo.png");
            Image dreadmore404Image = new Image(dreadmore404InputStream);
            ImageView dreadmore404ImageView = new ImageView(dreadmore404Image);
            dreadmore404ImageView.setPreserveRatio(true);
            dreadmore404ImageView.setFitWidth(128);

            Label frenchTranslationLabel = new Label(translatedToFrenchBy + " -foG.Nox");
            Label russianTranslationLabel = new Label(translatedToRussianBy + " Dreadmore404");

            GridPane gridPane = new GridPane();
            gridPane.add(cesarRgonImageView, 1, 1);
            gridPane.add(developedLabel, 2, 1);
            gridPane.add(spanishTranslationLabel, 2, 2);
            GridPane.setRowSpan(cesarRgonImageView, 2);

            gridPane.add(noxImageView, 1, 3);
            gridPane.add(frenchTranslationLabel, 2, 3);

            gridPane.add(dreadmore404ImageView, 1, 4);
            gridPane.add(russianTranslationLabel, 2, 4);

            gridPane.setPrefWidth(450);
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            Utils.infoDialog(headerText, gridPane);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void documentationMenuOnAction() {
        try {
            String documentationUrl = propertyService.getPropertyValue("properties/config.properties", "prop.config.helpReadmeUrl");
            Session.getInstance().setUrl(documentationUrl);
            loadNewContent("/views/webBrowser.fxml");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void githubMenuOnAction() {
        try {
            String githubUrl = propertyService.getPropertyValue("properties/config.properties", "prop.config.helpGithubUrl");
            Session.getInstance().setUrl(githubUrl);
            loadNewContent("/views/webBrowser.fxml");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void releasesMenuOnAction() {
        try {
            String releasesUrl = propertyService.getPropertyValue("properties/config.properties", "prop.config.releasePageGithubUrl");
            Session.getInstance().setUrl(releasesUrl);
            loadNewContent("/views/webBrowser.fxml");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void tipsMenuOnAction() {
        InputStream maxNumberOfTipsIS = null;
        try {
            maxNumberOfTipsIS = new URL("https://raw.githubusercontent.com/cesar-rgon/simple-kf2-server-launcher/master/tips/lastTip.txt").openStream();
            Integer maxNumberOfTips = Integer.parseInt(IOUtils.toString(maxNumberOfTipsIS, StandardCharsets.UTF_8));
            if (maxNumberOfTips == 0) {
                Utils.infoDialog("Show tips", "There are no elements to show");
            }

            Boolean dontShowTipsOnStartup = Boolean.parseBoolean(propertyService.getPropertyValue("properties/config.properties", "prop.config.dontShowTipsOnStartup"));
            Optional<Integer> actualTipNumber = Optional.ofNullable(maxNumberOfTips);
            if (actualTipNumber.isPresent()) {
                do {
                    actualTipNumber = Utils.renderTipMarkDown(actualTipNumber.get(), maxNumberOfTips, dontShowTipsOnStartup);
                } while (actualTipNumber.isPresent() && actualTipNumber.get() > 0);
            }
            if (actualTipNumber.isPresent() && actualTipNumber.get() == 0) {
                propertyService.setProperty("properties/config.properties", "prop.config.dontShowTipsOnStartup", "true");
            }
            if (actualTipNumber.isPresent() && actualTipNumber.get() == -1) {
                propertyService.setProperty("properties/config.properties", "prop.config.dontShowTipsOnStartup", "false");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.infoDialog("Show tips", "There are no elements to show");
        } finally {
            IOUtils.closeQuietly(maxNumberOfTipsIS);
        }
    }

    @FXML
    private void checkForUpdatesMenuOnAction() {
        try {
            if (Utils.upgradeLauncher(languageCode, false)) {

                Properties upgradeProperties = new Properties();
                ObservableList<PlatformDto> allPlatformList = facade.execute().getAllPlatformList();
                Optional<String> steamInstallationFolderOptional = allPlatformList.stream().filter(p -> EnumPlatform.STEAM.getDescripcion().equals(p.getValue())).map(PlatformDto::getInstallationFolder).findFirst();
                Optional<String> epicInstallationFolderOptional = allPlatformList.stream().filter(p -> EnumPlatform.EPIC.getDescripcion().equals(p.getValue())).map(PlatformDto::getInstallationFolder).findFirst();

                upgradeProperties.setProperty("prop.upgrade.fromVersion", propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationVersion"));
                upgradeProperties.setProperty("prop.upgrade.steamInstallationFolder", steamInstallationFolderOptional.orElse(StringUtils.EMPTY));
                upgradeProperties.setProperty("prop.upgrade.epicInstallationFolder", epicInstallationFolderOptional.orElse(StringUtils.EMPTY));

                ProfilesEditionManagerFacade profilesEditionManagerFacade = new ProfilesEditionManagerFacadeImpl();
                List<ProfileToDisplay> allProfiles = profilesEditionManagerFacade.selectProfilesToBeExported();

                Date date = new Date();
                Timestamp timestamp = new Timestamp(date.getTime());
                String timestampStr = StringUtils.replace(timestamp.toString(), " ", "_");
                timestampStr = StringUtils.replace(timestampStr, ":", "_");
                timestampStr = StringUtils.replace(timestampStr, ".", "_");
                String tempFolder = System.getProperty("java.io.tmpdir");
                File profilesExportedTemporalFile = new File(tempFolder + "exported_profiles_" + timestampStr + ".properties");

                profilesEditionManagerFacade.exportProfilesToFile(allProfiles, profilesExportedTemporalFile);
                upgradeProperties.setProperty("prop.upgrade.profilesExportedFile", profilesExportedTemporalFile.getAbsolutePath());

                String latestReleaseUrl = propertyService.getPropertyValue("properties/config.properties", "prop.config.latestReleasePageGithubUrl");
                String targetFolderStr = StringUtils.EMPTY;
                File zipFile = Utils.downloadZipFileFromGithub(latestReleaseUrl);
                if (zipFile != null && zipFile.exists() && zipFile.isFile()) {
                    String parentFolderStr = System.getProperty("user.dir") + "/../";
                    targetFolderStr = parentFolderStr + zipFile.getName().replace(".zip", "");
                    File targetFolder = new File(targetFolderStr);
                    if (targetFolder.exists()) {
                        targetFolderStr = targetFolderStr + "_" + timestampStr;
                        targetFolder = new File(targetFolderStr);
                        targetFolder.mkdir();
                    } else {
                        targetFolder.mkdir();
                    }
                    Utils.uncompressZipFile(zipFile, targetFolder);
                    zipFile.delete();
                }

                File upgradeTemporalFile = new File(tempFolder + "upgrade_launcher_" + timestampStr + ".properties");
                propertyService.savePropertiesToFile(upgradeProperties, upgradeTemporalFile);

                File fileToBeExecuted = new File(targetFolderStr + "/SimpleKF2ServerLauncher.jar");
                StringBuffer command = new StringBuffer();
                command.append("java -jar ").append(targetFolderStr).append("/").append(fileToBeExecuted.getName()).append(" --upgrade '").append(upgradeTemporalFile.getAbsolutePath()).append("'");
                Runtime.getRuntime().exec(command.toString(),null, fileToBeExecuted.getParentFile());

                Platform.exit();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void wizardMenuOnAction() {
        try {
            Session.getInstance().setWizardMode(true);
            MainApplication.setTemplate(new FXMLLoader(getClass().getResource("/views/wizard-step1.fxml")));
            Scene scene = new Scene(MainApplication.getTemplate().load());
            MainApplication.getPrimaryStage().setScene(scene);
            MainApplication.getPrimaryStage().setWidth(1024);
            MainApplication.getPrimaryStage().setHeight(750);
            MainApplication.getPrimaryStage().setMaximized(false);
            MainApplication.getPrimaryStage().show();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void donationMenuOnAction() {
        try {
            String donationUrl = "https://www.paypal.me/cesarrgon";
            Session.getInstance().setUrl(donationUrl);
            loadNewContent("/views/webBrowser.fxml");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void steamServerInstallMenuOnAction() {
        try {
            String installUpdateTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.installUpdateServer");
            loadNewContent(installUpdateTitle, "/views/installUpdateSteamServer.fxml");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void joinToDiscordMenuOnAction() {
        try {
            URI discordUrl = new URI("https://discord.gg/WdwRU522Fb");
            Desktop.getDesktop().browse(discordUrl);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void epicServerInstallMenuOnAction() {
        try {
            String installUpdateTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.installUpdateServer");
            loadNewContent(installUpdateTitle, "/views/installUpdateEpicServer.fxml");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }
}
