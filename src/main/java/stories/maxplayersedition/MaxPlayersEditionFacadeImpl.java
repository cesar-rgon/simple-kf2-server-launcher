package stories.maxplayersedition;

import daos.MaxPlayersDao;
import daos.ProfileDao;
import dtos.ProfileDto;
import dtos.SelectDto;
import dtos.factories.MaxPlayersDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.MaxPlayers;
import entities.Profile;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MaxPlayersEditionFacadeImpl implements MaxPlayersEditionFacade {

    private final MaxPlayersDtoFactory maxPlayersDtoFactory;
    private final PropertyService propertyService;
    private final ProfileDtoFactory profileDtoFactory;

    public MaxPlayersEditionFacadeImpl() {
        super();
        this.maxPlayersDtoFactory = new MaxPlayersDtoFactory();
        propertyService = new PropertyServiceImpl();
        profileDtoFactory = new ProfileDtoFactory();
    }

    public ObservableList<SelectDto> listAllMaxPlayers() throws SQLException {
        List<MaxPlayers> maxPlayersList = MaxPlayersDao.getInstance().listAll();
        return maxPlayersDtoFactory.newDtos(maxPlayersList);
    }

    @Override
    public SelectDto createNewMaxPlayers(String code, String description) throws Exception {
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
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
                String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
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
                String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
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
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.maxplayers." + code, newDescription);
            return maxPlayersDtoFactory.newDto(maxPlayersOpt.get());
        }
        return null;
    }

    @Override
    public ProfileDto unselectMaxPlayersInProfile(String profileName) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            profileOpt.get().setMaxPlayers(null);
            ProfileDao.getInstance().update(profileOpt.get());
            return profileDtoFactory.newDto(profileOpt.get());
        }
        return null;
    }
}
