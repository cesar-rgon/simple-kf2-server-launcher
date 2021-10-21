package entities;

import javax.persistence.*;

@Entity
@Table(name = "LANGUAGES")
public class Language extends AbstractExtendedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="languagesSequence")
    @SequenceGenerator(name="languagesSequence",sequenceName="LANGUAGES_SEQUENCE", allocationSize=1)
    @Column(name="ID", updatable=false, nullable=false)
    private Integer id;

    @Column(name="CODE", length=3, unique=true, nullable=false)
    private String code;

    @Column(name="DESCRIPTION", length=100, unique=true, nullable=false)
    private String description;

    public Language() {
        super();
    }

    public Language(String code, String description) {
        super();
        this.code = code;
        this.description = description;
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

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
