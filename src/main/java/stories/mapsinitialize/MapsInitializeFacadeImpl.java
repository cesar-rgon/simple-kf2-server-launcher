package stories.mapsinitialize;

import dtos.PlatformProfileMapDto;
import dtos.ProfileDto;
import dtos.factories.PlatformProfileMapDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.EpicPlatform;
import entities.PlatformProfileMap;
import entities.Profile;
import entities.SteamPlatform;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import javafx.collections.ObservableList;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import services.*;

import java.util.ArrayList;
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
        PlatformService platformService = new PlatformServiceImpl(em);
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);
        PlatformProfileMapDtoFactory platformProfileMapDtoFactory = new PlatformProfileMapDtoFactory(em);

        Optional<Profile> actualProfile = profileService.findProfileByCode(facadeModelContext.getProfileName());

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

        List<PlatformProfileMapDto> steamPlatformProfileMapDtoList = new ArrayList<PlatformProfileMapDto>();
        if (steamPlatformOptional.isPresent() && actualProfile.isPresent()) {
            List<PlatformProfileMap> steamPlatformProfileMapList = platformProfileMapService.listPlatformProfileMaps(
                    steamPlatformOptional.get(),
                    actualProfile.get()
            );
            steamPlatformProfileMapDtoList = platformProfileMapDtoFactory.newDtos(steamPlatformProfileMapList);
        }

        List<PlatformProfileMapDto> epicPlatformProfileMapDtoList = new ArrayList<PlatformProfileMapDto>();
        if (epicPlatformOptional.isPresent() && actualProfile.isPresent()) {
            List<PlatformProfileMap> epicPlatformProfileMapList = platformProfileMapService.listPlatformProfileMaps(
                    epicPlatformOptional.get(),
                    actualProfile.get()
            );
            epicPlatformProfileMapDtoList = platformProfileMapDtoFactory.newDtos(epicPlatformProfileMapList);
        }

        return new MapsInitializeFacadeResult(
                steamHasCorrectInstallationFolder,
                epicHasCorrectInstallationFolder,
                steamPlatformProfileMapDtoList,
                epicPlatformProfileMapDtoList
        );
    }
}
