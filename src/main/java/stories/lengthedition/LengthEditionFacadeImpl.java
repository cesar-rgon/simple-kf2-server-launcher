package stories.lengthedition;

import daos.LengthDao;
import daos.ProfileDao;
import dtos.ProfileDto;
import dtos.SelectDto;
import dtos.factories.LengthDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.Length;
import entities.Profile;
import services.LengthServiceImpl;
import services.ProfileService;
import services.ProfileServiceImpl;
import stories.AbstractEditionFacade;

import java.sql.SQLException;
import java.util.Optional;

public class LengthEditionFacadeImpl extends AbstractEditionFacade<Length, SelectDto> implements LengthEditionFacade {

    private final ProfileService profileService;

    public LengthEditionFacadeImpl() {
        super(
                Length.class,
                LengthDao.getInstance(),
                new LengthDtoFactory(),
                new LengthServiceImpl()
        );
        this.profileService = new ProfileServiceImpl();
    }

    @Override
    public ProfileDto unselectLengthInProfile(String profileName) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            profileOpt.get().setLength(null);
            ProfileDao.getInstance().update(profileOpt.get());
            ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory();
            return profileDtoFactory.newDto(profileOpt.get());
        }
        return null;
    }
}
