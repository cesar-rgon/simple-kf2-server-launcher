package stories.mapedition;

import entities.ProfileMap;

import java.sql.SQLException;
import java.time.LocalDate;

public interface MapEditionFacade {
    String findConfigPropertyValue(String key) throws Exception;
    boolean updateMapSetUrlPhoto(ProfileMap profileMap, String mapPreviewUrl) throws SQLException;
    boolean updateMapSetInfoUrl(ProfileMap profileMap, String mapInfoUrl) throws SQLException;
    boolean updateMapSetReleaseDate(ProfileMap profileMap, LocalDate releaseDate) throws SQLException;
    boolean updateMapSetAlias(ProfileMap profileMap, String alias) throws SQLException;
}
