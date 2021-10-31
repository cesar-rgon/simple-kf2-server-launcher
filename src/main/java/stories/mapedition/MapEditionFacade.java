package stories.mapedition;

import java.sql.SQLException;
import java.time.LocalDate;

public interface MapEditionFacade {
    String findConfigPropertyValue(String key) throws Exception;
    boolean updateMapSetUrlPhoto(String mapName, boolean isOfficial, String mapPreviewUrl) throws SQLException;
    boolean updateMapSetInfoUrl(String mapName, boolean isOfficial, String mapInfoUrl) throws SQLException;
    boolean updateMapSetReleaseDate(String mapName, boolean isOfficial, LocalDate releaseDate) throws SQLException;
}
