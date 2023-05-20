package stories.gametypesedition;

import dtos.GameTypeDto;
import dtos.factories.GameTypeDtoFactory;
import entities.GameType;
import framework.AbstractManagerFacade;
import framework.EmptyModelContext;
import services.GameTypeServiceImpl;
import services.PropertyService;
import services.PropertyServiceImpl;
import stories.createitem.CreateItemFacade;
import stories.createitem.CreateItemFacadeImpl;
import stories.createitem.CreateItemFacadeResult;
import stories.createitem.CreateItemModelContext;
import stories.deleteallitems.DeleteAllItemsFacade;
import stories.deleteallitems.DeleteAllItemsFacadeImpl;
import stories.deleteitem.DeleteItemFacade;
import stories.deleteitem.DeleteItemFacadeImpl;
import stories.deleteitem.DeleteItemModelContext;
import stories.listallitems.ListAllItemsFacade;
import stories.listallitems.ListAllItemsFacadeImpl;
import stories.listallitems.ListAllItemsFacadeResult;
import stories.populatedatabase.PopulateDatabaseFacade;
import stories.populatedatabase.PopulateDatabaseFacadeImpl;
import stories.updatechangeddifficultiesenabled.UpdateChangedDifficultiesEnabledFacade;
import stories.updatechangeddifficultiesenabled.UpdateChangedDifficultiesEnabledFacadeImpl;
import stories.updatechangeddifficultiesenabled.UpdateChangedDifficultiesEnabledModelContext;
import stories.updatechangedlengthsenabled.UpdateChangedLengthsEnabledFacade;
import stories.updatechangedlengthsenabled.UpdateChangedLengthsEnabledFacadeImpl;
import stories.updatechangedlengthsenabled.UpdateChangedLengthsEnabledModelContext;
import stories.updateitemcode.UpdateItemCodeFacade;
import stories.updateitemcode.UpdateItemCodeFacadeImpl;
import stories.updateitemcode.UpdateItemCodeFacadeResult;
import stories.updateitemcode.UpdateItemCodeModelContext;
import stories.updateitemdescription.UpdateItemDescriptionFacade;
import stories.updateitemdescription.UpdateItemDescriptionFacadeImpl;
import stories.updateitemdescription.UpdateItemDescriptionFacadeResult;
import stories.updateitemdescription.UpdateItemDescriptionModelContext;

import java.util.List;

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
    public void deleteItem(String actualProfileName, String code) throws Exception {
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
    public void updateChangedDifficultiesEnabled(String code, Boolean newDifficultiesEnabled) throws Exception {
        UpdateChangedDifficultiesEnabledModelContext updateChangedDifficultiesEnabledModelContext = new UpdateChangedDifficultiesEnabledModelContext(
                code,
                newDifficultiesEnabled
        );
        UpdateChangedDifficultiesEnabledFacade updateChangedDifficultiesEnabledFacade = new UpdateChangedDifficultiesEnabledFacadeImpl(updateChangedDifficultiesEnabledModelContext);
        updateChangedDifficultiesEnabledFacade.execute();
    }

    @Override
    public void updateChangedLengthsEnabled(String code, Boolean newLengthsEnabled) throws Exception {
        UpdateChangedLengthsEnabledModelContext updateChangedLengthsEnabledModelContext = new UpdateChangedLengthsEnabledModelContext(
                code,
                newLengthsEnabled
        );
        UpdateChangedLengthsEnabledFacade updateChangedLengthsEnabledFacade = new UpdateChangedLengthsEnabledFacadeImpl(updateChangedLengthsEnabledModelContext);
        updateChangedLengthsEnabledFacade.execute();
    }

    @Override
    public String findPropertyValue(String propertyFilePath, String key) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        return propertyService.getPropertyValue(propertyFilePath, key);
    }

    @Override
    public List<GameTypeDto> loadDefaultValues() throws Exception {
        GameTypeDtoFactory gameTypeDtoFactory = new GameTypeDtoFactory();

        DeleteAllItemsFacade deleteAllItemsFacade = new DeleteAllItemsFacadeImpl<>(GameTypeServiceImpl.class);
        deleteAllItemsFacade.execute();
        PopulateDatabaseFacade populateDatabaseFacade = new PopulateDatabaseFacadeImpl();
        List<GameType> defaultGametypeList = populateDatabaseFacade.loadDefaultGametypes();
        return gameTypeDtoFactory.newDtos(defaultGametypeList);
    }
}
