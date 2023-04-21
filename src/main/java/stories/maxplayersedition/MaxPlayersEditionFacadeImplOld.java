package stories.maxplayersedition;

import dtos.ProfileDto;
import dtos.SelectDto;
import dtos.factories.MaxPlayersDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.MaxPlayers;
import entities.Profile;
import services.*;
import stories.OldAEditionFacade;

import java.util.Optional;

public class MaxPlayersEditionFacadeImplOld extends OldAEditionFacade<MaxPlayers, SelectDto> implements MaxPlayersEditionFacade {

    private final ProfileService profileService;
    private final PlatformProfileMapService platformProfileMapService;

    public MaxPlayersEditionFacadeImplOld() {
        super(
                MaxPlayers.class,
                new MaxPlayersDtoFactory(),
                new MaxPlayersServiceImpl(null)
        );
        this.profileService = new ProfileServiceImpl(em);
        this.platformProfileMapService = new PlatformProfileMapServiceImpl(em);
    }

    @Override
    public ProfileDto unselectMaxPlayersInProfile(String profileName) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            profileOpt.get().setMaxPlayers(null);
            profileService.updateItem(profileOpt.get());
            ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory(em);
            return profileDtoFactory.newDto(profileOpt.get());
        }
        return null;
    }
}
