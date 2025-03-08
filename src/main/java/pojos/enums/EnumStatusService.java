package pojos.enums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum EnumStatusService {
    ACTIVE ("Service running", "Servicio ejecutándose", "Service en cours d'exécution", "Служба работает", "Dịch vụ đang chạy"),
    INACTIVE ("Service not running but installed", "Servicio no ejecutándose pero instalado", "Le service n'est pas en cours d'exécution mais est installé", "Служба не запущена, но установлена", "Dịch vụ không chạy nhưng đã được cài đặt"),
    NONEXISTENT ("Service not running and not installed", "Servicio no ejecutándose ni instalado", "Le service n'est pas en cours d'exécution et n'est pas installé", "Служба не запущена и не установлена", "Dịch vụ không chạy và không được cài đặt"),
    UNKNOWN ("Service unknown", "Servicio desconocido", "Service inconnu", "Сервис неизвестен", "Dịch vụ không xác định");

    private String enDescription;
    private String esDescription;
    private String frDescription;
    private String ruDescription;
    private String viDescription;

    private EnumStatusService(String enDescription, String esDescription, String frDescription, String ruDescription, String viDescription) {
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

    public static EnumStatusService getByName(String name) {
        return EnumStatusService.valueOf(name);
    }

    public static ObservableList<EnumStatusService> listAll() {
        return FXCollections.observableArrayList(
                Arrays.stream(EnumStatusService.values()).
                        collect(Collectors.toList())
        );
    }
}
