package stories.maxplayersedition;

import constants.Constants;
import daos.MaxPlayersDao;
import dtos.SelectDto;
import dtos.factories.MaxPlayersDtoFactory;
import entities.MaxPlayers;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MaxPlayersEditionFacadeImpl implements MaxPlayersEditionFacade {

    private final MaxPlayersDtoFactory maxPlayersDtoFactory;

    public MaxPlayersEditionFacadeImpl() {
        super();
        this.maxPlayersDtoFactory = new MaxPlayersDtoFactory();
    }

    public ObservableList<SelectDto> listAllMaxPlayers() throws SQLException {
        List<MaxPlayers> maxPlayersList = MaxPlayersDao.getInstance().listAll();
        return maxPlayersDtoFactory.newDtos(maxPlayersList);
    }

    @Override
    public SelectDto createNewMaxPlayers(String code, String description, SelectDto selectedLanguage) throws SQLException {
        String englishText = null;
        String spanishText = null;
        if ("en".equals(selectedLanguage.getKey())) {
            englishText = description;
            spanishText = Constants.DESCRIPTION_DEFAULT_TEXT;
        } else {
            englishText = Constants.DESCRIPTION_DEFAULT_TEXT;
            spanishText = description;
        }
        Description descriptionEntity = new Description(englishText, spanishText);
        descriptionEntity = DescriptionDao.getInstance().insert(descriptionEntity);
        MaxPlayers maxPlayers = new MaxPlayers(code, descriptionEntity);
        maxPlayers = MaxPlayersDao.getInstance().insert(maxPlayers);
        return maxPlayersDtoFactory.newDto(maxPlayers);
    }

    @Override
    public boolean deleteSelectedMaxPlayers(String code) throws SQLException {
        Optional<MaxPlayers> maxPlayersOpt = MaxPlayersDao.getInstance().findByCode(code);
        if (maxPlayersOpt.isPresent()) {
            Description description = maxPlayersOpt.get().getDescription();
            if (MaxPlayersDao.getInstance().remove(maxPlayersOpt.get())) {
                return DescriptionDao.getInstance().remove(description);
            }
        }
        return false;
    }

    @Override
    public SelectDto updateChangedMaxPlayersCode(String oldCode, String newCode) throws SQLException {
        if (StringUtils.isBlank(newCode) || newCode.equalsIgnoreCase(oldCode)) {
            return null;
        }
        Optional<MaxPlayers> maxPlayersOpt = MaxPlayersDao.getInstance().findByCode(oldCode);
        if (maxPlayersOpt.isPresent()) {
            maxPlayersOpt.get().setCode(newCode);
            if (MaxPlayersDao.getInstance().update(maxPlayersOpt.get())) {
                return maxPlayersDtoFactory.newDto(maxPlayersOpt.get());
            }
        }
        return null;
    }

    @Override
    public SelectDto updateChangedMaxPlayersDescription(String code, String oldDescription, String newDescription, SelectDto selectedLanguage) throws SQLException {
        if (StringUtils.isBlank(newDescription) || newDescription.equalsIgnoreCase(oldDescription)) {
            return null;
        }
        Optional<MaxPlayers> maxPlayersOpt = MaxPlayersDao.getInstance().findByCode(code);
        if (maxPlayersOpt.isPresent()) {
            Description descriptionEntity = maxPlayersOpt.get().getDescription();
            if ("en".equals(selectedLanguage.getKey())) {
                descriptionEntity.setEnglishText(newDescription);
            } else {
                descriptionEntity.setSpanishText(newDescription);
            }
            if (DescriptionDao.getInstance().update(descriptionEntity)) {
                return maxPlayersDtoFactory.newDto(maxPlayersOpt.get());
            }
        }
        return null;
    }
}
