package entities;

import javax.persistence.*;

@Entity
@Table(name = "GAME_TYPES")
public class GameType extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID", updatable=false, nullable=false)
    private Integer id;

    @Column(name="CODE", length=100, unique=true, nullable=false)
    private String code;

    @Column(name="DIFFICULTY_ENABLED", nullable=false)
    boolean difficultyEnabled;

    @Column(name="LENGTH_ENABLED", nullable=false)
    boolean lengthEnabled;

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
    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

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
}
