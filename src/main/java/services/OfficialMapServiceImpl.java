package services;

import daos.OfficialMapDao;
import entities.AbstractMap;
import entities.OfficialMap;
import entities.AbstractPlatform;
import entities.Profile;
import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class OfficialMapServiceImpl extends AbstractMapService {

    public OfficialMapServiceImpl(EntityManager em) {
        super(em);
    }

    @Override
    public List listAll() throws SQLException {
        return new OfficialMapDao(em).listAll();
    }

    @Override
    public Optional findByCode(String mapName) throws SQLException {
        return new OfficialMapDao(em).findByCode(mapName);
    }

    @Override
    public AbstractMap createItem(AbstractMap map) throws Exception {
        return new OfficialMapDao(em).insert((OfficialMap) map);
    }

    @Override
    public boolean deleteItem(AbstractMap map) throws Exception {
        return new OfficialMapDao(em).remove((OfficialMap) map);
    }

    @Override
    public boolean updateItem(AbstractMap map) throws SQLException {
        return new OfficialMapDao(em).update((OfficialMap) map);
    }

    @Override
    protected boolean idDownloadedMap() {
        return true;
    }

    public AbstractMap deleteMap(AbstractPlatform platform, OfficialMap map, Profile profile) throws Exception {
        return super.deleteMap(platform, map, profile);
    }
}
