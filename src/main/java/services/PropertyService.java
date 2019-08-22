package services;

import java.io.File;
import java.util.Properties;

public interface PropertyService {

    String getPropertyValue(String propFileName, String propKey) throws Exception;
    void setProperty(String propFileName, String propKey, String propValue) throws Exception;
    void removeProperty(String propFileName, String propKey) throws Exception;
    void savePropertiesToFile(Properties prop, File file) throws Exception;
    Properties loadPropertiesFromFile(File file) throws Exception;
}
