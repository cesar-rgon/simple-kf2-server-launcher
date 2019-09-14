package stories.mapsedition;

import daos.MapDao;
import daos.ProfileDao;
import dtos.MapDto;
import dtos.ProfileDto;
import dtos.factories.MapDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.Map;
import entities.Profile;
import javafx.collections.ObservableList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MapsEditionFacadeImpl implements MapsEditionFacade {

    private final MapDtoFactory mapDtoFactory;
    private final PropertyService propertyService;
    private final ProfileDtoFactory profileDtoFactory;

    public MapsEditionFacadeImpl() {
        super();
        this.mapDtoFactory = new MapDtoFactory();
        this.propertyService = new PropertyServiceImpl();
        this.profileDtoFactory = new ProfileDtoFactory();
    }

    @Override
    public String findConfigPropertyValue(String key) throws Exception {
        return propertyService.getPropertyValue("properties/config.properties", key);
    }

    @Override
    public void setConfigPropertyValue(String key, String value) throws Exception {
        propertyService.setProperty("properties/config.properties", key, value);
    }

    private Map createNewCustomMap(String mapName, Long idWorkShop, String urlPhoto, boolean downloaded, Boolean isMod, Profile profile) throws Exception {
        if ((StringUtils.isBlank(mapName) || idWorkShop == null)) {
            return null;
        }
        String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapBaseUrlWorkshop");
        String urlInfo = baseUrlWorkshop + idWorkShop;

        List<Profile> profileList = new ArrayList<Profile>();
        profileList.add(profile);
        Map customMap = new Map(mapName, false, urlInfo, idWorkShop, urlPhoto, downloaded, isMod, profileList);
        Map insertedMap = MapDao.getInstance().insert(customMap);
        profile.getMapList().add(insertedMap);
        ProfileDao.getInstance().update(profile);
        return insertedMap;
    }

    @Override
    public MapDto createNewCustomMapFromWorkshop(Long idWorkShop, String installationFolder, boolean downloaded, Boolean isMod, String profileName) throws Exception {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (!profileOpt.isPresent()) {
            return null;
        }

        URL urlWorkShop = null;
        String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapBaseUrlWorkshop");
        urlWorkShop = new URL(baseUrlWorkshop + idWorkShop);

        BufferedReader reader = new BufferedReader(new InputStreamReader(urlWorkShop.openStream()));
        String strUrlMapImage = null;
        String mapName = null;
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("image_src")) {
                String[] array = line.split("\"");
                strUrlMapImage = array[3];
            }
            if (line.contains("workshopItemTitle")) {
                String[] array = line.split(">");
                String[] array2 = array[1].split("<");
                mapName = array2[0];
            }
            if (StringUtils.isNotEmpty(strUrlMapImage) && StringUtils.isNotEmpty(mapName)) {
                break;
            }
        }
        reader.close();
        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapCustomLocalFolder");
        String absoluteTargetFolder = installationFolder + customMapLocalFolder;
        File localfile = Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, mapName);
        String relativeTargetFolder = customMapLocalFolder + "/" + localfile.getName();

        Map newMap = createNewCustomMap(mapName, idWorkShop, relativeTargetFolder, downloaded, isMod, profileOpt.get());
        return mapDtoFactory.newDto(newMap);
    }

    @Override
    public MapDto createNewCustomMapFromWorkshop(Long idWorkShop, String mapName, String installationFolder, boolean downloaded, Boolean isMod, String profileName) throws Exception {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (!profileOpt.isPresent()) {
            return null;
        }

        String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapBaseUrlWorkshop");
        URL urlWorkShop = new URL(baseUrlWorkshop + idWorkShop);
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlWorkShop.openStream()));
        String strUrlMapImage = null;
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("image_src")) {
                String[] array = line.split("\"");
                strUrlMapImage = array[3];
            }
            if (StringUtils.isNotEmpty(strUrlMapImage)) {
                break;
            }
        }
        reader.close();
        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapCustomLocalFolder");
        String absoluteTargetFolder = installationFolder + customMapLocalFolder;
        File localfile = Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, mapName);
        String relativeTargetFolder = customMapLocalFolder + "/" + localfile.getName();

        Map newMap = createNewCustomMap(mapName, idWorkShop, relativeTargetFolder, downloaded, isMod, profileOpt.get());
        return mapDtoFactory.newDto(newMap);
    }

    @Override
    public boolean isCorrectInstallationFolder(String installationFolder) {
        if (StringUtils.isNotBlank(installationFolder)) {
            File windowsExecutable = new File(installationFolder + "/Binaries/Win64/KFServer.exe");
            File linuxExecutable = new File(installationFolder + "/Binaries/Win64/KFGameSteamServer.bin.x86_64");
            if (windowsExecutable.exists() && linuxExecutable.exists()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public MapDto insertOfficialMap(String mapName, String profileName) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            List<Profile> profileList = new ArrayList<Profile>();
            profileList.add(profileOpt.get());
            Map newOfficialMap = new Map(mapName, true, null, null, "/KFGame/Web/images/maps/" + mapName + ".jpg", true, false, profileList);
            Map insertedMap = MapDao.getInstance().insert(newOfficialMap);
            if (insertedMap != null) {
                return mapDtoFactory.newDto(insertedMap);
            }
        }
        return null;
    }

    @Override
    public MapDto findMapOrModByIdWorkShop(Long idWorkShop) throws SQLException {
        Optional<Map> mapOpt = MapDao.getInstance().findByIdWorkShop(idWorkShop);
        if (mapOpt.isPresent()) {
            return mapDtoFactory.newDto(mapOpt.get());
        }
        return null;
    }

    @Override
    public ObservableList<ProfileDto> listAllProfiles() throws SQLException {
        List<Profile> profiles = ProfileDao.getInstance().listAll();
        return profileDtoFactory.newDtos(profiles);
    }

    @Override
    public List<MapDto> getMapsFromProfile(String profileName) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            return mapDtoFactory.newDtos(profileOpt.get().getMapList());
        }
        return new ArrayList<MapDto>();
    }

    @Override
    public Boolean addProfileToMap(String mapName, String profileName) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Optional<Map> mapOpt = profileOpt.get().getMapList().stream().filter(m -> m.getCode().equalsIgnoreCase(mapName)).findFirst();
            if (mapOpt.isPresent()) {
                return null;
            }
            Optional<Map> mapOptional = MapDao.getInstance().findByCode(mapName);
            if (mapOptional.isPresent()) {
                profileOpt.get().getMapList().add(mapOptional.get());
                ProfileDao.getInstance().update(profileOpt.get());
                mapOptional.get().getProfileList().add(profileOpt.get());
                return MapDao.getInstance().update(mapOptional.get());
            }
        }
        return false;
    }

    @Override
    public MapDto deleteMapFromProfile(String mapName, String profileName, String installationFolder) throws Exception {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Optional<Map> mapOpt = profileOpt.get().getMapList().stream().filter(m -> m.getCode().equalsIgnoreCase(mapName)).findFirst();
            if (mapOpt.isPresent()) {
                profileOpt.get().getMapList().remove(mapOpt.get());
                ProfileDao.getInstance().update(profileOpt.get());
                mapOpt.get().getProfileList().remove(profileOpt.get());
                MapDao.getInstance().update(mapOpt.get());

                if (!mapOpt.get().isOfficial() && mapOpt.get().getProfileList().isEmpty()) {
                    MapDao.getInstance().remove(mapOpt.get());
                    File photo = new File(installationFolder + mapOpt.get().getUrlPhoto());
                    photo.delete();
                    File cacheFolder = new File(installationFolder + "/KFGame/Cache/" + mapOpt.get().getIdWorkShop());
                    FileUtils.deleteDirectory(cacheFolder);
                }

                return mapDtoFactory.newDto(mapOpt.get());
            }
        }
        return null;
    }

    @Override
    public MapDto findMapByName(String mapName) throws SQLException {
        Optional<Map> mapOpt = MapDao.getInstance().findByCode(mapName);
        if (mapOpt.isPresent()) {
            return mapDtoFactory.newDto(mapOpt.get());
        }
        return null;
    }

    @Override
    public void unselectProfileMap(String profileName) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            profileOpt.get().setMap(null);
            ProfileDao.getInstance().update(profileOpt.get());
        }
    }
}
