package pojos.kf2factory;

import entities.Profile;

import java.nio.file.Path;

public interface Kf2Common {

    boolean isValid(String installationFolder);
    Long getIdWorkShopFromPath(Path path, String installationFolder);
    String joinServer(Profile profile);
    String runServer(Profile profile);
    String runServerByConsole(Profile profile);
    void runExecutableFile();
    void createConfigFolder(String installationFolder, String profileName);
}
