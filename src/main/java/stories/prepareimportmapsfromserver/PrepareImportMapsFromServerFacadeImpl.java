package stories.prepareimportmapsfromserver;

import dtos.PlatformDto;
import dtos.factories.PlatformDtoFactory;
import entities.AbstractPlatform;
import entities.EpicPlatform;
import entities.Profile;
import entities.SteamPlatform;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import old.mapsedition.OldMapsEditionFacadeImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.MapToDisplay;
import pojos.PlatformProfile;
import pojos.PlatformProfileToDisplay;
import pojos.PlatformProfileToDisplayFactory;
import pojos.enums.EnumPlatform;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import services.PlatformService;
import services.PlatformServiceImpl;
import services.ProfileService;
import services.ProfileServiceImpl;
import utils.Utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PrepareImportMapsFromServerFacadeImpl
        extends AbstractTransactionalFacade<PrepareImportMapsFromServerModelContext, PrepareImportMapsFromServerFacadeResult>
        implements PrepareImportMapsFromServerFacade {

    private static final Logger logger = LogManager.getLogger(PrepareImportMapsFromServerFacadeImpl.class);

    public PrepareImportMapsFromServerFacadeImpl(PrepareImportMapsFromServerModelContext prepareImportMapsFromServerModelContext) {
        super(prepareImportMapsFromServerModelContext, PrepareImportMapsFromServerFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(PrepareImportMapsFromServerModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected PrepareImportMapsFromServerFacadeResult internalExecute(PrepareImportMapsFromServerModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        PlatformDtoFactory platformDtoFactory = new PlatformDtoFactory();

        List<Profile> allProfiles = profileService.listAllProfiles();
        if (allProfiles.isEmpty()) {
            throw new RuntimeException("No profiles could be found");
        }

        List<AbstractPlatform> allPlatforms = listAllPlatforms(em);
        Optional<AbstractPlatform> steamPlatformOptional = allPlatforms.stream()
                .filter(p -> p.getCode().equals(EnumPlatform.STEAM.name()))
                .findFirst();

        Optional<AbstractPlatform> epicPlatformOptional = allPlatforms.stream()
                .filter(p -> p.getCode().equals(EnumPlatform.EPIC.name()))
                .findFirst();

        if (!steamPlatformOptional.isPresent() || !epicPlatformOptional.isPresent()) {
            throw new RuntimeException("Not all the platforms could be found");
        }

        for (AbstractPlatform platform: allPlatforms) {
            if (!isCorrectInstallationFolder(platform.getCode(), em)) {
                String message = "Invalid " + platform.getDescription() + " installation folder was found. Define in Install/Update section:" + platform.getInstallationFolder();
                throw new RuntimeException(message);
            }
            if (!Files.exists(Paths.get(platform.getInstallationFolder() + "/KFGame/Cache"))) {
                Files.createDirectory(Paths.get(platform.getInstallationFolder() + "/KFGame/Cache"));
            }
        }

        List<PlatformProfileToDisplay> platformProfileToDisplayList = getPlatformProfileToDisplayList(allPlatforms, allProfiles, facadeModelContext.getProfileName(), em);
        List<String> fullProfileNameList = EnumPlatform.listAll().stream().map(EnumPlatform::name).collect(Collectors.toList());

        if (platformProfileToDisplayList.isEmpty()) {
            throw new RuntimeException("No platforms and profiles could be found");
        }

        AbstractPlatform steamPlatform = steamPlatformOptional.get();
        AbstractPlatform epicPlatform = epicPlatformOptional.get();

        Kf2Common steamKf2Common = Kf2Factory.getInstance(steamPlatform, em);
        Kf2Common epicKf2Common = Kf2Factory.getInstance(epicPlatform, em);

        // CUSTOM MAP / MOD LIST FOR STEAM
        List<MapToDisplay> steamCustomMapModList = Files.walk(Paths.get(steamPlatform.getInstallationFolder() + "/KFGame/Cache"))
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().toUpperCase().startsWith("KF-"))
                .filter(path -> path.getFileName().toString().toUpperCase().endsWith(".KFM"))
                .map(path -> {
                    String filenameWithExtension = path.getFileName().toString();
                    String[] array = filenameWithExtension.split("\\.");
                    String mapName = array[0];
                    Long idWorkShop = steamKf2Common.getIdWorkShopFromPath(path.getParent());
                    return new MapToDisplay(idWorkShop, mapName);
                })
                .collect(Collectors.toList());

        File[] steamCacheFolderList = new File(steamPlatform.getInstallationFolder() + "/KFGame/Cache").listFiles();
        if (steamCacheFolderList != null && steamCacheFolderList.length > 0) {
            List<Long> idWorkShopCustomMapList = steamCustomMapModList.stream().map(MapToDisplay::getIdWorkShop).collect(Collectors.toList());
            steamCustomMapModList.addAll(
                    Arrays.stream(steamCacheFolderList)
                            .filter(file -> file.isDirectory())
                            .map(file -> file.toPath())
                            .filter(path -> !idWorkShopCustomMapList.contains(steamKf2Common.getIdWorkShopFromPath(path)))
                            .map(path -> {
                                Long idWorkShop = Long.parseLong(path.getFileName().toString());
                                return new MapToDisplay(idWorkShop, "MOD [" + idWorkShop + "]");
                            })
                            .collect(Collectors.toList())
            );
        }

        // CUSTOM MAP / MOD LIST FOR EPIC
        List<MapToDisplay> epicCustomMapModList = Files.walk(Paths.get(epicPlatform.getInstallationFolder() + "/KFGame/Cache"))
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().toUpperCase().startsWith("KF-"))
                .filter(path -> path.getFileName().toString().toUpperCase().endsWith(".KFM"))
                .map(path -> {
                    String filenameWithExtension = path.getFileName().toString();
                    String[] array = filenameWithExtension.split("\\.");
                    String mapName = array[0];
                    Long idWorkShop = epicKf2Common.getIdWorkShopFromPath(path.getParent());
                    return new MapToDisplay(idWorkShop, mapName);
                })
                .collect(Collectors.toList());

        File[] epicCacheFolderList = new File(epicPlatform.getInstallationFolder() + "/KFGame/Cache").listFiles();
        if (epicCacheFolderList != null && epicCacheFolderList.length > 0) {
            List<Long> idWorkShopCustomMapList = epicCustomMapModList.stream().map(MapToDisplay::getIdWorkShop).collect(Collectors.toList());
            epicCustomMapModList.addAll(
                    Arrays.stream(epicCacheFolderList)
                            .filter(file -> file.isDirectory())
                            .map(file -> file.toPath())
                            .filter(path -> !idWorkShopCustomMapList.contains(epicKf2Common.getIdWorkShopFromPath(path)))
                            .map(path -> {
                                Long idWorkShop = Long.parseLong(path.getFileName().toString());
                                return new MapToDisplay(idWorkShop, "MOD [" + idWorkShop + "]");
                            })
                            .collect(Collectors.toList())
            );
        }

        // OFFICIAL MAP LIST FOR STEAM
        List<String> steamOfficialMapNameList = Files.walk(Paths.get( steamPlatform.getInstallationFolder() + "/KFGame/BrewedPC/Maps"))
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().toUpperCase().startsWith("KF-"))
                .filter(path -> path.getFileName().toString().toUpperCase().endsWith(".KFM"))
                .map(path -> {
                    String filenameWithExtension = path.getFileName().toString();
                    String[] array = filenameWithExtension.split("\\.");
                    return array[0];
                })
                .collect(Collectors.toList());

        // OFFICIAL MAP LIST FOR EPIC
        List<String> epicOfficialMapNameList = Files.walk(Paths.get(epicPlatform.getInstallationFolder() + "/KFGame/BrewedPC/Maps"))
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().toUpperCase().startsWith("KF-"))
                .filter(path -> path.getFileName().toString().toUpperCase().endsWith(".KFM"))
                .map(path -> {
                    String filenameWithExtension = path.getFileName().toString();
                    String[] array = filenameWithExtension.split("\\.");
                    return array[0];
                })
                .collect(Collectors.toList());

        return new PrepareImportMapsFromServerFacadeResult(
                platformProfileToDisplayList,
                fullProfileNameList,
                platformDtoFactory.newDto(steamPlatform),
                platformDtoFactory.newDto(epicPlatform),
                steamOfficialMapNameList,
                epicOfficialMapNameList,
                steamCustomMapModList,
                epicCustomMapModList
        );
    }


    private List<AbstractPlatform> listAllPlatforms(EntityManager em) throws SQLException {
        PlatformService platformService = new PlatformServiceImpl(em);

        List<AbstractPlatform> allPlatforms = new ArrayList<AbstractPlatform>();
        Optional<SteamPlatform> steamPlatformOptional = platformService.findSteamPlatform();
        steamPlatformOptional.ifPresent(allPlatforms::add);
        Optional<EpicPlatform> epicPlatformOptional = platformService.findEpicPlatform();
        epicPlatformOptional.ifPresent(allPlatforms::add);
        return allPlatforms;
    }

    private boolean isCorrectInstallationFolder(String platformName, EntityManager em) {
        PlatformService platformService = new PlatformServiceImpl(em);

        try {
            if (EnumPlatform.STEAM.name().equals(platformName)) {
                Optional<SteamPlatform> steamPlatformOptional = platformService.findSteamPlatform();
                if (!steamPlatformOptional.isPresent()) {
                    return false;
                }

                Kf2Common steamKf2Common = Kf2Factory.getInstance(
                        steamPlatformOptional.get(), em
                );

                return steamKf2Common.isValidInstallationFolder();
            }

            if (EnumPlatform.EPIC.name().equals(platformName)) {
                Optional<EpicPlatform> epicPlatformOptional = platformService.findEpicPlatform();
                if (!epicPlatformOptional.isPresent()) {
                    return false;
                }

                Kf2Common epicKf2Common = Kf2Factory.getInstance(
                        epicPlatformOptional.get(), em
                );

                return epicKf2Common.isValidInstallationFolder();
            }

            return false;
        } catch (SQLException e) {
            logger.error("Error validating the installation folder", e);
            return false;
        }
    }


    private List<PlatformProfileToDisplay> getPlatformProfileToDisplayList(List<AbstractPlatform> allPlatforms, List<Profile> allProfiles, String defaultSelectedProfileName, EntityManager em) throws Exception {
        PlatformProfileToDisplayFactory platformProfileToDisplayFactory = new PlatformProfileToDisplayFactory(em);

        List<PlatformProfile> platformProfileList = new ArrayList<PlatformProfile>();
        for (Profile profile: allProfiles) {
            for (AbstractPlatform platform: allPlatforms) {
                platformProfileList.add(new PlatformProfile(platform, profile));
            }
        }
        return platformProfileToDisplayFactory.newOnes(platformProfileList);
    }
}
