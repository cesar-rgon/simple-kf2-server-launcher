package stories.profilesedition;

import daos.ProfileDao;
import dtos.ProfileDto;
import dtos.factories.ProfileDtoFactory;
import entities.Profile;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class ProfilesEditionFacadeImpl implements ProfilesEditionFacade {

    private final ProfileDtoFactory profileDtoFactory;

    public ProfilesEditionFacadeImpl() {
        profileDtoFactory = new ProfileDtoFactory();
    }

    @Override
    public ObservableList<ProfileDto> listAllProfiles() throws SQLException {
        List<Profile> profiles = ProfileDao.getInstance().listAll();
        return profileDtoFactory.newDtos(profiles);
    }

}
