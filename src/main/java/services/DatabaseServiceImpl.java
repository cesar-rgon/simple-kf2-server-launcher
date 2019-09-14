package services;

import daos.MapDao;
import daos.ProfileDao;
import entities.Map;
import entities.Profile;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DatabaseServiceImpl implements DatabaseService {

    public DatabaseServiceImpl() {
        super();
    }

    @Override
    public List<Map> listNotDownloadedMapsAndMods() throws SQLException {
        return MapDao.getInstance().listNotDownloadedMapsAndMods();
    }

    @Override
    public boolean updateMap(Map map) throws SQLException {
        return MapDao.getInstance().update(map);
    }

}
