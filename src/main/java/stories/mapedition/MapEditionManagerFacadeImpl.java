package stories.mapedition;

import framework.AbstractManagerFacade;
import framework.EmptyFacadeResult;
import framework.EmptyModelContext;
import services.PropertyService;
import services.PropertyServiceImpl;
import stories.updatemapsetalias.UpdateMapSetAliasFacade;
import stories.updatemapsetalias.UpdateMapSetAliasFacadeImpl;
import stories.updatemapsetalias.UpdateMapSetAliasModelContext;
import stories.updatemapsetinfourl.UpdateMapSetInfoUrlFacade;
import stories.updatemapsetinfourl.UpdateMapSetInfoUrlFacadeImpl;
import stories.updatemapsetinfourl.UpdateMapSetInfoUrlModelContext;
import stories.updatemapsetreleasedate.UpdateMapSetReleaseDateFacade;
import stories.updatemapsetreleasedate.UpdateMapSetReleaseDateFacadeImpl;
import stories.updatemapsetreleasedate.UpdateMapSetReleaseDateModelContext;
import stories.updatemapseturlphoto.UpdateMapSetUrlPhotoFacade;
import stories.updatemapseturlphoto.UpdateMapSetUrlPhotoFacadeImpl;
import stories.updatemapseturlphoto.UpdateMapSetUrlPhotoModelContext;

import java.time.LocalDate;

public class MapEditionManagerFacadeImpl
        extends AbstractManagerFacade<EmptyModelContext, EmptyFacadeResult>
        implements MapEditionManagerFacade {

    public MapEditionManagerFacadeImpl() {
        super(new EmptyModelContext(), EmptyFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(EmptyModelContext facadeModelContext) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(EmptyModelContext facadeModelContext) throws Exception {
        return null;
    }

    @Override
    public String findPropertyValue(String propertyFilePath, String key) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        return propertyService.getPropertyValue(propertyFilePath, key);
    }

    @Override
    public void updateMapSetAlias(String platformName, String profileName, String mapName, String newAlias) throws Exception {
        UpdateMapSetAliasModelContext updateMapSetAliasModelContext = new UpdateMapSetAliasModelContext(
                platformName,
                profileName,
                mapName,
                newAlias
        );
        UpdateMapSetAliasFacade updateMapSetAliasFacade = new UpdateMapSetAliasFacadeImpl(updateMapSetAliasModelContext);
        updateMapSetAliasFacade.execute();
    }

    @Override
    public void updateMapSetUrlPhoto(String platformName, String profileName, String mapName, String mapPreviewUrl) throws Exception {
        UpdateMapSetUrlPhotoModelContext updateMapSetUrlPhotoModelContext = new UpdateMapSetUrlPhotoModelContext(
                platformName,
                profileName,
                mapName,
                mapPreviewUrl
        );
        UpdateMapSetUrlPhotoFacade updateMapSetUrlPhotoFacade = new UpdateMapSetUrlPhotoFacadeImpl(updateMapSetUrlPhotoModelContext);
        updateMapSetUrlPhotoFacade.execute();
    }

    @Override
    public void updateMapSetInfoUrl(String platformName, String profileName, String mapName, String infoUrl) throws Exception {
        UpdateMapSetInfoUrlModelContext updateMapSetInfoUrlModelContext = new UpdateMapSetInfoUrlModelContext(
                platformName,
                profileName,
                mapName,
                infoUrl
        );
        UpdateMapSetInfoUrlFacade updateMapSetInfoUrlFacade = new UpdateMapSetInfoUrlFacadeImpl(updateMapSetInfoUrlModelContext);
        updateMapSetInfoUrlFacade.execute();
    }

    @Override
    public void updateMapSetReleaseDate(String platformName, String profileName, String mapName, LocalDate releaseDate) throws Exception {
        UpdateMapSetReleaseDateModelContext updateMapSetReleaseDateModelContext = new UpdateMapSetReleaseDateModelContext(
                platformName,
                profileName,
                mapName,
                releaseDate
        );
        UpdateMapSetReleaseDateFacade updateMapSetReleaseDateFacade = new UpdateMapSetReleaseDateFacadeImpl(updateMapSetReleaseDateModelContext);
        updateMapSetReleaseDateFacade.execute();
    }
}
