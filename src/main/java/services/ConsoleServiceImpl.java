package services;

import entities.AbstractPlatform;
import entities.Profile;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import pojos.session.Session;
import start.MainApplication;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ConsoleServiceImpl implements ConsoleService {

    private static final Logger logger = LogManager.getLogger(ConsoleServiceImpl.class);
    private final EntityManager em;
    private final ProfileService profileService;
    private final PlatformService platformService;

    public ConsoleServiceImpl(EntityManager em) {
        super();
        this.em = em;
        this.platformService = new PlatformServiceImpl(em);
        this.profileService = new ProfileServiceImpl(em);
    }

    @Override
    public void runServersByConsole(List<String> parameters) {
        for (String parameter: parameters) {
            String errorMessage = "\nInvalid parameters.\nUse --pp platformName/profileName [ platformName2/profileName2 ... ].\nValid platform names are: Steam, Epic.\nCase sensitive letters must be used in Profile Name.\n";
            try {
                String[] parameterArray = parameter.split("/");
                if (parameterArray.length != 2) {
                    throw new RuntimeException(errorMessage);
                }
                String platformName = parameterArray[0].toUpperCase();
                String profileName = parameterArray[1];

                Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(platformName);
                Optional<Profile> profileOptional = profileService.findProfileByCode(profileName);
                if (!platformOptional.isPresent() || !profileOptional.isPresent()) {
                    throw new RuntimeException(errorMessage);
                }

                Kf2Common kf2Common = Kf2Factory.getInstance(platformOptional.get(), em);
                kf2Common.runServerByConsole(profileOptional.get());

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                System.out.println(e.getMessage());
            }

        }
    }
}
