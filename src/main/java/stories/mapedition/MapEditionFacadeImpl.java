package stories.mapedition;

import daos.PlatformProfileMapDao;
import entities.PlatformProfileMap;
import services.*;
import stories.AbstractFacade;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class MapEditionFacadeImpl extends AbstractFacade implements MapEditionFacade {

    private final PlatformProfileMapService platformProfileMapService;

    protected MapEditionFacadeImpl() {
        super();
        this.platformProfileMapService = new PlatformProfileMapServiceImpl();
    }

    @Override
    public boolean updateMapSetUrlPhoto(PlatformProfileMap platformProfileMap, String mapPreviewUrl) throws Exception {
        platformProfileMap.setUrlPhoto(mapPreviewUrl);
        return platformProfileMapService.updateItem(platformProfileMap);
    }

    @Override
    public boolean updateMapSetInfoUrl(PlatformProfileMap platformProfileMap, String mapInfoUrl) throws Exception {
        platformProfileMap.setUrlInfo(mapInfoUrl);
        return platformProfileMapService.updateItem(platformProfileMap);
    }

    @Override
    public boolean updateMapSetReleaseDate(PlatformProfileMap platformProfileMap, LocalDate releaseDate) throws Exception {
        platformProfileMap.setReleaseDate(Date.from(releaseDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        return platformProfileMapService.updateItem(platformProfileMap);
    }

    @Override
    public boolean updateMapSetAlias(PlatformProfileMap platformProfileMap, String alias) throws Exception {
        platformProfileMap.setAlias(alias);
        return platformProfileMapService.updateItem(platformProfileMap);
    }
}
