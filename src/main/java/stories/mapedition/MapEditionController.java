package stories.mapedition;

import dtos.CustomMapModDto;
import dtos.ProfileDto;
import dtos.ProfileMapDto;
import dtos.factories.ProfileMapDtoFactory;
import entities.ProfileMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pojos.enums.EnumPlatform;
import pojos.session.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import start.MainApplication;
import utils.Utils;

import java.io.File;
import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class MapEditionController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MapEditionController.class);

    private final MapEditionFacade facade;
    private final PropertyService propertyService;

    private String installationFolder;
    private String languageCode;
    private int profileMapIndex;

    @FXML private WebView mapPreviewWebView;
    @FXML private Label mapNameValue;
    @FXML private ImageView mapPreviewUrlImg;
    @FXML private TextField mapPreviewUrlTextField;
    @FXML private Label officialValue;
    @FXML private ImageView downloadedImg;
    @FXML private Label downloadedValue;
    @FXML private ImageView idWorkShopImg;
    @FXML private Label idWorkShopLabel;
    @FXML private Label idWorkShopValue;
    @FXML private ImageView importationDateImg;
    @FXML private Label importationDateValue;
    @FXML private DatePicker releaseDatePicker;
    @FXML private ImageView infoUrlImg;
    @FXML private TextField infoUrlTextField;
    @FXML private Button previousMapButton;
    @FXML private Button nextMapButton;
    @FXML private Button backButton;
    @FXML private Label titleConfigLabel;
    @FXML private ImageView mapNameImg;
    @FXML private Label mapNameLabel;
    @FXML private Label mapPreviewUrlLabel;
    @FXML private ImageView officialImg;
    @FXML private Label officialLabel;
    @FXML private Label downloadedLabel;
    @FXML private Label importationDateLabel;
    @FXML private ImageView releaseDateImg;
    @FXML private Label releaseDateLabel;
    @FXML private Label infoUrlLabel;
    @FXML private TextField aliasTextField;
    @FXML private Label aliasLabel;
    @FXML private ImageView aliasImg;

    public MapEditionController() {
        super();
        facade = new MapEditionFacadeImpl();
        this.propertyService = new PropertyServiceImpl();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            if (EnumPlatform.STEAM.equals(Session.getInstance().getActualProfile().getPlatform())) {
                installationFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.steamInstallationFolder");
            } else {
                installationFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.epicInstallationFolder");
            }
            String editMapText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.editMap");
            titleConfigLabel.setText(editMapText);

            Double tooltipDuration = Double.parseDouble(
                    propertyService.getPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
            );

            String previousMapText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.previousMap");
            previousMapButton.setText(previousMapText);
            Tooltip previousMapButtonTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.previousMap"));
            previousMapButtonTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            previousMapButton.setTooltip(previousMapButtonTooltip);

            String nextMapText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.nextMap");
            nextMapButton.setText(nextMapText);
            Tooltip nextMapButtonTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.nextMap"));
            nextMapButtonTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            nextMapButton.setTooltip(nextMapButtonTooltip);

            String backText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.backMapsPage");
            backButton.setText(backText);
            Tooltip backButtonTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.backMapList"));
            backButtonTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            backButton.setTooltip(backButtonTooltip);

            String mapNameText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.mapName");
            mapNameLabel.setText(mapNameText);
            loadTooltip(languageCode, "prop.tooltip.mapName", mapNameImg, mapNameLabel, mapNameValue);

            loadTooltip(languageCode, "prop.tooltip.alias", aliasImg, aliasLabel, aliasTextField);

            String mapPreviewUrlText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.mapPrevireUrl");
            mapPreviewUrlLabel.setText(mapPreviewUrlText);
            loadTooltip(languageCode, "prop.tooltip.mapPreviewUrl", mapPreviewUrlImg, mapPreviewUrlLabel, mapPreviewUrlTextField);

            String officialText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.official") + " ?";
            officialLabel.setText(officialText);
            loadTooltip(languageCode, "prop.tooltip.isOfficial", officialImg, officialLabel, officialValue);

            String downloadedText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.downloaded");
            downloadedLabel.setText(downloadedText);
            loadTooltip(languageCode, "prop.tooltip.downloaded", downloadedImg, downloadedLabel, downloadedValue);

            loadTooltip(languageCode, "prop.tooltip.idWorkShop", idWorkShopImg, idWorkShopLabel, idWorkShopValue);

            String importationDateStr = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.importationDate");
            importationDateLabel.setText(importationDateStr);
            loadTooltip(languageCode, "prop.tooltip.importationDate", importationDateImg, importationDateLabel, importationDateValue);

            String releaseDateStr = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.releaseDate");
            releaseDateLabel.setText(releaseDateStr);
            loadTooltip(languageCode, "prop.tooltip.releaseDate", releaseDateImg, releaseDateLabel, releaseDatePicker);

            String infoUrlText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.infoUrl");
            infoUrlLabel.setText(infoUrlText);
            loadTooltip(languageCode, "prop.tooltip.infoUrl", infoUrlImg, infoUrlLabel, infoUrlTextField);

            Tooltip mapThumbnailTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.mapThumbnail"));
            mapThumbnailTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            Tooltip.install(mapPreviewWebView, mapThumbnailTooltip);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }

        profileMapIndex = 0;
        loadProfileMapData(profileMapIndex);

        mapPreviewWebView.getEngine().documentProperty().addListener(new ChangeListener<Document>() {
            @Override
            public void changed(ObservableValue<? extends Document> observable, Document oldDoc, Document doc) {
                if (doc != null) {
                    NodeList imgList = doc.getElementsByTagName("img");
                    if (imgList != null && imgList.getLength() > 0) {
                        Element img = (Element) imgList.item(0);
                        img.setAttribute("width", "512");
                        img.setAttribute("height", "256");
                    }

                }
            }
        });

        aliasTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                try {
                    if (!newValue && !Session.getInstance().getProfileMapList().isEmpty()) {
                        ProfileMap edittedProfileMap = Session.getInstance().getProfileMapList().get(profileMapIndex);
                        if (StringUtils.isBlank(aliasTextField.getText())) {
                            Utils.warningDialog("The alias can not be empty", "Setting the alias as map name");
                            aliasTextField.setText(edittedProfileMap.getMap().getCode());
                        }

                        if (facade.updateMapSetAlias(edittedProfileMap, aliasTextField.getText())) {
                            edittedProfileMap.setAlias(aliasTextField.getText());
                        } else {
                            logger.warn("The alias value could not be saved!" + aliasTextField.getText());
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                                    "prop.message.mapNotSaved");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                                    "prop.message.aliasNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                }
            }
        });

        mapPreviewUrlTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue && !Session.getInstance().getProfileMapList().isEmpty()) {
                        ProfileMap edittedProfileMap = Session.getInstance().getProfileMapList().get(profileMapIndex);

                        String relativeTargetFolder = StringUtils.EMPTY;
                        if (mapPreviewUrlTextField.getText() != null && mapPreviewUrlTextField.getText().startsWith("http")) {
                            String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapCustomLocalFolder");
                            String absoluteTargetFolder = installationFolder + customMapLocalFolder;
                            File localfile = Utils.downloadImageFromUrlToFile(mapPreviewUrlTextField.getText(), absoluteTargetFolder, edittedProfileMap.getMap().getCode());
                            relativeTargetFolder = customMapLocalFolder + "/" + localfile.getName();

                        } else if (mapPreviewUrlTextField.getText() != null && mapPreviewUrlTextField.getText().startsWith("file:")) {
                            relativeTargetFolder = mapPreviewUrlTextField.getText().replace("file:", "").replace(installationFolder, "");
                        }

                        if (facade.updateMapSetUrlPhoto(edittedProfileMap, relativeTargetFolder)) {
                            edittedProfileMap.setUrlPhoto(relativeTargetFolder);
                        } else {
                            logger.warn("The map image link value could not be saved!" + mapPreviewUrlTextField.getText());
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                                    "prop.message.mapNotSaved");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                                    "prop.message.mapImageLinkNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }

                        if (StringUtils.isNotEmpty(mapPreviewUrlTextField.getText())) {
                            mapPreviewWebView.getEngine().load(mapPreviewUrlTextField.getText());
                        } else {
                            File file = new File(System.getProperty("user.dir") + "/external-images/no-photo.png");
                            if (file.exists()) {
                                mapPreviewWebView.getEngine().load("file:" + System.getProperty("user.dir") + "/external-images/no-photo.png");
                            } else {
                                mapPreviewWebView.getEngine().load("file:" + getClass().getResource("/external-images/no-photo.png").getPath());
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                }
            }
        });

        infoUrlTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue && !Session.getInstance().getProfileMapList().isEmpty()) {
                        ProfileMap edittedProfileMap = Session.getInstance().getProfileMapList().get(profileMapIndex);

                        if (facade.updateMapSetInfoUrl(edittedProfileMap, infoUrlTextField.getText())) {
                            edittedProfileMap.setUrlInfo(infoUrlTextField.getText());
                        } else {
                            logger.warn("The map info link value could not be saved!" + infoUrlTextField.getText());
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                                    "prop.message.mapNotSaved");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                                    "prop.message.mapInfoLinkNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                }
            }
        });

        releaseDatePicker.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                try {
                    if (!newValue && !Session.getInstance().getProfileMapList().isEmpty()) {
                        ProfileMap edittedProfileMap = Session.getInstance().getProfileMapList().get(profileMapIndex);

                        if (facade.updateMapSetReleaseDate(edittedProfileMap, releaseDatePicker.getValue())) {
                            edittedProfileMap.setReleaseDate(
                                    Date.from(releaseDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())
                            );
                        } else {
                            logger.warn("The map release date value could not be saved!" + releaseDatePicker.getValue());
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                                    "prop.message.mapNotSaved");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                                    "prop.message.mapReleaseDateNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                }
            }
        });

    }

    private void loadTooltip(String languageCode, String propKey, ImageView img, Label label, Label textValue) throws Exception {
        Double tooltipDuration = Double.parseDouble(
                propertyService.getPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
        );
        Tooltip tooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        tooltip.setShowDuration(Duration.seconds(tooltipDuration));
        Tooltip.install(img, tooltip);
        label.setTooltip(tooltip);
        textValue.setTooltip(tooltip);
    }

    private void loadTooltip(String languageCode, String propKey, ImageView img, Label label, TextField textField) throws Exception {
        Double tooltipDuration = Double.parseDouble(
                propertyService.getPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
        );
        Tooltip tooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        tooltip.setShowDuration(Duration.seconds(tooltipDuration));
        Tooltip.install(img, tooltip);
        label.setTooltip(tooltip);
        textField.setTooltip(tooltip);
    }

    private void loadTooltip(String languageCode, String propKey, ImageView img, Label label, DatePicker calendar) throws Exception {
        Double tooltipDuration = Double.parseDouble(
                propertyService.getPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
        );
        Tooltip tooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        tooltip.setShowDuration(Duration.seconds(tooltipDuration));
        Tooltip.install(img, tooltip);
        label.setTooltip(tooltip);
        calendar.setTooltip(tooltip);
    }

    private void loadProfileMapData(int mapIndex) {
        try {
            WebEngine webEngine = mapPreviewWebView.getEngine();

            if (!Session.getInstance().getProfileMapList().isEmpty()) {
                ProfileMapDtoFactory profileMapDtoFactory = new ProfileMapDtoFactory();
                ProfileMapDto profileMapDto = profileMapDtoFactory.newDto(
                        Session.getInstance().getProfileMapList().get(mapIndex)
                );

                mapNameValue.setText(profileMapDto.getMapDto().getKey());
                aliasTextField.setText(profileMapDto.getAlias());

                String yesText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.yes");
                String noText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.no");

                officialValue.setText(profileMapDto.getMapDto().isOfficial() ? yesText : noText);
                downloadedValue.setText(profileMapDto.getMapDto().isOfficial() ? yesText : ((CustomMapModDto) profileMapDto.getMapDto()).isDownloaded() ? yesText : noText);
                idWorkShopValue.setText(profileMapDto.getMapDto().isOfficial() ? StringUtils.EMPTY : String.valueOf(((CustomMapModDto) profileMapDto.getMapDto()).getIdWorkShop()));
                String unknownStr = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.unknown");
                String dateHourPattern = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.code.dateHourPattern");
                importationDateValue.setText(
                        profileMapDto.getImportedDate().format(DateTimeFormatter.ofPattern(dateHourPattern))
                );

                releaseDatePicker.setValue(profileMapDto.getReleaseDate());
                infoUrlTextField.setText(
                        StringUtils.isNotBlank(profileMapDto.getUrlInfo()) ? profileMapDto.getUrlInfo() : StringUtils.EMPTY
                );

                if (StringUtils.isNotBlank(profileMapDto.getUrlPhoto())) {
                    webEngine.load("file:" + installationFolder + profileMapDto.getUrlPhoto());
                    mapPreviewUrlTextField.setText("file:" + installationFolder + profileMapDto.getUrlPhoto());
                } else {
                    File file = new File(System.getProperty("user.dir") + "/external-images/no-photo.png");
                    if (file.exists()) {
                        webEngine.load("file:" + System.getProperty("user.dir") + "/external-images/no-photo.png");
                    } else {
                        webEngine.load("file:" + getClass().getResource("/external-images/no-photo.png").getPath());
                    }
                    mapPreviewUrlTextField.setText(StringUtils.EMPTY);
                }
            } else {
                webEngine.load("file:" + getClass().getResource("/external-images/no-photo.png").getPath());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void backButtonOnAction(){
        loadNewContent("/views/mapsEdition.fxml");
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
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void previousMapButtonOnAction(){
        if (profileMapIndex > 0) {
            profileMapIndex--;
        } else {
            profileMapIndex = Session.getInstance().getProfileMapList().size() - 1;
        }
        loadProfileMapData(profileMapIndex);
    }

    @FXML
    private void nextMapButtonOnAction(){
        if (profileMapIndex < Session.getInstance().getProfileMapList().size() - 1) {
            profileMapIndex++;
        } else {
            profileMapIndex = 0;
        }
        loadProfileMapData(profileMapIndex);
    }

}
