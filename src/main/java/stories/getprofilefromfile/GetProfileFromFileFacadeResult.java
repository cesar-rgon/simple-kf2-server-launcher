package stories.getprofilefromfile;

import entities.Profile;
import framework.FacadeResult;

public class GetProfileFromFileFacadeResult extends FacadeResult {

    private Profile profileFromFile;

    public GetProfileFromFileFacadeResult() {
        super();
    }

    public GetProfileFromFileFacadeResult(Profile profileFromFile) {
        super();
        this.profileFromFile = profileFromFile;
    }

    public Profile getProfileFromFile() {
        return profileFromFile;
    }

    public void setProfileFromFile(Profile profileFromFile) {
        this.profileFromFile = profileFromFile;
    }
}
