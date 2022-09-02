package stories.mapedition;

import entities.PlatformProfileMap;

import java.sql.SQLException;
import java.time.LocalDate;

public interface MapEditionFacade {
    String findConfigPropertyValue(String key) throws Exception;
    boolean updateMapSetUrlPhoto(PlatformProfileMap platformProfileMap, String mapPreviewUrl) throws SQLException;
    boolean updateMapSetInfoUrl(PlatformProfileMap platformProfileMap, String mapInfoUrl) throws SQLException;
    boolean updateMapSetReleaseDate(PlatformProfileMap platformProfileMap, LocalDate releaseDate) throws SQLException;
    boolean updateMapSetAlias(PlatformProfileMap platformProfileMap, String alias) throws SQLException;
}
