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
    public List<Map> listAllMaps() throws SQLException {
        return MapDao.getInstance().listAll();
    }

    @Override
    public List<MapDto> getDtos(List<Map> mapList) {
        return mapDtoFactory.newDtos(mapList);
    }

    @Override
    public MapDto getDto(Map map) {
        return mapDtoFactory.newDto(map);
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
    public Map createNewCustomMap(String mapName, Long idWorkShop, String urlPhoto) throws SQLException {
        if ((StringUtils.isBlank(mapName) || idWorkShop == null)) {
            return null;
        }
        String urlInfo = Constants.MAP_BASE_URL_WORKSHOP + idWorkShop;
        Map customMap = new Map(mapName, null, false, urlInfo, idWorkShop, urlPhoto, false);
        return MapDao.getInstance().insert(customMap);
    }

    @Override
    public MapDto deleteSelectedMap(String mapName) throws SQLException {
        if (StringUtils.isBlank(mapName)) {
            return null;
        }
        Optional<Map> mapOpt = MapDao.getInstance().findByCode(mapName);
        if (mapOpt.isPresent() && MapDao.getInstance().remove(mapOpt.get())) {
            return mapDtoFactory.newDto(mapOpt.get());
        }
        return null;
    }
}
