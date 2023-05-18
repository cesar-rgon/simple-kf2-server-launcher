package stories.populatedatabase;

import entities.Difficulty;
import entities.GameType;
import entities.Length;
import entities.MaxPlayers;
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
import java.util.List;

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
        PropertyService propertyService = new PropertyServiceImpl();
        propertyService.setProperty("properties/config.properties", "prop.config.createDatabase", "false");
        logger.info("----- Ending the populate process over the application database -----");

        return new EmptyFacadeResult();
    }

    @Override
    public List<Difficulty> loadDefaultDifficulties() throws Exception {
        EntityManager em = beginTransaction();
        PopulateDatabase populateDatabase = new PopulateDatabase(em);
        List<Difficulty> result = populateDatabase.populateDifficulties();
        commitTransaction(em);
        return result;
    }

    @Override
    public List<GameType> loadDefaultGametypes() throws Exception {
        EntityManager em = beginTransaction();
        PopulateDatabase populateDatabase = new PopulateDatabase(em);
        List<GameType> result = populateDatabase.populateGameTypes();
        commitTransaction(em);
        return result;
    }

    @Override
    public List<Length> loadDefaultLengths() throws Exception {
        EntityManager em = beginTransaction();
        PopulateDatabase populateDatabase = new PopulateDatabase(em);
        List<Length> result = populateDatabase.populateLengths();
        commitTransaction(em);
        return result;
    }

    @Override
    public List<MaxPlayers> loadDefaultMaxPlayers() throws Exception {
        EntityManager em = beginTransaction();
        PopulateDatabase populateDatabase = new PopulateDatabase(em);
        List<MaxPlayers> result = populateDatabase.polulateMaximunPlayersList();
        commitTransaction(em);
        return result;
    }

}
