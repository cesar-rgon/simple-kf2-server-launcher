package services;

import daos.DescriptionDao;
import daos.MaxPlayersDao;
import entities.Difficulty;
import entities.Length;
import entities.MaxPlayers;
import entities.Profile;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class MaxPlayersServiceImpl extends AbstractService<MaxPlayers> {

    public MaxPlayersServiceImpl() {
        super();
    }
    public MaxPlayersServiceImpl(EntityManager em) {
        super(em);
    }


    @Override
    public MaxPlayers createItem(MaxPlayers maxPlayers) throws Exception {
        new DescriptionDao(em).insert(maxPlayers.getDescription());
        return new MaxPlayersDao(em).insert(maxPlayers);
    }

    @Override
    public boolean updateItem(MaxPlayers maxPlayers) throws Exception {
        new DescriptionDao(em).update(maxPlayers.getDescription());
        return new MaxPlayersDao(em).update(maxPlayers);
    }

    @Override
    public boolean deleteItem(MaxPlayers entity) throws Exception {
        return false;
    }

    @Override
    public List<MaxPlayers> listAll() throws Exception {
        return new MaxPlayersDao(em).listAll();
    }

    @Override
    public Optional<MaxPlayers> findByCode(String code) throws Exception {
        return new MaxPlayersDao(em).findByCode(code);
    }

    @Override
    public boolean deleteItem(MaxPlayers maxPlayers, List<Profile> allProfileList) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        if (!allProfileList.isEmpty()) {
            for (Profile profile: allProfileList) {
                if (profile.getMaxPlayers() != null && maxPlayers.getCode().equals(profile.getMaxPlayers().getCode())) {
                    profile.setMaxPlayers(null);
                    profileService.updateItem(profile);
                }
            }
        }
        boolean removed = new MaxPlayersDao(em).remove(maxPlayers);
        new DescriptionDao(em).remove(maxPlayers.getDescription());
        return removed;
    }

    @Override
    public boolean deleteAllItems(List<MaxPlayers> allItemList, List<Profile> allProfileList) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        for (Profile profile: allProfileList) {
            profile.setMaxPlayers(null);
            profileService.updateItem(profile);
        }
        for (MaxPlayers maxPlayers: allItemList) {
            new MaxPlayersDao(em).remove(maxPlayers);
            new DescriptionDao(em).remove(maxPlayers.getDescription());
        }
        return true;
    }
}
