package dtos;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SelectDto {

    private final StringProperty key;
    private final StringProperty value;

    public SelectDto(String key, String value) {
        this.key = new SimpleStringProperty(key);
        this.value = new SimpleStringProperty(value);
    }

    public StringProperty getKeyProperty() {
        return key;
    }

    public StringProperty getValueProperty() {
        return value;
    }

    public String getKey() {
        return key.get();
    }

    public String getValue() {
        return value.get();
    }

    @Override
    public String toString() {
        return value.get();
    }
}
