package pojos;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import pojos.enums.EnumCardOrientation;

public class CardNode {

    private final Label platformNameLabel;
    private final Label mapNameLabel;
    private final CheckBox checkbox;
    private final Hyperlink clickToDownloadMapLink;

    public CardNode(EnumCardOrientation cardOrientation, Node node) {
        super();
        HBox hBox = null;
        VBox vBox = null;

        switch (cardOrientation) {
            case RIGHT:
                hBox = (HBox) node;
                checkbox = (CheckBox) ((VBox) hBox.getChildren().get(1)).getChildren().get(3);
                mapNameLabel = (Label) ((VBox) hBox.getChildren().get(1)).getChildren().get(4);
                platformNameLabel = (Label) ((VBox) hBox.getChildren().get(1)).getChildren().get(0);
                clickToDownloadMapLink = (Hyperlink) ((StackPane) ((VBox) hBox.getChildren().get(1)).getChildren().get(8)).getChildren().get(2);
                break;

            case UP:
                vBox = (VBox) node;
                checkbox = (CheckBox) vBox.getChildren().get(3);
                mapNameLabel = (Label) vBox.getChildren().get(4);
                platformNameLabel = (Label) vBox.getChildren().get(0);
                clickToDownloadMapLink = (Hyperlink) ((StackPane) vBox.getChildren().get(8)).getChildren().get(2);
                break;

            case LEFT:
                hBox = (HBox) node;
                checkbox = (CheckBox) ((VBox) hBox.getChildren().get(0)).getChildren().get(3);
                mapNameLabel = (Label) ((VBox) hBox.getChildren().get(0)).getChildren().get(4);
                platformNameLabel = (Label) ((VBox) hBox.getChildren().get(0)).getChildren().get(0);
                clickToDownloadMapLink = (Hyperlink) ((StackPane) ((VBox) hBox.getChildren().get(0)).getChildren().get(8)).getChildren().get(2);
                break;

            case DETAILED:
                hBox = (HBox) node;
                checkbox = (CheckBox) ((Label) ((VBox) hBox.getChildren().get(1)).getChildren().get(0)).getGraphic();
                mapNameLabel = (Label) ((VBox) hBox.getChildren().get(2)).getChildren().get(2);
                platformNameLabel = (Label) ((VBox) hBox.getChildren().get(2)).getChildren().get(0);
                clickToDownloadMapLink = (Hyperlink) ((StackPane) ((VBox) hBox.getChildren().get(1)).getChildren().get(1)).getChildren().get(2);
                break;

            default:
                vBox = (VBox) node;
                checkbox = (CheckBox) vBox.getChildren().get(4);
                mapNameLabel = (Label) vBox.getChildren().get(5);
                platformNameLabel = (Label) vBox.getChildren().get(0);
                clickToDownloadMapLink = (Hyperlink) ((StackPane) vBox.getChildren().get(9)).getChildren().get(2);
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
}
