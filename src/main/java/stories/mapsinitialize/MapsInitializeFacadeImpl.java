package stories.mapsinitialize;

import dtos.ProfileDto;
import dtos.factories.ProfileDtoFactory;
import entities.EpicPlatform;
import entities.Profile;
import entities.SteamPlatform;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import javafx.collections.ObservableList;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import services.PlatformService;
import services.PlatformServiceImpl;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.List;
import java.util.Optional;

public class MapsInitializeFacadeImpl
        extends AbstractTransactionalFacade<MapsInitializeModelContext, MapsInitializeFacadeResult>
        implements MapsInitializeFacade {

    public MapsInitializeFacadeImpl(MapsInitializeModelContext mapsInitializeModelContext) {
        super(mapsInitializeModelContext, MapsInitializeFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(MapsInitializeModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        Optional<Profile> profileOptional = profileService.findProfileByCode(facadeModelContext.getProfileName());
        return profileOptional.isPresent();
    }

    @Override
    protected MapsInitializeFacadeResult internalExecute(MapsInitializeModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory(em);
        PlatformService platformService = new PlatformServiceImpl(em);

        List<Profile> allProfiles = profileService.listAllProfiles();
        ObservableList<ProfileDto> allProfileDtoList = profileDtoFactory.newDtos(allProfiles);

        ProfileDto actualProfileDto = null;
        Optional<Profile> actualProfile = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (actualProfile.isPresent()) {
            actualProfileDto = profileDtoFactory.newDto(actualProfile.get());
        }

        boolean steamHasCorrectInstallationFolder = false;
        Optional<SteamPlatform> steamPlatformOptional = platformService.findSteamPlatform();
        if (steamPlatformOptional.isPresent()) {
            Kf2Common steamKf2Common = Kf2Factory.getInstance(steamPlatformOptional.get(), em);
            assert steamKf2Common != null;
            steamHasCorrectInstallationFolder = steamKf2Common.isValidInstallationFolder();
        }

        boolean epicHasCorrectInstallationFolder = false;
        Optional<EpicPlatform> epicPlatformOptional = platformService.findEpicPlatform();
        if (epicPlatformOptional.isPresent()) {
            Kf2Common epicKf2Common = Kf2Factory.getInstance(epicPlatformOptional.get(), em);
            assert epicKf2Common != null;
            epicHasCorrectInstallationFolder = epicKf2Common.isValidInstallationFolder();
        }

        return new MapsInitializeFacadeResult(
                allProfileDtoList,
                actualProfileDto,
                steamHasCorrectInstallationFolder,
                epicHasCorrectInstallationFolder
        );
    }
}
