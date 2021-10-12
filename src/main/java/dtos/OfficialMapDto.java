package dtos;

public class OfficialMapDto extends AbstractMapDto {

    public OfficialMapDto(String key, String urlInfo, String urlPhoto) {
        super(key, urlInfo, urlPhoto, true);
    }

    @Override
    public String toString() {
        return key;
    }
}
