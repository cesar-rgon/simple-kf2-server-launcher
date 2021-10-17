package entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "OFFICIAL_MAPS")
public class OfficialMap extends AbstractMap {

    public OfficialMap() {
        super();
    }

    public OfficialMap(String code, String urlInfo, String urlPhoto, List<Profile> profileList, Date releaseDate) {
        super(code, urlInfo, urlPhoto, profileList, true, releaseDate);
    }
}
