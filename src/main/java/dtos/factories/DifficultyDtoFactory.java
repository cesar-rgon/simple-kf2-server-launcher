package dtos.factories;

import dtos.SelectDto;
import entities.Difficulty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class DifficultyDtoFactory extends AbstractDtoFactory<Difficulty, SelectDto> {

    @Override
    public SelectDto newDto(Difficulty difficulty) {
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            String description = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.difficulty." + difficulty.getCode());
            return new SelectDto(difficulty.getCode(), description);
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public ObservableList<SelectDto> newDtos(List<Difficulty> difficulties) {
        List<SelectDto> dtoList = difficulties.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }
}
