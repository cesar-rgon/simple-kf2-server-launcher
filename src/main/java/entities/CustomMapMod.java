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
    private List<AbstractPlatform> downnloadedMap;


    public CustomMapMod() {
        super();
        this.downnloadedMap = new ArrayList<AbstractPlatform>();
    }

    public CustomMapMod(String code, String urlInfo, String urlPhoto, Long idWorkShop, boolean steamDownloaded) {
        super(code, urlInfo, urlPhoto,false, null);
        this.idWorkShop = idWorkShop;
        this.downnloadedMap = new ArrayList<AbstractPlatform>();
        if (steamDownloaded) {
            AbstractPlatform steamPlatform = new SteamPlatform(EnumPlatform.STEAM);
            this.downnloadedMap.add(steamPlatform);
        }
    }

    public Long getIdWorkShop() {
        return idWorkShop;
    }

    public void setIdWorkShop(Long idWorkShop) {
        this.idWorkShop = idWorkShop;
    }

    public List<AbstractPlatform> getDownnloadedMap() {
        return downnloadedMap;
    }

    public void setDownnloadedMap(List<AbstractPlatform> downnloadedMap) {
        this.downnloadedMap = downnloadedMap;
    }

    public boolean isDownnloadedMapForSteam() {
        return !downnloadedMap.isEmpty() && downnloadedMap.contains(new SteamPlatform(EnumPlatform.STEAM));
    }

    public boolean isDownnloadedMapForEpic() {
        return !downnloadedMap.isEmpty() && downnloadedMap.contains(new EpicPlatform(EnumPlatform.EPIC));
    }

    @Override
    public String toString() {
        return "CustomMapMod{" +
                "idWorkShop=" + idWorkShop +
                ", downnloadedMap=" + downnloadedMap +
                "} " + super.toString();
    }
}
