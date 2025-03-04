package stories.stopservices;

import entities.AbstractPlatform;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import pojos.PlatformProfile;
import pojos.PlatformProfileToDisplay;
import pojos.PlatformProfileToDisplayFactory;
import pojos.StopServicesToDisplay;
import pojos.enums.EnumRunServer;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import pojos.session.Session;
import services.*;
import utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StopServicesFacadeImpl
        extends AbstractTransactionalFacade<StopServicesModelContext, EmptyFacadeResult>
        implements StopServicesFacade {

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
        List<Profile> allProfileList = profileService.listAllProfiles();
        PlatformService platformService = new PlatformServiceImpl(em);

        Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(stopServicesModelContext.getPlatformName());
        if (!platformOptional.isPresent()) {
            Utils.warningDialog("Stop operation aborted!", "The platform can not be found");
            throw new RuntimeException("Can not stop services. The platform can not be found");
        }

        StopServicesToDisplay stopServicesToDisplay = selectProfiles(
                allProfileList,
                stopServicesModelContext.getActualSelectedProfileName(),
                stopServicesModelContext.getActualSelectedLanguage(),
                em
        );

        List<PlatformProfileToDisplay> selectedProfiles = stopServicesToDisplay.getSelectedProfiles();
        List<String> selectedProfileNames = selectedProfiles.stream().map(PlatformProfileToDisplay::getProfileName).collect(Collectors.toList());

        List<Profile> selectedProfileList = selectedProfileNames.stream().map(profileName -> {
            try {
                Optional<Profile> profileOptional = profileService.findProfileByCode(profileName);
                return profileOptional.orElse(null);
            } catch (Exception e) {
                return null;
            }
        }).collect(Collectors.toList());

        boolean uninstallServices = stopServicesToDisplay.isUninstallServices();

        assert selectedProfileList != null;
        for (Profile profile: selectedProfileList) {
            Session.getInstance().setConsole(
                    (StringUtils.isNotBlank(Session.getInstance().getConsole())? Session.getInstance().getConsole() + "\n\n" : "") +
                            "< " + new Date() + " - Stop Service >\n" +
                            stopService(platformOptional.get(), Optional.ofNullable(profile), uninstallServices, em)
            );
        }

        return new EmptyFacadeResult();
    }

    private StopServicesToDisplay selectProfiles(List<Profile> allProfileList, String actualSelectedProfileName, String actualSelectedLanguage, EntityManager em) throws Exception {
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

    private String stopService(AbstractPlatform platform, Optional<Profile> profileOptional, boolean uninstallService, EntityManager em) {
        Kf2Common kf2Common = Kf2Factory.getInstance(platform, em);
        assert kf2Common != null;
        return kf2Common.stopService(profileOptional.orElse(null), uninstallService);
    }
}
