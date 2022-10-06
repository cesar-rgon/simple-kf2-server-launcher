package stories.webadmin;

import dtos.ProfileDto;

import java.sql.SQLException;

public interface WebAdminFacade {
    ProfileDto findProfileDtoByName(String profileName) throws Exception;
}
