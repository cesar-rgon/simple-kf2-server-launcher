package dtos.factories;

import dtos.SelectDto;
import entities.Difficulty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class DifficultyDtoFactory {

    public SelectDto newDto(Difficulty difficulty) {
        return new SelectDto(difficulty.getCode(), difficulty.getDescription().getEnglishText());
    }

    public ObservableList<SelectDto> newDtos(List<Difficulty> difficulties) {
        List<SelectDto> dtoList = difficulties.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }
}
