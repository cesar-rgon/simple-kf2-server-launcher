package pojos;

import entities.AbstractPlatform;
import entities.Profile;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.enums.EnumPlatform;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlatformProfileDaemonToDisplayFactory {

    private static final Logger logger = LogManager.getLogger(PlatformProfileDaemonToDisplayFactory.class);

    private final ProfileService profileService;

    public PlatformProfileDaemonToDisplayFactory(EntityManager em) {
        super();
        profileService = new ProfileServiceImpl(em);
    }

    public PlatformProfileDaemonToDisplay newOne(Profile profile, EnumPlatform enumPlatform, String serviceStatus) {
        return new PlatformProfileDaemonToDisplay(
                enumPlatform,
                profile.getId(),
                profile.getCode(),
                profile.getWebPort(),
                profile.getGamePort(),
                profile.getQueryPort(),
                serviceStatus
        );

    }

    public List<PlatformProfileDaemonToDisplay> newOnes(List<PlatformProfileDaemon> platformProfileDaemonList) {

        List<PlatformProfileDaemonToDisplay> result = new ArrayList<PlatformProfileDaemonToDisplay>();
        for (PlatformProfileDaemon platformProfileDaemon: platformProfileDaemonList) {

            EnumPlatform enumPlatform = EnumPlatform.getByName(platformProfileDaemon.getPlatform().getCode());
            if (enumPlatform == null) {
                continue;
            }
            result.add(newOne(
                    platformProfileDaemon.getProfile(),
                    enumPlatform,
                    platformProfileDaemon.getServiceStatus()
            ));
        }

        return result;
    }
}
