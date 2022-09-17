package dtos.factories;

import dtos.GameTypeDto;
import entities.GameType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class GameTypeDtoFactory extends AbstractDtoFactory<GameType, GameTypeDto> {

    public GameTypeDto newDto(GameType gameType) {
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            String description = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.gametype." + gameType.getCode());
            return new GameTypeDto(gameType.getCode(), description, gameType.isDifficultyEnabled(), gameType.isLengthEnabled());
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), e);
            return null;
        }
    }

    public ObservableList<GameTypeDto> newDtos(List<GameType> gameTypes) {
        List<GameTypeDto> dtoList = gameTypes.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

}
