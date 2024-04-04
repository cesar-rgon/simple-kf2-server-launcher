package dtos;

import java.time.LocalDate;
import java.util.List;

public class CustomMapModDto extends AbstractMapDto {

    private final Long idWorkShop;
    private final Boolean map;

    public CustomMapModDto(String key, String urlInfo, String urlPhoto, Long idWorkShop, Boolean map, LocalDate releaseDate, List<ImportedDateByProfileDto> importedDateByProfileList) {
        super(key, urlInfo, urlPhoto, false, releaseDate, importedDateByProfileList);
        this.idWorkShop = idWorkShop;
        this.map = map;
    }

    public Long getIdWorkShop() {
        return idWorkShop;
    }

    public Boolean isMap() {
        return map;
    }

    @Override
    public String toString() {
        return "[custom] " + key;
    }

}
