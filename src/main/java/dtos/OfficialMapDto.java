package dtos;

import java.util.List;

public class OfficialMapDto extends AbstractMapDto {

    public OfficialMapDto(String key, String urlInfo, String urlPhoto, String releaseDate, List<ImportedDateByProfileDto> importedDateByProfileList) {
        super(key, urlInfo, urlPhoto, true, releaseDate, importedDateByProfileList);
    }

    @Override
    public String toString() {
        return key;
    }
}
