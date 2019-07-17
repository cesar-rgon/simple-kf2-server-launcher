package stories.maincontent;

import daos.*;
import dtos.MapDto;
import dtos.ProfileDto;
import dtos.SelectDto;
import dtos.factories.*;
import entities.*;
import javafx.collections.ObservableList;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import utils.Utils;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MainContentFacadeImpl implements MainContentFacade {

    private final LanguageDtoFactory languageDtoFactory;
    private final ProfileDtoFactory profileDtoFactory;
    private final GameTypeDtoFactory gameTypeDtoFactory;
    private final MapDtoFactory mapDtoFactory;
    private final DifficultyDtoFactory difficultyDtoFactory;
    private final LengthDtoFactory lengthDtoFactory;
    private final MaxPlayersDtoFactory maxPlayersDtoFactory;

    public MainContentFacadeImpl() {
        super();
        languageDtoFactory = new LanguageDtoFactory();
        profileDtoFactory = new ProfileDtoFactory();
        gameTypeDtoFactory = new GameTypeDtoFactory();
        mapDtoFactory = new MapDtoFactory();
        difficultyDtoFactory = new DifficultyDtoFactory();
        lengthDtoFactory = new LengthDtoFactory();
        maxPlayersDtoFactory = new MaxPlayersDtoFactory();
    }

    @Override
    public ObservableList<ProfileDto> listAllProfiles() throws SQLException {
        List<Profile> profiles = ProfileDao.getInstance().listAll();
        return profileDtoFactory.newDtos(profiles);
    }

    @Override
    public ObservableList<SelectDto> listAllLanguages() throws SQLException {
        List<Language> languages = LanguageDao.getInstance().listAll();
        return languageDtoFactory.newDtos(languages);
    }

    @Override
    public ObservableList<SelectDto> listAllGameTypes() throws SQLException {
        List<GameType> gameTypes = GameTypeDao.getInstance().listAll();
        return gameTypeDtoFactory.newDtos(gameTypes);
    }

    @Override
    public ObservableList<MapDto> listDownloadedMaps() throws SQLException {
        List<Map> maps = MapDao.getInstance().listDownloadedMaps();
        return mapDtoFactory.newDtos(maps);
    }

    @Override
    public ObservableList<SelectDto> listAllDifficulties() throws SQLException {
        List<Difficulty> difficulties = DifficultyDao.getInstance().listAll();
        return difficultyDtoFactory.newDtos(difficulties);
    }

    @Override
    public ObservableList<SelectDto> listAllLengths() throws SQLException {
        List<Length> lengths = LengthDao.getInstance().listAll();
        return lengthDtoFactory.newDtos(lengths);
    }

    @Override
    public ObservableList<SelectDto> listAllPlayers() throws SQLException {
        List<MaxPlayers> players = MaxPlayersDao.getInstance().listAll();
        return maxPlayersDtoFactory.newDtos(players);
    }

    @Override
    public boolean updateProfileSetGameType(String profileName, String gameTypeCode) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<GameType> gameTypeOpt = GameTypeDao.getInstance().findByCode(gameTypeCode);
            if (gameTypeOpt.isPresent()) {
                profile.setGametype(gameTypeOpt.get());
                return ProfileDao.getInstance().update(profile);
            }
        }
        return false;
    }

    @Override
    public boolean updateProfileSetMap(String profileName, String mapCode) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<Map> mapOpt = MapDao.getInstance().findByCode(mapCode);
            if (mapOpt.isPresent()) {
                profile.setMap(mapOpt.get());
                return ProfileDao.getInstance().update(profile);
            }
        }
        return false;
    }

    @Override
    public boolean updateProfileSetDifficulty(String profileName, String difficultyCode) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<Difficulty> difficultyOpt = DifficultyDao.getInstance().findByCode(difficultyCode);
            if (difficultyOpt.isPresent()) {
                profile.setDifficulty(difficultyOpt.get());
                return ProfileDao.getInstance().update(profile);
            }
        }
        return false;
    }

    @Override
    public boolean updateProfileSetLength(String profileName, String lengthCode) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<Length> lengthOpt = LengthDao.getInstance().findByCode(lengthCode);
            if (lengthOpt.isPresent()) {
                profile.setLength(lengthOpt.get());
                return ProfileDao.getInstance().update(profile);
            }
        }
        return false;
    }

    @Override
    public boolean updateProfileSetMaxPlayers(String profileName, String maxPlayersCode) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<MaxPlayers> maxPlayersOpt = MaxPlayersDao.getInstance().findByCode(maxPlayersCode);
            if (maxPlayersOpt.isPresent()) {
                profile.setMaxPlayers(maxPlayersOpt.get());
                return ProfileDao.getInstance().update(profile);
            }
        }
        return false;
    }

    @Override
    public boolean updateProfileSetLanguage(String profileName, String languageCode) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            Optional<Language> languageOpt = LanguageDao.getInstance().findByCode(languageCode);
            if (languageOpt.isPresent()) {
                profile.setLanguage(languageOpt.get());
                return ProfileDao.getInstance().update(profile);
            }
        }
        return false;
    }

    @Override
    public boolean updateProfileSetServerName(String profileName, String serverName) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setServerName(serverName);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetServerPassword(String profileName, String serverPassword) throws Exception {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setServerPassword(Utils.encryptAES(serverPassword));
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetWebPassword(String profileName, String webPassword) throws Exception {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setWebPassword(Utils.encryptAES(webPassword));
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetWebPort(String profileName, Integer webPort) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setWebPort(webPort);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetGamePort(String profileName, Integer gamePort) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setGamePort(gamePort);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetQueryPort(String profileName, Integer queryPort) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setQueryPort(queryPort);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetYourClan(String profileName, String yourClan) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setYourClan(yourClan);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetYourWebLink(String profileName, String yourWebLink) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setYourWebLink(yourWebLink);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetUrlImageServer(String profileName, String urlImageServer) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setUrlImageServer(urlImageServer);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetWelcomeMessage(String profileName, String welcomeMessage) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setWelcomeMessage(welcomeMessage);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetCustomParameters(String profileName, String customParameters) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setCustomParameters(customParameters);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public boolean updateProfileSetWebPage(String profileName, boolean isSelected) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setWebPage(isSelected);
            return ProfileDao.getInstance().update(profile);
        }
        return false;
    }

    @Override
    public ProfileDto findProfileByName(String name) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(name);
        if (profileOpt.isPresent()) {
            return profileDtoFactory.newDto(profileOpt.get());
        } else {
            return null;
        }
    }

    @Override
    public String runServer(String profileName) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        Kf2Common kf2Common = Kf2Factory.getInstance();
        return kf2Common.runServer(profileOpt.isPresent()? profileOpt.get(): null);
    }

    @Override
    public void joinServer(String profileName) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Kf2Common kf2Common = Kf2Factory.getInstance();
            kf2Common.joinServer(profileOpt.get());
        }
    }
 }
