package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "DESCRIPTIONS")
public class Description extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="descriptionSequence")
    @SequenceGenerator(name="descriptionSequence",sequenceName="DESCRIPTIONS_SEQUENCE", allocationSize=1)
    @Column(name="ID", updatable=false, nullable=false)
    private Integer id;

    @Column(name="ENGLISH_VALUE", length=255, unique=true, nullable=true)
    private String englishValue;

    @Column(name="SPANISH_VALUE", length=255, unique=true, nullable=true)
    private String spanishValue;

    @Column(name="FRENCH_VALUE", length=255, unique=true, nullable=true)
    private String frenchValue;

    public Description() {
        super();
    }

    public Description(String englishValue, String spanishValue, String frenchValue) {
        super();
        this.englishValue = englishValue;
        this.spanishValue = spanishValue;
        this.frenchValue = frenchValue;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Object id) {
        this.id = (Integer) id;
    }

    public String getEnglishValue() {
        return englishValue;
    }

    public void setEnglishValue(String englishValue) {
        this.englishValue = englishValue;
    }

    public String getSpanishValue() {
        return spanishValue;
    }

    public void setSpanishValue(String spanishValue) {
        this.spanishValue = spanishValue;
    }

    public String getFrenchValue() {
        return frenchValue;
    }

    public void setFrenchValue(String frenchValue) {
        this.frenchValue = frenchValue;
    }
}
