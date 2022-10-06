package services;

import daos.DifficultyDao;
import entities.Difficulty;

import java.util.List;
import java.util.Optional;

public class DifficultyServiceImpl implements AbstractExtendedService<Difficulty> {

    private PropertyService propertyService;

    public DifficultyServiceImpl() {
        super();
        this.propertyService = new PropertyServiceImpl();
    }


    @Override
    public Difficulty createItem(Difficulty difficulty) throws Exception {
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.difficulty." + difficulty.getCode(), difficulty.getDescription());
        return DifficultyDao.getInstance().insert(difficulty);
    }

    @Override
    public boolean updateItem(Difficulty difficulty) throws Exception {
        return DifficultyDao.getInstance().update(difficulty);
    }

    @Override
    public List<Difficulty> listAll() throws Exception {
        return DifficultyDao.getInstance().listAll();
    }

    @Override
    public Optional<Difficulty> findByCode(String code) throws Exception {
        return DifficultyDao.getInstance().findByCode(code);
    }


    @Override
    public boolean updateItemCode(Difficulty difficulty, String oldCode) throws Exception {
        if (DifficultyDao.getInstance().update(difficulty)) {
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            String value = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.difficulty." + oldCode);
            propertyService.removeProperty("properties/languages/" + languageCode + ".properties", "prop.difficulty." + oldCode);
            propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.difficulty." + difficulty.getCode(), value);
            return true;
        }
        return false;
    }

    @Override
    public void updateItemDescription(Difficulty difficulty) throws Exception {
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.difficulty." + difficulty.getCode(), difficulty.getDescription());
    }

    @Override
    public boolean deleteItem(Difficulty difficulty) throws Exception {
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        propertyService.removeProperty("properties/languages/" + languageCode + ".properties", "prop.difficulty." + difficulty.getCode());
        return DifficultyDao.getInstance().remove(difficulty);
    }

}
