package stories.importprofilesfromfile;

import dtos.factories.ProfileDtoFactory;
import entities.AbstractPlatform;
import entities.EpicPlatform;
import entities.Profile;
import entities.SteamPlatform;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import services.*;
import utils.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

public class ImportProfilesFromFileFacadeImpl
        extends AbstractTransactionalFacade<ImportProfilesFromFileModelContext, ImportProfilesFromFileFacadeResult>
        implements ImportProfilesFromFileFacade {

    private static final Logger logger = LogManager.getLogger(ImportProfilesFromFileFacadeImpl.class);

    public ImportProfilesFromFileFacadeImpl(ImportProfilesFromFileModelContext importProfilesFromFileModelContext) {
        super(importProfilesFromFileModelContext, ImportProfilesFromFileFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(ImportProfilesFromFileModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        PropertyService propertyService = new PropertyServiceImpl();

        List<Profile> profileInDatabaseList = profileService.listAllProfiles();
        List<String> profileNameInDatabaseList = profileInDatabaseList.stream().map(Profile::getName).collect(Collectors.toList());
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");

        StringBuffer duplicatedProfileNameList = new StringBuffer();
        for (Profile selectedProfile: facadeModelContext.getSelectedProfileList()) {
            if (profileNameInDatabaseList.contains(selectedProfile.getName())) {
                if (StringUtils.isBlank(duplicatedProfileNameList)) {
                    duplicatedProfileNameList.append(selectedProfile.getName());
                } else {
                    duplicatedProfileNameList.append(", ");
                    duplicatedProfileNameList.append(selectedProfile.getName());
                }
            }
        }

        if (StringUtils.isNotBlank(duplicatedProfileNameList)) {
            Utils.warningDialog(headerText, findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.importProfileDuplicated", duplicatedProfileNameList.toString()));
            return false;
        }
        return true;
    }

    @Override
    protected ImportProfilesFromFileFacadeResult internalExecute(ImportProfilesFromFileModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory(em);

        List<Profile> importedProfileList = profileService.importProfilesFromFile(facadeModelContext.getSelectedProfileList(), facadeModelContext.getProperties(), facadeModelContext.getErrorMessage());

        if (importedProfileList.isEmpty()) {
            return new ImportProfilesFromFileFacadeResult();
        }

        for (Profile importedProfile: importedProfileList) {
            try {
                createConfigFolder(importedProfile.getName(), em);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        return new ImportProfilesFromFileFacadeResult(
                profileDtoFactory.newDtos(importedProfileList)
        );
    }

    private void createConfigFolder(String profileName, EntityManager em) throws SQLException {
        PlatformService platformService = new PlatformServiceImpl(em);

        List<AbstractPlatform> validPlatformList = new ArrayList<AbstractPlatform>();
        Optional<SteamPlatform> steamPlatformOptional = platformService.findSteamPlatform();
        if (steamPlatformOptional.isPresent()) {
            if (Kf2Factory.getInstance(steamPlatformOptional.get(), em).isValidInstallationFolder()) {
                validPlatformList.add(steamPlatformOptional.get());
            }
        }
        Optional<EpicPlatform> epicPlatformOptional = platformService.findEpicPlatform();
        if (epicPlatformOptional.isPresent()) {
            if (Kf2Factory.getInstance(epicPlatformOptional.get(), em).isValidInstallationFolder()) {
                validPlatformList.add(epicPlatformOptional.get());
            }
        }

        for (AbstractPlatform platform: validPlatformList) {
            Kf2Common kf2Common = Kf2Factory.getInstance(platform, em);
            kf2Common.createConfigFolder(platform.getInstallationFolder(), profileName);
        }
    }

    private String findPropertyValue(String propFileRelativePath, String propKey, String profileParam) throws Exception {
        Properties prop = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("./" + propFileRelativePath);
        } catch (FileNotFoundException e) {
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileRelativePath);
        }
        prop.load(inputStream);
        inputStream.close();

        return MessageFormat.format(prop.getProperty(propKey), profileParam);
    }
}
