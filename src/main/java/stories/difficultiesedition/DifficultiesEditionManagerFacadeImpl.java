package stories.difficultiesedition;

import dtos.SelectDto;
import dtos.factories.DifficultyDtoFactory;
import entities.Difficulty;
import framework.AbstractManagerFacade;
import framework.EmptyModelContext;
import services.DifficultyServiceImpl;
import services.ProfileServiceImpl;
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
import stories.unselectdifficultyinprofile.UnselectDifficultyInProfileFacade;
import stories.unselectdifficultyinprofile.UnselectDifficultyInProfileFacadeImpl;
import stories.unselectdifficultyinprofile.UnselectDifficultyInProfileModelContext;
import stories.updateitemcode.UpdateItemCodeFacade;
import stories.updateitemcode.UpdateItemCodeFacadeImpl;
import stories.updateitemcode.UpdateItemCodeFacadeResult;
import stories.updateitemcode.UpdateItemCodeModelContext;
import stories.updateitemdescription.UpdateItemDescriptionFacade;
import stories.updateitemdescription.UpdateItemDescriptionFacadeImpl;
import stories.updateitemdescription.UpdateItemDescriptionFacadeResult;
import stories.updateitemdescription.UpdateItemDescriptionModelContext;

import java.util.List;

public class DifficultiesEditionManagerFacadeImpl
        extends AbstractManagerFacade<EmptyModelContext, ListAllItemsFacadeResult>
        implements DifficultiesEditionManagerFacade {

    public DifficultiesEditionManagerFacadeImpl() {
        super(new EmptyModelContext(), ListAllItemsFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(EmptyModelContext facadeModelContext) throws Exception {
        return true;
    }

    @Override
    protected ListAllItemsFacadeResult internalExecute(EmptyModelContext facadeModelContext) throws Exception {
        ListAllItemsFacade listAllItemsFacade = new ListAllItemsFacadeImpl(
                DifficultyServiceImpl.class,
                DifficultyDtoFactory.class
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
                Difficulty.class,
                DifficultyServiceImpl.class,
                DifficultyDtoFactory.class,
                createItemModelContext
        );
        CreateItemFacadeResult<SelectDto> result = createItemFacade.execute();
        return result.getNewItem();
    }

    @Override
    public void deleteItem(String actualProfileName, String code) throws Exception {
        UnselectDifficultyInProfileModelContext unselectDifficultyInProfileModelContext = new UnselectDifficultyInProfileModelContext(
                actualProfileName,
                code
        );
        UnselectDifficultyInProfileFacade unselectDifficultyInProfileFacade = new UnselectDifficultyInProfileFacadeImpl(unselectDifficultyInProfileModelContext);
        unselectDifficultyInProfileFacade.execute();

        DeleteItemModelContext deleteItemModelContext = new DeleteItemModelContext(
                code
        );
        DeleteItemFacade deleteItemFacade = new DeleteItemFacadeImpl(
                Difficulty.class,
                DifficultyServiceImpl.class,
                DifficultyDtoFactory.class,
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
                DifficultyServiceImpl.class,
                DifficultyDtoFactory.class,
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
                DifficultyServiceImpl.class,
                DifficultyDtoFactory.class,
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

    @Override
    public List<SelectDto> loadDefaultValues() throws Exception {
        DifficultyDtoFactory difficultyDtoFactory = new DifficultyDtoFactory();

        DeleteAllItemsFacade deleteAllItemsFacade = new DeleteAllItemsFacadeImpl<>(DifficultyServiceImpl.class);
        deleteAllItemsFacade.execute();
        PopulateDatabaseFacade populateDatabaseFacade = new PopulateDatabaseFacadeImpl();
        List<Difficulty> defaultDifficultyList = populateDatabaseFacade.loadDefaultDifficulties();
        return difficultyDtoFactory.newDtos(defaultDifficultyList);
    }
}
