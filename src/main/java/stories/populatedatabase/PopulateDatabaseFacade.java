package stories.populatedatabase;

import entities.Difficulty;
import entities.GameType;
import entities.Length;
import entities.MaxPlayers;
import framework.EmptyFacadeResult;

import java.util.List;

public interface PopulateDatabaseFacade {
    EmptyFacadeResult execute() throws Exception;
    List<Difficulty> loadDefaultDifficulties() throws Exception;
    List<GameType> loadDefaultGametypes() throws Exception;
    List<Length> loadDefaultLengths() throws Exception;
    List<MaxPlayers> loadDefaultMaxPlayers() throws Exception;
}
