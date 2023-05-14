package stories.gametypesedition;

import dtos.GameTypeDto;
import dtos.ProfileDto;
import dtos.SelectDto;
import dtos.factories.DifficultyDtoFactory;
import dtos.factories.GameTypeDtoFactory;
import entities.Difficulty;
import entities.GameType;
import framework.AbstractManagerFacade;
import framework.EmptyFacadeResult;
import framework.EmptyModelContext;
import javafx.collections.ObservableList;
import services.DifficultyServiceImpl;
import services.GameTypeServiceImpl;
import stories.createitem.CreateItemFacade;
import stories.createitem.CreateItemFacadeImpl;
import stories.createitem.CreateItemFacadeResult;
import stories.createitem.CreateItemModelContext;
import stories.deleteitem.DeleteItemFacade;
import stories.deleteitem.DeleteItemFacadeImpl;
import stories.deleteitem.DeleteItemModelContext;
import stories.listallitems.ListAllItemsFacade;
import stories.listallitems.ListAllItemsFacadeImpl;
import stories.listallitems.ListAllItemsFacadeResult;
import stories.updateitemcode.UpdateItemCodeFacade;
import stories.updateitemcode.UpdateItemCodeFacadeImpl;
import stories.updateitemcode.UpdateItemCodeFacadeResult;
import stories.updateitemcode.UpdateItemCodeModelContext;
import stories.updateitemdescription.UpdateItemDescriptionFacade;
import stories.updateitemdescription.UpdateItemDescriptionFacadeImpl;
import stories.updateitemdescription.UpdateItemDescriptionFacadeResult;
import stories.updateitemdescription.UpdateItemDescriptionModelContext;

public class GameTypesEditionManagerFacadeImpl
        extends AbstractManagerFacade<EmptyModelContext, ListAllItemsFacadeResult>
        implements GameTypesEditionManagerFacade {

    public GameTypesEditionManagerFacadeImpl() {
        super(new EmptyModelContext(), ListAllItemsFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(EmptyModelContext facadeModelContext) throws Exception {
        return true;
    }

    @Override
    protected ListAllItemsFacadeResult internalExecute(EmptyModelContext facadeModelContext) throws Exception {
        ListAllItemsFacade listAllItemsFacade = new ListAllItemsFacadeImpl(
                GameTypeServiceImpl.class,
                GameTypeDtoFactory.class
        );
        return listAllItemsFacade.execute();
    }

    @Override
    public GameTypeDto createItem(String code, String description, String languageCode) throws Exception {
        CreateItemModelContext createItemModelContext = new CreateItemModelContext(
                code,
                description,
                languageCode
        );
        CreateItemFacade createItemFacade = new CreateItemFacadeImpl(
                GameType.class,
                GameTypeServiceImpl.class,
                GameTypeDtoFactory.class,
                createItemModelContext
        );
        CreateItemFacadeResult<GameTypeDto> result = createItemFacade.execute();
        return result.getNewItem();
    }

    @Override
    public void deleteItem(String code) throws Exception {
        DeleteItemModelContext deleteItemModelContext = new DeleteItemModelContext(
                code
        );
        DeleteItemFacade deleteItemFacade = new DeleteItemFacadeImpl(
                GameType.class,
                GameTypeServiceImpl.class,
                GameTypeDtoFactory.class,
                deleteItemModelContext
        );
        deleteItemFacade.execute();
    }

    @Override
    public GameTypeDto updateItemCode(String oldCode, String newCode) throws Exception {
        UpdateItemCodeModelContext updateItemCodeModelContext = new UpdateItemCodeModelContext(
                oldCode,
                newCode
        );
        UpdateItemCodeFacade updateItemCodeFacade = new UpdateItemCodeFacadeImpl(
                GameTypeServiceImpl.class,
                GameTypeDtoFactory.class,
                updateItemCodeModelContext
        );
        UpdateItemCodeFacadeResult<GameTypeDto> result = updateItemCodeFacade.execute();
        return result.getUpdatedItem();
    }

    @Override
    public GameTypeDto updateItemDescription(String code, String oldDescription, String newDescription, String languageCode) throws Exception {
        UpdateItemDescriptionModelContext updateItemDescriptionModelContext = new UpdateItemDescriptionModelContext(
                code,
                oldDescription,
                newDescription,
                languageCode
        );
        UpdateItemDescriptionFacade updateItemDescriptionFacade = new UpdateItemDescriptionFacadeImpl(
                GameTypeServiceImpl.class,
                GameTypeDtoFactory.class,
                updateItemDescriptionModelContext
        );
        UpdateItemDescriptionFacadeResult<GameTypeDto> result = updateItemDescriptionFacade.execute();
        return result.getUpdatedItem();
    }

    @Override
    public GameTypeDto updateChangedDifficultiesEnabled(String code, Boolean newDifficultiesEnabled) throws Exception {
        return null;
    }

    @Override
    public GameTypeDto updateChangedLengthsEnabled(String code, Boolean newLengthsEnabled) throws Exception {
        return null;
    }

    @Override
    public ProfileDto unselectGametypeInProfile(String profileName) throws Exception {
        return null;
    }

    @Override
    public ProfileDto findProfileDtoByName(String profileName) throws Exception {
        return null;
    }
}
