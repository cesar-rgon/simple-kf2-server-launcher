package pojos.enums;

public enum EnumCardOrientation {
    DOWN ("Card down"),
    RIGHT ("Card right"),
    UP ("Card up"),
    LEFT ("Card left"),
    DETAILED ("Detailed card");

    private String descripcion;

    private EnumCardOrientation(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String toString() {
        return descripcion;
    }

    public static EnumCardOrientation getByName(String name) {
        return EnumCardOrientation.valueOf(name);
    }
}
