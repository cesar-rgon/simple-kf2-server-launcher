package stories.updateprofilesetlanguage;

import entities.Difficulty;
import entities.Language;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.DifficultyServiceImpl;
import services.LanguageServiceImpl;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetLanguageFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetLanguageModelContext, EmptyFacadeResult>
        implements UpdateProfileSetLanguageFacade {

    public UpdateProfileSetLanguageFacadeImpl(UpdateProfileSetLanguageModelContext updateProfileSetLanguageModelContext) {
        super(updateProfileSetLanguageModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions() throws Exception {
        return false;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetLanguageModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        LanguageServiceImpl languageService = new LanguageServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating language in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        Optional<Language> languageOpt = languageService.findByCode(facadeModelContext.getLanguageCode());
        if (!languageOpt.isPresent()) {
            throw new RuntimeException("Error updating language in Profile. The language can not be found [language name: " + facadeModelContext.getLanguageCode() + "]");
        }

        profile.setLanguage(languageOpt.get());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
