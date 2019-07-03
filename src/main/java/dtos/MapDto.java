package dtos;

import org.apache.commons.lang3.StringUtils;

public class MapDto {

    private final String key;
    private final String value;
    private final Boolean official;
    private final String urlInfo;
    private final Long idWorkShop;
    private final String urlPhoto;

    public MapDto(String key, String value, Boolean official, String urlInfo, Long idWorkShop, String urlPhoto) {
        this.key = key;
        this.value = value;
        this.official = official;
        this.urlInfo = urlInfo;
        this.idWorkShop = idWorkShop;
        this.urlPhoto = urlPhoto;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
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

    @Override
    public String toString() {
        if (StringUtils.isNotBlank(value)) {
            return value;
        } else {
            return key;
        }
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }
}
