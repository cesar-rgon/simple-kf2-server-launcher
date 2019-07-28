package dtos;

import org.apache.commons.lang3.StringUtils;

public class MapDto {

    private final String key;
    private final Boolean official;
    private final String urlInfo;
    private final Long idWorkShop;
    private final String urlPhoto;
    private final Boolean downloaded;

    public MapDto(String key, Boolean official, String urlInfo, Long idWorkShop, String urlPhoto, Boolean downloaded) {
        this.key = key;
        this.official = official;
        this.urlInfo = urlInfo;
        this.idWorkShop = idWorkShop;
        this.urlPhoto = urlPhoto;
        this.downloaded = downloaded;
    }

    public String getKey() {
        return key;
    }

    public Boolean getOfficial() {
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

    public Boolean getDownloaded() {
        return downloaded;
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
