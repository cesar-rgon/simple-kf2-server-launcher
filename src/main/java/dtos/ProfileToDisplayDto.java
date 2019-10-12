package dtos;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProfileToDisplayDto {

    private final int profileFileIndex;
    private final String profileName;
    private final StringProperty profileNameProperty;
    private final String gameTypeDescription;
    private final StringProperty gameTypeDescriptionProperty;
    private final String mapName;
    private final StringProperty mapNameProperty;
    private final String difficultyDescription;
    private final StringProperty difficultyDescriptionProperty;
    private final String lengthDescription;
    private final StringProperty lengthDescriptionProperty;

    public ProfileToDisplayDto(int profileFileIndex,
                               String profileName,
                               String gameTypeDescription,
                               String mapName,
                               String difficultyDescription,
                               String lengthDescription) {

        super();
        this.profileFileIndex = profileFileIndex;
        this.profileName = profileName;
        this.profileNameProperty = new SimpleStringProperty(profileName);
        this.gameTypeDescription = gameTypeDescription;
        this.gameTypeDescriptionProperty = new SimpleStringProperty(gameTypeDescription);
        this.mapName = mapName;
        this.mapNameProperty = new SimpleStringProperty(mapName);
        this.difficultyDescription = difficultyDescription;
        this.difficultyDescriptionProperty = new SimpleStringProperty(difficultyDescription);
        this.lengthDescription = lengthDescription;
        this.lengthDescriptionProperty = new SimpleStringProperty(lengthDescription);
    }

    public int getProfileFileIndex() {
        return profileFileIndex;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getGameTypeDescription() {
        return gameTypeDescription;
    }

    public String getMapName() {
        return mapName;
    }

    public String getDifficultyDescription() {
        return difficultyDescription;
    }

    public String getLengthDescription() {
        return lengthDescription;
    }

    public StringProperty profileNameProperty() {
        return profileNameProperty;
    }

    public StringProperty gameTypeDescriptionProperty() {
        return gameTypeDescriptionProperty;
    }

    public StringProperty mapNameProperty() {
        return mapNameProperty;
    }

    public StringProperty difficultyDescriptionProperty() {
        return difficultyDescriptionProperty;
    }

    public StringProperty lengthDescriptionProperty() {
        return lengthDescriptionProperty;
    }

    @Override
    public String toString() {
        return gameTypeDescription + ", " + mapName + ", " + difficultyDescription + ", " + lengthDescription;
    }
}
