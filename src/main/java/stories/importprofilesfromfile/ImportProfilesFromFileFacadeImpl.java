package stories.importprofilesfromfile;

import dtos.factories.ProfileDtoFactory;
import entities.AbstractPlatform;
import entities.EpicPlatform;
import entities.Profile;
import entities.SteamPlatform;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import services.PlatformService;
import services.PlatformServiceImpl;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImportProfilesFromFileFacadeImpl
        extends AbstractTransactionalFacade<ImportProfilesFromFileModelContext, ImportProfilesFromFileFacadeResult>
        implements ImportProfilesFromFileFacade {

    private static final Logger logger = LogManager.getLogger(ImportProfilesFromFileFacadeImpl.class);

    public ImportProfilesFromFileFacadeImpl(ImportProfilesFromFileModelContext importProfilesFromFileModelContext) {
        super(importProfilesFromFileModelContext, ImportProfilesFromFileFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(ImportProfilesFromFileModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected ImportProfilesFromFileFacadeResult internalExecute(ImportProfilesFromFileModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory(em);

        List<Profile> importedProfileList = profileService.importProfilesFromFile(facadeModelContext.getSelectedProfileList(), facadeModelContext.getProperties(), facadeModelContext.getErrorMessage());

        if (importedProfileList.isEmpty()) {
            return new ImportProfilesFromFileFacadeResult();
        }

        for (Profile importedProfile: importedProfileList) {
            try {
                createConfigFolder(importedProfile.getName(), em);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        return new ImportProfilesFromFileFacadeResult(
                profileDtoFactory.newDtos(importedProfileList)
        );
    }

    private void createConfigFolder(String profileName, EntityManager em) throws SQLException {
        PlatformService platformService = new PlatformServiceImpl(em);

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
            kf2Common.createConfigFolder(platform.getInstallationFolder(), profileName);
        }
    }
}
