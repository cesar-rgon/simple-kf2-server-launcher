package services;

import daos.ProfileDao;
import entities.Profile;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ConsoleServiceImpl implements ConsoleService {

    @Override
    public void runServersByConsole(List<String> parameters) {
        for (String parameter: parameters) {
            if (!"--profiles".equalsIgnoreCase(parameter)) {
                try {
                    Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(parameter);
                    if (profileOpt.isPresent()) {
                        Kf2Common kf2Common = Kf2Factory.getInstance();
                        kf2Common.runServerByConsole(profileOpt.get());
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
