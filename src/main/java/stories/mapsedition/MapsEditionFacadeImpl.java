package stories.mapsedition;

import daos.MapDao;
import daos.PropertyDao;
import dtos.MapDto;
import dtos.SelectDto;
import dtos.factories.MapDtoFactory;
import entities.Map;
import entities.Property;

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
}
