package stories.webadmin;

import daos.ProfileDao;
import dtos.ProfileDto;
import dtos.factories.ProfileDtoFactory;
import entities.Profile;

import java.sql.SQLException;
import java.util.Optional;

public class WebAdminFacadeImpl implements WebAdminFacade {

    private final ProfileDtoFactory profileDtoFactory;

    public WebAdminFacadeImpl() {
        super();
        this.profileDtoFactory = new ProfileDtoFactory();
    }

    @Override
    public ProfileDto findProfileByName(String profileName) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            return profileDtoFactory.newDto(profileOpt.get());
        }
        return null;
    }
}
