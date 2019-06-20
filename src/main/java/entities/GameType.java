package entities;

import javax.persistence.*;

@Entity
@Table(name = "GAME_TYPES")
public class GameType extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="ID", updatable=false, nullable=false)
    private Integer id;

    @Column(name="CODE", length=100, unique=true, nullable=false)
    private String code;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ID_DESCRIPTION", referencedColumnName="ID", unique=true, nullable=false)
    private Description description;

    public GameType() {
        super();
    }

    public GameType(String code, Description description) {
        super();
        this.code = code;
        this.description = description;
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

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }
}
