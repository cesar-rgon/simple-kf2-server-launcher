package stories.listlanguageswizardstep1;

import dtos.SelectDto;
import framework.FacadeResult;
import javafx.collections.ObservableList;

public class ListLanguagesWizardStep1FacadeResult extends FacadeResult {

    private ObservableList<SelectDto> languageDtoList;

    public ListLanguagesWizardStep1FacadeResult() {
        super();
    }

    public ListLanguagesWizardStep1FacadeResult(ObservableList<SelectDto> languageDtoList) {
        super();
        this.languageDtoList = languageDtoList;
    }

    public ObservableList<SelectDto> getLanguageDtoList() {
        return languageDtoList;
    }

    public void setLanguageDtoList(ObservableList<SelectDto> languageDtoList) {
        this.languageDtoList = languageDtoList;
    }
}
