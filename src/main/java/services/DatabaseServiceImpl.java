package services;

import daos.MapDao;
import entities.Map;

import java.sql.SQLException;
import java.util.List;

public class DatabaseServiceImpl implements DatabaseService {

    public DatabaseServiceImpl() {
        super();
    }

    @Override
    public List<Map> listAllMaps() throws SQLException {
        return MapDao.getInstance().listAllMaps();
    }

    @Override
    public boolean updateMap(Map map) throws SQLException {
        return MapDao.getInstance().update(map);
    }

}
