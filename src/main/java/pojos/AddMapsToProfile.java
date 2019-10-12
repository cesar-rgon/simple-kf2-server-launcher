package pojos;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AddMapsToProfile {

    private StringProperty profileName;
    private StringProperty mapList;

    public AddMapsToProfile(String profileName) {
        super();
        this.profileName = new SimpleStringProperty(profileName);
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
}
