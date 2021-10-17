package dtos;

public abstract class AbstractMapDto {

    protected final String key;
    protected final String urlInfo;
    protected final String urlPhoto;
    protected final String importedDate;
    protected final String releaseDate;
    private final boolean official;

    protected AbstractMapDto(String key, String urlInfo, String urlPhoto, boolean official, String importedDate, String releaseDate) {
        this.key = key;
        this.urlInfo = urlInfo;
        this.urlPhoto = urlPhoto;
        this.official = official;
        this.importedDate = importedDate;
        this.releaseDate = releaseDate;
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

    public String getImportedDate() {
        return importedDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
