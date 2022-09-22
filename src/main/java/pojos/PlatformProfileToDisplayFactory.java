package pojos;

import dtos.factories.DifficultyDtoFactory;
import dtos.factories.GameTypeDtoFactory;
import dtos.factories.LengthDtoFactory;
import entities.PlatformProfileMap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PlatformProfileToDisplayFactory {

    private final GameTypeDtoFactory gameTypeDtoFactory;
    private final DifficultyDtoFactory difficultyDtoFactory;
    private final LengthDtoFactory lengthDtoFactory;

    public PlatformProfileToDisplayFactory() {
        super();
        this.gameTypeDtoFactory = new GameTypeDtoFactory();
        this.difficultyDtoFactory = new DifficultyDtoFactory();
        this.lengthDtoFactory = new LengthDtoFactory();
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public PlatformProfileToDisplay newOne(PlatformProfileMap platformProfileMap) {
        return new PlatformProfileToDisplay(
                platformProfileMap.getProfile().getId(),
                platformProfileMap.getProfile().getCode(),
                platformProfileMap.getProfile().getGametype() != null ? gameTypeDtoFactory.newDto(platformProfileMap.getProfile().getGametype()).getValue(): "",
                platformProfileMap.getProfile().getMap() != null ? platformProfileMap.getProfile().getMap().getCode(): "",
                platformProfileMap.getProfile().getDifficulty() != null ? difficultyDtoFactory.newDto(platformProfileMap.getProfile().getDifficulty()).getValue(): "",
                platformProfileMap.getProfile().getLength() != null ? lengthDtoFactory.newDto(platformProfileMap.getProfile().getLength()).getValue(): ""
        );
    }

    public List<PlatformProfileToDisplay> newOnes(List<PlatformProfileMap> platformProfileMapList) {
        return platformProfileMapList.stream().
                filter( distinctByKey(ppm -> ppm.getProfile())).
                map(this::newOne).
                collect(Collectors.toList());

    }
}
