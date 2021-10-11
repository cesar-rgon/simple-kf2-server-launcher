package dtos.factories;

import dtos.SelectDto;
import entities.Difficulty;
import entities.Language;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class LanguageDtoFactory extends AbstractDtoFactory<Language, SelectDto> {

    @Override
    public SelectDto newDto(Language language) {
        return new SelectDto(language.getCode(), language.getDescription());
    }

    @Override
    public ObservableList<SelectDto> newDtos(List<Language> languages) {
        List<SelectDto> dtoList = languages.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

}
