package pojos;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImportMapResultToDisplay {

    private String profileName;
    private StringProperty platformName;
    private StringProperty mapName;
    private BooleanProperty isOfficial;
    private StringProperty importedDate;
    private StringProperty errorMessage;
    private String officialText;
    private String customText;
    private Long idWorkshop;

    public ImportMapResultToDisplay(
            String profileName,
            String platformName,
            String mapName,
            boolean isOfficial,
            Long idWorkshop,
            Date importedDate,
            String errorMessage) {


        this.profileName = profileName;
        this.platformName = new SimpleStringProperty(platformName);
        this.mapName = new SimpleStringProperty(mapName);
        this.isOfficial = new SimpleBooleanProperty(isOfficial);
        this.idWorkshop = idWorkshop;
        this.errorMessage = new SimpleStringProperty(errorMessage);
        String datePattern;
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            datePattern = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.code.dateHourPattern");
            this.officialText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.official");
            this.customText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.custom");
        } catch (Exception e) {
            this.officialText = "OFFICIAL";
            this.customText = "CUSTOM";
            datePattern = "yyyy-MM-dd HH:mm";
        }

        if (importedDate != null) {
            DateFormat importedDateFormat = new SimpleDateFormat(datePattern);
            this.importedDate = new SimpleStringProperty(
                    importedDateFormat.format(importedDate)
            );
        } else {
            this.importedDate = new SimpleStringProperty();
        }
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getPlatformName() {
        return platformName.get();
    }

    public StringProperty platformNameProperty() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName.set(platformName);
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

    public boolean isOfficial() {
        return isOfficial.get();
    }

    public StringProperty isOfficialProperty() {
        if (isOfficial()) {
            return new SimpleStringProperty(officialText);
        } else {
            return new SimpleStringProperty(customText);
        }
    }

    public void setOfficial(boolean isOfficial) {
        this.isOfficial.set(isOfficial);
    }

    public Long getIdWorkshop() {
        return idWorkshop;
    }

    public void setIdWorkshop(Long idWorkshop) {
        this.idWorkshop = idWorkshop;
    }

    public String getImportedDate() {
        return importedDate.get();
    }

    public StringProperty importedDateProperty() {
        return importedDate;
    }

    public void setImportedDate(String importedDate) {
        this.importedDate.set(importedDate);
    }


    public String getErrorMessage() {
        return errorMessage.get();
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage.set(errorMessage);
    }
}
