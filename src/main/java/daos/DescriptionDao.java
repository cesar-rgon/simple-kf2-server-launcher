package daos;

import entities.Description;

public class DescriptionDao extends CommonDao<Description> {

    private static DescriptionDao instance = null;

    /**
     * Singleton constructor
     */
    private DescriptionDao() {
        super(Description.class);
    }

    public static DescriptionDao getInstance() {
        if (instance == null) {
            instance = new DescriptionDao();
        }
        return instance;
    }
}
