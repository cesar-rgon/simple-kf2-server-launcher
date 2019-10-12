package pojos;

import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.ToggleGroup;

public class RadioButtonTableCell extends TableCell<ProfileToDisplay, Boolean> {
    private RadioButton radioButton;
    private ToggleGroup toggleGroup;

    public RadioButtonTableCell(ToggleGroup toggleGroup) {
        this.radioButton = new RadioButton();
        this.toggleGroup = toggleGroup;
        radioButton.setToggleGroup(toggleGroup);

        radioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            ProfileToDisplay actualProfileToDisplay = (ProfileToDisplay) getTableRow().getItem();
            actualProfileToDisplay.setSelected(newValue);
        });
    }

    @Override
    protected void updateItem(Boolean select, boolean empty)
    {
        super.updateItem(select, empty);
        radioButton.setToggleGroup(toggleGroup);

        if (empty || select == null) {
            radioButton.setSelected(false);
            setGraphic(null);
        } else {
            radioButton.setSelected(select);
            setGraphic(radioButton);
        }
    }
}