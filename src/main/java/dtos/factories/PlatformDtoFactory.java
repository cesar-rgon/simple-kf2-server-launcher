package dtos.factories;

import dtos.PlatformDto;
import entities.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class PlatformDtoFactory extends AbstractDtoFactory<Platform, PlatformDto> {

    @Override
    public PlatformDto newDto(Platform platform) {
        try {
            return new PlatformDto(
                    platform.getCode(),
                    platform.getDescription(),
                    platform.getLogoPath(),
                    platform.getSmallLogoPath(),
                    platform.getInstallationFolder()
            );
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public ObservableList<PlatformDto> newDtos(List<Platform> platforms) {
        List<PlatformDto> dtoList = platforms.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }
}
