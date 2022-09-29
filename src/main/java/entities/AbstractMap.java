package entities;


import jakarta.persistence.*;

import java.util.Date;

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

    protected AbstractMap() {
        super();
    }

    protected AbstractMap(String code, String urlInfo, String urlPhoto, Date releaseDate) {
        this();
        this.code = code;
        this.urlInfo = urlInfo;
        this.urlPhoto = urlPhoto;
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

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

}
