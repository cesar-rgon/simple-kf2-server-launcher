package services;

import daos.EpicPlatformDao;
import daos.SteamPlatformDao;
import entities.AbstractPlatform;
import entities.EpicPlatform;
import entities.SteamPlatform;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PlatformService {

    List<AbstractPlatform> listAllPlatforms() throws SQLException;
    Optional<AbstractPlatform> findPlatformByName(String platformName) throws SQLException;
    Optional<SteamPlatform> findSteamPlatform() throws SQLException;
    Optional<EpicPlatform> findEpicPlatform() throws SQLException;
    SteamPlatform createSteamPlatform(SteamPlatform steamPlatform) throws SQLException;
    EpicPlatform createEpicPlatform(EpicPlatform epicPlatform) throws SQLException;
    boolean updateSteamPlatform(SteamPlatform steamPlatform) throws Exception;
    boolean updateEpicPlatform(EpicPlatform epicPlatform) throws Exception;

}
