package dtos.factories;

import dtos.SelectDto;
import entities.Length;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pojos.session.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class LengthDtoFactory {

    public SelectDto newDto(Length length) {
        try {
            String languageCode = Session.getInstance().getActualProfile().getLanguage().getKey();
            PropertyService propertyService = new PropertyServiceImpl();
            String description = propertyService.getPropertyValue(languageCode + ".properties", "prop.length." + length.getCode());
            return new SelectDto(length.getCode(), description);
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), "See stacktrace for more information", e);
            return null;
        }
    }

    public ObservableList<SelectDto> newDtos(List<Length> lengths) {
        List<SelectDto> dtoList = lengths.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

}
