package pojos;

public class UpdateLauncher {

    private boolean update;
    private boolean dontShowAtStartup;

    public UpdateLauncher(boolean update, boolean dontShowAtStartup) {
        super();
        this.update = update;
        this.dontShowAtStartup = dontShowAtStartup;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isDontShowAtStartup() {
        return dontShowAtStartup;
    }

    public void setDontShowAtStartup(boolean dontShowAtStartup) {
        this.dontShowAtStartup = dontShowAtStartup;
    }
}
