package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "PROFILES_MAPS")
public class ProfileMap extends AbstractEntity {

    @Embeddable
    public static class IdProfileMap implements Serializable {
        private static final long serialVersionUID = 1L;

        @Column(name="ID_PROFILE", nullable = false)
        private Integer idProfile;

        @Column(name = "ID_MAP", nullable = false)
        private Integer idMap;

        public IdProfileMap() {}

        public IdProfileMap(Integer idProfile, Integer idMap) {
            super();
            this.idProfile = idProfile;
            this.idMap = idMap;
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
            if (o != null && o instanceof IdProfileMap) {
                IdProfileMap that = (IdProfileMap)o;
                return this.idProfile.equals(that.idProfile) && this.idMap.equals(that.idMap);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return idProfile.hashCode() + idMap.hashCode();
        }
    }

    @EmbeddedId
    private IdProfileMap idProfileMap;

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

    public ProfileMap() {
        super();
    }

    public ProfileMap(Profile profile, AbstractMap map) {
        super();
        this.idProfileMap = new IdProfileMap(profile.getId(), map.getId());
        this.profile = profile;
        this.map = map;
        this.alias = map.getCode();
        this.importedDate = new Date();
    }

    // Getters y Setters

    @Override
    public Object getId() {
        return idProfileMap;
    }

    @Override
    public void setId(Object id) {
        this.idProfileMap = (IdProfileMap) id;
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
        if (o != null && o instanceof ProfileMap) {
            ProfileMap that = (ProfileMap)o;
            return this.idProfileMap.equals(that.idProfileMap);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return idProfileMap.hashCode();
    }

}
