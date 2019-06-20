package dtos.factories;

import dtos.SelectDto;
import entities.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class MapDtoFactory {

    public SelectDto newDto(Map map) {
        return new SelectDto(map.getCode(), map.getDescription().getEnglishText());
    }

    public ObservableList<SelectDto> newDtos(List<Map> maps) {
        List<SelectDto> dtoList = maps.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

}
