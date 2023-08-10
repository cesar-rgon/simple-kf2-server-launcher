package stories.cloneselectedprofile;

import dtos.factories.ProfileDtoFactory;
import entities.AbstractPlatform;
import entities.EpicPlatform;
import entities.Profile;
import entities.SteamPlatform;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import services.*;
import start.MainApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CloneSelectedProfileFacadeImpl
        extends AbstractTransactionalFacade<CloneSelectedProfileModelContext, CloneSelectedProfileFacadeResult>
        implements CloneSelectedProfileFacade {

    public CloneSelectedProfileFacadeImpl(CloneSelectedProfileModelContext cloneSelectedProfileModelContext) {
        super(cloneSelectedProfileModelContext, CloneSelectedProfileFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(CloneSelectedProfileModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected CloneSelectedProfileFacadeResult internalExecute(CloneSelectedProfileModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        PlatformService platformService = new PlatformServiceImpl(em);
        ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory(em);

        Optional<Profile> profileToBeClonedOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());

        if (!profileToBeClonedOpt.isPresent()) {
            return new CloneSelectedProfileFacadeResult();
        }

        Profile newProfile = profileService.cloneProfile(profileToBeClonedOpt.get(), facadeModelContext.getNewProfileName());

        List<AbstractPlatform> validPlatformList = new ArrayList<AbstractPlatform>();
        Optional<SteamPlatform> steamPlatformOptional = platformService.findSteamPlatform();
        if (steamPlatformOptional.isPresent()) {
            if (Kf2Factory.getInstance(steamPlatformOptional.get(), em).isValidInstallationFolder()) {
                validPlatformList.add(steamPlatformOptional.get());
            }
        }
        Optional<EpicPlatform> epicPlatformOptional = platformService.findEpicPlatform();
        if (epicPlatformOptional.isPresent()) {
            if (Kf2Factory.getInstance(epicPlatformOptional.get(), em).isValidInstallationFolder()) {
                validPlatformList.add(epicPlatformOptional.get());
            }
        }

        for (AbstractPlatform platform: validPlatformList) {
            Kf2Common kf2Common = Kf2Factory.getInstance(platform, em);
            kf2Common.createConfigFolder(platform.getInstallationFolder(), newProfile.getName());
        }

        if (MainApplication.getEmbeddedWebServer() != null) {
            MainApplication.getEmbeddedWebServer().stop();
            MainApplication.setEmbeddedWebServer(null);
        }

        return new CloneSelectedProfileFacadeResult(
                profileDtoFactory.newDto(newProfile)
        );
    }
}
