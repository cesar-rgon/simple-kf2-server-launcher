package stories;

import dtos.ProfileDto;
import dtos.factories.ProfileDtoFactory;
import entities.AbstractPlatform;
import entities.Profile;
import services.*;

import java.sql.SQLException;
import java.util.Optional;

public abstract class AbstractFacade {

    private final ProfileService profileService;
    private final PlatformService platformService;

    protected AbstractFacade() {
        super();
        this.profileService = new ProfileServiceImpl();
        this.platformService = new PlatformServiceImpl();
    }

    public Profile findProfileByCode(String profileName) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            return profileOpt.get();
        }
        return null;
    }

    public AbstractPlatform findPlatformByCode(String platformName) throws SQLException {
        Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(platformName);
        return platformOptional.isPresent() ? platformOptional.get(): null;
    }

    public ProfileDto findProfileDtoByName(String profileName) throws Exception {
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
