package dtos.factories;

import dtos.SelectDto;
import entities.MaxPlayers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pojos.session.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class MaxPlayersDtoFactory {

    public SelectDto newDto(MaxPlayers maxPlayers) {
        try {
            String languageCode = Session.getInstance().getActualProfile().getLanguage().getKey();
            PropertyService propertyService = new PropertyServiceImpl();
            String description = propertyService.getPropertyValue(languageCode + ".properties", "prop.maxplayers." + maxPlayers.getCode());
            return new SelectDto(maxPlayers.getCode(), description);
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), "See stacktrace for more information", e);
            return null;
        }
    }

    public ObservableList<SelectDto> newDtos(List<MaxPlayers> maxPlayersList) {
        List<SelectDto> dtoList = maxPlayersList.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

}
