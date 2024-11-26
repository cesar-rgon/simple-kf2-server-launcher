package stories.wizardstep1;

import stories.listlanguageswizardstep1.ListLanguagesWizardStep1FacadeResult;

public interface WizardStep1ManagerFacade {

    ListLanguagesWizardStep1FacadeResult execute() throws Exception;
    String findPropertyValue(String propertyFilePath, String key) throws Exception;
}
