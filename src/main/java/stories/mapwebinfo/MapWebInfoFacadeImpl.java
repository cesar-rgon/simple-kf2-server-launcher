package stories.mapwebinfo;

import daos.MapDao;
import entities.Map;

import java.sql.SQLException;
import java.util.Optional;

public class MapWebInfoFacadeImpl implements MapWebInfoFacade {

    public MapWebInfoFacadeImpl() {
        super();
    }

    @Override
    public boolean isMapInDataBase(Long idWorkShop) {
        try {
            Optional<Map> mapOpt = MapDao.getInstance().findByIdWorkShop(idWorkShop);
            return mapOpt.isPresent();
        } catch (SQLException e) {
            return false;
        }
    }
}
