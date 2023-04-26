package stories.updateprofilesetchatlogging;

import framework.ModelContext;

public class UpdateProfileSetChatLoggingModelContext extends ModelContext {

    private final String profileName;
    private final boolean chatLoggingSelected;

    public UpdateProfileSetChatLoggingModelContext(String profileName, boolean chatLoggingSelected) {
        super();
        this.profileName = profileName;
        this.chatLoggingSelected = chatLoggingSelected;
    }

    public String getProfileName() {
        return profileName;
    }

    public boolean isChatLoggingSelected() {
        return chatLoggingSelected;
    }
}
