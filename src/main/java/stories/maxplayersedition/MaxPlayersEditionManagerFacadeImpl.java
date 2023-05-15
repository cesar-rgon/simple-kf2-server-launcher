package stories.maxplayersedition;

import dtos.ProfileDto;
import dtos.SelectDto;
import dtos.factories.DifficultyDtoFactory;
import dtos.factories.MaxPlayersDtoFactory;
import entities.Difficulty;
import entities.MaxPlayers;
import framework.AbstractManagerFacade;
import framework.EmptyModelContext;
import services.DifficultyServiceImpl;
import services.MaxPlayersServiceImpl;
import services.PropertyService;
import services.PropertyServiceImpl;
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
import stories.unselectdifficultyinprofile.UnselectDifficultyInProfileFacade;
import stories.unselectdifficultyinprofile.UnselectDifficultyInProfileFacadeImpl;
import stories.unselectdifficultyinprofile.UnselectDifficultyInProfileModelContext;
import stories.unselectmaxplayersinprofile.UnselectMaxPlayersInProfileFacade;
import stories.unselectmaxplayersinprofile.UnselectMaxPlayersInProfileFacadeImpl;
import stories.unselectmaxplayersinprofile.UnselectMaxPlayersInProfileModelContext;
import stories.updateitemcode.UpdateItemCodeFacade;
import stories.updateitemcode.UpdateItemCodeFacadeImpl;
import stories.updateitemcode.UpdateItemCodeFacadeResult;
import stories.updateitemcode.UpdateItemCodeModelContext;
import stories.updateitemdescription.UpdateItemDescriptionFacade;
import stories.updateitemdescription.UpdateItemDescriptionFacadeImpl;
import stories.updateitemdescription.UpdateItemDescriptionFacadeResult;
import stories.updateitemdescription.UpdateItemDescriptionModelContext;

public class MaxPlayersEditionManagerFacadeImpl
        extends AbstractManagerFacade<EmptyModelContext, ListAllItemsFacadeResult>
        implements MaxPlayersEditionManagerFacade {

    public MaxPlayersEditionManagerFacadeImpl() {
        super(new EmptyModelContext(), ListAllItemsFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(EmptyModelContext facadeModelContext) throws Exception {
        return true;
    }

    @Override
    protected ListAllItemsFacadeResult internalExecute(EmptyModelContext facadeModelContext) throws Exception {
        ListAllItemsFacade listAllItemsFacade = new ListAllItemsFacadeImpl(
                MaxPlayersServiceImpl.class,
                MaxPlayersDtoFactory.class
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
                MaxPlayers.class,
                MaxPlayersServiceImpl.class,
                MaxPlayersDtoFactory.class,
                createItemModelContext
        );
        CreateItemFacadeResult<SelectDto> result = createItemFacade.execute();
        return result.getNewItem();
    }

    @Override
    public void deleteItem(String actualProfileName, String code) throws Exception {
        UnselectMaxPlayersInProfileModelContext unselectMaxPlayersInProfileModelContext = new UnselectMaxPlayersInProfileModelContext(
                actualProfileName,
                code
        );
        UnselectMaxPlayersInProfileFacade unselectMaxPlayersInProfileFacade = new UnselectMaxPlayersInProfileFacadeImpl(unselectMaxPlayersInProfileModelContext);
        unselectMaxPlayersInProfileFacade.execute();

        DeleteItemModelContext deleteItemModelContext = new DeleteItemModelContext(
                code
        );
        DeleteItemFacade deleteItemFacade = new DeleteItemFacadeImpl(
                MaxPlayers.class,
                MaxPlayersServiceImpl.class,
                MaxPlayersDtoFactory.class,
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
                MaxPlayersServiceImpl.class,
                MaxPlayersDtoFactory.class,
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
                MaxPlayersServiceImpl.class,
                MaxPlayersDtoFactory.class,
                updateItemDescriptionModelContext
        );
        UpdateItemDescriptionFacadeResult<SelectDto> result = updateItemDescriptionFacade.execute();
        return result.getUpdatedItem();
    }

    @Override
    public String findPropertyValue(String propertyFilePath, String key) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        return propertyService.getPropertyValue(propertyFilePath, key);
    }
}
