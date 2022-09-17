package dtos;

public class SteamPlatformDto extends PlatformDto {

    private final boolean validateFiles;
    private final boolean isBeta;
    private final String betaBrunch;

    public SteamPlatformDto(String key, String value, String logoPath, String smallLogoPath, String installationFolder, boolean validateFiles, boolean isBeta, String betaBrunch) {
        super(key, value, logoPath, smallLogoPath, installationFolder);
        this.validateFiles = validateFiles;
        this.isBeta = isBeta;
        this.betaBrunch = betaBrunch;
    }

    public boolean isValidateFiles() {
        return validateFiles;
    }

    public boolean isBeta() {
        return isBeta;
    }

    public String getBetaBrunch() {
        return betaBrunch;
    }


}
