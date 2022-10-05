package services;

import entities.Profile;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import pojos.session.Session;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ConsoleServiceImpl implements ConsoleService {

    private final ProfileService profileService;

    public ConsoleServiceImpl() {
        super();
        this.profileService = new ProfileServiceImpl();
    }

    @Override
    public void runServersByConsole(List<String> parameters) {
        for (String parameter: parameters) {
            if (!"--profiles".equalsIgnoreCase(parameter)) {
                try {
                    Optional<Profile> profileOpt = profileService.findProfileByCode(parameter);
                    if (profileOpt.isPresent()) {
                        /*
                        Kf2Common kf2Common = Kf2Factory.getInstance(
                                Session.getInstance().getPlatform()
                        );
                        kf2Common.runServerByConsole(profileOpt.get());
                        */
                    } else {
                        System.out.println("\n--> Wrong profile name: " + parameter + ". Can not find in database.\n");
                    }
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }
    }
}
