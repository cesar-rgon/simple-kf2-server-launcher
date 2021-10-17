package dtos;

public class CustomMapModDto extends AbstractMapDto {

    private final Long idWorkShop;
    private final boolean downloaded;

    public CustomMapModDto(String key, String urlInfo, String urlPhoto, Long idWorkShop, boolean downloaded, String importedDate, String releaseDate) {
        super(key, urlInfo, urlPhoto, false, importedDate, releaseDate);
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
