package stories.maincontent;

import dtos.PlatformDto;
import dtos.PlatformProfileMapDto;
import dtos.ProfileDto;
import framework.AbstractManagerFacade;
import framework.EmptyModelContext;
import javafx.collections.ObservableList;
import pojos.enums.EnumPlatform;
import services.PropertyService;
import services.PropertyServiceImpl;
import stories.listvaluesmaincontent.ListValuesMainContentFacade;
import stories.listvaluesmaincontent.ListValuesMainContentFacadeImpl;
import stories.listvaluesmaincontent.ListValuesMainContentFacadeResult;
import stories.loadactualprofile.LoadActualProfileFacade;
import stories.loadactualprofile.LoadActualProfileFacadeImpl;
import stories.loadactualprofile.LoadActualProfileFacadeResult;
import stories.loadactualprofile.LoadActualProfileModelContext;
import stories.updateprofilesetdifficulty.UpdateProfileSetDifficultyFacade;
import stories.updateprofilesetdifficulty.UpdateProfileSetDifficultyFacadeImpl;
import stories.updateprofilesetdifficulty.UpdateProfileSetDifficultyModelContext;
import stories.updateprofilesetgameport.UpdateProfileSetGamePortFacade;
import stories.updateprofilesetgameport.UpdateProfileSetGamePortFacadeImpl;
import stories.updateprofilesetgameport.UpdateProfileSetGamePortModelContext;
import stories.updateprofilesetgametype.UpdateProfileSetGameTypeFacade;
import stories.updateprofilesetgametype.UpdateProfileSetGameTypeFacadeImpl;
import stories.updateprofilesetgametype.UpdateProfileSetGameTypeModelContext;
import stories.updateprofilesetlanguage.UpdateProfileSetLanguageFacade;
import stories.updateprofilesetlanguage.UpdateProfileSetLanguageFacadeImpl;
import stories.updateprofilesetlanguage.UpdateProfileSetLanguageModelContext;
import stories.updateprofilesetlength.UpdateProfileSetLengthFacade;
import stories.updateprofilesetlength.UpdateProfileSetLengthFacadeImpl;
import stories.updateprofilesetlength.UpdateProfileSetLengthModelContext;
import stories.updateprofilesetmap.UpdateProfileSetMapFacade;
import stories.updateprofilesetmap.UpdateProfileSetMapFacadeImpl;
import stories.updateprofilesetmap.UpdateProfileSetMapModelContext;
import stories.updateprofilesetmaxplayers.UpdateProfileSetMaxPlayersFacade;
import stories.updateprofilesetmaxplayers.UpdateProfileSetMaxPlayersFacadeImpl;
import stories.updateprofilesetmaxplayers.UpdateProfileSetMaxPlayersModelContext;
import stories.updateprofilesetqueryport.UpdateProfileSetQueryPortFacade;
import stories.updateprofilesetqueryport.UpdateProfileSetQueryPortFacadeImpl;
import stories.updateprofilesetqueryport.UpdateProfileSetQueryPortModelContext;
import stories.updateprofilesetservername.UpdateProfileSetServerNameFacade;
import stories.updateprofilesetservername.UpdateProfileSetServerNameFacadeImpl;
import stories.updateprofilesetservername.UpdateProfileSetServerNameModelContext;
import stories.updateprofilesetserverpassword.UpdateProfileSetServerPasswordFacade;
import stories.updateprofilesetserverpassword.UpdateProfileSetServerPasswordFacadeImpl;
import stories.updateprofilesetserverpassword.UpdateProfileSetServerPasswordModelContext;
import stories.updateprofilesetwebpassword.UpdateProfileSetWebPasswordFacade;
import stories.updateprofilesetwebpassword.UpdateProfileSetWebPasswordFacadeImpl;
import stories.updateprofilesetwebpassword.UpdateProfileSetWebPasswordModelContext;
import stories.updateprofilesetwebport.UpdateProfileSetWebPortFacade;
import stories.updateprofilesetwebport.UpdateProfileSetWebPortFacadeImpl;
import stories.updateprofilesetwebport.UpdateProfileSetWebPortModelContext;
import utils.Utils;

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
        ListValuesMainContentFacade listValuesMainContentFacade = new ListValuesMainContentFacadeImpl();
        return listValuesMainContentFacade.execute();
    }

    @Override
    public LoadActualProfileFacadeResult loadActualProfile(String platformName, String profileName) throws Exception {
        LoadActualProfileModelContext loadActualProfileModelContext = new LoadActualProfileModelContext(
                platformName,
                profileName
        );
        LoadActualProfileFacade loadActualProfileFacade = new LoadActualProfileFacadeImpl(loadActualProfileModelContext);
        LoadActualProfileFacadeResult result = loadActualProfileFacade.execute();

        PropertyService propertyService = new PropertyServiceImpl();
        propertyService.setProperty("properties/config.properties", "prop.config.lastSelectedProfile", profileName);
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");

        if (!languageCode.equals(result.getProfileDto().getLanguage().getKey())) {
            propertyService.setProperty("properties/config.properties", "prop.config.selectedLanguageCode", result.getProfileDto().getLanguage().getKey());
            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                    "prop.message.languageChanged");
            String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                    "prop.message.applicationMustBeRestarted");
            Utils.infoDialog(headerText, contentText);
        }

        return result;
    }

    @Override
    public ObservableList<ProfileDto> listAllProfiles() throws Exception {
        return null;
    }

    @Override
    public void updateProfileSetGameType(String profileName, String gameTypeCode) throws Exception {
        UpdateProfileSetGameTypeModelContext updateProfileSetGameTypeModelContext = new UpdateProfileSetGameTypeModelContext(
                profileName,
                gameTypeCode
        );
        UpdateProfileSetGameTypeFacade updateProfileSetGameTypeFacade = new UpdateProfileSetGameTypeFacadeImpl(updateProfileSetGameTypeModelContext);
        updateProfileSetGameTypeFacade.execute();
    }

    @Override
    public void updateProfileSetMap(String profileName, String mapCode, boolean isOfficial) throws Exception {
        UpdateProfileSetMapModelContext updateProfileSetMapModelContext = new UpdateProfileSetMapModelContext(
                profileName,
                mapCode,
                isOfficial
        );
        UpdateProfileSetMapFacade updateProfileSetMapFacade = new UpdateProfileSetMapFacadeImpl(updateProfileSetMapModelContext);
        updateProfileSetMapFacade.execute();
    }

    @Override
    public void updateProfileSetDifficulty(String profileName, String difficultyCode) throws Exception {
        UpdateProfileSetDifficultyModelContext updateProfileSetDifficultyModelContext = new UpdateProfileSetDifficultyModelContext(
                profileName,
                difficultyCode
        );
        UpdateProfileSetDifficultyFacade updateProfileSetDifficultyFacade = new UpdateProfileSetDifficultyFacadeImpl(updateProfileSetDifficultyModelContext);
        updateProfileSetDifficultyFacade.execute();
    }

    @Override
    public void updateProfileSetLength(String profileName, String lengthCode) throws Exception {
        UpdateProfileSetLengthModelContext updateProfileSetLengthModelContext = new UpdateProfileSetLengthModelContext(
                profileName,
                lengthCode
        );
        UpdateProfileSetLengthFacade updateProfileSetLengthFacade = new UpdateProfileSetLengthFacadeImpl(updateProfileSetLengthModelContext);
        updateProfileSetLengthFacade.execute();
    }

    @Override
    public void updateProfileSetMaxPlayers(String profileName, String maxPlayersCode) throws Exception {
        UpdateProfileSetMaxPlayersModelContext updateProfileSetMaxPlayersModelContext = new UpdateProfileSetMaxPlayersModelContext(
                profileName,
                maxPlayersCode
        );
        UpdateProfileSetMaxPlayersFacade updateProfileSetMaxPlayersFacade = new UpdateProfileSetMaxPlayersFacadeImpl(updateProfileSetMaxPlayersModelContext);
        updateProfileSetMaxPlayersFacade.execute();
    }

    @Override
    public void updateProfileSetLanguage(String profileName, String languageCode) throws Exception {
        UpdateProfileSetLanguageModelContext updateProfileSetLanguageModelContext = new UpdateProfileSetLanguageModelContext(
                profileName,
                languageCode
        );
        UpdateProfileSetLanguageFacade updateProfileSetLanguageFacade = new UpdateProfileSetLanguageFacadeImpl(updateProfileSetLanguageModelContext);
        updateProfileSetLanguageFacade.execute();

        PropertyService propertyService = new PropertyServiceImpl();
        propertyService.setProperty("properties/config.properties", "prop.config.selectedLanguageCode", languageCode);
        String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                "prop.message.languageChanged");
        String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                "prop.message.applicationMustBeRestarted");
        Utils.infoDialog(headerText, contentText);
    }

    @Override
    public void updateProfileSetServerName(String profileName, String serverName) throws Exception {
        UpdateProfileSetServerNameModelContext updateProfileSetServerNameModelContext = new UpdateProfileSetServerNameModelContext(
                profileName,
                serverName
        );
        UpdateProfileSetServerNameFacade updateProfileSetServerNameFacade = new UpdateProfileSetServerNameFacadeImpl(updateProfileSetServerNameModelContext);
        updateProfileSetServerNameFacade.execute();
    }

    @Override
    public void updateProfileSetServerPassword(String profileName, String serverPassword) throws Exception {
        UpdateProfileSetServerPasswordModelContext updateProfileSetServerPasswordModelContext = new UpdateProfileSetServerPasswordModelContext(
                profileName,
                serverPassword
        );
        UpdateProfileSetServerPasswordFacade updateProfileSetServerPasswordFacade = new UpdateProfileSetServerPasswordFacadeImpl(updateProfileSetServerPasswordModelContext);
        updateProfileSetServerPasswordFacade.execute();
    }

    @Override
    public void updateProfileSetWebPassword(String profileName, String webPassword) throws Exception {
        UpdateProfileSetWebPasswordModelContext updateProfileSetWebPasswordModelContext = new UpdateProfileSetWebPasswordModelContext(
                profileName,
                webPassword
        );
        UpdateProfileSetWebPasswordFacade updateProfileSetWebPasswordFacade = new UpdateProfileSetWebPasswordFacadeImpl(updateProfileSetWebPasswordModelContext);
        updateProfileSetWebPasswordFacade.execute();
    }

    @Override
    public void updateProfileSetWebPort(String profileName, Integer webPort) throws Exception {
        UpdateProfileSetWebPortModelContext updateProfileSetWebPortModelContext = new UpdateProfileSetWebPortModelContext(
                profileName,
                webPort
        );
        UpdateProfileSetWebPortFacade updateProfileSetWebPortFacade = new UpdateProfileSetWebPortFacadeImpl(updateProfileSetWebPortModelContext);
        updateProfileSetWebPortFacade.execute();
    }

    @Override
    public void updateProfileSetGamePort(String profileName, Integer gamePort) throws Exception {
        UpdateProfileSetGamePortModelContext updateProfileSetGamePortModelContext = new UpdateProfileSetGamePortModelContext(
                profileName,
                gamePort
        );
        UpdateProfileSetGamePortFacade updateProfileSetGamePortFacade = new UpdateProfileSetGamePortFacadeImpl(updateProfileSetGamePortModelContext);
        updateProfileSetGamePortFacade.execute();
    }

    @Override
    public void updateProfileSetQueryPort(String profileName, Integer queryPort) throws Exception {
        UpdateProfileSetQueryPortModelContext updateProfileSetQueryPortModelContext = new UpdateProfileSetQueryPortModelContext(
                profileName,
                queryPort
        );
        UpdateProfileSetQueryPortFacade updateProfileSetQueryPortFacade = new UpdateProfileSetQueryPortFacadeImpl(updateProfileSetQueryPortModelContext);
        updateProfileSetQueryPortFacade.execute();
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
