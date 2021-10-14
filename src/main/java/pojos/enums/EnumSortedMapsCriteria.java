package pojos.enums;

public enum EnumSortedMapsCriteria {

    NOMBRE_ASC ("Ordenación por nombre ascendente"),
    NOMBRE_DESC ("Ordenación por nombre descendente"),
    FECHA_ASC ("Ordenación por fecha ascendente"),
    FECHA_DESC ("Ordenación por fecha descendente");

    private final String description;

    private EnumSortedMapsCriteria(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
