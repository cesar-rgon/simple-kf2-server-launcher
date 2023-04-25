package old.lengthedition;

import dtos.ProfileDto;
import dtos.SelectDto;
import dtos.factories.LengthDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.Length;
import entities.Profile;
import services.LengthServiceImpl;
import services.ProfileService;
import services.ProfileServiceImpl;
import old.OldAEditionFacade;

import java.util.Optional;

public class LengthEditionFacadeImplOld extends OldAEditionFacade<Length, SelectDto> implements LengthEditionFacade {

    private final ProfileService profileService;

    public LengthEditionFacadeImplOld() {
        super(
                Length.class,
                new LengthDtoFactory(),
                new LengthServiceImpl(null)
        );
        this.profileService = new ProfileServiceImpl(em);
    }

    @Override
    public ProfileDto unselectLengthInProfile(String profileName) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            profileOpt.get().setLength(null);
            profileService.updateItem(profileOpt.get());
            ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory(em);
            return profileDtoFactory.newDto(profileOpt.get());
        }
        return null;
    }
}
