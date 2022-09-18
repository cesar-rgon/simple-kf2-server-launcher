package pojos;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pojos.enums.EnumPlatform;

public class AddMapsToPlatformProfile {

    private StringProperty profileName;
    private StringProperty platformName;
    private StringProperty mapList;

    public AddMapsToPlatformProfile(String profileName) {
        super();
        this.profileName = new SimpleStringProperty(profileName);
        this.platformName = new SimpleStringProperty(EnumPlatform.ALL.getDescripcion());
        this.mapList = new SimpleStringProperty();
    }

    public String getProfileName() {
        return profileName.get();
    }

    public StringProperty profileNameProperty() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName.set(profileName);
    }

    public String getMapList() {
        return mapList.get();
    }

    public StringProperty mapListProperty() {
        return mapList;
    }

    public void setMapList(String mapList) {
        this.mapList.set(mapList);
    }

    public String getPlatformName() {
        return platformName.get();
    }

    public StringProperty platformNameProperty() {
        return platformName;
    }

}
