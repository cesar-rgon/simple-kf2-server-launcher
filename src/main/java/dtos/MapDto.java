package dtos;

public class MapDto {

    private final String key;
    private final String value;
    private final Boolean official;
    private final String urlInfo;

    public MapDto(String key, String value, Boolean official, String urlInfo) {
        this.key = key;
        this.value = value;
        this.official = official;
        this.urlInfo = urlInfo;
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

    @Override
    public String toString() {
        return value;
    }
}
