package stories.mapedition;

import daos.ProfileMapDao;
import entities.ProfileMap;
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
    public boolean updateMapSetUrlPhoto(ProfileMap profileMap, String mapPreviewUrl) throws SQLException {
        profileMap.setUrlPhoto(mapPreviewUrl);
        return ProfileMapDao.getInstance().update(profileMap);
    }

    @Override
    public boolean updateMapSetInfoUrl(ProfileMap profileMap, String mapInfoUrl) throws SQLException {
        profileMap.setUrlInfo(mapInfoUrl);
        return ProfileMapDao.getInstance().update(profileMap);
    }

    @Override
    public boolean updateMapSetReleaseDate(ProfileMap profileMap, LocalDate releaseDate) throws SQLException {
        profileMap.setReleaseDate(Date.from(releaseDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        return ProfileMapDao.getInstance().update(profileMap);
    }

    @Override
    public boolean updateMapSetAlias(ProfileMap profileMap, String alias) throws SQLException {
        profileMap.setAlias(alias);
        return ProfileMapDao.getInstance().update(profileMap);
    }
}
