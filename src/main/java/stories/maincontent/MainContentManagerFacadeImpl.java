package stories.maincontent;

import dtos.ProfileDto;
import framework.AbstractManagerFacade;
import framework.EmptyModelContext;
import services.PropertyService;
import services.PropertyServiceImpl;
import stories.findprofilebyname.FindProfileByNameFacade;
import stories.findprofilebyname.FindProfileByNameFacadeImpl;
import stories.findprofilebyname.FindProfileByNameFacadeResult;
import stories.findprofilebyname.FindProfileByNameModelContext;
import stories.installationfolder.InstallationFolderFacade;
import stories.installationfolder.InstallationFolderFacadeImpl;
import stories.installationfolder.InstallationFolderFacadeResult;
import stories.installationfolder.InstallationFolderModelContext;
import stories.joinservers.JoinServersFacade;
import stories.joinservers.JoinServersFacadeImpl;
import stories.joinservers.JoinServersModelContext;
import stories.runservers.RunServersFacade;
import stories.runservers.RunServersFacadeImpl;
import stories.listvaluesmaincontent.ListValuesMainContentFacade;
import stories.listvaluesmaincontent.ListValuesMainContentFacadeImpl;
import stories.listvaluesmaincontent.ListValuesMainContentFacadeResult;
import stories.loadactualprofile.LoadActualProfileFacade;
import stories.loadactualprofile.LoadActualProfileFacadeImpl;
import stories.loadactualprofile.LoadActualProfileFacadeResult;
import stories.loadactualprofile.LoadActualProfileModelContext;
import stories.runservers.RunServersModelContext;
import stories.updateprofilesetadmincanpause.UpdateProfileSetAdminCanPauseFacade;
import stories.updateprofilesetadmincanpause.UpdateProfileSetAdminCanPauseFacadeImpl;
import stories.updateprofilesetadmincanpause.UpdateProfileSetAdminCanPauseModelContext;
import stories.updateprofilesetannounceadminlogin.UpdateProfileSetAnnounceAdminLoginFacade;
import stories.updateprofilesetannounceadminlogin.UpdateProfileSetAnnounceAdminLoginFacadeImpl;
import stories.updateprofilesetannounceadminlogin.UpdateProfileSetAnnounceAdminLoginModelContext;
import stories.updateprofilesetchatlogging.UpdateProfileSetChatLoggingFacade;
import stories.updateprofilesetchatlogging.UpdateProfileSetChatLoggingFacadeImpl;
import stories.updateprofilesetchatlogging.UpdateProfileSetChatLoggingModelContext;
import stories.updateprofilesetchatloggingfile.UpdateProfileSetChatLoggingFileFacade;
import stories.updateprofilesetchatloggingfile.UpdateProfileSetChatLoggingFileFacadeImpl;
import stories.updateprofilesetchatloggingfile.UpdateProfileSetChatLoggingFileModelContext;
import stories.updateprofilesetchatloggingfiletimestamp.UpdateProfileSetChatLoggingFileTimestampFacade;
import stories.updateprofilesetchatloggingfiletimestamp.UpdateProfileSetChatLoggingFileTimestampFacadeImpl;
import stories.updateprofilesetchatloggingfiletimestamp.UpdateProfileSetChatLoggingFileTimestampModelContext;
import stories.updateprofilesetcustomparameters.UpdateProfileSetCustomParametersFacade;
import stories.updateprofilesetcustomparameters.UpdateProfileSetCustomParametersFacadeImpl;
import stories.updateprofilesetcustomparameters.UpdateProfileSetCustomParametersModelContext;
import stories.updateprofilesetdeadplayerscantalk.UpdateProfileSetDeadPlayersCanTalkFacade;
import stories.updateprofilesetdeadplayerscantalk.UpdateProfileSetDeadPlayersCanTalkFacadeImpl;
import stories.updateprofilesetdeadplayerscantalk.UpdateProfileSetDeadPlayersCanTalkModelContext;
import stories.updateprofilesetdifficulty.UpdateProfileSetDifficultyFacade;
import stories.updateprofilesetdifficulty.UpdateProfileSetDifficultyFacadeImpl;
import stories.updateprofilesetdifficulty.UpdateProfileSetDifficultyModelContext;
import stories.updateprofilesetfriendlyfirepercentage.UpdateProfileSetFriendlyFirePercentageFacade;
import stories.updateprofilesetfriendlyfirepercentage.UpdateProfileSetFriendlyFirePercentageFacadeImpl;
import stories.updateprofilesetfriendlyfirepercentage.UpdateProfileSetFriendlyFirePercentageModelContext;
import stories.updateprofilesetgameport.UpdateProfileSetGamePortFacade;
import stories.updateprofilesetgameport.UpdateProfileSetGamePortFacadeImpl;
import stories.updateprofilesetgameport.UpdateProfileSetGamePortModelContext;
import stories.updateprofilesetgamestartdelay.UpdateProfileSetGameStartDelayFacade;
import stories.updateprofilesetgamestartdelay.UpdateProfileSetGameStartDelayFacadeImpl;
import stories.updateprofilesetgamestartdelay.UpdateProfileSetGameStartDelayModelContext;
import stories.updateprofilesetgametype.UpdateProfileSetGameTypeFacade;
import stories.updateprofilesetgametype.UpdateProfileSetGameTypeFacadeImpl;
import stories.updateprofilesetgametype.UpdateProfileSetGameTypeModelContext;
import stories.updateprofilesetkickpercentage.UpdateProfileSetKickPercentageFacade;
import stories.updateprofilesetkickpercentage.UpdateProfileSetKickPercentageFacadeImpl;
import stories.updateprofilesetkickpercentage.UpdateProfileSetKickPercentageModelContext;
import stories.updateprofilesetkickvoting.UpdateProfileSetKickVotingFacade;
import stories.updateprofilesetkickvoting.UpdateProfileSetKickVotingFacadeImpl;
import stories.updateprofilesetkickvoting.UpdateProfileSetKickVotingModelContext;
import stories.updateprofilesetlanguage.UpdateProfileSetLanguageFacade;
import stories.updateprofilesetlanguage.UpdateProfileSetLanguageFacadeImpl;
import stories.updateprofilesetlanguage.UpdateProfileSetLanguageModelContext;
import stories.updateprofilesetlength.UpdateProfileSetLengthFacade;
import stories.updateprofilesetlength.UpdateProfileSetLengthFacadeImpl;
import stories.updateprofilesetlength.UpdateProfileSetLengthModelContext;
import stories.updateprofilesetmap.UpdateProfileSetMapFacade;
import stories.updateprofilesetmap.UpdateProfileSetMapFacadeImpl;
import stories.updateprofilesetmap.UpdateProfileSetMapModelContext;
import stories.updateprofilesetmapobjetives.UpdateProfileSetMapObjetivesFacade;
import stories.updateprofilesetmapobjetives.UpdateProfileSetMapObjetivesFacadeImpl;
import stories.updateprofilesetmapobjetives.UpdateProfileSetMapObjetivesModelContext;
import stories.updateprofilesetmapvoting.UpdateProfileSetMapVotingFacade;
import stories.updateprofilesetmapvoting.UpdateProfileSetMapVotingFacadeImpl;
import stories.updateprofilesetmapvoting.UpdateProfileSetMapVotingModelContext;
import stories.updateprofilesetmapvotingtime.UpdateProfileSetMapVotingTimeFacade;
import stories.updateprofilesetmapvotingtime.UpdateProfileSetMapVotingTimeFacadeImpl;
import stories.updateprofilesetmapvotingtime.UpdateProfileSetMapVotingTimeModelContext;
import stories.updateprofilesetmaxidletime.UpdateProfileSetMaxIdleTimeFacade;
import stories.updateprofilesetmaxidletime.UpdateProfileSetMaxIdleTimeFacadeImpl;
import stories.updateprofilesetmaxidletime.UpdateProfileSetMaxIdleTimeModelContext;
import stories.updateprofilesetmaxplayers.UpdateProfileSetMaxPlayersFacade;
import stories.updateprofilesetmaxplayers.UpdateProfileSetMaxPlayersFacadeImpl;
import stories.updateprofilesetmaxplayers.UpdateProfileSetMaxPlayersModelContext;
import stories.updateprofilesetmaxspectators.UpdateProfileSetMaxSpectatorsFacade;
import stories.updateprofilesetmaxspectators.UpdateProfileSetMaxSpectatorsFacadeImpl;
import stories.updateprofilesetmaxspectators.UpdateProfileSetMaxSpectatorsModelContext;
import stories.updateprofilesetpickupitems.UpdateProfileSetPickupItemsFacade;
import stories.updateprofilesetpickupitems.UpdateProfileSetPickupItemsFacadeImpl;
import stories.updateprofilesetpickupitems.UpdateProfileSetPickupItemsModelContext;
import stories.updateprofilesetpublictextchat.UpdateProfileSetPublicTextChatFacade;
import stories.updateprofilesetpublictextchat.UpdateProfileSetPublicTextChatFacadeImpl;
import stories.updateprofilesetpublictextchat.UpdateProfileSetPublicTextChatModelContext;
import stories.updateprofilesetqueryport.UpdateProfileSetQueryPortFacade;
import stories.updateprofilesetqueryport.UpdateProfileSetQueryPortFacadeImpl;
import stories.updateprofilesetqueryport.UpdateProfileSetQueryPortModelContext;
import stories.updateprofilesetreadyupdelay.UpdateProfileSetReadyUpDelayFacade;
import stories.updateprofilesetreadyupdelay.UpdateProfileSetReadyUpDelayFacadeImpl;
import stories.updateprofilesetreadyupdelay.UpdateProfileSetReadyUpDelayModelContext;
import stories.updateprofilesetservername.UpdateProfileSetServerNameFacade;
import stories.updateprofilesetservername.UpdateProfileSetServerNameFacadeImpl;
import stories.updateprofilesetservername.UpdateProfileSetServerNameModelContext;
import stories.updateprofilesetserverpassword.UpdateProfileSetServerPasswordFacade;
import stories.updateprofilesetserverpassword.UpdateProfileSetServerPasswordFacadeImpl;
import stories.updateprofilesetserverpassword.UpdateProfileSetServerPasswordModelContext;
import stories.updateprofilesetspectatorschat.UpdateProfileSetSpectatorsChatFacade;
import stories.updateprofilesetspectatorschat.UpdateProfileSetSpectatorsChatFacadeImpl;
import stories.updateprofilesetspectatorschat.UpdateProfileSetSpectatorsChatModelContext;
import stories.updateprofilesettakeover.UpdateProfileSetTakeoverFacade;
import stories.updateprofilesettakeover.UpdateProfileSetTakeoverFacadeImpl;
import stories.updateprofilesettakeover.UpdateProfileSetTakeoverModelContext;
import stories.updateprofilesetteamcollision.UpdateProfileSetTeamCollisionFacade;
import stories.updateprofilesetteamcollision.UpdateProfileSetTeamCollisionFacadeImpl;
import stories.updateprofilesetteamcollision.UpdateProfileSetTeamCollisionModelContext;
import stories.updateprofilesettimebetweenkicks.UpdateProfileSetTimeBetweenKicksFacade;
import stories.updateprofilesettimebetweenkicks.UpdateProfileSetTimeBetweenKicksFacadeImpl;
import stories.updateprofilesettimebetweenkicks.UpdateProfileSetTimeBetweenKicksModelContext;
import stories.updateprofileseturlimageserver.UpdateProfileSetUrlImageServerFacade;
import stories.updateprofileseturlimageserver.UpdateProfileSetUrlImageServerFacadeImpl;
import stories.updateprofileseturlimageserver.UpdateProfileSetUrlImageServerModelContext;
import stories.updateprofilesetvoip.UpdateProfileSetVoipFacade;
import stories.updateprofilesetvoip.UpdateProfileSetVoipFacadeImpl;
import stories.updateprofilesetvoip.UpdateProfileSetVoipModelContext;
import stories.updateprofilesetwebpage.UpdateProfileSetWebPageFacade;
import stories.updateprofilesetwebpage.UpdateProfileSetWebPageFacadeImpl;
import stories.updateprofilesetwebpage.UpdateProfileSetWebPageModelContext;
import stories.updateprofilesetwebpassword.UpdateProfileSetWebPasswordFacade;
import stories.updateprofilesetwebpassword.UpdateProfileSetWebPasswordFacadeImpl;
import stories.updateprofilesetwebpassword.UpdateProfileSetWebPasswordModelContext;
import stories.updateprofilesetwebport.UpdateProfileSetWebPortFacade;
import stories.updateprofilesetwebport.UpdateProfileSetWebPortFacadeImpl;
import stories.updateprofilesetwebport.UpdateProfileSetWebPortModelContext;
import stories.updateprofilesetwelcomemessage.UpdateProfileSetWelcomeMessageFacade;
import stories.updateprofilesetwelcomemessage.UpdateProfileSetWelcomeMessageFacadeImpl;
import stories.updateprofilesetwelcomemessage.UpdateProfileSetWelcomeMessageModelContext;
import stories.updateprofilesetyourclan.UpdateProfileSetYourClanFacade;
import stories.updateprofilesetyourclan.UpdateProfileSetYourClanFacadeImpl;
import stories.updateprofilesetyourclan.UpdateProfileSetYourClanModelContext;
import stories.updateprofilesetyourweblink.UpdateProfileSetYourWebLinkFacade;
import stories.updateprofilesetyourweblink.UpdateProfileSetYourWebLinkFacadeImpl;
import stories.updateprofilesetyourweblink.UpdateProfileSetYourWebLinkModelContext;
import utils.Utils;

import java.sql.SQLException;

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
    public void updateProfileSetYourClan(String profileName, String yourClan) throws Exception {
        UpdateProfileSetYourClanModelContext updateProfileSetYourClanModelContext = new UpdateProfileSetYourClanModelContext(
                profileName,
                yourClan
        );
        UpdateProfileSetYourClanFacade updateProfileSetYourClanFacade = new UpdateProfileSetYourClanFacadeImpl(updateProfileSetYourClanModelContext);
        updateProfileSetYourClanFacade.execute();
    }

    @Override
    public void updateProfileSetYourWebLink(String profileName, String yourWebLink) throws Exception {
        UpdateProfileSetYourWebLinkModelContext updateProfileSetYourWebLinkModelContext = new UpdateProfileSetYourWebLinkModelContext(
                profileName,
                yourWebLink
        );
        UpdateProfileSetYourWebLinkFacade updateProfileSetYourWebLinkFacade = new UpdateProfileSetYourWebLinkFacadeImpl(updateProfileSetYourWebLinkModelContext);
        updateProfileSetYourWebLinkFacade.execute();
    }

    @Override
    public void updateProfileSetUrlImageServer(String profileName, String urlImageServer) throws Exception {
        UpdateProfileSetUrlImageServerModelContext updateProfileSetUrlImageServerModelContext = new UpdateProfileSetUrlImageServerModelContext(
                profileName,
                urlImageServer
        );
        UpdateProfileSetUrlImageServerFacade updateProfileSetUrlImageServerFacade = new UpdateProfileSetUrlImageServerFacadeImpl(updateProfileSetUrlImageServerModelContext);
        updateProfileSetUrlImageServerFacade.execute();
    }

    @Override
    public void updateProfileSetWelcomeMessage(String profileName, String welcomeMessage) throws Exception {
        UpdateProfileSetWelcomeMessageModelContext updateProfileSetWelcomeMessageModelContext = new UpdateProfileSetWelcomeMessageModelContext(
                profileName,
                welcomeMessage
        );
        UpdateProfileSetWelcomeMessageFacade updateProfileSetWelcomeMessageFacade = new UpdateProfileSetWelcomeMessageFacadeImpl(updateProfileSetWelcomeMessageModelContext);
        updateProfileSetWelcomeMessageFacade.execute();
    }

    @Override
    public void updateProfileSetCustomParameters(String profileName, String customParameters) throws Exception {
        UpdateProfileSetCustomParametersModelContext updateProfileSetCustomParametersModelContext = new UpdateProfileSetCustomParametersModelContext(
                profileName,
                customParameters
        );
        UpdateProfileSetCustomParametersFacade updateProfileSetCustomParametersFacade = new UpdateProfileSetCustomParametersFacadeImpl(updateProfileSetCustomParametersModelContext);
        updateProfileSetCustomParametersFacade.execute();
    }

    @Override
    public void updateProfileSetWebPage(String profileName, boolean isSelected) throws Exception {
        UpdateProfileSetWebPageModelContext updateProfileSetWebPageModelContext = new UpdateProfileSetWebPageModelContext(
                profileName,
                isSelected
        );
        UpdateProfileSetWebPageFacade updateProfileSetWebPageFacade = new UpdateProfileSetWebPageFacadeImpl(updateProfileSetWebPageModelContext);
        updateProfileSetWebPageFacade.execute();
    }

    @Override
    public void runServers(String platformName, String actualSelectedProfileName, String actualSelectedLanguage) throws Exception {
        RunServersModelContext runServersModelContext = new RunServersModelContext(
                platformName,
                actualSelectedProfileName,
                actualSelectedLanguage
        );
        RunServersFacade runServersFacade = new RunServersFacadeImpl(runServersModelContext);
        runServersFacade.execute();
    }

    @Override
    public void joinServer(String platformName, String actualSelectedProfileName, String actualSelectedLanguage) throws Exception {
        JoinServersModelContext joinServersModelContext = new JoinServersModelContext(
                platformName,
                actualSelectedProfileName,
                actualSelectedLanguage
        );
        JoinServersFacade joinServersFacade = new JoinServersFacadeImpl(joinServersModelContext);
        joinServersFacade.execute();
    }

    @Override
    public boolean isCorrectInstallationFolder(String platformName) throws Exception {
        InstallationFolderModelContext installationFolderModelContext = new InstallationFolderModelContext(
                platformName
        );
        InstallationFolderFacade installationFolderFacade = new InstallationFolderFacadeImpl(installationFolderModelContext);
        InstallationFolderFacadeResult result = installationFolderFacade.execute();
        return result.isCorrectInstallationFolder();
    }

    @Override
    public void updateProfileSetTakeover(String profileName, boolean takeoverSelected) throws Exception {
        UpdateProfileSetTakeoverModelContext updateProfileSetTakeoverModelContext = new UpdateProfileSetTakeoverModelContext(
                profileName,
                takeoverSelected
        );
        UpdateProfileSetTakeoverFacade updateProfileSetTakeoverFacade = new UpdateProfileSetTakeoverFacadeImpl(updateProfileSetTakeoverModelContext);
        updateProfileSetTakeoverFacade.execute();
    }

    @Override
    public void updateProfileSetMapVoting(String profileName, boolean mapVotingSelected) throws Exception {
        UpdateProfileSetMapVotingModelContext updateProfileSetMapVotingModelContext = new UpdateProfileSetMapVotingModelContext(
                profileName,
                mapVotingSelected
        );
        UpdateProfileSetMapVotingFacade updateProfileSetMapVotingFacade = new UpdateProfileSetMapVotingFacadeImpl(updateProfileSetMapVotingModelContext);
        updateProfileSetMapVotingFacade.execute();
    }

    @Override
    public void updateProfileSetKickVoting(String profileName, boolean kickVotingSelected) throws Exception {
        UpdateProfileSetKickVotingModelContext updateProfileSetKickVotingModelContext = new UpdateProfileSetKickVotingModelContext(
                profileName,
                kickVotingSelected
        );
        UpdateProfileSetKickVotingFacade updateProfileSetKickVotingFacade = new UpdateProfileSetKickVotingFacadeImpl(updateProfileSetKickVotingModelContext);
        updateProfileSetKickVotingFacade.execute();
    }

    @Override
    public void updateProfileSetPublicTextChat(String profileName, boolean publicTextChatSelected) throws Exception {
        UpdateProfileSetPublicTextChatModelContext updateProfileSetPublicTextChatModelContext = new UpdateProfileSetPublicTextChatModelContext(
                profileName,
                publicTextChatSelected
        );
        UpdateProfileSetPublicTextChatFacade updateProfileSetPublicTextChatFacade = new UpdateProfileSetPublicTextChatFacadeImpl(updateProfileSetPublicTextChatModelContext);
        updateProfileSetPublicTextChatFacade.execute();
    }

    @Override
    public void updateProfileSetSpectatorsChat(String profileName, boolean spectatorsChatSelected) throws Exception {
        UpdateProfileSetSpectatorsChatModelContext updateProfileSetSpectatorsChatModelContext = new UpdateProfileSetSpectatorsChatModelContext(
                profileName,
                spectatorsChatSelected
        );
        UpdateProfileSetSpectatorsChatFacade updateProfileSetSpectatorsChatFacade = new UpdateProfileSetSpectatorsChatFacadeImpl(updateProfileSetSpectatorsChatModelContext);
        updateProfileSetSpectatorsChatFacade.execute();
    }

    @Override
    public void updateProfileSetVoip(String profileName, boolean voipSelected) throws Exception {
        UpdateProfileSetVoipModelContext updateProfileSetVoipModelContext = new UpdateProfileSetVoipModelContext(
                profileName,
                voipSelected
        );
        UpdateProfileSetVoipFacade updateProfileSetVoipFacade = new UpdateProfileSetVoipFacadeImpl(updateProfileSetVoipModelContext);
        updateProfileSetVoipFacade.execute();
    }

    @Override
    public void updateProfileSetTeamCollision(String profileName, boolean teamCollisionSelected) throws Exception {
        UpdateProfileSetTeamCollisionModelContext updateProfileSetTeamCollisionModelContext = new UpdateProfileSetTeamCollisionModelContext(
                profileName,
                teamCollisionSelected
        );
        UpdateProfileSetTeamCollisionFacade updateProfileSetTeamCollisionFacade = new UpdateProfileSetTeamCollisionFacadeImpl(updateProfileSetTeamCollisionModelContext);
        updateProfileSetTeamCollisionFacade.execute();
    }

    @Override
    public void updateProfileSetAdminCanPause(String profileName, boolean adminCanPauseSelected) throws Exception {
        UpdateProfileSetAdminCanPauseModelContext updateProfileSetAdminCanPauseModelContext = new UpdateProfileSetAdminCanPauseModelContext(
                profileName,
                adminCanPauseSelected
        );
        UpdateProfileSetAdminCanPauseFacade updateProfileSetAdminCanPauseFacade = new UpdateProfileSetAdminCanPauseFacadeImpl(updateProfileSetAdminCanPauseModelContext);
        updateProfileSetAdminCanPauseFacade.execute();
    }

    @Override
    public void updateProfileSetAnnounceAdminLogin(String profileName, boolean announceAdminLoginSelected) throws Exception {
        UpdateProfileSetAnnounceAdminLoginModelContext updateProfileSetAnnounceAdminLoginModelContext = new UpdateProfileSetAnnounceAdminLoginModelContext(
                profileName,
                announceAdminLoginSelected
        );
        UpdateProfileSetAnnounceAdminLoginFacade updateProfileSetAnnounceAdminLoginFacade = new UpdateProfileSetAnnounceAdminLoginFacadeImpl(updateProfileSetAnnounceAdminLoginModelContext);
        updateProfileSetAnnounceAdminLoginFacade.execute();
    }

    @Override
    public void updateProfileSetChatLogging(String profileName, boolean chatLoggingSelected) throws Exception {
        UpdateProfileSetChatLoggingModelContext updateProfileSetChatLoggingModelContext = new UpdateProfileSetChatLoggingModelContext(
                profileName,
                chatLoggingSelected
        );
        UpdateProfileSetChatLoggingFacade updateProfileSetChatLoggingFacade = new UpdateProfileSetChatLoggingFacadeImpl(updateProfileSetChatLoggingModelContext);
        updateProfileSetChatLoggingFacade.execute();
    }

    @Override
    public void updateProfileSetMapVotingTime(String profileName, Double mapVotingTime) throws Exception {
        UpdateProfileSetMapVotingTimeModelContext updateProfileSetMapVotingTimeModelContext = new UpdateProfileSetMapVotingTimeModelContext(
                profileName,
                mapVotingTime
        );
        UpdateProfileSetMapVotingTimeFacade updateProfileSetMapVotingTimeFacade = new UpdateProfileSetMapVotingTimeFacadeImpl(updateProfileSetMapVotingTimeModelContext);
        updateProfileSetMapVotingTimeFacade.execute();
    }

    @Override
    public void updateProfileSetKickPercentage(String profileName, Double kickPercentage) throws Exception {
        UpdateProfileSetKickPercentageModelContext updateProfileSetKickPercentageModelContext = new UpdateProfileSetKickPercentageModelContext(
                profileName,
                kickPercentage
        );
        UpdateProfileSetKickPercentageFacade updateProfileSetKickPercentageFacade = new UpdateProfileSetKickPercentageFacadeImpl(updateProfileSetKickPercentageModelContext);
        updateProfileSetKickPercentageFacade.execute();
    }

    @Override
    public void updateProfileSetChatLoggingFile(String profileName, String chatLoggingFile) throws Exception {
        UpdateProfileSetChatLoggingFileModelContext updateProfileSetChatLoggingFileModelContext = new UpdateProfileSetChatLoggingFileModelContext(
                profileName,
                chatLoggingFile
        );
        UpdateProfileSetChatLoggingFileFacade updateProfileSetChatLoggingFileFacade = new UpdateProfileSetChatLoggingFileFacadeImpl(updateProfileSetChatLoggingFileModelContext);
        updateProfileSetChatLoggingFileFacade.execute();
    }

    @Override
    public void updateProfileSetChatLoggingFileTimestamp(String profileName, boolean chatLoggingFileTimestampSelected) throws Exception {
        UpdateProfileSetChatLoggingFileTimestampModelContext updateProfileSetChatLoggingFileTimestampModelContext = new UpdateProfileSetChatLoggingFileTimestampModelContext(
                profileName,
                chatLoggingFileTimestampSelected
        );
        UpdateProfileSetChatLoggingFileTimestampFacade updateProfileSetChatLoggingFileTimestampFacade = new UpdateProfileSetChatLoggingFileTimestampFacadeImpl(updateProfileSetChatLoggingFileTimestampModelContext);
        updateProfileSetChatLoggingFileTimestampFacade.execute();
    }

    @Override
    public void updateProfileSetTimeBetweenKicks(String profileName, Double timeBetweenKicks) throws Exception {
        UpdateProfileSetTimeBetweenKicksModelContext updateProfileSetTimeBetweenKicksModelContext = new UpdateProfileSetTimeBetweenKicksModelContext(
                profileName,
                timeBetweenKicks
        );
        UpdateProfileSetTimeBetweenKicksFacade updateProfileSetTimeBetweenKicksFacade = new UpdateProfileSetTimeBetweenKicksFacadeImpl(updateProfileSetTimeBetweenKicksModelContext);
        updateProfileSetTimeBetweenKicksFacade.execute();
    }

    @Override
    public void updateProfileSetMaxIdleTime(String profileName, Double maxIdleTime) throws Exception {
        UpdateProfileSetMaxIdleTimeModelContext updateProfileSetMaxIdleTimeModelContext = new UpdateProfileSetMaxIdleTimeModelContext(
                profileName,
                maxIdleTime
        );
        UpdateProfileSetMaxIdleTimeFacade updateProfileSetMaxIdleTimeFacade = new UpdateProfileSetMaxIdleTimeFacadeImpl(updateProfileSetMaxIdleTimeModelContext);
        updateProfileSetMaxIdleTimeFacade.execute();
    }

    @Override
    public void updateProfileSetDeadPlayersCanTalk(String profileName, boolean deadPlayersCanTalkSelected) throws Exception {
        UpdateProfileSetDeadPlayersCanTalkModelContext updateProfileSetDeadPlayersCanTalkModelContext = new UpdateProfileSetDeadPlayersCanTalkModelContext(
                profileName,
                deadPlayersCanTalkSelected
        );
        UpdateProfileSetDeadPlayersCanTalkFacade updateProfileSetDeadPlayersCanTalkFacade = new UpdateProfileSetDeadPlayersCanTalkFacadeImpl(updateProfileSetDeadPlayersCanTalkModelContext);
        updateProfileSetDeadPlayersCanTalkFacade.execute();
    }

    @Override
    public void updateProfileSetReadyUpDelay(String profileName, Integer readyUpDelay) throws Exception {
        UpdateProfileSetReadyUpDelayModelContext updateProfileSetReadyUpDelayModelContext = new UpdateProfileSetReadyUpDelayModelContext(
                profileName,
                readyUpDelay
        );
        UpdateProfileSetReadyUpDelayFacade updateProfileSetReadyUpDelayFacade = new UpdateProfileSetReadyUpDelayFacadeImpl(updateProfileSetReadyUpDelayModelContext);
        updateProfileSetReadyUpDelayFacade.execute();
    }

    @Override
    public void updateProfileSetGameStartDelay(String profileName, Integer gameStartDelay) throws Exception {
        UpdateProfileSetGameStartDelayModelContext updateProfileSetGameStartDelayModelContext = new UpdateProfileSetGameStartDelayModelContext(
                profileName,
                gameStartDelay
        );
        UpdateProfileSetGameStartDelayFacade updateProfileSetGameStartDelayFacade = new UpdateProfileSetGameStartDelayFacadeImpl(updateProfileSetGameStartDelayModelContext);
        updateProfileSetGameStartDelayFacade.execute();
    }

    @Override
    public void updateProfileSetMaxSpectators(String profileName, Integer maxSpectators) throws Exception {
        UpdateProfileSetMaxSpectatorsModelContext updateProfileSetMaxSpectatorsModelContext = new UpdateProfileSetMaxSpectatorsModelContext(
                profileName,
                maxSpectators
        );
        UpdateProfileSetMaxSpectatorsFacade updateProfileSetMaxSpectatorsFacade = new UpdateProfileSetMaxSpectatorsFacadeImpl(updateProfileSetMaxSpectatorsModelContext);
        updateProfileSetMaxSpectatorsFacade.execute();
    }

    @Override
    public void updateProfileSetMapObjetives(String profileName, boolean mapObjetivesSelected) throws Exception {
        UpdateProfileSetMapObjetivesModelContext updateProfileSetMapObjetivesModelContext = new UpdateProfileSetMapObjetivesModelContext(
                profileName,
                mapObjetivesSelected
        );
        UpdateProfileSetMapObjetivesFacade updateProfileSetMapObjetivesFacade = new UpdateProfileSetMapObjetivesFacadeImpl(updateProfileSetMapObjetivesModelContext);
        updateProfileSetMapObjetivesFacade.execute();
    }

    @Override
    public void updateProfileSetPickupItems(String profileName, boolean pickupItemsSelected) throws Exception {
        UpdateProfileSetPickupItemsModelContext updateProfileSetPickupItemsModelContext = new UpdateProfileSetPickupItemsModelContext(
                profileName,
                pickupItemsSelected
        );
        UpdateProfileSetPickupItemsFacade updateProfileSetPickupItemsFacade = new UpdateProfileSetPickupItemsFacadeImpl(updateProfileSetPickupItemsModelContext);
        updateProfileSetPickupItemsFacade.execute();
    }

    @Override
    public void updateProfileSetFriendlyFirePercentage(String profileName, Double friendlyFirePercentage) throws Exception {
        UpdateProfileSetFriendlyFirePercentageModelContext updateProfileSetFriendlyFirePercentageModelContext = new UpdateProfileSetFriendlyFirePercentageModelContext(
                profileName,
                friendlyFirePercentage
        );
        UpdateProfileSetFriendlyFirePercentageFacade updateProfileSetFriendlyFirePercentageFacade = new UpdateProfileSetFriendlyFirePercentageFacadeImpl(updateProfileSetFriendlyFirePercentageModelContext);
        updateProfileSetFriendlyFirePercentageFacade.execute();
    }

    @Override
    public ProfileDto findProfileDtoByName(String profileName) throws Exception {
        FindProfileByNameModelContext findProfileByNameModelContext = new FindProfileByNameModelContext(
                profileName
        );
        FindProfileByNameFacade findProfileByNameFacade = new FindProfileByNameFacadeImpl(findProfileByNameModelContext);
        FindProfileByNameFacadeResult result = findProfileByNameFacade.execute();
        return result.getProfileDto();
    }
}
