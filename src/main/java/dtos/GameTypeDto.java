package dtos;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class GameTypeDto extends SelectDto {

    private final BooleanProperty difficultyEnabled;
    private final BooleanProperty lengthEnabled;

    public GameTypeDto(String key, String value, boolean difficultyEnabled, boolean lengthEnabled) {
        super(key, value);
        this.difficultyEnabled = new SimpleBooleanProperty(difficultyEnabled);
        this.lengthEnabled = new SimpleBooleanProperty(lengthEnabled);
    }

    public BooleanProperty isDifficultyEnabledProperty() {
        return difficultyEnabled;
    }

    public BooleanProperty isLengthEnabledProperty() {
        return lengthEnabled;
    }

    public boolean isDifficultyEnabled() {
        return difficultyEnabled.get();
    }

    public boolean isLengthEnabled() {
        return lengthEnabled.get();
    }
}
