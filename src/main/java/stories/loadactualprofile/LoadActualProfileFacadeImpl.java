package stories.loadactualprofile;

import dtos.CustomMapModDto;
import dtos.PlatformDto;
import dtos.PlatformProfileMapDto;
import dtos.ProfileDto;
import dtos.factories.PlatformDtoFactory;
import dtos.factories.PlatformProfileMapDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.AbstractPlatform;
import entities.CustomMapMod;
import entities.PlatformProfileMap;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import services.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LoadActualProfileFacadeImpl
        extends AbstractTransactionalFacade<LoadActualProfileModelContext, LoadActualProfileFacadeResult>
        implements LoadActualProfileFacade {


    public LoadActualProfileFacadeImpl(LoadActualProfileModelContext loadActualProfileModelContext) {
        super(loadActualProfileModelContext, LoadActualProfileFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions(LoadActualProfileModelContext loadActualProfileModelContext, EntityManager em) throws Exception {
        PlatformService platformService = new PlatformServiceImpl(em);
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(loadActualProfileModelContext.getPlatformName());
        Optional<Profile> profileOptional = profileService.findProfileByCode(loadActualProfileModelContext.getProfileName());

        return platformOptional.isPresent() && profileOptional.isPresent();
    }

    @Override
    protected LoadActualProfileFacadeResult internalExecute(LoadActualProfileModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory(em);
        PlatformService platformService = new PlatformServiceImpl(em);

        Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(facadeModelContext.getPlatformName());
        Optional<Profile> profileOptional = profileService.findProfileByCode(facadeModelContext.getProfileName());

        Profile profile = profileOptional.get();
        List<PlatformProfileMapDto> platformProfileMapList = listPlatformProfileMaps(platformOptional.get(), profile, em);

        List<PlatformProfileMapDto> officialMapsInMapsCycle = platformProfileMapList.stream()
                .filter(ppm -> ppm.getMapDto().isOfficial())
                .filter(PlatformProfileMapDto::isInMapsCycle)
                .sorted((ppm1, ppm2) -> ppm1.getAlias().compareTo(ppm2.getAlias()))
                .collect(Collectors.toList());

        List<PlatformProfileMapDto> downloadedCustomMapsInMapsCycle = platformProfileMapList.stream()
                .filter(ppm -> !ppm.getMapDto().isOfficial())
                .filter(ppm -> ((CustomMapModDto)ppm.getMapDto()).isMap() != null ? ((CustomMapModDto)ppm.getMapDto()).isMap(): false)
                .filter(PlatformProfileMapDto::isDownloaded)
                .filter(PlatformProfileMapDto::isInMapsCycle)
                .sorted((ppm1, ppm2) -> ppm1.getAlias().compareTo(ppm2.getAlias()))
                .collect(Collectors.toList());

        List<PlatformProfileMapDto> filteredMapList = new ArrayList<PlatformProfileMapDto>(officialMapsInMapsCycle);
        filteredMapList.addAll(downloadedCustomMapsInMapsCycle);
        ObservableList<PlatformProfileMapDto> filteredMapDtoList = FXCollections.observableArrayList(filteredMapList);

        Optional<PlatformProfileMapDto> selectedProfileMapOptional = Optional.empty();
        if (profile.getMap() != null) {
            selectedProfileMapOptional = filteredMapList.stream()
                    .filter(pm -> pm.getMapDto().getKey().equals(profile.getMap().getCode()))
                    .findFirst();
        }

        ProfileDto profileDto = profileDtoFactory.newDto(profileOptional.get());

        return new LoadActualProfileFacadeResult(
                profileDto,
                filteredMapDtoList,
                selectedProfileMapOptional
        );

    }

    private List<PlatformProfileMapDto> listPlatformProfileMaps(AbstractPlatform platform, Profile profile, EntityManager em) throws Exception {
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);
        PlatformProfileMapDtoFactory platformProfileMapDtoFactory = new PlatformProfileMapDtoFactory(em);

        List<PlatformProfileMap> platformProfileMapList = platformProfileMapService.listPlatformProfileMaps(platform, profile);
        return platformProfileMapDtoFactory.newDtos(platformProfileMapList);
    }
}
