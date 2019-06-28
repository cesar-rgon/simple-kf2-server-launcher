package stories.profilesedition;

import constants.Constants;
import daos.*;
import dtos.ProfileDto;
import dtos.factories.ProfileDtoFactory;
import entities.Profile;
import entities.Property;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProfilesEditionFacadeImpl implements ProfilesEditionFacade {

    private final Integer FIRST_ELEMENT_ID = 1;
    private final ProfileDtoFactory profileDtoFactory;

    public ProfilesEditionFacadeImpl() {
        profileDtoFactory = new ProfileDtoFactory();
    }

    @Override
    public ObservableList<ProfileDto> listAllProfiles() throws SQLException {
        List<Profile> profiles = ProfileDao.getInstance().listAll();
        return profileDtoFactory.newDtos(profiles);
    }

    @Override
    public ProfileDto createNewProfile(String profileName) throws Exception {
        Optional<Property> defaultServernameOpt = PropertyDao.getInstance().findByKey(Constants.KEY_DEFAULT_SERVERNAME);
        Optional<Property> defaultWebPort = PropertyDao.getInstance().findByKey(Constants.KEY_DEFAULT_WEB_PORT);
        Optional<Property> defaultGamePort = PropertyDao.getInstance().findByKey(Constants.KEY_DEFAULT_GAME_PORT);
        Optional<Property> defaultQueryPort = PropertyDao.getInstance().findByKey(Constants.KEY_DEFAULT_QUERY_PORT);
        Profile newProfile = new Profile(
                profileName,
                LanguageDao.getInstance().get(FIRST_ELEMENT_ID),
                GameTypeDao.getInstance().get(FIRST_ELEMENT_ID),
                MapDao.getInstance().get(FIRST_ELEMENT_ID),
                DifficultyDao.getInstance().get(FIRST_ELEMENT_ID),
                LengthDao.getInstance().get(FIRST_ELEMENT_ID),
                MaxPlayersDao.getInstance().get(FIRST_ELEMENT_ID),
                defaultServernameOpt.isPresent() ? defaultServernameOpt.get().getValue(): Constants.VALUE_DEFAULT_SERVERNAME,
                defaultWebPort.isPresent() ? Integer.parseInt(defaultWebPort.get().getValue()): Constants.VALUE_DEFAULT_WEB_PORT,
                defaultGamePort.isPresent() ? Integer.parseInt(defaultGamePort.get().getValue()): Constants.VALUE_DEFAULT_GAME_PORT,
                defaultQueryPort.isPresent() ? Integer.parseInt(defaultQueryPort.get().getValue()): Constants.VALUE_DEFAULT_QUERY_PORT
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
    public String findPropertyValue(String key) throws SQLException {
        Optional<Property> propertyOpt = PropertyDao.getInstance().findByKey(key);
        if (propertyOpt.isPresent()) {
            return propertyOpt.get().getValue();
        } else {
            return "";
        }
    }
}
