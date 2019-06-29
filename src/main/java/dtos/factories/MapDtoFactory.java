package dtos.factories;

import dtos.MapDto;
import dtos.SelectDto;
import entities.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class MapDtoFactory {

    public MapDto newDto(Map map) {
        return new MapDto(map.getCode(),
                          map.getDescription().getEnglishText(),
                          map.getOfficial(),
                          map.getUrlInfo());
    }

    public ObservableList<MapDto> newDtos(List<Map> maps) {
        List<MapDto> dtoList = maps.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

}
