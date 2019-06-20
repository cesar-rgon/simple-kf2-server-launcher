package pojos.kf2factory;

import org.apache.commons.lang3.StringUtils;
import utils.Utils;

public class Kf2Factory {

    public static Kf2Common getInstance() {
        String os = System.getProperty("os.name");
        if (StringUtils.isNotEmpty(os)) {
            if (os.contains("Windows")) {
                return new Kf2WindowsImpl();
            }
        } else {
            Utils.errorDialog("Error when detecting the operating system", "The proccess is aborted!", null);
        }
        return null;
    }
}
