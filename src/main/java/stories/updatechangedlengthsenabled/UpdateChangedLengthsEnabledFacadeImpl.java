package stories.updatechangedlengthsenabled;

import entities.GameType;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.GameTypeServiceImpl;

import java.util.Optional;

public class UpdateChangedLengthsEnabledFacadeImpl
        extends AbstractTransactionalFacade<UpdateChangedLengthsEnabledModelContext, EmptyFacadeResult>
        implements UpdateChangedLengthsEnabledFacade {


    public UpdateChangedLengthsEnabledFacadeImpl(UpdateChangedLengthsEnabledModelContext updateChangedLengthsEnabledModelContext) {
        super(updateChangedLengthsEnabledModelContext, EmptyFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(UpdateChangedLengthsEnabledModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateChangedLengthsEnabledModelContext facadeModelContext, EntityManager em) throws Exception {
        GameTypeServiceImpl gameTypeService = new GameTypeServiceImpl(em);
        Optional<GameType> gameTypeOpt = gameTypeService.findByCode(facadeModelContext.getCode());

        if (!gameTypeOpt.isPresent()) {
            throw new RuntimeException("The game type could not be found");
        }
        gameTypeOpt.get().setLengthEnabled(facadeModelContext.getNewLengthsEnabled());
        gameTypeService.updateItem(gameTypeOpt.get());

        return new EmptyFacadeResult();
    }
}
