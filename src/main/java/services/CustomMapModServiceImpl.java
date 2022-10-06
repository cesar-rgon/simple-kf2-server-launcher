package services;

import daos.CustomMapModDao;
import daos.PlatformProfileMapDao;
import entities.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomMapModServiceImpl extends AbstractMapService {

    public CustomMapModServiceImpl() {
        super();
    }

    @Override
    public List listAll() throws SQLException {
        return CustomMapModDao.getInstance().listAll();
    }

    @Override
    public Optional<AbstractMap> findByCode(String mapName) throws Exception {
        Optional<CustomMapMod> customMapModOptional = CustomMapModDao.getInstance().findByCode(mapName);
        if (!customMapModOptional.isPresent()) {
            return Optional.empty();
        }
        return Optional.ofNullable(customMapModOptional.get());
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

    public CustomMapMod deleteMap(AbstractPlatform platform, CustomMapMod map, Profile profile) throws Exception {
        super.deleteMap(platform, map, profile);

        List<PlatformProfileMap> ppmListForMap = PlatformProfileMapDao.getInstance().listPlatformProfileMaps(map);

        List<PlatformProfileMap> ppmListForPlatformMap = ppmListForMap.stream().filter(ppm -> ppm.getPlatform().equals(platform)).collect(Collectors.toList());
        if (ppmListForPlatformMap.isEmpty()) {
            File photo = new File(platform.getInstallationFolder() + map.getUrlPhoto());
            photo.delete();
            File cacheFolder = new File(platform.getInstallationFolder() + "/KFGame/Cache/" + map.getIdWorkShop());
            FileUtils.deleteDirectory(cacheFolder);
        }

        if (ppmListForMap.isEmpty()) {
            deleteItem(map);
        }
        return map;
    }

    public Optional findByIdWorkShop(Long idWorkShop) throws SQLException {
        return CustomMapModDao.getInstance().findByIdWorkShop(idWorkShop);
    }
}
