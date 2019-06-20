package dtos.factories;

import dtos.SelectDto;
import entities.GameType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class GameTypeDtoFactory {

    public SelectDto newDto(GameType gameType) {
        return new SelectDto(gameType.getCode(), gameType.getDescription().getEnglishText());
    }

    public ObservableList<SelectDto> newDtos(List<GameType> gameTypes) {
        List<SelectDto> dtoList = gameTypes.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

}
