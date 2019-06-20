package entities;

import javax.persistence.*;

@Entity
@Table(name = "PROPERTIES")
public class Property extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="ID", updatable=false, nullable=false)
    private Integer id;

    @Column(name="KEY_PROPERTY", length=100, unique=true, nullable=false)
    private String key;

    @Column(name="VALUE", length=500)
    private String value;

    public Property() {
        super();
    }

    public Property(String key, String value) {
        super();
        this.key = key;
        this.value = value;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
