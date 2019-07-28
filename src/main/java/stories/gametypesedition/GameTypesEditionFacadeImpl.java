package stories.gametypesedition;

import constants.Constants;
import daos.GameTypeDao;
import dtos.SelectDto;
import dtos.factories.GameTypeDtoFactory;
import entities.GameType;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import pojos.session.Session;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class GameTypesEditionFacadeImpl implements GameTypesEditionFacade {

    private final GameTypeDtoFactory gameTypeDtoFactory;
    private final PropertyService propertyService;

    public GameTypesEditionFacadeImpl() {
        super();
        gameTypeDtoFactory = new GameTypeDtoFactory();
        propertyService = new PropertyServiceImpl();
    }

    @Override
    public ObservableList<SelectDto> listAllGameTypes() throws SQLException {
        List<GameType> gameTypes = GameTypeDao.getInstance().listAll();
        return gameTypeDtoFactory.newDtos(gameTypes);
    }

    @Override
    public SelectDto createNewGameType(String code, String description) throws Exception {
        String languageCode = Session.getInstance().getActualProfile() != null ?
                Session.getInstance().getActualProfile().getLanguage().getKey():
                propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_DEFAULT_LANGUAGE_CODE);

        propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.gametype." + code, description);
        GameType gameType = new GameType(code);
        gameType = GameTypeDao.getInstance().insert(gameType);
        return gameTypeDtoFactory.newDto(gameType);
    }

    @Override
    public boolean deleteSelectedGameType(String code) throws Exception {
        Optional<GameType> gameTypeOpt = GameTypeDao.getInstance().findByCode(code);
        if (gameTypeOpt.isPresent()) {
            String languageCode = Session.getInstance().getActualProfile() != null ?
                    Session.getInstance().getActualProfile().getLanguage().getKey():
                    propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_DEFAULT_LANGUAGE_CODE);

            propertyService.removeProperty("properties/languages/" + languageCode + ".properties", "prop.gametype." + code);
            return GameTypeDao.getInstance().remove(gameTypeOpt.get());
        }
        return false;
    }

    @Override
    public SelectDto updateChangedGameTypeCode(String oldCode, String newCode) throws Exception {
        if (StringUtils.isBlank(newCode) || newCode.equalsIgnoreCase(oldCode)) {
            return null;
        }
        Optional<GameType> gameTypeOpt = GameTypeDao.getInstance().findByCode(oldCode);
        if (gameTypeOpt.isPresent()) {
            gameTypeOpt.get().setCode(newCode);
            if (GameTypeDao.getInstance().update(gameTypeOpt.get())) {
                String languageCode = Session.getInstance().getActualProfile() != null ?
                        Session.getInstance().getActualProfile().getLanguage().getKey():
                        propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_DEFAULT_LANGUAGE_CODE);

                String value = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.gametype." + oldCode);
                propertyService.removeProperty("properties/languages/" + languageCode + ".properties", "prop.gametype." + oldCode);
                propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.gametype." + newCode, value);
                return gameTypeDtoFactory.newDto(gameTypeOpt.get());
            }
        }
        return null;
    }

    @Override
    public SelectDto updateChangedGameTypeDescription(String code, String oldDescription, String newDescription) throws Exception {
        if (StringUtils.isBlank(newDescription) || newDescription.equalsIgnoreCase(oldDescription)) {
            return null;
        }
        Optional<GameType> gameTypeOpt = GameTypeDao.getInstance().findByCode(code);
        if (gameTypeOpt.isPresent()) {
            String languageCode = Session.getInstance().getActualProfile() != null ?
                    Session.getInstance().getActualProfile().getLanguage().getKey():
                    propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_DEFAULT_LANGUAGE_CODE);

            propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.gametype." + code, newDescription);
            return gameTypeDtoFactory.newDto(gameTypeOpt.get());
        }
        return null;
    }
}
