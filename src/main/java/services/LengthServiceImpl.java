package services;

import daos.DescriptionDao;
import daos.LengthDao;
import entities.Difficulty;
import entities.GameType;
import entities.Length;
import entities.Profile;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class LengthServiceImpl extends AbstractService<Length> {

    public LengthServiceImpl() {
        super();
    }
    public LengthServiceImpl(EntityManager em) {
        super(em);
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
    public boolean deleteItem(Length entity) throws Exception {
        return false;
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
    public boolean deleteItem(Length length, List<Profile> allProfileList) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        if (!allProfileList.isEmpty()) {
            for (Profile profile: allProfileList) {
                if (profile.getLength() != null && length.getCode().equals(profile.getLength().getCode())) {
                    profile.setLength(null);
                    profileService.updateItem(profile);
                }
            }
        }
        boolean removed = new LengthDao(em).remove(length);
        new DescriptionDao(em).remove(length.getDescription());
        return removed;
    }
    @Override
    public boolean deleteAllItems(List<Length> allItemList, List<Profile> allProfileList) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        for (Profile profile: allProfileList) {
            profile.setLength(null);
            profileService.updateItem(profile);
        }
        for (Length length: allItemList) {
            new LengthDao(em).remove(length);
            new DescriptionDao(em).remove(length.getDescription());
        }
        return true;
    }
}
