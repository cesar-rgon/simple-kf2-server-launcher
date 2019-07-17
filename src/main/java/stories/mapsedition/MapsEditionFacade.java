package stories.mapsedition;

import dtos.MapDto;
import entities.Map;

import java.sql.SQLException;
import java.util.List;

public interface MapsEditionFacade {
    List<Map> listAllMaps() throws SQLException;
    List<MapDto> getDtos(List<Map> mapList);
    MapDto getDto(Map map);
    String findPropertyValue(String key) throws SQLException;
    Map createNewCustomMap(String mapName, Long idWorkShop, String urlPhoto) throws SQLException;
    MapDto deleteSelectedMap(String mapName) throws SQLException;
    boolean isCorrectInstallationFolder(String installationFolder);
}
