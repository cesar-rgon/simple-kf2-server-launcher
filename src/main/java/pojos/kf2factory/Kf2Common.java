package pojos.kf2factory;

import entities.CustomMapMod;
import entities.PlatformProfileMap;
import entities.Profile;

import java.nio.file.Path;
import java.util.List;

public interface Kf2Common {

    boolean isValidInstallationFolder();
    Long getIdWorkShopFromPath(Path path);
    String joinServer(Profile profile);
    String runServer(Profile profile);
    String runServerByConsole(Profile profile);
    void runExecutableFile();
    void createConfigFolder(String installationFolder, String profileName);
    boolean downloadMapFromSteamCmd(CustomMapMod customMap) throws Exception;
    void copyMapToCachePlatform(CustomMapMod customMap) throws Exception;
}
