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
        Optional<AbstractMap> mapOpt = findByCode(mapName);
        List<Integer> idsMapasOficiales = OfficialMapDao.getInstance().listAll().stream().map(OfficialMap::getId).collect(Collectors.toList());
        if (mapOpt.isPresent()) {
            if (idsMapasOficiales.contains(mapOpt.get().getId())) {
                mapOpt.get().setOfficial(true);
            } else {
                mapOpt.get().setOfficial(false);
            }
        }
        return mapOpt;
    }

    public List<AbstractMap> listAllMaps() throws SQLException {
        List<AbstractMap> mapList = listAll();
        List<Integer> idsMapasOficiales = OfficialMapDao.getInstance().listAll().stream().map(OfficialMap::getId).collect(Collectors.toList());
        if (mapList != null && !mapList.isEmpty()) {
            mapList.forEach(m -> {
                if (idsMapasOficiales.contains(m.getId())) {
                    m.setOfficial(true);
                } else {
                    m.setOfficial(false);
                }
            });
        }
        return mapList;
    }

    public AbstractMap createMap(AbstractPlatform platform, AbstractMap map, List<Profile> profileList, List<ImportMapResultToDisplay> importMapResultToDisplayList) {
        AbstractMap insertedMap = null;
        try {
            List<Integer> idsMapasOficiales = OfficialMapDao.getInstance().listAll().stream().map(OfficialMap::getId).collect(Collectors.toList());
            if (idsMapasOficiales.contains(map.getId())) {
                map.setOfficial(true);
            } else {
                map.setOfficial(false);
            }
            insertedMap = createItem(map);
            if (insertedMap == null) {
                String errorMessage = "Map to be created not found: " + map.getCode();
                logger.error(errorMessage);
                if (importMapResultToDisplayList != null) {
                    profileList.stream().forEach(profile -> {
                        importMapResultToDisplayList.add(new ImportMapResultToDisplay(
                                profile.getName(),
                                map.getCode(),
                                map.isOfficial(),
                                null,
                                errorMessage
                        ));
                    });
                }
                return null;
            }
        } catch (Exception e) {
            String errorMessage = "Error creating the map: " + map.getCode();
            logger.error(errorMessage, e);
            if (importMapResultToDisplayList != null) {
                profileList.stream().forEach(profile -> {
                    importMapResultToDisplayList.add(new ImportMapResultToDisplay(
                            profile.getName(),
                            map.getCode(),
                            map.isOfficial(),
                            null,
                            errorMessage
                    ));
                });
            }
            return null;
        }

        AbstractMap finalInsertedMap = insertedMap;
        profileList.stream().forEach(profile -> {
            try {
                PlatformProfileMap newPlatformProfileMap = new PlatformProfileMap(platform, profile, finalInsertedMap, map.getReleaseDate(), map.getUrlInfo(), map.getUrlPhoto());
                platformProfileMapService.createItem(newPlatformProfileMap);
                finalInsertedMap.getProfileMapList().add(newPlatformProfileMap);

                if (importMapResultToDisplayList != null) {
                    importMapResultToDisplayList.add(new ImportMapResultToDisplay(
                            profile.getName(),
                            finalInsertedMap.getCode(),
                            finalInsertedMap.isOfficial(),
                            new Date(),
                            StringUtils.EMPTY
                    ));
                }
            } catch (Exception e) {
                String errorMessage = "Error creating the relation between the profile: " + profile.getCode() + " and the new map: " + map.getCode();
                logger.error(errorMessage, e);

                if (importMapResultToDisplayList != null) {
                    importMapResultToDisplayList.add(new ImportMapResultToDisplay(
                            profile.getName(),
                            map.getCode(),
                            map.isOfficial(),
                            null,
                            errorMessage
                    ));
                }
            }
        });

        return insertedMap;
    }

    public AbstractMap addProfilesToMap(AbstractPlatform platform, AbstractMap map, List<Profile> profileList, List<ImportMapResultToDisplay> importMapResultToDisplayList) {
        profileList.stream().forEach(profile -> {
            try {
                if (!profile.getMapList().contains(map)) {
                    PlatformProfileMap newPlatformProfileMap = new PlatformProfileMap(platform, profile, map, map.getReleaseDate(), map.getUrlInfo(), map.getUrlPhoto());
                    platformProfileMapService.createItem(newPlatformProfileMap);
                    map.getProfileMapList().add(newPlatformProfileMap);

                    if (importMapResultToDisplayList != null) {
                        importMapResultToDisplayList.add(new ImportMapResultToDisplay(
                                profile.getName(),
                                map.getCode(),
                                map.isOfficial(),
                                new Date(),
                                StringUtils.EMPTY
                        ));
                    }
                }
            } catch (Exception e) {
                String errorMessage = "Error creating the relation between the profile: " + profile.getCode() + " and the new map: " + map.getCode();
                logger.error(errorMessage, e);

                if (importMapResultToDisplayList != null) {
                    importMapResultToDisplayList.add(new ImportMapResultToDisplay(
                            profile.getName(),
                            map.getCode(),
                            map.isOfficial(),
                            null,
                            errorMessage
                    ));
                }
            }
        });
        return map;
    }


    protected AbstractMap deleteMap(AbstractPlatform platform, AbstractMap map, Profile profile) throws Exception {
        PlatformProfileMap.IdPlatformProfileMap idPlatformProfileMap = new PlatformProfileMap.IdPlatformProfileMap(platform.getId(), profile.getId(), map.getId());
        PlatformProfileMap platformProfileMap = PlatformProfileMapDao.getInstance().get(idPlatformProfileMap);
        platformProfileMapService.deleteItem(platformProfileMap);
        return map;
    }

}
