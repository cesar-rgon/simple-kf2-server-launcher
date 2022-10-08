package services;

import entities.AbstractMap;
import entities.AbstractPlatform;
import entities.Profile;
import entities.PlatformProfileMap;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PlatformProfileMapService extends AbstractService<PlatformProfileMap> {

    Optional<PlatformProfileMap> findPlatformProfileMapByNames(String platformName, String profileName, String mapName) throws SQLException;
    List<PlatformProfileMap> listPlatformProfileMaps(AbstractPlatform platform, Profile profile) throws SQLException;
    List<PlatformProfileMap> listPlatformProfileMaps(List<AbstractPlatform> platformList, List<Profile> profileList) throws SQLException;
    List<PlatformProfileMap> listPlatformProfileMaps(AbstractMap map) throws SQLException;
    List<PlatformProfileMap> listPlatformProfileMaps(Profile profile) throws SQLException;
    List<PlatformProfileMap> listPlatformProfileMaps(AbstractPlatform platform) throws SQLException;
    List<PlatformProfileMap> listPlatformProfileMaps(AbstractPlatform platform, AbstractMap map) throws SQLException;
}
