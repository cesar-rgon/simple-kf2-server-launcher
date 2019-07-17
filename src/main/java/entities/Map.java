package entities;

import javax.persistence.*;

@Entity
@Table(name = "MAPS")
public class Map extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="ID", updatable=false, nullable=false)
    private Integer id;

    @Column(name="CODE", length=100, unique=true, nullable=false)
    private String code;

    @Column(name="OFFICIAL", nullable=false)
    private Boolean official;

    @Column(name="URL_INFO", length=255)
    private String urlInfo;

    @Column(name="ID_WORKSHOP")
    private Long idWorkShop;

    @Column(name="URL_PHOTO")
    private String urlPhoto;

    @Column(name="DOWNLOADED", nullable=false)
    private Boolean downloaded;

    public Map() {
        super();
    }

    public Map(String code, Boolean official, String urlInfo, Long idWorkShop, String urlPhoto, Boolean downloaded) {
        super();
        this.code = code;
        this.official = official;
        this.urlInfo = urlInfo;
        this.idWorkShop = idWorkShop;
        this.urlPhoto = urlPhoto;
        this.downloaded = downloaded;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getOfficial() {
        return official;
    }

    public void setOfficial(Boolean official) {
        this.official = official;
    }

    public String getUrlInfo() {
        return urlInfo;
    }

    public void setUrlInfo(String urlInfo) {
        this.urlInfo = urlInfo;
    }

    public Long getIdWorkShop() {
        return idWorkShop;
    }

    public void setIdWorkShop(Long idWorkShop) {
        this.idWorkShop = idWorkShop;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public Boolean getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(Boolean downloaded) {
        this.downloaded = downloaded;
    }
}
