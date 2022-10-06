package stories;

import daos.AbstractExtendedDao;
import dtos.SelectDto;
import dtos.factories.AbstractDtoFactory;
import entities.AbstractExtendedEntity;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import services.AbstractExtendedService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class AbstractEditionFacade<E extends AbstractExtendedEntity, D extends SelectDto> {

    private final Class<E> entityClass;
    protected final AbstractDtoFactory dtoFactory;
    protected final AbstractExtendedService<E> abstractService;

    protected AbstractEditionFacade(Class<E> entityClass, AbstractDtoFactory dtoFactory, AbstractExtendedService<E> abstractService) {
        this.entityClass = entityClass;
        this.dtoFactory = dtoFactory;
        this.abstractService = abstractService;
    }

    public ObservableList<D> listAllItems() throws Exception {
        List<E> itemList = abstractService.listAll();
        return dtoFactory.newDtos(itemList);
    }


    public D createItem(String code, String description) throws Exception {
        E item = entityClass.newInstance();
        item.setCode(code);
        item.setDescription(description);
        return (D) dtoFactory.newDto(
            abstractService.createItem(item)
        );
    }


    public boolean deleteItem(String code) throws Exception {
        Optional<E> itemOptional = abstractService.findByCode(code);
        if (itemOptional.isPresent()) {
            return abstractService.deleteItem(itemOptional.get());
        }
        return false;
    }


    public D updateItemCode(String oldCode, String newCode) throws Exception {
        if (StringUtils.isBlank(newCode) || newCode.equalsIgnoreCase(oldCode)) {
            return null;
        }
        Optional<E> itemOptional = abstractService.findByCode(oldCode);
        if (itemOptional.isPresent()) {
            itemOptional.get().setCode(newCode);
            if (abstractService.updateItemCode(itemOptional.get(), oldCode)) {
                return (D) dtoFactory.newDto(itemOptional.get());
            }
        }
        return null;
    }

    public D updateItemDescription(String code, String oldDescription, String newDescription) throws Exception {
        if (StringUtils.isBlank(newDescription) || newDescription.equalsIgnoreCase(oldDescription)) {
            return null;
        }
        Optional<E> itemOptional = abstractService.findByCode(code);
        if (itemOptional.isPresent()) {
            itemOptional.get().setDescription(newDescription);
            abstractService.updateItemDescription(itemOptional.get());
            return (D) dtoFactory.newDto(itemOptional.get());
        }
        return null;
    }

}
