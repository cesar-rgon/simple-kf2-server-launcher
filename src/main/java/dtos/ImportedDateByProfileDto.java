package dtos;

public class ImportedDateByProfileDto {

    private final String profileName;
    private final String importedDate;

    public ImportedDateByProfileDto(String profileName, String importedDate) {
        super();
        this.profileName = profileName;
        this.importedDate = importedDate;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getImportedDate() {
        return importedDate;
    }
}
