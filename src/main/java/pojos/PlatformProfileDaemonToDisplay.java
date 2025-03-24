package pojos;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pojos.enums.EnumPlatform;

public class PlatformProfileDaemonToDisplay extends PlatformProfileToDisplay {

    private StringProperty serviceStatusInfo;

    public PlatformProfileDaemonToDisplay(EnumPlatform enumPlatform,
                                            int profileFileIndex,
                                            String profileName,
                                            Integer webPort,
                                            Integer gamePort,
                                            Integer queryPort,
                                            String serviceStatusInfo) {

        super(enumPlatform, profileFileIndex, profileName, webPort, gamePort, queryPort);
        this.serviceStatusInfo = new SimpleStringProperty(serviceStatusInfo);
    }

    public String getServiceStatusInfo() {
        return serviceStatusInfo.get();
    }

    public StringProperty serviceStatusInfoProperty() {
        return serviceStatusInfo;
    }

    public void setServiceStatusInfo(String serviceStatusInfo) {
        this.serviceStatusInfo.set(serviceStatusInfo);
    }
}
