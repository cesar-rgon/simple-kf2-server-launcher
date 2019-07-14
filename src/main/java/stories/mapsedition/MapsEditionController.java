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
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import pojos.session.Session;
import start.MainApplication;
import utils.Utils;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MapsEditionController implements Initializable {

    private final MapsEditionFacade facade;
    private String installationFolder;
    private List<Map> mapList;

    @FXML private Slider mapsSlider;
    @FXML private ComboBox<MapViewOptions> viewPaneCombo;
    @FXML private ScrollPane officialMapsScrollPane;
    @FXML private FlowPane officialMapsFlowPane;
    @FXML private ScrollPane customMapsScrollPane;
    @FXML private FlowPane customMapsFlowPane;
    @FXML private Label officialMapsLabel;
    @FXML private Label customMapsLabel;
    @FXML private TextField searchMaps;

    public MapsEditionController() {
        super();
        this.facade = new MapsEditionFacadeImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            viewPaneCombo.setItems(MapViewOptions.listAll());
            viewPaneCombo.setValue(MapViewOptions.VIEW_BOTH);
            installationFolder = facade.findPropertyValue(Constants.KEY_INSTALLATION_FOLDER);
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
        } catch (SQLException e) {
            Utils.errorDialog("Error getting map list", "See stacktrace for more details", e);
        }
    }


    private GridPane createMapGridPane(MapDto map) {
        Label mapNameLabel = new Label(map.getValue() != null ? map.getValue(): map.getKey());
        Image image = null;
        if (StringUtils.isNotBlank(map.getUrlPhoto())) {
            image = new Image("file:" + installationFolder + "/" + map.getUrlPhoto());
        } else {
            image = new Image("file:src/main/resources/images/photo-borders.png");
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
            String mapLabel = StringUtils.upperCase(map.getValue() != null ? map.getValue(): map.getKey());
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
    private void addNewMapOnAction() {
        if (MapViewOptions.VIEW_OFFICIAL.equals(viewPaneCombo.getValue())) {
            viewPaneCombo.setValue(MapViewOptions.VIEW_BOTH);
        }
        Optional<String> result = Utils.OneTextInputDialog("Add a new custom map", "Enter url / id WorkShop");
        try {
            if (result.isPresent() && StringUtils.isNotBlank(result.get())) {
                Long idWorkShop = null;
                URL urlWorkShop = null;
                if (result.get().contains("http")) {
                    urlWorkShop = new URL(result.get());
                    String[] array = result.get().split("=");
                    idWorkShop = Long.parseLong(array[1]);
                } else {
                    idWorkShop = Long.parseLong(result.get());
                    urlWorkShop = new URL(Constants.MAP_BASE_URL_WORKSHOP + idWorkShop);
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlWorkShop.openStream()));
                String strUrlMapImage = null;
                String mapName = null;
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("image_src")) {
                        String[] array = line.split("\"");
                        strUrlMapImage = array[3];
                    }
                    if (line.contains("workshopItemTitle")) {
                        String[] array = line.split(">");
                        String[] array2 = array[1].split("<");
                        mapName = array2[0];
                    }
                    if (StringUtils.isNotEmpty(strUrlMapImage) && StringUtils.isNotEmpty(mapName)) {
                        break;
                    }
                }
                reader.close();
                String absoluteTargetFolder = installationFolder + Constants.MAP_CUSTOM_LOCAL_FOLDER;
                File localfile = Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, mapName);
                String relativeTargetFolder = Constants.MAP_CUSTOM_LOCAL_FOLDER + "/" + localfile.getName();
                Map customMap = facade.createNewCustomMap(mapName,
                                                          idWorkShop,
                                                          relativeTargetFolder);
                if (customMap != null) {
                    mapList.add(customMap);
                    Kf2Common kf2Common = Kf2Factory.getInstance();
                    kf2Common.addCustomMapToKfEngineIni(customMap.getIdWorkShop(), installationFolder);
                    GridPane gridpane = createMapGridPane(facade.getDto(customMap));
                    customMapsFlowPane.getChildren().add(gridpane);
                } else {
                    Utils.errorDialog("Error adding new custom map", "The map could not be found", null);
                }
            }
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void removeMapsOnAction() {
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
                    List<Long> idWorkShopListToRemove = mapsToRemove.stream().map(m -> m.getIdWorkShop()).collect(Collectors.toList());
                    Kf2Common kf2Common = Kf2Factory.getInstance();
                    kf2Common.removeCustomMapsFromKfEngineIni(idWorkShopListToRemove, installationFolder);
                    List<String> mapNameListToRemove = mapsToRemove.stream().map(m -> m.getKey()).collect(Collectors.toList());
                    kf2Common.removeCustomMapsFromKfGameIni(mapNameListToRemove, installationFolder, mapList);
                }
                if (StringUtils.isNotBlank(errors.toString())) {
                    Utils.errorDialog("Next maps could not be deleted", errors.toString(), null);
                }
            }
        }
    }
}
