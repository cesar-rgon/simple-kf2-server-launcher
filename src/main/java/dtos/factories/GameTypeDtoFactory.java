package dtos.factories;

import dtos.SelectDto;
import entities.GameType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pojos.session.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class GameTypeDtoFactory {

    public SelectDto newDto(GameType gameType) {
        try {
            String languageCode = Session.getInstance().getActualProfile().getLanguage().getKey();
            PropertyService propertyService = new PropertyServiceImpl();
            String description = propertyService.getPropertyValue(languageCode + ".properties", "prop.gametype." + gameType.getCode());
            return new SelectDto(gameType.getCode(), description);
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), "See stacktrace for more information", e);
            return null;
        }
    }

    public ObservableList<SelectDto> newDtos(List<GameType> gameTypes) {
        List<SelectDto> dtoList = gameTypes.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

}
