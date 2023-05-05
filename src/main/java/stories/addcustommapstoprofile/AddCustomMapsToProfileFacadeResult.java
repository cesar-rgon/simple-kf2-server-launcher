package stories.addcustommapstoprofile;

import dtos.PlatformProfileMapDto;
import framework.FacadeResult;

import java.util.List;

public class AddCustomMapsToProfileFacadeResult extends FacadeResult {

    private List<PlatformProfileMapDto> platformProfileMapDtoList;
    private StringBuffer success;
    private StringBuffer errors;

    public AddCustomMapsToProfileFacadeResult() {
        super();
    }

    public AddCustomMapsToProfileFacadeResult(List<PlatformProfileMapDto> platformProfileMapDtoList, StringBuffer success, StringBuffer errors) {
        super();
        this.platformProfileMapDtoList = platformProfileMapDtoList;
        this.success = success;
        this.errors = errors;
    }

    public List<PlatformProfileMapDto> getPlatformProfileMapDtoList() {
        return platformProfileMapDtoList;
    }

    public void setPlatformProfileMapDtoList(List<PlatformProfileMapDto> platformProfileMapDtoList) {
        this.platformProfileMapDtoList = platformProfileMapDtoList;
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
