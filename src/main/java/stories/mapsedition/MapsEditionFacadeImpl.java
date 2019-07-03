package stories.mapsedition;

import constants.Constants;
import daos.MapDao;
import daos.PropertyDao;
import dtos.MapDto;
import dtos.factories.MapDtoFactory;
import entities.Map;
import entities.Property;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MapsEditionFacadeImpl implements MapsEditionFacade {

    private final MapDtoFactory mapDtoFactory;

    public MapsEditionFacadeImpl() {
        super();
        this.mapDtoFactory = new MapDtoFactory();
    }

    @Override
    public List<MapDto> listAllMaps() throws SQLException {
        List<Map> maps = MapDao.getInstance().listAll();
        return mapDtoFactory.newDtos(maps);
    }

    @Override
    public String findPropertyValue(String key) throws SQLException {
        Optional<Property> propertyOpt = PropertyDao.getInstance().findByKey(key);
        if (propertyOpt.isPresent()) {
            return propertyOpt.get().getValue();
        } else {
            return "";
        }
    }

    @Override
    public MapDto createNewCustomMap(String mapName, Long idWorkShop, String urlPhoto) throws SQLException {
        if ((StringUtils.isBlank(mapName) || idWorkShop == null)) {
            return null;
        }
        String urlInfo = Constants.MAP_BASE_URL_WORKSHOP + idWorkShop;
        Map customMap = new Map(mapName, null, false, urlInfo, idWorkShop, urlPhoto);
        MapDao.getInstance().insert(customMap);
        return mapDtoFactory.newDto(customMap);
    }
}
