package stories.addplatformprofilestomap;

import framework.FacadeResult;

public class AddPlatformProfilesToMapFacadeResult extends FacadeResult {

    private StringBuffer success;
    private StringBuffer errors;

    public AddPlatformProfilesToMapFacadeResult() {
        super();
    }

    public AddPlatformProfilesToMapFacadeResult(StringBuffer success, StringBuffer errors) {
        super();
        this.success = success;
        this.errors = errors;
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
