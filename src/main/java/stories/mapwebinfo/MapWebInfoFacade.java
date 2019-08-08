package stories.mapwebinfo;

import entities.Map;

public interface MapWebInfoFacade {
    boolean isMapInDataBase(Long idWorkShop);
    String findPropertyValue(String key) throws Exception;
    boolean isCorrectInstallationFolder(String installationFolder);
    Map createNewCustomMapFromWorkshop(Long idWorkShop, String mapName, String strUrlMapImage, String installationFolder) throws Exception;
}
