package pojos;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import pojos.enums.EnumPlatform;

public class PlatformProfileToDisplay {

    private int profileFileIndex;
    private BooleanProperty selected;
    private StringProperty profileName;
    private StringProperty platformName;
    private IntegerProperty webPort;
    private IntegerProperty gamePort;
    private IntegerProperty queryPort;

    public PlatformProfileToDisplay(EnumPlatform enumPlatform,
                                    int profileFileIndex,
                                    String profileName,
                                    Integer webPort,
                                    Integer gamePort,
                                    Integer queryPort) {

        super();
        this.platformName = new SimpleStringProperty(enumPlatform.getDescripcion());
        this.selected = new SimpleBooleanProperty(false);
        this.profileFileIndex = profileFileIndex;
        this.profileName = new SimpleStringProperty(profileName);
        this.webPort = new SimpleIntegerProperty(webPort);
        this.gamePort = new SimpleIntegerProperty(gamePort);
        this.queryPort = new SimpleIntegerProperty(queryPort);
    }

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public int getProfileFileIndex() {
        return profileFileIndex;
    }

    public void setProfileFileIndex(int profileFileIndex) {
        this.profileFileIndex = profileFileIndex;
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

    public String getPlatformName() {
        return platformName.get();
    }

    public StringProperty platformNameProperty() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName.set(platformName);
    }

    public int getWebPort() {
        return webPort.get();
    }

    public ObservableValue<Integer> webPortProperty() {
        return webPort.asObject();
    }

    public void setWebPort(int webPort) {
        this.webPort.set(webPort);
    }

    public int getGamePort() {
        return gamePort.get();
    }

    public ObservableValue<Integer> gamePortProperty() {
        return gamePort.asObject();
    }

    public void setGamePort(int gamePort) {
        this.gamePort.set(gamePort);
    }

    public int getQueryPort() {
        return queryPort.get();
    }

    public ObservableValue<Integer> queryPortProperty() {
        return queryPort.asObject();
    }

    public void setQueryPort(int queryPort) {
        this.queryPort.set(queryPort);
    }

    @Override
    public String toString() {
        return webPort + ", " + gamePort + ", " + queryPort;
    }
}
