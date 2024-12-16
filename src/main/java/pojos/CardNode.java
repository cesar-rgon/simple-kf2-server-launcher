package pojos;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import pojos.enums.EnumCardOrientation;
import services.PropertyService;
import services.PropertyServiceImpl;

public class CardNode {

    private final Label platformNameLabel;
    private final Label isOfficialLabel;
    private final Label mapNameLabel;
    private final CheckBox checkbox;
    private Hyperlink clickToDownloadMapLink;
    private final Label isInMapsCycleLabel;
    private final Label isMapLabel;

    public CardNode(EnumCardOrientation cardOrientation, Node node) {
        super();
        HBox hBox = null;
        VBox vBox = null;
        String isMapModLabelText = StringUtils.EMPTY;
        PropertyService propertyService = new PropertyServiceImpl();

        try {
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            isMapModLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.isMap");
        } catch (Exception e) {
            isMapModLabelText = "MAP";
        }

        switch (cardOrientation) {
            case RIGHT:
                hBox = (HBox) node;
                checkbox = (CheckBox) ((VBox) hBox.getChildren().get(1)).getChildren().get(3);
                mapNameLabel = ((Label) ((VBox) hBox.getChildren().get(1)).getChildren().get(4));
                platformNameLabel = (Label) ((VBox) hBox.getChildren().get(1)).getChildren().get(0);
                isOfficialLabel = (Label) ((VBox) hBox.getChildren().get(1)).getChildren().get(1);
                try {
                    clickToDownloadMapLink = (Hyperlink) ((StackPane) ((VBox) hBox.getChildren().get(1)).getChildren().get(8)).getChildren().get(2);
                } catch (Exception e) {
                    clickToDownloadMapLink = null;
                }
                isMapLabel = "false".equals(isOfficialLabel.getText()) ? (Label) ((VBox) hBox.getChildren().get(1)).getChildren().get(7): null;
                isInMapsCycleLabel = isMapLabel != null && isMapModLabelText.equals(isMapLabel.getText()) ? (Label) ((StackPane) ((VBox) hBox.getChildren().get(1)).getChildren().get(8)).getChildren().get(1): null;
                break;

            case UP:
                vBox = (VBox) node;
                checkbox = (CheckBox) vBox.getChildren().get(3);
                mapNameLabel = (Label) vBox.getChildren().get(4);
                platformNameLabel = (Label) vBox.getChildren().get(0);
                isOfficialLabel = (Label) vBox.getChildren().get(1);
                try {
                    clickToDownloadMapLink = (Hyperlink) ((StackPane) vBox.getChildren().get(8)).getChildren().get(2);
                } catch (Exception e) {
                    clickToDownloadMapLink = null;
                }
                isMapLabel = "false".equals(isOfficialLabel.getText()) ? (Label) vBox.getChildren().get(7): null;
                isInMapsCycleLabel = isMapLabel != null &&  isMapModLabelText.equals(isMapLabel.getText()) ? (Label) ((StackPane) vBox.getChildren().get(8)).getChildren().get(1): null;
                break;

            case LEFT:
                hBox = (HBox) node;
                checkbox = (CheckBox) ((VBox) hBox.getChildren().get(0)).getChildren().get(3);
                mapNameLabel = (Label) ((VBox) hBox.getChildren().get(0)).getChildren().get(4);
                platformNameLabel = (Label) ((VBox) hBox.getChildren().get(0)).getChildren().get(0);
                isOfficialLabel = (Label) ((VBox) hBox.getChildren().get(0)).getChildren().get(1);
                try {
                    clickToDownloadMapLink = (Hyperlink) ((StackPane) ((VBox) hBox.getChildren().get(0)).getChildren().get(8)).getChildren().get(2);
                } catch (Exception e) {
                    clickToDownloadMapLink = null;
                }
                isMapLabel = "false".equals(isOfficialLabel.getText()) ? (Label) ((VBox) hBox.getChildren().get(0)).getChildren().get(7): null;
                isInMapsCycleLabel = isMapLabel != null &&  isMapModLabelText.equals(isMapLabel.getText()) ? (Label) ((StackPane) ((VBox) hBox.getChildren().get(0)).getChildren().get(8)).getChildren().get(1): null;
                break;

            case DETAILED:
                hBox = (HBox) node;
                checkbox = (CheckBox) ((Label) ((VBox) hBox.getChildren().get(1)).getChildren().get(0)).getGraphic();
                mapNameLabel = (Label) ((VBox) hBox.getChildren().get(2)).getChildren().get(2);
                platformNameLabel = (Label) ((VBox) hBox.getChildren().get(2)).getChildren().get(0);
                isOfficialLabel = (Label) ((VBox) hBox.getChildren().get(2)).getChildren().get(1);
                try {
                    clickToDownloadMapLink = (Hyperlink) ((StackPane) ((VBox) hBox.getChildren().get(1)).getChildren().get(1)).getChildren().get(2);
                } catch (Exception e) {
                    clickToDownloadMapLink = null;
                }
                isMapLabel = "false".equals(isOfficialLabel.getText()) ? (Label) ((VBox) hBox.getChildren().get(1)).getChildren().get(1): null;
                if ("true".equals(isOfficialLabel.getText())) {
                    isInMapsCycleLabel = (Label) ((StackPane) ((VBox) hBox.getChildren().get(1)).getChildren().get(1)).getChildren().get(1);
                } else {
                    isInMapsCycleLabel = isMapLabel != null && isMapModLabelText.equals(isMapLabel.getText()) ? (Label) ((StackPane) ((VBox) hBox.getChildren().get(1)).getChildren().get(2)).getChildren().get(1): null;
                }
                break;

            default:
                vBox = (VBox) node;
                checkbox = (CheckBox) vBox.getChildren().get(4);
                mapNameLabel = (Label) vBox.getChildren().get(5);
                platformNameLabel = (Label) vBox.getChildren().get(0);
                isOfficialLabel = (Label) vBox.getChildren().get(1);
                try {
                    clickToDownloadMapLink = (Hyperlink) ((StackPane) vBox.getChildren().get(9)).getChildren().get(2);
                } catch (Exception e) {
                    clickToDownloadMapLink = null;
                }
                isMapLabel = "false".equals(isOfficialLabel.getText()) ? (Label) vBox.getChildren().get(8): null;
                isInMapsCycleLabel = isMapLabel != null && isMapModLabelText.equals(isMapLabel.getText()) ? (Label) ((StackPane) vBox.getChildren().get(9)).getChildren().get(1): null;
                break;
        }
    }

    public Label getPlatformNameLabel() {
        return platformNameLabel;
    }

    public Label getMapNameLabel() {
        return mapNameLabel;
    }

    public CheckBox getCheckbox() {
        return checkbox;
    }

    public Hyperlink getClickToDownloadMapLink() {
        return clickToDownloadMapLink;
    }

    public Label getIsOfficialLabel() {
        return isOfficialLabel;
    }

    public Label getIsInMapsCycleLabel() {
        return isInMapsCycleLabel;
    }
}
