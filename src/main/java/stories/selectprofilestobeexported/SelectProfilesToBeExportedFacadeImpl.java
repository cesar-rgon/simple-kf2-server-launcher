package stories.selectprofilestobeexported;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyModelContext;
import jakarta.persistence.EntityManager;
import pojos.ProfileToDisplay;
import pojos.ProfileToDisplayFactory;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.List;

public class SelectProfilesToBeExportedFacadeImpl
        extends AbstractTransactionalFacade<EmptyModelContext, SelectProfilesToBeExportedFacadeResult>
        implements SelectProfilesToBeExportedFacade {

    public SelectProfilesToBeExportedFacadeImpl() {
        super(new EmptyModelContext(), SelectProfilesToBeExportedFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(EmptyModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected SelectProfilesToBeExportedFacadeResult internalExecute(EmptyModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        ProfileToDisplayFactory profileToDisplayFactory = new ProfileToDisplayFactory();

        List<Profile> allProfiles = profileService.listAllProfiles();
        List<ProfileToDisplay> allProfilesToDisplay = profileToDisplayFactory.newOnes(allProfiles);
        allProfilesToDisplay.stream().forEach(p -> p.setSelected(true));
        return new SelectProfilesToBeExportedFacadeResult(
                allProfilesToDisplay
        );
    }
}
