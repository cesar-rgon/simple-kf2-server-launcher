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
    private final GameTypeServiceImpl gameTypeService;
    public GameTypesEditionFacadeImpl() {
        super(
                GameType.class,
                new GameTypeDtoFactory(),
                new GameTypeServiceImpl()
        );
        this.profileService = new ProfileServiceImpl();
        this.gameTypeService = new GameTypeServiceImpl();
    }

    @Override
    public GameTypeDto updateChangedDifficultiesEnabled(String code, Boolean newDifficultiesEnabled) throws Exception {
        Optional<GameType> gameTypeOpt = gameTypeService.findByCode(code);
        if (gameTypeOpt.isPresent()) {
            gameTypeOpt.get().setDifficultyEnabled(newDifficultiesEnabled);
            if (gameTypeService.updateItem(gameTypeOpt.get())) {
                return (GameTypeDto) dtoFactory.newDto(gameTypeOpt.get());
            }
        }
        return null;
    }

    @Override
    public GameTypeDto updateChangedLengthsEnabled(String code, Boolean newLengthsEnabled) throws Exception {
        Optional<GameType> gameTypeOpt = gameTypeService.findByCode(code);
        if (gameTypeOpt.isPresent()) {
            gameTypeOpt.get().setLengthEnabled(newLengthsEnabled);
            if (gameTypeService.updateItem(gameTypeOpt.get())) {
                return (GameTypeDto) dtoFactory.newDto(gameTypeOpt.get());
            }
        }
        return null;
    }

    @Override
    public ProfileDto unselectGametypeInProfile(String profileName) throws Exception {
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
