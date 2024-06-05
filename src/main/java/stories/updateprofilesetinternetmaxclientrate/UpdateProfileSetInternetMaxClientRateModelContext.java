package stories.updateprofilesetinternetmaxclientrate;

import framework.ModelContext;

public class UpdateProfileSetInternetMaxClientRateModelContext extends ModelContext {

    private final String profileName;
    private final Integer internetMaxClientRate;

    public UpdateProfileSetInternetMaxClientRateModelContext(String profileName, Integer internetMaxClientRate) {
        super();
        this.profileName = profileName;
        this.internetMaxClientRate = internetMaxClientRate;
    }

    public String getProfileName() {
        return profileName;
    }

    public Integer getInternetMaxClientRate() {
        return internetMaxClientRate;
    }
}
