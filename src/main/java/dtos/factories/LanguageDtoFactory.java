package dtos.factories;

import dtos.SelectDto;
import entities.Language;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class LanguageDtoFactory {

    public SelectDto newDto(Language language) {
        return new SelectDto(language.getCode(), language.getDescription());
    }

    public ObservableList<SelectDto> newDtos(List<Language> languages) {
        List<SelectDto> dtoList = languages.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

}
