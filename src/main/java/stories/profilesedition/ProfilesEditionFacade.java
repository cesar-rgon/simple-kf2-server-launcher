package stories.profilesedition;

import dtos.ProfileDto;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface ProfilesEditionFacade {
    ObservableList<ProfileDto> listAllProfiles() throws SQLException;
}
