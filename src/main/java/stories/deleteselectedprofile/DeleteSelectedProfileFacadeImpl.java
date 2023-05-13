package stories.deleteselectedprofile;

import entities.AbstractPlatform;
import entities.EpicPlatform;
import entities.Profile;
import entities.SteamPlatform;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import org.apache.commons.io.FileUtils;
import pojos.kf2factory.Kf2Factory;
import services.PlatformService;
import services.PlatformServiceImpl;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeleteSelectedProfileFacadeImpl
        extends AbstractTransactionalFacade<DeleteSelectedProfileModelContext, EmptyFacadeResult>
        implements DeleteSelectedProfileFacade {

    public DeleteSelectedProfileFacadeImpl(DeleteSelectedProfileModelContext deleteSelectedProfileModelContext) {
        super(deleteSelectedProfileModelContext, EmptyFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(DeleteSelectedProfileModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(DeleteSelectedProfileModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        PlatformService platformService = new PlatformServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            return new EmptyFacadeResult();
        }

        if (profileService.deleteProfile(profileOpt.get())) {

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
                File platformProfileConfigFolder = new File(platform.getInstallationFolder() + "/KFGame/Config/" + facadeModelContext.getProfileName());
                if (platformProfileConfigFolder.exists()) {
                    FileUtils.deleteDirectory(platformProfileConfigFolder);
                }
            }
        }
        return new EmptyFacadeResult();
    }
}
