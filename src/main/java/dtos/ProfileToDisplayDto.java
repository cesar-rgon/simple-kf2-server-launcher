package dtos;

public class ProfileToDisplayDto {

    private final int profileFileIndex;
    private final String profileName;
    private final String gameTypeDescription;
    private final String mapName;
    private final String difficultyDescription;
    private final String lengthDescription;

    public ProfileToDisplayDto(int profileFileIndex,
                               String profileName,
                               String gameTypeDescription,
                               String mapName,
                               String difficultyDescription,
                               String lengthDescription) {

        super();
        this.profileFileIndex = profileFileIndex;
        this.profileName = profileName;
        this.gameTypeDescription = gameTypeDescription;
        this.mapName = mapName;
        this.difficultyDescription = difficultyDescription;
        this.lengthDescription = lengthDescription;
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

    @Override
    public String toString() {
        return gameTypeDescription + ", " + mapName + ", " + difficultyDescription + ", " + lengthDescription;
    }
}
