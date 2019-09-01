package stories.mapsedition;

import dtos.MapDto;
import entities.Map;
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
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private List<Map> mapList;
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
            mapList = facade.listAllMapsAndMods();
            List<MapDto> mapListDto = facade.getDtos(mapList);
            for (MapDto map: mapListDto) {
                GridPane gridpane = createMapGridPane(map);
                if (map.isOfficial()) {
                    officialMapsFlowPane.getChildren().add(gridpane);
                } else {
                    customMapsFlowPane.getChildren().add(gridpane);
                }
            }
            officialMapsTab.setGraphic(new Label("(" + officialMapsFlowPane.getChildren().size() + ")"));
            customMapsModsTab.setGraphic(new Label("(" + customMapsFlowPane.getChildren().size() + ")"));

            String addNewMapsText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.addMaps");
            addNewMaps.setText(addNewMapsText);
            String searchInWorkShopText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.searchInWorkShop");
            searchInWorkShop.setText(searchInWorkShopText);
            String removeMapsText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.removeMaps");
            removeMaps.setText(removeMapsText);
            String selectAllMapsText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.selectMaps");
            selectAllMaps.setText(selectAllMapsText);
            String importMapsFromServerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.importMaps");
            importMapsFromServer.setText(importMapsFromServerText);
            String sliderLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.slider");
            sliderLabel.setText(sliderLabelText);
            String customMapsModsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.customMaps");
            customMapsModsTab.setText(customMapsModsTabText);
            String officialMapsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.officiaslMaps");
            officialMapsTab.setText(officialMapsTabText);

            Double sliderColumns = Double.parseDouble(facade.findConfigPropertyValue("prop.config.mapSliderValue"));
            mapsSlider.setValue(sliderColumns);
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
        mapNameLabel.setMaxWidth(mapPreview.getFitWidth() - 25);
        mapNameLabel.setAlignment(Pos.CENTER);

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
        return (MainApplication.getPrimaryStage().getWidth() - (50 * mapsSlider.getValue()) - 75) / mapsSlider.getValue();
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
        List<MapDto> mapListDto = facade.getDtos(mapList);
        for (MapDto map: mapListDto) {
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
            if (officialMapsTab.isSelected()) {
                SingleSelectionModel<Tab> selectionModel = mapsModsTabPane.getSelectionModel();
                selectionModel.select(customMapsModsTab);
            }
            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.addCustomMaps");
            String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.urlsIdsWorkshop");
            Optional<String> result = Utils.OneTextInputDialog(headerText, contentText);

            if (result.isPresent() && StringUtils.isNotBlank(result.get())) {
                StringBuffer success = new StringBuffer();
                StringBuffer errors = new StringBuffer();

                String[] idUrlWorkShopArray = result.get().replaceAll(" ", "").split(",");
                Map customMap = null;
                for (int i = 0; i < idUrlWorkShopArray.length; i++) {
                    try {
                        Long idWorkShop = null;
                        if (idUrlWorkShopArray[i].contains("http")) {
                            String[] array = idUrlWorkShopArray[i].split("=");
                            idWorkShop = Long.parseLong(array[1]);
                        } else {
                            idWorkShop = Long.parseLong(idUrlWorkShopArray[i]);
                        }
                        Optional<Map> mapModInDataBase = facade.findMapOrModByIdWorkShop(idWorkShop);
                        if (!mapModInDataBase.isPresent()) {
                            customMap = facade.createNewCustomMapFromWorkshop(idWorkShop, installationFolder, false, null);
                            if (customMap != null) {
                                mapList.add(customMap);
                                GridPane gridpane = createMapGridPane(facade.getDto(customMap));
                                customMapsFlowPane.getChildren().add(gridpane);
                                String mapNameMessage = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapName");
                                success.append(mapNameMessage + ": ").append(customMap.getCode()).append(" - id WorkShop: ").append(customMap.getIdWorkShop()).append("\n");
                            } else {
                                errors.append("url/id WorkShop: ").append(idUrlWorkShopArray[i]).append("\n");
                            }
                        } else {
                            errors.append("url/id WorkShop: ").append(idUrlWorkShopArray[i]).append("\n");
                        }
                    } catch (Exception e) {
                        errors.append("url/id WorkShop: ").append(idUrlWorkShopArray[i]).append("\n");
                    }
                }
                if (StringUtils.isNotBlank(success)) {
                    customMapsModsTab.setGraphic(new Label("(" + customMapsFlowPane.getChildren().size() + ")"));
                    String message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapsAdded");
                    Utils.infoDialog(message + ":", success.toString());
                } else {
                    headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapsNotAdded");
                    contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.checkUrlIdWorkshop");
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
                            if (Session.getInstance().getActualProfile() != null && mapNameLabel.getText().equalsIgnoreCase(Session.getInstance().getActualProfile().getMap().getKey())) {
                                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapNotDeleted");
                                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapIsSelected");
                                Utils.warningDialog(headerText, contentText + ":\n" + mapNameLabel.getText());
                            } else {
                                MapDto map = facade.deleteSelectedMap(mapNameLabel.getText());
                                if (map != null) {
                                    mapsToRemove.add(map);
                                    if (map.isOfficial()) {
                                        officialMapsFlowPane.getChildren().remove(gridpane);
                                    } else {
                                        customMapsFlowPane.getChildren().remove(gridpane);
                                        File photo = new File(installationFolder + map.getUrlPhoto());
                                        photo.delete();
                                        File cacheFoler = new File(installationFolder + "/KFGame/Cache/" + map.getIdWorkShop());
                                        FileUtils.deleteDirectory(cacheFoler);
                                    }
                                } else {
                                    errors.append(mapNameLabel.getText()).append("\n");
                                }
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                            Utils.errorDialog(e.getMessage(), e);
                        }
                    }
                    if (!mapsToRemove.isEmpty()) {
                        try {
                            mapList.clear();
                            mapList = facade.listAllMapsAndMods();
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

                importOfficialMapsFromServer(officialMapList, successOfficialMaps, errorsOfficialMaps);
                importCustomMapsFromServer(customMapList, successCustomMaps, errorsCustomMaps);
                importModsFromServer(modList, successMods, errorsMods);

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

    private void importOfficialMapsFromServer(List<Path> officialMapList, StringBuffer success, StringBuffer errors) {
        if (officialMapList != null && !officialMapList.isEmpty()) {
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
                    Optional<Map> mapInDataBase = facade.findMapOrModByCode(mapName);
                    if (!mapInDataBase.isPresent()) {
                        Map newOfficialMap = new Map(mapName, true, null, null, "/KFGame/Web/images/maps/" + mapName + ".jpg", true, false);
                        Map insertedMap = facade.insertMap(newOfficialMap);
                        if (insertedMap != null) {
                            mapList.add(insertedMap);
                            GridPane gridpane = createMapGridPane(facade.getDto(insertedMap));
                            officialMapsFlowPane.getChildren().add(gridpane);
                            success.append(mapNameLabel).append(": ").append(insertedMap.getCode()).append("\n");
                        } else {
                            logger.error("Error importing the official map with name: " + mapName);
                            errors.append(mapNameLabel).append(": ").append(mapName).append("\n");
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error importing the official map: " + mapName, e);
                    errors.append(mapNameLabel).append(": ").append(mapName).append("\n");
                }
            }
        }
    }

    private void importCustomMapsFromServer(List<Path> customMapList, StringBuffer success, StringBuffer errors) {
        if (customMapList != null && !customMapList.isEmpty()) {
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
                    Optional<Map> mapInDataBase = facade.findMapOrModByIdWorkShop(idWorkShop);
                    if (!mapInDataBase.isPresent()) {
                        Map customMap = facade.createNewCustomMapFromWorkshop(idWorkShop, mapName, installationFolder, true, false);
                        if (customMap != null) {
                            mapList.add(customMap);
                            GridPane gridpane = createMapGridPane(facade.getDto(customMap));
                            customMapsFlowPane.getChildren().add(gridpane);
                            success.append(mapNameLabel).append(": ").append(customMap.getCode()).append(" - id WorkShop: ").append(idWorkShop).append("\n");
                        } else {
                            logger.error("Error importing the custom map with idWorkShop: " + idWorkShop);
                            errors.append(mapNameLabel).append(": ").append(mapName).append(" - id WorkShop: ").append(idWorkShop).append("\n");
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error importing the custom map: " + mapName, e);
                    errors.append(mapNameLabel).append(": ").append(mapName).append(" - idWorkShop: ").append(idWorkShop).append("\n");
                }
            }
        }
    }

    private void importModsFromServer(List<Path> modList, StringBuffer success, StringBuffer errors) {
        if (modList != null && !modList.isEmpty()) {
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
                    Optional<Map> modInDataBase = facade.findMapOrModByIdWorkShop(idWorkShop);
                    if (!modInDataBase.isPresent()) {
                        Map mod = facade.createNewCustomMapFromWorkshop(idWorkShop, installationFolder, true, true);
                        if (mod != null) {
                            mapList.add(mod);
                            GridPane gridpane = createMapGridPane(facade.getDto(mod));
                            customMapsFlowPane.getChildren().add(gridpane);
                            success.append(modNameLabel).append(": ").append(mod.getCode()).append(" - id WorkShop: ").append(idWorkShop).append("\n");
                        } else {
                            logger.error("Error importing the mod with idWorkShop: " + idWorkShop);
                            errors.append("id WorkShop: ").append(idWorkShop).append("\n");
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error importing a mod: ","See stacktrace for more details", e);
                    errors.append("id WorkShop: ").append(idWorkShop).append("\n");
                }
            }
        }
    }

    @FXML
    private void searchInWorkShopOnAction() {
        Session.getInstance().setMap(null);
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
}
