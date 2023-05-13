package stories.updateprofilename;

import dtos.factories.ProfileDtoFactory;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileNameFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileNameModelContext, UpdateProfileNameFacadeResult>
        implements UpdateProfileNameFacade {


    public UpdateProfileNameFacadeImpl(UpdateProfileNameModelContext updateProfileNameModelContext) {
        super(updateProfileNameModelContext, UpdateProfileNameFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(UpdateProfileNameModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected UpdateProfileNameFacadeResult internalExecute(UpdateProfileNameModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory(em);

        if (StringUtils.isBlank(facadeModelContext.getNewProfileName()) || facadeModelContext.getNewProfileName().equalsIgnoreCase(facadeModelContext.getOldProfileName())) {
            return new UpdateProfileNameFacadeResult();
        }

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getOldProfileName());
        if (!profileOpt.isPresent()) {
            return new UpdateProfileNameFacadeResult();
        }

        profileOpt.get().setCode(facadeModelContext.getNewProfileName());

        if (!profileService.updateItem(profileOpt.get())) {
            return new UpdateProfileNameFacadeResult();
        }

        return new UpdateProfileNameFacadeResult(
                profileDtoFactory.newDto(profileOpt.get())
        );
    }
}
