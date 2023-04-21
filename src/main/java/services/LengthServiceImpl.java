package services;

import daos.DescriptionDao;
import daos.LengthDao;
import entities.Length;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class LengthServiceImpl implements AbstractService<Length> {

    private final EntityManager em;

    public LengthServiceImpl(EntityManager em) {
        super();
        this.em = em;
    }

    @Override
    public Length createItem(Length length) throws Exception {
        new DescriptionDao(em).insert(length.getDescription());
        return new LengthDao(em).insert(length);
    }

    @Override
    public boolean updateItem(Length length) throws Exception {
        new DescriptionDao(em).update(length.getDescription());
        return new LengthDao(em).update(length);
    }

    @Override
    public List<Length> listAll() throws Exception {
        return new LengthDao(em).listAll();
    }

    @Override
    public Optional<Length> findByCode(String code) throws Exception {
        return new LengthDao(em).findByCode(code);
    }


    @Override
    public boolean deleteItem(Length length) throws Exception {
        boolean removed = new LengthDao(em).remove(length);
        new DescriptionDao(em).remove(length.getDescription());
        return removed;
    }
}
