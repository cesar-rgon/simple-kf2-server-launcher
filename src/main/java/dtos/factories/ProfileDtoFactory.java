package dtos.factories;

import dtos.ProfileDto;
import entities.Profile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class ProfileDtoFactory {

    private final LanguageDtoFactory languageDtoFactory;
    private final GameTypeDtoFactory gameTypeDtoFactory;
    private final MapDtoFactory mapDtoFactory;
    private final DifficultyDtoFactory difficultyDtoFactory;
    private final LengthDtoFactory lengthDtoFactory;
    private final MaxPlayersDtoFactory maxPlayersDtoFactory;

    public ProfileDtoFactory() {
        super();
        languageDtoFactory = new LanguageDtoFactory();
        gameTypeDtoFactory = new GameTypeDtoFactory();
        mapDtoFactory = new MapDtoFactory();
        difficultyDtoFactory = new DifficultyDtoFactory();
        lengthDtoFactory = new LengthDtoFactory();
        maxPlayersDtoFactory = new MaxPlayersDtoFactory();
    }

    public ProfileDto newDto(Profile profile) {
        return new ProfileDto(profile.getName(),
                languageDtoFactory.newDto(profile.getLanguage()),
                gameTypeDtoFactory.newDto(profile.getGametype()),
                mapDtoFactory.newDto(profile.getMap()),
                difficultyDtoFactory.newDto(profile.getDifficulty()),
                lengthDtoFactory.newDto(profile.getLength()),
                maxPlayersDtoFactory.newDto(profile.getMaxPlayers()),
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
                profile.getCustomParameters());
    }

    public ObservableList<ProfileDto> newDtos(List<Profile> profiles) {
        List<ProfileDto> dtoList = profiles.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

}
