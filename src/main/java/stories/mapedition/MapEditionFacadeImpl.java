package stories.mapedition;

import daos.PlatformProfileMapDao;
import entities.PlatformProfileMap;
import services.AbstractMapService;
import services.CustomMapModServiceImpl;
import services.OfficialMapServiceImpl;
import stories.AbstractFacade;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class MapEditionFacadeImpl extends AbstractFacade implements MapEditionFacade {

    private final AbstractMapService officialMapService;
    private final AbstractMapService customMapService;

    protected MapEditionFacadeImpl() {
        super();
        this.officialMapService = new OfficialMapServiceImpl();
        this.customMapService = new CustomMapModServiceImpl();
    }

    @Override
    public boolean updateMapSetUrlPhoto(PlatformProfileMap platformProfileMap, String mapPreviewUrl) throws SQLException {
        platformProfileMap.setUrlPhoto(mapPreviewUrl);
        return PlatformProfileMapDao.getInstance().update(platformProfileMap);
    }

    @Override
    public boolean updateMapSetInfoUrl(PlatformProfileMap platformProfileMap, String mapInfoUrl) throws SQLException {
        platformProfileMap.setUrlInfo(mapInfoUrl);
        return PlatformProfileMapDao.getInstance().update(platformProfileMap);
    }

    @Override
    public boolean updateMapSetReleaseDate(PlatformProfileMap platformProfileMap, LocalDate releaseDate) throws SQLException {
        platformProfileMap.setReleaseDate(Date.from(releaseDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        return PlatformProfileMapDao.getInstance().update(platformProfileMap);
    }

    @Override
    public boolean updateMapSetAlias(PlatformProfileMap platformProfileMap, String alias) throws SQLException {
        platformProfileMap.setAlias(alias);
        return PlatformProfileMapDao.getInstance().update(platformProfileMap);
    }
}
