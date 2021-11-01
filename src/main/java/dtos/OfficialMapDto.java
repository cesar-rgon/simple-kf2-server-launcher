package dtos;

import java.time.LocalDate;
import java.util.List;

public class OfficialMapDto extends AbstractMapDto {

    public OfficialMapDto(String key, String alias, String urlInfo, String urlPhoto, LocalDate releaseDate, List<ImportedDateByProfileDto> importedDateByProfileList) {
        super(key, alias, urlInfo, urlPhoto, true, releaseDate, importedDateByProfileList);
    }

    @Override
    public String toString() {
        return key;
    }
}
