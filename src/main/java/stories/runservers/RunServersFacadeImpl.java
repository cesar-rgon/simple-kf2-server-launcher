package stories.runservers;

import entities.AbstractPlatform;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import pojos.PlatformProfile;
import pojos.PlatformProfileToDisplay;
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

public class RunServersFacadeImpl
        extends AbstractTransactionalFacade<RunServersModelContext, EmptyFacadeResult>
        implements RunServersFacade {

    public RunServersFacadeImpl(RunServersModelContext runServersModelContext) {
        super(runServersModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(RunServersModelContext runServersModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        PlatformService platformService = new PlatformServiceImpl(em);

        Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(runServersModelContext.getPlatformName());
        if (!platformOptional.isPresent()) {
            Utils.warningDialog("Run operation aborted!", "The platform can not be found");
            throw new RuntimeException("Can not execute the server. The platform can not be found");
        }

        List<Profile> allProfileList = profileService.listAllProfiles();
        switch (allProfileList.size()) {
            case 0:
                runServer(platformOptional.get(), Optional.empty());
                break;

            case 1:
                runExecutableFile(platformOptional.get());
                Session.getInstance().setConsole(
                        (StringUtils.isNotBlank(Session.getInstance().getConsole())? Session.getInstance().getConsole() + "\n\n" : "") +
                        "< " + new Date() + " - Run Server >\n" +
                        runServer(platformOptional.get(), Optional.ofNullable(allProfileList.get(0)))
                );
                break;

            default:
                runExecutableFile(platformOptional.get());
                List<Profile> selectedProfileList = selectProfiles(
                        allProfileList,
                        runServersModelContext.getActualSelectedProfileName(),
                        runServersModelContext.getActualSelectedLanguage(),
                        em
                );
                assert selectedProfileList != null;
                for (Profile profile: selectedProfileList) {
                    Session.getInstance().setConsole(
                            (StringUtils.isNotBlank(Session.getInstance().getConsole())? Session.getInstance().getConsole() + "\n\n" : "") +
                            "< " + new Date() + " - Run Server >\n" +
                            runServer(platformOptional.get(), Optional.ofNullable(profile))
                    );
                }
                break;
        }

        return new EmptyFacadeResult();
    }

    private void runExecutableFile(AbstractPlatform platform) {
        Kf2Common kf2Common = Kf2Factory.getInstance(platform);
        assert kf2Common != null;
        kf2Common.runExecutableFile();
    }

    private String runServer(AbstractPlatform platform, Optional<Profile> profileOptional) {
        Kf2Common kf2Common = Kf2Factory.getInstance(platform);
        assert kf2Common != null;
        return kf2Common.runServer(profileOptional.orElse(null));
    }

    private List<Profile> selectProfiles(List<Profile> allProfileList, String actualSelectedProfileName, String actualSelectedLanguage, EntityManager em) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        PlatformService platformService = new PlatformServiceImpl(em);
        ProfileService profileService = new ProfileServiceImpl(em);

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

        List<PlatformProfileToDisplay> selectedProfiles = Utils.selectPlatformProfilesDialog(message + ":", platformProfileList, selectedProfileNameList);
        List<String> selectedProfileNames = selectedProfiles.stream().map(PlatformProfileToDisplay::getProfileName).collect(Collectors.toList());

        return selectedProfileNames.stream().map(profileName -> {
            try {
                Optional<Profile> profileOptional = profileService.findProfileByCode(profileName);
                return profileOptional.orElse(null);
            } catch (Exception e) {
                return null;
            }
        }).collect(Collectors.toList());
    }
}
