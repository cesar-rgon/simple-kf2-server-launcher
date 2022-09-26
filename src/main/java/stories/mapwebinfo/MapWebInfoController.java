package stories.mapwebinfo;

import dtos.CustomMapModDto;
import dtos.ProfileDto;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pojos.PlatformProfile;
import pojos.PlatformProfileToDisplay;
import pojos.enums.EnumPlatform;
import pojos.session.Session;
import services.ProfileService;
import services.PropertyService;
import services.PropertyServiceImpl;
import start.MainApplication;
import utils.Utils;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MapWebInfoController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MapWebInfoController.class);
    private final MapWebInfoFacade facade;
    private Long idWorkShop;
    private String mapName;
    private String strUrlMapImage;
    private final PropertyService propertyService;
    protected String languageCode;

    @FXML private WebView mapInfoWebView;
    @FXML private Label mapNameLabel;
    @FXML private Button addMap;
    @FXML private Label alreadyInLauncher;
    @FXML private Button backButton;
    @FXML private ProgressIndicator progressIndicator;

    public MapWebInfoController() {
        super();
        facade = new MapWebInfoFacadeImpl();
        propertyService = new PropertyServiceImpl();
        idWorkShop = null;
        mapName = null;
        strUrlMapImage = null;
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            if (Session.getInstance().getMap() != null) {
                mapNameLabel.setText(Session.getInstance().getMap().getKey());
                mapInfoWebView.getEngine().load(Session.getInstance().getMap().getUrlInfo());
            } else {
                mapNameLabel.setText("");
                mapInfoWebView.getEngine().load("https://steamcommunity.com/app/232090/workshop/");
            }

            String backButtonText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.backMapsPage");
            backButton.setText(backButtonText);
            String addMapText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.addNewMap");
            addMap.setText(addMapText);
            String alreadyInLauncherText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.alreadyInLauncher");
            alreadyInLauncher.setText(alreadyInLauncherText);

            // Put black color for background of the browser's page
            Field f = mapInfoWebView.getEngine().getClass().getDeclaredField("page");
            f.setAccessible(true);
            com.sun.webkit.WebPage page = (com.sun.webkit.WebPage) f.get(mapInfoWebView.getEngine());
            page.setBackgroundColor((new java.awt.Color(0.0f, 0.0f, 0.0f, 1f)).getRGB());

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }

        mapInfoWebView.getEngine().documentProperty().addListener(new ChangeListener<Document>() {
            @Override
            public void changed(ObservableValue<? extends Document> ov, Document oldDoc, Document doc) {
                if (doc == null) {
                    return;
                }
                progressIndicator.setVisible(false);
                if (Session.getInstance().getMap() == null || !Session.getInstance().getMap().isOfficial()) {
                    try {
                        NodeList titleList = doc.getElementsByTagName("title");
                        String urlWorkShop = doc.getDocumentURI();
                        if (StringUtils.isNotBlank(urlWorkShop)) {
                            String[] array = urlWorkShop.split("=");
                            idWorkShop = null;
                            mapName = null;
                            strUrlMapImage = null;
                            if (array != null && array.length > 1) {
                                String[] arrayTwo = array[1].split("&");
                                idWorkShop = Long.parseLong(arrayTwo[0]);
                            }
                            if (titleList != null && titleList.getLength() > 0) {
                                Node title = titleList.item(0);
                                if (title.getTextContent().toUpperCase().startsWith("STEAM WORKSHOP")) {
                                    mapName = title.getTextContent().replace("Steam Workshop::", "");
                                    NodeList linkList = doc.getElementsByTagName("link");
                                    for (int i=0; i < linkList.getLength(); i++) {
                                        Node link = linkList.item(i);
                                        if (link.hasAttributes()) {
                                            NamedNodeMap linkAttrList = link.getAttributes();
                                            if ("image_src".equalsIgnoreCase(linkAttrList.getNamedItem("rel").getTextContent())) {
                                                strUrlMapImage = linkAttrList.getNamedItem("href").getTextContent();
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            mapNameLabel.setText(mapName);
                            List<PlatformProfile> platformProfilesWithoutMap = facade.getPlatformProfileListWithoutMap(idWorkShop);
                            if (idWorkShop != null && (platformProfilesWithoutMap == null || platformProfilesWithoutMap.isEmpty())) {
                                addMap.setVisible(false);
                                alreadyInLauncher.setVisible(true);
                            } else {
                                addMap.setVisible(StringUtils.isNotBlank(urlWorkShop) && StringUtils.isNotBlank(mapName) && platformProfilesWithoutMap != null && !platformProfilesWithoutMap.isEmpty());
                                alreadyInLauncher.setVisible(false);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("Error in loading process of new page from Steam's WorkShop\nSee stacktrace for more details", e);
                        mapNameLabel.setText(null);
                        addMap.setVisible(false);
                        alreadyInLauncher.setVisible(false);
                    }
                }
            }
        });
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
    private void addMapOnAction() {
        try {
            if (!facade.isCorrectInstallationFolder(EnumPlatform.STEAM.name()) && !facade.isCorrectInstallationFolder(EnumPlatform.EPIC.name())) {
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installationFolderNotValid");
                Utils.warningDialog(headerText, contentText);
                return;
            }

            List<PlatformProfile> platformProfileListWithoutMap = facade.getPlatformProfileListWithoutMap(idWorkShop);
            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectProfiles");

            List<ProfileDto> fullProfileList = facade.getAllProfileList();
            List<String> fullProfileNameList = fullProfileList.stream().map(ProfileDto::getName).collect(Collectors.toList());

            List<PlatformProfileToDisplay> selectedProfiles = Utils.selectPlatformProfilesDialog(headerText + ":", platformProfileListWithoutMap, fullProfileNameList);
            if (selectedProfiles != null && !selectedProfiles.isEmpty()) {
                List<String> selectedProfileNameList = selectedProfiles.stream().map(p -> p.getProfileName()).collect(Collectors.toList());
                CustomMapModDto mapModInDataBase = facade.findMapOrModByIdWorkShop(idWorkShop);

                for (String selectedProfileName: selectedProfileNameList) {
                    try {
                        Optional<String> platformNameOptional = selectedProfiles.stream().
                                filter(p -> p.getProfileName().equals(selectedProfileName)).
                                map(PlatformProfileToDisplay::getPlatformName).
                                findFirst();

                        if (!platformNameOptional.isPresent()) {
                            logger.error("The platform could not be found for the profile " + selectedProfileName);
                            continue;
                        }

                        List<String> platformNameList = new ArrayList<String>();
                        switch (platformNameOptional.get()) {
                            case "Steam":
                                if (facade.isCorrectInstallationFolder(EnumPlatform.STEAM.name())) {
                                    platformNameList.add(EnumPlatform.STEAM.name());
                                }
                                break;

                            case "Epic Games":
                                if (facade.isCorrectInstallationFolder(EnumPlatform.EPIC.name())) {
                                    platformNameList.add(EnumPlatform.EPIC.name());
                                }
                                break;

                            case "All platforms":
                                if (facade.isCorrectInstallationFolder(EnumPlatform.STEAM.name())) {
                                    platformNameList.add(EnumPlatform.STEAM.name());
                                }
                                if (facade.isCorrectInstallationFolder(EnumPlatform.EPIC.name())) {
                                    platformNameList.add(EnumPlatform.EPIC.name());
                                }
                                break;
                        }

                        List<String> profileNameList = new ArrayList<String>();
                        profileNameList.add(selectedProfileName);

                        if (mapModInDataBase == null) {
                            CustomMapModDto customMap = facade.createNewCustomMapFromWorkshop(
                                    platformNameList,
                                    idWorkShop,
                                    mapName,
                                    strUrlMapImage,
                                    profileNameList
                            );

                            if (customMap != null) {
                                if (platformProfileListWithoutMap.stream().map(PlatformProfile::getProfile).distinct().collect(Collectors.toList()).size() - selectedProfiles.size() == 0) {
                                    addMap.setVisible(false);
                                    alreadyInLauncher.setVisible(true);
                                }
                                headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.OperationDone");
                                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapName");
                                Utils.infoDialog(headerText, contentText + ": " + mapName + "\nURL/Id WorkShop: " + idWorkShop);
                            } else {
                                headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapName");
                                Utils.warningDialog(headerText, contentText + ": " + mapName + "\nURL/Id WorkShop: " + idWorkShop);
                            }

                        } else {

                            facade.addProfilesToMap(
                                    platformNameList,
                                    mapModInDataBase.getKey(),
                                    profileNameList
                            );
                            if (platformProfileListWithoutMap.stream().map(PlatformProfile::getProfile).distinct().collect(Collectors.toList()).size() - selectedProfiles.size() == 0) {
                                addMap.setVisible(false);
                                alreadyInLauncher.setVisible(true);
                            }
                            headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.OperationDone");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapName");
                            Utils.infoDialog(headerText, contentText + ": " + mapName + "\nURL/Id WorkShop: " + idWorkShop);
                        }
                    } catch (Exception e) {
                        String message = "Error adding map/mod to the launcher";
                        logger.error(message, e);
                        Utils.errorDialog(message, e);
                    }
                }
            }
        } catch (Exception e) {
            String message = "Error adding map/mod to the launcher";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }
}
