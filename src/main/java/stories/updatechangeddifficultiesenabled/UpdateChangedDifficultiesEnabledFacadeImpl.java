package stories.updatechangeddifficultiesenabled;

import dtos.GameTypeDto;
import entities.GameType;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.GameTypeServiceImpl;

import java.util.Optional;

public class UpdateChangedDifficultiesEnabledFacadeImpl
        extends AbstractTransactionalFacade<UpdateChangedDifficultiesEnabledModelContext, EmptyFacadeResult>
        implements UpdateChangedDifficultiesEnabledFacade {

    public UpdateChangedDifficultiesEnabledFacadeImpl(UpdateChangedDifficultiesEnabledModelContext updateChangedDifficultiesEnabledModelContext) {
        super(updateChangedDifficultiesEnabledModelContext, EmptyFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(UpdateChangedDifficultiesEnabledModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateChangedDifficultiesEnabledModelContext facadeModelContext, EntityManager em) throws Exception {
        GameTypeServiceImpl gameTypeService = new GameTypeServiceImpl(em);
        Optional<GameType> gameTypeOpt = gameTypeService.findByCode(facadeModelContext.getCode());

        if (!gameTypeOpt.isPresent()) {
            throw new RuntimeException("The game type could not be found");
        }
        gameTypeOpt.get().setDifficultyEnabled(facadeModelContext.getNewDifficultiesEnabled());
        gameTypeService.updateItem(gameTypeOpt.get());

        return new EmptyFacadeResult();
    }
}
