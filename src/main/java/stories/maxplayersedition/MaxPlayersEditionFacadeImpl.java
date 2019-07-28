package stories.maxplayersedition;

import constants.Constants;
import daos.MaxPlayersDao;
import dtos.SelectDto;
import dtos.factories.MaxPlayersDtoFactory;
import entities.MaxPlayers;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import pojos.session.Session;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MaxPlayersEditionFacadeImpl implements MaxPlayersEditionFacade {

    private final MaxPlayersDtoFactory maxPlayersDtoFactory;
    private final PropertyService propertyService;

    public MaxPlayersEditionFacadeImpl() {
        super();
        this.maxPlayersDtoFactory = new MaxPlayersDtoFactory();
        propertyService = new PropertyServiceImpl();
    }

    public ObservableList<SelectDto> listAllMaxPlayers() throws SQLException {
        List<MaxPlayers> maxPlayersList = MaxPlayersDao.getInstance().listAll();
        return maxPlayersDtoFactory.newDtos(maxPlayersList);
    }

    @Override
    public SelectDto createNewMaxPlayers(String code, String description) throws Exception {
        String languageCode = Session.getInstance().getActualProfile() != null ?
                Session.getInstance().getActualProfile().getLanguage().getKey():
                propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_DEFAULT_LANGUAGE_CODE);

        propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.maxplayers." + code, description);
        MaxPlayers maxPlayers = new MaxPlayers(code);
        maxPlayers = MaxPlayersDao.getInstance().insert(maxPlayers);
        return maxPlayersDtoFactory.newDto(maxPlayers);
    }

    @Override
    public boolean deleteSelectedMaxPlayers(String code) throws Exception {
        Optional<MaxPlayers> maxPlayersOpt = MaxPlayersDao.getInstance().findByCode(code);
        if (maxPlayersOpt.isPresent()) {
            if (MaxPlayersDao.getInstance().remove(maxPlayersOpt.get())) {
                String languageCode = Session.getInstance().getActualProfile() != null ?
                        Session.getInstance().getActualProfile().getLanguage().getKey():
                        propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_DEFAULT_LANGUAGE_CODE);

                propertyService.removeProperty("properties/languages/" + languageCode + ".properties", "prop.maxplayers." + code);
                return MaxPlayersDao.getInstance().remove(maxPlayersOpt.get());
            }
        }
        return false;
    }

    @Override
    public SelectDto updateChangedMaxPlayersCode(String oldCode, String newCode) throws Exception {
        if (StringUtils.isBlank(newCode) || newCode.equalsIgnoreCase(oldCode)) {
            return null;
        }
        Optional<MaxPlayers> maxPlayersOpt = MaxPlayersDao.getInstance().findByCode(oldCode);
        if (maxPlayersOpt.isPresent()) {
            maxPlayersOpt.get().setCode(newCode);
            if (MaxPlayersDao.getInstance().update(maxPlayersOpt.get())) {
                String languageCode = Session.getInstance().getActualProfile() != null ?
                        Session.getInstance().getActualProfile().getLanguage().getKey():
                        propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_DEFAULT_LANGUAGE_CODE);

                String value = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.maxplayers." + oldCode);
                propertyService.removeProperty("properties/languages/" + languageCode + ".properties", "prop.maxplayers." + oldCode);
                propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.maxplayers." + newCode, value);
                return maxPlayersDtoFactory.newDto(maxPlayersOpt.get());
            }
        }
        return null;
    }

    @Override
    public SelectDto updateChangedMaxPlayersDescription(String code, String oldDescription, String newDescription) throws Exception {
        if (StringUtils.isBlank(newDescription) || newDescription.equalsIgnoreCase(oldDescription)) {
            return null;
        }
        Optional<MaxPlayers> maxPlayersOpt = MaxPlayersDao.getInstance().findByCode(code);
        if (maxPlayersOpt.isPresent()) {
            String languageCode = Session.getInstance().getActualProfile() != null ?
                    Session.getInstance().getActualProfile().getLanguage().getKey():
                    propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_DEFAULT_LANGUAGE_CODE);

            propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.maxplayers." + code, newDescription);
            return maxPlayersDtoFactory.newDto(maxPlayersOpt.get());
        }
        return null;
    }
}
