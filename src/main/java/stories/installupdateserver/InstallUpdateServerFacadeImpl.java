package stories.installupdateserver;

import services.PropertyService;
import services.PropertyServiceImpl;

public class InstallUpdateServerFacadeImpl implements InstallUpdateServerFacade {

    private final PropertyService propertyService;

    public InstallUpdateServerFacadeImpl() {
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
