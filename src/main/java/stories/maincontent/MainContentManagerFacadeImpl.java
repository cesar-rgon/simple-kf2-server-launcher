package stories.maincontent;

import dtos.*;
import framework.AbstractManagerFacade;
import framework.EmptyModelContext;
import javafx.collections.ObservableList;
import pojos.enums.EnumPlatform;
import stories.listvaluesmaincontent.ListValuesMainContentTransactionalFacade;
import stories.listvaluesmaincontent.ListValuesMainContentTransactionalFacadeImpl;
import stories.listvaluesmaincontent.ListValuesMainContentFacadeResult;

import java.sql.SQLException;
import java.util.List;

public class MainContentManagerFacadeImpl
        extends AbstractManagerFacade<EmptyModelContext, ListValuesMainContentFacadeResult>
        implements MainContentManagerFacade {

    public MainContentManagerFacadeImpl() {
        super(new EmptyModelContext(), ListValuesMainContentFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    public ListValuesMainContentFacadeResult internalExecute(EmptyModelContext modelContext) throws Exception {
        ListValuesMainContentTransactionalFacade listValuesMainContentTransactionalFacade = new ListValuesMainContentTransactionalFacadeImpl();
        return listValuesMainContentTransactionalFacade.execute();
    }

    @Override
    public ObservableList<ProfileDto> listAllProfiles() throws Exception {
        return null;
    }

    @Override
    public boolean updateProfileSetGameType(String profileName, String gameTypeCode) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetMap(String profileName, String mapCode, boolean isOfficial) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetDifficulty(String profileName, String difficultyCode) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetLength(String profileName, String lengthCode) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetMaxPlayers(String profileName, String maxPlayersCode) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetLanguage(String profileName, String languageCode) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetServerName(String profileName, String serverName) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetServerPassword(String profileName, String serverPassword) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetWebPassword(String profileName, String webPassword) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetWebPort(String profileName, Integer webPort) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetGamePort(String profileName, Integer gamePort) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetQueryPort(String profileName, Integer queryPort) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetYourClan(String profileName, String yourClan) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetYourWebLink(String profileName, String yourWebLink) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetUrlImageServer(String profileName, String urlImageServer) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetWelcomeMessage(String profileName, String welcomeMessage) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetCustomParameters(String profileName, String customParameters) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetWebPage(String profileName, boolean isSelected) throws Exception {
        return false;
    }

    @Override
    public ProfileDto findProfileDtoByName(String name) throws Exception {
        return null;
    }

    @Override
    public String runServer(String platformName, String profileName) throws Exception {
        return null;
    }

    @Override
    public String joinServer(String platformName, String profileName) throws Exception {
        return null;
    }

    @Override
    public List<String> selectProfiles(String message, String actualProfileName) throws SQLException {
        return null;
    }

    @Override
    public String selectProfile(String message, String actualProfileName) throws SQLException {
        return null;
    }

    @Override
    public boolean isCorrectInstallationFolder(String platformName) throws SQLException {
        return false;
    }

    @Override
    public boolean updateProfileSetTakeover(String profileName, boolean isSelected) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetMapVoting(String profileName, boolean isSelected) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetKickVoting(String profileName, boolean isSelected) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetPublicTextChat(String profileName, boolean isSelected) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetSpectatorsChat(String profileName, boolean isSelected) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetVoip(String profileName, boolean isSelected) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetTeamCollision(String profileName, boolean isSelected) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetAdminCanPause(String profileName, boolean isSelected) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetAnnounceAdminLogin(String profileName, boolean isSelected) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetChatLogging(String profileName, boolean isSelected) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetMapVotingTime(String profileName, Double mapVotingTime) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetKickPercentage(String profileName, Double kickPercentage) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetChatLoggingFile(String profileName, String chatLoggingFile) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetChatLoggingFileTimestamp(String profileName, boolean isSelected) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetTimeBetweenKicks(String profileName, Double timeBetweenKicks) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetMaxIdleTime(String profileName, Double maxIdleTime) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetDeadPlayersCanTalk(String profileName, boolean isSelected) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetReadyUpDelay(String profileName, Integer readyUpDelay) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetGameStartDelay(String profileName, Integer gameStartDelay) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetMaxSpectators(String profileName, Integer maxSpectators) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetMapObjetives(String profileName, boolean isSelected) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetPickupItems(String profileName, boolean isSelected) throws Exception {
        return false;
    }

    @Override
    public boolean updateProfileSetFriendlyFirePercentage(String profileName, Double friendlyFirePercentage) throws Exception {
        return false;
    }

    @Override
    public List<PlatformProfileMapDto> listPlatformProfileMaps(String platformName, String profileName) throws Exception {
        return null;
    }

    @Override
    public void runExecutableFile(String platformName) throws SQLException {

    }

    @Override
    public PlatformDto getPlatform(EnumPlatform enumPlatform) throws SQLException {
        return null;
    }
}
