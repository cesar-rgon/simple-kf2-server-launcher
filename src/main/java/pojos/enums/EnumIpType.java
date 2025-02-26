package pojos.enums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum EnumIpType {
    LOCALHOST("Localhost", "Localhost", "Localhost", "Localhost", "Localhost"),
    PUBLIC_IP("Public IP", "IP pública", "IP publique", "Публичный IP", "IP công cộng");

    private String enDescription;
    private String esDescription;
    private String frDescription;
    private String ruDescription;
    private String viDescription;

    private EnumIpType(String enDescription, String esDescription, String frDescription, String ruDescription, String viDescription) {
        this.enDescription = enDescription;
        this.esDescription = esDescription;
        this.frDescription = frDescription;
        this.ruDescription = ruDescription;
        this.viDescription = viDescription;
    }

    public String getEnglishDescription() {
        return enDescription;
    }

    public String getSpanishDescription() {
        return esDescription;
    }

    public String getFrenchDescription() {
        return frDescription;
    }

    public String getRussianDescription() {
        return ruDescription;
    }

    public String getVietnameseDescription() {
        return viDescription;
    }

    public String toString() {
        String languageCode;
        PropertyService propertyService = new PropertyServiceImpl();
        try {
            languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            switch (languageCode) {
                case "en":
                    return enDescription;
                case "es":
                    return esDescription;
                case "fr":
                    return frDescription;
                case "ru":
                    return ruDescription;
                case "vi":
                    return viDescription;
            }
        } catch (Exception e) {
            return enDescription;
        }
        return enDescription;
    }

    public static EnumIpType getByName(String name) {
        return EnumIpType.valueOf(name);
    }

    public static ObservableList<EnumIpType> listAll() {
        return FXCollections.observableArrayList(
                Arrays.stream(EnumIpType.values()).
                        collect(Collectors.toList())
        );
    }
}
