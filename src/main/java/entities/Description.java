package entities;

import jakarta.persistence.*;
import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "DESCRIPTIONS")
public class Description extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="descriptionSequence")
    @SequenceGenerator(name="descriptionSequence",sequenceName="DESCRIPTIONS_SEQUENCE", allocationSize=1)
    @Column(name="ID", updatable=false, nullable=false)
    private Integer id;

    @Column(name="ENGLISH_VALUE", length=255, unique=false, nullable=true)
    private String englishValue;

    @Column(name="SPANISH_VALUE", length=255, unique=false, nullable=true)
    private String spanishValue;

    @Column(name="FRENCH_VALUE", length=255, unique=false, nullable=true)
    private String frenchValue;

    @Column(name="RUSSIAN_VALUE", length=255, unique=false, nullable=true)
    private String russianValue;

    public Description() {
        super();
    }

    public Description(String englishValue, String spanishValue, String frenchValue, String russianValue) {
        super();
        this.englishValue = englishValue;
        this.spanishValue = spanishValue;
        this.frenchValue = frenchValue;
        this.russianValue = russianValue;
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

    public String getRussianValue() {
        return russianValue;
    }

    public void setRussianValue(String russianValue) {
        this.russianValue = russianValue;
    }

    public String getValue(String languageCode) {
        switch (languageCode) {
            case "en": return englishValue;
            case "es": return spanishValue;
            case "fr": return frenchValue;
            case "ru": return russianValue;
            default: return StringUtils.EMPTY;
        }
    }

    public void setValue(String value, String languageCode) {
        switch (languageCode) {
            case "en": englishValue = value;
            case "es": spanishValue = value;
            case "fr": frenchValue = value;
            case "ru": russianValue = value;
            default: ;
        }
    }
}
