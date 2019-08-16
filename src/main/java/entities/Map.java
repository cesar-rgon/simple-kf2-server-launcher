package entities;

import javax.persistence.*;

@Entity
@Table(name = "MAPS")
public class Map extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", updatable=false, nullable=false)
    private Integer id;

    @Column(name="CODE", length=100, unique=true, nullable=false)
    private String code;

    @Column(name="OFFICIAL", nullable=false)
    private boolean official;

    @Column(name="URL_INFO", length=255)
    private String urlInfo;

    @Column(name="ID_WORKSHOP")
    private Long idWorkShop;

    @Column(name="URL_PHOTO")
    private String urlPhoto;

    @Column(name="DOWNLOADED", nullable=false)
    private boolean downloaded;

    @Column(name="MOD")
    private Boolean mod;

    public Map() {
        super();
    }

    public Map(String code, boolean official, String urlInfo, Long idWorkShop, String urlPhoto, boolean downloaded, Boolean mod) {
        super();
        this.code = code;
        this.official = official;
        this.urlInfo = urlInfo;
        this.idWorkShop = idWorkShop;
        this.urlPhoto = urlPhoto;
        this.downloaded = downloaded;
        this.mod = mod;
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

    public boolean isOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
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

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public Boolean getMod() {
        return mod;
    }

    public void setMod(Boolean mod) {
        this.mod = mod;
    }
}
