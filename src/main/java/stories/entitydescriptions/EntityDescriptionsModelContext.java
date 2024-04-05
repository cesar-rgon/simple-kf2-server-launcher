package stories.entitydescriptions;

import framework.ModelContext;

public class EntityDescriptionsModelContext extends ModelContext {

    private final String languageCode;
    private final String gameTypeCode;
    private final String difficultyCode;
    private final String lengthCode;

    public EntityDescriptionsModelContext(String languageCode, String gameTypeCode, String difficultyCode, String lengthCode) {
        super();
        this.languageCode = languageCode;
        this.gameTypeCode = gameTypeCode;
        this.difficultyCode = difficultyCode;
        this.lengthCode = lengthCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getGameTypeCode() {
        return gameTypeCode;
    }

    public String getDifficultyCode() {
        return difficultyCode;
    }

    public String getLengthCode() {
        return lengthCode;
    }
}
