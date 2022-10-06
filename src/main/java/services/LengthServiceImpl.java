package services;

import daos.LengthDao;
import entities.Length;

import java.util.List;
import java.util.Optional;

public class LengthServiceImpl implements AbstractExtendedService<Length> {

    private PropertyService propertyService;

    public LengthServiceImpl() {
        super();
        this.propertyService = new PropertyServiceImpl();
    }

    @Override
    public Length createItem(Length length) throws Exception {
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.length." + length.getCode(), length.getDescription());
        return LengthDao.getInstance().insert(length);
    }

    @Override
    public boolean updateItem(Length length) throws Exception {
        return LengthDao.getInstance().update(length);
    }

    @Override
    public List<Length> listAll() throws Exception {
        return LengthDao.getInstance().listAll();
    }

    @Override
    public Optional<Length> findByCode(String code) throws Exception {
        return LengthDao.getInstance().findByCode(code);
    }

    @Override
    public boolean updateItemCode(Length length, String oldCode) throws Exception {
        if (LengthDao.getInstance().update(length)) {
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            String value = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.length." + oldCode);
            propertyService.removeProperty("properties/languages/" + languageCode + ".properties", "prop.length." + oldCode);
            propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.length." + length.getCode(), value);
            return true;
        }
        return false;
    }

    public void updateItemDescription(Length length) throws Exception {
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.length." + length.getCode(), length.getDescription());
    }

    @Override
    public boolean deleteItem(Length length) throws Exception {
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        propertyService.removeProperty("properties/languages/" + languageCode + ".properties", "prop.length." + length.getCode());
        return LengthDao.getInstance().remove(length);
    }
}
