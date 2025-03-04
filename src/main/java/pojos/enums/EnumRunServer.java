package pojos.enums;

public enum EnumRunServer {
    TERMINAL("Run server in terminal"),
    SERVICE("Run server as a service / daemon");

    private String descripcion;

    private EnumRunServer(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String toString() {
        return descripcion;
    }

    public static EnumRunServer getByName(String name) {
        return EnumRunServer.valueOf(name);
    }
}
