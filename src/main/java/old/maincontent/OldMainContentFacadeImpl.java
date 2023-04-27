package old.maincontent;

import dtos.*;
import dtos.factories.*;
import entities.*;
import org.apache.commons.lang3.StringUtils;
import pojos.PlatformProfile;
import pojos.PlatformProfileToDisplay;
import pojos.enums.EnumPlatform;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import services.*;
import old.OldAFacade;
import utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OldMainContentFacadeImpl extends OldAFacade {

    private final LanguageDtoFactory languageDtoFactory;
    private final ProfileDtoFactory profileDtoFactory;
    private final GameTypeDtoFactory gameTypeDtoFactory;
    private final DifficultyDtoFactory difficultyDtoFactory;
    private final LengthDtoFactory lengthDtoFactory;
    private final MaxPlayersDtoFactory maxPlayersDtoFactory;
    private final ProfileService profileService;
    private final PlatformProfileMapDtoFactory platformProfileMapDtoFactory;
    private final PlatformProfileMapService platformProfileMapService;
    private final PlatformDtoFactory platformDtoFactory;
    private final PlatformService platformService;
    private final AbstractMapService officialMapService;
    private final AbstractMapService customMapService;
    private final DifficultyServiceImpl difficultyService;
    private final GameTypeServiceImpl gameTypeService;
    private final LanguageServiceImpl languageService;
    private final LengthServiceImpl lengthService;
    private final MaxPlayersServiceImpl maxPlayersService;

    public OldMainContentFacadeImpl() {
        super(null);
        languageDtoFactory = new LanguageDtoFactory();
        profileDtoFactory = new ProfileDtoFactory(em);
        gameTypeDtoFactory = new GameTypeDtoFactory();
        difficultyDtoFactory = new DifficultyDtoFactory();
        lengthDtoFactory = new LengthDtoFactory();
        maxPlayersDtoFactory = new MaxPlayersDtoFactory();
        profileService = new ProfileServiceImpl(em);
        platformProfileMapDtoFactory = new PlatformProfileMapDtoFactory(em);
        platformProfileMapService = new PlatformProfileMapServiceImpl(em);
        platformDtoFactory = new PlatformDtoFactory();
        platformService = new PlatformServiceImpl(em);
        officialMapService = new OfficialMapServiceImpl(em);
        customMapService = new CustomMapModServiceImpl(em);
        difficultyService = new DifficultyServiceImpl(em);
        gameTypeService = new GameTypeServiceImpl(em);
        languageService = new LanguageServiceImpl(em);
        lengthService = new LengthServiceImpl(em);
        maxPlayersService = new MaxPlayersServiceImpl(em);
    }


    public boolean isCorrectInstallationFolder(String platformName) throws SQLException {
        Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(platformName);
        if (!platformOptional.isPresent()) {
            Utils.warningDialog("Operation aborted!", "The platform can not be found");
            return false;
        }
        Kf2Common kf2Common = Kf2Factory.getInstance(platformOptional.get());
        return kf2Common.isValidInstallationFolder();
    }
}
