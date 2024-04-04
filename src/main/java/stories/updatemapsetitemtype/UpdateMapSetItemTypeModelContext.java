package stories.updatemapsetitemtype;

import framework.ModelContext;

public class UpdateMapSetItemTypeModelContext extends ModelContext {

    private final String mapName;
    private final boolean isMap;

    public UpdateMapSetItemTypeModelContext(String mapName, boolean isMap) {
        super();
        this.mapName = mapName;
        this.isMap = isMap;
    }

    public String getMapName() {
        return mapName;
    }

    public boolean isMap() {
        return isMap;
    }
}
