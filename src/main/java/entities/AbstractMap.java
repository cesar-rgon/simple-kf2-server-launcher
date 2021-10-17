package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MAPS")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractMap extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="ID", updatable=false, nullable=false)
    private Integer id;

    @Column(name="CODE", length=100, unique=true, nullable=false)
    private String code;

    @Column(name="URL_INFO", length=255)
    private String urlInfo;

    @Column(name="URL_PHOTO")
    private String urlPhoto;

    @Column(name="IMPORTED_DATE", nullable = false)
    private Date importedDate;

    @Column(name="RELEASE_DATE")
    private Date releaseDate;


    @ManyToMany(mappedBy = "mapList", fetch = FetchType.EAGER)
    private List<Profile> profileList;

    // Not mapped attribute
    @Transient
    private boolean official;

    protected AbstractMap() {
        super();
        this.profileList = new ArrayList<Profile>();
    }

    protected AbstractMap(String code, String urlInfo, String urlPhoto, List<Profile> profileList, boolean official, Date releaseDate) {
        super();
        this.code = code;
        this.urlInfo = urlInfo;
        this.urlPhoto = urlPhoto;
        this.profileList = profileList;
        this.official = official;
        this.importedDate = new Date();
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
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

    public List<Profile> getProfileList() {
        return profileList;
    }

    public void setProfileList(List<Profile> profileList) {
        this.profileList = profileList;
    }

    public boolean isOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }

    public Date getImportedDate() {
        return importedDate;
    }

    public void setImportedDate(Date importedDate) {
        this.importedDate = importedDate;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}
