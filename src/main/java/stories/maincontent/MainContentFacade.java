package stories.maincontent;

import dtos.GameTypeDto;
import dtos.MapDto;
import dtos.ProfileDto;
import dtos.SelectDto;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.Optional;

public interface MainContentFacade {
    ObservableList<ProfileDto> listAllProfiles() throws SQLException;
    ObservableList<SelectDto> listAllLanguages() throws SQLException;
    ObservableList<GameTypeDto> listAllGameTypes() throws SQLException;
    ObservableList<MapDto> listDownloadedMaps() throws SQLException;
    ObservableList<SelectDto> listAllDifficulties() throws SQLException;
    ObservableList<SelectDto> listAllLengths() throws SQLException;
    ObservableList<SelectDto> listAllPlayers() throws SQLException;
    boolean updateProfileSetGameType(String profileName, String gameTypeCode) throws SQLException;
    boolean updateProfileSetMap(String profileName, String mapCode) throws SQLException;
    boolean updateProfileSetDifficulty(String profileName, String difficultyCode) throws SQLException;
    boolean updateProfileSetLength(String profileName, String lengthCode) throws SQLException;
    boolean updateProfileSetMaxPlayers(String profileName, String maxPlayersCode) throws SQLException;
    boolean updateProfileSetLanguage(String profileName, String languageCode) throws SQLException;
    boolean updateProfileSetServerName(String profileName, String serverName) throws SQLException;
    boolean updateProfileSetServerPassword(String profileName, String serverPassword) throws Exception;
    boolean updateProfileSetWebPassword(String profileName, String webPassword) throws Exception;
    boolean updateProfileSetWebPort(String profileName, Integer webPort) throws SQLException;
    boolean updateProfileSetGamePort(String profileName, Integer gamePort) throws SQLException;
    boolean updateProfileSetQueryPort(String profileName, Integer queryPort) throws SQLException;
    boolean updateProfileSetYourClan(String profileName, String yourClan) throws SQLException;
    boolean updateProfileSetYourWebLink(String profileName, String yourWebLink) throws SQLException;
    boolean updateProfileSetUrlImageServer(String profileName, String urlImageServer) throws SQLException;
    boolean updateProfileSetWelcomeMessage(String profileName, String welcomeMessage) throws SQLException;
    boolean updateProfileSetCustomParameters(String profileName, String customParameters) throws SQLException;
    boolean updateProfileSetWebPage(String profileName, boolean isSelected) throws SQLException;
    ProfileDto findProfileByName(String name) throws SQLException;
    String runServer(String profileName) throws SQLException;
    String joinServer(String profileName) throws SQLException;
}
