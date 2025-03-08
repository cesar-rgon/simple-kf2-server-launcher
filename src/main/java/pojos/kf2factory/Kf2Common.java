package pojos.kf2factory;

import entities.CustomMapMod;
import entities.Profile;
import pojos.enums.EnumRunServer;

import java.nio.file.Path;

public interface Kf2Common {

    boolean isValidInstallationFolder();
    Long getIdWorkShopFromPath(Path path);
    String joinServer(Profile profile);
    String runServer(Profile profile, EnumRunServer enumRunServer);
    String runServerByConsole(Profile profile);
    void runExecutableFile();
    void createConfigFolder(String installationFolder, String profileName);
    boolean downloadMapFromSteamCmd(CustomMapMod customMap) throws Exception;
    String copyMapToCachePlatform(CustomMapMod customMap) throws Exception;
    String stopService(Profile profile, boolean uninstallService);
    void checkStatusServices();
}
