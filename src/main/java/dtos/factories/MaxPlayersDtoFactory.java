package dtos.factories;

import dtos.SelectDto;
import entities.MaxPlayers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class MaxPlayersDtoFactory {

    public SelectDto newDto(MaxPlayers maxPlayers) {
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            String description = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.maxplayers." + maxPlayers.getCode());
            return new SelectDto(maxPlayers.getCode(), description);
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), e);
            return null;
        }
    }

    public ObservableList<SelectDto> newDtos(List<MaxPlayers> maxPlayersList) {
        List<SelectDto> dtoList = maxPlayersList.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

}
