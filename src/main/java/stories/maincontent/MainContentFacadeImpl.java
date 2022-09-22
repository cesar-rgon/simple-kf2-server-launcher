package stories.maincontent;

import daos.*;
import dtos.*;
import dtos.factories.*;
import entities.*;
import javafx.collections.ObservableList;
import pojos.PlatformProfileToDisplay;
import pojos.PlatformProfileToDisplayFactory;
import pojos.enums.EnumPlatform;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import pojos.kf2factory.Kf2Steam;
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
    }

    @Override
    public ObservableList<ProfileDto> listAllProfiles() throws SQLException {
        List<Profile> profiles = profileService.listAllProfiles();
        return profileDtoFactory.newDtos(profiles);
    }

    @Override
    public ObservableList<SelectDto> listAllLanguages() throws SQLException {
        List<Language> languages = LanguageDao.getInstance().listAll();
        return languageDtoFactory.newDtos(languages);
    }

    @Override
    public ObservableList<GameTypeDto> listAllGameTypes() throws SQLException {
        List<GameType> gameTypes = GameTypeDao.getInstance().listAll();
        return gameTypeDtoFactory.newDtos(gameTypes);
    }

    @Override
    public ObservableList<SelectDto> listAllDifficulties() throws SQLException {
        List<Difficulty> difficulties = DifficultyDao.getInstance().listAll();
        return difficultyDtoFactory.newDtos(difficulties);
    }

    @Override
    public ObservableList<SelectDto> listAllLengths() throws SQLException {
        List<Length> lengths = LengthDao.getInstance().listAll();
        return lengthDtoFactory.newDtos(lengths);
    }

    @Override
    public ObservableList<SelectDto> listAllPlayers() throws SQLException {
        List<MaxPlayers> playerList = MaxPlayersDao.getInstance().listAll();
        return maxPlayersDtoFactory.newDtos(playerList);
    }

    public ObservableList<PlatformDto> listAllPlatforms() throws SQLException {
        List<AbstractPlatform> platformList = AbstractPlatformDao.getInstance().listAll();
        return platformDtoFactory.newDtos(platformList);
    }

    @Override
    public boolean updateProfileSetGameType(String profileName, String gameTypeCode) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<GameType> gameTypeOpt = GameTypeDao.getInstance().findByCode(gameTypeCode);
            if (gameTypeOpt.isPresent()) {
                profile.setGametype(gameTypeOpt.get());
                return ProfileDao.getInstance().update(profile);
            }
        }
        return false;
    }

    @Override
    public boolean updateProfileSetMap(String profileName, String mapCode, boolean isOfficial) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        Optional mapOptionaL;
        if (isOfficial) {
            mapOptionaL = OfficialMapDao.getInstance().findByCode(mapCode);
        } else {
            mapOptionaL = CustomMapModDao.getInstance().findByCode(mapCode);
        }

        if (profileOpt.isPresent() && mapOptionaL.isPresent()) {
            profileOpt.get().setMap((AbstractMap) mapOptionaL.get());
            return ProfileDao.getInstance().update(profileOpt.get());
        }
        return false;
    }

    @Override
    public boolean updateProfileSetDifficulty(String profileName, String difficultyCode) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<Difficulty> difficultyOpt = DifficultyDao.getInstance().findByCode(difficultyCode);
            if (difficultyOpt.isPresent()) {
                profile.setDifficulty(difficultyOpt.get());
                return ProfileDao.getInstance().update(profile);
            }
        }
        return false;
    }

    @Override
    public boolean updateProfileSetLength(String profileName, String lengthCode) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<Length> lengthOpt = LengthDao.getInstance().findByCode(lengthCode);
            if (lengthOpt.isPresent()) {
                profile.setLength(lengthOpt.get());
                return ProfileDao.getInstance().update(profile);
            }
        }
        return false;
    }

    @Override
    public boolean updateProfileSetMaxPlayers(String profileName, String maxPlayersCode) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<MaxPlayers> maxPlayersOpt = MaxPlayersDao.getInstance().findByCode(maxPlayersCode);
            if (maxPlayersOpt.isPresent()) {
                profile.setMaxPlayers(maxPlayersOpt.get());
                return ProfileDao.getInstance().update(profile);
            }
        }
        return false;
    }

    @Override
    public boolean updateProfileSetLanguage(String profileName, String languageCode) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<Language> languageOpt = LanguageDao.getInstance().findByCode(languageCode);
            if (languageOpt.isPresent()) {
                profile.setLanguage(languageOpt.get());
                return ProfileDao.getInstance().update(profile);
            }
        }
        return false;
    }

    @Override
    public boolean updateProfileSetServerName(String profileName, String serverName) throws SQLException {
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
    public boolean updateProfileSetWebPort(String profileName, Integer webPort) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setWebPort(webPort);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetGamePort(String profileName, Integer gamePort) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setGamePort(gamePort);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetQueryPort(String profileName, Integer queryPort) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setQueryPort(queryPort);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetYourClan(String profileName, String yourClan) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setYourClan(yourClan);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetYourWebLink(String profileName, String yourWebLink) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setYourWebLink(yourWebLink);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetUrlImageServer(String profileName, String urlImageServer) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setUrlImageServer(urlImageServer);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetWelcomeMessage(String profileName, String welcomeMessage) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setWelcomeMessage(welcomeMessage);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetCustomParameters(String profileName, String customParameters) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setCustomParameters(customParameters);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetWebPage(String profileName, boolean isSelected) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setWebPage(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public String runServer(PlatformDto platform, String profileName) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        Kf2Common kf2Common = Kf2Factory.getInstance(platform.getKey());
        return kf2Common.runServer(profileOpt.isPresent()? profileOpt.get(): null);
    }

    @Override
    public String joinServer(PlatformDto platform, String profileName) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Kf2Common kf2Common = Kf2Factory.getInstance(platform.getKey());
            return ((Kf2Steam)kf2Common).joinServer(profileOpt.get());
        } else {
            Utils.warningDialog("Join operation aborted!", "The profile name can not be empty");
        }
        return "";
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
        PlatformProfileToDisplayFactory platformProfileToDisplayFactory = new PlatformProfileToDisplayFactory();
        List<PlatformProfileMap> platformProfileMapList = PlatformProfileMapDao.getInstance().listPlatformProfileMaps();
        List<PlatformProfileToDisplay> allProfilesToDisplay = platformProfileToDisplayFactory.newOnes(platformProfileMapList);

        Optional<PlatformProfileToDisplay> actualProfile = allProfilesToDisplay.stream().filter(p -> p.getProfileName().equalsIgnoreCase(actualProfileName)).findFirst();
        if (actualProfile.isPresent()) {
            actualProfile.get().setSelected(true);
        }

        List<PlatformProfileToDisplay> selectedProfiles = Utils.selectPlatformProfilesDialog(message + ":", allProfilesToDisplay);
        return selectedProfiles.stream().map(dto -> dto.getProfileName()).collect(Collectors.toList());
    }


    @Override
    public String selectProfile(String message, String actualProfileName) throws SQLException {
        PlatformProfileToDisplayFactory platformProfileToDisplayFactory = new PlatformProfileToDisplayFactory();
        List<PlatformProfileMap> platformProfileMapList = PlatformProfileMapDao.getInstance().listPlatformProfileMaps();
        List<PlatformProfileToDisplay> allProfilesToDisplay = platformProfileToDisplayFactory.newOnes(platformProfileMapList);

        Optional<PlatformProfileToDisplay> actualProfile = allProfilesToDisplay.stream().filter(p -> p.getProfileName().equalsIgnoreCase(actualProfileName)).findFirst();
        if (actualProfile.isPresent()) {
            actualProfile.get().setSelected(true);
        }

        Optional<PlatformProfileToDisplay> selectedProfile = Utils.selectProfileDialog(message + ":", allProfilesToDisplay);
        if (selectedProfile.isPresent()) {
            return selectedProfile.get().getProfileName();
        }
        return null;
    }

    @Override
    public boolean isCorrectInstallationFolder(PlatformDto platform) {
        Kf2Common kf2Common = Kf2Factory.getInstance(platform.getKey());
        return kf2Common.isValid(platform.getInstallationFolder());
    }

    @Override
    public boolean updateProfileSetTakeover(String profileName, boolean isSelected) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setTakeover(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetMapVoting(String profileName, boolean isSelected) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setMapVoting(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetKickVoting(String profileName, boolean isSelected) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setKickVoting(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetPublicTextChat(String profileName, boolean isSelected) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setPublicTextChat(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetSpectatorsChat(String profileName, boolean isSelected) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setSpectatorsOnlyChatToOtherSpectators(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetVoip(String profileName, boolean isSelected) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setVoip(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetTeamCollision(String profileName, boolean isSelected) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setTeamCollision(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetAdminCanPause(String profileName, boolean isSelected) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setAdminCanPause(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetAnnounceAdminLogin(String profileName, boolean isSelected) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setAnnounceAdminLogin(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetChatLogging(String profileName, boolean isSelected) throws SQLException {
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
    public boolean updateProfileSetChatLoggingFile(String profileName, String chatLoggingFile) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setChatLoggingFile(chatLoggingFile);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetChatLoggingFileTimestamp(String profileName, boolean isSelected) throws SQLException {
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
    public boolean updateProfileSetDeadPlayersCanTalk(String profileName, boolean isSelected) throws SQLException {
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
    public boolean updateProfileSetMapObjetives(String profileName, boolean isSelected) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setMapObjetives(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetPickupItems(String profileName, boolean isSelected) throws SQLException {
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
    public List<PlatformProfileMapDto> listPlatformProfileMaps(String platformName, String profileName) throws SQLException {
        Optional<AbstractPlatform> platformOpt = AbstractPlatformDao.getInstance().findByCode(platformName);
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (platformOpt.isPresent() && profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            List<PlatformProfileMap> platformProfileMapList = platformProfileMapService.listPlatformProfileMaps(platformOpt.get(), profile);
            return platformProfileMapDtoFactory.newDtos(platformProfileMapList);
        }
        return new ArrayList<PlatformProfileMapDto>();
    }

    @Override
    public void runExecutableFile(PlatformDto platform) {
        Kf2Common kf2Common = Kf2Factory.getInstance(platform.getKey());
        kf2Common.runExecutableFile();
    }

    @Override
    public PlatformDto getPlatform(EnumPlatform enumPlatform) throws SQLException {
        Optional<AbstractPlatform> platformOptional = AbstractPlatformDao.getInstance().findByCode(enumPlatform.name());
        return platformOptional.isPresent() ? platformDtoFactory.newSteamDto(platformOptional.get()): null;
    }
}
