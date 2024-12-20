package pojos.session;

import dtos.PlatformDto;
import dtos.PlatformProfileMapDto;
import dtos.ProfileDto;
import entities.PlatformProfileMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.enums.EnumLanguage;
import pojos.enums.EnumMapsTab;
import pojos.enums.EnumSortedMapsCriteria;

import java.util.ArrayList;
import java.util.List;

public class Session {

    private static final Logger logger = LogManager.getLogger(Session.class);
    private static Session instance = null;

    private String actualProfileName;
    private String console;
    private PlatformProfileMapDto ppm;
    private List<Process> processList;
    private ProfileDto mapsProfile;
    private EnumSortedMapsCriteria sortedMapsCriteria;
    private EnumMapsTab selectedMapTab;
    private List<PlatformProfileMapDto> platformProfileMapList;
    private PlatformDto platform;
    private String url;
    private boolean firstBoot;
    private boolean wizardMode;
    private EnumLanguage wizardLanguage;

    /**
     * Singleton constructor
     */
    private Session() {
        super();
        console = StringUtils.EMPTY;
        url = StringUtils.EMPTY;
        ppm = null;
        processList = new ArrayList<Process>();
        sortedMapsCriteria = EnumSortedMapsCriteria.NAME_DESC;
        selectedMapTab = EnumMapsTab.STEAM_OFFICIAL_MAPS_TAB;
        platformProfileMapList = new ArrayList<PlatformProfileMapDto>();
        firstBoot = true;
        wizardLanguage = EnumLanguage.en;
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public boolean isRunningProcess() {
        if (processList.isEmpty()) {
            return false;
        }
        for (Process process: processList) {
            if (process.isAlive()) {
                return true;
            }
        }
        return false;
    }

    public String getActualProfileName() {
        return actualProfileName;
    }

    public void setActualProfileName(String actualProfileName) {
        this.actualProfileName = actualProfileName;
    }

    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }

    public PlatformProfileMapDto getPpm() {
        return ppm;
    }

    public void setPpm(PlatformProfileMapDto ppm) {
        this.ppm = ppm;
    }

    public List<Process> getProcessList() {
        return processList;
    }

    public void setProcessList(List<Process> processList) {
        this.processList = processList;
    }

    public ProfileDto getMapsProfile() {
        return mapsProfile;
    }

    public void setMapsProfile(ProfileDto mapsProfile) {
        this.mapsProfile = mapsProfile;
    }

    public EnumSortedMapsCriteria getSortedMapsCriteria() {
        return sortedMapsCriteria;
    }

    public void setSortedMapsCriteria(EnumSortedMapsCriteria sortedMapsCriteria) {
        this.sortedMapsCriteria = sortedMapsCriteria;
    }

    public EnumMapsTab getSelectedMapTab() {
        return selectedMapTab;
    }

    public void setSelectedMapTab(EnumMapsTab selectedMapTab) {
        this.selectedMapTab = selectedMapTab;
    }

    public List<PlatformProfileMapDto> getPlatformProfileMapList() {
        return platformProfileMapList;
    }

    public void setPlatformProfileMapList(List<PlatformProfileMapDto> platformProfileMapList) {
        this.platformProfileMapList = platformProfileMapList;
    }

    public PlatformDto getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformDto platform) {
        this.platform = platform;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isFirstBoot() {
        return firstBoot;
    }

    public void setFirstBoot(boolean firstBoot) {
        this.firstBoot = firstBoot;
    }

    public boolean isWizardMode() {
        return wizardMode;
    }

    public void setWizardMode(boolean wizardMode) {
        this.wizardMode = wizardMode;
    }

    public EnumLanguage getWizardLanguage() {
        return wizardLanguage;
    }

    public void setWizardLanguage(EnumLanguage wizardLanguage) {
        this.wizardLanguage = wizardLanguage;
    }
}
