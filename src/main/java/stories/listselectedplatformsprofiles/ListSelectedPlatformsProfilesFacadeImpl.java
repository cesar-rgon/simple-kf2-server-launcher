package stories.listselectedplatformsprofiles;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import pojos.PlatformProfileToDisplay;
import pojos.PlatformProfileToDisplayFactory;
import services.ProfileService;
import services.ProfileServiceImpl;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class ListSelectedPlatformsProfilesFacadeImpl
        extends AbstractTransactionalFacade<ListSelectedPlatformsProfilesModelContext, ListSelectedPlatformsProfilesFacadeResult>
        implements ListSelectedPlatformsProfilesFacade {

    public ListSelectedPlatformsProfilesFacadeImpl(ListSelectedPlatformsProfilesModelContext listSelectedPlatformsProfilesModelContext) {
        super(listSelectedPlatformsProfilesModelContext, ListSelectedPlatformsProfilesFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(ListSelectedPlatformsProfilesModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected ListSelectedPlatformsProfilesFacadeResult internalExecute(ListSelectedPlatformsProfilesModelContext facadeModelContext, EntityManager em) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        ProfileService profileService = new ProfileServiceImpl(em);

        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectProfiles");

        List<Profile> fullProfileList = profileService.listAllProfiles();
        List<String> fullProfileNameList = fullProfileList.stream().map(Profile::getName).collect(Collectors.toList());

        PlatformProfileToDisplayFactory platformProfileToDisplayFactory = new PlatformProfileToDisplayFactory(em);
        List<PlatformProfileToDisplay> platformProfileToDisplayList = platformProfileToDisplayFactory.newOnes(facadeModelContext.getPlatformProfileList());

        List<PlatformProfileToDisplay> selectedPlatformProfileList = Utils.selectPlatformProfilesToRunDialog(
                headerText + ":",
                platformProfileToDisplayList,
                fullProfileNameList
        );

        return new ListSelectedPlatformsProfilesFacadeResult(
                selectedPlatformProfileList
        );
    }
}
