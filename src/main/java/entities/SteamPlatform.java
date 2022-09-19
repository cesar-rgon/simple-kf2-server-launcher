package entities;

import pojos.enums.EnumPlatform;
import jakarta.persistence.*;

@Entity
@Table(name = "STEAM_PLATFORM")
public class SteamPlatform extends AbstractPlatform {

    @Column(name="VALIDATE_FILES", unique=false, nullable=true)
    private boolean validateFiles;

    @Column(name="IS_BETA", unique=false, nullable=true)
    private boolean isBeta;

    @Column(name="BETA_BRUNCH", length=100, unique=false, nullable=true)
    private String betaBrunch;

    public SteamPlatform() {
        super();
    }

    public SteamPlatform(EnumPlatform platform) {
        super(platform);
    }

    public SteamPlatform(EnumPlatform platform, boolean validateFiles, boolean isBeta, String betaBrunch) {
        super(platform);
        this.validateFiles = validateFiles;
        this.isBeta = isBeta;
        this.betaBrunch = betaBrunch;
    }

    public boolean isValidateFiles() {
        return validateFiles;
    }

    public void setValidateFiles(boolean validateFiles) {
        this.validateFiles = validateFiles;
    }

    public boolean isBeta() {
        return isBeta;
    }

    public void setBeta(boolean beta) {
        isBeta = beta;
    }

    public String getBetaBrunch() {
        return betaBrunch;
    }

    public void setBetaBrunch(String betaBrunch) {
        this.betaBrunch = betaBrunch;
    }

    @Override
    public String toString() {
        return "SteamPlatform{" +
                "validateFiles=" + validateFiles +
                ", isBeta=" + isBeta +
                ", betaBrunch='" + betaBrunch + '\'' +
                "} " + super.toString();
    }
}
