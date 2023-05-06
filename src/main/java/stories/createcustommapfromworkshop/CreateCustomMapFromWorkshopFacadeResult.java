package stories.createcustommapfromworkshop;

import dtos.CustomMapModDto;
import framework.FacadeResult;

public class CreateCustomMapFromWorkshopFacadeResult extends FacadeResult {

    private CustomMapModDto customMapDto;
    private StringBuffer success;
    private StringBuffer errors;

    public CreateCustomMapFromWorkshopFacadeResult() {
        super();
    }

    public CreateCustomMapFromWorkshopFacadeResult(CustomMapModDto customMapDto, StringBuffer success, StringBuffer errors) {
        super();
        this.customMapDto = customMapDto;
        this.success = success;
        this.errors = errors;
    }

    public CustomMapModDto getCustomMapDto() {
        return customMapDto;
    }

    public void setCustomMapDto(CustomMapModDto customMapDto) {
        this.customMapDto = customMapDto;
    }

    public StringBuffer getSuccess() {
        return success;
    }

    public void setSuccess(StringBuffer success) {
        this.success = success;
    }

    public StringBuffer getErrors() {
        return errors;
    }

    public void setErrors(StringBuffer errors) {
        this.errors = errors;
    }
}
