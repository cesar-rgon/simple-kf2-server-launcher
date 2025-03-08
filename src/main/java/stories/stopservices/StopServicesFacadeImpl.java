package stories.stopservices;

import entities.AbstractPlatform;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.PlatformProfile;
import pojos.PlatformProfileToDisplay;
import pojos.PlatformProfileToDisplayFactory;
import pojos.StopServicesToDisplay;
import pojos.enums.EnumPlatform;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import pojos.session.Session;
import services.*;
import utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class StopServicesFacadeImpl
        extends AbstractTransactionalFacade<StopServicesModelContext, EmptyFacadeResult>
        implements StopServicesFacade {

    private static final Logger logger = LogManager.getLogger(StopServicesFacadeImpl.class);

    public StopServicesFacadeImpl(StopServicesModelContext stopServicesModelContext) {
        super(stopServicesModelContext, EmptyFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(StopServicesModelContext stopServicesModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        List<Profile> allProfileList = profileService.listAllProfiles();
        return !allProfileList.isEmpty();
    }

    @Override
    protected EmptyFacadeResult internalExecute(StopServicesModelContext stopServicesModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        PlatformService platformService = new PlatformServiceImpl(em);

        List<AbstractPlatform> allPlatformList = platformService.listAllPlatforms();
        if (allPlatformList.isEmpty()) {
            Utils.warningDialog("Stop operation aborted!", "No platforms can be found");
            logger.warn("Can not stop services! No platforms can be found");
            return new EmptyFacadeResult();
        }

        List<Profile> allProfileList = profileService.listAllProfiles();
        if (allProfileList.isEmpty()) {
            String message = "No profiles in launcher. The service can not be stopped.";
            logger.error(message);
            Utils.errorDialog(message);
            return new EmptyFacadeResult();
        }

        StopServicesToDisplay stopServices = selectPlatformProfiles(
                allProfileList,
                stopServicesModelContext.getActualSelectedProfileName(),
                stopServicesModelContext.getActualSelectedLanguage(),
                em
        );

        for (PlatformProfileToDisplay platformProfile : stopServices.getSelectedPlatformProfileList()) {
            try {
                List<AbstractPlatform> selectedPlatformList = new ArrayList<AbstractPlatform>();
                if (EnumPlatform.ALL.getDescripcion().equals(platformProfile.getPlatformName())) {
                    selectedPlatformList.addAll(allPlatformList);
                } else {
                    Optional<AbstractPlatform> platformOptional = Optional.empty();
                    if (EnumPlatform.STEAM.getDescripcion().equals(platformProfile.getPlatformName())) {
                        platformOptional = platformService.findPlatformByName(EnumPlatform.STEAM.name());
                    } else {
                        platformOptional = platformService.findPlatformByName(EnumPlatform.EPIC.name());
                    }
                    if (!platformOptional.isPresent()) {
                        continue;
                    }
                    selectedPlatformList.add(platformOptional.get());
                }

                Optional<Profile> profileOptional = profileService.findProfileByCode(platformProfile.getProfileName());
                if (!profileOptional.isPresent()) {
                    continue;
                }
                Profile profile = profileOptional.get();
                boolean uninstallServices = stopServices.isUninstallServices();

                for (AbstractPlatform platform: selectedPlatformList) {
                    Session.getInstance().setConsole(
                            (StringUtils.isNotBlank(Session.getInstance().getConsole())? Session.getInstance().getConsole() + "\n\n" : "") +
                                    "< " + new Date() + " - Stop Service >\n" +
                                    stopService(platform, profile, uninstallServices, em)
                    );
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        return new EmptyFacadeResult();
    }

    private StopServicesToDisplay selectPlatformProfiles(List<Profile> allProfileList, String actualSelectedProfileName, String actualSelectedLanguage, EntityManager em) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        PlatformService platformService = new PlatformServiceImpl(em);

        String message = propertyService.getPropertyValue("properties/languages/" + actualSelectedLanguage + ".properties",
                "prop.message.stopServices");
        List<AbstractPlatform> allPlatformList = platformService.listAllPlatforms();
        List<PlatformProfile> platformProfileList = new ArrayList<PlatformProfile>();
        for (Profile profile: allProfileList) {
            for (AbstractPlatform platform: allPlatformList) {
                platformProfileList.add(new PlatformProfile(platform, profile));
            }
        }
        List<String> selectedProfileNameList = new ArrayList<String>();
        selectedProfileNameList.add(actualSelectedProfileName);

        PlatformProfileToDisplayFactory platformProfileToDisplayFactory = new PlatformProfileToDisplayFactory(em);
        List<PlatformProfileToDisplay> platformProfileToDisplayList = platformProfileToDisplayFactory.newOnes(platformProfileList);

        return Utils.selectPlatformProfilesToStopDialog(message + ":", platformProfileToDisplayList, selectedProfileNameList);
    }

    private String stopService(AbstractPlatform platform, Profile profile, boolean uninstallService, EntityManager em) {
        Kf2Common kf2Common = Kf2Factory.getInstance(platform, em);
        assert kf2Common != null;
        return kf2Common.stopService(profile, uninstallService);
    }
}
