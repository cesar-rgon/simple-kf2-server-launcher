package stories.mapsedition;

import constants.Constants;
import dtos.MapDto;
import entities.Map;
import enums.MapViewOptions;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MapsEditionController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MapsEditionController.class);

    private final MapsEditionFacade facade;
    private String installationFolder;
    private List<Map> mapList;
    private boolean selectMaps;

    @FXML private Slider mapsSlider;
    @FXML private ComboBox<MapViewOptions> viewPaneCombo;
    @FXML private ScrollPane officialMapsScrollPane;
    @FXML private FlowPane officialMapsFlowPane;
    @FXML private ScrollPane customMapsScrollPane;
    @FXML private FlowPane customMapsFlowPane;
    @FXML private Label officialMapsLabel;
    @FXML private Label customMapsLabel;
    @FXML private TextField searchMaps;
    @FXML private Button selectAllMaps;

    public MapsEditionController() {
        super();
        this.facade = new MapsEditionFacadeImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            selectMaps = true;
            viewPaneCombo.setItems(MapViewOptions.listAll());
            viewPaneCombo.setValue(MapViewOptions.VIEW_BOTH);
            installationFolder = facade.findPropertyValue(Constants.CONFIG_INSTALLATION_FOLDER);
            mapList = facade.listAllMaps();
            List<MapDto> mapListDto = facade.getDtos(mapList);
            for (MapDto map: mapListDto) {
                GridPane gridpane = createMapGridPane(map);
                if (map.getOfficial()) {
                    officialMapsFlowPane.getChildren().add(gridpane);
                } else {
                    customMapsFlowPane.getChildren().add(gridpane);
                }
            }
        } catch (Exception e) {
            Utils.errorDialog("Error getting map list", "See stacktrace for more details", e);
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
        if (map.getOfficial()) {
            gridpane.add(mapNameLabel, 1, 2);
        } else {
            GridPane.setColumnSpan(mapPreview, 2);
            gridpane.add(new CheckBox(), 1, 2);
            gridpane.add(mapNameLabel, 2, 2);
            if (!map.getDownloaded()) {
                Label warningMessage = new Label("Start server to download it");
                warningMessage.setStyle("-fx-text-fill: yellow;");
                GridPane.setColumnSpan(warningMessage, 2);
                gridpane.add(warningMessage,1,3);
                GridPane.setHalignment(warningMessage, HPos.CENTER);
            }
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
    private void viewPaneComboOnAction() {
        if (viewPaneCombo.getValue().equals(viewPaneCombo.getItems().get(0))) {
            GridPane.setRowSpan(officialMapsScrollPane, 1);
            officialMapsLabel.setVisible(true);
            officialMapsFlowPane.setVisible(true);
            officialMapsScrollPane.setVisible(true);
            GridPane.setRowIndex(customMapsScrollPane, 4);
            GridPane.setRowSpan(customMapsScrollPane, 1);
            GridPane.setRowIndex(customMapsLabel, 3);
            customMapsLabel.setVisible(true);
            customMapsScrollPane.setVisible(true);
            customMapsFlowPane.setVisible(true);
        } else {
            if (viewPaneCombo.getValue().equals(viewPaneCombo.getItems().get(1))) {
                GridPane.setRowSpan(officialMapsScrollPane, 3);
                officialMapsLabel.setVisible(true);
                officialMapsFlowPane.setVisible(true);
                officialMapsScrollPane.setVisible(true);
                customMapsFlowPane.setVisible(false);
                customMapsScrollPane.setVisible(false);
                customMapsLabel.setVisible(false);
            } else {
                officialMapsLabel.setVisible(false);
                officialMapsFlowPane.setVisible(false);
                officialMapsScrollPane.setVisible(false);
                GridPane.setRowIndex(customMapsLabel, 1);
                GridPane.setRowIndex(customMapsScrollPane, 2);
                GridPane.setRowSpan(customMapsScrollPane, 3);
                customMapsLabel.setVisible(true);
                customMapsScrollPane.setVisible(true);
                customMapsFlowPane.setVisible(true);
            }
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
                if (map.getOfficial()) {
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
            Utils.warningDialog("No maps can be added!", "The installation folder is not correct.\nSet it up in Install / Update section.");
            return;
        }
        if (Session.getInstance().isRunningProcess()) {
            Utils.warningDialog("No maps can be added!", "At least one instance of the server is running. Close them.");
            return;
        }
        if (MapViewOptions.VIEW_OFFICIAL.equals(viewPaneCombo.getValue())) {
            viewPaneCombo.setValue(MapViewOptions.VIEW_BOTH);
        }
        Optional<String> result = Utils.OneTextInputDialog("Add new custom maps", "Enter url/id WorkShop\nIf more than one use\ncomma as separator");
        if (result.isPresent() && StringUtils.isNotBlank(result.get())) {
            StringBuffer success = new StringBuffer();
            StringBuffer errors = new StringBuffer();

            String[] idUrlWorkShopArray = result.get().replaceAll(" ", "").split(",");
            Map customMap = null;
            for (int i=0; i < idUrlWorkShopArray.length; i++) {
                try {
                    customMap = facade.createNewCustomMapFromWorkshop(idUrlWorkShopArray[i], installationFolder);
                    if (customMap != null) {
                        mapList.add(customMap);
                        GridPane gridpane = createMapGridPane(facade.getDto(customMap));
                        customMapsFlowPane.getChildren().add(gridpane);
                        success.append("map name: ").append(customMap.getCode()).append(" - idWorkShop: ").append(customMap.getIdWorkShop()).append("\n");
                    } else {
                        errors.append("url/id WorkShop: ").append(idUrlWorkShopArray[i]).append("\n");
                    }
                } catch (Exception e) {
                    errors.append("url/id WorkShop: ").append(idUrlWorkShopArray[i]).append("\n");
                }
            }
            if (StringUtils.isNotBlank(success)) {
                Utils.infoDialog("These maps were successfully added to the launcher:", success.toString());
            } else {
                Utils.infoDialog("No maps were added", "Check the url/id WorkShop of the maps");
            }
            if (StringUtils.isNotBlank(errors)) {
                Utils.errorDialog("Error adding next maps to the launcher:", errors.toString(), null);
            }
        }
    }

    @FXML
    private void removeMapsOnAction() {
        if (!facade.isCorrectInstallationFolder(installationFolder)) {
            Utils.warningDialog("No maps can be removed!", "The installation folder is not correct.\nSet it up in Install / Update section.");
            return;
        }
        if (Session.getInstance().isRunningProcess()) {
            Utils.warningDialog("No maps can be removed!", "At least one instance of the server is running. Close them.");
            return;
        }
        List<Node> removeList = new ArrayList<Node>();
        StringBuffer message = new StringBuffer();
        ObservableList<Node> nodes = customMapsFlowPane.getChildren();
        for (Node node: nodes) {
            GridPane gridpane = (GridPane) node;
            CheckBox checkbox = (CheckBox)gridpane.getChildren().get(1);
            Label mapNameLabel = (Label)gridpane.getChildren().get(2);
            if (checkbox.isSelected()) {
                removeList.add(gridpane);
                message.append(mapNameLabel.getText()).append("\n");
            }
        }
        if (removeList.isEmpty()) {
            Utils.warningDialog("No maps selected", "You must select at least one map to be deleted");
        } else {
            Optional<ButtonType> result = Utils.questionDialog("Are you sure that you want to delete next maps?", message.toString());
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                List<MapDto> mapsToRemove = new ArrayList<MapDto>();
                StringBuffer errors = new StringBuffer();
                for (Node node: removeList) {
                    try {
                        GridPane gridpane = (GridPane) node;
                        Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                        MapDto customMap = facade.deleteSelectedMap(mapNameLabel.getText());
                        if (customMap != null) {
                            mapsToRemove.add(customMap);
                            customMapsFlowPane.getChildren().remove(gridpane);
                            File photo = new File(installationFolder + customMap.getUrlPhoto());
                            photo.delete();
                            File cacheFoler = new File(installationFolder + "/KFGame/Cache/" + customMap.getIdWorkShop());
                            FileUtils.deleteDirectory(cacheFoler);
                        } else {
                            errors.append(mapNameLabel.getText()).append("\n");
                        }
                    } catch (Exception e) {
                        Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
                    }
                }
                if (!mapsToRemove.isEmpty()) {
                    try {
                        mapList.clear();
                        mapList = facade.listAllMaps();
                    } catch (SQLException e) {
                        Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
                    }
                }
                if (StringUtils.isNotBlank(errors.toString())) {
                    Utils.errorDialog("Next maps could not be deleted", errors.toString(), null);
                }
            }
        }
    }

    @FXML
    private void importMapsFromServerOnAction() {
        if (!facade.isCorrectInstallationFolder(installationFolder)) {
            Utils.warningDialog("No maps can be imported!", "The installation folder is not correct.\nSet it up in Install / Update section.");
            return;
        }
        if (Session.getInstance().isRunningProcess()) {
            Utils.warningDialog("No maps can be imported!", "At least one instance of the server is running. Close them.");
            return;
        }

        Optional<ButtonType> result = Utils.questionDialog("Import maps from server to the launcher", "This operation can take a few minutes.\nAre you sure you want to continue?");
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            logger.info("Starting the process to import maps from the server to the launcher");

            File cacheFolder = new File(installationFolder + "/KFGame/Cache/");
            File[] listOfFiles = cacheFolder.listFiles();
            StringBuffer success = new StringBuffer();
            StringBuffer errors = new StringBuffer();
            for (int i=0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isDirectory()) {
                    Map customMap = null;
                    Long idWorkShop = null;
                    try {
                        idWorkShop = Long.parseLong(listOfFiles[i].getName());
                        List<Path> kfmFilesPath = Files.walk(Paths.get(installationFolder + "/KFGame/Cache/" + idWorkShop))
                                .filter(Files::isRegularFile)
                                .filter(f -> f.getFileName().toString().toUpperCase().startsWith("KF-"))
                                .filter(f -> f.getFileName().toString().toUpperCase().endsWith(".KFM"))
                                .collect(Collectors.toList());

                        if (kfmFilesPath != null && !kfmFilesPath.isEmpty()) {
                            String filenameWithExtension = kfmFilesPath.get(0).getFileName().toString();
                            String[] array = filenameWithExtension.split(".kfm");
                            String mapName = array[0];
                            Optional<Map> customMapInDataBase = facade.findMapByCode(mapName);
                            if (!customMapInDataBase.isPresent()) {
                                customMap = facade.createNewCustomMapFromWorkshop(idWorkShop, mapName, installationFolder);
                                if (customMap != null) {
                                    mapList.add(customMap);
                                    GridPane gridpane = createMapGridPane(facade.getDto(customMap));
                                    customMapsFlowPane.getChildren().add(gridpane);
                                    success.append("map name: ").append(customMap.getCode()).append(" - idWorkShop: ").append(idWorkShop).append("\n");
                                } else {
                                    logger.error("Error importing the customMap with idWorkShop: " + idWorkShop);
                                    errors.append("idWorkShop: ").append(idWorkShop).append("\n");
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.error("Error importing the customMap: " + customMap.getCode() + " with idWorkShop: " + idWorkShop, e);
                        errors.append("map name: ").append(customMap.getCode()).append(" - idWorkShop: ").append(idWorkShop).append("\n");
                    }
                }
            }
            logger.info("The process to import maps from the server to the launcher has finished.");
            if (StringUtils.isNotBlank(success)) {
                Utils.infoDialog("These maps were successfully imported from server to the launcher:", success.toString());
            } else {
                Utils.infoDialog("No maps were imported", "The server does not contain new maps to be imported\nor the maps could not be imported successfully\nSee launcher.log file for more details.");
            }
            if (StringUtils.isNotBlank(errors)) {
                Utils.errorDialog("Error importing next maps from server to the launcher:", errors.toString() + "\nSee launcher.log file for more details.", null);
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
        ObservableList<Node> nodes = customMapsFlowPane.getChildren();
        for (Node node: nodes) {
            GridPane gridpane = (GridPane) node;
            CheckBox checkbox = (CheckBox)gridpane.getChildren().get(1);
            checkbox.setSelected(selectMaps);
        }
        if (selectMaps) {
            selectAllMaps.setText("Unselect all maps");
            selectMaps = false;
        } else {
            selectAllMaps.setText("Select all maps");
            selectMaps = true;
        }
    }
}
