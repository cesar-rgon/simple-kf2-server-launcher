package services;

import daos.OfficialMapDao;
import daos.PlatformProfileMapDao;
import entities.*;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.ImportMapResultToDisplay;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractMapService implements AbstractService<AbstractMap> {

    private static final Logger logger = LogManager.getLogger(AbstractMapService.class);
    private final PlatformProfileMapService platformProfileMapService;

    protected AbstractMapService(EntityManager em) {
        super();
        this.platformProfileMapService = new PlatformProfileMapServiceImpl(em);
    }

    public boolean updateItemCode(AbstractMap map, String oldCode) throws Exception {
        return false;
    }

    public void updateItemDescription(AbstractMap map) throws Exception {
    }

    public abstract List<AbstractMap> listAll() throws SQLException;
    public abstract Optional<AbstractMap> findByCode(String mapName) throws Exception;
    public abstract AbstractMap createItem(AbstractMap map) throws Exception;
    public abstract boolean deleteItem(AbstractMap map) throws Exception;
    public abstract boolean updateItem(AbstractMap map) throws SQLException;
    protected abstract boolean idDownloadedMap();

    public Optional<AbstractMap> findMapByCode(String mapName) throws Exception {
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
            downloaded = idDownloadedMap();
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
        Optional<PlatformProfileMap> platformProfileMapOptional = platformProfileMapService.findPlatformProfileMapByNames(platform.getCode(), profile.getName(), map.getCode());
        if (platformProfileMapOptional.isPresent()) {
            platformProfileMapService.deleteItem(platformProfileMapOptional.get());
            return map;
        }
        return null;
    }

}
