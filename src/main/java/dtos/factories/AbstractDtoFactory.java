package dtos.factories;

import dtos.SelectDto;
import entities.AbstractEntity;
import javafx.collections.ObservableList;

import java.util.List;

public abstract class AbstractDtoFactory<E extends AbstractEntity, D extends SelectDto> {

    public abstract D newDto(E entity);
    public abstract ObservableList<D> newDtos(List<E> entityList);

}
