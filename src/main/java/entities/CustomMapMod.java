package entities;

import pojos.enums.EnumPlatform;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CUSTOM_MAPS_MODS")
public class CustomMapMod extends AbstractMap {

    @Column(name="ID_WORKSHOP")
    private Long idWorkShop;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "DOWNLOADED_MAPS",
            joinColumns = @JoinColumn(name = "ID_WORKSHOP", nullable = false),
            inverseJoinColumns = @JoinColumn(name="ID", nullable = false)
    )
    private List<Platform> downnloadedMap;


    public CustomMapMod() {
        super();
        this.downnloadedMap = new ArrayList<Platform>();
    }

    public CustomMapMod(String code, String urlInfo, String urlPhoto, Long idWorkShop, boolean steamDownloaded) {
        super(code, urlInfo, urlPhoto,false, null);
        this.idWorkShop = idWorkShop;
        this.downnloadedMap = new ArrayList<Platform>();
        if (steamDownloaded) {
            Platform steamPlatform = new Platform(EnumPlatform.STEAM);
            this.downnloadedMap.add(steamPlatform);
        }
    }

    public Long getIdWorkShop() {
        return idWorkShop;
    }

    public void setIdWorkShop(Long idWorkShop) {
        this.idWorkShop = idWorkShop;
    }

    public List<Platform> getDownnloadedMap() {
        return downnloadedMap;
    }

    public void setDownnloadedMap(List<Platform> downnloadedMap) {
        this.downnloadedMap = downnloadedMap;
    }

    public boolean isDownnloadedMapForSteam() {
        return !downnloadedMap.isEmpty() && downnloadedMap.contains(new Platform(EnumPlatform.STEAM));
    }

    public boolean isDownnloadedMapForEpic() {
        return !downnloadedMap.isEmpty() && downnloadedMap.contains(new Platform(EnumPlatform.EPIC));
    }
}
