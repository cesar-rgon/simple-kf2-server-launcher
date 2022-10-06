package stories.maincontent;

import daos.*;
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
import stories.AbstractFacade;
import utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainContentFacadeImpl extends AbstractFacade implements MainContentFacade {

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

    public MainContentFacadeImpl() {
        super();
        languageDtoFactory = new LanguageDtoFactory();
        profileDtoFactory = new ProfileDtoFactory();
        gameTypeDtoFactory = new GameTypeDtoFactory();
        difficultyDtoFactory = new DifficultyDtoFactory();
        lengthDtoFactory = new LengthDtoFactory();
        maxPlayersDtoFactory = new MaxPlayersDtoFactory();
        profileService = new ProfileServiceImpl();
        platformProfileMapDtoFactory = new PlatformProfileMapDtoFactory();
        platformProfileMapService = new PlatformProfileMapServiceImpl();
        platformDtoFactory = new PlatformDtoFactory();
        platformService = new PlatformServiceImpl();
        officialMapService = new OfficialMapServiceImpl();
        customMapService = new CustomMapModServiceImpl();
        difficultyService = new DifficultyServiceImpl();
        gameTypeService = new GameTypeServiceImpl();
        languageService = new LanguageServiceImpl();
        lengthService = new LengthServiceImpl();
        maxPlayersService = new MaxPlayersServiceImpl();
    }

    @Override
    public ObservableList<ProfileDto> listAllProfiles() throws SQLException {
        List<Profile> profiles = profileService.listAllProfiles();
        return profileDtoFactory.newDtos(profiles);
    }

    @Override
    public ObservableList<SelectDto> listAllLanguages() throws Exception {
        List<Language> languages = languageService.listAll();
        return languageDtoFactory.newDtos(languages);
    }

    @Override
    public ObservableList<GameTypeDto> listAllGameTypes() throws Exception {
        List<GameType> gameTypes = gameTypeService.listAll();
        return gameTypeDtoFactory.newDtos(gameTypes);
    }

    @Override
    public ObservableList<SelectDto> listAllDifficulties() throws Exception {
        List<Difficulty> difficulties = difficultyService.listAll();
        return difficultyDtoFactory.newDtos(difficulties);
    }

    @Override
    public ObservableList<SelectDto> listAllLengths() throws Exception {
        List<Length> lengths = lengthService.listAll();
        return lengthDtoFactory.newDtos(lengths);
    }

    @Override
    public ObservableList<SelectDto> listAllPlayers() throws Exception {
        List<MaxPlayers> playerList = maxPlayersService.listAll();
        return maxPlayersDtoFactory.newDtos(playerList);
    }

    public ObservableList<PlatformDto> listAllPlatforms() throws SQLException {

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

    @Override
    public boolean updateProfileSetGameType(String profileName, String gameTypeCode) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<GameType> gameTypeOpt = gameTypeService.findByCode(gameTypeCode);
            if (gameTypeOpt.isPresent()) {
                profile.setGametype(gameTypeOpt.get());
                return ProfileDao.getInstance().update(profile);
            }
        }
        return false;
    }

    @Override
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
            return ProfileDao.getInstance().update(profileOpt.get());
        }
        return false;
    }

    @Override
    public boolean updateProfileSetDifficulty(String profileName, String difficultyCode) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<Difficulty> difficultyOpt = difficultyService.findByCode(difficultyCode);
            if (difficultyOpt.isPresent()) {
                profile.setDifficulty(difficultyOpt.get());
                return ProfileDao.getInstance().update(profile);
            }
        }
        return false;
    }

    @Override
    public boolean updateProfileSetLength(String profileName, String lengthCode) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<Length> lengthOpt = lengthService.findByCode(lengthCode);
            if (lengthOpt.isPresent()) {
                profile.setLength(lengthOpt.get());
                return ProfileDao.getInstance().update(profile);
            }
        }
        return false;
    }

    @Override
    public boolean updateProfileSetMaxPlayers(String profileName, String maxPlayersCode) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<MaxPlayers> maxPlayersOpt = maxPlayersService.findByCode(maxPlayersCode);
            if (maxPlayersOpt.isPresent()) {
                profile.setMaxPlayers(maxPlayersOpt.get());
                return ProfileDao.getInstance().update(profile);
            }
        }
        return false;
    }

    @Override
    public boolean updateProfileSetLanguage(String profileName, String languageCode) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<Language> languageOpt = languageService.findByCode(languageCode);
            if (languageOpt.isPresent()) {
                profile.setLanguage(languageOpt.get());
                return ProfileDao.getInstance().update(profile);
            }
        }
        return false;
    }

    @Override
    public boolean updateProfileSetServerName(String profileName, String serverName) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setServerName(serverName);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetServerPassword(String profileName, String serverPassword) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setServerPassword(Utils.encryptAES(serverPassword));
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetWebPassword(String profileName, String webPassword) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setWebPassword(Utils.encryptAES(webPassword));
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetWebPort(String profileName, Integer webPort) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setWebPort(webPort);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetGamePort(String profileName, Integer gamePort) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setGamePort(gamePort);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetQueryPort(String profileName, Integer queryPort) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setQueryPort(queryPort);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetYourClan(String profileName, String yourClan) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setYourClan(yourClan);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetYourWebLink(String profileName, String yourWebLink) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setYourWebLink(yourWebLink);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetUrlImageServer(String profileName, String urlImageServer) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setUrlImageServer(urlImageServer);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetWelcomeMessage(String profileName, String welcomeMessage) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setWelcomeMessage(welcomeMessage);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetCustomParameters(String profileName, String customParameters) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setCustomParameters(customParameters);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetWebPage(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setWebPage(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
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

    @Override
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

    @Override
    public ProfileDto getLastSelectedProfile() throws Exception {
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

    @Override
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


    @Override
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

    @Override
    public boolean isCorrectInstallationFolder(String platformName) throws SQLException {
        Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(platformName);
        if (!platformOptional.isPresent()) {
            Utils.warningDialog("Operation aborted!", "The platform can not be found");
            return false;
        }
        Kf2Common kf2Common = Kf2Factory.getInstance(platformOptional.get());
        return kf2Common.isValidInstallationFolder();
    }

    @Override
    public boolean updateProfileSetTakeover(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setTakeover(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetMapVoting(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setMapVoting(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetKickVoting(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setKickVoting(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetPublicTextChat(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setPublicTextChat(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetSpectatorsChat(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setSpectatorsOnlyChatToOtherSpectators(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetVoip(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setVoip(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetTeamCollision(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setTeamCollision(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetAdminCanPause(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setAdminCanPause(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetAnnounceAdminLogin(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setAnnounceAdminLogin(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetChatLogging(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setChatLogging(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetMapVotingTime(String profileName, Double mapVotingTime) throws Exception {
        if (mapVotingTime < 0) {
            throw new Exception("Error: Invalid value. Minimun: 0");
        }

        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setMapVotingTime(mapVotingTime);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetKickPercentage(String profileName, Double kickPercentage) throws Exception {
        if ((kickPercentage < 0) || (kickPercentage > 1)) {
            throw new Exception("Error: Invalid value. Minimun: 0, maximum: 1.");
        }

        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setKickPercentage(kickPercentage);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetChatLoggingFile(String profileName, String chatLoggingFile) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setChatLoggingFile(chatLoggingFile);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetChatLoggingFileTimestamp(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setChatLoggingFileTimestamp(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetTimeBetweenKicks(String profileName, Double timeBetweenKicks) throws Exception {
        if (timeBetweenKicks < 0) {
            throw new Exception("Error: Invalid value. Minimun: 0");
        }

        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setTimeBetweenKicks(timeBetweenKicks);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetMaxIdleTime(String profileName, Double maxIdleTime) throws Exception {
        if ((maxIdleTime < 0) || (maxIdleTime > 300)) {
            throw new Exception("Error: Invalid value. Minimun: 0, maximum: 300.");
        }

        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setMaxIdleTime(maxIdleTime);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetDeadPlayersCanTalk(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setDeadPlayersCanTalk(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetReadyUpDelay(String profileName, Integer readyUpDelay) throws Exception {
        if (readyUpDelay < 0) {
            throw new Exception("Error: Invalid value. Minimun: 0.");
        }

        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setReadyUpDelay(readyUpDelay);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetGameStartDelay(String profileName, Integer gameStartDelay) throws Exception {
        if (gameStartDelay < 0) {
            throw new Exception("Error: Invalid value. Minimun: 0.");
        }

        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setGameStartDelay(gameStartDelay);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetMaxSpectators(String profileName, Integer maxSpectators) throws Exception {
        if (maxSpectators < 0) {
            throw new Exception("Error: Invalid value. Minimun: 0.");
        }

        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setMaxSpectators(maxSpectators);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetMapObjetives(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setMapObjetives(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetPickupItems(String profileName, boolean isSelected) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setPickupItems(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetFriendlyFirePercentage(String profileName, Double friendlyFirePercentage) throws Exception {
        if ((friendlyFirePercentage < 0) || (friendlyFirePercentage > 1)) {
            throw new Exception("Error: Invalid value. Minimun: 0, maximum: 1.");
        }

        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setFriendlyFirePercentage(friendlyFirePercentage);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public List<PlatformProfileMapDto> listPlatformProfileMaps(String platformName, String profileName) throws Exception {
        Optional<AbstractPlatform> platformOpt = platformService.findPlatformByName(platformName);
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (platformOpt.isPresent() && profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            List<PlatformProfileMap> platformProfileMapList = platformProfileMapService.listPlatformProfileMaps(platformOpt.get(), profile);
            return platformProfileMapDtoFactory.newDtos(platformProfileMapList);
        }
        return new ArrayList<PlatformProfileMapDto>();
    }

    @Override
    public void runExecutableFile(String platformName) throws SQLException {
        Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(platformName);
        if (!platformOptional.isPresent()) {
            Utils.warningDialog("Run operation aborted!", "The platform can not be found");
            return;
        }
        Kf2Common kf2Common = Kf2Factory.getInstance(platformOptional.get());
        kf2Common.runExecutableFile();
    }

    @Override
    public PlatformDto getPlatform(EnumPlatform enumPlatform) throws SQLException {
        Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(enumPlatform.name());
        return platformOptional.isPresent() ? platformDtoFactory.newSteamDto(platformOptional.get()): null;
    }
}
