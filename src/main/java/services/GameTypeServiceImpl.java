package services;

import daos.DescriptionDao;
import daos.GameTypeDao;
import entities.GameType;

import java.util.List;
import java.util.Optional;

public class GameTypeServiceImpl implements AbstractService<GameType> {

    public GameTypeServiceImpl() {
        super();
    }


    @Override
    public GameType createItem(GameType gameType) throws Exception {
        DescriptionDao.getInstance().insert(gameType.getDescription());
        return GameTypeDao.getInstance().insert(gameType);
    }

    @Override
    public boolean updateItem(GameType gameType) throws Exception {
        DescriptionDao.getInstance().update(gameType.getDescription());
        return GameTypeDao.getInstance().update(gameType);
    }

    @Override
    public List<GameType> listAll() throws Exception {
        return GameTypeDao.getInstance().listAll();
    }

    @Override
    public Optional<GameType> findByCode(String code) throws Exception {
        return GameTypeDao.getInstance().findByCode(code);
    }

    @Override
    public boolean deleteItem(GameType gameType) throws Exception {
        boolean removed = GameTypeDao.getInstance().remove(gameType);
        DescriptionDao.getInstance().remove(gameType.getDescription());
        return removed;
    }
}
