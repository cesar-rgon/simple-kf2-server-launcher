package services;

import daos.EpicPlatformDao;
import daos.SteamPlatformDao;
import entities.AbstractPlatform;
import entities.EpicPlatform;
import entities.PlatformProfileMap;
import entities.SteamPlatform;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import pojos.enums.EnumPlatform;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlatformServiceImpl implements PlatformService {

    private static final Logger logger = LogManager.getLogger(PlatformServiceImpl.class);

    private final EntityManager em;
    private final PlatformProfileMapService platformProfileMapService;

    public PlatformServiceImpl(EntityManager em) {
        super();
        this.em = em;
        this.platformProfileMapService = new PlatformProfileMapServiceImpl(em);
    }

    @Override
    public List<AbstractPlatform> listAllPlatforms() throws SQLException  {
        Optional<SteamPlatform> steamPlatformOptional = new SteamPlatformDao(em).findByCode(EnumPlatform.STEAM.name());
        Optional<EpicPlatform> epicPlatformOptional = new EpicPlatformDao(em).findByCode(EnumPlatform.EPIC.name());

        List<AbstractPlatform> result = new ArrayList<AbstractPlatform>();
        if (steamPlatformOptional.isPresent()) {
            result.add(steamPlatformOptional.get());
        }
        if (epicPlatformOptional.isPresent()) {
            result.add(epicPlatformOptional.get());
        }
        return result;
    }


    @Override
    public Optional<AbstractPlatform> findPlatformByName(String platformName) throws SQLException {
        Optional<SteamPlatform> steamPlatformOptional = new SteamPlatformDao(em).findByCode(platformName);
        if (steamPlatformOptional.isPresent()) {
            SteamPlatform steamPlatform = steamPlatformOptional.get();
            return Optional.ofNullable(steamPlatform);
        }

        Optional<EpicPlatform> epicPlatformOptional = new EpicPlatformDao(em).findByCode(platformName);
        if (epicPlatformOptional.isPresent()) {
            EpicPlatform epicPlatform = epicPlatformOptional.get();
            return Optional.ofNullable(epicPlatform);
        }

        return Optional.empty();
    }

    @Override
    public Optional<SteamPlatform> findSteamPlatform() throws SQLException {
        Optional<AbstractPlatform> platformOptional = findPlatformByName(EnumPlatform.STEAM.name());
        if (!platformOptional.isPresent()) {
            return Optional.empty();
        }
        AbstractPlatform platform = platformOptional.get();
        SteamPlatform steamPlatform = (SteamPlatform) Hibernate.unproxy(platform);
        return Optional.ofNullable(steamPlatform);
    }

    @Override
    public Optional<EpicPlatform> findEpicPlatform() throws SQLException {
        Optional<AbstractPlatform> platformOptional = findPlatformByName(EnumPlatform.EPIC.name());
        if (!platformOptional.isPresent()) {
            return Optional.empty();
        }
        AbstractPlatform platform = platformOptional.get();
        EpicPlatform epicPlatform = (EpicPlatform) Hibernate.unproxy(platform);
        return Optional.ofNullable(epicPlatform);
    }

    @Override
    public SteamPlatform createSteamPlatform(SteamPlatform steamPlatform) throws SQLException {
        return new SteamPlatformDao(em).insert(steamPlatform);
    }

    @Override
    public EpicPlatform createEpicPlatform(EpicPlatform epicPlatform) throws SQLException {
        return new EpicPlatformDao(em).insert(epicPlatform);
    }

    @Override
    public boolean updateSteamPlatform(SteamPlatform steamPlatform) throws Exception {
        if (new SteamPlatformDao(em).update(steamPlatform)) {
            List<PlatformProfileMap> ppmList = platformProfileMapService.listPlatformProfileMaps(steamPlatform);
            for (PlatformProfileMap ppm: ppmList) {
                ppm.setPlatform(steamPlatform);
                platformProfileMapService.updateItem(ppm);
            }
        }
        return true;
    }

    @Override
    public boolean updateEpicPlatform(EpicPlatform epicPlatform) throws Exception {
        if (new EpicPlatformDao(em).update(epicPlatform)) {
            List<PlatformProfileMap> ppmList = platformProfileMapService.listPlatformProfileMaps(epicPlatform);
            for (PlatformProfileMap ppm: ppmList) {
                ppm.setPlatform(epicPlatform);
                platformProfileMapService.updateItem(ppm);
            }
        }
        return true;
    }

    @Override
    public boolean isValidInstallationFolder(String platformName) throws Exception {
        Optional<AbstractPlatform> platformOptional = findPlatformByName(platformName);
        if (!platformOptional.isPresent()) {
            return false;
        }
        Kf2Common kf2Common = Kf2Factory.getInstance(platformOptional.get(), em);
        assert kf2Common != null;
        return kf2Common.isValidInstallationFolder();
    }

    @Override
    public List<AbstractPlatform> getPlatformListByNames(List<String> platformNameList, StringBuffer success, StringBuffer errors) {

        List<AbstractPlatform> platformList = platformNameList.stream().map(pn -> {
            String message = "Error finding the platform with name" + pn;
            try {
                Optional<AbstractPlatform> steamPlatformOptional = findPlatformByName(pn);
                if (steamPlatformOptional.isPresent()) {
                    success.append("The platform [" + pn + "] has been found!\n");
                    return steamPlatformOptional.get();
                }
                errors.append(message + "\n");
                return null;

            } catch (SQLException e) {
                errors.append(message + "\n");
                logger.error(message, e);
                throw new RuntimeException(message, e);
            }
        }).collect(Collectors.toList());
        if (platformList == null || platformList.isEmpty()) {
            String message = "No platforms were found";
            errors.append(message + "\n");
            throw new RuntimeException(message);
        }

        return platformList;
    }
}
