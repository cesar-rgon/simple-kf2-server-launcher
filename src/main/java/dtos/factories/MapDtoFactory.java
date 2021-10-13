package dtos.factories;

import daos.CustomMapModDao;
import daos.OfficialMapDao;
import dtos.AbstractMapDto;
import dtos.CustomMapModDto;
import dtos.OfficialMapDto;
import entities.AbstractMap;
import entities.CustomMapMod;
import entities.OfficialMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MapDtoFactory {

    private static final Logger logger = LogManager.getLogger(MapDtoFactory.class);

    private OfficialMapDto newOfficialMapDto(OfficialMap map) {
        return new OfficialMapDto(
                map.getCode(),
                map.getUrlInfo(),
                map.getUrlPhoto()
        );
    }

    private CustomMapModDto newCustomMapModDto(CustomMapMod map) {
        return new CustomMapModDto(
                map.getCode(),
                map.getUrlInfo(),
                map.getUrlPhoto(),
                map.getIdWorkShop(),
                map.isDownloaded()
        );
    }

    public AbstractMapDto newDto(AbstractMap map) {
        if (map.isOfficial()) {
            return newOfficialMapDto((OfficialMap) map);
        } else {
            return newCustomMapModDto((CustomMapMod) map);
        }
    }

    public ObservableList<AbstractMapDto> newDtos(List<AbstractMap> maps) {
        List<AbstractMapDto> dtoList = maps.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }
}
