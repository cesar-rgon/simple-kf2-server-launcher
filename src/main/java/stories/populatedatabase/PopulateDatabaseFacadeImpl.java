package stories.populatedatabase;

import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import framework.EmptyModelContext;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.PopulateDatabase;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.io.File;

public class PopulateDatabaseFacadeImpl
        extends AbstractTransactionalFacade<EmptyModelContext, EmptyFacadeResult>
        implements PopulateDatabaseFacade {

    private static final Logger logger = LogManager.getLogger(PopulateDatabaseFacadeImpl.class);

    public PopulateDatabaseFacadeImpl() {
        super(new EmptyModelContext(), EmptyFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(EmptyModelContext facadeModelContext, EntityManager em) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        return Boolean.parseBoolean(propertyService.getPropertyValue("properties/config.properties", "prop.config.createDatabase"));
    }

    @Override
    protected EmptyFacadeResult internalExecute(EmptyModelContext facadeModelContext, EntityManager em) throws Exception {
        logger.info("----- Starting the populate process over the application database -----");
        PopulateDatabase populateDatabase = new PopulateDatabase(em);
        populateDatabase.start();
        logger.info("----- Ending the populate process over the application database -----");

        return new EmptyFacadeResult();
    }
}
