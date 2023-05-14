package stories.importentitiesfromfile;

import entities.Language;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import services.*;

import java.util.List;
import java.util.Properties;

public class ImportEntitiesFromFileFacadeImpl
        extends AbstractTransactionalFacade<ImportEntitiesFromFileModelContext, ImportEntitiesFromFileFacadeResult>
        implements ImportEntitiesFromFileFacade {

    public ImportEntitiesFromFileFacadeImpl(ImportEntitiesFromFileModelContext importEntitiesFromFileModelContext) {
        super(importEntitiesFromFileModelContext, ImportEntitiesFromFileFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(ImportEntitiesFromFileModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected ImportEntitiesFromFileFacadeResult internalExecute(ImportEntitiesFromFileModelContext facadeModelContext, EntityManager em) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        LanguageServiceImpl languageService = new LanguageServiceImpl(em);
        ProfileService profileService = new ProfileServiceImpl(em);

        Properties properties = propertyService.loadPropertiesFromFile(facadeModelContext.getFile());
        List<Language> languageList = languageService.listAll();
        profileService.importGameTypesFromFile(properties, languageList);
        profileService.importDifficultiesFromFile(properties, languageList);
        profileService.importLengthsFromFile(properties, languageList);
        profileService.importMaxPlayersFromFile(properties, languageList);

        return new ImportEntitiesFromFileFacadeResult(
                properties
        );
    }
}
