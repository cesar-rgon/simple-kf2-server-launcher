package stories.installupdateepicserver;

import services.PropertyService;
import services.PropertyServiceImpl;

public class InstallUpdateEpicServerFacadeImpl implements InstallUpdateEpicServerFacade {

    private final PropertyService propertyService;

    public InstallUpdateEpicServerFacadeImpl() {
        super();
        propertyService = new PropertyServiceImpl();
    }

    @Override
    public boolean saveOrUpdateProperty(String key, String newValue) throws Exception {
        propertyService.setProperty("properties/config.properties", key, newValue);
        return true;
    }

    @Override
    public String findPropertyValue(String key) throws Exception {
        return propertyService.getPropertyValue("properties/config.properties", key);
    }

}
