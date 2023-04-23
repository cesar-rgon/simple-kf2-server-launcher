package stories.maincontent;

import dtos.*;
import dtos.factories.*;
import entities.*;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import pojos.PlatformProfile;
import pojos.PlatformProfileToDisplay;
import pojos.enums.EnumPlatform;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import services.*;
import stories.OldAFacade;
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

    public boolean updateProfileSetGameType(String profileName, String gameTypeCode) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<GameType> gameTypeOpt = gameTypeService.findByCode(gameTypeCode);
            if (gameTypeOpt.isPresent()) {
                profile.setGametype(gameTypeOpt.get());
                return profileService.updateItem(profile);
            }
        }
        return false;
    }

    
    public boolean updateProfileSetMap(String profileName, String mapCode, boolean isOfficial) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        Optional mapOptionaL;
        if (isOfficial) {
            mapOptionaL = officialMapService.findByCode(mapCode);
        } else {
            mapOptionaL = customMapService.findByCode(mapCode);
        }

        if (profileOpt.isPresent() && mapOptionaL.isPresent()) {
            profileOpt.get().setMap((AbstractMap) mapOptionaL.get());
            return profileService.updateItem(profileOpt.get());
        }
        return false;
    }

    
    public boolean updateProfileSetDifficulty(String profileName, String difficultyCode) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<Difficulty> difficultyOpt = difficultyService.findByCode(difficultyCode);
            if (difficultyOpt.isPresent()) {
                profile.setDifficulty(difficultyOpt.get());
                return profileService.updateItem(profile);
            }
        }
        return false;
    }

    
    public boolean updateProfileSetLength(String profileName, String lengthCode) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<Length> lengthOpt = lengthService.findByCode(lengthCode);
            if (lengthOpt.isPresent()) {
                profile.setLength(lengthOpt.get());
                return profileService.updateItem(profile);
            }
        }
        return false;
    }

    
    public boolean updateProfileSetMaxPlayers(String profileName, String maxPlayersCode) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<MaxPlayers> maxPlayersOpt = maxPlayersService.findByCode(maxPlayersCode);
            if (maxPlayersOpt.isPresent()) {
                profile.setMaxPlayers(maxPlayersOpt.get());
                return profileService.updateItem(profile);
            }
        }
        return false;
    }

    
    public boolean updateProfileSetLanguage(String profileName, String languageCode) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<Language> languageOpt = languageService.findByCode(languageCode);
            if (languageOpt.isPresent()) {
                profile.setLanguage(languageOpt.get());
                return profileService.updateItem(profile);
            }
        }
        return false;
    }

    
    public boolean updateProfileSetServerName(String profileName, String serverName) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setServerName(serverName);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetServerPassword(String profileName, String serverPassword) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setServerPassword(Utils.encryptAES(serverPassword));
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetWebPassword(String profileName, String webPassword) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setWebPassword(Utils.encryptAES(webPassword));
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetWebPort(String profileName, Integer webPort) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setWebPort(webPort);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetGamePort(String profileName, Integer gamePort) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setGamePort(gamePort);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetQueryPort(String profileName, Integer queryPort) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setQueryPort(queryPort);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetYourClan(String profileName, String yourClan) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setYourClan(yourClan);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetYourWebLink(String profileName, String yourWebLink) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setYourWebLink(yourWebLink);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetUrlImageServer(String profileName, String urlImageServer) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setUrlImageServer(urlImageServer);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetWelcomeMessage(String profileName, String welcomeMessage) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setWelcomeMessage(welcomeMessage);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetCustomParameters(String profileName, String customParameters) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setCustomParameters(customParameters);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetWebPage(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setWebPage(isSelected);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public String runServer(String platformName, String profileName) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(platformName);
        if (!platformOptional.isPresent()) {
            Utils.warningDialog("Run operation aborted!", "The platform can not be found");
            return StringUtils.EMPTY;
        }
        Kf2Common kf2Common = Kf2Factory.getInstance(platformOptional.get());
        return kf2Common.runServer(profileOpt.isPresent() ? profileOpt.get() : null);
    }

    
    public String joinServer(String platformName, String profileName) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(platformName);
        if (!platformOptional.isPresent()) {
            Utils.warningDialog("Join operation aborted!", "The platform can not be found");
            return StringUtils.EMPTY;
        }
        if (profileOpt.isPresent()) {
            Kf2Common kf2Common = Kf2Factory.getInstance(platformOptional.get());
            return kf2Common.joinServer(profileOpt.get());
        } else {
            Utils.warningDialog("Join operation aborted!", "The profile name can not be empty");
        }
        return StringUtils.EMPTY;
    }

    
    public List<String> selectProfiles(String message, String actualProfileName) throws SQLException {
        List<AbstractPlatform> allPlatformList = platformService.listAllPlatforms();
        List<Profile> allProfileList = profileService.listAllProfiles();
        List<PlatformProfile> platformProfileList = new ArrayList<PlatformProfile>();
        for (Profile profile: allProfileList) {
            for (AbstractPlatform platform: allPlatformList) {
                platformProfileList.add(new PlatformProfile(platform, profile));
            }
        }

        List<String> selectedProfileNameList = new ArrayList<String>();
        selectedProfileNameList.add(actualProfileName);

        List<PlatformProfileToDisplay> selectedProfiles = Utils.selectPlatformProfilesDialog(message + ":", platformProfileList, selectedProfileNameList);
        return selectedProfiles.stream().map(dto -> dto.getProfileName()).collect(Collectors.toList());
    }


    
    public String selectProfile(String message, String actualProfileName) throws SQLException {
        List<AbstractPlatform> allPlatformList = platformService.listAllPlatforms();
        List<Profile> allProfileList = profileService.listAllProfiles();
        List<PlatformProfile> platformProfileList = new ArrayList<PlatformProfile>();
        for (Profile profile: allProfileList) {
            for (AbstractPlatform platform: allPlatformList) {
                platformProfileList.add(new PlatformProfile(platform, profile));
            }
        }
        List<String> selectedProfileNameList = new ArrayList<String>();
        selectedProfileNameList.add(actualProfileName);
        Optional<PlatformProfileToDisplay> selectedProfile = Utils.selectProfileDialog(message + ":", platformProfileList, selectedProfileNameList);
        if (selectedProfile.isPresent()) {
            return selectedProfile.get().getProfileName();
        }
        return null;
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

    
    public boolean updateProfileSetTakeover(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setTakeover(isSelected);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetMapVoting(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setMapVoting(isSelected);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetKickVoting(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setKickVoting(isSelected);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetPublicTextChat(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setPublicTextChat(isSelected);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetSpectatorsChat(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setSpectatorsOnlyChatToOtherSpectators(isSelected);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetVoip(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setVoip(isSelected);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetTeamCollision(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setTeamCollision(isSelected);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetAdminCanPause(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setAdminCanPause(isSelected);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetAnnounceAdminLogin(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setAnnounceAdminLogin(isSelected);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetChatLogging(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setChatLogging(isSelected);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetMapVotingTime(String profileName, Double mapVotingTime) throws Exception {
        if (mapVotingTime < 0) {
            throw new Exception("Error: Invalid value. Minimun: 0");
        }

        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setMapVotingTime(mapVotingTime);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetKickPercentage(String profileName, Double kickPercentage) throws Exception {
        if ((kickPercentage < 0) || (kickPercentage > 1)) {
            throw new Exception("Error: Invalid value. Minimun: 0, maximum: 1.");
        }

        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setKickPercentage(kickPercentage);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetChatLoggingFile(String profileName, String chatLoggingFile) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setChatLoggingFile(chatLoggingFile);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetChatLoggingFileTimestamp(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setChatLoggingFileTimestamp(isSelected);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetTimeBetweenKicks(String profileName, Double timeBetweenKicks) throws Exception {
        if (timeBetweenKicks < 0) {
            throw new Exception("Error: Invalid value. Minimun: 0");
        }

        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setTimeBetweenKicks(timeBetweenKicks);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetMaxIdleTime(String profileName, Double maxIdleTime) throws Exception {
        if ((maxIdleTime < 0) || (maxIdleTime > 300)) {
            throw new Exception("Error: Invalid value. Minimun: 0, maximum: 300.");
        }

        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setMaxIdleTime(maxIdleTime);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetDeadPlayersCanTalk(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setDeadPlayersCanTalk(isSelected);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetReadyUpDelay(String profileName, Integer readyUpDelay) throws Exception {
        if (readyUpDelay < 0) {
            throw new Exception("Error: Invalid value. Minimun: 0.");
        }

        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setReadyUpDelay(readyUpDelay);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetGameStartDelay(String profileName, Integer gameStartDelay) throws Exception {
        if (gameStartDelay < 0) {
            throw new Exception("Error: Invalid value. Minimun: 0.");
        }

        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setGameStartDelay(gameStartDelay);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetMaxSpectators(String profileName, Integer maxSpectators) throws Exception {
        if (maxSpectators < 0) {
            throw new Exception("Error: Invalid value. Minimun: 0.");
        }

        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setMaxSpectators(maxSpectators);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetMapObjetives(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setMapObjetives(isSelected);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetPickupItems(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setPickupItems(isSelected);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public boolean updateProfileSetFriendlyFirePercentage(String profileName, Double friendlyFirePercentage) throws Exception {
        if ((friendlyFirePercentage < 0) || (friendlyFirePercentage > 1)) {
            throw new Exception("Error: Invalid value. Minimun: 0, maximum: 1.");
        }

        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setFriendlyFirePercentage(friendlyFirePercentage);
            return profileService.updateItem(profile);
        }
        return false;
    }

    
    public void runExecutableFile(String platformName) throws SQLException {
        Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(platformName);
        if (!platformOptional.isPresent()) {
            Utils.warningDialog("Run operation aborted!", "The platform can not be found");
            return;
        }
        Kf2Common kf2Common = Kf2Factory.getInstance(platformOptional.get());
        kf2Common.runExecutableFile();
    }

    
    public PlatformDto getPlatform(EnumPlatform enumPlatform) throws SQLException {
        Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(enumPlatform.name());
        return platformOptional.isPresent() ? platformDtoFactory.newSteamDto(platformOptional.get()): null;
    }
}
