package stories.lengthedition;

import daos.LengthDao;
import dtos.SelectDto;
import dtos.factories.LengthDtoFactory;
import entities.Length;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import pojos.session.Session;
import services.PropertyService;
import services.PropertyServiceImpl;

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
    public SelectDto createNewLength(String code, String description) throws Exception {
        String languageCode = Session.getInstance().getActualProfile().getLanguage().getKey();
        PropertyService propertyService = new PropertyServiceImpl();
        propertyService.setProperty(languageCode + ".properties", "prop.length." + code, description);
        Length length = new Length(code);
        length = LengthDao.getInstance().insert(length);
        return lengthDtoFactory.newDto(length);
    }

    @Override
    public boolean deleteSelectedLength(String code) throws Exception {
        Optional<Length> lengthOpt = LengthDao.getInstance().findByCode(code);
        if (lengthOpt.isPresent()) {
            if (LengthDao.getInstance().remove(lengthOpt.get())) {
                String languageCode = Session.getInstance().getActualProfile().getLanguage().getKey();
                PropertyService propertyService = new PropertyServiceImpl();
                propertyService.removeProperty(languageCode + ".properties", "prop.length." + code);
                return true;
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
    public SelectDto updateChangedLengthDescription(String code, String oldDescription, String newDescription) throws Exception {
        if (StringUtils.isBlank(newDescription) || newDescription.equalsIgnoreCase(oldDescription)) {
            return null;
        }
        Optional<Length> lengthOpt = LengthDao.getInstance().findByCode(code);
        if (lengthOpt.isPresent()) {
            String languageCode = Session.getInstance().getActualProfile().getLanguage().getKey();
            PropertyService propertyService = new PropertyServiceImpl();
            propertyService.setProperty(languageCode + ".properties", "prop.length." + code, newDescription);
            return lengthDtoFactory.newDto(lengthOpt.get());
        }
        return null;
    }
}
