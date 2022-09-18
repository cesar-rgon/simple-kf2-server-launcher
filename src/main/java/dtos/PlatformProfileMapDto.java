package dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PlatformProfileMapDto {

    private final PlatformDto platformDto;
    private final ProfileDto profileDto;
    private final AbstractMapDto mapDto;
    private final String alias;
    private final String urlPhoto;
    private final LocalDate releaseDate;
    private final LocalDateTime importedDate;
    private final String urlInfo;

    private boolean downloaded;

    public PlatformProfileMapDto(PlatformDto platformDto, ProfileDto profileDto, AbstractMapDto mapDto, String alias, String urlPhoto, LocalDate releaseDate, LocalDateTime importedDate, String urlInfo, boolean downloaded) {
        super();
        this.platformDto = platformDto;
        this.profileDto = profileDto;
        this.mapDto = mapDto;
        this.alias = alias;
        this.urlPhoto = urlPhoto;
        this.releaseDate = releaseDate;
        this.importedDate = importedDate;
        this.urlInfo = urlInfo;
        this.downloaded = downloaded;
    }

    public PlatformDto getPlatformDto() {
        return platformDto;
    }

    public ProfileDto getProfileDto() {
        return profileDto;
    }

    public AbstractMapDto getMapDto() {
        return mapDto;
    }

    public String getAlias() {
        return alias;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public LocalDateTime getImportedDate() {
        return importedDate;
    }

    public String getUrlInfo() {
        return urlInfo;
    }

    public boolean isDownloaded() {
        return downloaded;
    }
}
