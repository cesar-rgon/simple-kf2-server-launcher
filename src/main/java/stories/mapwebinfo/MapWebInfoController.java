package stories.mapwebinfo;

import dtos.CustomMapModDto;
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
import start.MainApplication;
import stories.addplatformprofilestomap.AddPlatformProfilesToMapFacadeResult;
import stories.createcustommapfromworkshop.CreateCustomMapFromWorkshopFacadeResult;
import stories.getplatformprofilelistwithoutmap.GetPlatformProfileListWithoutMapFacadeResult;
import stories.getplatformprofilelistwithoutmap.GetPlatformProfileListWithoutMapModelContext;
import utils.Utils;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MapWebInfoController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MapWebInfoController.class);
    private MapWebInfoManagerFacade facade;
    private Long idWorkShop;
    private String mapName;
    private String strUrlMapImage;
    protected String languageCode;

    @FXML private WebView mapInfoWebView;
    @FXML private Label mapIdWorkshop;
    @FXML private Button addMap;
    @FXML private Label alreadyInLauncher;
    @FXML private Button backButton;
    @FXML private ProgressIndicator progressIndicator;

    public MapWebInfoController() {
        super();
        facade = new MapWebInfoManagerFacadeImpl(new GetPlatformProfileListWithoutMapModelContext(null));
        idWorkShop = null;
        mapName = null;
        strUrlMapImage = null;
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            languageCode = facade.findPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            if (Session.getInstance().getPpm() != null) {
                if (Session.getInstance().getPpm().getMapDto().isOfficial()) {
                    mapIdWorkshop.setText("Map name: " + Session.getInstance().getPpm().getMapDto().getKey());
                } else {
                    mapIdWorkshop.setText("id WorkShop: " + ((CustomMapModDto) Session.getInstance().getPpm().getMapDto()).getIdWorkShop());
                }
                mapInfoWebView.getEngine().load(Session.getInstance().getPpm().getUrlInfo());
            } else {
                mapIdWorkshop.setText("");
                mapInfoWebView.getEngine().load("https://steamcommunity.com/app/232090/workshop/");
            }

            String backButtonText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.backMapsPage");
            backButton.setText(backButtonText);
            String addMapText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.addNewMap");
            addMap.setText(addMapText);
            String alreadyInLauncherText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.alreadyInLauncher");
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
                if (Session.getInstance().getPpm() == null || !Session.getInstance().getPpm().getMapDto().isOfficial()) {
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
                                if (idWorkShop != null) {
                                    String[] contentArray = title.getTextContent().split("::");
                                    mapName = contentArray[1];
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
                            mapIdWorkshop.setText("id WorkShop: " + idWorkShop);
                            GetPlatformProfileListWithoutMapModelContext modelContext = new GetPlatformProfileListWithoutMapModelContext(
                                    idWorkShop
                            );
                            facade = new MapWebInfoManagerFacadeImpl(modelContext);
                            GetPlatformProfileListWithoutMapFacadeResult result = facade.execute();
                            List<PlatformProfile> platformProfilesWithoutMap = result.getPlatformProfileListWithoutMap();
                            if (idWorkShop != null && (platformProfilesWithoutMap == null || platformProfilesWithoutMap.isEmpty())) {
                                addMap.setVisible(false);
                                alreadyInLauncher.setVisible(true);
                            } else {
                                addMap.setVisible(StringUtils.isNotBlank(urlWorkShop) && idWorkShop != null && platformProfilesWithoutMap != null && !platformProfilesWithoutMap.isEmpty());
                                alreadyInLauncher.setVisible(false);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("Error in loading process of new page from Steam's WorkShop\nSee stacktrace for more details", e);
                        mapIdWorkshop.setText("");
                        addMap.setVisible(false);
                        alreadyInLauncher.setVisible(false);
                    }
                }
            }
        });
    }

    @FXML
    private void backButtonOnAction(){
        loadNewContent("/views/maps.fxml");
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
                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installationFolderNotValid");
                Utils.warningDialog(headerText, contentText);
                return;
            }

            GetPlatformProfileListWithoutMapModelContext modelContext = new GetPlatformProfileListWithoutMapModelContext(
                    idWorkShop
            );
            facade = new MapWebInfoManagerFacadeImpl(modelContext);
            GetPlatformProfileListWithoutMapFacadeResult getPlatformProfileListWithoutMapFacadeResult = facade.execute();
            List<PlatformProfile> platformProfileListWithoutMap = getPlatformProfileListWithoutMapFacadeResult.getPlatformProfileListWithoutMap();

            List<PlatformProfileToDisplay> selectedPlatformProfiles = facade.getSelectedPlatformProfileList(
                    platformProfileListWithoutMap
            );

            StringBuffer success = new StringBuffer();
            StringBuffer errors = new StringBuffer();
            if (selectedPlatformProfiles != null && !selectedPlatformProfiles.isEmpty()) {
                List<String> selectedProfileNameList = selectedPlatformProfiles.stream().map(p -> p.getProfileName()).collect(Collectors.toList());
                CustomMapModDto mapModInDataBase = facade.findMapOrModByIdWorkShop(idWorkShop);

                int countPlatformsProfilesForMap = 0;
                if (mapModInDataBase != null) {
                    countPlatformsProfilesForMap = facade.countPlatformsProfilesForMap(mapModInDataBase.getKey());
                }

                for (String selectedProfileName: selectedProfileNameList) {
                    try {
                        Optional<String> platformNameOptional = selectedPlatformProfiles.stream().
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
                            CreateCustomMapFromWorkshopFacadeResult result = facade.createNewCustomMapFromWorkshop(
                                    platformNameList,
                                    idWorkShop,
                                    mapName,
                                    strUrlMapImage,
                                    profileNameList
                            );
                            mapModInDataBase = result.getCustomMapDto();
                            success.append(result.getSuccess());
                            errors.append(result.getErrors());

                            if (mapModInDataBase == null) {
                                throw new RuntimeException("Error adding map/mod with name " + mapName);
                            }
                        } else {
                            AddPlatformProfilesToMapFacadeResult result = facade.addPlatformProfilesToMap(
                                    platformNameList,
                                    mapModInDataBase.getKey(),
                                    strUrlMapImage,
                                    profileNameList
                            );
                            success.append(result.getSuccess());
                            errors.append(result.getErrors());

                        }
                    } catch (Exception e) {
                        String message = "Error adding map/mod to the launcher";
                        logger.error(message, e);
                        Utils.errorDialog(message, e);
                    }
                }

                int countPlatformsProfilesForMapAfterProcess = 0;
                if (mapModInDataBase != null) {
                    countPlatformsProfilesForMapAfterProcess = facade.countPlatformsProfilesForMap(mapModInDataBase.getKey());
                }
                int countNewPlatformsProfilesForMap = countPlatformsProfilesForMapAfterProcess - countPlatformsProfilesForMap;

                if (platformProfileListWithoutMap.size() - countNewPlatformsProfilesForMap == 0) {
                    addMap.setVisible(false);
                    alreadyInLauncher.setVisible(true);
                }

                if (StringUtils.isNotBlank(success)) {
                    String successHeaderText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.OperationDone");
                    Utils.infoDialog(successHeaderText, success.toString());
                }

                if (StringUtils.isNotBlank(errors)) {
                    String errorsHeaderText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                    Utils.infoDialog(errorsHeaderText, errors.toString());
                }
            }
        } catch (Exception e) {
            String message = "Error adding map/mod to the launcher";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }
}
