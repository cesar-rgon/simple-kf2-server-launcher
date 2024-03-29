package stories.joinservers;

import entities.AbstractPlatform;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import pojos.PlatformProfile;
import pojos.PlatformProfileToDisplay;
import pojos.PlatformProfileToDisplayFactory;
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

public class JoinServersFacadeImpl
        extends AbstractTransactionalFacade<JoinServersModelContext, EmptyFacadeResult>
        implements JoinServersFacade {

    public JoinServersFacadeImpl(JoinServersModelContext joinServersModelContext) {
        super(joinServersModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions(JoinServersModelContext joinServersModelContext, EntityManager em) throws Exception {
        PlatformService platformService = new PlatformServiceImpl(em);
        return platformService.isValidInstallationFolder(joinServersModelContext.getPlatformName());
    }

    @Override
    protected EmptyFacadeResult internalExecute(JoinServersModelContext joinServerModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        PlatformService platformService = new PlatformServiceImpl(em);

        Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(joinServerModelContext.getPlatformName());
        if (!platformOptional.isPresent()) {
            Utils.warningDialog("Join operation aborted!", "The platform can not be found");
            throw new RuntimeException("Can not join to the server. The platform can not be found");
        }

        List<Profile> allProfileList = profileService.listAllProfiles();
        switch (allProfileList.size()) {
            case 0:
                joinServer(platformOptional.get(), Optional.empty(), em);
                break;

            case 1:
                Session.getInstance().setConsole(
                        (StringUtils.isNotBlank(Session.getInstance().getConsole())? Session.getInstance().getConsole() + "\n\n" : "") +
                                "< " + new Date() + " - Join Server >\n" +
                                joinServer(platformOptional.get(), Optional.ofNullable(allProfileList.get(0)), em)
                );
                break;

            default:
                Profile selectedProfile = selectProfile(
                        allProfileList,
                        joinServerModelContext.getActualSelectedProfileName(),
                        joinServerModelContext.getActualSelectedLanguage(),
                        em
                );
                Session.getInstance().setConsole(
                        (StringUtils.isNotBlank(Session.getInstance().getConsole())? Session.getInstance().getConsole() + "\n\n" : "") +
                                "< " + new Date() + " - Join Server >\n" +
                                joinServer(platformOptional.get(), Optional.ofNullable(selectedProfile), em)
                );
                break;
        }

        return new EmptyFacadeResult();
    }

    private String joinServer(AbstractPlatform platform, Optional<Profile> profileOptional, EntityManager em) {
        Kf2Common kf2Common = Kf2Factory.getInstance(platform, em);
        assert kf2Common != null;
        return kf2Common.joinServer(profileOptional.orElse(null));
    }

    private Profile selectProfile(List<Profile> allProfileList, String actualSelectedProfileName, String actualSelectedLanguage, EntityManager em) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        PlatformService platformService = new PlatformServiceImpl(em);
        ProfileService profileService = new ProfileServiceImpl(em);

        String message = propertyService.getPropertyValue("properties/languages/" + actualSelectedLanguage + ".properties",
                "prop.message.joinServer");
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

        Optional<PlatformProfileToDisplay> selectedProfileOptional = Utils.selectProfileDialog(message + ":", platformProfileToDisplayList, selectedProfileNameList);
        if (!selectedProfileOptional.isPresent()) {
            return null;
        }
        String profileName = selectedProfileOptional.get().getProfileName();
        Optional<Profile> profileOptional = profileService.findProfileByCode(profileName);
        return profileOptional.orElse(null);
    }
}
