package stories.updatemapsetitemtype;

import framework.ModelContext;

public class UpdateMapSetItemTypeModelContext extends ModelContext {

    private final String mapName;
    private final Boolean isMap;

    public UpdateMapSetItemTypeModelContext(String mapName, Boolean isMap) {
        super();
        this.mapName = mapName;
        this.isMap = isMap;
    }

    public String getMapName() {
        return mapName;
    }

    public Boolean isMap() {
        return isMap;
    }
}
