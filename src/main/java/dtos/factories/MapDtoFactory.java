package dtos.factories;

import daos.OfficialMapDao;
import dtos.AbstractMapDto;
import dtos.CustomMapModDto;
import dtos.ImportedDateByProfileDto;
import dtos.OfficialMapDto;
import entities.AbstractMap;
import entities.CustomMapMod;
import entities.OfficialMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapDtoFactory {

    private static final Logger logger = LogManager.getLogger(MapDtoFactory.class);
    private final PropertyService propertyService;
    private String datePattern;
    private String unknownStr;

    public MapDtoFactory() {
        super();
        this.propertyService = new PropertyServiceImpl();
        try {
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            this.datePattern = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.code.dateHourPattern");
            this.unknownStr = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.unknown");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            this.datePattern = "yyyy-MM-dd HH:mm";
            this.unknownStr = "Unknown";
        }
    }

    private OfficialMapDto newOfficialMapDto(OfficialMap map) {

        DateFormat importedDateFormat = new SimpleDateFormat(datePattern);
        List<ImportedDateByProfileDto> importedDateByProfileDtoList = new ArrayList<ImportedDateByProfileDto>();
        map.getProfileMapList().forEach(pm -> {
            importedDateByProfileDtoList.add(
                new ImportedDateByProfileDto(
                    pm.getProfile().getName(),
                    pm.getImportedDate() != null ? importedDateFormat.format(pm.getImportedDate()): unknownStr
                )
            );
        });

        LocalDate releaseDate = map.getReleaseDate() != null ? map.getReleaseDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null;
        return new OfficialMapDto(
                map.getCode(),
                map.getUrlInfo(),
                map.getUrlPhoto(),
                releaseDate,
                importedDateByProfileDtoList
        );
    }

    private CustomMapModDto newCustomMapModDto(CustomMapMod map) {

        DateFormat importedDateFormat = new SimpleDateFormat(datePattern);
        List<ImportedDateByProfileDto> importedDateByProfileDtoList = new ArrayList<ImportedDateByProfileDto>();
        map.getProfileMapList().forEach(pm -> {
            importedDateByProfileDtoList.add(
                    new ImportedDateByProfileDto(
                            pm.getProfile().getName(),
                            pm.getImportedDate() != null ? importedDateFormat.format(pm.getImportedDate()): unknownStr
                    )
            );
        });

        LocalDate releaseDate = map.getReleaseDate() != null ? map.getReleaseDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null;

        return new CustomMapModDto(
                map.getCode(),
                map.getUrlInfo(),
                map.getUrlPhoto(),
                map.getIdWorkShop(),
                releaseDate,
                importedDateByProfileDtoList
        );
    }

    public AbstractMapDto newDto(AbstractMap map) {
        try {
            List<Integer> idsMapasOficiales = OfficialMapDao.getInstance().listAll().stream().map(OfficialMap::getId).collect(Collectors.toList());
            if (idsMapasOficiales.contains(map.getId())) {
                map.setOfficial(true);
                return newOfficialMapDto((OfficialMap) map);
            } else {
                map.setOfficial(false);
                return newCustomMapModDto((CustomMapMod) map);
            }
        } catch (SQLException e) {
            logger.error("Error getting dto of map entity " + map.getCode(), e);
        }
        return null;
    }

    public ObservableList<AbstractMapDto> newDtos(List<AbstractMap> maps) {
        List<AbstractMapDto> dtoList = maps.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }
}
