package stories.listvaluesmaincontent;

import dtos.ProfileDto;
import dtos.SelectDto;
import dtos.factories.LanguageDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.Language;
import entities.Profile;
import framework.AbstractFacade;
import framework.EmptyModelContext;
import jakarta.persistence.EntityManager;
import javafx.collections.ObservableList;
import services.LanguageServiceImpl;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.List;

public class ListValuesMainContentFacadeImpl extends AbstractFacade<EmptyModelContext, ListValuesMainContentFacadeResult> implements ListValuesMainContentFacade {

    public ListValuesMainContentFacadeImpl() {
        super(new EmptyModelContext(), ListValuesMainContentFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    public ListValuesMainContentFacadeResult internalExecute(EmptyModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory(em);
        LanguageServiceImpl languageService = new LanguageServiceImpl(em);
        LanguageDtoFactory languageDtoFactory = new LanguageDtoFactory();

        List<Profile> profileList = profileService.listAllProfiles();
        ObservableList<ProfileDto> profileDtoList = profileDtoFactory.newDtos(profileList);

        List<Language> languageList = languageService.listAll();
        ObservableList<SelectDto> languageDtoList = languageDtoFactory.newDtos(languageList);

        return new ListValuesMainContentFacadeResult(
                profileDtoList,
                languageDtoList
        );
    }
}
