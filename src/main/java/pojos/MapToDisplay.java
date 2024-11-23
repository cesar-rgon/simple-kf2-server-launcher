package pojos;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.enums.EnumPlatform;

public class MapToDisplay {

    private static final Logger logger = LogManager.getLogger(MapToDisplay.class);
    private BooleanProperty selected;
    private Long idWorkShop;
    private StringProperty commentary;
    private String platformDescription;

    public MapToDisplay(Long idWorkShop,
                        String commentary) {
        super();
        this.selected = new SimpleBooleanProperty(true);
        this.idWorkShop = idWorkShop;
        this.commentary = new SimpleStringProperty(commentary);
        this.platformDescription = EnumPlatform.STEAM.name();
    }

    public MapToDisplay(String commentary) {
        super();
        this.commentary = new SimpleStringProperty(commentary);
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
        return idWorkShop;
    }

    public void setIdWorkShop(Long idWorkShop) {
        this.idWorkShop = idWorkShop;
    }

    public String getCommentary() {
        return commentary.get();
    }

    public StringProperty commentaryProperty() {
        return new SimpleStringProperty("[ " + platformDescription + " ] " + commentary.get());
    }

    public void setCommentary(String commentary) {
        this.commentary.set(commentary);
    }

    public String getPlatformDescription() {
        return platformDescription;
    }

    public void setPlatformDescription(String platformDescription) {
        this.platformDescription = platformDescription;
    }
}
