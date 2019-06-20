package dtos.factories;

import dtos.SelectDto;
import entities.Length;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class LengthDtoFactory {

    public SelectDto newDto(Length length) {
        return new SelectDto(length.getCode(), length.getDescription().getEnglishText());
    }

    public ObservableList<SelectDto> newDtos(List<Length> lengths) {
        List<SelectDto> dtoList = lengths.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

}
