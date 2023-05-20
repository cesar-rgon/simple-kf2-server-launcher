package services;

import daos.DescriptionDao;
import daos.GameTypeDao;
import daos.ProfileDao;
import entities.Difficulty;
import entities.GameType;
import entities.Profile;
import jakarta.persistence.EntityManager;
import pojos.session.Session;

import java.util.List;
import java.util.Optional;

public class GameTypeServiceImpl extends AbstractService<GameType> {

    public GameTypeServiceImpl() {
        super();
    }

    public GameTypeServiceImpl(EntityManager em) {
        super(em);
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
    public boolean deleteItem(GameType entity) throws Exception {
        return false;
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
    public boolean deleteItem(GameType gameType, List<Profile> allProfileList) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        if (!allProfileList.isEmpty()) {
            for (Profile profile: allProfileList) {
                if (profile.getGametype() != null && gameType.getCode().equals(profile.getGametype().getCode())) {
                    profile.setGametype(null);
                    profileService.updateItem(profile);
                }
            }
        }
        boolean removed = new GameTypeDao(em).remove(gameType);
        new DescriptionDao(em).remove(gameType.getDescription());
        return removed;
    }

    @Override
    public boolean deleteAllItems(List<GameType> allItemList, List<Profile> allProfileList) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        for (Profile profile: allProfileList) {
            profile.setGametype(null);
            profileService.updateItem(profile);
        }
        for (GameType gameType: allItemList) {
            new GameTypeDao(em).remove(gameType);
            new DescriptionDao(em).remove(gameType.getDescription());
        }
        return true;
    }
}
