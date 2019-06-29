package stories.mapsedition;

import constants.Constants;
import dtos.MapDto;
import dtos.SelectDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.StringUtils;
import pojos.session.Session;
import start.MainApplication;
import utils.Utils;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MapsEditionController implements Initializable {

    private final MapsEditionFacade facade;

    @FXML private Slider mapsSlider;
    @FXML private ComboBox<String> viewPaneCombo;
    @FXML private ScrollPane officialMapsScrollPane;
    @FXML private FlowPane officialMapsFlowPane;
    @FXML private ScrollPane customMapsScrollPane;
    @FXML private FlowPane customMapsFlowPane;
    @FXML private Label officialMapsLabel;
    @FXML private Label customMapsLabel;

    public MapsEditionController() {
        super();
        this.facade = new MapsEditionFacadeImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            List<String> viewOptions = new ArrayList<String>();
            viewOptions.add("Official and custom maps");
            viewOptions.add("Only official maps");
            viewOptions.add("Only custom maps");
            ObservableList<String> viewItems = FXCollections.observableArrayList(viewOptions);
            viewPaneCombo.setItems(viewItems);
            viewPaneCombo.setValue(viewOptions.get(0));

            String installationFolder = facade.findPropertyValue(Constants.KEY_INSTALLATION_FOLDER);
            List<MapDto> maps = facade.listAllMaps();
            for (MapDto map: maps) {
                Label mapDescriptionLabel = new Label(map.getValue());
                Image image = new Image("file:" + installationFolder + "/KFGame/Web/images/maps/" + map.getKey() + ".jpg");
                //Image image = new Image("file:src/main/resources/images/photo-borders.png");
                ImageView mapPreview = new ImageView(image);
                mapPreview.setPreserveRatio(true);
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
                gridpane.add(mapDescriptionLabel, 1, 2);
                GridPane.setHalignment(mapDescriptionLabel, HPos.CENTER);
                if (map.getOfficial()) {
                    officialMapsFlowPane.getChildren().add(gridpane);
                } else {
                    customMapsFlowPane.getChildren().add(gridpane);
                }
            }
            mapsSliderOnMouseClicked();
        } catch (SQLException e) {
            Utils.errorDialog("Error getting map list", "See stacktrace for more details", e);
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
}
