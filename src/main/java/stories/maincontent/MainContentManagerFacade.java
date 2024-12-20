package stories.maincontent;

import dtos.ProfileDto;
import stories.listvaluesmaincontent.ListValuesMainContentFacadeResult;
import stories.loadactualprofile.LoadActualProfileFacadeResult;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public interface MainContentManagerFacade {

    ListValuesMainContentFacadeResult execute() throws Exception;
    LoadActualProfileFacadeResult loadActualProfile(String platformName, String profileName) throws Exception;
    void updateProfileSetGameType(String profileName, String gameTypeCode) throws Exception;
    void updateProfileSetMap(String profileName, String mapCode, boolean isOfficial) throws Exception;
    void updateProfileSetDifficulty(String profileName, String difficultyCode) throws Exception;
    void updateProfileSetLength(String profileName, String lengthCode) throws Exception;
    void updateProfileSetMaxPlayers(String profileName, String maxPlayersCode) throws Exception;
    void updateProfileSetLanguage(String profileName, String languageCode) throws Exception;
    void updateProfileSetServerName(String profileName, String serverName) throws Exception;
    void updateProfileSetServerPassword(String profileName, String serverPassword) throws Exception;
    void updateProfileSetWebPassword(String profileName, String webPassword) throws Exception;
    void updateProfileSetWebPort(String profileName, Integer webPort) throws Exception;
    void updateProfileSetGamePort(String profileName, Integer gamePort) throws Exception;
    void updateProfileSetQueryPort(String profileName, Integer queryPort) throws Exception;
    void updateProfileSetYourClan(String profileName, String yourClan) throws Exception;
    void updateProfileSetYourWebLink(String profileName, String yourWebLink) throws Exception;
    void updateProfileSetUrlImageServer(String profileName, String urlImageServer) throws Exception;
    void updateProfileSetWelcomeMessage(String profileName, String welcomeMessage) throws Exception;
    void updateProfileSetCustomParameters(String profileName, String customParameters) throws Exception;
    void updateProfileSetWebPage(String profileName, boolean isSelected) throws Exception;
    void updateProfileSetTakeover(String profileName, boolean isSelected) throws Exception;
    void updateProfileSetMapVoting(String profileName, boolean isSelected) throws Exception;
    void updateProfileSetKickVoting(String profileName, boolean isSelected) throws Exception;
    void updateProfileSetPublicTextChat(String profileName, boolean isSelected) throws Exception;
    void updateProfileSetSpectatorsChat(String profileName, boolean isSelected) throws Exception;
    void updateProfileSetVoip(String profileName, boolean isSelected) throws Exception;
    void updateProfileSetTeamCollision(String profileName, boolean isSelected) throws Exception;
    void updateProfileSetAdminCanPause(String profileName, boolean isSelected) throws Exception;
    void updateProfileSetAnnounceAdminLogin(String profileName, boolean isSelected) throws Exception;
    void updateProfileSetChatLogging(String profileName, boolean isSelected) throws Exception;
    void updateProfileSetMapVotingTime(String profileName, Double mapVotingTime) throws Exception;
    void updateProfileSetKickPercentage(String profileName, Double kickPercentage) throws Exception;
    void updateProfileSetChatLoggingFile(String profileName, String chatLoggingFile) throws Exception;
    void updateProfileSetChatLoggingFileTimestamp(String profileName, boolean isSelected) throws Exception;
    void updateProfileSetTimeBetweenKicks(String profileName, Double timeBetweenKicks) throws Exception;
    void updateProfileSetMaxIdleTime(String profileName, Double maxIdleTime) throws Exception;
    void updateProfileSetDeadPlayersCanTalk(String profileName, boolean isSelected) throws Exception;
    void updateProfileSetReadyUpDelay(String profileName, Integer readyUpDelay) throws Exception;
    void updateProfileSetGameStartDelay(String profileName, Integer gameStartDelay) throws Exception;
    void updateProfileSetMaxSpectators(String profileName, Integer maxSpectators) throws Exception;
    void updateProfileSetMapObjetives(String profileName, boolean isSelected) throws Exception;
    void updateProfileSetPickupItems(String profileName, boolean isSelected) throws Exception;
    void updateProfileSetFriendlyFirePercentage(String profileName, Double friendlyFirePercentage) throws Exception;
    void runServers(String platformName, String actualSelectedProfileName, String actualSelectedLanguage) throws Exception;
    void joinServer(String platformName, String actualSelectedProfileName, String actualSelectedLanguage) throws Exception;
    boolean isCorrectInstallationFolder(String platformName) throws Exception;
    ProfileDto findProfileDtoByName(String name) throws Exception;
    String findPropertyValue(String propertyFilePath, String key) throws Exception;
    String findPropertyValue(File propertyFile, String key) throws Exception;
    void removeProperty(String propertyFilePath, String propKey) throws Exception;
    void updateProfileSetNetTickrate(String profileName, Integer netTickrate) throws Exception;
    void updateProfileSetLanTickrate(String profileName, Integer lanTickrate) throws Exception;
    void updateProfileSetLanMaxClientRate(String profileName, Integer lanMaxClientRate) throws Exception;
    void updateProfileSetInternetMaxClientRate(String profileName, Integer internetMaxClientRate) throws Exception;

}
