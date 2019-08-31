package pojos.kf2factory;

import org.apache.commons.lang3.StringUtils;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

public class Kf2Factory {

    public static Kf2Common getInstance() {
        String os = System.getProperty("os.name");
        if (StringUtils.isNotEmpty(os)) {
            if (os.contains("Windows")) {
                return new Kf2WindowsImpl();
            } else {
                if (os.contains("Linux")) {
                    return new Kf2LinuxImpl();
                }
            }
        } else {
            try {
                PropertyService propertyService = new PropertyServiceImpl();
                String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.osNotDetected");
                Utils.warningDialog(headerText, contentText);
            } catch (Exception e) {
                Utils.errorDialog(e.getMessage(), e);
            }
        }
        return null;
    }
}
