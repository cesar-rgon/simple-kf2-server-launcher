package pojos;

import javafx.beans.property.*;
import javafx.scene.control.Hyperlink;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.enums.EnumPlatform;
import services.PropertyService;
import services.PropertyServiceImpl;

public class MapToDisplay {

    private static final Logger logger = LogManager.getLogger(MapToDisplay.class);
    private BooleanProperty selected;
    private Long idWorkShop;
    private Hyperlink workShopPage;
    private StringProperty commentary;
    private String platformDescription;

    public MapToDisplay(Long idWorkShop,
                        String commentary) {
        super();
        this.selected = new SimpleBooleanProperty(true);
        this.idWorkShop = idWorkShop;
        PropertyService propertyService = new PropertyServiceImpl();
        String baseUrlWorkshop = null;
        try {
            baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapBaseUrlWorkshop");
            this.workShopPage = new Hyperlink(baseUrlWorkshop + idWorkShop);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
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

    public Hyperlink getWorkShopPage() {
        return workShopPage;
    }

    public void setWorkShopPage(Hyperlink workShopPage) {
        this.workShopPage = workShopPage;
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
