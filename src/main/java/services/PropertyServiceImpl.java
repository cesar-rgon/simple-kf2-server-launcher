package services;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Properties;

public class PropertyServiceImpl implements PropertyService {

    public PropertyServiceImpl() {
        super();
    }

    @Override
    public String getPropertyValue(String propFileRelativePath, String propKey) throws Exception {
        Properties prop = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("./" + propFileRelativePath);
        } catch (FileNotFoundException e) {
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileRelativePath);
        }
        prop.load(inputStream);
        inputStream.close();
        return prop.getProperty(propKey);
    }

    @Override
    public void setProperty(String propFileRelativePath, String propKey, String propValue) throws Exception {
        if (StringUtils.isBlank(propValue)) {
            removeProperty(propFileRelativePath, propKey);
            return;
        }
        Properties prop = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("./" + propFileRelativePath);
        } catch (FileNotFoundException e) {
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileRelativePath);
        }
        prop.load(inputStream);
        prop.setProperty(propKey, propValue);

        File propFile = new File("./" + propFileRelativePath);
        if (!propFile.exists()) {
            propFile = new File(getClass().getResource("/" + propFileRelativePath).toURI());
        }
        OutputStream outputStream = new FileOutputStream(propFile);
        prop.store(outputStream, null);
    }

    @Override
    public void removeProperty(String propFileRelativePath, String propKey) throws Exception {
        Properties prop = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("./" + propFileRelativePath);
        } catch (FileNotFoundException e) {
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileRelativePath);
        }
        prop.load(inputStream);
        prop.remove(propKey);

        File propFile = new File("./" + propFileRelativePath);
        if (!propFile.exists()) {
            propFile = new File(getClass().getResource("/" + propFileRelativePath).toURI());
        }
        OutputStream outputStream = new FileOutputStream(propFile);
        prop.store(outputStream, null);
    }
}
