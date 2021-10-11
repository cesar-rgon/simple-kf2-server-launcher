package entities;

import javax.persistence.*;

@Entity
@Table(name = "MAX_PLAYERS")
public class MaxPlayers extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID", updatable=false, nullable=false)
    private Integer id;

    @Column(name="CODE", length=100, unique=true, nullable=false)
    private String code;

    // Not mapped attribute. This value is stored in .properties file of each language
    private String description;

    public MaxPlayers() {
        super();
    }

    public MaxPlayers(String code) {
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

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
