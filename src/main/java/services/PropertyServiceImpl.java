package services;

import java.io.*;
import java.util.Properties;

public class PropertyServiceImpl implements PropertyService {

    public PropertyServiceImpl() {
        super();
    }

    @Override
    public String getPropertyValue(String propFileName, String propKey) throws Exception {
        Properties prop = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("Property file '" + propFileName + "' not found in the classpath");
        }
        inputStream.close();
        return prop.getProperty(propKey);
    }

    @Override
    public void setProperty(String propFileName, String propKey, String propValue) throws Exception {
        Properties prop = new Properties();
        prop.setProperty(propKey, propValue);
        File propFile = new File(propFileName);
        OutputStream outputStream = new FileOutputStream(propFile);
        prop.store(outputStream, null);
    }

    @Override
    public void removeProperty(String propFileName, String propKey) throws Exception {
        Properties prop = new Properties();
        prop.remove(propKey);
        File propFile = new File(propFileName);
        OutputStream outputStream = new FileOutputStream(propFile);
        prop.store(outputStream, null);
    }
}
