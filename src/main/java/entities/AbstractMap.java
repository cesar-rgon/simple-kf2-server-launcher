package entities;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "MAPS")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractMap extends AbstractExtendedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="mapsSequence")
    @SequenceGenerator(name="mapsSequence",sequenceName="MAPS_SEQUENCE", allocationSize=1)
    @Column(name="ID", updatable=false, nullable=false)
    private Integer id;

    @Column(name="CODE", length=100, unique=true, nullable=false)
    private String code;

    @Column(name="URL_INFO", length=255)
    private String urlInfo;

    @Column(name="URL_PHOTO")
    private String urlPhoto;

    @Column(name="RELEASE_DATE")
    private Date releaseDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "map")
    private List<PlatformProfileMap> platformProfileMapList;

    // Not mapped attribute
    @Transient
    private boolean official;

    protected AbstractMap() {
        super();
        this.platformProfileMapList = new ArrayList<PlatformProfileMap>();
    }

    protected AbstractMap(String code, String urlInfo, String urlPhoto, boolean official, Date releaseDate) {
        this();
        this.code = code;
        this.urlInfo = urlInfo;
        this.urlPhoto = urlPhoto;
        this.official = official;
        this.releaseDate = releaseDate;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Object id) {
        this.id = (Integer) id;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void setDescription(String description) {
    }

    public String getUrlInfo() {
        return urlInfo;
    }

    public void setUrlInfo(String urlInfo) {
        this.urlInfo = urlInfo;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public boolean isOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<PlatformProfileMap> getPlatformProfileMapList() {
        return platformProfileMapList;
    }

    public void setPlatformProfileMapList(List<PlatformProfileMap> platformProfileMapList) {
        this.platformProfileMapList = platformProfileMapList;
    }

    public List<Profile> getProfileList() {
        return platformProfileMapList.
                stream().
                map(PlatformProfileMap::getProfile).
                collect(Collectors.toList());
    }
}
