package dtos.factories;

import dtos.PropertyDto;
import entities.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class PropertyDtoFactory {

    public PropertyDtoFactory() {
        super();
    }

    public PropertyDto newDto(Property property) {
        return new PropertyDto(property.getKey(), property.getValue());
    }

    public ObservableList<PropertyDto> newDtos(List<Property> properties) {
        List<PropertyDto> dtoList = properties.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }
}
