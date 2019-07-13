package services;

import daos.MapDao;
import daos.PropertyDao;
import entities.Map;
import entities.Property;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DatabaseServiceImpl implements DatabaseService {

    public DatabaseServiceImpl() {
        super();
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
    public List<Map> listNotDownloadedMaps() throws SQLException {
        return MapDao.getInstance().listNotDownloadedMaps();
    }

    @Override
    public boolean updateMap(Map map) throws SQLException {
        return MapDao.getInstance().update(map);
    }
}
