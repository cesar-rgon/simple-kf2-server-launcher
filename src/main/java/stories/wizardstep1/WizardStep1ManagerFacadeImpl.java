package stories.wizardstep1;

import framework.AbstractManagerFacade;
import framework.EmptyModelContext;
import services.PropertyService;
import services.PropertyServiceImpl;
import stories.listlanguageswizardstep1.ListLanguagesWizardStep1Facade;
import stories.listlanguageswizardstep1.ListLanguagesWizardStep1FacadeImpl;
import stories.listlanguageswizardstep1.ListLanguagesWizardStep1FacadeResult;

public class WizardStep1ManagerFacadeImpl
        extends AbstractManagerFacade<EmptyModelContext, ListLanguagesWizardStep1FacadeResult>
        implements WizardStep1ManagerFacade {


    protected WizardStep1ManagerFacadeImpl() {
        super(new EmptyModelContext(), ListLanguagesWizardStep1FacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(EmptyModelContext facadeModelContext) throws Exception {
        return true;
    }

    @Override
    protected ListLanguagesWizardStep1FacadeResult internalExecute(EmptyModelContext facadeModelContext) throws Exception {
        ListLanguagesWizardStep1Facade listLanguagesWizardStep1Facade = new ListLanguagesWizardStep1FacadeImpl();
        return listLanguagesWizardStep1Facade.execute();
    }

    @Override
    public String findPropertyValue(String propertyFilePath, String key) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        return propertyService.getPropertyValue(propertyFilePath, key);
    }
}
