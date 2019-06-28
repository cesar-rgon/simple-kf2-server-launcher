package stories.difficultiesedition;

import constants.Constants;
import daos.DescriptionDao;
import daos.DifficultyDao;
import dtos.SelectDto;
import dtos.factories.DifficultyDtoFactory;
import entities.Description;
import entities.Difficulty;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DifficultiesEditionFacadeImpl implements DifficultiesEditionFacade {

    private final DifficultyDtoFactory difficultyDtoFactory;

    public DifficultiesEditionFacadeImpl() {
        super();
        this.difficultyDtoFactory = new DifficultyDtoFactory();
    }

    @Override
    public ObservableList<SelectDto> listAllDifficulties() throws SQLException {
        List<Difficulty> difficulties = DifficultyDao.getInstance().listAll();
        return difficultyDtoFactory.newDtos(difficulties);
    }

    @Override
    public SelectDto createNewDifficulty(String code, String description, SelectDto selectedLanguage) throws SQLException {
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
        Difficulty difficulty = new Difficulty(code, descriptionEntity);
        difficulty = DifficultyDao.getInstance().insert(difficulty);
        return difficultyDtoFactory.newDto(difficulty);
    }

    @Override
    public boolean deleteSelectedDifficulty(String code) throws SQLException {
        Optional<Difficulty> difficultyOpt = DifficultyDao.getInstance().findByCode(code);
        if (difficultyOpt.isPresent()) {
            Description description = difficultyOpt.get().getDescription();
            if (DifficultyDao.getInstance().remove(difficultyOpt.get())) {
                return DescriptionDao.getInstance().remove(description);
            }
        }
        return false;
    }

    @Override
    public SelectDto updateChangedDifficultyCode(String oldCode, String newCode) throws SQLException {
        if (StringUtils.isBlank(newCode) || newCode.equalsIgnoreCase(oldCode)) {
            return null;
        }
        Optional<Difficulty> difficultyOpt = DifficultyDao.getInstance().findByCode(oldCode);
        if (difficultyOpt.isPresent()) {
            difficultyOpt.get().setCode(newCode);
            if (DifficultyDao.getInstance().update(difficultyOpt.get())) {
                return difficultyDtoFactory.newDto(difficultyOpt.get());
            }
        }
        return null;
    }

    @Override
    public SelectDto updateChangedDifficultyDescription(String code, String oldDescription, String newDescription, SelectDto selectedLanguage) throws SQLException {
        if (StringUtils.isBlank(newDescription) || newDescription.equalsIgnoreCase(oldDescription)) {
            return null;
        }
        Optional<Difficulty> difficultyOpt = DifficultyDao.getInstance().findByCode(code);
        if (difficultyOpt.isPresent()) {
            Description descriptionEntity = difficultyOpt.get().getDescription();
            if ("en".equals(selectedLanguage.getKey())) {
                descriptionEntity.setEnglishText(newDescription);
            } else {
                descriptionEntity.setSpanishText(newDescription);
            }
            if (DescriptionDao.getInstance().update(descriptionEntity)) {
                return difficultyDtoFactory.newDto(difficultyOpt.get());
            }
        }
        return null;
    }
}
