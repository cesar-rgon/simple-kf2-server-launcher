package services;

import daos.LengthDao;
import entities.Length;

public class LengthServiceImpl implements Kf2Service<Length> {

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

    @Override
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
