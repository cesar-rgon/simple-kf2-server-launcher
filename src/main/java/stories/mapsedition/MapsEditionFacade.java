package stories.mapsedition;

import dtos.MapDto;
import entities.Map;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MapsEditionFacade {
    List<Map> listAllMapsAndMods() throws SQLException;
    List<MapDto> getDtos(List<Map> mapList);
    MapDto getDto(Map map);
    String findPropertyValue(String key) throws Exception;
    Map createNewCustomMapFromWorkshop(Long idWorkShop, String installationFolder, boolean downloaded, Boolean isMod) throws Exception;
    Map createNewCustomMapFromWorkshop(Long idWorkShop, String mapName, String installationFolder, boolean downloaded, Boolean isMod) throws Exception;
    MapDto deleteSelectedMap(String mapName) throws SQLException;
    boolean isCorrectInstallationFolder(String installationFolder);
    Optional<Map> findMapOrModByCode(String mapName) throws SQLException;
    Map insertMap(Map map) throws SQLException;
    Optional<Map> findMapOrModByIdWorkShop(Long idWorkShop) throws SQLException;
}
