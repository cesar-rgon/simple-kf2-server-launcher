package services;

import daos.DescriptionDao;
import daos.DifficultyDao;
import entities.Difficulty;

import java.util.List;
import java.util.Optional;

public class DifficultyServiceImpl implements AbstractService<Difficulty> {

    public DifficultyServiceImpl() {
        super();
    }


    @Override
    public Difficulty createItem(Difficulty difficulty) throws Exception {
        DescriptionDao.getInstance().insert(difficulty.getDescription());
        return DifficultyDao.getInstance().insert(difficulty);
    }

    @Override
    public boolean updateItem(Difficulty difficulty) throws Exception {
        DescriptionDao.getInstance().update(difficulty.getDescription());
        return DifficultyDao.getInstance().update(difficulty);
    }

    @Override
    public List<Difficulty> listAll() throws Exception {
        return DifficultyDao.getInstance().listAll();
    }

    @Override
    public Optional<Difficulty> findByCode(String code) throws Exception {
        return DifficultyDao.getInstance().findByCode(code);
    }

    @Override
    public boolean deleteItem(Difficulty difficulty) throws Exception {
        boolean removed = DifficultyDao.getInstance().remove(difficulty);
        DescriptionDao.getInstance().remove(difficulty.getDescription());
        return removed;
    }

}
