package pojos;

import javafx.beans.property.*;

public class MapToDisplay {

    private BooleanProperty selected;
    private StringProperty idWorkShop;
    private StringProperty mapName;

    public MapToDisplay(Long idWorkShop,
                        String mapName) {
        super();
        this.selected = new SimpleBooleanProperty(true);
        this.idWorkShop = new SimpleStringProperty(String.valueOf(idWorkShop));
        this.mapName = new SimpleStringProperty(mapName);
    }

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public Long getIdWorkShop() {
        try {
            return Long.parseLong(idWorkShop.get());
        } catch (Exception e) {
            return null;
        }
    }

    public StringProperty idWorkShopProperty() {
        return idWorkShop;
    }

    public void setIdWorkShop(String idWorkShop) {
        this.idWorkShop.set(idWorkShop);
    }

    public String getMapName() {
        return mapName.get();
    }

    public StringProperty mapNameProperty() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName.set(mapName);
    }
}
