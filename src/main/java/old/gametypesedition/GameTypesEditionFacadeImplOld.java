package old.gametypesedition;

import dtos.GameTypeDto;
import dtos.ProfileDto;
import dtos.factories.GameTypeDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.GameType;
import entities.Profile;
import services.GameTypeServiceImpl;
import services.ProfileService;
import services.ProfileServiceImpl;
import old.OldAEditionFacade;

import java.util.Optional;

public class GameTypesEditionFacadeImplOld extends OldAEditionFacade<GameType, GameTypeDto> implements GameTypesEditionFacade {

    private final ProfileService profileService;
    private final GameTypeServiceImpl gameTypeService;
    public GameTypesEditionFacadeImplOld() {
        super(
                GameType.class,
                new GameTypeDtoFactory(),
                new GameTypeServiceImpl(null)
        );
        this.profileService = new ProfileServiceImpl(em);
        this.gameTypeService = new GameTypeServiceImpl(em);
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
            profileService.updateItem(profileOpt.get());
            ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory(em);
            return profileDtoFactory.newDto(profileOpt.get());
        }
        return null;
    }
}
