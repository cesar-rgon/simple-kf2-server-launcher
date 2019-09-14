package stories.profilesedition;

import com.sun.org.apache.xpath.internal.operations.Bool;
import daos.*;
import dtos.MapDto;
import dtos.ProfileDto;
import dtos.factories.ProfileDtoFactory;
import entities.*;
import javafx.collections.ObservableList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;


public class ProfilesEditionFacadeImpl implements ProfilesEditionFacade {

    private static final Logger logger = LogManager.getLogger(ProfilesEditionFacadeImpl.class);
    private final ProfileDtoFactory profileDtoFactory;
    private final PropertyService propertyService;

    public ProfilesEditionFacadeImpl() {
        profileDtoFactory = new ProfileDtoFactory();
        propertyService = new PropertyServiceImpl();
    }

    @Override
    public ObservableList<ProfileDto> listAllProfiles() throws SQLException {
        List<Profile> profiles = ProfileDao.getInstance().listAll();
        return profileDtoFactory.newDtos(profiles);
    }

    @Override
    public ProfileDto createNewProfile(String profileName) throws Exception {
        String defaultServername = propertyService.getPropertyValue("properties/config.properties", "prop.config.defaultServername");
        String defaultWebPort = propertyService.getPropertyValue("properties/config.properties", "prop.config.defaultWebPort");
        String defaultGamePort = propertyService.getPropertyValue("properties/config.properties", "prop.config.defaultGamePort");
        String defaultQueryPort = propertyService.getPropertyValue("properties/config.properties", "prop.config.defaultQueryPort");

        List<Map> officialMaps = MapDao.getInstance().listOfficialMaps();
        Map firstOfficialMap = null;
        if (officialMaps != null && !officialMaps.isEmpty()) {
            firstOfficialMap = officialMaps.get(0);
        }

        Profile newProfile = new Profile(
                profileName,
                LanguageDao.getInstance().listAll().get(0),
                GameTypeDao.getInstance().listAll().get(0),
                firstOfficialMap,
                DifficultyDao.getInstance().listAll().get(0),
                LengthDao.getInstance().listAll().get(0),
                MaxPlayersDao.getInstance().listAll().get(0),
                StringUtils.isNotBlank(defaultServername) ? defaultServername: "Killing Floor 2 Server",
                Integer.parseInt(defaultWebPort),
                Integer.parseInt(defaultGamePort),
                Integer.parseInt(defaultQueryPort),
                officialMaps
        );
        ProfileDao.getInstance().insert(newProfile);
        return profileDtoFactory.newDto(newProfile);
    }

    @Override
    public boolean deleteSelectedProfile(String profileName, String installationFolder) throws Exception {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {

            List<Map> mapList = new ArrayList<Map>(profileOpt.get().getMapList());
            profileOpt.get().getMapList().clear();
            ProfileDao.getInstance().update(profileOpt.get());
            profileOpt = ProfileDao.getInstance().findByName(profileName);
            for (Map map: mapList) {
                map.getProfileList().remove(profileOpt.get());
                MapDao.getInstance().update(map);
                if (!map.isOfficial() && map.getProfileList().isEmpty()) {
                    if (MapDao.getInstance().remove(map)) {
                        File photo = new File(installationFolder + map.getUrlPhoto());
                        photo.delete();
                        File cacheFolder = new File(installationFolder + "/KFGame/Cache/" + map.getIdWorkShop());
                        FileUtils.deleteDirectory(cacheFolder);
                    }
                }
            }

            return ProfileDao.getInstance().remove(profileOpt.get());
        }
        return false;
    }

    @Override
    public ProfileDto updateChangedProfile(String oldProfileName, String newProfileName) throws SQLException {
        if (StringUtils.isBlank(newProfileName) || newProfileName.equalsIgnoreCase(oldProfileName)) {
            return null;
        }
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(oldProfileName);
        if (profileOpt.isPresent()) {
            profileOpt.get().setName(newProfileName);
            if (ProfileDao.getInstance().update(profileOpt.get())) {
                return profileDtoFactory.newDto(profileOpt.get());
            }
        }
        return null;
    }

    @Override
    public String findPropertyValue(String key) throws Exception {
        return propertyService.getPropertyValue("properties/config.properties", key);
    }

    @Override
    public ProfileDto cloneSelectedProfile(String profileName, String newProfileName) throws SQLException {
        Optional<Profile> profileToBeClonedOpt = ProfileDao.getInstance().findByName(profileName);

        if (profileToBeClonedOpt.isPresent()) {
            Profile newProfile = new Profile(
                    newProfileName,
                    profileToBeClonedOpt.get().getLanguage(),
                    profileToBeClonedOpt.get().getGametype(),
                    profileToBeClonedOpt.get().getMap(),
                    profileToBeClonedOpt.get().getDifficulty(),
                    profileToBeClonedOpt.get().getLength(),
                    profileToBeClonedOpt.get().getMaxPlayers(),
                    profileToBeClonedOpt.get().getServerName(),
                    profileToBeClonedOpt.get().getServerPassword(),
                    profileToBeClonedOpt.get().getWebPage(),
                    profileToBeClonedOpt.get().getWebPassword(),
                    profileToBeClonedOpt.get().getWebPort(),
                    profileToBeClonedOpt.get().getGamePort(),
                    profileToBeClonedOpt.get().getQueryPort(),
                    profileToBeClonedOpt.get().getYourClan(),
                    profileToBeClonedOpt.get().getYourWebLink(),
                    profileToBeClonedOpt.get().getUrlImageServer(),
                    profileToBeClonedOpt.get().getWelcomeMessage(),
                    profileToBeClonedOpt.get().getCustomParameters(),
                    new ArrayList<Map>(profileToBeClonedOpt.get().getMapList())
            );
            ProfileDao.getInstance().insert(newProfile);
            return profileDtoFactory.newDto(newProfile);
        } else {
            return null;
        }
    }

    @Override
    public void exportProfilesToFile(List<ProfileDto> profiles, File file) throws Exception {
        Properties properties = new Properties();
        properties.setProperty("exported.profiles.number", String.valueOf(profiles.size()));
        int profileIndex = 1;
        for (ProfileDto profile: profiles) {
            properties.setProperty("exported.profile" + profileIndex + ".name", profile.getName());
            properties.setProperty("exported.profile" + profileIndex + ".language", profile.getLanguage().getKey());
            properties.setProperty("exported.profile" + profileIndex + ".gameType", profile.getGametype().getKey());
            properties.setProperty("exported.profile" + profileIndex + ".map", profile.getMap().getKey());
            properties.setProperty("exported.profile" + profileIndex + ".difficulty", profile.getDifficulty().getKey());
            properties.setProperty("exported.profile" + profileIndex + ".length", profile.getLength().getKey());
            properties.setProperty("exported.profile" + profileIndex + ".maxPlayers", profile.getMaxPlayers().getKey());
            properties.setProperty("exported.profile" + profileIndex + ".serverName", profile.getServerName());
            properties.setProperty("exported.profile" + profileIndex + ".serverPassword", StringUtils.isNotBlank(profile.getServerPassword())? profile.getServerPassword(): "");
            properties.setProperty("exported.profile" + profileIndex + ".webPage", profile.getWebPage()!=null? String.valueOf(profile.getWebPage()): "false");
            properties.setProperty("exported.profile" + profileIndex + ".webPassword", StringUtils.isNotBlank(profile.getWebPassword())? profile.getWebPassword(): "");
            properties.setProperty("exported.profile" + profileIndex + ".webPort", profile.getWebPort()!=null? String.valueOf(profile.getWebPort()): "");
            properties.setProperty("exported.profile" + profileIndex + ".gamePort", profile.getGamePort()!=null?String.valueOf(profile.getGamePort()): "");
            properties.setProperty("exported.profile" + profileIndex + ".queryPort", profile.getQueryPort()!=null?String.valueOf(profile.getQueryPort()): "");
            properties.setProperty("exported.profile" + profileIndex + ".yourClan", StringUtils.isNotBlank(profile.getYourClan())? profile.getYourClan(): "");
            properties.setProperty("exported.profile" + profileIndex + ".yourWebLink", StringUtils.isNotBlank(profile.getYourWebLink())? profile.getYourWebLink():"");
            properties.setProperty("exported.profile" + profileIndex + ".urlImageServer", StringUtils.isNotBlank(profile.getUrlImageServer())? profile.getUrlImageServer(): "");
            properties.setProperty("exported.profile" + profileIndex + ".welcomeMessage", StringUtils.isNotBlank(profile.getWelcomeMessage())? profile.getWelcomeMessage(): "");
            properties.setProperty("exported.profile" + profileIndex + ".customParameters", StringUtils.isNotBlank(profile.getCustomParameters())? profile.getCustomParameters(): "");
            properties.setProperty("exported.profile" + profileIndex + ".mapListSize", profile.getMapList()!=null? String.valueOf(profile.getMapList().size()): "0");
            if (profile.getMapList() != null && !profile.getMapList().isEmpty()) {
                int mapIndex = 1;
                for (MapDto map: profile.getMapList()) {
                    properties.setProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".name", map.getKey());
                    properties.setProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".official", String.valueOf(map.isOfficial()));
                    properties.setProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".downloaded", String.valueOf(map.isDownloaded()));
                    properties.setProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".urlInfo", StringUtils.isNotBlank(map.getUrlInfo())? map.getUrlInfo(): "");
                    properties.setProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".idWorkShop", map.getIdWorkShop()!=null? String.valueOf(map.getIdWorkShop()): "");
                    properties.setProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".urlPhoto", StringUtils.isNotBlank(map.getUrlPhoto())? map.getUrlPhoto(): "");
                    properties.setProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".mod", map.getMod()!=null? String.valueOf(map.getMod()): "");
                    mapIndex++;
                }
            }
            profileIndex ++;
        }
        propertyService.savePropertiesToFile(properties, file);
    }

    @Override
    public ObservableList<ProfileDto> addProfilesToBeImportedFromFile(File file, String message, StringBuffer errorMessage) throws Exception {
        Properties properties = propertyService.loadPropertiesFromFile(file);
        int numberOfProfiles = Integer.parseInt(properties.getProperty("exported.profiles.number"));
        List<Profile> profileToBeImportedList = new ArrayList<Profile>();

        for (int profileIndex = 1; profileIndex <= numberOfProfiles; profileIndex++) {
            Optional<Language> languageOpt = LanguageDao.getInstance().findByCode(properties.getProperty("exported.profile" + profileIndex + ".language"));
            Optional<GameType> gameTypeOpt = GameTypeDao.getInstance().findByCode(properties.getProperty("exported.profile" + profileIndex + ".gameType"));
            Optional<Difficulty> difficultyOpt = DifficultyDao.getInstance().findByCode(properties.getProperty("exported.profile" + profileIndex + ".difficulty"));
            Optional<Length> lengthOpt = LengthDao.getInstance().findByCode(properties.getProperty("exported.profile" + profileIndex + ".length"));
            Optional<MaxPlayers> maxPlayersOpt = MaxPlayersDao.getInstance().findByCode(properties.getProperty("exported.profile" + profileIndex + ".maxPlayers"));

            Profile profileToBeImported = new Profile(
                    properties.getProperty("exported.profile" + profileIndex + ".name"),
                    languageOpt.isPresent()? languageOpt.get(): null,
                    gameTypeOpt.isPresent()? gameTypeOpt.get(): null,
                    null,
                    difficultyOpt.isPresent()? difficultyOpt.get(): null,
                    lengthOpt.isPresent()? lengthOpt.get(): null,
                    maxPlayersOpt.isPresent()? maxPlayersOpt.get(): null,
                    properties.getProperty("exported.profile" + profileIndex + ".serverName"),
                    properties.getProperty("exported.profile" + profileIndex + ".serverPassword"),
                    Boolean.parseBoolean(properties.getProperty("exported.profile" + profileIndex + ".webPage")),
                    properties.getProperty("exported.profile" + profileIndex + ".webPassword"),
                    Integer.parseInt(properties.getProperty("exported.profile" + profileIndex + ".webPort")),
                    Integer.parseInt(properties.getProperty("exported.profile" + profileIndex + ".gamePort")),
                    Integer.parseInt(properties.getProperty("exported.profile" + profileIndex + ".queryPort")),
                    properties.getProperty("exported.profile" + profileIndex + ".yourClan"),
                    properties.getProperty("exported.profile" + profileIndex + ".yourWebLink"),
                    properties.getProperty("exported.profile" + profileIndex + ".urlImageServer"),
                    properties.getProperty("exported.profile" + profileIndex + ".welcomeMessage"),
                    properties.getProperty("exported.profile" + profileIndex + ".customParameters"),
                    new ArrayList<Map>()
            );

            int numberOfMaps = Integer.parseInt(properties.getProperty("exported.profile" + profileIndex + ".mapListSize"));

            for (int mapIndex = 1; mapIndex <= numberOfMaps; mapIndex++) {
                String mapName = properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".name");
                Optional<Map> mapInDataBaseOpt = MapDao.getInstance().findByCode(mapName);
                if (mapInDataBaseOpt.isPresent()) {
                    mapInDataBaseOpt.get().getProfileList().add(profileToBeImported);
                    profileToBeImported.getMapList().add(mapInDataBaseOpt.get());
                } else {
                    boolean official = Boolean.parseBoolean(properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".official"));
                    boolean downloaded = Boolean.parseBoolean(properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".downloaded"));
                    String urlInfo = properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".urlInfo");
                    String urlPhoto = properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".urlPhoto");
                    String strIdWorkShop = properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".idWorkShop");
                    Long idWorkShop = StringUtils.isNotBlank(strIdWorkShop)? Long.parseLong(strIdWorkShop): 0;
                    String strMod = properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".mod");
                    Boolean mod = StringUtils.isNotBlank(strMod)? Boolean.parseBoolean(strMod): null;
                    List<Profile> profileList = new ArrayList<Profile>();
                    profileList.add(profileToBeImported);
                    Map mapToImport = new Map(mapName, official, urlInfo, idWorkShop, urlPhoto, downloaded, mod, profileList);
                    profileToBeImported.getMapList().add(mapToImport);
                }
            }

            String mapName = properties.getProperty("exported.profile" + profileIndex + ".map");
            Optional<Map> mapOpt = profileToBeImported.getMapList().stream()
                    .filter(m -> m.getCode().equalsIgnoreCase(mapName)).findFirst();
            profileToBeImported.setMap(mapOpt.isPresent()? mapOpt.get(): null);

            profileToBeImportedList.add(profileToBeImported);
        }

        List<Profile> selectedProfiles = Utils.selectProfilesDialog(message + ":", profileToBeImportedList, profileToBeImportedList);
        List<Profile> insertedProfiles = insertProfiles(selectedProfiles);

        if (insertedProfiles.size() < selectedProfiles.size()) {
            for (Profile selectedProfile: selectedProfiles) {
                if (!insertedProfiles.contains(selectedProfile)) {
                    errorMessage.append(selectedProfile.getName() + "\n");
                }
            }
        }

        return profileDtoFactory.newDtos(insertedProfiles);
    }


    private List<Profile> insertProfiles(List<Profile> profileList) {

        List<Profile> insertedProfileList = new ArrayList<Profile>();
        for (Profile profile: profileList) {
            try {
                Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profile.getName());
                if (profileOpt.isPresent()) {
                    logger.error("The profile " + profile.getName() + " is already in database. It could not be imported from file");
                } else {
                    for (Map map: profile.getMapList()) {
                        Optional<Map> mapInDataBase;
                        if (map.isDownloaded()) {
                            mapInDataBase = MapDao.getInstance().findByCode(map.getCode());
                        } else {
                            mapInDataBase = MapDao.getInstance().findByIdWorkShop(map.getIdWorkShop());
                        }

                        if (mapInDataBase.isPresent()) {
                            MapDao.getInstance().update(map);
                        } else {
                            MapDao.getInstance().insert(map);
                        }
                    }

                    Profile insertedProfile = ProfileDao.getInstance().insert(profile);
                    if (insertedProfile != null) {
                        insertedProfileList.add(profile);
                    } else {
                        logger.error("The profile " + profile.getName() + " could not be imported from file");
                    }
                }
            } catch (SQLException e) {
                logger.error("The profile " + profile.getName() + " could not be imported from file", e);
            }
        }
        return insertedProfileList;
    }

    @Override
    public List<ProfileDto> selectProfiles(String message) throws SQLException {
        List<Profile> allProfiles = ProfileDao.getInstance().listAll();
        List<Profile> selectedProfiles = Utils.selectProfilesDialog(message + ":", allProfiles, allProfiles);
        return profileDtoFactory.newDtos(selectedProfiles);
    }
}
