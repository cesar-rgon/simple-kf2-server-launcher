package stories.maincontent;

import dtos.*;
import javafx.collections.ObservableList;
import pojos.enums.EnumPlatform;
import stories.listvaluesmaincontent.ListValuesMainContentFacadeResult;
import stories.loadactualprofile.LoadActualProfileFacadeResult;

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
    ProfileDto findProfileDtoByName(String name) throws Exception;
    String runServer(String platformName, String profileName) throws Exception;
    String joinServer(String platformName, String profileName) throws Exception;
    List<String> selectProfiles(String message, String actualProfileName) throws SQLException;
    String selectProfile(String message, String actualProfileName) throws SQLException;
    boolean isCorrectInstallationFolder(String platformName) throws SQLException;
    void updateProfileSetTakeover(String profileName, boolean isSelected) throws Exception;
    void updateProfileSetMapVoting(String profileName, boolean isSelected) throws Exception;
    void updateProfileSetKickVoting(String profileName, boolean isSelected) throws Exception;
    void updateProfileSetPublicTextChat(String profileName, boolean isSelected) throws Exception;
    void updateProfileSetSpectatorsChat(String profileName, boolean isSelected) throws Exception;
    void updateProfileSetVoip(String profileName, boolean isSelected) throws Exception;
    boolean updateProfileSetTeamCollision(String profileName, boolean isSelected) throws Exception;
    boolean updateProfileSetAdminCanPause(String profileName, boolean isSelected) throws Exception;
    boolean updateProfileSetAnnounceAdminLogin(String profileName, boolean isSelected) throws Exception;
    boolean updateProfileSetChatLogging(String profileName, boolean isSelected) throws Exception;
    boolean updateProfileSetMapVotingTime(String profileName, Double mapVotingTime) throws Exception;
    boolean updateProfileSetKickPercentage(String profileName, Double kickPercentage) throws Exception;
    boolean updateProfileSetChatLoggingFile(String profileName, String chatLoggingFile) throws Exception;
    boolean updateProfileSetChatLoggingFileTimestamp(String profileName, boolean isSelected) throws Exception;
    boolean updateProfileSetTimeBetweenKicks(String profileName, Double timeBetweenKicks) throws Exception;
    boolean updateProfileSetMaxIdleTime(String profileName, Double maxIdleTime) throws Exception;
    boolean updateProfileSetDeadPlayersCanTalk(String profileName, boolean isSelected) throws Exception;
    boolean updateProfileSetReadyUpDelay(String profileName, Integer readyUpDelay) throws Exception;
    boolean updateProfileSetGameStartDelay(String profileName, Integer gameStartDelay) throws Exception;
    boolean updateProfileSetMaxSpectators(String profileName, Integer maxSpectators) throws Exception;
    boolean updateProfileSetMapObjetives(String profileName, boolean isSelected) throws Exception;
    boolean updateProfileSetPickupItems(String profileName, boolean isSelected) throws Exception;
    boolean updateProfileSetFriendlyFirePercentage(String profileName, Double friendlyFirePercentage) throws Exception;
    List<PlatformProfileMapDto> listPlatformProfileMaps(String platformName, String profileName) throws Exception;
    void runExecutableFile(String platformName) throws SQLException;
    PlatformDto getPlatform(EnumPlatform enumPlatform) throws SQLException;
    ObservableList<ProfileDto> listAllProfiles() throws Exception;

}
