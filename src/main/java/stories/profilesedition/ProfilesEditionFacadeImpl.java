package stories.profilesedition;

import daos.*;
import dtos.ProfileDto;
import dtos.factories.ProfileDtoFactory;
import entities.*;
import entities.AbstractMap;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.ProfileToDisplay;
import pojos.ProfileToDisplayFactory;
import pojos.enums.EnumPlatform;
import services.*;
import stories.AbstractFacade;
import utils.Utils;

import java.io.File;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


public class ProfilesEditionFacadeImpl extends AbstractFacade implements ProfilesEditionFacade {

    private static final Logger logger = LogManager.getLogger(ProfilesEditionFacadeImpl.class);
    private final ProfileDtoFactory profileDtoFactory;
    private final PropertyService propertyService;
    private final ProfileToDisplayFactory profileToDisplayFactory;
    private final ProfileService profileService;
    private final AbstractMapService officialMapService;
    private final ProfileMapService profileMapService;

    public ProfilesEditionFacadeImpl() {
        profileDtoFactory = new ProfileDtoFactory();
        propertyService = new PropertyServiceImpl();
        profileToDisplayFactory = new ProfileToDisplayFactory();
        this.profileService = new ProfileServiceImpl();
        this.officialMapService = new OfficialMapServiceImpl();
        this.profileMapService = new ProfileMapServiceImpl();
    }


    @Override
    public ObservableList<ProfileDto> listAllProfiles() throws SQLException {
        List<Profile> profiles = profileService.listAllProfiles();
        return profileDtoFactory.newDtos(profiles);
    }


    @Override
    public ProfileDto createNewProfile(String profileName) throws Exception {
        String defaultServername = propertyService.getPropertyValue("properties/config.properties", "prop.config.defaultServername");
        String defaultWebPort = propertyService.getPropertyValue("properties/config.properties", "prop.config.defaultWebPort");
        String defaultGamePort = propertyService.getPropertyValue("properties/config.properties", "prop.config.defaultGamePort");
        String defaultQueryPort = propertyService.getPropertyValue("properties/config.properties", "prop.config.defaultQueryPort");
        Optional<MaxPlayers> defaultMaxPlayers = MaxPlayersDao.getInstance().findByCode("6");

        List<AbstractMap> officialMaps = officialMapService.listAllMaps();
        OfficialMap firstOfficialMap = null;
        if (officialMaps != null && !officialMaps.isEmpty()) {
            firstOfficialMap = (OfficialMap) officialMaps.get(0);
        }

        Profile newProfile = new Profile(
                profileName,
                LanguageDao.getInstance().listAll().get(0),
                GameTypeDao.getInstance().listAll().get(0),
                firstOfficialMap,
                DifficultyDao.getInstance().listAll().get(0),
                LengthDao.getInstance().listAll().get(0),
                defaultMaxPlayers.isPresent()? defaultMaxPlayers.get(): null,
                StringUtils.isNotBlank(defaultServername) ? defaultServername: "Killing Floor 2 Server",
                null,
                true,
                null,
                Integer.parseInt(defaultWebPort),
                Integer.parseInt(defaultGamePort),
                Integer.parseInt(defaultQueryPort),
                null,
                null,
                null,
                null,
                null,
                new ArrayList<ProfileMap>(),
                false,
                true,
                false,
                true,
                true,
                60.0,
                true,
                0.66,
                true,
                false,
                true,
                false,
                null,
                true,
                10.0,
                0.0,
                true,
                90,
                4,
                2,
                true,
                true,
                0.0,
                EnumPlatform.STEAM
        );


        Profile savedProfile = profileService.createItem(newProfile);

        officialMaps.stream().forEach(map -> {
            try {
                profileMapService.createItem(new ProfileMap(savedProfile, map, map.getReleaseDate(), map.getUrlInfo(), map.getUrlPhoto()));
            } catch (Exception e) {
                logger.error("Error creating the relation between the profile: " + savedProfile.getName() + " and the map: " + map.getCode(), e);
            }
        });

        return profileDtoFactory.newDto(savedProfile);
    }

    @Override
    public boolean deleteSelectedProfile(String profileName, String installationFolder) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            return profileService.deleteProfile(profileOpt.get(), installationFolder);
        }
        return false;
    }


    @Override
    public ProfileDto updateChangedProfile(String oldProfileName, String newProfileName) throws SQLException {
        if (StringUtils.isBlank(newProfileName) || newProfileName.equalsIgnoreCase(oldProfileName)) {
            return null;
        }

        Optional<Profile> profileOpt = profileService.findProfileByCode(oldProfileName);
        if (profileOpt.isPresent()) {
            profileOpt.get().setCode(newProfileName);
            if (ProfileDao.getInstance().update(profileOpt.get())) {
                return profileDtoFactory.newDto(profileOpt.get());
            }
        }
        return null;
    }


    @Override
    public ProfileDto cloneSelectedProfile(String profileName, String newProfileName) throws Exception {

        Optional<Profile> profileToBeClonedOpt = profileService.findProfileByCode(profileName);
        if (profileToBeClonedOpt.isPresent()) {
            Profile newProfile = profileService.cloneProfile(profileToBeClonedOpt.get(), newProfileName);
            return profileDtoFactory.newDto(newProfile);
        } else {
            return null;
        }
    }

    @Override
    public void exportProfilesToFile(List<ProfileToDisplay> profilesToExportDto, File file) throws Exception {
        List<Profile> profilesToExport = profilesToExportDto.stream().map(dto -> {
            try {
                Optional<Profile> profileOpt = profileService.findProfileByCode(dto.getProfileName());
                if (profileOpt.isPresent()) {
                    return profileOpt.get();
                }
            } catch (SQLException e) {
                logger.error("Error in operation of export profiles to file", e);
            }
            return null;
        }).collect(Collectors.toList());
        profileService.exportProfilesToFile(profilesToExport, file);
    }

    @Override
    public Optional<ButtonType> questionToImportEntitiesFromFile() throws Exception {
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                "prop.message.proceed");
        String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                "prop.message.importItems");

        String gameTypesText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                "prop.menu.configuration.gameTypes");
        String difficultiesText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                "prop.menu.configuration.difficulties");
        String lengthText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                "prop.menu.configuration.length");
        String maxPlayersText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                "prop.menu.configuration.maxPlayers");

        return Utils.questionDialog(headerText, contentText + ":\n\n" + gameTypesText + "\n" + difficultiesText + "\n" + lengthText + "\n" + maxPlayersText);
    }

    @Override
    public Properties importEntitiesFromFile(File file) throws Exception {
        Properties properties = propertyService.loadPropertiesFromFile(file);
        List<Language> languageList = LanguageDao.getInstance().listAll();
        profileService.importGameTypesFromFile(properties, languageList);
        profileService.importDifficultiesFromFile(properties, languageList);
        profileService.importLengthsFromFile(properties, languageList);
        profileService.importMaxPlayersFromFile(properties, languageList);
        return properties;
    }

    @Override
    public List<Profile> questionToImportProfilesFromFile(Properties properties, String message) throws Exception {
        return profileService.selectProfilesToBeImported(properties, message);
    }

    @Override
    public ObservableList<ProfileDto> importProfilesFromFile(List<Profile> selectedProfileList, Properties properties, StringBuffer errorMessage) throws Exception {
        List<Profile> savedProfileList = profileService.importProfilesFromFile(selectedProfileList, properties, errorMessage);
        return profileDtoFactory.newDtos(savedProfileList);
    }

    @Override
    public List<ProfileToDisplay> selectProfilesToBeExported(String message) throws SQLException {
        List<Profile> allProfiles = profileService.listAllProfiles();
        List<ProfileToDisplay> allProfilesToDisplay = profileToDisplayFactory.newOnes(allProfiles);
        allProfilesToDisplay.stream().forEach(p -> p.setSelected(true));
        return Utils.selectProfilesDialog(message + ":", allProfilesToDisplay);
    }
}
