package stories;

import daos.AbstractDao;
import dtos.SelectDto;
import dtos.factories.AbstractDtoFactory;
import entities.AbstractEntity;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import services.Kf2Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class AbstractEditionFacade<E extends AbstractEntity, D extends SelectDto> {

    private final Class<E> entityClass;
    protected final AbstractDao dao;
    protected final AbstractDtoFactory dtoFactory;
    protected final Kf2Service<E> kf2Service;

    protected AbstractEditionFacade(Class<E> entityClass, AbstractDao dao, AbstractDtoFactory dtoFactory, Kf2Service<E> kf2Service) {
        this.entityClass = entityClass;
        this.dao = dao;
        this.dtoFactory = dtoFactory;
        this.kf2Service = kf2Service;
    }

    public ObservableList<D> listAllItems() throws SQLException {
        List<E> itemList = dao.listAll();
        return dtoFactory.newDtos(itemList);
    }


    public D createItem(String code, String description) throws Exception {
        E item = entityClass.newInstance();
        item.setCode(code);
        item.setDescription(description);
        return (D) dtoFactory.newDto(
            kf2Service.createItem(item)
        );
    }


    public boolean deleteItem(String code) throws Exception {
        Optional<E> itemOptional = dao.findByCode(code);
        if (itemOptional.isPresent()) {
            return kf2Service.deleteItem(itemOptional.get());
        }
        return false;
    }


    public D updateItemCode(String oldCode, String newCode) throws Exception {
        if (StringUtils.isBlank(newCode) || newCode.equalsIgnoreCase(oldCode)) {
            return null;
        }
        Optional<E> itemOptional = dao.findByCode(oldCode);
        if (itemOptional.isPresent()) {
            itemOptional.get().setCode(newCode);
            if (kf2Service.updateItemCode(itemOptional.get(), oldCode)) {
                return (D) dtoFactory.newDto(itemOptional.get());
            }
        }
        return null;
    }

    public D updateItemDescription(String code, String oldDescription, String newDescription) throws Exception {
        if (StringUtils.isBlank(newDescription) || newDescription.equalsIgnoreCase(oldDescription)) {
            return null;
        }
        Optional<E> itemOptional = dao.findByCode(code);
        if (itemOptional.isPresent()) {
            itemOptional.get().setDescription(newDescription);
            kf2Service.updateItemDescription(itemOptional.get());
            return (D) dtoFactory.newDto(itemOptional.get());
        }
        return null;
    }

}
