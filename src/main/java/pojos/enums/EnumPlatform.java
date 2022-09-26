package pojos.enums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum EnumPlatform {
    STEAM("Steam", "images/steam-logo.png", "images/steam-small-logo.png"),
    EPIC("Epic Games", "images/epic-logo.png", "images/epic-small-logo.png"),
    ALL("All platforms", "", "");


    private String descripcion;
    private String logoPath;
    private String smallLogoPath;

    private EnumPlatform(String descripcion, String logoPath, String smallLogoPath) {
        this.descripcion = descripcion;
        this.logoPath = logoPath;
        this.smallLogoPath = smallLogoPath;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public String getSmallLogoPath() {
        return smallLogoPath;
    }

    public String toString() {
        return descripcion;
    }

    public static EnumPlatform getByName(String name) {
        return EnumPlatform.valueOf(name);
    }

    public static ObservableList<EnumPlatform> listAll() {
        return FXCollections.observableArrayList(
                Arrays.stream(EnumPlatform.values()).
                        filter(ep -> !ep.equals(ALL)).
                        collect(Collectors.toList())
        );
    }
}
