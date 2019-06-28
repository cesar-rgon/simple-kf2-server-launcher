package stories.webadmin;

import dtos.ProfileDto;

import java.sql.SQLException;

public interface WebAdminFacade {
    ProfileDto findProfileByName(String profileName) throws SQLException;
}
