package stories.profilesedition;

import constants.Constants;
import daos.*;
import dtos.ProfileDto;
import dtos.factories.ProfileDtoFactory;
import entities.*;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.listener.TimeListener;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;


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
        String defaultServername = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_DEFAULT_SERVERNAME);
        String defaultWebPort = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_DEFAULT_WEB_PORT);
        String defaultGamePort = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_DEFAULT_GAME_PORT);
        String defaultQueryPort = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_DEFAULT_QUERY_PORT);

        Profile newProfile = new Profile(
                profileName,
                LanguageDao.getInstance().listAll().get(0),
                GameTypeDao.getInstance().listAll().get(0),
                MapDao.getInstance().listDownloadedMaps().get(0),
                DifficultyDao.getInstance().listAll().get(0),
                LengthDao.getInstance().listAll().get(0),
                MaxPlayersDao.getInstance().listAll().get(0),
                StringUtils.isNotBlank(defaultServername) ? defaultServername: "Killing Floor 2 Server",
                Integer.parseInt(defaultWebPort),
                Integer.parseInt(defaultGamePort),
                Integer.parseInt(defaultQueryPort)
        );
        ProfileDao.getInstance().insert(newProfile);
        return profileDtoFactory.newDto(newProfile);
    }

    @Override
    public boolean deleteSelectedProfile(String profileName) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
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
                    profileToBeClonedOpt.get().getCustomParameters()
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
        int index = 1;
        for (ProfileDto profile: profiles) {
            properties.setProperty("exported.profile" + index + ".name", profile.getName());
            properties.setProperty("exported.profile" + index + ".language", profile.getLanguage().getKey());
            properties.setProperty("exported.profile" + index + ".gameType", profile.getGametype().getKey());
            properties.setProperty("exported.profile" + index + ".map", profile.getMap().getKey());
            properties.setProperty("exported.profile" + index + ".difficulty", profile.getDifficulty().getKey());
            properties.setProperty("exported.profile" + index + ".length", profile.getLength().getKey());
            properties.setProperty("exported.profile" + index + ".maxPlayers", profile.getMaxPlayers().getKey());
            properties.setProperty("exported.profile" + index + ".serverName", profile.getServerName());
            properties.setProperty("exported.profile" + index + ".serverPassword", StringUtils.isNotBlank(profile.getServerPassword())? profile.getServerPassword(): "");
            properties.setProperty("exported.profile" + index + ".webPage", profile.getWebPage()!=null? String.valueOf(profile.getWebPage()): "false");
            properties.setProperty("exported.profile" + index + ".webPassword", StringUtils.isNotBlank(profile.getWebPassword())? profile.getWebPassword(): "");
            properties.setProperty("exported.profile" + index + ".webPort", profile.getWebPort()!=null? String.valueOf(profile.getWebPort()): "");
            properties.setProperty("exported.profile" + index + ".gamePort", profile.getGamePort()!=null?String.valueOf(profile.getGamePort()): "");
            properties.setProperty("exported.profile" + index + ".queryPort", profile.getQueryPort()!=null?String.valueOf(profile.getQueryPort()): "");
            properties.setProperty("exported.profile" + index + ".yourClan", StringUtils.isNotBlank(profile.getYourClan())? profile.getYourClan(): "");
            properties.setProperty("exported.profile" + index + ".yourWebLink", StringUtils.isNotBlank(profile.getYourWebLink())? profile.getYourWebLink():"");
            properties.setProperty("exported.profile" + index + ".urlImageServer", StringUtils.isNotBlank(profile.getUrlImageServer())? profile.getUrlImageServer(): "");
            properties.setProperty("exported.profile" + index + ".welcomeMessage", StringUtils.isNotBlank(profile.getWelcomeMessage())? profile.getWelcomeMessage(): "");
            properties.setProperty("exported.profile" + index + ".customParameters", StringUtils.isNotBlank(profile.getCustomParameters())? profile.getCustomParameters(): "");
            index ++;
        }
        propertyService.savePropertiesToFile(properties, file);
    }

    @Override
    public ObservableList<ProfileDto> getProfilesToBeImportedFromFile(File file) throws Exception {
        Properties properties = propertyService.loadPropertiesFromFile(file);
        int numberOfProfiles = Integer.parseInt(properties.getProperty("exported.profiles.number"));
        List<Profile> profileToBeImportedList = new ArrayList<Profile>();

        for (int index = 1; index <= numberOfProfiles; index++) {
            Optional<Language> languageOpt = LanguageDao.getInstance().findByCode(properties.getProperty("exported.profile" + index + ".language"));
            Optional<GameType> gameTypeOpt = GameTypeDao.getInstance().findByCode(properties.getProperty("exported.profile" + index + ".gameType"));
            Optional<Map> mapOpt = MapDao.getInstance().findByCode(properties.getProperty("exported.profile" + index + ".map"));
            Optional<Difficulty> difficultyOpt = DifficultyDao.getInstance().findByCode(properties.getProperty("exported.profile" + index + ".difficulty"));
            Optional<Length> lengthOpt = LengthDao.getInstance().findByCode(properties.getProperty("exported.profile" + index + ".length"));
            Optional<MaxPlayers> maxPlayersOpt = MaxPlayersDao.getInstance().findByCode(properties.getProperty("exported.profile" + index + ".maxPlayers"));

            Profile profileToBeImported = new Profile(
                    properties.getProperty("exported.profile" + index + ".name"),
                    languageOpt.isPresent()? languageOpt.get(): null,
                    gameTypeOpt.isPresent()? gameTypeOpt.get(): null,
                    mapOpt.isPresent()? mapOpt.get(): null,
                    difficultyOpt.isPresent()? difficultyOpt.get(): null,
                    lengthOpt.isPresent()? lengthOpt.get(): null,
                    maxPlayersOpt.isPresent()? maxPlayersOpt.get(): null,
                    properties.getProperty("exported.profile" + index + ".serverName"),
                    properties.getProperty("exported.profile" + index + ".serverPassword"),
                    Boolean.parseBoolean(properties.getProperty("exported.profile" + index + ".webPage")),
                    properties.getProperty("exported.profile" + index + ".webPassword"),
                    Integer.parseInt(properties.getProperty("exported.profile" + index + ".webPort")),
                    Integer.parseInt(properties.getProperty("exported.profile" + index + ".gamePort")),
                    Integer.parseInt(properties.getProperty("exported.profile" + index + ".queryPort")),
                    properties.getProperty("exported.profile" + index + ".yourClan"),
                    properties.getProperty("exported.profile" + index + ".yourWebLink"),
                    properties.getProperty("exported.profile" + index + ".urlImageServer"),
                    properties.getProperty("exported.profile" + index + ".welcomeMessage"),
                    properties.getProperty("exported.profile" + index + ".customParameters")
            );
            profileToBeImportedList.add(profileToBeImported);
        }
        return profileDtoFactory.newDtos(profileToBeImportedList);
    }

    @Override
    public List<ProfileDto> insertProfiles(List<ProfileDto> profileDtoList) {

        List<ProfileDto> insertedProfileList = new ArrayList<ProfileDto>();
        for (ProfileDto profileDto: profileDtoList) {
            try {
                Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileDto.getName());
                if (profileOpt.isPresent()) {
                    logger.error("The profile " + profileDto.getName() + " is already in database. It could not be imported from file");
                } else {
                    Optional<Language> languageOpt = LanguageDao.getInstance().findByCode(profileDto.getLanguage().getKey());
                    Optional<GameType> gameTypeOpt = GameTypeDao.getInstance().findByCode(profileDto.getGametype().getKey());
                    Optional<Map> mapOpt = MapDao.getInstance().findByCode(profileDto.getMap().getKey());
                    Optional<Difficulty> difficultyOpt = DifficultyDao.getInstance().findByCode(profileDto.getDifficulty().getKey());
                    Optional<Length> lengthOpt = LengthDao.getInstance().findByCode(profileDto.getLength().getKey());
                    Optional<MaxPlayers> maxPlayersOpt = MaxPlayersDao.getInstance().findByCode(profileDto.getMaxPlayers().getKey());

                    Profile profileToImport = new Profile(
                            profileDto.getName(),
                            languageOpt.isPresent()? languageOpt.get(): null,
                            gameTypeOpt.isPresent()? gameTypeOpt.get(): null,
                            mapOpt.isPresent()? mapOpt.get(): null,
                            difficultyOpt.isPresent()? difficultyOpt.get(): null,
                            lengthOpt.isPresent()? lengthOpt.get(): null,
                            maxPlayersOpt.isPresent()? maxPlayersOpt.get(): null,
                            profileDto.getServerName(),
                            profileDto.getServerPassword(),
                            profileDto.getWebPage(),
                            profileDto.getWebPassword(),
                            profileDto.getWebPort(),
                            profileDto.getGamePort(),
                            profileDto.getQueryPort(),
                            profileDto.getYourClan(),
                            profileDto.getYourWebLink(),
                            profileDto.getUrlImageServer(),
                            profileDto.getWelcomeMessage(),
                            profileDto.getCustomParameters()
                    );

                    Profile insertedProfile = ProfileDao.getInstance().insert(profileToImport);
                    if (insertedProfile != null) {
                        insertedProfileList.add(profileDto);
                    } else {
                        logger.error("The profile " + profileDto.getName() + " could not be imported from file");
                    }
                }
            } catch (SQLException e) {
                logger.error("The profile " + profileDto.getName() + " could not be imported from file", e);
            }
        }
        return insertedProfileList;
    }
}
