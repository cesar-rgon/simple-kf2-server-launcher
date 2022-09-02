package dtos;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlatformDto extends SelectDto {

    private final StringProperty logoPath;
    private final StringProperty smallLogoPath;

    public PlatformDto(String key, String value, String logoPath, String smallLogoPath) {
        super(key, value);
        this.logoPath = new SimpleStringProperty(logoPath);
        this.smallLogoPath = new SimpleStringProperty(smallLogoPath);
    }

    public String getLogoPath() {
        return logoPath.get();
    }

    public StringProperty getLogoPathProperty() {
        return logoPath;
    }

    public String getSmallLogoPath() {
        return smallLogoPath.get();
    }

    public StringProperty getSmallLogoPathProperty() {
        return smallLogoPath;
    }
}
