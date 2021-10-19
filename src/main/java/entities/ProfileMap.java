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

    @Column(name = "IMPORTED_DATE", nullable = false)
    private Date importedDate;

    public ProfileMap() {
        super();
    }

    public ProfileMap(Profile profile, AbstractMap map) {
        super();
        this.idProfileMap = new IdProfileMap(profile.getId(), map.getId());
        this.profile = profile;
        this.map = map;
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
