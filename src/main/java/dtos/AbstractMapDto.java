package dtos;

import dtos.factories.MapDtoFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractMapDto {

    private static final Logger logger = LogManager.getLogger(AbstractMapDto.class);

    protected final String key;
    protected final String urlInfo;
    protected final String urlPhoto;
    protected final LocalDate releaseDate;
    private final boolean official;
    private final List<ImportedDateByProfileDto> importedDateByProfileDtoList;

    protected AbstractMapDto(String key, String urlInfo, String urlPhoto, boolean official, LocalDate releaseDate, List<ImportedDateByProfileDto> importedDateByProfileList) {
        this.key = key;
        this.urlInfo = urlInfo;
        this.urlPhoto = urlPhoto;
        this.official = official;
        this.releaseDate = releaseDate;
        this.importedDateByProfileDtoList = importedDateByProfileList;
    }

    public String getKey() {
        return key;
    }

    public String getUrlInfo() {
        return urlInfo;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public boolean isOfficial() {
        return official;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public List<ImportedDateByProfileDto> getImportedDateByProfileDtoList() {
        return importedDateByProfileDtoList;
    }

}
