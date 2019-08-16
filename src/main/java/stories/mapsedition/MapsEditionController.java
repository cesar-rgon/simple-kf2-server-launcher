package stories.mapsedition;

import constants.Constants;
import dtos.MapDto;
import entities.Map;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
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
    private String installationFolder;
    private List<Map> mapList;
    private boolean selectMaps;

    @FXML private Slider mapsSlider;
    @FXML private ScrollPane officialMapsScrollPane;
    @FXML private FlowPane officialMapsFlowPane;
    @FXML private ScrollPane customMapsScrollPane;
    @FXML private FlowPane customMapsFlowPane;
    @FXML private TextField searchMaps;
    @FXML private Button selectAllMaps;
    @FXML private TabPane mapsModsTabPane;
    @FXML private Tab customMapsModsTab;
    @FXML private Tab officialMapsTab;

    public MapsEditionController() {
        super();
        this.facade = new MapsEditionFacadeImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            selectMaps = true;
            installationFolder = facade.findPropertyValue(Constants.CONFIG_INSTALLATION_FOLDER);
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
        } catch (Exception e) {
            String message = "Error getting map list";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
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
        Double width = (mapsSlider.getValue() * 3.84) + 128;
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
        int rowIndex = 3;
        if (!map.isDownloaded()) {
            Label warningMessage = new Label("Start server to download it");
            warningMessage.setStyle("-fx-text-fill: yellow;");
            GridPane.setColumnSpan(warningMessage, 2);
            gridpane.add(warningMessage,1, rowIndex);
            GridPane.setHalignment(warningMessage, HPos.CENTER);
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
        GridPane.setHalignment(mapNameLabel, HPos.CENTER);
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
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void mapsSliderOnMouseClicked() {
        for (int i = 0; i < officialMapsFlowPane.getChildren().size(); i++) {
            GridPane gridPane = (GridPane)officialMapsFlowPane.getChildren().get(i);
            ImageView mapPreview = (ImageView) gridPane.getChildren().get(0);
            Double width = (mapsSlider.getValue() * 3.84) + 128;
            mapPreview.setFitWidth(width);
            mapPreview.setFitHeight(width/2);
        }
        for (int i = 0; i < customMapsFlowPane.getChildren().size(); i++) {
            GridPane gridPane = (GridPane)customMapsFlowPane.getChildren().get(i);
            ImageView mapPreview = (ImageView) gridPane.getChildren().get(0);
            Double width = (mapsSlider.getValue() * 3.84) + 128;
            mapPreview.setFitWidth(width);
            mapPreview.setFitHeight(width/2);
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
        if (!facade.isCorrectInstallationFolder(installationFolder)) {
            String message = "The installation folder is not correct: " + installationFolder + ".\nSet it up in Install / Update section.";
            logger.warn(message);
            Utils.warningDialog("No maps/mods can be added!", message);
            return;
        }
        if (officialMapsTab.isSelected()) {
            SingleSelectionModel<Tab> selectionModel = mapsModsTabPane.getSelectionModel();
            selectionModel.select(customMapsModsTab);
        }
        Optional<String> result = Utils.OneTextInputDialog("Add new custom maps/mods", "Enter url/id WorkShop\nIf more than one use\ncomma as separator");
        if (result.isPresent() && StringUtils.isNotBlank(result.get())) {
            StringBuffer success = new StringBuffer();
            StringBuffer errors = new StringBuffer();

            String[] idUrlWorkShopArray = result.get().replaceAll(" ", "").split(",");
            Map customMap = null;
            for (int i=0; i < idUrlWorkShopArray.length; i++) {
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
                            success.append("map name: ").append(customMap.getCode()).append(" - idWorkShop: ").append(customMap.getIdWorkShop()).append("\n");
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
                Utils.infoDialog("These maps/mods were successfully added to the launcher:", success.toString());
            } else {
                Utils.infoDialog("No maps/mods were added", "Check the url/id WorkShop of the maps");
            }
            if (StringUtils.isNotBlank(errors)) {
                String message = "Error adding next maps/mods to the launcher:" + errors.toString();
                logger.warn(message);
                Utils.warningDialog("Error adding next maps/mods to the launcher:", errors.toString());
            }
        }
    }

    @FXML
    private void removeMapsOnAction() {
        if (!facade.isCorrectInstallationFolder(installationFolder)) {
            String message = "The installation folder is not correct: " + installationFolder + ".\nSet it up in Install / Update section.";
            logger.warn(message);
            Utils.warningDialog("No maps/mods can be removed!", message);
            return;
        }
        List<Node> removeList = new ArrayList<Node>();
        StringBuffer message = new StringBuffer();
        ObservableList<Node> officialNodes = officialMapsFlowPane.getChildren();
        ObservableList<Node> customNodes = customMapsFlowPane.getChildren();

        for (Node node: officialNodes) {
            GridPane gridpane = (GridPane) node;
            CheckBox checkbox = (CheckBox)gridpane.getChildren().get(1);
            Label mapNameLabel = (Label)gridpane.getChildren().get(2);
            if (checkbox.isSelected()) {
                removeList.add(gridpane);
                message.append(mapNameLabel.getText()).append("\n");
            }
        }

        for (Node node: customNodes) {
            GridPane gridpane = (GridPane) node;
            CheckBox checkbox = (CheckBox)gridpane.getChildren().get(1);
            Label mapNameLabel = (Label)gridpane.getChildren().get(2);
            if (checkbox.isSelected()) {
                removeList.add(gridpane);
                message.append(mapNameLabel.getText()).append("\n");
            }
        }
        if (removeList.isEmpty()) {
            String warnMessage = "You must select at least one item to be deleted";
            logger.warn(message);
            Utils.warningDialog("No maps/mods selected", warnMessage);
        } else {
            Optional<ButtonType> result = Utils.questionDialog("Are you sure that you want to delete next maps/mods?", message.toString());
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                List<MapDto> mapsToRemove = new ArrayList<MapDto>();
                StringBuffer errors = new StringBuffer();
                for (Node node: removeList) {
                    try {
                        GridPane gridpane = (GridPane) node;
                        Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                        if (Session.getInstance().getActualProfile() != null && mapNameLabel.getText().equalsIgnoreCase(Session.getInstance().getActualProfile().getMap().getKey())) {
                            Utils.warningDialog("The map can not be deleted!", "Actually the map " + mapNameLabel.getText() + " is being selected\nin the maps combo of main page.\nChange the map in maps combo of main page and try again.");
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
                        Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
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
                        Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
                    }
                }
                if (StringUtils.isNotBlank(errors.toString())) {
                    logger.warn("Next maps/mods could not be deleted" + errors.toString());
                    Utils.warningDialog("Next maps/mods could not be deleted", errors.toString());
                }
            }
        }
    }

    @FXML
    private void importMapsFromServerOnAction() {
        if (!facade.isCorrectInstallationFolder(installationFolder)) {
            String message = "The installation folder is not correct: " + installationFolder + ".\nSet it up in Install / Update section.";
            logger.warn(message);
            Utils.warningDialog("No maps and mods can be imported!", message);
            return;
        }
        Optional<ButtonType> result = Utils.questionDialog("Import maps and mods from server to the launcher", "This operation can take a few minutes.\nAre you sure you want to continue?");
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
            try {
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
            } catch (Exception e) {
                String message = "Error importing maps and mods from server to the launcher";
                logger.error(message, e);
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
                    message.append("\nOFFICIAL MAPS:\n").append(successOfficialMaps);
                }
                if (StringUtils.isNotBlank(successCustomMaps)) {
                    message.append("\nCUSTOM MAPS:\n").append(successCustomMaps);
                }
                if (StringUtils.isNotBlank(successMods)) {
                    message.append("\nMODS:\n").append(successMods);
                }
                Utils.infoDialog("These maps and mods were successfully imported from server to the launcher:", message.toString());
            } else {
                Utils.infoDialog("No maps and mods were imported", "The server does not contain new maps and mods to be\nimported, or they could not be imported successfully\nSee launcher.log file for more details.");
            }
            if (StringUtils.isNotBlank(errorsOfficialMaps) || StringUtils.isNotBlank(errorsCustomMaps) || StringUtils.isNotBlank(errorsMods)) {
                StringBuffer message = new StringBuffer();
                if (StringUtils.isNotBlank(errorsOfficialMaps)) {
                    message.append("\nOFFICIAL MAPS:\n").append(errorsOfficialMaps);
                }
                if (StringUtils.isNotBlank(errorsCustomMaps)) {
                    message.append("\nCUSTOM MAPS:\n").append(errorsCustomMaps);
                }
                if (StringUtils.isNotBlank(errorsMods)) {
                    message.append("\nMODS:\n").append(errorsMods);
                }
                logger.warn("Error importing next maps and mods from server to the launcher:" + message.toString());
                Utils.warningDialog("Error importing next maps and mods from server to the launcher:", message.toString() + "\nSee launcher.log file for more details.");
            }
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
                            success.append("map name: ").append(insertedMap.getCode()).append("\n");
                        } else {
                            logger.error("Error importing the official map with name: " + mapName);
                            errors.append("map name: ").append(mapName).append("\n");
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error importing the official map: " + mapName, "See stacktrace for more details", e);
                    errors.append("map name: ").append(mapName).append("\n");
                }
            }
        }
    }

    private void importCustomMapsFromServer(List<Path> customMapList, StringBuffer success, StringBuffer errors) {
        if (customMapList != null && !customMapList.isEmpty()) {
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
                            success.append("map name: ").append(customMap.getCode()).append(" - idWorkShop: ").append(idWorkShop).append("\n");
                        } else {
                            logger.error("Error importing the custom map with idWorkShop: " + idWorkShop);
                            errors.append("map name: ").append(mapName).append(" - idWorkShop: ").append(idWorkShop).append("\n");
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error importing the custom map: " + mapName, "See stacktrace for more details", e);
                    errors.append("map name: ").append(mapName).append(" - idWorkShop: ").append(idWorkShop).append("\n");
                }
            }
        }
    }

    private void importModsFromServer(List<Path> modList, StringBuffer success, StringBuffer errors) {
        if (modList != null && !modList.isEmpty()) {
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
                            success.append("mod name: ").append(mod.getCode()).append(" - idWorkShop: ").append(idWorkShop).append("\n");
                        } else {
                            logger.error("Error importing the mod with idWorkShop: " + idWorkShop);
                            errors.append("mod's idWorkShop: ").append(idWorkShop).append("\n");
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error importing a mod: ","See stacktrace for more details", e);
                    errors.append("mod's idWorkShop: ").append(idWorkShop).append("\n");
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
        if (selectMaps) {
            selectAllMaps.setText("Unselect all maps/mods");
            selectMaps = false;
        } else {
            selectAllMaps.setText("Select all maps/mods");
            selectMaps = true;
        }
    }
}
