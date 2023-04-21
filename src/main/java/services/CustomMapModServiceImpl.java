package services;

import daos.CustomMapModDao;
import entities.*;
import jakarta.persistence.EntityManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomMapModServiceImpl extends AbstractMapService {

    private final EntityManager em;
    private final PlatformProfileMapService platformProfileMapService;

    public CustomMapModServiceImpl(EntityManager em) {
        super(em);
        this.em = em;
        this.platformProfileMapService = new PlatformProfileMapServiceImpl(em);
    }

    @Override
    public List listAll() throws SQLException {
        return new CustomMapModDao(em).listAll();
    }

    @Override
    public Optional<AbstractMap> findByCode(String mapName) throws Exception {
        Optional<CustomMapMod> customMapModOptional = new CustomMapModDao(em).findByCode(mapName);
        if (!customMapModOptional.isPresent()) {
            return Optional.empty();
        }
        return Optional.ofNullable(customMapModOptional.get());
    }

    @Override
    public AbstractMap createItem(AbstractMap map) throws Exception {
        return new CustomMapModDao(em).insert((CustomMapMod) map);
    }

    @Override
    public boolean deleteItem(AbstractMap map) throws Exception {
        return new CustomMapModDao(em).remove((CustomMapMod) map);
    }

    @Override
    public boolean updateItem(AbstractMap map) throws SQLException {
        return new CustomMapModDao(em).update((CustomMapMod) map);
    }

    @Override
    protected boolean idDownloadedMap() {
        return false;
    }

    public CustomMapMod deleteMap(AbstractPlatform platform, CustomMapMod map, Profile profile) throws Exception {
        super.deleteMap(platform, map, profile);

        List<PlatformProfileMap> ppmListForMap = platformProfileMapService.listPlatformProfileMaps(map);

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
        return new CustomMapModDao(em).findByIdWorkShop(idWorkShop);
    }
}
