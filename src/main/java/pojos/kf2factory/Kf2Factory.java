package pojos.kf2factory;

import org.apache.commons.lang3.StringUtils;
import pojos.enums.EnumPlatform;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

public class Kf2Factory {

    public static Kf2Common getInstance(String platformName) {
        if (StringUtils.isBlank(platformName)) {
            showDialog(null);
            return null;
        }
        if (EnumPlatform.EPIC.name().equals(platformName)) {
            return new Kf2EpicWindowsImpl();
        }
        String os = System.getProperty("os.name");
        if (StringUtils.isEmpty(os)) {
            showDialog(EnumPlatform.STEAM);
            return null;
        }

        if (os.contains("Windows")) {
            return new Kf2SteamWindowsImpl();
        } else {
            if (os.contains("Linux")) {
                return new Kf2SteamLinuxImpl();
            }
        }
        return null;
    }

    private static void showDialog(EnumPlatform platform) {
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
            String contentText = StringUtils.EMPTY;
            if (platform == null) {
                contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profileNotEmpty");
            } else if (EnumPlatform.STEAM.equals(platform)) {
                contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.osNotDetected");
            }
            Utils.warningDialog(headerText, contentText);
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), e);
        }
    }
}
