package services;

import daos.DescriptionDao;
import daos.DifficultyDao;
import entities.AbstractEntity;
import entities.AbstractExtendedEntity;
import entities.Difficulty;
import entities.Profile;
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
    public boolean deleteItem(Difficulty entity) throws Exception {
        return false;
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
    public boolean deleteItem(Difficulty difficulty, List<Profile> allProfileList) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        if (!allProfileList.isEmpty()) {
            for (Profile profile: allProfileList) {
                if (profile.getDifficulty() != null && difficulty.getCode().equals(profile.getDifficulty().getCode())) {
                    profile.setDifficulty(null);
                    profileService.updateItem(profile);
                }
            }
        }
        boolean removed = new DifficultyDao(em).remove(difficulty);
        new DescriptionDao(em).remove(difficulty.getDescription());
        return removed;
    }

    @Override
    public boolean deleteAllItems(List<Difficulty> allItemList, List<Profile> allProfileList) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        for (Profile profile: allProfileList) {
            profile.setDifficulty(null);
            profileService.updateItem(profile);
        }
        for (Difficulty difficulty: allItemList) {
            new DifficultyDao(em).remove(difficulty);
            new DescriptionDao(em).remove(difficulty.getDescription());
        }
        return true;
    }
}
