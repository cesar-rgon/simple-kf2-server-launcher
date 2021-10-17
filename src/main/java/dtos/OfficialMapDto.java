package dtos;

public class OfficialMapDto extends AbstractMapDto {

    public OfficialMapDto(String key, String urlInfo, String urlPhoto, String importedDate, String releaseDate) {
        super(key, urlInfo, urlPhoto, true, importedDate, releaseDate);
    }

    @Override
    public String toString() {
        return key;
    }
}
