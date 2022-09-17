package dtos.factories;

import dtos.PlatformDto;
import dtos.EpicPlatformDto;
import dtos.SteamPlatformDto;
import entities.AbstractPlatform;
import entities.SteamPlatform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class PlatformDtoFactory {

    public PlatformDto newSteamDto(AbstractPlatform platform) {
        try {
            SteamPlatform steamPlatform = (SteamPlatform) platform;
            return new SteamPlatformDto(
                    platform.getCode(),
                    platform.getDescription(),
                    platform.getLogoPath(),
                    platform.getSmallLogoPath(),
                    platform.getInstallationFolder(),
                    steamPlatform.isValidateFiles(),
                    steamPlatform.isBeta(),
                    steamPlatform.getBetaBrunch()
            );
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), e);
            return null;
        }
    }

    public PlatformDto newEpicDto(AbstractPlatform platform) {
        try {
            return new EpicPlatformDto(
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

    public PlatformDto newDto(AbstractPlatform platform) {
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

    public ObservableList<PlatformDto> newSteamDtos(List<AbstractPlatform> platforms) {
        List<PlatformDto> dtoList = platforms.stream().map(this::newSteamDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

    public ObservableList<PlatformDto> newEpicDtos(List<AbstractPlatform> platforms) {
        List<PlatformDto> dtoList = platforms.stream().map(this::newEpicDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

    public ObservableList<PlatformDto> newDtos(List<AbstractPlatform> platforms) {
        List<PlatformDto> dtoList = platforms.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }
}
