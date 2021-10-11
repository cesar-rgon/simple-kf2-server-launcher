package stories.difficultiesedition;

import daos.DifficultyDao;
import daos.ProfileDao;
import dtos.ProfileDto;
import dtos.SelectDto;
import dtos.factories.DifficultyDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.Difficulty;
import entities.Profile;
import services.DifficultyServiceImpl;
import stories.AbstractEditionFacade;

import java.sql.SQLException;
import java.util.Optional;

public class DifficultiesEditionFacadeImpl extends AbstractEditionFacade<Difficulty, SelectDto> implements DifficultiesEditionFacade {

    public DifficultiesEditionFacadeImpl() {
        super(
                Difficulty.class,
                DifficultyDao.getInstance(),
                new DifficultyDtoFactory(),
                new DifficultyServiceImpl()
        );
    }


    @Override
    public ProfileDto unselectDifficultyInProfile(String profileName) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByCode(profileName);
        if (profileOpt.isPresent()) {
            profileOpt.get().setDifficulty(null);
            ProfileDao.getInstance().update(profileOpt.get());
            ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory();
            return profileDtoFactory.newDto(profileOpt.get());
        }
        return null;
    }
}
