package pojos.enums;

public enum EnumCardOrderType {

    BY_ALIAS ("Order cards by map's alias"),
    BY_NAME ("Order cards by map's name"),
    BY_RELEASE_DATE ("Order cards by map's release date"),
    BY_IMPORTED_DATE ("Order cards by map's imported date"),
    BY_DOWNLOAD ("Order cards by map's download state"),
    BY_MAPS_CYCLE ("Order cards by maps cycle");

    private String descripcion;

    private EnumCardOrderType(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String toString() {
        return descripcion;
    }

    public static EnumCardOrderType getByName(String name) {
        return EnumCardOrderType.valueOf(name);
    }

}
