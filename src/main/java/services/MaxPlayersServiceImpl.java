package services;

import daos.DescriptionDao;
import daos.MaxPlayersDao;
import entities.MaxPlayers;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class MaxPlayersServiceImpl implements AbstractService<MaxPlayers> {

    private final EntityManager em;

    public MaxPlayersServiceImpl(EntityManager em) {
        super();
        this.em = em;
    }


    @Override
    public MaxPlayers createItem(MaxPlayers maxPlayers) throws Exception {
        new DescriptionDao(em).insert(maxPlayers.getDescription());
        return new MaxPlayersDao(em).insert(maxPlayers);
    }

    @Override
    public boolean updateItem(MaxPlayers maxPlayers) throws Exception {
        new DescriptionDao(em).update(maxPlayers.getDescription());
        return new MaxPlayersDao(em).update(maxPlayers);
    }

    @Override
    public List<MaxPlayers> listAll() throws Exception {
        return new MaxPlayersDao(em).listAll();
    }

    @Override
    public Optional<MaxPlayers> findByCode(String code) throws Exception {
        return new MaxPlayersDao(em).findByCode(code);
    }

    @Override
    public boolean deleteItem(MaxPlayers maxPlayers) throws Exception {
       boolean removed = new MaxPlayersDao(em).remove(maxPlayers);
        new DescriptionDao(em).remove(maxPlayers.getDescription());
        return removed;
    }
}
