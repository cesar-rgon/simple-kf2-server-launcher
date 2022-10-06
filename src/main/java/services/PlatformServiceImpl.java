package services;

import daos.EpicPlatformDao;
import daos.SteamPlatformDao;
import entities.AbstractPlatform;
import entities.EpicPlatform;
import entities.SteamPlatform;
import org.hibernate.Hibernate;
import pojos.enums.EnumPlatform;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlatformServiceImpl implements PlatformService {

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
    public boolean updateSteamPlatform(SteamPlatform steamPlatform) throws SQLException {
        return SteamPlatformDao.getInstance().update(steamPlatform);
    }

    @Override
    public boolean updateEpicPlatform(EpicPlatform epicPlatform) throws SQLException {
        return EpicPlatformDao.getInstance().update(epicPlatform);
    }
}
