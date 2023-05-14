package services;

import daos.DescriptionDao;
import daos.DifficultyDao;
import entities.AbstractEntity;
import entities.AbstractExtendedEntity;
import entities.Difficulty;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class DifficultyServiceImpl extends AbstractService<Difficulty> {

    public DifficultyServiceImpl() {
        super();
    }

    public DifficultyServiceImpl(EntityManager em) {
        super(em);
    }


    @Override
    public Difficulty createItem(Difficulty difficulty) throws Exception {
        new DescriptionDao(em).insert(difficulty.getDescription());
        return new DifficultyDao(em).insert(difficulty);
    }

    @Override
    public boolean updateItem(Difficulty difficulty) throws Exception {
        new DescriptionDao(em).update(difficulty.getDescription());
        return new DifficultyDao(em).update(difficulty);
    }

    @Override
    public List<Difficulty> listAll() throws Exception {
        return new DifficultyDao(em).listAll();
    }

    @Override
    public Optional<Difficulty> findByCode(String code) throws Exception {
        return new DifficultyDao(em).findByCode(code);
    }

    @Override
    public boolean deleteItem(Difficulty difficulty) throws Exception {
        boolean removed = new DifficultyDao(em).remove(difficulty);
        new DescriptionDao(em).remove(difficulty.getDescription());
        return removed;
    }


}
