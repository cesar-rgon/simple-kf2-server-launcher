package services;

import daos.OfficialMapDao;
import daos.PlatformProfileMapDao;
import entities.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.ImportMapResultToDisplay;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractMapService implements AbstractExtendedService<AbstractMap> {

    private static final Logger logger = LogManager.getLogger(AbstractMapService.class);
    private PlatformProfileMapService platformProfileMapService;

    protected AbstractMapService() {
        super();
        this.platformProfileMapService = new PlatformProfileMapServiceImpl();
    }

    @Override
    public boolean updateItemCode(AbstractMap map, String oldCode) throws Exception {
        return false;
    }

    @Override
    public void updateItemDescription(AbstractMap map) throws Exception {
    }

    public abstract List<AbstractMap> listAll() throws SQLException;
    public abstract Optional<AbstractMap> findByCode(String mapName) throws SQLException;
    public abstract AbstractMap createItem(AbstractMap map) throws Exception;
    public abstract boolean deleteItem(AbstractMap map) throws Exception;
    public abstract boolean updateItem(AbstractMap map) throws SQLException;


    public Optional<AbstractMap> findMapByCode(String mapName) throws SQLException {
        return findByCode(mapName);
    }

    public List<AbstractMap> listAllMaps() throws SQLException {
        return listAll();
    }

    public AbstractMap createMap(List<AbstractPlatform> platformList, AbstractMap map, List<Profile> profileList, StringBuffer success, StringBuffer errors) {
        AbstractMap insertedMap = null;
        boolean downloaded = false;
        try {
            insertedMap = createItem(map);
            if (insertedMap == null) {
                String errorMessage = "Map to be created not found: " + map.getCode();
                errors.append(errorMessage + "\n");
                logger.error(errorMessage);
                return null;
            }
            downloaded = OfficialMapDao.getInstance().findByCode(map.getCode()).isPresent();
        } catch (Exception e) {
            String errorMessage = "Error creating the map: " + map.getCode();
            errors.append(errorMessage + "\n");
            logger.error(errorMessage, e);
            return null;
        }

        AbstractMap finalInsertedMap = insertedMap;
        boolean finalDownloaded = downloaded;
        profileList.stream().forEach(profile -> {
            platformList.stream().forEach(platform -> {
                try {
                    PlatformProfileMap newPlatformProfileMap = new PlatformProfileMap(platform, profile, finalInsertedMap, map.getReleaseDate(), map.getUrlInfo(), map.getUrlPhoto(), finalDownloaded);
                    platformProfileMapService.createItem(newPlatformProfileMap);

                    String successMessage = "The relation between the map " + map.getCode() + ", the profile " + profile.getCode() + " and the platform " + platform.getDescription() + " has been created";
                    success.append(successMessage + "\n");
                } catch (Exception e) {
                    String errorMessage = "Error creating the relation between the map " + map.getCode() + ", the profile " + profile.getCode() + " and the platform " + platform.getDescription();
                    errors.append(errorMessage + "\n");
                    logger.error(errorMessage, e);
                }
            });
        });

        return insertedMap;
    }

    public void addPlatformProfileMapList(List<PlatformProfileMap> platformProfileMapListToAdd, StringBuffer success, StringBuffer errors) {
        platformProfileMapListToAdd.stream().forEach(ppm -> {
            try {
                platformProfileMapService.createItem(ppm);
                String successMessage = "The relation between the map " + ppm.getMap().getCode() + ", the profile " + ppm.getProfile().getCode() + " and the platform " + ppm.getPlatform().getDescription() + " has been created";
                success.append(successMessage + "\n");
            } catch (Exception e) {
                String errorMessage = "Error creating the relation between the map " + ppm.getMap().getCode() + ", the profile " + ppm.getProfile().getCode() + " and the platform " + ppm.getPlatform().getDescription();
                errors.append(errorMessage + "\n");
                logger.error(errorMessage, e);
            }
        });
    }


    protected AbstractMap deleteMap(AbstractPlatform platform, AbstractMap map, Profile profile) throws Exception {
        PlatformProfileMap.IdPlatformProfileMap idPlatformProfileMap = new PlatformProfileMap.IdPlatformProfileMap(platform.getId(), profile.getId(), map.getId());
        PlatformProfileMap platformProfileMap = PlatformProfileMapDao.getInstance().get(idPlatformProfileMap);
        platformProfileMapService.deleteItem(platformProfileMap);
        return map;
    }

}
