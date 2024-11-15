package stories.entitydescriptions;

import entities.Difficulty;
import entities.GameType;
import entities.Length;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import pojos.enums.EnumLanguage;
import services.DifficultyServiceImpl;
import services.GameTypeServiceImpl;
import services.LengthServiceImpl;

import java.util.Optional;

public class EntityDescriptionsFacadeImpl
        extends AbstractTransactionalFacade<EntityDescriptionsModelContext, EntityDescriptionsFacadeResult>
        implements EntityDescriptionsFacade {


    public EntityDescriptionsFacadeImpl(EntityDescriptionsModelContext entityDescriptionsModelContext) {
        super(entityDescriptionsModelContext, EntityDescriptionsFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(EntityDescriptionsModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EntityDescriptionsFacadeResult internalExecute(EntityDescriptionsModelContext facadeModelContext, EntityManager em) throws Exception {
        GameTypeServiceImpl gameTypeService = new GameTypeServiceImpl(em);
        DifficultyServiceImpl difficultyService = new DifficultyServiceImpl(em);
        LengthServiceImpl lengthService = new LengthServiceImpl(em);

        Optional<GameType> gameTypeOptional = gameTypeService.findByCode(facadeModelContext.getGameTypeCode());
        String gameTypeDescription = gameTypeOptional.isPresent() ?
                EnumLanguage.en.name().equals(facadeModelContext.getLanguageCode()) ? gameTypeOptional.get().getDescription().getEnglishValue():
                EnumLanguage.es.name().equals(facadeModelContext.getLanguageCode()) ? gameTypeOptional.get().getDescription().getSpanishValue():
                EnumLanguage.fr.name().equals(facadeModelContext.getLanguageCode()) ? gameTypeOptional.get().getDescription().getFrenchValue():
                EnumLanguage.ru.name().equals(facadeModelContext.getLanguageCode()) ? gameTypeOptional.get().getDescription().getRussianValue():
                StringUtils.EMPTY:
                StringUtils.EMPTY;

        Optional<Difficulty> difficultyOptional = difficultyService.findByCode(facadeModelContext.getDifficultyCode());
        String difficultyDescription = difficultyOptional.isPresent() ?
                EnumLanguage.en.name().equals(facadeModelContext.getLanguageCode()) ? difficultyOptional.get().getDescription().getEnglishValue():
                EnumLanguage.es.name().equals(facadeModelContext.getLanguageCode()) ? difficultyOptional.get().getDescription().getSpanishValue():
                EnumLanguage.fr.name().equals(facadeModelContext.getLanguageCode()) ? difficultyOptional.get().getDescription().getFrenchValue():
                EnumLanguage.ru.name().equals(facadeModelContext.getLanguageCode()) ? difficultyOptional.get().getDescription().getRussianValue():
                StringUtils.EMPTY:
                StringUtils.EMPTY;

        Optional<Length> lengthOptional = lengthService.findByCode(facadeModelContext.getLengthCode());
        String lengthDescription = lengthOptional.isPresent() ?
                EnumLanguage.en.name().equals(facadeModelContext.getLanguageCode()) ? lengthOptional.get().getDescription().getEnglishValue():
                EnumLanguage.es.name().equals(facadeModelContext.getLanguageCode()) ? lengthOptional.get().getDescription().getSpanishValue():
                EnumLanguage.fr.name().equals(facadeModelContext.getLanguageCode()) ? lengthOptional.get().getDescription().getFrenchValue():
                EnumLanguage.ru.name().equals(facadeModelContext.getLanguageCode()) ? lengthOptional.get().getDescription().getRussianValue():
                StringUtils.EMPTY:
                StringUtils.EMPTY;


        return new EntityDescriptionsFacadeResult(
                gameTypeDescription,
                difficultyDescription,
                lengthDescription
        );
    }
}
