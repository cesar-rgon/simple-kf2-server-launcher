package stories.maxplayersedition;

import daos.DifficultyDao;
import daos.MaxPlayersDao;
import daos.ProfileDao;
import dtos.ProfileDto;
import dtos.SelectDto;
import dtos.factories.DifficultyDtoFactory;
import dtos.factories.MaxPlayersDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.Difficulty;
import entities.MaxPlayers;
import entities.Profile;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import services.*;
import stories.AbstractEditionFacade;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MaxPlayersEditionFacadeImpl extends AbstractEditionFacade<MaxPlayers, SelectDto>  implements MaxPlayersEditionFacade {

    public MaxPlayersEditionFacadeImpl() {
        super(
                MaxPlayers.class,
                MaxPlayersDao.getInstance(),
                new MaxPlayersDtoFactory(),
                new MaxPlayersServiceImpl()
        );
    }

    @Override
    public ProfileDto unselectMaxPlayersInProfile(String profileName) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByCode(profileName);
        if (profileOpt.isPresent()) {
            profileOpt.get().setMaxPlayers(null);
            ProfileDao.getInstance().update(profileOpt.get());
            ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory();
            return profileDtoFactory.newDto(profileOpt.get());
        }
        return null;
    }
}
