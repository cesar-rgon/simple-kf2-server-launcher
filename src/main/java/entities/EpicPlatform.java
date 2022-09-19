package entities;

import pojos.enums.EnumPlatform;
import jakarta.persistence.*;

@Entity
@Table(name = "EPIC_PLATFORM")
public class EpicPlatform extends AbstractPlatform {

    public EpicPlatform() {
        super();
    }

    public EpicPlatform(EnumPlatform platform) {
        super(platform);
    }

    @Override
    public String toString() {
        return "EpicPlatform{} " + super.toString();
    }
}
