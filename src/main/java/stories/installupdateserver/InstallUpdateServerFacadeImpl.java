package stories.installupdateserver;

import daos.PropertyDao;
import entities.Property;
import services.DatabaseService;
import services.DatabaseServiceImpl;

import java.sql.SQLException;
import java.util.Optional;

public class InstallUpdateServerFacadeImpl implements InstallUpdateServerFacade {

    private final DatabaseService databaseService;

    public InstallUpdateServerFacadeImpl() {
        super();
        databaseService = new DatabaseServiceImpl();
    }

    @Override
    public boolean saveOrUpdateProperty(String key, String newValue) throws SQLException {
        Optional<Property> propertyOpt = PropertyDao.getInstance().findByKey(key);
        if (propertyOpt.isPresent()) {
            propertyOpt.get().setValue(newValue);
            return PropertyDao.getInstance().update(propertyOpt.get());
        } else {
            Property property = new Property(key, newValue);
            return PropertyDao.getInstance().insert(property) != null;
        }
    }

    @Override
    public String findPropertyValue(String key) throws SQLException {
        return databaseService.findPropertyValue(key);
    }
}
