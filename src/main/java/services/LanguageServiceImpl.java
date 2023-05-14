package services;

import daos.LanguageDao;
import entities.Language;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class LanguageServiceImpl extends AbstractService<Language> {

    public LanguageServiceImpl(EntityManager em) {
        super(em);
    }

    @Override
    public List<Language> listAll() throws Exception {
        return new LanguageDao(em).listAll();
    }

    @Override
    public Optional<Language> findByCode(String languageCode) throws Exception {
        return new LanguageDao(em).findByCode(languageCode);
    }

    @Override
    public Language createItem(Language language) throws Exception {
        return new LanguageDao(em).insert(language);
    }

    @Override
    public boolean updateItem(Language language) throws Exception {
        return new LanguageDao(em).update(language);
    }

    @Override
    public boolean deleteItem(Language language) throws Exception {
        return new LanguageDao(em).remove(language);
    }
}
