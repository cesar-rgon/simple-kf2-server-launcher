package stories.listvaluesmaincontent;

import dtos.GameTypeDto;
import dtos.PlatformDto;
import dtos.ProfileDto;
import dtos.SelectDto;
import dtos.factories.*;
import entities.*;
import framework.AbstractTransactionalFacade;
import framework.EmptyModelContext;
import jakarta.persistence.EntityManager;
import javafx.collections.ObservableList;
import services.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListValuesMainContentFacadeImpl
        extends AbstractTransactionalFacade<EmptyModelContext, ListValuesMainContentFacadeResult>
        implements ListValuesMainContentFacade {

    public ListValuesMainContentFacadeImpl() {
        super(new EmptyModelContext(), ListValuesMainContentFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    public ListValuesMainContentFacadeResult internalExecute(EmptyModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory(em);
        LanguageServiceImpl languageService = new LanguageServiceImpl(em);
        LanguageDtoFactory languageDtoFactory = new LanguageDtoFactory();
        GameTypeServiceImpl gameTypeService = new GameTypeServiceImpl(em);
        GameTypeDtoFactory gameTypeDtoFactory = new GameTypeDtoFactory();
        DifficultyServiceImpl difficultyService = new DifficultyServiceImpl(em);
        DifficultyDtoFactory difficultyDtoFactory = new DifficultyDtoFactory();
        LengthServiceImpl lengthService = new LengthServiceImpl(em);
        LengthDtoFactory lengthDtoFactory = new LengthDtoFactory();
        MaxPlayersServiceImpl maxPlayersService = new MaxPlayersServiceImpl(em);
        MaxPlayersDtoFactory maxPlayersDtoFactory = new MaxPlayersDtoFactory();

        List<Profile> profileList = profileService.listAllProfiles();
        ObservableList<ProfileDto> profileDtoList = profileDtoFactory.newDtos(profileList);
        ProfileDto lastSelectedProfile = getLastSelectedProfile(profileService, profileDtoFactory);

        List<Language> languageList = languageService.listAll();
        ObservableList<SelectDto> languageDtoList = languageDtoFactory.newDtos(languageList);

        List<GameType> gameTypeList = gameTypeService.listAll();
        ObservableList<GameTypeDto> gameTypeDtoList = gameTypeDtoFactory.newDtos(gameTypeList);

        List<Difficulty> difficultyList = difficultyService.listAll();
        ObservableList<SelectDto> difficultyDtoList = difficultyDtoFactory.newDtos(difficultyList);

        List<Length> lengthList = lengthService.listAll();
        ObservableList<SelectDto> lengthDtoList = lengthDtoFactory.newDtos(lengthList);

        List<MaxPlayers> playerList = maxPlayersService.listAll();
        ObservableList<SelectDto> playerDtoList = maxPlayersDtoFactory.newDtos(playerList);

        ObservableList<PlatformDto> platformDtoList = listAllPlatforms(em);

        return new ListValuesMainContentFacadeResult(
                profileDtoList,
                languageDtoList,
                gameTypeDtoList,
                difficultyDtoList,
                lengthDtoList,
                playerDtoList,
                platformDtoList,
                lastSelectedProfile
        );
    }


    private ObservableList<PlatformDto> listAllPlatforms(EntityManager em) throws SQLException {
        PlatformService platformService = new PlatformServiceImpl(em);
        PlatformDtoFactory platformDtoFactory = new PlatformDtoFactory();

        Optional<SteamPlatform> steamPlatformOptional = platformService.findSteamPlatform();
        Optional<EpicPlatform> epicPlatformOptional = platformService.findEpicPlatform();

        List<AbstractPlatform> platformList = new ArrayList<AbstractPlatform>();
        if (steamPlatformOptional.isPresent()) {
            platformList.add(steamPlatformOptional.get());
        }
        if (epicPlatformOptional.isPresent()) {
            platformList.add(epicPlatformOptional.get());
        }

        return platformDtoFactory.newDtos(platformList);
    }

    private ProfileDto getLastSelectedProfile(ProfileService profileService, ProfileDtoFactory profileDtoFactory) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();

        String lastProfileName = propertyService.getPropertyValue("properties/config.properties", "prop.config.lastSelectedProfile");
        Optional<Profile> lastProfile = profileService.findProfileByCode(lastProfileName);
        if (lastProfile.isPresent()) {
            return profileDtoFactory.newDto(lastProfile.get());
        } else {
            List<Profile> profileList = profileService.listAllProfiles();
            if (profileList != null && !profileList.isEmpty()) {
                return profileDtoFactory.newDto(profileList.get(0));
            } else {
                return null;
            }
        }
    }
}
