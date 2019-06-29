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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ID_DESCRIPTION", referencedColumnName="ID", unique=true, nullable=false)
    private Description description;

    @Column(name="OFFICIAL", nullable=false)
    private Boolean official;

    @Column(name="URL_INFO", length=255)
    private String urlInfo;

    public Map() {
        super();
    }

    public Map(String code, Description description, Boolean official, String urlInfo) {
        super();
        this.code = code;
        this.description = description;
        this.official = official;
        this.urlInfo = urlInfo;
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

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
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
}
