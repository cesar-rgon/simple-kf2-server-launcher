package stories.listlanguageswizardstep1;

import dtos.SelectDto;
import dtos.factories.LanguageDtoFactory;
import entities.Language;
import framework.AbstractTransactionalFacade;
import framework.EmptyModelContext;
import jakarta.persistence.EntityManager;
import javafx.collections.ObservableList;
import services.LanguageServiceImpl;

import java.util.List;

public class ListLanguagesWizardStep1FacadeImpl
        extends AbstractTransactionalFacade<EmptyModelContext, ListLanguagesWizardStep1FacadeResult>
        implements ListLanguagesWizardStep1Facade {


    public ListLanguagesWizardStep1FacadeImpl() {
        super(new EmptyModelContext(), ListLanguagesWizardStep1FacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(EmptyModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected ListLanguagesWizardStep1FacadeResult internalExecute(EmptyModelContext facadeModelContext, EntityManager em) throws Exception {
        LanguageServiceImpl languageService = new LanguageServiceImpl(em);
        LanguageDtoFactory languageDtoFactory = new LanguageDtoFactory();

        List<Language> languageList = languageService.listAll();
        ObservableList<SelectDto> languageDtoList = languageDtoFactory.newDtos(languageList);

        return new ListLanguagesWizardStep1FacadeResult(
                languageDtoList
        );
    }
}
