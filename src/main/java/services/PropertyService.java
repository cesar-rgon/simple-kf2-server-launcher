package services;

public interface PropertyService {

    String getPropertyValue(String propFileName, String propKey) throws Exception;
    void setProperty(String propFileName, String propKey, String propValue) throws Exception;
    void removeProperty(String propFileName, String propKey) throws Exception;
}
