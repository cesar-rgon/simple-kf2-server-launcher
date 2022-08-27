package stories.installupdateepicserver;

public interface InstallUpdateEpicServerFacade {
    boolean saveOrUpdateProperty(String key, String newValue) throws Exception;
    String findPropertyValue(String key) throws Exception;
}
