package stories.listProfiles;

import dtos.ProfileDto;
import dtos.factories.ProfileDtoFactory;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import javafx.collections.ObservableList;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.List;
import java.util.Optional;

public class ListProfilesFacadeImpl
        extends AbstractTransactionalFacade<ListProfilesModelContext, ListProfilesFacadeResult>
        implements ListProfilesFacade {

    public ListProfilesFacadeImpl(ListProfilesModelContext listProfilesModelContext) {
        super(listProfilesModelContext, ListProfilesFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(ListProfilesModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        Optional<Profile> profileOptional = profileService.findProfileByCode(facadeModelContext.getProfileName());
        return profileOptional.isPresent();
    }

    @Override
    protected ListProfilesFacadeResult internalExecute(ListProfilesModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory(em);

        List<Profile> allProfiles = profileService.listAllProfiles();
        ObservableList<ProfileDto> allProfileDtoList = profileDtoFactory.newDtos(allProfiles);

        ProfileDto actualProfileDto = null;
        Optional<Profile> actualProfile = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (actualProfile.isPresent()) {
            actualProfileDto = profileDtoFactory.newDto(actualProfile.get());
        }

        return new ListProfilesFacadeResult(
                allProfileDtoList,
                actualProfileDto
        );
    }
}
