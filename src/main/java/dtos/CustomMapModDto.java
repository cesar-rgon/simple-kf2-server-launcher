package dtos;

import java.time.LocalDate;
import java.util.List;

public class CustomMapModDto extends AbstractMapDto {

    private final Long idWorkShop;

    public CustomMapModDto(String key, String urlInfo, String urlPhoto, Long idWorkShop, LocalDate releaseDate, List<ImportedDateByProfileDto> importedDateByProfileList) {
        super(key, urlInfo, urlPhoto, false, releaseDate, importedDateByProfileList);
        this.idWorkShop = idWorkShop;
    }

    public Long getIdWorkShop() {
        return idWorkShop;
    }

    @Override
    public String toString() {
        return "[custom] " + key;
    }

}
