package pojos;

public class CustomMapIndex {
    private Long idWorkShop;
    private Boolean isMap;
    private Integer mapIndex;

    public CustomMapIndex(Integer mapIndex, Long idWorkShop, Boolean isMap) {
        super();
        this.mapIndex = mapIndex;
        this.idWorkShop = idWorkShop;
        this.isMap = isMap;
    }

    public Long getIdWorkShop() {
        return idWorkShop;
    }

    public void setIdWorkShop(Long idWorkShop) {
        this.idWorkShop = idWorkShop;
    }

    public Boolean getMap() {
        return isMap;
    }

    public void setMap(Boolean map) {
        isMap = map;
    }

    public Integer getMapIndex() {
        return mapIndex;
    }

    public void setMapIndex(Integer mapIndex) {
        this.mapIndex = mapIndex;
    }
}
