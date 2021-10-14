package services;

import daos.CustomMapModDao;
import entities.AbstractMap;
import entities.CustomMapMod;
import entities.Profile;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CustomMapModServiceImpl extends AbstractMapService {

    public CustomMapModServiceImpl() {
        super();
    }

    @Override
    public List listAll() throws SQLException {
        return CustomMapModDao.getInstance().listAll();
    }

    @Override
    public Optional findByCode(String mapName) throws SQLException {
        return CustomMapModDao.getInstance().findByCode(mapName);
    }

    @Override
    public AbstractMap createItem(AbstractMap map) throws Exception {
        return CustomMapModDao.getInstance().insert((CustomMapMod) map);
    }

    @Override
    public boolean deleteItem(AbstractMap map) throws Exception {
        return CustomMapModDao.getInstance().remove((CustomMapMod) map);
    }

    @Override
    public boolean updateItem(AbstractMap map) throws SQLException {
        return CustomMapModDao.getInstance().update((CustomMapMod) map);
    }

    public CustomMapMod deleteMap(CustomMapMod map, Profile profile, String installationFolder) throws Exception {
        super.deleteMap(map, profile);

        if (map.getProfileList().isEmpty()) {
            deleteItem(map);
            File photo = new File(installationFolder + map.getUrlPhoto());
            photo.delete();
            File cacheFolder = new File(installationFolder + "/KFGame/Cache/" + map.getIdWorkShop());
            FileUtils.deleteDirectory(cacheFolder);
        }

        return map;
    }
}
