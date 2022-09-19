package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "GAME_TYPES")
public class GameType extends AbstractExtendedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gameTypesSequence")
    @SequenceGenerator(name="gameTypesSequence",sequenceName="GAME_TYPES_SEQUENCE", allocationSize=1)
    @Column(name="ID", updatable=false, nullable=false)
    private Integer id;

    @Column(name="CODE", length=100, unique=true, nullable=false)
    private String code;

    @Column(name="DIFFICULTY_ENABLED", nullable=false)
    boolean difficultyEnabled;

    @Column(name="LENGTH_ENABLED", nullable=false)
    boolean lengthEnabled;

    // Not mapped attribute. This value is stored in .properties file of each language
    @Transient
    private String description;

    public GameType() {
        super();
    }

    public GameType(String code) {
        super();
        this.code = code;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Object id) {
        this.id = (Integer) id;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    public boolean isDifficultyEnabled() {
        return difficultyEnabled;
    }

    public void setDifficultyEnabled(boolean difficultyEnabled) {
        this.difficultyEnabled = difficultyEnabled;
    }

    public boolean isLengthEnabled() {
        return lengthEnabled;
    }

    public void setLengthEnabled(boolean lengthEnabled) {
        this.lengthEnabled = lengthEnabled;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "GameType{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", difficultyEnabled=" + difficultyEnabled +
                ", lengthEnabled=" + lengthEnabled +
                ", description='" + description + '\'' +
                '}';
    }
}
