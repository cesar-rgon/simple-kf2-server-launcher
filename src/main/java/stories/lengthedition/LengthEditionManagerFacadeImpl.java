package stories.lengthedition;

import dtos.GameTypeDto;
import dtos.ProfileDto;
import dtos.SelectDto;
import dtos.factories.DifficultyDtoFactory;
import dtos.factories.GameTypeDtoFactory;
import dtos.factories.LengthDtoFactory;
import entities.Difficulty;
import entities.GameType;
import entities.Length;
import framework.AbstractManagerFacade;
import framework.EmptyFacadeResult;
import framework.EmptyModelContext;
import javafx.collections.ObservableList;
import services.DifficultyServiceImpl;
import services.GameTypeServiceImpl;
import services.LengthServiceImpl;
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

public class LengthEditionManagerFacadeImpl
        extends AbstractManagerFacade<EmptyModelContext, ListAllItemsFacadeResult>
        implements LengthEditionManagerFacade {

    public LengthEditionManagerFacadeImpl() {
        super(new EmptyModelContext(), ListAllItemsFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(EmptyModelContext facadeModelContext) throws Exception {
        return true;
    }

    @Override
    protected ListAllItemsFacadeResult internalExecute(EmptyModelContext facadeModelContext) throws Exception {
        ListAllItemsFacade listAllItemsFacade = new ListAllItemsFacadeImpl(
                LengthServiceImpl.class,
                LengthDtoFactory.class
        );
        return listAllItemsFacade.execute();
    }

    @Override
    public SelectDto createItem(String code, String description, String languageCode) throws Exception {
        CreateItemModelContext createItemModelContext = new CreateItemModelContext(
                code,
                description,
                languageCode
        );
        CreateItemFacade createItemFacade = new CreateItemFacadeImpl(
                Length.class,
                LengthServiceImpl.class,
                LengthDtoFactory.class,
                createItemModelContext
        );
        CreateItemFacadeResult<SelectDto> result = createItemFacade.execute();
        return result.getNewItem();
    }

    @Override
    public void deleteItem(String code) throws Exception {
        DeleteItemModelContext deleteItemModelContext = new DeleteItemModelContext(
                code
        );
        DeleteItemFacade deleteItemFacade = new DeleteItemFacadeImpl(
                Length.class,
                LengthServiceImpl.class,
                LengthDtoFactory.class,
                deleteItemModelContext
        );
        deleteItemFacade.execute();
    }

    @Override
    public SelectDto updateItemCode(String oldCode, String newCode) throws Exception {
        UpdateItemCodeModelContext updateItemCodeModelContext = new UpdateItemCodeModelContext(
                oldCode,
                newCode
        );
        UpdateItemCodeFacade updateItemCodeFacade = new UpdateItemCodeFacadeImpl(
                LengthServiceImpl.class,
                LengthDtoFactory.class,
                updateItemCodeModelContext
        );
        UpdateItemCodeFacadeResult<SelectDto> result = updateItemCodeFacade.execute();
        return result.getUpdatedItem();
    }

    @Override
    public SelectDto updateItemDescription(String code, String oldDescription, String newDescription, String languageCode) throws Exception {
        UpdateItemDescriptionModelContext updateItemDescriptionModelContext = new UpdateItemDescriptionModelContext(
                code,
                oldDescription,
                newDescription,
                languageCode
        );
        UpdateItemDescriptionFacade updateItemDescriptionFacade = new UpdateItemDescriptionFacadeImpl(
                LengthServiceImpl.class,
                LengthDtoFactory.class,
                updateItemDescriptionModelContext
        );
        UpdateItemDescriptionFacadeResult<SelectDto> result = updateItemDescriptionFacade.execute();
        return result.getUpdatedItem();
    }

    @Override
    public ProfileDto unselectLengthInProfile(String profileName) throws Exception {
        return null;
    }

    @Override
    public ProfileDto findProfileDtoByName(String profileName) throws Exception {
        return null;
    }
}
