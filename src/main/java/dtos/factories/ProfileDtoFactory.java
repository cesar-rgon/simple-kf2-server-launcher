package dtos.factories;

import dtos.AbstractMapDto;
import dtos.ProfileDto;
import entities.AbstractMap;
import entities.Profile;
import jakarta.persistence.EntityManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProfileDtoFactory {

    private final LanguageDtoFactory languageDtoFactory;
    private final GameTypeDtoFactory gameTypeDtoFactory;
    private final MapDtoFactory mapDtoFactory;
    private final DifficultyDtoFactory difficultyDtoFactory;
    private final LengthDtoFactory lengthDtoFactory;
    private final MaxPlayersDtoFactory maxPlayersDtoFactory;

    public ProfileDtoFactory(EntityManager em) {
        super();
        languageDtoFactory = new LanguageDtoFactory();
        gameTypeDtoFactory = new GameTypeDtoFactory();
        mapDtoFactory = new MapDtoFactory(em);
        difficultyDtoFactory = new DifficultyDtoFactory();
        lengthDtoFactory = new LengthDtoFactory();
        maxPlayersDtoFactory = new MaxPlayersDtoFactory();
    }

    public ProfileDto newDto(Profile profile) {

        return new ProfileDto(profile.getCode(),
                languageDtoFactory.newDto(profile.getLanguage()),
                profile.getGametype() != null ? gameTypeDtoFactory.newDto(profile.getGametype()): null,
                profile.getMap() != null ? mapDtoFactory.newDto(profile.getMap()): null,
                profile.getDifficulty() != null ? difficultyDtoFactory.newDto(profile.getDifficulty()): null,
                profile.getLength() != null ? lengthDtoFactory.newDto(profile.getLength()): null,
                profile.getMaxPlayers() != null ? maxPlayersDtoFactory.newDto(profile.getMaxPlayers()): null,
                profile.getServerName(),
                profile.getServerPassword(),
                profile.getWebPage(),
                profile.getWebPassword(),
                profile.getWebPort(),
                profile.getGamePort(),
                profile.getQueryPort(),
                profile.getYourClan(),
                profile.getYourWebLink(),
                profile.getUrlImageServer(),
                profile.getWelcomeMessage(),
                profile.getCustomParameters(),
                profile.getTakeover(),
                profile.getTeamCollision(),
                profile.getAdminCanPause(),
                profile.getAnnounceAdminLogin(),
                profile.getMapVoting(),
                profile.getMapVotingTime(),
                profile.getKickVoting(),
                profile.getKickPercentage(),
                profile.getPublicTextChat(),
                profile.getSpectatorsOnlyChatToOtherSpectators(),
                profile.getVoip(),
                profile.getChatLogging(),
                profile.getChatLoggingFile(),
                profile.getChatLoggingFileTimestamp(),
                profile.getTimeBetweenKicks(),
                profile.getMaxIdleTime(),
                profile.getDeadPlayersCanTalk(),
                profile.getReadyUpDelay(),
                profile.getGameStartDelay(),
                profile.getMaxSpectators(),
                profile.getMapObjetives(),
                profile.getPickupItems(),
                profile.getFriendlyFirePercentage(),
                profile.getNetTickrate(),
                profile.getLanTickrate(),
                profile.getLanMaxClientRate(),
                profile.getInternetMaxClientRate()
        );
    }

    public ObservableList<ProfileDto> newDtos(List<Profile> profiles) {
        List<ProfileDto> dtoList = profiles.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

}
