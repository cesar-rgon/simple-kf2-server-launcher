package stories.getprofilefromfile;

import entities.*;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.*;

import java.util.Optional;
import java.util.Properties;

public class GetProfileFromFileFacadeImpl
        extends AbstractTransactionalFacade<GetProfileFromFileModelContext, GetProfileFromFileFacadeResult>
        implements GetProfileFromFileFacade {

    private static final Logger logger = LogManager.getLogger(GetProfileFromFileFacadeImpl.class);

    public GetProfileFromFileFacadeImpl(GetProfileFromFileModelContext getProfileFromFileModelContext) {
        super(getProfileFromFileModelContext, GetProfileFromFileFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(GetProfileFromFileModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected GetProfileFromFileFacadeResult internalExecute(GetProfileFromFileModelContext facadeModelContext, EntityManager em) throws Exception {
        LanguageServiceImpl languageService = new LanguageServiceImpl(em);
        GameTypeServiceImpl gameTypeService = new GameTypeServiceImpl(em);
        DifficultyServiceImpl difficultyService = new DifficultyServiceImpl(em);
        LengthServiceImpl lengthService = new LengthServiceImpl(em);
        MaxPlayersServiceImpl maxPlayersService = new MaxPlayersServiceImpl(em);
        Properties properties = facadeModelContext.getProperties();
        int profileIndex = facadeModelContext.getProfileIndex();

        String profileName = properties.getProperty("exported.profile" + profileIndex + ".name");
        Optional<Language> languageOpt = languageService.findByCode(properties.getProperty("exported.profile" + profileIndex + ".language"));
        Optional<GameType> gameTypeOpt = gameTypeService.findByCode(properties.getProperty("exported.profile" + profileIndex + ".gameType"));
        Optional<Difficulty> difficultyOpt = difficultyService.findByCode(properties.getProperty("exported.profile" + profileIndex + ".difficulty"));
        Optional<Length> lengthOpt = lengthService.findByCode(properties.getProperty("exported.profile" + profileIndex + ".length"));
        Optional<MaxPlayers> maxPlayersOpt = maxPlayersService.findByCode(properties.getProperty("exported.profile" + profileIndex + ".maxPlayers"));
        String webPortStr = properties.getProperty("exported.profile" + profileIndex + ".webPort");
        String gamePortStr = properties.getProperty("exported.profile" + profileIndex + ".gamePort");
        String queryPortStr = properties.getProperty("exported.profile" + profileIndex + ".queryPort");
        String mapVotingTimeStr = properties.getProperty("exported.profile" + profileIndex + ".mapVotingTime");
        String kickPercentageStr = properties.getProperty("exported.profile" + profileIndex + ".kickPercentage");
        String timeBetweenKicksStr = properties.getProperty("exported.profile" + profileIndex + ".timeBetweenKicks");
        String maxIdleTimeStr = properties.getProperty("exported.profile" + profileIndex + ".maxIdleTime");
        String readyUpDelayStr = properties.getProperty("exported.profile" + profileIndex + ".readyUpDelay");
        String gameStartDelayStr = properties.getProperty("exported.profile" + profileIndex + ".gameStartDelay");
        String maxSpectatorsStr = properties.getProperty("exported.profile" + profileIndex + ".maxSpectators");
        String friendlyFirePercentageStr = properties.getProperty("exported.profile" + profileIndex + ".friendlyFirePercentage");
        String webPageStr = properties.getProperty("exported.profile" + profileIndex + ".webPage");
        String takeoverStr = properties.getProperty("exported.profile" + profileIndex + ".takeover");
        String teamCollisionStr = properties.getProperty("exported.profile" + profileIndex + ".teamCollision");
        String adminPauseStr = properties.getProperty("exported.profile" + profileIndex + ".adminPause");
        String adminLoginStr = properties.getProperty("exported.profile" + profileIndex + ".adminLogin");
        String mapVotingStr = properties.getProperty("exported.profile" + profileIndex + ".mapVoting");
        String kickVotingStr = properties.getProperty("exported.profile" + profileIndex + ".kickVoting");
        String publicTextChatStr = properties.getProperty("exported.profile" + profileIndex + ".publicTextChat");
        String spectatorsChatStr = properties.getProperty("exported.profile" + profileIndex + ".spectatorsChat");
        String voipStr = properties.getProperty("exported.profile" + profileIndex + ".voip");
        String chatLoggingStr = properties.getProperty("exported.profile" + profileIndex + ".chatLogging");
        String chatLoggingFileTimestampStr = properties.getProperty("exported.profile" + profileIndex + ".chatLoggingFileTimestamp");
        String deadPlayersCanTalkStr = properties.getProperty("exported.profile" + profileIndex + ".deadPlayersCanTalk");
        String mapObjetivesStr = properties.getProperty("exported.profile" + profileIndex + ".mapObjetives");
        String pickupItemsStr = properties.getProperty("exported.profile" + profileIndex + ".pickupItems");
        String platformName = properties.getProperty("exported.profile" + profileIndex + ".platform");
        String netTickrateStr = properties.getProperty("exported.profile" + profileIndex + ".netTickrate");
        String lanTickrateStr = properties.getProperty("exported.profile" + profileIndex + ".lanTickrate");
        String lanMaxClientRateStr = properties.getProperty("exported.profile" + profileIndex + ".lanMaxClientRate");
        String internetMaxClientRateStr = properties.getProperty("exported.profile" + profileIndex + ".internetMaxClientRate");

        Profile profileFromFile = new Profile(
                profileName,
                languageOpt.isPresent() ? languageOpt.get() : null,
                gameTypeOpt.isPresent() ? gameTypeOpt.get() : null,
                null,
                difficultyOpt.isPresent() ? difficultyOpt.get() : null,
                lengthOpt.isPresent() ? lengthOpt.get() : null,
                maxPlayersOpt.isPresent() ? maxPlayersOpt.get() : null,
                properties.getProperty("exported.profile" + profileIndex + ".serverName"),
                properties.getProperty("exported.profile" + profileIndex + ".serverPassword"),
                StringUtils.isNotBlank(webPageStr) ? Boolean.valueOf(webPageStr): true,
                properties.getProperty("exported.profile" + profileIndex + ".webPassword"),
                StringUtils.isNotBlank(webPortStr) ? Integer.parseInt(webPortStr): 8080,
                StringUtils.isNotBlank(gamePortStr) ? Integer.parseInt(gamePortStr): 7777,
                StringUtils.isNotBlank(queryPortStr) ? Integer.parseInt(queryPortStr): 27015,
                properties.getProperty("exported.profile" + profileIndex + ".yourClan"),
                properties.getProperty("exported.profile" + profileIndex + ".yourWebLink"),
                properties.getProperty("exported.profile" + profileIndex + ".urlImageServer"),
                properties.getProperty("exported.profile" + profileIndex + ".welcomeMessage"),
                properties.getProperty("exported.profile" + profileIndex + ".customParameters"),
                StringUtils.isNotBlank(takeoverStr) ? Boolean.valueOf(takeoverStr): false,
                StringUtils.isNotBlank(teamCollisionStr) ? Boolean.valueOf(teamCollisionStr): true,
                StringUtils.isNotBlank(adminPauseStr) ? Boolean.valueOf(adminPauseStr): false,
                StringUtils.isNotBlank(adminLoginStr) ? Boolean.valueOf(adminLoginStr): true,
                StringUtils.isNotBlank(mapVotingStr) ? Boolean.valueOf(mapVotingStr): true,
                StringUtils.isNotBlank(mapVotingTimeStr) ? Double.parseDouble(mapVotingTimeStr): 60,
                StringUtils.isNotBlank(kickVotingStr) ? Boolean.valueOf(kickVotingStr): true,
                StringUtils.isNotBlank(kickPercentageStr) ? Double.parseDouble(kickPercentageStr): 0.66,
                StringUtils.isNotBlank(publicTextChatStr) ? Boolean.valueOf(publicTextChatStr): true,
                StringUtils.isNotBlank(spectatorsChatStr) ? Boolean.valueOf(spectatorsChatStr): false,
                StringUtils.isNotBlank(voipStr) ? Boolean.valueOf(voipStr): true,
                StringUtils.isNotBlank(chatLoggingStr) ? Boolean.valueOf(chatLoggingStr): false,
                properties.getProperty("exported.profile" + profileIndex + ".chatLoggingFile"),
                StringUtils.isNotBlank(chatLoggingFileTimestampStr) ? Boolean.valueOf(chatLoggingFileTimestampStr): true,
                StringUtils.isNotBlank(timeBetweenKicksStr) ? Double.parseDouble(timeBetweenKicksStr): 10,
                StringUtils.isNotBlank(maxIdleTimeStr) ? Double.parseDouble(maxIdleTimeStr): 0,
                StringUtils.isNotBlank(deadPlayersCanTalkStr) ? Boolean.valueOf(deadPlayersCanTalkStr): true,
                StringUtils.isNotBlank(readyUpDelayStr) ? Integer.parseInt(readyUpDelayStr): 90,
                StringUtils.isNotBlank(gameStartDelayStr) ? Integer.parseInt(gameStartDelayStr): 4,
                StringUtils.isNotBlank(maxSpectatorsStr) ? Integer.parseInt(maxSpectatorsStr): 2,
                StringUtils.isNotBlank(mapObjetivesStr) ? Boolean.valueOf(mapObjetivesStr): true,
                StringUtils.isNotBlank(pickupItemsStr) ? Boolean.valueOf(pickupItemsStr): true,
                StringUtils.isNotBlank(friendlyFirePercentageStr) ? Double.parseDouble(friendlyFirePercentageStr): 0,
                StringUtils.isNotBlank(netTickrateStr) ? Integer.parseInt(netTickrateStr): 30,
                StringUtils.isNotBlank(lanTickrateStr) ? Integer.parseInt(lanTickrateStr): 35,
                StringUtils.isNotBlank(lanMaxClientRateStr) ? Integer.parseInt(lanMaxClientRateStr): 15000,
                StringUtils.isNotBlank(internetMaxClientRateStr) ? Integer.parseInt(internetMaxClientRateStr): 10000
        );

        return new GetProfileFromFileFacadeResult(
                profileFromFile
        );
    }
}
