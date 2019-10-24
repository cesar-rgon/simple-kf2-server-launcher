package stories.mapsedition;

import dtos.MapDto;
import dtos.ProfileDto;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.AddMapsToProfile;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import pojos.session.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import start.MainApplication;
import utils.Utils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class MapsEditionController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MapsEditionController.class);

    private final MapsEditionFacade facade;
    private final PropertyService propertyService;
    protected String languageCode;
    private String installationFolder;
    private List<MapDto> mapList;
    private boolean selectMaps;

    @FXML private Slider mapsSlider;
    @FXML private FlowPane officialMapsFlowPane;
    @FXML private FlowPane customMapsFlowPane;
    @FXML private TextField searchMaps;
    @FXML private Button addNewMaps;
    @FXML private Button searchInWorkShop;
    @FXML private Button removeMaps;
    @FXML private Button selectAllMaps;
    @FXML private Button importMapsFromServer;
    @FXML private TabPane mapsModsTabPane;
    @FXML private Tab customMapsModsTab;
    @FXML private Tab officialMapsTab;
    @FXML private Label sliderLabel;
    @FXML private ImageView searchImg;
    @FXML private ComboBox<ProfileDto> profileSelect;
    @FXML private Label profileLabel;

    public MapsEditionController() {
        super();
        facade = new MapsEditionFacadeImpl();
        propertyService = new PropertyServiceImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            selectMaps = true;
            installationFolder = facade.findConfigPropertyValue("prop.config.installationFolder");

            String profileLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.profileLowercase");
            profileLabel.setText(profileLabelText);

            ObservableList<ProfileDto> profileOptions = facade.listAllProfiles();
            profileSelect.setItems(profileOptions);
            if (!profileOptions.isEmpty()) {
                profileSelect.setValue(Session.getInstance().getMapsProfile() != null?
                        Session.getInstance().getMapsProfile():
                        Session.getInstance().getActualProfile() != null?
                                Session.getInstance().getActualProfile():
                                profileSelect.getItems().get(0));

                mapList = facade.getMapsFromProfile(profileSelect.getValue().getName());
                for (MapDto map: mapList) {
                    GridPane gridpane = createMapGridPane(map);
                    if (map.isOfficial()) {
                        officialMapsFlowPane.getChildren().add(gridpane);
                    } else {
                        customMapsFlowPane.getChildren().add(gridpane);
                    }
                }
            } else {
                profileSelect.setValue(null);
                mapList = null;
            }
            Session.getInstance().setMapsProfile(profileSelect.getValue());

            officialMapsTab.setGraphic(new Label("(" + officialMapsFlowPane.getChildren().size() + ")"));
            String officialMapsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.officiaslMaps");
            officialMapsTab.setText(officialMapsTabText);

            customMapsModsTab.setGraphic(new Label("(" + customMapsFlowPane.getChildren().size() + ")"));
            String customMapsModsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.customMaps");
            customMapsModsTab.setText(customMapsModsTabText);

            Tooltip searchTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.searchMaps"));
            searchMaps.setTooltip(searchTooltip);
            Tooltip.install(searchImg, searchTooltip);

            String addNewMapsText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.addMaps");
            addNewMaps.setText(addNewMapsText);
            addNewMaps.setTooltip(new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.addMaps")));

            String searchInWorkShopText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.searchInWorkShop");
            searchInWorkShop.setText(searchInWorkShopText);
            searchInWorkShop.setTooltip(new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.searchInWorkShop")));

            String removeMapsText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.removeMaps");
            removeMaps.setText(removeMapsText);
            removeMaps.setTooltip(new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.removeMaps")));

            String selectAllMapsText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.selectMaps");
            selectAllMaps.setText(selectAllMapsText);
            selectAllMaps.setTooltip(new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.selectAllMaps")));

            String importMapsFromServerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.importMaps");
            importMapsFromServer.setText(importMapsFromServerText);
            importMapsFromServer.setTooltip(new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.importMaps")));

            String sliderLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.slider");
            sliderLabel.setText(sliderLabelText);
            Tooltip sliderTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.slide"));
            sliderLabel.setTooltip(sliderTooltip);
            Double sliderColumns = Double.parseDouble(facade.findConfigPropertyValue("prop.config.mapSliderValue"));
            mapsSlider.setValue(sliderColumns);
            mapsSlider.setTooltip(sliderTooltip);
            mapsSliderOnMouseClicked();

            MainApplication.getPrimaryStage().widthProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                    mapsSliderOnMouseClicked();
                }
            });
        } catch (Exception e) {
            String message = "Error getting map list";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }


    private GridPane createMapGridPane(MapDto map) {
        Label mapNameLabel = new Label(map.getKey());
        Image image = null;
        if (facade.isCorrectInstallationFolder(installationFolder) && StringUtils.isNotBlank(map.getUrlPhoto())) {
            image = new Image("file:" + installationFolder + "/" + map.getUrlPhoto());
        } else {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/no-photo.png");
            image = new Image(inputStream);
        }
        ImageView mapPreview = new ImageView(image);
        mapPreview.setPreserveRatio(false);
        Double width = getWidthGridPaneByNumberOfColums();
        mapPreview.setFitWidth(width);
        mapPreview.setFitHeight(width/2);
        if (StringUtils.isNotBlank(map.getUrlInfo())) {
            mapPreview.setCursor(Cursor.HAND);
            mapPreview.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Session.getInstance().setMap(map);
                    loadNewContent("/views/mapWebInfo.fxml");
                    event.consume();
                }
            });
        }
        GridPane gridpane = new GridPane();
        gridpane.add(mapPreview, 1, 1);
        GridPane.setColumnSpan(mapPreview, 2);
        gridpane.add(new CheckBox(), 1, 2);
        gridpane.add(mapNameLabel, 2, 2);
        mapNameLabel.setMinHeight(20);
        mapNameLabel.setMaxWidth(mapPreview.getFitWidth() - 25);
        mapNameLabel.setAlignment(Pos.BOTTOM_CENTER);

        int rowIndex = 3;
        if (!map.isDownloaded()) {
            String message = "";
            try {
                message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.startServer");
            } catch (Exception e) {
                message = "Start server to download it";
            }
            Label warningMessage = new Label(message);
            warningMessage.setStyle("-fx-text-fill: yellow;");
            GridPane.setColumnSpan(warningMessage, 2);
            gridpane.add(warningMessage,1, rowIndex);
            warningMessage.setMaxWidth(mapPreview.getFitWidth());
            warningMessage.setAlignment(Pos.CENTER);
            rowIndex++;
        }
        if (map.getMod() != null && map.getMod()) {
            Label warningMessage = new Label("[MOD]");
            warningMessage.setStyle("-fx-text-fill: yellow;");
            GridPane.setColumnSpan(warningMessage, 2);
            gridpane.add(warningMessage,1, rowIndex);
            GridPane.setHalignment(warningMessage, HPos.CENTER);
            rowIndex++;
        }
        StringBuffer tooltipText = new StringBuffer();
        if (!map.isOfficial()) {
            tooltipText.append("id WorkShop: ").append(map.getIdWorkShop());
        }
        if (StringUtils.isNotBlank(map.getUrlPhoto())) {
            if (StringUtils.isNotBlank(tooltipText)) {
                tooltipText.append("\n");
            }
            String message = "";
            try {
                message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.urlInfo");
            } catch (Exception e) {
                message = "Url info";
            }
            tooltipText.append(message).append(": ").append(map.getUrlInfo());
        }
        if (map.isDownloaded()) {
            if (StringUtils.isNotBlank(tooltipText)) {
                tooltipText.append("\n");
            }
            String message = "";
            try {
                message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.photoLocation");
            } catch (Exception e) {
                message = "Photo location";
            }
            tooltipText.append(message).append(": ").append(installationFolder).append(map.getUrlPhoto());
        }
        if (StringUtils.isNotBlank(tooltipText)) {
            Tooltip tooltip = new Tooltip(tooltipText.toString());
            Tooltip.install(gridpane, tooltip);
        }
        return gridpane;
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

    private Double getWidthGridPaneByNumberOfColums() {
        return (MainApplication.getPrimaryStage().getWidth() - (50 * mapsSlider.getValue()) - 90) / mapsSlider.getValue();
    }

    private void resizeGridPane(GridPane gridPane) {
        ImageView mapPreview = (ImageView) gridPane.getChildren().get(0);
        Label mapNameLabel = (Label) gridPane.getChildren().get(2);
        Double width = getWidthGridPaneByNumberOfColums();
        mapPreview.setFitWidth(width);
        mapPreview.setFitHeight(width/2);
        mapNameLabel.setMaxWidth(mapPreview.getFitWidth() - 25);
        if (gridPane.getChildren().size() > 3) {
            Label warningMessage = (Label) gridPane.getChildren().get(3);
            warningMessage.setMaxWidth(mapPreview.getFitWidth());
            warningMessage.setAlignment(Pos.CENTER);
        }
    }

    @FXML
    private void mapsSliderOnMouseClicked() {
        for (int index = 0; index < officialMapsFlowPane.getChildren().size(); index++) {
            GridPane gridPane = (GridPane)officialMapsFlowPane.getChildren().get(index);
            resizeGridPane(gridPane);
        }
        for (int index = 0; index < customMapsFlowPane.getChildren().size(); index++) {
            GridPane gridPane = (GridPane)customMapsFlowPane.getChildren().get(index);
            resizeGridPane(gridPane);
        }
        try {
            facade.setConfigPropertyValue("prop.config.mapSliderValue", String.valueOf(mapsSlider.getValue()));
        } catch (Exception e) {
            String message = "Error setting maps slider value in config.properties file";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    @FXML
    private void searchMapsKeyReleased() {
        officialMapsFlowPane.getChildren().clear();
        customMapsFlowPane.getChildren().clear();
        for (MapDto map: mapList) {
            String mapLabel = StringUtils.upperCase(map.getKey());
            if (mapLabel.contains(StringUtils.upperCase(searchMaps.getText()))) {
                GridPane gridpane = createMapGridPane(map);
                if (map.isOfficial()) {
                    officialMapsFlowPane.getChildren().add(gridpane);
                } else {
                    customMapsFlowPane.getChildren().add(gridpane);
                }
            }
        }
    }

    @FXML
    private void addNewMapsOnAction() {
        try {
            if (!facade.isCorrectInstallationFolder(installationFolder)) {
                logger.warn("Installation folder can not be empty and can not contains whitespaces. Define in Install/Update section: " + installationFolder);
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installationFolderNotValid");
                Utils.warningDialog(headerText, contentText);
                return;
            }
            if (profileSelect.getValue() == null) {
                logger.warn("No profiles defined. Add new maps aborted");
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profileNotEmpty");
                Utils.warningDialog(headerText, contentText);
                return;
            }

            if (officialMapsTab.isSelected()) {
                SingleSelectionModel<Tab> selectionModel = mapsModsTabPane.getSelectionModel();
                selectionModel.select(customMapsModsTab);
            }
            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.addCustomMaps");
            List<AddMapsToProfile> addMapsToProfileList = Utils.defineCustomMapsToAddPerProfile(headerText, profileSelect.getItems());

            if (addMapsToProfileList != null && !addMapsToProfileList.isEmpty()) {
                StringBuffer success = new StringBuffer();
                StringBuffer errors = new StringBuffer();

                for (AddMapsToProfile addMapsToProfile: addMapsToProfileList) {
                    if (StringUtils.isNotBlank(addMapsToProfile.getMapList())) {
                        String[] idUrlWorkShopArray = addMapsToProfile.getMapList().replaceAll(" ", "").split(",");

                        for (int i = 0; i < idUrlWorkShopArray.length; i++) {
                            String profileNameMessage = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.profileName");
                            try {
                                Long idWorkShop = null;
                                if (idUrlWorkShopArray[i].contains("http")) {
                                    String[] array = idUrlWorkShopArray[i].split("=");
                                    idWorkShop = Long.parseLong(array[1]);
                                } else {
                                    idWorkShop = Long.parseLong(idUrlWorkShopArray[i]);
                                }
                                MapDto mapModInDataBase = facade.findMapOrModByIdWorkShop(idWorkShop);
                                if (mapModInDataBase == null) {
                                    // The map is not in dabatabase, create a new map for actual profile
                                    List<String> profileNameList = new ArrayList<String>();
                                    profileNameList.add(addMapsToProfile.getProfileName());
                                    MapDto customMap = facade.createNewCustomMapFromWorkshop(idWorkShop, installationFolder, false, null, profileNameList);
                                    if (customMap != null) {
                                        if (addMapsToProfile.getProfileName().equalsIgnoreCase(profileSelect.getValue().getName())) {
                                            mapList.add(customMap);
                                            GridPane gridpane = createMapGridPane(customMap);
                                            customMapsFlowPane.getChildren().add(gridpane);
                                        }
                                        String mapNameMessage = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapName");
                                        success.append(profileNameMessage + ": ").append(addMapsToProfile.getProfileName()).append(" - " + mapNameMessage + ": ").append(customMap.getKey()).append(" - id WorkShop: ").append(customMap.getIdWorkShop()).append("\n");
                                    } else {
                                        errors.append(profileNameMessage + ": ").append(addMapsToProfile.getProfileName()).append(" - url/id WorkShop: ").append(idUrlWorkShopArray[i]).append("\n");
                                    }
                                } else {
                                    List<String> profileList = new ArrayList<String>();
                                    profileList.add(addMapsToProfile.getProfileName());
                                    if (facade.addProfilesToMap(mapModInDataBase.getKey(), profileList)) {
                                        if (addMapsToProfile.getProfileName().equalsIgnoreCase(profileSelect.getValue().getName())) {
                                            mapList.add(mapModInDataBase);
                                            GridPane gridpane = createMapGridPane(mapModInDataBase);
                                            customMapsFlowPane.getChildren().add(gridpane);
                                        }
                                        String mapNameMessage = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapName");
                                        success.append(profileNameMessage + ": ").append(addMapsToProfile.getProfileName()).append(" - " + mapNameMessage + ": ").append(mapModInDataBase.getKey()).append(" - id WorkShop: ").append(mapModInDataBase.getIdWorkShop()).append("\n");
                                    } else {
                                        errors.append(profileNameMessage + ": ").append(addMapsToProfile.getProfileName()).append(" - url/id WorkShop: ").append(idUrlWorkShopArray[i]).append("\n");
                                    }
                                }
                            } catch (Exception e) {
                                errors.append(profileNameMessage + ": ").append(addMapsToProfile.getProfileName()).append(" - url/id WorkShop: ").append(idUrlWorkShopArray[i]).append("\n");
                            }
                        }
                    }
                }

                if (StringUtils.isNotBlank(success)) {
                    customMapsModsTab.setGraphic(new Label("(" + customMapsFlowPane.getChildren().size() + ")"));
                    String message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapsAdded");
                    Utils.infoDialog(message + ":", success.toString());
                } else {
                    headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapsNotAdded");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.checkUrlIdWorkshop");
                    Utils.infoDialog(headerText, contentText);
                }
                if (StringUtils.isNotBlank(errors)) {
                    logger.warn("Error adding next maps/mods to the launcher: " + errors.toString());
                    String message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapsNotAddedError");
                    Utils.warningDialog(message + ":", errors.toString());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void removeMapsOnAction() {
        try {
            if (!facade.isCorrectInstallationFolder(installationFolder)) {
                logger.warn("Installation folder can not be empty and can not contains whitespaces. Define in Install/Update section: " + installationFolder);
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installationFolderNotValid");
                Utils.warningDialog(headerText, contentText);
                return;
            }
            List<Node> removeList = new ArrayList<Node>();
            StringBuffer message = new StringBuffer();
            ObservableList<Node> officialNodes = officialMapsFlowPane.getChildren();
            ObservableList<Node> customNodes = customMapsFlowPane.getChildren();

            for (Node node : officialNodes) {
                GridPane gridpane = (GridPane) node;
                CheckBox checkbox = (CheckBox) gridpane.getChildren().get(1);
                Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                if (checkbox.isSelected()) {
                    removeList.add(gridpane);
                    message.append(mapNameLabel.getText()).append("\n");
                }
            }

            for (Node node : customNodes) {
                GridPane gridpane = (GridPane) node;
                CheckBox checkbox = (CheckBox) gridpane.getChildren().get(1);
                Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                if (checkbox.isSelected()) {
                    removeList.add(gridpane);
                    message.append(mapNameLabel.getText()).append("\n");
                }
            }
            if (removeList.isEmpty()) {
                logger.warn("No selected maps/mods to delete. You must select at least one item to be deleted");
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapsNotSelected");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectItems");
                Utils.warningDialog(headerText, contentText);

            } else {
                String question = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.deleteMapsQuestion");
                Optional<ButtonType> result = Utils.questionDialog(question, message.toString());
                if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                    List<MapDto> mapsToRemove = new ArrayList<MapDto>();
                    StringBuffer errors = new StringBuffer();
                    for (Node node : removeList) {
                        try {
                            GridPane gridpane = (GridPane) node;
                            Label mapNameLabel = (Label) gridpane.getChildren().get(2);

                            if (profileSelect.getValue().getMap() != null && mapNameLabel.getText().equalsIgnoreCase(profileSelect.getValue().getMap().getKey())) {
                                facade.unselectProfileMap(profileSelect.getValue().getName());
                            }
                            MapDto map = facade.deleteMapFromProfile(mapNameLabel.getText(), profileSelect.getValue().getName(), installationFolder);
                            if (map != null) {
                                mapsToRemove.add(map);
                                if (map.isOfficial()) {
                                    officialMapsFlowPane.getChildren().remove(gridpane);
                                } else {
                                    customMapsFlowPane.getChildren().remove(gridpane);
                                }
                            } else {
                                errors.append(mapNameLabel.getText()).append("\n");
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                            Utils.errorDialog(e.getMessage(), e);
                        }
                    }
                    if (!mapsToRemove.isEmpty()) {
                        try {
                            mapList.clear();
                            mapList = facade.getMapsFromProfile(profileSelect.getValue().getName());
                            officialMapsTab.setGraphic(new Label("(" + officialMapsFlowPane.getChildren().size() + ")"));
                            customMapsModsTab.setGraphic(new Label("(" + customMapsFlowPane.getChildren().size() + ")"));
                        } catch (SQLException e) {
                            logger.error(e.getMessage(), e);
                            Utils.errorDialog(e.getMessage(), e);
                        }
                    }
                    if (StringUtils.isNotBlank(errors.toString())) {
                        logger.warn("Next maps/mods could not be deleted: " + errors.toString());
                        String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapsNotDeleted");
                        Utils.warningDialog(headerText, errors.toString());
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void importMapsFromServerOnAction() {
        try {
            if (!facade.isCorrectInstallationFolder(installationFolder)) {
                logger.warn("Installation folder can not be empty and can not contains whitespaces. Define in Install/Update section: " + installationFolder);
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installationFolderNotValid");
                Utils.warningDialog(headerText, contentText);
                return;
            }
            if (profileSelect.getValue() == null) {
                logger.warn("No profiles defined. Import operation aborted");
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profileNotEmpty");
                Utils.warningDialog(headerText, contentText);
                return;
            }

            List<String> selectedProfileNameList = facade.selectProfilesToImport(profileSelect.getValue().getName());
            if (selectedProfileNameList == null || selectedProfileNameList.isEmpty()) {
                return;
            }

            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.importOperation");
            String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.confirmImportOperation");
            Optional<ButtonType> result = Utils.questionDialog(headerText, contentText);
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                logger.info("Starting the process to import maps and mods from the server to the launcher");

                StringBuffer successOfficialMaps = new StringBuffer();
                StringBuffer successCustomMaps = new StringBuffer();
                StringBuffer successMods = new StringBuffer();
                StringBuffer errorsOfficialMaps = new StringBuffer();
                StringBuffer errorsCustomMaps = new StringBuffer();
                StringBuffer errorsMods = new StringBuffer();
                List<Path> officialMapList = null;
                List<Path> customMapList = null;
                List<Path> modList = null;

                officialMapList = Files.walk(Paths.get(installationFolder + "/KFGame/BrewedPC/Maps"))
                        .filter(Files::isRegularFile)
                        .filter(f -> f.getFileName().toString().toUpperCase().startsWith("KF-"))
                        .filter(f -> f.getFileName().toString().toUpperCase().endsWith(".KFM"))
                        .collect(Collectors.toList());

                customMapList = Files.walk(Paths.get(installationFolder + "/KFGame/Cache"))
                        .filter(Files::isRegularFile)
                        .filter(f -> f.getFileName().toString().toUpperCase().startsWith("KF-"))
                        .filter(f -> f.getFileName().toString().toUpperCase().endsWith(".KFM"))
                        .collect(Collectors.toList());

                File[] cacheFolderList = new File(installationFolder + "/KFGame/Cache").listFiles();
                if (cacheFolderList != null && cacheFolderList.length > 0) {
                    List<Long> idWorkShopCustomMapList = getIdWorkShopListFromPathList(customMapList);
                    Kf2Common kf2Common = Kf2Factory.getInstance();

                    modList = Arrays.stream(cacheFolderList)
                            .filter(f -> f.isDirectory())
                            .map(f -> f.toPath())
                            .filter(f -> !idWorkShopCustomMapList.contains(kf2Common.getIdWorkShopFromPath(f, installationFolder)))
                            .collect(Collectors.toList());
                }

                importOfficialMapsFromServer(officialMapList, selectedProfileNameList, successOfficialMaps, errorsOfficialMaps);
                importCustomMapsFromServer(customMapList, selectedProfileNameList, successCustomMaps, errorsCustomMaps);
                importModsFromServer(modList, selectedProfileNameList, successMods, errorsMods);

                officialMapsTab.setGraphic(new Label("(" + officialMapsFlowPane.getChildren().size() + ")"));
                customMapsModsTab.setGraphic(new Label("(" + customMapsFlowPane.getChildren().size() + ")"));

                logger.info("The process to import maps and mods from the server to the launcher has finished.");
                if (StringUtils.isNotBlank(successOfficialMaps) || StringUtils.isNotBlank(successCustomMaps) || StringUtils.isNotBlank(successMods)) {
                    StringBuffer message = new StringBuffer();
                    if (StringUtils.isNotBlank(successOfficialMaps)) {
                        headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.officialMaps");
                        message.append("\n" + headerText + ":\n").append(successOfficialMaps);
                    }
                    if (StringUtils.isNotBlank(successCustomMaps)) {
                        headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.customMaps");
                        message.append("\n" + headerText + ":\n").append(successCustomMaps);
                    }
                    if (StringUtils.isNotBlank(successMods)) {
                        headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mods");
                        message.append("\n" + headerText + ":\n").append(successMods);
                    }
                    headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.importSuccess");
                    Utils.infoDialog(headerText, message.toString());
                } else {
                    headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.noMapsImported");
                    contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.noMapsImportedWarning");
                    Utils.infoDialog(headerText, contentText);
                }
                if (StringUtils.isNotBlank(errorsOfficialMaps) || StringUtils.isNotBlank(errorsCustomMaps) || StringUtils.isNotBlank(errorsMods)) {
                    StringBuffer message = new StringBuffer();
                    if (StringUtils.isNotBlank(errorsOfficialMaps)) {
                        headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.officialMaps");
                        message.append("\n" + headerText + ":\n").append(errorsOfficialMaps);
                    }
                    if (StringUtils.isNotBlank(errorsCustomMaps)) {
                        headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.customMaps");
                        message.append("\n" + headerText + ":\n").append(errorsCustomMaps);
                    }
                    if (StringUtils.isNotBlank(errorsMods)) {
                        headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mods");
                        message.append("\n" + headerText + ":\n").append(errorsMods);
                    }
                    logger.warn("Error importing next maps and mods from server to the launcher: " + message.toString());
                    headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.importError");
                    contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.seeLauncherLog");
                    Utils.warningDialog(headerText + ":", message.toString() + "\n" + contentText);
                }
            }
        } catch (Exception e) {
            String message = "Error importing maps and mods from server to the launcher";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    private List<Long> getIdWorkShopListFromPathList(List<Path> pathList) {
        List<Long> idWorkShopList = new ArrayList<Long>();
        Kf2Common kf2Common = Kf2Factory.getInstance();
        for (Path path: pathList) {
            Long idWorkShop = kf2Common.getIdWorkShopFromPath(path, installationFolder) ;
            if (idWorkShop != null) {
                idWorkShopList.add(idWorkShop);
            }
        }
        return idWorkShopList;
    }

    private void importOfficialMapsFromServer(List<Path> officialMapList, List<String> selectedProfileNameList, StringBuffer success, StringBuffer errors) {
        if (officialMapList == null || officialMapList.isEmpty()) {
            return;
        }

        String mapNameLabel = "";
        try {
            mapNameLabel = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapName");
        } catch (Exception e) {
            mapNameLabel = "Map name";
        }

        for (Path officialMapPath: officialMapList) {
            String filenameWithExtension = officialMapPath.getFileName().toString();
            String[] array = filenameWithExtension.split(".kfm");
            String mapName = array[0];

            try {
                MapDto mapInDataBase = facade.findMapByName(mapName);
                if (mapInDataBase == null) {
                    MapDto insertedMap = facade.insertOfficialMap(mapName, selectedProfileNameList);
                    if (insertedMap != null) {
                        if (selectedProfileNameList.contains(profileSelect.getValue().getName()) &&
                                !mapList.stream().filter(m -> m.isOfficial() && m.getKey().equalsIgnoreCase(mapInDataBase.getKey())).findFirst().isPresent()) {
                            mapList.add(insertedMap);
                            GridPane gridpane = createMapGridPane(insertedMap);
                            officialMapsFlowPane.getChildren().add(gridpane);
                        }
                        success.append(mapNameLabel).append(": ").append(insertedMap.getKey()).append("\n");
                    } else {
                        logger.error("Error importing the official map with name: " + mapName);
                        errors.append(mapNameLabel).append(": ").append(mapName).append("\n");
                    }
                } else {
                    Boolean mapAddedToProfiles = facade.addProfilesToMap(mapInDataBase.getKey(), selectedProfileNameList);
                    if (mapAddedToProfiles != null) {
                        if (mapAddedToProfiles) {
                            if (selectedProfileNameList.contains(profileSelect.getValue().getName()) &&
                                    !mapList.stream().filter(m -> m.isOfficial() && m.getKey().equalsIgnoreCase(mapInDataBase.getKey())).findFirst().isPresent()) {
                                mapList.add(mapInDataBase);
                                GridPane gridpane = createMapGridPane(mapInDataBase);
                                officialMapsFlowPane.getChildren().add(gridpane);
                            }
                            success.append(mapNameLabel).append(": ").append(mapInDataBase.getKey()).append("\n");
                        } else {
                            logger.error("Error importing the official map with name: " + mapName);
                            errors.append(mapNameLabel).append(": ").append(mapName).append("\n");
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Error importing the official map: " + mapName, e);
                errors.append(mapNameLabel).append(": ").append(mapName).append("\n");
            }
        }
    }

    private void importCustomMapsFromServer(List<Path> customMapList, List<String> selectedProfileNameList, StringBuffer success, StringBuffer errors) {
        if (customMapList == null || customMapList.isEmpty()) {
            return;
        }

        String mapNameLabel = "";
        try {
            mapNameLabel = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapName");
        } catch (Exception e) {
            mapNameLabel = "Map name";
        }

        Kf2Common kf2Common = Kf2Factory.getInstance();
        for (Path customMapPath: customMapList) {
            String filenameWithExtension = customMapPath.getFileName().toString();
            String[] array = filenameWithExtension.split(".kfm");
            String mapName = array[0];
            Long idWorkShop = kf2Common.getIdWorkShopFromPath(customMapPath.getParent(), installationFolder);
            try {
                MapDto mapInDataBase = facade.findMapOrModByIdWorkShop(idWorkShop);
                if (mapInDataBase == null) {
                    MapDto customMap = facade.createNewCustomMapFromWorkshop(idWorkShop, mapName, installationFolder, true, false, selectedProfileNameList);
                    if (customMap != null) {
                        if (selectedProfileNameList.contains(profileSelect.getValue().getName()) &&
                                !mapList.stream().filter(m -> !m.isOfficial() && m.getIdWorkShop().equals(mapInDataBase.getIdWorkShop())).findFirst().isPresent()) {
                            mapList.add(customMap);
                            GridPane gridpane = createMapGridPane(customMap);
                            customMapsFlowPane.getChildren().add(gridpane);
                        }
                        success.append(mapNameLabel).append(": ").append(customMap.getKey()).append(" - id WorkShop: ").append(idWorkShop).append("\n");
                    } else {
                        logger.error("Error importing the custom map with idWorkShop: " + idWorkShop);
                        errors.append(mapNameLabel).append(": ").append(mapName).append(" - id WorkShop: ").append(idWorkShop).append("\n");
                    }
                } else {
                    Boolean mapAddedToProfiles = facade.addProfilesToMap(mapInDataBase.getKey(), selectedProfileNameList);
                    if (mapAddedToProfiles != null) {
                        if (mapAddedToProfiles) {
                            if (selectedProfileNameList.contains(profileSelect.getValue().getName()) &&
                                    !mapList.stream().filter(m -> !m.isOfficial() && m.getIdWorkShop().equals(mapInDataBase.getIdWorkShop())).findFirst().isPresent()) {
                                mapList.add(mapInDataBase);
                                GridPane gridpane = createMapGridPane(mapInDataBase);
                                customMapsFlowPane.getChildren().add(gridpane);
                            }
                            success.append(mapNameLabel).append(": ").append(mapInDataBase.getKey()).append(" - id WorkShop: ").append(idWorkShop).append("\n");
                        } else {
                            logger.error("Error importing the custom map with idWorkShop: " + idWorkShop);
                            errors.append(mapNameLabel).append(": ").append(mapName).append(" - id WorkShop: ").append(idWorkShop).append("\n");
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Error importing the custom map: " + mapName, e);
                errors.append(mapNameLabel).append(": ").append(mapName).append(" - idWorkShop: ").append(idWorkShop).append("\n");
            }
        }
    }


    private void importModsFromServer(List<Path> modList, List<String> selectedProfileNameList, StringBuffer success, StringBuffer errors) {
        if (modList == null || modList.isEmpty()) {
            return;
        }

        String modNameLabel = "";
        try {
            modNameLabel = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.modName");
        } catch (Exception e) {
            modNameLabel = "Mod name";
        }

        Kf2Common kf2Common = Kf2Factory.getInstance();
        for (Path modPath: modList) {
            Long idWorkShop = kf2Common.getIdWorkShopFromPath(modPath, installationFolder);
            try {
                MapDto modInDataBase = facade.findMapOrModByIdWorkShop(idWorkShop);
                if (modInDataBase == null) {
                    MapDto mod = facade.createNewCustomMapFromWorkshop(idWorkShop, installationFolder, true, true, selectedProfileNameList);
                    if (mod != null) {
                        if (selectedProfileNameList.contains(profileSelect.getValue().getName()) &&
                                !mapList.stream().filter(m -> !m.isOfficial() && m.getIdWorkShop().equals(modInDataBase.getIdWorkShop())).findFirst().isPresent()) {
                            mapList.add(mod);
                            GridPane gridpane = createMapGridPane(mod);
                            customMapsFlowPane.getChildren().add(gridpane);
                        }
                        success.append(modNameLabel).append(": ").append(mod.getKey()).append(" - id WorkShop: ").append(idWorkShop).append("\n");
                    } else {
                        logger.error("Error importing the mod with idWorkShop: " + idWorkShop);
                        errors.append("id WorkShop: ").append(idWorkShop).append("\n");
                    }
                } else {
                    Boolean modAddedToProfiles = facade.addProfilesToMap(modInDataBase.getKey(), selectedProfileNameList);
                    if (modAddedToProfiles != null) {
                        if (modAddedToProfiles) {
                            if (selectedProfileNameList.contains(profileSelect.getValue().getName()) &&
                                    !mapList.stream().filter(m -> !m.isOfficial() && m.getIdWorkShop().equals(modInDataBase.getIdWorkShop())).findFirst().isPresent()) {
                                mapList.add(modInDataBase);
                                GridPane gridpane = createMapGridPane(modInDataBase);
                                customMapsFlowPane.getChildren().add(gridpane);
                            }
                            success.append(modNameLabel).append(": ").append(modInDataBase.getKey()).append(" - id WorkShop: ").append(idWorkShop).append("\n");
                        } else {
                            logger.error("Error importing the mod with idWorkShop: " + idWorkShop);
                            errors.append("id WorkShop: ").append(idWorkShop).append("\n");
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Error importing a mod: ","See stacktrace for more details", e);
                errors.append("id WorkShop: ").append(idWorkShop).append("\n");
            }
        }

    }

    @FXML
    private void searchInWorkShopOnAction() {
        Session.getInstance().setMap(null);
        Session.getInstance().setMapsProfile(profileSelect.getValue());
        loadNewContent("/views/mapWebInfo.fxml");
    }

    @FXML
    private void selectAllMapsOnAction() {
        ObservableList<Node> nodes = null;
        if (officialMapsTab.isSelected()) {
            nodes = officialMapsFlowPane.getChildren();
        } else {
            nodes = customMapsFlowPane.getChildren();
        }
        for (Node node: nodes) {
            GridPane gridpane = (GridPane) node;
            CheckBox checkbox = (CheckBox)gridpane.getChildren().get(1);
            checkbox.setSelected(selectMaps);
        }
        try {
            String labelText = "";
            if (selectMaps) {
                labelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.unselectMaps");
            } else {
                labelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.selectMaps");
            }
            selectMaps = !selectMaps;
            selectAllMaps.setText(labelText);
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void profileOnAction() {
        try {
            customMapsFlowPane.getChildren().clear();
            officialMapsFlowPane.getChildren().clear();
            mapList = facade.getMapsFromProfile(profileSelect.getValue().getName());
            for (MapDto map : mapList) {
                GridPane gridpane = createMapGridPane(map);
                if (map.isOfficial()) {
                    officialMapsFlowPane.getChildren().add(gridpane);
                } else {
                    customMapsFlowPane.getChildren().add(gridpane);
                }
            }

            officialMapsTab.setGraphic(new Label("(" + officialMapsFlowPane.getChildren().size() + ")"));
            customMapsModsTab.setGraphic(new Label("(" + customMapsFlowPane.getChildren().size() + ")"));
            Session.getInstance().setMapsProfile(profileSelect.getValue());
        } catch (SQLException e) {
            Utils.errorDialog(e.getMessage(), e);
        }
    }
}
