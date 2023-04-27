package pojos.enums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum EnumPlatform {
    STEAM("Steam", "images/steam-logo.png", "images/steam-small-logo.png", 0),
    EPIC("Epic Games", "images/epic-logo.png", "images/epic-small-logo.png", 1),
    ALL("All platforms", "", "", 2);


    private String descripcion;
    private String logoPath;
    private String smallLogoPath;
    private Integer index;

    private EnumPlatform(String descripcion, String logoPath, String smallLogoPath, Integer index) {
        this.descripcion = descripcion;
        this.logoPath = logoPath;
        this.smallLogoPath = smallLogoPath;
        this.index = index;
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

    public Integer getIndex() {
        return index;
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
