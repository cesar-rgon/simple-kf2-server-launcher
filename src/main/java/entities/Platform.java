package entities;

import org.apache.commons.lang3.StringUtils;
import pojos.enums.EnumPlatform;

import javax.persistence.*;
import java.io.File;

@Entity
@Table(name = "PLATFORMS")
public class Platform extends AbstractExtendedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="platformSequence")
    @SequenceGenerator(name="platformSequence",sequenceName="PLATFORM_SEQUENCE", allocationSize=1)
    @Column(name="ID", updatable=false, nullable=false)
    private Integer id;

    @Column(name="CODE", length=100, unique=true, nullable=false)
    private String code;

    @Column(name="DESCRIPTION", length=255, unique=true, nullable=false)
    private String description;

    @Column(name="LOGO_PATH", unique=false, nullable=true)
    private File logoPath;

    @Column(name="SMALL_LOGO_PATH", unique=false, nullable=true)
    private File smallLogoPath;


    public Platform() {
        super();
    }

    public Platform(EnumPlatform platform) {
        super();
        this.code = platform.name();
        this.description = platform.getDescripcion();
        this.logoPath = StringUtils.isNotBlank(platform.getLogoPath()) ? new File(platform.getLogoPath()): null;
        this.smallLogoPath = StringUtils.isNotBlank(platform.getSmallLogoPath()) ? new File(platform.getSmallLogoPath()): null;
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
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    public File getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(File logoPath) {
        this.logoPath = logoPath;
    }

    public File getSmallLogoPath() {
        return smallLogoPath;
    }

    public void setSmallLogoPath(File smallLogoPath) {
        this.smallLogoPath = smallLogoPath;
    }
}
