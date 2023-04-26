package stories.updateprofilesetchatloggingfiletimestamp;

import framework.ModelContext;

public class UpdateProfileSetChatLoggingFileTimestampModelContext extends ModelContext {

    private final String profileName;
    private final boolean chatLoggingFileTimestampSelected;

    public UpdateProfileSetChatLoggingFileTimestampModelContext(String profileName, boolean chatLoggingFileTimestampSelected) {
        super();
        this.profileName = profileName;
        this.chatLoggingFileTimestampSelected = chatLoggingFileTimestampSelected;
    }

    public String getProfileName() {
        return profileName;
    }

    public boolean isChatLoggingFileTimestampSelected() {
        return chatLoggingFileTimestampSelected;
    }
}
