package daos;

import entities.ProfileMap;

public class ProfileMapDao extends AbstractDao<ProfileMap> {

    private static ProfileMapDao instance = null;

    /**
     * Singleton constructor
     */
    private ProfileMapDao() {
        super(ProfileMap.class);
    }

    public static ProfileMapDao getInstance() {
        if (instance == null) {
            instance = new ProfileMapDao();
        }
        return instance;
    }


}
