package stories.updateprofilesetchatloggingfile;

import framework.ModelContext;

public class UpdateProfileSetChatLoggingFileModelContext extends ModelContext {

    private final String profileName;
    private final String chatLoggingFile;

    public UpdateProfileSetChatLoggingFileModelContext(String profileName, String chatLoggingFile) {
        super();
        this.profileName = profileName;
        this.chatLoggingFile = chatLoggingFile;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getChatLoggingFile() {
        return chatLoggingFile;
    }
}
