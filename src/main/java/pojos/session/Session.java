package pojos.session;

import dtos.AbstractMapDto;
import dtos.PlatformDto;
import dtos.ProfileDto;
import dtos.factories.ProfileDtoFactory;
import entities.PlatformProfileMap;
import entities.Profile;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.enums.EnumMapsTab;
import pojos.enums.EnumSortedMapsCriteria;
import services.ProfileService;
import services.ProfileServiceImpl;
import stories.maxplayersedition.MaxPlayersEditionController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Session {

    private static final Logger logger = LogManager.getLogger(Session.class);
    private static Session instance = null;

    private String actualProfileName;
    private String console;
    private AbstractMapDto map;
    private List<Process> processList;
    private ProfileDto mapsProfile;
    private EnumSortedMapsCriteria sortedMapsCriteria;
    private EnumMapsTab selectedMapTab;
    private List<PlatformProfileMap> platformProfileMapList;
    private PlatformDto platform;
    private final ProfileService profileService;
    private final ProfileDtoFactory profileDtoFactory;

    /**
     * Singleton constructor
     */
    private Session() {
        super();
        console = "";
        map = null;
        processList = new ArrayList<Process>();
        sortedMapsCriteria = EnumSortedMapsCriteria.NAME_DESC;
        selectedMapTab = EnumMapsTab.STEAM_OFFICIAL_MAPS_TAB;
        platformProfileMapList = new ArrayList<PlatformProfileMap>();
        profileService = new ProfileServiceImpl();
        profileDtoFactory = new ProfileDtoFactory();
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

    public ProfileDto getActualProfile() {
        if (StringUtils.isEmpty(getActualProfileName())) {
            return null;
        }
        try {
            Optional<Profile> actualProfileOptional = profileService.findProfileByCode(getActualProfileName());
            if (actualProfileOptional.isPresent()) {
                return profileDtoFactory.newDto(actualProfileOptional.get());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
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

    public AbstractMapDto getMap() {
        return map;
    }

    public void setMap(AbstractMapDto map) {
        this.map = map;
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

    public List<PlatformProfileMap> getProfileMapList() {
        return platformProfileMapList;
    }

    public void setProfileMapList(List<PlatformProfileMap> platformProfileMapList) {
        this.platformProfileMapList = platformProfileMapList;
    }

    public List<PlatformProfileMap> getPlatformProfileMapList() {
        return platformProfileMapList;
    }

    public void setPlatformProfileMapList(List<PlatformProfileMap> platformProfileMapList) {
        this.platformProfileMapList = platformProfileMapList;
    }

    public PlatformDto getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformDto platform) {
        this.platform = platform;
    }
}
