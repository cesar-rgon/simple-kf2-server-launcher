package entities;

import jakarta.persistence.*;


@Entity
@Table(name = "CUSTOM_MAPS_MODS")
public class CustomMapMod extends AbstractMap {

    @Column(name="ID_WORKSHOP")
    private Long idWorkShop;

    @Column(name="ISMAP")
    private Boolean map;

    public CustomMapMod() {
        super();
    }

    public CustomMapMod(String code, String urlInfo, String urlPhoto, Long idWorkShop, Boolean map) {
        super(code, urlInfo, urlPhoto, null);
        this.idWorkShop = idWorkShop;
        this.map = map;
    }

    public Long getIdWorkShop() {
        return idWorkShop;
    }

    public void setIdWorkShop(Long idWorkShop) {
        this.idWorkShop = idWorkShop;
    }

    public Boolean getMap() {
        return map;
    }

    public void setMap(Boolean map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "CustomMapMod{" +
                "idWorkShop=" + idWorkShop +
                "map=" + map +
                "} " + super.toString();
    }
}
