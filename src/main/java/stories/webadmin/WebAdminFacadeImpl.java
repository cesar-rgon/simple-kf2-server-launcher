package stories.webadmin;

import dtos.factories.ProfileDtoFactory;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class WebAdminFacadeImpl
        extends AbstractTransactionalFacade<WebAdminModelContext, WebAdminFacadeResult>
        implements WebAdminFacade {

    public WebAdminFacadeImpl(WebAdminModelContext webAdminModelContext) {
        super(webAdminModelContext, WebAdminFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(WebAdminModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected WebAdminFacadeResult internalExecute(WebAdminModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("The profile [" + facadeModelContext.getProfileName() + "] can not be found!");
        }

        ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory(em);
        return new WebAdminFacadeResult(
                profileDtoFactory.newDto(profileOpt.get())
        );
    }
}
