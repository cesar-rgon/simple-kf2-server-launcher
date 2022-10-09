package pojos;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProfileToDisplay {

    private int profileFileIndex;
    private BooleanProperty selected;
    private StringProperty profileName;
    private StringProperty gameTypeDescription;
    private StringProperty mapName;
    private StringProperty difficultyDescription;
    private StringProperty lengthDescription;

    public ProfileToDisplay(int profileFileIndex,
                            String profileName,
                            String gameTypeDescription,
                            String mapName,
                            String difficultyDescription,
                            String lengthDescription) {

        super();
        this.selected = new SimpleBooleanProperty(false);
        this.profileFileIndex = profileFileIndex;
        this.profileName = new SimpleStringProperty(profileName);
        this.gameTypeDescription = new SimpleStringProperty(gameTypeDescription);
        this.mapName = new SimpleStringProperty(mapName);
        this.difficultyDescription = new SimpleStringProperty(difficultyDescription);
        this.lengthDescription = new SimpleStringProperty(lengthDescription);
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

    public String getGameTypeDescription() {
        return gameTypeDescription.get();
    }

    public StringProperty gameTypeDescriptionProperty() {
        return gameTypeDescription;
    }

    public void setGameTypeDescription(String gameTypeDescription) {
        this.gameTypeDescription.set(gameTypeDescription);
    }

    public String getMapName() {
        return mapName.get();
    }

    public StringProperty mapNameProperty() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName.set(mapName);
    }

    public String getDifficultyDescription() {
        return difficultyDescription.get();
    }

    public StringProperty difficultyDescriptionProperty() {
        return difficultyDescription;
    }

    public void setDifficultyDescription(String difficultyDescription) {
        this.difficultyDescription.set(difficultyDescription);
    }

    public String getLengthDescription() {
        return lengthDescription.get();
    }

    public StringProperty lengthDescriptionProperty() {
        return lengthDescription;
    }

    public void setLengthDescription(String lengthDescription) {
        this.lengthDescription.set(lengthDescription);
    }

    @Override
    public String toString() {
        return gameTypeDescription + ", " + mapName + ", " + difficultyDescription + ", " + lengthDescription;
    }
}
