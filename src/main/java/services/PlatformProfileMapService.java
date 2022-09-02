package services;

import entities.Platform;
import entities.Profile;
import entities.PlatformProfileMap;
import pojos.enums.EnumPlatform;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PlatformProfileMapService extends AbstractService<PlatformProfileMap> {

    Optional<PlatformProfileMap> findPlatformProfileMapByNames(String platformName, String profileName, String mapName) throws SQLException;
    List<PlatformProfileMap> listPlatformProfileMaps(Platform platform, Profile profile) throws SQLException;
}
