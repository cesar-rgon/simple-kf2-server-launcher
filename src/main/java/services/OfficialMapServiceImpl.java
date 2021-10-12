package services;

import daos.OfficialMapDao;
import entities.AbstractMap;
import entities.OfficialMap;
import entities.Profile;

import java.sql.SQLException;

public class OfficialMapServiceImpl extends AbstractMapService {

    public OfficialMapServiceImpl() {
        super();
    }

    @Override
    public AbstractMap createItem(AbstractMap map) throws Exception {
        return OfficialMapDao.getInstance().insert((OfficialMap) map);
    }

    @Override
    public boolean deleteItem(AbstractMap map) throws Exception {
        return OfficialMapDao.getInstance().remove((OfficialMap) map);
    }

    @Override
    public boolean updateItem(AbstractMap map) throws SQLException {
        return OfficialMapDao.getInstance().update((OfficialMap) map);
    }

    public AbstractMap deleteMap(OfficialMap map, Profile profile) throws Exception {
        return super.deleteMap(map, profile);
    }
}
