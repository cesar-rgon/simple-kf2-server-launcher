package stories.difficultiesedition;

import daos.DifficultyDao;
import daos.ProfileDao;
import dtos.ProfileDto;
import dtos.SelectDto;
import dtos.factories.DifficultyDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.Difficulty;
import entities.Profile;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DifficultiesEditionFacadeImpl implements DifficultiesEditionFacade {

    private final DifficultyDtoFactory difficultyDtoFactory;
    private final PropertyService propertyService;
    private final ProfileDtoFactory profileDtoFactory;

    public DifficultiesEditionFacadeImpl() {
        super();
        this.difficultyDtoFactory = new DifficultyDtoFactory();
        propertyService = new PropertyServiceImpl();
        profileDtoFactory = new ProfileDtoFactory();
    }

    @Override
    public ObservableList<SelectDto> listAllDifficulties() throws SQLException {
        List<Difficulty> difficulties = DifficultyDao.getInstance().listAll();
        return difficultyDtoFactory.newDtos(difficulties);
    }

    @Override
    public SelectDto createNewDifficulty(String code, String description) throws Exception {
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.difficulty." + code, description);
        Difficulty difficulty = new Difficulty(code);
        difficulty = DifficultyDao.getInstance().insert(difficulty);
        return difficultyDtoFactory.newDto(difficulty);
    }

    @Override
    public boolean deleteSelectedDifficulty(String code) throws Exception {
        Optional<Difficulty> difficultyOpt = DifficultyDao.getInstance().findByCode(code);
        if (difficultyOpt.isPresent()) {
            if (DifficultyDao.getInstance().remove(difficultyOpt.get())) {
                String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
                propertyService.removeProperty("properties/languages/" + languageCode + ".properties", "prop.difficulty." + code);
                return DifficultyDao.getInstance().remove(difficultyOpt.get());
            }
        }
        return false;
    }

    @Override
    public SelectDto updateChangedDifficultyCode(String oldCode, String newCode) throws Exception {
        if (StringUtils.isBlank(newCode) || newCode.equalsIgnoreCase(oldCode)) {
            return null;
        }
        Optional<Difficulty> difficultyOpt = DifficultyDao.getInstance().findByCode(oldCode);
        if (difficultyOpt.isPresent()) {
            difficultyOpt.get().setCode(newCode);
            if (DifficultyDao.getInstance().update(difficultyOpt.get())) {
                String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
                String value = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.difficulty." + oldCode);
                propertyService.removeProperty("properties/languages/" + languageCode + ".properties", "prop.difficulty." + oldCode);
                propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.difficulty." + newCode, value);
                return difficultyDtoFactory.newDto(difficultyOpt.get());
            }
        }
        return null;
    }

    @Override
    public SelectDto updateChangedDifficultyDescription(String code, String oldDescription, String newDescription) throws Exception {
        if (StringUtils.isBlank(newDescription) || newDescription.equalsIgnoreCase(oldDescription)) {
            return null;
        }
        Optional<Difficulty> difficultyOpt = DifficultyDao.getInstance().findByCode(code);
        if (difficultyOpt.isPresent()) {
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.difficulty." + code, newDescription);
            return difficultyDtoFactory.newDto(difficultyOpt.get());
        }
        return null;
    }

    @Override
    public ProfileDto unselectDifficultyInProfile(String profileName) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            profileOpt.get().setDifficulty(null);
            ProfileDao.getInstance().update(profileOpt.get());
            return profileDtoFactory.newDto(profileOpt.get());
        }
        return null;
    }
}
