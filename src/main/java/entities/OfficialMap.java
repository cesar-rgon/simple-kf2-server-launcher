package entities;

import jakarta.persistence.*;
import java.util.Date;


@Entity
@Table(name = "OFFICIAL_MAPS")
public class OfficialMap extends AbstractMap {

    public OfficialMap() {
        super();
    }

    public OfficialMap(String code, String urlInfo, String urlPhoto, Date releaseDate) {
        super(code, urlInfo, urlPhoto, true, releaseDate);
    }

    @Override
    public String toString() {
        return "OfficialMap{} " + super.toString();
    }
}
