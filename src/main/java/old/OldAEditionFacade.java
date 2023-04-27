package old;

import daos.DescriptionDao;
import dtos.SelectDto;
import dtos.factories.AbstractDtoFactory;
import entities.AbstractExtendedEntity;
import entities.Description;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import pojos.enums.EnumLanguage;
import services.AbstractService;

import java.util.List;
import java.util.Optional;

public abstract class OldAEditionFacade<E extends AbstractExtendedEntity, D extends SelectDto> extends OldAFacade {

    private final Class<E> entityClass;
    protected final AbstractDtoFactory dtoFactory;
    protected final AbstractService<E> abstractService;

    protected OldAEditionFacade(Class<E> entityClass, AbstractDtoFactory dtoFactory, AbstractService<E> abstractService) {
        super(null);
        this.entityClass = entityClass;
        this.dtoFactory = dtoFactory;
        this.abstractService = abstractService;
    }

    public ObservableList<D> listAllItems() throws Exception {
        List<E> itemList = abstractService.listAll();
        return dtoFactory.newDtos(itemList);
    }


    public D createItem(String code, String descriptionValue, String languageCode) throws Exception {

        EnumLanguage language = EnumLanguage.valueOf(languageCode);
        Description description = new Description();
        switch (language) {
            case en: description.setEnglishValue(descriptionValue); break;
            case es: description.setSpanishValue(descriptionValue); break;
            case fr: description.setFrenchValue(descriptionValue); break;
        }
        new DescriptionDao(em).insert(description);

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
            boolean removed = abstractService.deleteItem(itemOptional.get());
            new DescriptionDao(em).remove((Description) itemOptional.get().getDescription());
            return removed;
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
            if (abstractService.updateItem(itemOptional.get())) {
                return (D) dtoFactory.newDto(itemOptional.get());
            }
        }
        return null;
    }

    public D updateItemDescription(String code, String oldDescriptionValue, String newDescriptionValue, String languageCode) throws Exception {
        if (StringUtils.isBlank(newDescriptionValue) || newDescriptionValue.equals(oldDescriptionValue)) {
            return null;
        }
        Optional<E> itemOptional = abstractService.findByCode(code);
        if (itemOptional.isPresent()) {

            EnumLanguage language = EnumLanguage.valueOf(languageCode);
            Description newDescription = new Description();
            switch (language) {
                case en: newDescription.setEnglishValue(newDescriptionValue); break;
                case es: newDescription.setSpanishValue(newDescriptionValue); break;
                case fr: newDescription.setFrenchValue(newDescriptionValue); break;
            }
            new DescriptionDao(em).insert(newDescription);

            itemOptional.get().setDescription(newDescription);
            abstractService.updateItem(itemOptional.get());

            Optional<Description> oldDescriptionOptional = new DescriptionDao(em).findByValue(oldDescriptionValue, language);
            if (oldDescriptionOptional.isPresent()) {
                new DescriptionDao(em).remove(oldDescriptionOptional.get());
            }
            return (D) dtoFactory.newDto(itemOptional.get());
        }
        return null;
    }

}