package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "LENGTHS")
public class Length extends AbstractExtendedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="lengthSequence")
    @SequenceGenerator(name="lengthSequence",sequenceName="LENGTH_SEQUENCE", allocationSize=1)
    @Column(name="ID", updatable=false, nullable=false)
    private Integer id;

    @Column(name="CODE", length=100, unique=true, nullable=false)
    private String code;

    // Not mapped attribute. This value is stored in .properties file of each language
    @Transient
    private String description;

    public Length() {
        super();
    }

    public Length(String code) {
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
        return "Length{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
