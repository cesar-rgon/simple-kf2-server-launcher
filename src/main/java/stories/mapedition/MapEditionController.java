package stories.mapedition;

import dtos.CustomMapModDto;
import dtos.PlatformProfileMapDto;
import dtos.factories.PlatformProfileMapDtoFactory;
import entities.PlatformProfileMap;
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
    private String languageCode;
    private int profileMapIndex;

    @FXML private WebView mapPreviewWebView;
    @FXML private Label mapNameValue;
    @FXML private ImageView mapPreviewUrlImg;
    @FXML private TextField mapPreviewUrlTextField;
    @FXML private Label officialValue;
    @FXML private ImageView downloadedImg;
    @FXML private Label downloadedValue;
    @FXML private ImageView platformImg;
    @FXML private Label platformLabel;
    @FXML private Label platformValue;
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

            String platformText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.platform");
            platformLabel.setText(platformText);
            loadTooltip(languageCode, "prop.tooltip.platform", platformImg, platformLabel, platformValue);

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
                        PlatformProfileMap edittedPlatformProfileMap = Session.getInstance().getProfileMapList().get(profileMapIndex);
                        if (StringUtils.isBlank(aliasTextField.getText())) {
                            Utils.warningDialog("The alias can not be empty", "Setting the alias as map name");
                            aliasTextField.setText(edittedPlatformProfileMap.getMap().getCode());
                        }

                        if (facade.updateMapSetAlias(edittedPlatformProfileMap, aliasTextField.getText())) {
                            edittedPlatformProfileMap.setAlias(aliasTextField.getText());
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
                        PlatformProfileMap edittedPlatformProfileMap = Session.getInstance().getProfileMapList().get(profileMapIndex);

                        String mapPreviewUrl = StringUtils.EMPTY;
                        if (StringUtils.isNotBlank(mapPreviewUrlTextField.getText())) {
                            if (mapPreviewUrlTextField.getText().startsWith("http") || mapPreviewUrlTextField.getText().startsWith("file:")) {
                                mapPreviewUrl = mapPreviewUrlTextField.getText();
                            } else if (mapPreviewUrlTextField.getText().startsWith("/KFGame")) {
                                mapPreviewUrl = "file:" + edittedPlatformProfileMap.getPlatform().getInstallationFolder() + mapPreviewUrlTextField.getText();
                            } else {
                                mapPreviewUrl = "file:" + mapPreviewUrlTextField.getText();
                            }
                        }

                        if (facade.updateMapSetUrlPhoto(edittedPlatformProfileMap, mapPreviewUrl)) {
                            edittedPlatformProfileMap.setUrlPhoto(mapPreviewUrl);
                            mapPreviewUrlTextField.setText(mapPreviewUrl);
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
                        PlatformProfileMap edittedPlatformProfileMap = Session.getInstance().getProfileMapList().get(profileMapIndex);

                        if (facade.updateMapSetInfoUrl(edittedPlatformProfileMap, infoUrlTextField.getText())) {
                            edittedPlatformProfileMap.setUrlInfo(infoUrlTextField.getText());
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
                        PlatformProfileMap edittedPlatformProfileMap = Session.getInstance().getProfileMapList().get(profileMapIndex);

                        if (facade.updateMapSetReleaseDate(edittedPlatformProfileMap, releaseDatePicker.getValue())) {
                            edittedPlatformProfileMap.setReleaseDate(
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
                PlatformProfileMapDtoFactory platformProfileMapDtoFactory = new PlatformProfileMapDtoFactory();
                PlatformProfileMapDto platformProfileMapDto = platformProfileMapDtoFactory.newDto(
                        Session.getInstance().getProfileMapList().get(mapIndex)
                );

                mapNameValue.setText(platformProfileMapDto.getMapDto().getKey());
                aliasTextField.setText(platformProfileMapDto.getAlias());

                String yesText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.yes");
                String noText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.label.no");

                officialValue.setText(platformProfileMapDto.getMapDto().isOfficial() ? yesText : noText);
                downloadedValue.setText(platformProfileMapDto.getMapDto().isOfficial() ? yesText : platformProfileMapDto.isDownloaded() ? yesText : noText);
                platformValue.setText(platformProfileMapDto.getPlatformDto().getValue());
                idWorkShopValue.setText(platformProfileMapDto.getMapDto().isOfficial() ? StringUtils.EMPTY : String.valueOf(((CustomMapModDto) platformProfileMapDto.getMapDto()).getIdWorkShop()));
                String unknownStr = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.unknown");
                String dateHourPattern = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.code.dateHourPattern");
                importationDateValue.setText(
                        platformProfileMapDto.getImportedDate().format(DateTimeFormatter.ofPattern(dateHourPattern))
                );

                releaseDatePicker.setValue(platformProfileMapDto.getReleaseDate());
                infoUrlTextField.setText(
                        StringUtils.isNotBlank(platformProfileMapDto.getUrlInfo()) ? platformProfileMapDto.getUrlInfo() : StringUtils.EMPTY
                );

                String urlPhoto = StringUtils.EMPTY;
                if (StringUtils.isNotBlank(platformProfileMapDto.getUrlPhoto())) {
                    if (platformProfileMapDto.getUrlPhoto().startsWith("http") || platformProfileMapDto.getUrlPhoto().startsWith("file:")) {
                        urlPhoto = platformProfileMapDto.getUrlPhoto();
                    } else if (platformProfileMapDto.getUrlPhoto().startsWith("/KFGame")) {
                        urlPhoto = "file:" + platformProfileMapDto.getPlatformDto().getInstallationFolder() + platformProfileMapDto.getUrlPhoto();
                    } else {
                        urlPhoto = "file:" + platformProfileMapDto.getUrlPhoto();
                    }
                }

                if (StringUtils.isNotBlank(urlPhoto)) {
                    webEngine.load(urlPhoto);
                    mapPreviewUrlTextField.setText(urlPhoto);
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
