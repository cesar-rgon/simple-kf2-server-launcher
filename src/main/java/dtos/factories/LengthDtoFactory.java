package dtos.factories;

import constants.Constants;
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
            PropertyService propertyService = new PropertyServiceImpl();
            String languageCode = Session.getInstance().getActualProfile() != null ?
                    Session.getInstance().getActualProfile().getLanguage().getKey():
                    propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_DEFAULT_LANGUAGE_CODE);

            String description = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.length." + length.getCode());
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
