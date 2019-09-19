package dtos;

public class MapDto {

    private final String key;
    private final boolean official;
    private final String urlInfo;
    private final Long idWorkShop;
    private final String urlPhoto;
    private final boolean downloaded;
    private final Boolean mod;

    public MapDto(String key, boolean official, String urlInfo, Long idWorkShop, String urlPhoto, boolean downloaded, Boolean mod) {
        this.key = key;
        this.official = official;
        this.urlInfo = urlInfo;
        this.idWorkShop = idWorkShop;
        this.urlPhoto = urlPhoto;
        this.downloaded = downloaded;
        this.mod = mod;
    }

    public String getKey() {
        return key;
    }

    public boolean isOfficial() {
        return official;
    }

    public String getUrlInfo() {
        return urlInfo;
    }

    public Long getIdWorkShop() {
        return idWorkShop;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public Boolean getMod() {
        return mod;
    }

    @Override
    public String toString() {
        if (official) {
            return key;
        } else {
            return "[custom] " + key;
        }
    }
}
