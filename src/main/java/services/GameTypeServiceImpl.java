package services;

import daos.DescriptionDao;
import daos.GameTypeDao;
import entities.GameType;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class GameTypeServiceImpl implements AbstractService<GameType> {

    private final EntityManager em;

    public GameTypeServiceImpl(EntityManager em) {
        super();
        this.em = em;
    }


    @Override
    public GameType createItem(GameType gameType) throws Exception {
        new DescriptionDao(em).insert(gameType.getDescription());
        return new GameTypeDao(em).insert(gameType);
    }

    @Override
    public boolean updateItem(GameType gameType) throws Exception {
        new DescriptionDao(em).update(gameType.getDescription());
        return new GameTypeDao(em).update(gameType);
    }

    @Override
    public List<GameType> listAll() throws Exception {
        return new GameTypeDao(em).listAll();
    }

    @Override
    public Optional<GameType> findByCode(String code) throws Exception {
        return new GameTypeDao(em).findByCode(code);
    }

    @Override
    public boolean deleteItem(GameType gameType) throws Exception {
        boolean removed = new GameTypeDao(em).remove(gameType);
        new DescriptionDao(em).remove(gameType.getDescription());
        return removed;
    }
}
