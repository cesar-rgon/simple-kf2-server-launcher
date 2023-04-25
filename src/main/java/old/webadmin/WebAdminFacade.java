package old.webadmin;

import dtos.ProfileDto;

public interface WebAdminFacade {
    ProfileDto findProfileDtoByName(String profileName) throws Exception;
}
