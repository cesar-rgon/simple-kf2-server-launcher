package stories.lengthedition;

import daos.LengthDao;
import daos.ProfileDao;
import dtos.ProfileDto;
import dtos.SelectDto;
import dtos.factories.LengthDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.Length;
import entities.Profile;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LengthEditionFacadeImpl implements LengthEditionFacade {

    private final LengthDtoFactory lengthDtoFactory;
    private final PropertyService propertyService;
    private final ProfileDtoFactory profileDtoFactory;

    public LengthEditionFacadeImpl() {
        super();
        this.lengthDtoFactory = new LengthDtoFactory();
        propertyService = new PropertyServiceImpl();
        profileDtoFactory = new ProfileDtoFactory();
    }

    public ObservableList<SelectDto> listAllLength() throws SQLException {
        List<Length> lengthList = LengthDao.getInstance().listAll();
        return lengthDtoFactory.newDtos(lengthList);
    }

    @Override
    public SelectDto createNewLength(String code, String description) throws Exception {
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.length." + code, description);
        Length length = new Length(code);
        length = LengthDao.getInstance().insert(length);
        return lengthDtoFactory.newDto(length);
    }

    @Override
    public boolean deleteSelectedLength(String code) throws Exception {
        Optional<Length> lengthOpt = LengthDao.getInstance().findByCode(code);
        if (lengthOpt.isPresent()) {
            if (LengthDao.getInstance().remove(lengthOpt.get())) {
                String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
                propertyService.removeProperty("properties/languages/" + languageCode + ".properties", "prop.length." + code);
                return LengthDao.getInstance().remove(lengthOpt.get());
            }
        }
        return false;
    }

    @Override
    public SelectDto updateChangedLengthCode(String oldCode, String newCode) throws Exception {
        if (StringUtils.isBlank(newCode) || newCode.equalsIgnoreCase(oldCode)) {
            return null;
        }
        Optional<Length> lengthOpt = LengthDao.getInstance().findByCode(oldCode);
        if (lengthOpt.isPresent()) {
            lengthOpt.get().setCode(newCode);
            if (LengthDao.getInstance().update(lengthOpt.get())) {
                String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
                String value = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.length." + oldCode);
                propertyService.removeProperty("properties/languages/" + languageCode + ".properties", "prop.length." + oldCode);
                propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.length." + newCode, value);
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
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            propertyService.setProperty("properties/languages/" + languageCode + ".properties", "prop.length." + code, newDescription);
            return lengthDtoFactory.newDto(lengthOpt.get());
        }
        return null;
    }

    @Override
    public ProfileDto unselectLengthInProfile(String profileName) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            profileOpt.get().setLength(null);
            ProfileDao.getInstance().update(profileOpt.get());
            return profileDtoFactory.newDto(profileOpt.get());
        }
        return null;
    }
}
