package services;

import daos.MaxPlayersDao;
import entities.MaxPlayers;

public class MaxPlayersServiceImpl implements AbstractExtendedService<MaxPlayers> {

    private PropertyService propertyService;

    public MaxPlayersServiceImpl() {
        super();
        this.propertyService = new PropertyServiceImpl();
    }


    @Override
    public MaxPlayers createItem(MaxPlayers maxPlayers) throws Exception {
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.maxplayers." + maxPlayers.getCode(), maxPlayers.getDescription());
        return MaxPlayersDao.getInstance().insert(maxPlayers);
    }

    @Override
    public boolean updateItemCode(MaxPlayers maxPlayers, String oldCode) throws Exception {
        if (MaxPlayersDao.getInstance().update(maxPlayers)) {
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            String value = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.maxplayers." + oldCode);
            propertyService.removeProperty("properties/languages/" + languageCode + ".properties", "prop.maxplayers." + oldCode);
            propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.maxplayers." + maxPlayers.getCode(), value);
            return true;
        }
        return false;
    }

    public void updateItemDescription(MaxPlayers maxPlayers) throws Exception {
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.maxplayers." + maxPlayers.getCode(), maxPlayers.getDescription());
    }

    @Override
    public boolean deleteItem(MaxPlayers maxPlayers) throws Exception {
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        propertyService.removeProperty("properties/languages/" + languageCode + ".properties", "prop.maxplayers." + maxPlayers.getCode());
        return MaxPlayersDao.getInstance().remove(maxPlayers);
    }
}
