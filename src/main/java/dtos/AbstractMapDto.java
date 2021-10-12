package dtos;

public abstract class AbstractMapDto {

    protected final String key;
    protected final String urlInfo;
    protected final String urlPhoto;
    private final boolean official;

    protected AbstractMapDto(String key, String urlInfo, String urlPhoto, boolean official) {
        this.key = key;
        this.urlInfo = urlInfo;
        this.urlPhoto = urlPhoto;
        this.official = official;
    }

    public String getKey() {
        return key;
    }

    public String getUrlInfo() {
        return urlInfo;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public boolean isOfficial() {
        return official;
    }
}
