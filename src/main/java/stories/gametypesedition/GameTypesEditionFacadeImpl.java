package stories.gametypesedition;

import daos.GameTypeDao;
import daos.ProfileDao;
import dtos.GameTypeDto;
import dtos.ProfileDto;
import dtos.factories.GameTypeDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.GameType;
import entities.Profile;
import services.GameTypeServiceImpl;
import services.ProfileService;
import services.ProfileServiceImpl;
import stories.AbstractEditionFacade;

import java.sql.SQLException;
import java.util.Optional;

public class GameTypesEditionFacadeImpl extends AbstractEditionFacade<GameType, GameTypeDto> implements GameTypesEditionFacade {

    private final ProfileService profileService;

    public GameTypesEditionFacadeImpl() {
        super(
                GameType.class,
                GameTypeDao.getInstance(),
                new GameTypeDtoFactory(),
                new GameTypeServiceImpl()
        );
        this.profileService = new ProfileServiceImpl();
    }

    @Override
    public GameTypeDto updateChangedDifficultiesEnabled(String code, Boolean newDifficultiesEnabled) throws SQLException {
        Optional<GameType> gameTypeOpt = GameTypeDao.getInstance().findByCode(code);
        if (gameTypeOpt.isPresent()) {
            gameTypeOpt.get().setDifficultyEnabled(newDifficultiesEnabled);
            if (GameTypeDao.getInstance().update(gameTypeOpt.get())) {
                return (GameTypeDto) dtoFactory.newDto(gameTypeOpt.get());
            }
        }
        return null;
    }

    @Override
    public GameTypeDto updateChangedLengthsEnabled(String code, Boolean newLengthsEnabled) throws SQLException {
        Optional<GameType> gameTypeOpt = GameTypeDao.getInstance().findByCode(code);
        if (gameTypeOpt.isPresent()) {
            gameTypeOpt.get().setLengthEnabled(newLengthsEnabled);
            if (GameTypeDao.getInstance().update(gameTypeOpt.get())) {
                return (GameTypeDto) dtoFactory.newDto(gameTypeOpt.get());
            }
        }
        return null;
    }

    @Override
    public ProfileDto unselectGametypeInProfile(String profileName) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            profileOpt.get().setGametype(null);
            ProfileDao.getInstance().update(profileOpt.get());
            ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory();
            return profileDtoFactory.newDto(profileOpt.get());
        }
        return null;
    }
}
