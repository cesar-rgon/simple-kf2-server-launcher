package stories.updateprofilesetlanmaxclientrate;

import framework.ModelContext;

public class UpdateProfileSetLanMaxClientRateModelContext extends ModelContext {

    private final String profileName;
    private final Integer lanMaxClientRate;

    public UpdateProfileSetLanMaxClientRateModelContext(String profileName, Integer lanMaxClientRate) {
        super();
        this.profileName = profileName;
        this.lanMaxClientRate = lanMaxClientRate;
    }

    public String getProfileName() {
        return profileName;
    }

    public Integer getLanMaxClientRate() {
        return lanMaxClientRate;
    }
}
