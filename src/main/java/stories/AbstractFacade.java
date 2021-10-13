package stories;

import daos.ProfileDao;
import dtos.ProfileDto;
import dtos.factories.ProfileDtoFactory;
import entities.Profile;
import services.ProfileService;
import services.ProfileServiceImpl;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.sql.SQLException;
import java.util.Optional;

public abstract class AbstractFacade {

    private final ProfileService profileService;

    protected AbstractFacade() {
        super();
        this.profileService = new ProfileServiceImpl();
    }

    public Profile findProfileByCode(String profileName) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            return profileOpt.get();
        }
        return null;
    }

    public ProfileDto findProfileDtoByName(String profileName) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory();
            return profileDtoFactory.newDto(profileOpt.get());
        }
        return null;
    }

    public String findConfigPropertyValue(String key) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        return propertyService.getPropertyValue("properties/config.properties", key);
    }

    public void setConfigPropertyValue(String key, String value) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        propertyService.setProperty("properties/config.properties", key, value);
    }
}
