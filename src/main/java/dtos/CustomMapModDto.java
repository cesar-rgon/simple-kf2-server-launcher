package dtos;

import java.time.LocalDate;
import java.util.List;

public class CustomMapModDto extends AbstractMapDto {

    private final Long idWorkShop;
    private final boolean downloaded;

    public CustomMapModDto(String key, String urlInfo, String urlPhoto, Long idWorkShop, boolean downloaded, LocalDate releaseDate, List<ImportedDateByProfileDto> importedDateByProfileList) {
        super(key, urlInfo, urlPhoto, false, releaseDate, importedDateByProfileList);
        this.idWorkShop = idWorkShop;
        this.downloaded = downloaded;
    }

    public Long getIdWorkShop() {
        return idWorkShop;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    @Override
    public String toString() {
        return "[custom] " + key;
    }

}
