package pojos.enums;

public enum EnumLanguage {
    en ("English"),
    es ("Español"),
    fr ("Français"),
    ru ("Русский");

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

    public static EnumLanguage getByName(String name) {
        return EnumLanguage.valueOf(name);
    }

}
