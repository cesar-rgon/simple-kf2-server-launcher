package services;

import daos.ProfileMapDao;
import entities.ProfileMap;

public class ProfileMapServiceImpl implements ProfileMapService {

    @Override
    public ProfileMap createItem(ProfileMap entity) throws Exception {
        return ProfileMapDao.getInstance().insert(entity);
    }

    @Override
    public boolean deleteItem(ProfileMap entity) throws Exception {
        return ProfileMapDao.getInstance().remove(entity);
    }
}
