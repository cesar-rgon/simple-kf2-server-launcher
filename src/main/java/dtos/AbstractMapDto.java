package dtos;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractMapDto {

    protected final String key;
    protected final String urlInfo;
    protected final String urlPhoto;
    protected final String releaseDate;
    private final boolean official;
    private final List<ImportedDateByProfileDto> importedDateByProfileDtoList;

    protected AbstractMapDto(String key, String urlInfo, String urlPhoto, boolean official, String releaseDate, List<ImportedDateByProfileDto> importedDateByProfileList) {
        this.key = key;
        this.urlInfo = urlInfo;
        this.urlPhoto = urlPhoto;
        this.official = official;
        this.releaseDate = releaseDate;
        this.importedDateByProfileDtoList = importedDateByProfileList;
    }

    public String getKey() {
        return key;
    }

    public String getUrlInfo() {
        return urlInfo;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public boolean isOfficial() {
        return official;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public List<ImportedDateByProfileDto> getImportedDateByProfileDtoList() {
        return importedDateByProfileDtoList;
    }

    public String getImportedDate(String profileName) {
        Optional<ImportedDateByProfileDto> importedDateByProfileDtoOpt = importedDateByProfileDtoList.stream().
                filter(importedDateByProfileDto -> importedDateByProfileDto.getProfileName().equals(profileName)).
                findFirst();

        if (importedDateByProfileDtoOpt.isPresent()) {
            return importedDateByProfileDtoOpt.get().getImportedDate();
        } else {
            return "Unknown";
        }
    }
}
