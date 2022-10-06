package services;

import daos.GameTypeDao;
import entities.GameType;

import java.util.List;
import java.util.Optional;

public class GameTypeServiceImpl implements AbstractExtendedService<GameType> {

    private PropertyService propertyService;

    public GameTypeServiceImpl() {
        super();
        this.propertyService = new PropertyServiceImpl();
    }


    @Override
    public GameType createItem(GameType gameType) throws Exception {
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.gametype." + gameType.getCode(), gameType.getDescription());
        return GameTypeDao.getInstance().insert(gameType);
    }

    @Override
    public boolean updateItem(GameType gameType) throws Exception {
        return GameTypeDao.getInstance().update(gameType);
    }

    @Override
    public List<GameType> listAll() throws Exception {
        return GameTypeDao.getInstance().listAll();
    }

    @Override
    public Optional<GameType> findByCode(String code) throws Exception {
        return GameTypeDao.getInstance().findByCode(code);
    }

    @Override
    public boolean updateItemCode(GameType gameType, String oldCode) throws Exception {
        if (GameTypeDao.getInstance().update(gameType)) {
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            String value = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.gametype." + oldCode);
            propertyService.removeProperty("properties/languages/" + languageCode + ".properties", "prop.gametype." + oldCode);
            propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.gametype." + gameType.getCode(), value);
            return true;
        }
        return false;
    }

    public void updateItemDescription(GameType gameType) throws Exception {
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.gametype." + gameType.getCode(), gameType.getDescription());
    }

    @Override
    public boolean deleteItem(GameType gameType) throws Exception {
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        propertyService.removeProperty("properties/languages/" + languageCode + ".properties", "prop.gametype." + gameType.getCode());
        return GameTypeDao.getInstance().remove(gameType);
    }
}
