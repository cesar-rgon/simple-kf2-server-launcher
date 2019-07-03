package enums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.List;

public enum MapViewOptions {

    VIEW_BOTH("Official and custom maps", "Mapas oficiales y propios"),
    VIEW_OFFICIAL("Only official maps", "Sólo mapas oficiales"),
    VIEW_CUSTOM("Only custom maps", "Sólo mapas propios");

    private final String englishDescription;
    private final String spanishDescription;

    MapViewOptions(String englishDescription, String spanishDescription) {
        this.englishDescription = englishDescription;
        this.spanishDescription = spanishDescription;
    }

    public String getEnglishDescription() {
        return englishDescription;
    }

    public String getSpanishDescription() {
        return spanishDescription;
    }

    public static ObservableList<MapViewOptions> listAll() {
        List<MapViewOptions> list = Arrays.asList(MapViewOptions.values());
        return FXCollections.observableArrayList(list);
    }

    @Override
    public String toString() {
        return englishDescription;
    }
}
