package stories.maxplayersedition;

import daos.MaxPlayersDao;
import daos.ProfileDao;
import dtos.ProfileDto;
import dtos.SelectDto;
import dtos.factories.MaxPlayersDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.MaxPlayers;
import entities.Profile;
import services.MaxPlayersServiceImpl;
import services.ProfileService;
import services.ProfileServiceImpl;
import stories.AbstractEditionFacade;

import java.sql.SQLException;
import java.util.Optional;

public class MaxPlayersEditionFacadeImpl extends AbstractEditionFacade<MaxPlayers, SelectDto>  implements MaxPlayersEditionFacade {

    private final ProfileService profileService;

    public MaxPlayersEditionFacadeImpl() {
        super(
                MaxPlayers.class,
                new MaxPlayersDtoFactory(),
                new MaxPlayersServiceImpl()
        );
        this.profileService = new ProfileServiceImpl();
    }

    @Override
    public ProfileDto unselectMaxPlayersInProfile(String profileName) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            profileOpt.get().setMaxPlayers(null);
            ProfileDao.getInstance().update(profileOpt.get());
            ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory();
            return profileDtoFactory.newDto(profileOpt.get());
        }
        return null;
    }
}
