package stories.lengthedition;

import constants.Constants;
import daos.DescriptionDao;
import daos.LengthDao;
import dtos.SelectDto;
import dtos.factories.LengthDtoFactory;
import entities.Description;
import entities.Length;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LengthEditionFacadeImpl implements LengthEditionFacade {

    private final LengthDtoFactory lengthDtoFactory;

    public LengthEditionFacadeImpl() {
        super();
        this.lengthDtoFactory = new LengthDtoFactory();
    }

    public ObservableList<SelectDto> listAllLength() throws SQLException {
        List<Length> lengthList = LengthDao.getInstance().listAll();
        return lengthDtoFactory.newDtos(lengthList);
    }

    @Override
    public SelectDto createNewLength(String code, String description, SelectDto selectedLanguage) throws SQLException {
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
        Length length = new Length(code, descriptionEntity);
        length = LengthDao.getInstance().insert(length);
        return lengthDtoFactory.newDto(length);
    }

    @Override
    public boolean deleteSelectedLength(String code) throws SQLException {
        Optional<Length> lengthOpt = LengthDao.getInstance().findByCode(code);
        if (lengthOpt.isPresent()) {
            Description description = lengthOpt.get().getDescription();
            if (LengthDao.getInstance().remove(lengthOpt.get())) {
                return DescriptionDao.getInstance().remove(description);
            }
        }
        return false;
    }

    @Override
    public SelectDto updateChangedLengthCode(String oldCode, String newCode) throws SQLException {
        if (StringUtils.isBlank(newCode) || newCode.equalsIgnoreCase(oldCode)) {
            return null;
        }
        Optional<Length> lengthOpt = LengthDao.getInstance().findByCode(oldCode);
        if (lengthOpt.isPresent()) {
            lengthOpt.get().setCode(newCode);
            if (LengthDao.getInstance().update(lengthOpt.get())) {
                return lengthDtoFactory.newDto(lengthOpt.get());
            }
        }
        return null;
    }

    @Override
    public SelectDto updateChangedLengthDescription(String code, String oldDescription, String newDescription, SelectDto selectedLanguage) throws SQLException {
        if (StringUtils.isBlank(newDescription) || newDescription.equalsIgnoreCase(oldDescription)) {
            return null;
        }
        Optional<Length> lengthOpt = LengthDao.getInstance().findByCode(code);
        if (lengthOpt.isPresent()) {
            Description descriptionEntity = lengthOpt.get().getDescription();
            if ("en".equals(selectedLanguage.getKey())) {
                descriptionEntity.setEnglishText(newDescription);
            } else {
                descriptionEntity.setSpanishText(newDescription);
            }
            if (DescriptionDao.getInstance().update(descriptionEntity)) {
                return lengthDtoFactory.newDto(lengthOpt.get());
            }
        }
        return null;
    }
}
