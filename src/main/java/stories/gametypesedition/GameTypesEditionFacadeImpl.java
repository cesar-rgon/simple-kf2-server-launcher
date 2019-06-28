package stories.gametypesedition;

import constants.Constants;
import daos.DescriptionDao;
import daos.GameTypeDao;
import dtos.SelectDto;
import dtos.factories.GameTypeDtoFactory;
import entities.Description;
import entities.GameType;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class GameTypesEditionFacadeImpl implements GameTypesEditionFacade {

    private final GameTypeDtoFactory gameTypeDtoFactory;

    public GameTypesEditionFacadeImpl() {
        super();
        gameTypeDtoFactory = new GameTypeDtoFactory();
    }

    @Override
    public ObservableList<SelectDto> listAllGameTypes() throws SQLException {
        List<GameType> gameTypes = GameTypeDao.getInstance().listAll();
        return gameTypeDtoFactory.newDtos(gameTypes);
    }

    @Override
    public SelectDto createNewGameType(String code, String description, SelectDto selectedLanguage) throws SQLException {
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
        GameType gameType = new GameType(code, descriptionEntity);
        gameType = GameTypeDao.getInstance().insert(gameType);
        return gameTypeDtoFactory.newDto(gameType);
    }

    @Override
    public boolean deleteSelectedGameType(String code) throws SQLException {
        Optional<GameType> gameTypeOpt = GameTypeDao.getInstance().findByCode(code);
        if (gameTypeOpt.isPresent()) {
            Description description = gameTypeOpt.get().getDescription();
            if (GameTypeDao.getInstance().remove(gameTypeOpt.get())) {
                return DescriptionDao.getInstance().remove(description);
            }
        }
        return false;
    }

    @Override
    public SelectDto updateChangedGameTypeCode(String oldCode, String newCode) throws SQLException {
        if (StringUtils.isBlank(newCode) || newCode.equalsIgnoreCase(oldCode)) {
            return null;
        }
        Optional<GameType> gameTypeOpt = GameTypeDao.getInstance().findByCode(oldCode);
        if (gameTypeOpt.isPresent()) {
            gameTypeOpt.get().setCode(newCode);
            if (GameTypeDao.getInstance().update(gameTypeOpt.get())) {
                return gameTypeDtoFactory.newDto(gameTypeOpt.get());
            }
        }
        return null;
    }

    @Override
    public SelectDto updateChangedGameTypeDescription(String code, String oldDescription, String newDescription, SelectDto selectedLanguage) throws SQLException {
        if (StringUtils.isBlank(newDescription) || newDescription.equalsIgnoreCase(oldDescription)) {
            return null;
        }
        Optional<GameType> gameTypeOpt = GameTypeDao.getInstance().findByCode(code);
        if (gameTypeOpt.isPresent()) {
            Description descriptionEntity = gameTypeOpt.get().getDescription();
            if ("en".equals(selectedLanguage.getKey())) {
                descriptionEntity.setEnglishText(newDescription);
            } else {
                descriptionEntity.setSpanishText(newDescription);
            }
            if (DescriptionDao.getInstance().update(descriptionEntity)) {
                return gameTypeDtoFactory.newDto(gameTypeOpt.get());
            }
        }
        return null;
    }
}
