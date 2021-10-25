package pojos;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImportMapResultToDisplay {

    private String profileName;
    private StringProperty mapName;
    private BooleanProperty isOfficial;
    private StringProperty importedDate;
    private StringProperty errorMessage;

    public ImportMapResultToDisplay(
            String profileName,
            String mapName,
            boolean isOfficial,
            Date importedDate,
            String errorMessage) {

        DateFormat importedDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.profileName = profileName;
        this.mapName = new SimpleStringProperty(mapName);
        this.isOfficial = new SimpleBooleanProperty(isOfficial);
        this.importedDate = new SimpleStringProperty(
                importedDateFormat.format(importedDate)
        );
        this.errorMessage = new SimpleStringProperty(errorMessage);
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
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
            return new SimpleStringProperty("OFFICIAL");
        } else {
            return new SimpleStringProperty("CUSTOM");
        }
    }

    public void setOfficial(boolean isOfficial) {
        this.isOfficial.set(isOfficial);
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
