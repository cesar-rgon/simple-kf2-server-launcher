package dtos.factories;

import dtos.MapDto;
import dtos.ProfileDto;
import dtos.SelectDto;
import entities.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapDtoFactory {

    public MapDto newDto(Map map) {
        return new MapDto(map.getCode(),
                          map.isOfficial(),
                          map.getUrlInfo(),
                          map.getIdWorkShop(),
                          map.getUrlPhoto(),
                          map.isDownloaded(),
                          map.getMod());
    }

    public ObservableList<MapDto> newDtos(List<Map> maps) {
        List<MapDto> dtoList = maps.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

}
