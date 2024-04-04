package stories.mapedition;

import java.time.LocalDate;

public interface MapEditionManagerFacade {

    String findPropertyValue(String propertyFilePath, String key) throws Exception;
    void updateMapSetAlias(String platformName, String profileName, String mapName, String newAlias) throws Exception;
    void updateMapSetUrlPhoto(String platformName, String profileName, String mapName, String mapPreviewUrl) throws Exception;
    void updateMapSetInfoUrl(String platformName, String profileName, String mapName, String infoUrl) throws Exception;
    void updateMapSetReleaseDate(String platformName, String profileName, String mapName, LocalDate releaseDate) throws Exception;
    void updateMapSetItemType(String mapName, boolean isMap) throws Exception;
}
