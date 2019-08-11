package stories.mapsedition;

import dtos.MapDto;
import entities.Map;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MapsEditionFacade {
    List<Map> listAllMaps() throws SQLException;
    List<MapDto> getDtos(List<Map> mapList);
    MapDto getDto(Map map);
    String findPropertyValue(String key) throws Exception;
    Map createNewCustomMapFromWorkshop(String urlIdWorkshop, String installationFolder) throws Exception;
    Map createNewCustomMapFromWorkshop(Long idWorkShop, String mapName, String installationFolder) throws Exception;
    MapDto deleteSelectedMap(String mapName) throws SQLException;
    boolean isCorrectInstallationFolder(String installationFolder);
    Optional<Map> findMapByCode(String mapName) throws SQLException;
    Map insertMap(Map map) throws SQLException;
}
