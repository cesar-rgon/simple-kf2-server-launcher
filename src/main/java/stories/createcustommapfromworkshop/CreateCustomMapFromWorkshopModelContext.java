package stories.createcustommapfromworkshop;

import framework.ModelContext;

import java.util.List;

public class CreateCustomMapFromWorkshopModelContext extends ModelContext {

    private final List<String> platformNameList;
    private final Long idWorkShop;
    private final String mapName;
    private final String strUrlMapImage;
    private final List<String> profileNameList;

    public CreateCustomMapFromWorkshopModelContext(List<String> platformNameList, Long idWorkShop, String mapName, String strUrlMapImage, List<String> profileNameList) {
        super();
        this.platformNameList = platformNameList;
        this.idWorkShop = idWorkShop;
        this.mapName = mapName;
        this.strUrlMapImage = strUrlMapImage;
        this.profileNameList = profileNameList;
    }

    public List<String> getPlatformNameList() {
        return platformNameList;
    }

    public Long getIdWorkShop() {
        return idWorkShop;
    }

    public String getMapName() {
        return mapName;
    }

    public String getStrUrlMapImage() {
        return strUrlMapImage;
    }

    public List<String> getProfileNameList() {
        return profileNameList;
    }
}
