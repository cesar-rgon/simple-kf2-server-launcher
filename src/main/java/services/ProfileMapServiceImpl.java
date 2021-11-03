package services;

import daos.OfficialMapDao;
import daos.ProfileDao;
import daos.ProfileMapDao;
import entities.AbstractMap;
import entities.OfficialMap;
import entities.Profile;
import entities.ProfileMap;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProfileMapServiceImpl implements ProfileMapService {

    @Override
    public ProfileMap createItem(ProfileMap entity) throws Exception {
        return ProfileMapDao.getInstance().insert(entity);
    }

    @Override
    public boolean deleteItem(ProfileMap entity) throws Exception {
        return ProfileMapDao.getInstance().remove(entity);
    }

    @Override
    public Optional<ProfileMap> findProfileMapByNames(String profileName, String mapName) throws SQLException {
        Optional<ProfileMap> profileMapOpt = ProfileMapDao.getInstance().findByProfileNameMapName(profileName, mapName);
        List<Integer> idsMapasOficiales = OfficialMapDao.getInstance().listAll().stream().map(OfficialMap::getId).collect(Collectors.toList());
        if (profileMapOpt.isPresent()) {
            if (idsMapasOficiales.contains(profileMapOpt.get().getMap().getId())) {
                profileMapOpt.get().getMap().setOfficial(true);
            } else {
                profileMapOpt.get().getMap().setOfficial(false);
            }
        }
        return profileMapOpt;
    }

    @Override
    public List<ProfileMap> listProfileMaps(Profile profile) throws SQLException {
        List<ProfileMap> profileMapList = ProfileMapDao.getInstance().listProfileMaps(profile);
        List<Integer> idsMapasOficiales = OfficialMapDao.getInstance().listAll().stream().map(OfficialMap::getId).collect(Collectors.toList());
        if (profileMapList != null && !profileMapList.isEmpty()) {

            profileMapList.forEach(pm -> {
                if (idsMapasOficiales.contains(pm.getMap().getId())) {
                    pm.getMap().setOfficial(true);
                } else {
                    pm.getMap().setOfficial(false);
                }
            });
        }
        return profileMapList;
    }
}
