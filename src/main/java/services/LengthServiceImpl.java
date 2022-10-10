package services;

import daos.DescriptionDao;
import daos.LengthDao;
import entities.Length;

import java.util.List;
import java.util.Optional;

public class LengthServiceImpl implements AbstractService<Length> {

    public LengthServiceImpl() {
        super();
    }

    @Override
    public Length createItem(Length length) throws Exception {
        DescriptionDao.getInstance().insert(length.getDescription());
        return LengthDao.getInstance().insert(length);
    }

    @Override
    public boolean updateItem(Length length) throws Exception {
        DescriptionDao.getInstance().update(length.getDescription());
        return LengthDao.getInstance().update(length);
    }

    @Override
    public List<Length> listAll() throws Exception {
        return LengthDao.getInstance().listAll();
    }

    @Override
    public Optional<Length> findByCode(String code) throws Exception {
        return LengthDao.getInstance().findByCode(code);
    }


    @Override
    public boolean deleteItem(Length length) throws Exception {
        boolean removed = LengthDao.getInstance().remove(length);
        DescriptionDao.getInstance().remove(length.getDescription());
        return removed;
    }
}
