package stories.maincontent;

import dtos.GameTypeDto;
import dtos.ProfileDto;
import dtos.ProfileMapDto;
import dtos.SelectDto;
import pojos.enums.EnumPlatform;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public interface MainContentFacade {
    ObservableList<ProfileDto> listAllProfiles() throws SQLException;
    ObservableList<SelectDto> listAllLanguages() throws SQLException;
    ObservableList<GameTypeDto> listAllGameTypes() throws SQLException;
    ObservableList<SelectDto> listAllDifficulties() throws SQLException;
    ObservableList<SelectDto> listAllLengths() throws SQLException;
    ObservableList<SelectDto> listAllPlayers() throws SQLException;
    ObservableList<EnumPlatform> listAllPlatforms() throws SQLException;

    boolean updateProfileSetGameType(String profileName, String gameTypeCode) throws SQLException;
    boolean updateProfileSetMap(String profileName, String mapCode) throws SQLException;
    boolean updateProfileSetDifficulty(String profileName, String difficultyCode) throws SQLException;
    boolean updateProfileSetLength(String profileName, String lengthCode) throws SQLException;
    boolean updateProfileSetMaxPlayers(String profileName, String maxPlayersCode) throws SQLException;
    boolean updateProfileSetLanguage(String profileName, String languageCode) throws SQLException;
    boolean updateProfileSetServerName(String profileName, String serverName) throws SQLException;
    boolean updateProfileSetServerPassword(String profileName, String serverPassword) throws Exception;
    boolean updateProfileSetWebPassword(String profileName, String webPassword) throws Exception;
    boolean updateProfileSetWebPort(String profileName, Integer webPort) throws SQLException;
    boolean updateProfileSetGamePort(String profileName, Integer gamePort) throws SQLException;
    boolean updateProfileSetQueryPort(String profileName, Integer queryPort) throws SQLException;
    boolean updateProfileSetYourClan(String profileName, String yourClan) throws SQLException;
    boolean updateProfileSetYourWebLink(String profileName, String yourWebLink) throws SQLException;
    boolean updateProfileSetUrlImageServer(String profileName, String urlImageServer) throws SQLException;
    boolean updateProfileSetWelcomeMessage(String profileName, String welcomeMessage) throws SQLException;
    boolean updateProfileSetCustomParameters(String profileName, String customParameters) throws SQLException;
    boolean updateProfileSetWebPage(String profileName, boolean isSelected) throws SQLException;
    ProfileDto findProfileDtoByName(String name) throws SQLException;
    String runServer(String profileName) throws SQLException;
    String joinServer(String profileName) throws SQLException;
    ProfileDto getLastSelectedProfile() throws Exception;
    List<String> selectProfiles(String message, String actualProfileName) throws SQLException;
    String selectProfile(String message, String actualProfileName) throws SQLException;
    boolean isCorrectInstallationFolder(String installationFolder);
    boolean updateProfileSetTakeover(String profileName, boolean isSelected) throws SQLException;
    boolean updateProfileSetMapVoting(String profileName, boolean isSelected) throws SQLException;
    boolean updateProfileSetKickVoting(String profileName, boolean isSelected) throws SQLException;
    boolean updateProfileSetPublicTextChat(String profileName, boolean isSelected) throws SQLException;
    boolean updateProfileSetSpectatorsChat(String profileName, boolean isSelected) throws SQLException;
    boolean updateProfileSetVoip(String profileName, boolean isSelected) throws SQLException;
    boolean updateProfileSetTeamCollision(String profileName, boolean isSelected) throws SQLException;
    boolean updateProfileSetAdminCanPause(String profileName, boolean isSelected) throws SQLException;
    boolean updateProfileSetAnnounceAdminLogin(String profileName, boolean isSelected) throws SQLException;
    boolean updateProfileSetChatLogging(String profileName, boolean isSelected) throws SQLException;
    boolean updateProfileSetMapVotingTime(String profileName, Double mapVotingTime) throws Exception;
    boolean updateProfileSetKickPercentage(String profileName, Double kickPercentage) throws Exception;
    boolean updateProfileSetChatLoggingFile(String profileName, String chatLoggingFile) throws SQLException;
    boolean updateProfileSetChatLoggingFileTimestamp(String profileName, boolean isSelected) throws SQLException;
    boolean updateProfileSetTimeBetweenKicks(String profileName, Double timeBetweenKicks) throws Exception;
    boolean updateProfileSetMaxIdleTime(String profileName, Double maxIdleTime) throws Exception;
    boolean updateProfileSetDeadPlayersCanTalk(String profileName, boolean isSelected) throws SQLException;
    boolean updateProfileSetReadyUpDelay(String profileName, Integer readyUpDelay) throws Exception;
    boolean updateProfileSetGameStartDelay(String profileName, Integer gameStartDelay) throws Exception;
    boolean updateProfileSetMaxSpectators(String profileName, Integer maxSpectators) throws Exception;
    boolean updateProfileSetMapObjetives(String profileName, boolean isSelected) throws SQLException;
    boolean updateProfileSetPickupItems(String profileName, boolean isSelected) throws SQLException;
    boolean updateProfileSetFriendlyFirePercentage(String profileName, Double friendlyFirePercentage) throws Exception;
    boolean updateProfileSetPlatform(String profileName, EnumPlatform platform) throws SQLException;
    List<ProfileMapDto> listProfileMaps(String profileName) throws SQLException;
    void runExecutableFile();

}
