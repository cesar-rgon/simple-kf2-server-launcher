package stories.updateprofilesetpublictextchat;

import framework.ModelContext;

public class UpdateProfileSetPublicTextChatModelContext extends ModelContext {

    private final String profileName;
    private final boolean publicTextChatSelected;

    public UpdateProfileSetPublicTextChatModelContext(String profileName, boolean publicTextChatSelected) {
        super();
        this.profileName = profileName;
        this.publicTextChatSelected = publicTextChatSelected;
    }

    public String getProfileName() {
        return profileName;
    }

    public boolean isPublicTextChatSelected() {
        return publicTextChatSelected;
    }
}
