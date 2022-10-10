package services;

import daos.DescriptionDao;
import daos.MaxPlayersDao;
import entities.MaxPlayers;

import java.util.List;
import java.util.Optional;

public class MaxPlayersServiceImpl implements AbstractService<MaxPlayers> {

    public MaxPlayersServiceImpl() {
        super();
    }


    @Override
    public MaxPlayers createItem(MaxPlayers maxPlayers) throws Exception {
        DescriptionDao.getInstance().insert(maxPlayers.getDescription());
        return MaxPlayersDao.getInstance().insert(maxPlayers);
    }

    @Override
    public boolean updateItem(MaxPlayers maxPlayers) throws Exception {
        DescriptionDao.getInstance().update(maxPlayers.getDescription());
        return MaxPlayersDao.getInstance().update(maxPlayers);
    }

    @Override
    public List<MaxPlayers> listAll() throws Exception {
        return MaxPlayersDao.getInstance().listAll();
    }

    @Override
    public Optional<MaxPlayers> findByCode(String code) throws Exception {
        return MaxPlayersDao.getInstance().findByCode(code);
    }

    @Override
    public boolean deleteItem(MaxPlayers maxPlayers) throws Exception {
       boolean removed = MaxPlayersDao.getInstance().remove(maxPlayers);
        DescriptionDao.getInstance().remove(maxPlayers.getDescription());
        return removed;
    }
}
