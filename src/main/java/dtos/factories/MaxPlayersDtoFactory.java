package dtos.factories;

import dtos.SelectDto;
import entities.MaxPlayers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class MaxPlayersDtoFactory {

    public SelectDto newDto(MaxPlayers maxPlayers) {
        return new SelectDto(maxPlayers.getCode(), maxPlayers.getDescription().getEnglishText());
    }

    public ObservableList<SelectDto> newDtos(List<MaxPlayers> maxPlayersList) {
        List<SelectDto> dtoList = maxPlayersList.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

}
