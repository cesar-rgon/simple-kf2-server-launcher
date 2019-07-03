package stories.mapsedition;

import dtos.MapDto;

import java.sql.SQLException;
import java.util.List;

public interface MapsEditionFacade {
    List<MapDto> listAllMaps() throws SQLException;
    String findPropertyValue(String key) throws SQLException;
    MapDto createNewCustomMap(String mapName, Long idWorkShop, String urlPhoto) throws SQLException;
}
