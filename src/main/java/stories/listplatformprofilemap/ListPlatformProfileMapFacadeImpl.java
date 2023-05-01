package stories.listplatformprofilemap;

import dtos.PlatformProfileMapDto;
import dtos.factories.PlatformProfileMapDtoFactory;
import entities.EpicPlatform;
import entities.PlatformProfileMap;
import entities.Profile;
import entities.SteamPlatform;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import services.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListPlatformProfileMapFacadeImpl
        extends AbstractTransactionalFacade<ListPlatformProfileMapModelContext, ListPlatformProfileMapFacadeResult>
        implements ListPlatformProfileMapFacade {

    public ListPlatformProfileMapFacadeImpl(ListPlatformProfileMapModelContext listPlatformProfileMapModelContext) {
        super(listPlatformProfileMapModelContext, ListPlatformProfileMapFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(ListPlatformProfileMapModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        Optional<Profile> profileOptional = profileService.findProfileByCode(facadeModelContext.getProfileName());
        return profileOptional.isPresent();
    }

    @Override
    protected ListPlatformProfileMapFacadeResult internalExecute(ListPlatformProfileMapModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);
        PlatformProfileMapDtoFactory platformProfileMapDtoFactory = new PlatformProfileMapDtoFactory(em);
        PlatformService platformService = new PlatformServiceImpl(em);

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
        if (steamPlatformOptional.isPresent() && actualProfile.isPresent() && steamHasCorrectInstallationFolder) {
            List<PlatformProfileMap> steamPlatformProfileMapList = platformProfileMapService.listPlatformProfileMaps(
                    steamPlatformOptional.get(),
                    actualProfile.get()
            );
            steamPlatformProfileMapDtoList = platformProfileMapDtoFactory.newDtos(steamPlatformProfileMapList);
        }

        List<PlatformProfileMapDto> epicPlatformProfileMapDtoList = new ArrayList<PlatformProfileMapDto>();
        if (epicPlatformOptional.isPresent() && actualProfile.isPresent() && epicHasCorrectInstallationFolder) {
            List<PlatformProfileMap> epicPlatformProfileMapList = platformProfileMapService.listPlatformProfileMaps(
                    epicPlatformOptional.get(),
                    actualProfile.get()
            );
            epicPlatformProfileMapDtoList = platformProfileMapDtoFactory.newDtos(epicPlatformProfileMapList);
        }

        return new ListPlatformProfileMapFacadeResult(
                steamPlatformProfileMapDtoList,
                epicPlatformProfileMapDtoList
        );
    }
}
