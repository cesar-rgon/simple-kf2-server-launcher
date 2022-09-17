package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "PLATFORMS_PROFILES_MAPS")
public class PlatformProfileMap extends AbstractEntity {

    @Embeddable
    public static class IdPlatformProfileMap implements Serializable {
        private static final long serialVersionUID = 1L;

        @Column(name="ID_PLATFORM", nullable = false)
        private Integer idPlatform;

        @Column(name="ID_PROFILE", nullable = false)
        private Integer idProfile;

        @Column(name = "ID_MAP", nullable = false)
        private Integer idMap;

        public IdPlatformProfileMap() {}

        public IdPlatformProfileMap(Integer idPlatform, Integer idProfile, Integer idMap) {
            super();
            this.idPlatform = idPlatform;
            this.idProfile = idProfile;
            this.idMap = idMap;
        }

        public Integer getIdPlatform() {
            return idPlatform;
        }

        public void setIdPlatform(Integer idPlatform) {
            this.idPlatform = idPlatform;
        }

        public Integer getIdProfile() {
            return idProfile;
        }

        public void setIdProfile(Integer idProfile) {
            this.idProfile = idProfile;
        }

        public Integer getIdMap() {
            return idMap;
        }

        public void setIdMap(Integer idMap) {
            this.idMap = idMap;
        }

        @Override
        public boolean equals(Object o) {
            if (o != null && o instanceof IdPlatformProfileMap) {
                IdPlatformProfileMap that = (IdPlatformProfileMap)o;
                return this.idPlatform.equals(that.idPlatform) && this.idProfile.equals(that.idProfile) && this.idMap.equals(that.idMap);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return idPlatform.hashCode() + idProfile.hashCode() + idMap.hashCode();
        }
    }

    @EmbeddedId
    private IdPlatformProfileMap idPlatformProfileMap;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ID_PLATFORM", referencedColumnName = "ID", insertable = false, updatable = false, nullable = false)
    private AbstractPlatform platform;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ID_PROFILE", referencedColumnName = "ID", insertable = false, updatable = false, nullable = false)
    private Profile profile;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ID_MAP", referencedColumnName = "ID", insertable = false, updatable = false, nullable = false)
    private AbstractMap map;

    @Column(name="ALIAS", length=255, nullable=false)
    private String alias;

    @Column(name="URL_PHOTO")
    private String urlPhoto;

    @Column(name="RELEASE_DATE")
    private Date releaseDate;

    @Column(name = "IMPORTED_DATE", nullable = false)
    private Date importedDate;

    @Column(name="URL_INFO", length=255)
    private String urlInfo;

    public PlatformProfileMap() {
        super();
    }

    public PlatformProfileMap(AbstractPlatform platform, Profile profile, AbstractMap map, Date releaseDate, String urlInfo, String urlPhoto) {
        super();
        this.idPlatformProfileMap = new IdPlatformProfileMap(platform.getId(), profile.getId(), map.getId());
        this.platform = platform;
        this.profile = profile;
        this.map = map;
        this.alias = map.getCode();
        this.importedDate = new Date();
        this.releaseDate = releaseDate;
        this.urlInfo = urlInfo;
        this.urlPhoto = urlPhoto;
    }

    // Getters y Setters

    @Override
    public Object getId() {
        return idPlatformProfileMap;
    }

    @Override
    public void setId(Object id) {
        this.idPlatformProfileMap = (IdPlatformProfileMap) id;
    }

    public AbstractPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(AbstractPlatform platform) {
        this.platform = platform;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public AbstractMap getMap() {
        return map;
    }

    public void setMap(AbstractMap map) {
        this.map = map;
    }

    public Date getImportedDate() {
        return importedDate;
    }

    public void setImportedDate(Date importedDate) {
        this.importedDate = importedDate;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getUrlInfo() {
        return urlInfo;
    }

    public void setUrlInfo(String urlInfo) {
        this.urlInfo = urlInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof PlatformProfileMap) {
            PlatformProfileMap that = (PlatformProfileMap)o;
            return this.idPlatformProfileMap.equals(that.idPlatformProfileMap);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return idPlatformProfileMap.hashCode();
    }

    @Override
    public String toString() {
        return "PlatformProfileMap{" +
                "idPlatformProfileMap=" + idPlatformProfileMap +
                ", platform=" + platform +
                ", profile=" + profile +
                ", map=" + map +
                ", alias='" + alias + '\'' +
                ", urlPhoto='" + urlPhoto + '\'' +
                ", releaseDate=" + releaseDate +
                ", importedDate=" + importedDate +
                ", urlInfo='" + urlInfo + '\'' +
                '}';
    }
}
