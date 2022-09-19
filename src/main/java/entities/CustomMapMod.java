package entities;

import jakarta.persistence.*;


@Entity
@Table(name = "CUSTOM_MAPS_MODS")
public class CustomMapMod extends AbstractMap {

    @Column(name="ID_WORKSHOP")
    private Long idWorkShop;

    public CustomMapMod() {
        super();
    }

    public CustomMapMod(String code, String urlInfo, String urlPhoto, Long idWorkShop) {
        super(code, urlInfo, urlPhoto,false, null);
        this.idWorkShop = idWorkShop;
    }

    public Long getIdWorkShop() {
        return idWorkShop;
    }

    public void setIdWorkShop(Long idWorkShop) {
        this.idWorkShop = idWorkShop;
    }

    @Override
    public String toString() {
        return "CustomMapMod{" +
                "idWorkShop=" + idWorkShop +
                "} " + super.toString();
    }
}
