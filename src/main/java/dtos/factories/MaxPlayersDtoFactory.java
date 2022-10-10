package dtos.factories;

import dtos.SelectDto;
import entities.MaxPlayers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import pojos.enums.EnumLanguage;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class MaxPlayersDtoFactory extends AbstractDtoFactory<MaxPlayers, SelectDto> {

    @Override
    public SelectDto newDto(MaxPlayers maxPlayers) {
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            EnumLanguage language = EnumLanguage.valueOf(propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode"));
            String description = StringUtils.EMPTY;
            switch (language) {
                case en: description = maxPlayers.getDescription().getEnglishValue(); break;
                case es: description = maxPlayers.getDescription().getSpanishValue(); break;
                case fr: description = maxPlayers.getDescription().getFrenchValue(); break;
            }
            return new SelectDto(maxPlayers.getCode(), description);
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public ObservableList<SelectDto> newDtos(List<MaxPlayers> maxPlayersList) {
        List<SelectDto> dtoList = maxPlayersList.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

}
