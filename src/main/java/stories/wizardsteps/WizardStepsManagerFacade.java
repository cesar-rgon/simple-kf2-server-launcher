package stories.wizardsteps;

import stories.listlanguageswizardstep1.ListLanguagesWizardStep1FacadeResult;

public interface WizardStepsManagerFacade {

    ListLanguagesWizardStep1FacadeResult execute() throws Exception;
    String findPropertyValue(String propertyFilePath, String key) throws Exception;
}
