package pojos.enums;

public enum EnumLanguage {
    en ("English"),
    es ("Español"),
    fr ("Français");

    private String descripcion;

    private EnumLanguage(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String toString() {
        return descripcion;
    }

    public static EnumPlatform getByName(String name) {
        return EnumPlatform.valueOf(name);
    }

}
