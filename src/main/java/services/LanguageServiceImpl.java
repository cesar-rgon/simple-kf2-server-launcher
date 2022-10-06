package services;

import daos.LanguageDao;
import entities.Language;

import java.util.List;
import java.util.Optional;

public class LanguageServiceImpl implements AbstractService<Language> {

    @Override
    public List<Language> listAll() throws Exception {
        return LanguageDao.getInstance().listAll();
    }

    @Override
    public Optional<Language> findByCode(String languageCode) throws Exception {
        return LanguageDao.getInstance().findByCode(languageCode);
    }

    @Override
    public Language createItem(Language language) throws Exception {
        return LanguageDao.getInstance().insert(language);
    }

    @Override
    public boolean updateItem(Language language) throws Exception {
        return LanguageDao.getInstance().update(language);
    }

    @Override
    public boolean deleteItem(Language language) throws Exception {
        return LanguageDao.getInstance().remove(language);
    }
}
