package entities;

import javax.persistence.*;

@Entity
@Table(name = "DESCRIPTIONS")
public class Description extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="ID", updatable=false, nullable=false)
    private Integer id;

    @Column(name="EN", length=255, unique=false, nullable=false)
    private String englishText;

    @Column(name="ES", length=255, unique=false, nullable=false)
    private String spanishText;

    public Description() {
        super();
    }

    public Description(String englishText, String spanishText) {
        super();
        this.englishText = englishText;
        this.spanishText = spanishText;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getEnglishText() {
        return englishText;
    }

    public void setEnglishText(String englishText) {
        this.englishText = englishText;
    }

    public String getSpanishText() {
        return spanishText;
    }

    public void setSpanishText(String spanishText) {
        this.spanishText = spanishText;
    }

}
