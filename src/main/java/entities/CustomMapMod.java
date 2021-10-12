package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "CUSTOM_MAPS_MODS")
public class CustomMapMod extends AbstractMap {

    @Column(name="ID_WORKSHOP")
    private Long idWorkShop;

    @Column(name="DOWNLOADED", nullable=false)
    private boolean downloaded;

    public CustomMapMod() {
        super();
    }

    public CustomMapMod(String code, String urlInfo, String urlPhoto, List<Profile> profileList, Long idWorkShop, boolean downloaded) {
        super(code, urlInfo, urlPhoto, profileList, false);
        this.idWorkShop = idWorkShop;
        this.downloaded = downloaded;
    }

    public Long getIdWorkShop() {
        return idWorkShop;
    }

    public void setIdWorkShop(Long idWorkShop) {
        this.idWorkShop = idWorkShop;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }
}
