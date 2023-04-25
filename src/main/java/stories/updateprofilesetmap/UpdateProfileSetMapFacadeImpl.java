package stories.updateprofilesetmap;

import entities.AbstractMap;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.*;

import java.util.Optional;

public class UpdateProfileSetMapFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetMapModelContext, EmptyFacadeResult>
        implements UpdateProfileSetMapFacade {

    public UpdateProfileSetMapFacadeImpl(UpdateProfileSetMapModelContext updateProfileSetMapModelContext) {
        super(updateProfileSetMapModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }


    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetMapModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        AbstractMapService mapService;
        if (facadeModelContext.isOfficial()) {
            mapService = new OfficialMapServiceImpl(em);
        } else {
            mapService = new CustomMapModServiceImpl(em);
        }

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating map in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        Optional<AbstractMap> mapOpt = mapService.findByCode(facadeModelContext.getMapCode());
        if (!mapOpt.isPresent()) {
            throw new RuntimeException("Error updating map in Profile. The map can not be found [map name: " + facadeModelContext.getMapCode() + "]");
        }

        profile.setMap(mapOpt.get());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
