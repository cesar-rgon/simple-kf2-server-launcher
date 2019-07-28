package stories.profilesedition;

import constants.Constants;
import daos.*;
import dtos.ProfileDto;
import dtos.factories.ProfileDtoFactory;
import entities.Profile;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProfilesEditionFacadeImpl implements ProfilesEditionFacade {

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
}
