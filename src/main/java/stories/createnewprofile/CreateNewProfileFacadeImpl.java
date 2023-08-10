package stories.createnewprofile;

import dtos.factories.ProfileDtoFactory;
import entities.*;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import services.*;
import start.MainApplication;

import java.io.File;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CreateNewProfileFacadeImpl
        extends AbstractTransactionalFacade<CreateNewProfileModelContext, CreateNewProfileFacadeResult>
        implements CreateNewProfileFacade {

    private static final Logger logger = LogManager.getLogger(CreateNewProfileFacadeImpl.class);

    public CreateNewProfileFacadeImpl(CreateNewProfileModelContext createNewProfileModelContext) {
        super(createNewProfileModelContext, CreateNewProfileFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(CreateNewProfileModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected CreateNewProfileFacadeResult internalExecute(CreateNewProfileModelContext facadeModelContext, EntityManager em) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        MaxPlayersServiceImpl maxPlayersService = new MaxPlayersServiceImpl(em);
        OfficialMapServiceImpl officialMapService = new OfficialMapServiceImpl(em);
        LanguageServiceImpl languageService = new LanguageServiceImpl(em);
        GameTypeServiceImpl gameTypeService = new GameTypeServiceImpl(em);
        DifficultyServiceImpl difficultyService = new DifficultyServiceImpl(em);
        LengthServiceImpl lengthService = new LengthServiceImpl(em);
        ProfileService profileService = new ProfileServiceImpl(em);
        PlatformService platformService = new PlatformServiceImpl(em);
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);
        ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory(em);

        String defaultServername = propertyService.getPropertyValue("properties/config.properties", "prop.config.defaultServername");
        String defaultWebPort = propertyService.getPropertyValue("properties/config.properties", "prop.config.defaultWebPort");
        String defaultGamePort = propertyService.getPropertyValue("properties/config.properties", "prop.config.defaultGamePort");
        String defaultQueryPort = propertyService.getPropertyValue("properties/config.properties", "prop.config.defaultQueryPort");
        Optional<MaxPlayers> defaultMaxPlayers = maxPlayersService.findByCode("6");
        String webServerIp = propertyService.getPropertyValue("properties/config.properties", "prop.config.webServerIp");
        String webServerPort = propertyService.getPropertyValue("properties/config.properties", "prop.config.webServerPort");

        List<AbstractMap> officialMaps = officialMapService.listAllMaps();
        OfficialMap firstOfficialMap = null;
        if (officialMaps != null && !officialMaps.isEmpty()) {
            firstOfficialMap = (OfficialMap) officialMaps.get(0);
        }

        URL imagesUrl = getClass().getClassLoader().getResource("images/");
        assert imagesUrl != null;
        File undertowFolder = new File(MainApplication.getAppData() + "/.undertow");
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        String timestampStr = StringUtils.replace(timestamp.toString(), " ", "_");
        timestampStr = StringUtils.replace(timestampStr, ":", "_");
        timestampStr = StringUtils.replace(timestampStr, ".", "_");
        File sourceFile = new File(imagesUrl.getPath() + "/default-banner.png");
        File targetFile = new File(undertowFolder.getAbsolutePath() + "/" + facadeModelContext.getProfileName().toLowerCase() + "_" + timestampStr + ".png");
        FileUtils.copyFile(sourceFile, targetFile);

        Profile newProfile = new Profile(
                facadeModelContext.getProfileName(),
                languageService.listAll().get(0),
                gameTypeService.listAll().get(0),
                firstOfficialMap,
                difficultyService.listAll().get(0),
                lengthService.listAll().get(0),
                defaultMaxPlayers.isPresent()? defaultMaxPlayers.get(): null,
                StringUtils.isNotBlank(defaultServername) ? defaultServername: "Killing Floor 2 Server",
                null,
                true,
                null,
                Integer.parseInt(defaultWebPort),
                Integer.parseInt(defaultGamePort),
                Integer.parseInt(defaultQueryPort),
                null,
                null,
                "http://" + webServerIp + ":" + webServerPort + "/" + facadeModelContext.getProfileName().toLowerCase(),
                null,
                null,
                false,
                true,
                false,
                true,
                true,
                60.0,
                true,
                0.66,
                true,
                false,
                true,
                false,
                null,
                true,
                10.0,
                0.0,
                true,
                90,
                4,
                2,
                true,
                true,
                0.0
        );

        Profile savedProfile = profileService.createItem(newProfile);
        List<AbstractPlatform> validPlatformList = new ArrayList<AbstractPlatform>();
        Optional<SteamPlatform> steamPlatformOptional = platformService.findSteamPlatform();
        if (steamPlatformOptional.isPresent()) {
            if (Kf2Factory.getInstance(steamPlatformOptional.get(), em).isValidInstallationFolder()) {
                validPlatformList.add(steamPlatformOptional.get());
            }
        }
        Optional<EpicPlatform> epicPlatformOptional = platformService.findEpicPlatform();
        if (epicPlatformOptional.isPresent()) {
            if (Kf2Factory.getInstance(epicPlatformOptional.get(), em).isValidInstallationFolder()) {
                validPlatformList.add(epicPlatformOptional.get());
            }
        }

        for (AbstractMap map: officialMaps) {
            for (AbstractPlatform platform: validPlatformList) {
                try {
                    PlatformProfileMap ppm = new PlatformProfileMap(platform, savedProfile, map, map.getReleaseDate(), map.getUrlInfo(), map.getUrlPhoto(), true);
                    platformProfileMapService.createItem(ppm);
                } catch (Exception e) {
                    logger.error("Error creating the relation between the profile: " + savedProfile.getName() + " and the map: " + map.getCode(), e);
                }
            }
        }

        for (AbstractPlatform platform: validPlatformList) {
            Kf2Common kf2Common = Kf2Factory.getInstance(platform, em);
            kf2Common.createConfigFolder(platform.getInstallationFolder(), newProfile.getName());
        }

        if (MainApplication.getEmbeddedWebServer() != null) {
            MainApplication.getEmbeddedWebServer().stop();
            MainApplication.setEmbeddedWebServer(null);
        }

        return new CreateNewProfileFacadeResult(
                profileDtoFactory.newDto(savedProfile)
        );
    }
}
