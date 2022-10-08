package services;

import daos.EpicPlatformDao;
import daos.SteamPlatformDao;
import entities.AbstractPlatform;
import entities.EpicPlatform;
import entities.PlatformProfileMap;
import entities.SteamPlatform;
import org.hibernate.Hibernate;
import pojos.enums.EnumPlatform;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlatformServiceImpl implements PlatformService {

    private final PlatformProfileMapService platformProfileMapService;

    public PlatformServiceImpl() {
        super();
        this.platformProfileMapService = new PlatformProfileMapServiceImpl();
    }

    @Override
    public List<AbstractPlatform> listAllPlatforms() throws SQLException  {
        Optional<SteamPlatform> steamPlatformOptional = SteamPlatformDao.getInstance().findByCode(EnumPlatform.STEAM.name());
        Optional<EpicPlatform> epicPlatformOptional = EpicPlatformDao.getInstance().findByCode(EnumPlatform.EPIC.name());

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
        Optional<SteamPlatform> steamPlatformOptional = SteamPlatformDao.getInstance().findByCode(platformName);
        if (steamPlatformOptional.isPresent()) {
            SteamPlatform steamPlatform = steamPlatformOptional.get();
            return Optional.ofNullable(steamPlatform);
        }

        Optional<EpicPlatform> epicPlatformOptional = EpicPlatformDao.getInstance().findByCode(platformName);
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
        return SteamPlatformDao.getInstance().insert(steamPlatform);
    }

    @Override
    public EpicPlatform createEpicPlatform(EpicPlatform epicPlatform) throws SQLException {
        return EpicPlatformDao.getInstance().insert(epicPlatform);
    }

    @Override
    public boolean updateSteamPlatform(SteamPlatform steamPlatform) throws Exception {
        if (SteamPlatformDao.getInstance().update(steamPlatform)) {
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
        if (EpicPlatformDao.getInstance().update(epicPlatform)) {
            List<PlatformProfileMap> ppmList = platformProfileMapService.listPlatformProfileMaps(epicPlatform);
            for (PlatformProfileMap ppm: ppmList) {
                ppm.setPlatform(epicPlatform);
                platformProfileMapService.updateItem(ppm);
            }
        }
        return true;
    }
}
