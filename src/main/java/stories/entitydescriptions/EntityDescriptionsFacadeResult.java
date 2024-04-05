package stories.entitydescriptions;

import framework.FacadeResult;

public class EntityDescriptionsFacadeResult extends FacadeResult {

    private String gametypeDescription;
    private String difficultyDescription;
    private String lengthDescription;

    public EntityDescriptionsFacadeResult(String gametypeDescription, String difficultyDescription, String lengthDescription) {
        super();
        this.gametypeDescription = gametypeDescription;
        this.difficultyDescription = difficultyDescription;
        this.lengthDescription = lengthDescription;
    }

    public String getGametypeDescription() {
        return gametypeDescription;
    }

    public void setGametypeDescription(String gametypeDescription) {
        this.gametypeDescription = gametypeDescription;
    }

    public String getDifficultyDescription() {
        return difficultyDescription;
    }

    public void setDifficultyDescription(String difficultyDescription) {
        this.difficultyDescription = difficultyDescription;
    }

    public String getLengthDescription() {
        return lengthDescription;
    }

    public void setLengthDescription(String lengthDescription) {
        this.lengthDescription = lengthDescription;
    }
}
