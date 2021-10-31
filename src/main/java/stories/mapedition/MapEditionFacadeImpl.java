package stories.mapedition;

import daos.CustomMapModDao;
import daos.OfficialMapDao;
import entities.AbstractMap;
import entities.CustomMapMod;
import entities.OfficialMap;
import services.AbstractMapService;
import services.CustomMapModServiceImpl;
import services.OfficialMapServiceImpl;
import stories.AbstractFacade;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class MapEditionFacadeImpl extends AbstractFacade implements MapEditionFacade {

    private final AbstractMapService officialMapService;
    private final AbstractMapService customMapService;

    protected MapEditionFacadeImpl() {
        super();
        this.officialMapService = new OfficialMapServiceImpl();
        this.customMapService = new CustomMapModServiceImpl();
    }

    @Override
    public boolean updateMapSetUrlPhoto(String mapName, boolean isOfficial, String mapPreviewUrl) throws SQLException {
        Optional<AbstractMap> mapOptional;
        if (isOfficial) {
            mapOptional = officialMapService.findMapByCode(mapName);
            if (!mapOptional.isPresent()) {
                return false;
            }
            mapOptional.get().setUrlPhoto(mapPreviewUrl);
            return OfficialMapDao.getInstance().update((OfficialMap) mapOptional.get());
        } else {
            mapOptional = customMapService.findMapByCode(mapName);
            if (!mapOptional.isPresent()) {
                return false;
            }
            mapOptional.get().setUrlPhoto(mapPreviewUrl);
            return CustomMapModDao.getInstance().update((CustomMapMod) mapOptional.get());
        }
    }

    @Override
    public boolean updateMapSetInfoUrl(String mapName, boolean isOfficial, String mapInfoUrl) throws SQLException {
        Optional<AbstractMap> mapOptional;
        if (isOfficial) {
            mapOptional = officialMapService.findMapByCode(mapName);
            if (!mapOptional.isPresent()) {
                return false;
            }
            mapOptional.get().setUrlInfo(mapInfoUrl);
            return OfficialMapDao.getInstance().update((OfficialMap) mapOptional.get());
        } else {
            mapOptional = customMapService.findMapByCode(mapName);
            if (!mapOptional.isPresent()) {
                return false;
            }
            mapOptional.get().setUrlInfo(mapInfoUrl);
            return CustomMapModDao.getInstance().update((CustomMapMod) mapOptional.get());
        }
    }

    @Override
    public boolean updateMapSetReleaseDate(String mapName, boolean isOfficial, LocalDate releaseDate) throws SQLException {
        Optional<AbstractMap> mapOptional;
        if (isOfficial) {
            mapOptional = officialMapService.findMapByCode(mapName);
            if (!mapOptional.isPresent()) {
                return false;
            }
            mapOptional.get().setReleaseDate(
                    Date.from(releaseDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
            );
            return OfficialMapDao.getInstance().update((OfficialMap) mapOptional.get());
        } else {
            mapOptional = customMapService.findMapByCode(mapName);
            if (!mapOptional.isPresent()) {
                return false;
            }
            mapOptional.get().setReleaseDate(
                    Date.from(releaseDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
            );
            return CustomMapModDao.getInstance().update((CustomMapMod) mapOptional.get());
        }
    }
}
