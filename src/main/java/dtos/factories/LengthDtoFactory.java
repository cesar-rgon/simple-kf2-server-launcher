package dtos.factories;

import dtos.SelectDto;
import entities.Length;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import pojos.enums.EnumLanguage;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class LengthDtoFactory extends AbstractDtoFactory<Length, SelectDto> {

    @Override
    public SelectDto newDto(Length length) {
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            EnumLanguage language = EnumLanguage.valueOf(propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode"));
            String description = StringUtils.EMPTY;
            switch (language) {
                case en: description = length.getDescription().getEnglishValue(); break;
                case es: description = length.getDescription().getSpanishValue(); break;
                case fr: description = length.getDescription().getFrenchValue(); break;
                default: description = length.getDescription().getRussianValue(); break;
            }
            return new SelectDto(length.getCode(), description);
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public ObservableList<SelectDto> newDtos(List<Length> lengths) {
        List<SelectDto> dtoList = lengths.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

}
