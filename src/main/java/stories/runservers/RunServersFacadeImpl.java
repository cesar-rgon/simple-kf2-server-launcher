package stories.runservers;

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
import pojos.enums.EnumPlatform;
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

public class RunServersFacadeImpl
        extends AbstractTransactionalFacade<RunServersModelContext, EmptyFacadeResult>
        implements RunServersFacade {

    private static final Logger logger = LogManager.getLogger(RunServersFacadeImpl.class);

    public RunServersFacadeImpl(RunServersModelContext runServersModelContext) {
        super(runServersModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions(RunServersModelContext runServersModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(RunServersModelContext runServersModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        PlatformService platformService = new PlatformServiceImpl(em);

        List<AbstractPlatform> allPlatformList = platformService.listAllPlatforms();
        if (allPlatformList.isEmpty()) {
            Utils.warningDialog("Run operation aborted!", "No platforms can be found");
            logger.warn("Run operation aborted! No platforms can be found");
            return new EmptyFacadeResult();
        }

        List<Profile> allProfileList = profileService.listAllProfiles();
        if (allProfileList.isEmpty()) {
            String message = "No profiles in launcher. The server can not be executed.";
            logger.error(message);
            Utils.errorDialog(message);
            return new EmptyFacadeResult();
        }

        List<PlatformProfileToDisplay> selectedPlatformProfileList = selectPlatformProfiles(
                allProfileList,
                runServersModelContext.getActualSelectedProfileName(),
                runServersModelContext.getActualSelectedLanguage(),
                em
        );


        for (PlatformProfileToDisplay platformProfile : selectedPlatformProfileList) {
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

                for (AbstractPlatform platform: selectedPlatformList) {
                    runExecutableFile(platform, em);
                    Session.getInstance().setConsole(
                            (StringUtils.isNotBlank(Session.getInstance().getConsole()) ? Session.getInstance().getConsole() + "\n\n" : "") +
                                    "< " + new Date() + " - " + (EnumRunServer.TERMINAL.equals(runServersModelContext.getEnumRunServer()) ? "Run Server" : "Run Service") + " >\n" +
                                    runServer(platform, profile, runServersModelContext.getEnumRunServer(), em)
                    );
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        return new EmptyFacadeResult();
    }

    private void runExecutableFile(AbstractPlatform platform, EntityManager em) {
        Kf2Common kf2Common = Kf2Factory.getInstance(platform, em);
        assert kf2Common != null;
        kf2Common.runExecutableFile();
    }

    private String runServer(AbstractPlatform platform, Profile profile, EnumRunServer enumRunServer, EntityManager em) {
        Kf2Common kf2Common = Kf2Factory.getInstance(platform, em);
        assert kf2Common != null;
        return kf2Common.runServer(profile, enumRunServer);
    }

    private List<PlatformProfileToDisplay> selectPlatformProfiles(List<Profile> allProfileList, String actualSelectedProfileName, String actualSelectedLanguage, EntityManager em) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        PlatformService platformService = new PlatformServiceImpl(em);

        String message = propertyService.getPropertyValue("properties/languages/" + actualSelectedLanguage + ".properties",
                "prop.message.runServers");
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

        return Utils.selectPlatformProfilesToRunDialog(message + ":", platformProfileToDisplayList, selectedProfileNameList);
    }
}
