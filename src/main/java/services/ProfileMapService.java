package services;

import entities.Profile;
import entities.ProfileMap;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProfileMapService extends AbstractService<ProfileMap> {

    Optional<ProfileMap> findProfileMapByNames(String profileName, String mapName) throws SQLException;
    List<ProfileMap> listProfileMaps(Profile profile) throws SQLException;
}
