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
    public List<Map> listDownloadedMaps() throws SQLException {
        return MapDao.getInstance().listDownloadedMaps();
    }

    @Override
    public List<Map> listNotDownloadedMapsAndMods() throws SQLException {
        return MapDao.getInstance().listNotDownloadedMapsAndMods();
    }

    @Override
    public boolean updateMap(Map map) throws SQLException {
        return MapDao.getInstance().update(map);
    }

    @Override
    public List<Map> listCustomMapsAndMods() throws SQLException {
        return MapDao.getInstance().listCustomMapsAndMods();
    }

    @Override
    public List<Map> listCustomMaps() throws SQLException {
        return MapDao.getInstance().listCustomMaps();
    }

    @Override
    public Map findMapByName(String mapName) throws SQLException {
        Optional<Map> mapOpt = MapDao.getInstance().findByCode(mapName);
        if (mapOpt.isPresent()) {
            return mapOpt.get();
        }
        return null;
    }
}
