package stories.exportprofilestofile;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import old.profilesedition.OldProfilesEditionFacadeImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExportProfilesToFileFacadeImpl
        extends AbstractTransactionalFacade<ExportProfilesToFileModelContext, EmptyFacadeResult>
        implements ExportProfilesToFileFacade {

    private static final Logger logger = LogManager.getLogger(ExportProfilesToFileFacadeImpl.class);

    public ExportProfilesToFileFacadeImpl(ExportProfilesToFileModelContext exportProfilesToFileModelContext) {
        super(exportProfilesToFileModelContext, EmptyFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(ExportProfilesToFileModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(ExportProfilesToFileModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        List<Profile> profilesToExport = facadeModelContext.getProfilesToExportDto().stream().map(dto -> {
            try {
                Optional<Profile> profileOpt = profileService.findProfileByCode(dto.getProfileName());
                if (profileOpt.isPresent()) {
                    return profileOpt.get();
                }
            } catch (Exception e) {
                logger.error("Error in operation of export profiles to file", e);
            }
            return null;
        }).collect(Collectors.toList());
        profileService.exportProfilesToFile(profilesToExport, facadeModelContext.getFile());

        return new EmptyFacadeResult();
    }
}
