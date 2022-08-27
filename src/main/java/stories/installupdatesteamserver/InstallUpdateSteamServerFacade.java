package stories.installupdatesteamserver;

public interface InstallUpdateSteamServerFacade {
    boolean saveOrUpdateProperty(String key, String newValue) throws Exception;
    String findPropertyValue(String key) throws Exception;
}
