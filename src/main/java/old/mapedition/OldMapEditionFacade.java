package old.mapedition;

import entities.PlatformProfileMap;

import java.time.LocalDate;

public interface OldMapEditionFacade {
    String findConfigPropertyValue(String key) throws Exception;
    boolean updateMapSetUrlPhoto(PlatformProfileMap platformProfileMap, String mapPreviewUrl) throws Exception;
    boolean updateMapSetInfoUrl(PlatformProfileMap platformProfileMap, String mapInfoUrl) throws Exception;
    boolean updateMapSetReleaseDate(PlatformProfileMap platformProfileMap, LocalDate releaseDate) throws Exception;
    boolean updateMapSetAlias(PlatformProfileMap platformProfileMap, String alias) throws Exception;
}
