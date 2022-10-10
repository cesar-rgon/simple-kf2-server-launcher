package dtos.factories;

import dtos.GameTypeDto;
import dtos.SelectDto;
import entities.GameType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import pojos.enums.EnumLanguage;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class GameTypeDtoFactory extends AbstractDtoFactory<GameType, GameTypeDto> {

    public GameTypeDto newDto(GameType gameType) {
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            EnumLanguage language = EnumLanguage.valueOf(propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode"));
            String description = StringUtils.EMPTY;
            switch (language) {
                case en: description = gameType.getDescription().getEnglishValue(); break;
                case es: description = gameType.getDescription().getSpanishValue(); break;
                case fr: description = gameType.getDescription().getFrenchValue(); break;
            }
            return new GameTypeDto(
                    gameType.getCode(),
                    description,
                    gameType.isDifficultyEnabled(),
                    gameType.isLengthEnabled());
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
