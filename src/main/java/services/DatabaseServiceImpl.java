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
    public List<Map> listNotDownloadedMaps() throws SQLException {
        return MapDao.getInstance().listNotDownloadedMaps();
    }

    @Override
    public boolean updateMap(Map map) throws SQLException {
        return MapDao.getInstance().update(map);
    }

    @Override
    public List<Profile> listAllProfiles() throws SQLException {
        return ProfileDao.getInstance().listAll();
    }

    @Override
    public List<Map> listOfficialMaps() throws SQLException {
        return MapDao.getInstance().listOfficialMaps();
    }

    @Override
    public Map insertMap(Map map) throws SQLException {
        return MapDao.getInstance().insert(map);
    }
}
