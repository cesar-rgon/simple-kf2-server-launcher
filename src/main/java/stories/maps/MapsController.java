package stories.maps;

import dtos.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.*;
import pojos.enums.*;
import pojos.session.Session;
import start.MainApplication;
import stories.addcustommapstoprofile.AddCustomMapsToProfileFacadeResult;
import stories.listplatformprofilemap.ListPlatformProfileMapFacadeResult;
import stories.mapsinitialize.MapsInitializeFacadeResult;
import stories.mapsinitialize.MapsInitializeModelContext;
import stories.prepareimportmapsfromserver.PrepareImportMapsFromServerFacadeResult;
import utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MapsController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MapsController.class);

    private final MapsManagerFacade facade;

    protected String languageCode;
    private List<PlatformProfileMapDto> steamPlatformProfileMapDtoList;
    private List<PlatformProfileMapDto> epicPlatformProfileMapDtoList;
    private EnumCardOrientation cardOrientation;
    private EnumCardOrderType orderType;

    @FXML private Slider mapsSlider;
    @FXML private FlowPane steamOfficialMapsFlowPane;
    @FXML private FlowPane steamCustomMapsFlowPane;
    @FXML private FlowPane epicOfficialMapsFlowPane;
    @FXML private FlowPane epicCustomMapsFlowPane;
    @FXML private TextField searchMaps;
    @FXML private MenuItem addNewMaps;
    @FXML private MenuItem searchInWorkShop;
    @FXML private MenuItem removeMaps;
    @FXML private MenuItem importMapsFromServer;
    @FXML private TabPane mapsTabPane;
    @FXML private Tab steamOfficialMapsTab;
    @FXML private Tab steamCustomMapsTab;
    @FXML private Tab epicOfficialMapsTab;
    @FXML private Tab epicCustomMapsTab;
    @FXML private Label sliderLabel;
    @FXML private ImageView profileImg;
    @FXML private ImageView searchImg;
    @FXML private ComboBox<ProfileDto> profileSelect;
    @FXML private Label profileLabel;
    @FXML private Label actionsLabel;
    @FXML private Menu mapsCycle;
    @FXML private Menu orderMaps;
    @FXML private MenuItem orderMapsByAlias;
    @FXML private MenuItem orderMapsByName;
    @FXML private MenuItem orderMapsByReleaseDate;
    @FXML private MenuItem orderMapsByImportedDate;
    @FXML private MenuItem orderMapsByDownload;
    @FXML private MenuItem orderMapsByMapsCycle;
    @FXML private MenuItem editMaps;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private MenuItem addToMapsCycle;
    @FXML private MenuItem removeFromMapsCycle;
    @FXML private MenuItem downloadMaps;
    @FXML private Label viewLabel;
    @FXML private MenuItem cardDown;
    @FXML private MenuItem cardRight;
    @FXML private MenuItem cardUp;
    @FXML private MenuItem cardLeft;
    @FXML private MenuItem detailedCard;

    public MapsController() {
        super();
        MapsInitializeModelContext mapsInitializeModelContext = new MapsInitializeModelContext(
                Session.getInstance().getActualProfileName()
        );
        facade = new MapsManagerFacadeImpl(mapsInitializeModelContext);
        steamPlatformProfileMapDtoList = new ArrayList<PlatformProfileMapDto>();
        epicPlatformProfileMapDtoList = new ArrayList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        progressIndicator.setVisible(true);
        try {
            String lastUsedViewName = facade.findPropertyValue("properties/config.properties", "prop.config.lastUsedView");
            cardOrientation = EnumCardOrientation.getByName(lastUsedViewName);
            setLabelText();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        Task<MapsInitializeFacadeResult> task = new Task<MapsInitializeFacadeResult>() {
            @Override
            protected MapsInitializeFacadeResult call() throws Exception {
                MapsInitializeFacadeResult result = facade.execute();

                if (!result.getAllProfileDtoList().isEmpty()) {
                    ProfileDto selectedProfile = Session.getInstance().getMapsProfile() != null?
                            Session.getInstance().getMapsProfile():
                            result.getActualProfileDto() != null?
                                    result.getActualProfileDto():
                                    result.getAllProfileDtoList().get(0);

                    ListPlatformProfileMapFacadeResult listPlatformProfileMapFacadeResult = facade.getPlatformProfileMapList(selectedProfile.getName());
                    steamPlatformProfileMapDtoList = listPlatformProfileMapFacadeResult.getSteamPlatformProfileMapDtoList();
                    epicPlatformProfileMapDtoList = listPlatformProfileMapFacadeResult.getEpicPlatformProfileMapDtoList();
                }

                return result;
            }
        };

        task.setOnSucceeded(wse -> {
            profileSelect.setItems(task.getValue().getAllProfileDtoList());
            if (!task.getValue().getAllProfileDtoList().isEmpty()) {
                profileSelect.setValue(Session.getInstance().getMapsProfile() != null?
                        Session.getInstance().getMapsProfile():
                        task.getValue().getActualProfileDto() != null?
                                task.getValue().getActualProfileDto():
                                profileSelect.getItems().get(0));
            } else {
                profileSelect.setValue(null);
                steamPlatformProfileMapDtoList = null;
                epicPlatformProfileMapDtoList = null;
            }

            steamOfficialMapsTab.setDisable(!task.getValue().isSteamHasCorrectInstallationFolder());
            steamCustomMapsTab.setDisable(!task.getValue().isSteamHasCorrectInstallationFolder());
            epicOfficialMapsTab.setDisable(!task.getValue().isEpicHasCorrectInstallationFolder());
            epicCustomMapsTab.setDisable(!task.getValue().isEpicHasCorrectInstallationFolder());

            Session.getInstance().setMapsProfile(profileSelect.getValue());

            SingleSelectionModel<Tab> selectionModel = mapsTabPane.getSelectionModel();
            switch (Session.getInstance().getSelectedMapTab()) {
                case STEAM_OFFICIAL_MAPS_TAB:
                    selectionModel.select(steamOfficialMapsTab);
                    break;

                case STEAM_CUSTOM_MAPS_TAB:
                    selectionModel.select(steamCustomMapsTab);
                    break;

                case EPIC_OFFICIAL_MAPS_TAB:
                    selectionModel.select(epicOfficialMapsTab);
                    break;

                case EPIC_CUSTOM_MAPS_TAB:
                    selectionModel.select(epicCustomMapsTab);
                    break;
            }

            if (!profileSelect.getItems().isEmpty()) {
                orderMapsByNameOnAction();
            }

            mapsSliderOnMouseClicked();
            progressIndicator.setVisible(false);
        });

        task.setOnFailed(wse -> {
            progressIndicator.setVisible(false);
        });

        Thread thread = new Thread(task);
        thread.start();

        MainApplication.getPrimaryStage().widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                mapsSliderOnMouseClicked();
            }
        });



        mapsTabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {

                        if (newTab.equals(steamOfficialMapsTab)) {
                            Session.getInstance().setSelectedMapTab(EnumMapsTab.STEAM_OFFICIAL_MAPS_TAB);
                        }
                        if (newTab.equals(steamCustomMapsTab)) {
                            Session.getInstance().setSelectedMapTab(EnumMapsTab.STEAM_CUSTOM_MAPS_TAB);
                        }
                        if (newTab.equals(epicOfficialMapsTab)) {
                            Session.getInstance().setSelectedMapTab(EnumMapsTab.EPIC_OFFICIAL_MAPS_TAB);
                        }
                        if (newTab.equals(epicCustomMapsTab)) {
                            Session.getInstance().setSelectedMapTab(EnumMapsTab.EPIC_CUSTOM_MAPS_TAB);
                        }
                    }
                }
        );

        steamOfficialMapsFlowPane.getChildren().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(Change<? extends Node> change) {
                checkIfHasMaps();
            }
        });
        steamCustomMapsFlowPane.getChildren().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(Change<? extends Node> change) {
                checkIfHasMaps();
            }
        });
        epicOfficialMapsFlowPane.getChildren().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(Change<? extends Node> change) {
                checkIfHasMaps();
            }
        });
        epicCustomMapsFlowPane.getChildren().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(Change<? extends Node> change) {
                checkIfHasMaps();
            }
        });

    }

    private void checkIfHasMaps() {
        try {
            if (Session.getInstance().isWizardMode()) {
                FXMLLoader wizardStepTemplate = MainApplication.getTemplate();
                Button nextStep = (Button) wizardStepTemplate.getNamespace().get("nextStep");
                if (steamOfficialMapsFlowPane.getChildren().size() == 0 && steamCustomMapsFlowPane.getChildren().size() == 0 &&
                        epicOfficialMapsFlowPane.getChildren().size() == 0 && epicCustomMapsFlowPane.getChildren().size() == 0) {
                    nextStep.setDisable(true);
                } else {
                    nextStep.setDisable(false);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    private void setLabelText() throws Exception {
        Double tooltipDuration = Double.parseDouble(
                facade.findPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
        );
        if (Session.getInstance().isWizardMode()) {
            languageCode = Session.getInstance().getWizardLanguage().name();
        } else {
            languageCode = facade.findPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        }

        String profileLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.profile");
        profileLabel.setText(profileLabelText);
        Utils.loadTooltip(languageCode, "prop.tooltip.profile", profileImg, profileLabel, profileSelect);

        String actionsLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.actions");
        actionsLabel.setText(actionsLabelText);

        Tooltip searchTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.searchMaps"));
        searchTooltip.setShowDuration(Duration.seconds(tooltipDuration));
        searchMaps.setTooltip(searchTooltip);
        Tooltip.install(searchImg, searchTooltip);

        String addNewMapsText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.addMaps");
        addNewMaps.setText(addNewMapsText);

        String searchInWorkShopText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.searchInWorkShop");
        searchInWorkShop.setText(searchInWorkShopText);

        String removeMapsText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.removeMaps");
        removeMaps.setText(removeMapsText);

        String importMapsFromServerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.importMaps");
        importMapsFromServer.setText(importMapsFromServerText);

        String addToMapsCycleText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.addToMapsCycle");
        addToMapsCycle.setText(addToMapsCycleText);

        String removeFromMapsCycleText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.removeFromMapsCycle");
        removeFromMapsCycle.setText(removeFromMapsCycleText);

        String mapsCycleText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.mapsCycle");
        mapsCycle.setText(mapsCycleText);

        String orderMapsText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.orderMaps");
        orderMaps.setText(orderMapsText);

        String orderMapsByAliasText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.orderMapsByAlias");
        orderMapsByAlias.setText(orderMapsByAliasText);

        String orderMapsByNameText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.orderMapsByName");
        orderMapsByName.setText(orderMapsByNameText);

        String orderMapsByReleaseDateText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.orderMapsByReleaseDate");
        orderMapsByReleaseDate.setText(orderMapsByReleaseDateText);

        String orderMapsByImportedDateText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.orderMapsByImportedDate");
        orderMapsByImportedDate.setText(orderMapsByImportedDateText);

        String orderMapsByDownloadText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.orderMapsByDownload");
        orderMapsByDownload.setText(orderMapsByDownloadText);

        String orderMapsByMapsCycleText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.orderMapsByMapsCycle");
        orderMapsByMapsCycle.setText(orderMapsByMapsCycleText);

        String editMapsText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.editMaps");
        editMaps.setText(editMapsText);

        String downloadMapsText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.downloadMaps");
        downloadMaps.setText(downloadMapsText);

        String viewLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.views.menu.label");
        viewLabel.setText(viewLabelText);

        String cardDownText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.views.cardsDown");
        cardDown.setText(cardDownText);

        String cardRightText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.views.cardsRight");
        cardRight.setText(cardRightText);

        String cardUpText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.views.cardsUp");
        cardUp.setText(cardUpText);

        String cardLeftText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.views.cardsLeft");
        cardLeft.setText(cardLeftText);

        String detailedCardText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.views.detailedCards");
        detailedCard.setText(detailedCardText);

        String sliderLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.slider");
        sliderLabel.setText(sliderLabelText);
        Tooltip sliderTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.slide"));
        sliderTooltip.setShowDuration(Duration.seconds(tooltipDuration));
        sliderLabel.setTooltip(sliderTooltip);
        if (!EnumCardOrientation.DETAILED.equals(cardOrientation)) {
            double sliderColumns = Double.parseDouble(facade.findPropertyValue("properties/config.properties", "prop.config.mapSliderValue"));
            mapsSlider.setValue(sliderColumns);
        } else {
            double sliderColumns = Double.parseDouble(facade.findPropertyValue("properties/config.properties", "prop.config.mapSliderDetailedCardValue"));
            mapsSlider.setValue(sliderColumns);
        }
        mapsSlider.setTooltip(sliderTooltip);

        String officialMapsTabText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.officialMaps");
        String customMapsTabText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.customMaps");

        steamOfficialMapsTab.setGraphic(createTabTitle(EnumPlatform.STEAM, true, officialMapsTabText, steamOfficialMapsFlowPane.getChildren().size()));
        steamOfficialMapsTab.setText("");
        steamCustomMapsTab.setGraphic(createTabTitle(EnumPlatform.STEAM, false, customMapsTabText, steamCustomMapsFlowPane.getChildren().size()));
        steamCustomMapsTab.setText("");

        epicOfficialMapsTab.setGraphic(createTabTitle(EnumPlatform.EPIC, true, officialMapsTabText, epicOfficialMapsFlowPane.getChildren().size()));
        epicOfficialMapsTab.setText("");
        epicCustomMapsTab.setGraphic(createTabTitle(EnumPlatform.EPIC, false, customMapsTabText, epicCustomMapsFlowPane.getChildren().size()));
        epicCustomMapsTab.setText("");
    }

    private HBox createTabTitle(EnumPlatform enumPlatform, boolean officialMaps, String text, int numberOfMaps) {

        CheckBox tabCheckbox = new CheckBox();
        tabCheckbox.setId("selectAll");
        tabCheckbox.setPrefWidth(20);
        tabCheckbox.setOnAction((ActionEvent event) -> {
            ObservableList<Node> nodes = null;

            if (EnumPlatform.STEAM.equals(enumPlatform)) {
                if (officialMaps) {
                    nodes = steamOfficialMapsFlowPane.getChildren();
                } else {
                    nodes = steamCustomMapsFlowPane.getChildren();
                }
            }
            if (EnumPlatform.EPIC.equals(enumPlatform)) {
                if (officialMaps) {
                    nodes = epicOfficialMapsFlowPane.getChildren();
                } else {
                    nodes = epicCustomMapsFlowPane.getChildren();
                }
            }

            for (Node node: nodes) {
                CardNode cardNode = new CardNode(cardOrientation, node);

                cardNode.getCheckbox().setSelected(tabCheckbox.isSelected());
                if (cardNode.getCheckbox().isSelected()) {
                    cardNode.getCheckbox().setOpacity(1);
                } else {
                    cardNode.getCheckbox().setOpacity(0.5);
                }
            }
        });

        Circle circle = new Circle(0,0, 10, Color.LIGHTBLUE);
        Text numberOfMapsText = new Text(String.valueOf(numberOfMaps));
        numberOfMapsText.setFont(Font.loadFont(getClass().getClassLoader().getResource("fonts/Nunito-Bold.ttf").toExternalForm(), 11));
        numberOfMapsText.setFill(Color.DARKBLUE);
        StackPane stackPane = new StackPane(circle, numberOfMapsText);
        stackPane.setPrefWidth(30);

        Text title = new Text(enumPlatform.getDescripcion() + ". " + text);
        title.setFill(Color.WHITE);

        HBox contentPane = new HBox();
        contentPane.setAlignment(Pos.CENTER);
        contentPane.getChildren().addAll(tabCheckbox, stackPane, title);

        return contentPane;
    }

    private String getUrlPhoto(PlatformProfileMapDto platformProfileMapDto) {
        String urlPhoto = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(platformProfileMapDto.getUrlPhoto())) {
            if (platformProfileMapDto.getUrlPhoto().startsWith("http") || platformProfileMapDto.getUrlPhoto().startsWith("file:")) {
                urlPhoto = platformProfileMapDto.getUrlPhoto();
            } else if (platformProfileMapDto.getUrlPhoto().startsWith("/KFGame")) {
                urlPhoto = "file:" + platformProfileMapDto.getPlatformDto().getInstallationFolder() + platformProfileMapDto.getUrlPhoto();
            } else {
                urlPhoto = "file:" + platformProfileMapDto.getUrlPhoto();
            }
        }
        return urlPhoto;
    }

    private ImageView createMapPreview(PlatformProfileMapDto platformProfileMapDto, String urlPhoto, Double widthNodeByNumberOfColums) {

        ImageView mapPreview = new ImageView();
        try {
            Image image = null;
            if (StringUtils.isNotBlank(urlPhoto)) {
                image = new Image(urlPhoto);
            } else {
                File file = new File(System.getProperty("user.dir") + "/external-images/no-photo.png");
                InputStream inputStream;
                if (file.exists()) {
                    inputStream = new FileInputStream(file.getAbsolutePath());
                } else {
                    inputStream = getClass().getClassLoader().getResourceAsStream("external-images/no-photo.png");
                }
                image = new Image(inputStream);
            }
            mapPreview = new ImageView(image);

            if (platformProfileMapDto.getMapDto().isOfficial()) {
                mapPreview.setPreserveRatio(true);
                mapPreview.setFitWidth(widthNodeByNumberOfColums);
            } else {
                mapPreview.setPreserveRatio(false);
                mapPreview.setFitWidth(widthNodeByNumberOfColums);
                mapPreview.setFitHeight(mapPreview.getFitWidth());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return mapPreview;
    }


    private CheckBox createCheckBox() {
        CheckBox checkbox = new CheckBox();
        checkbox.setOpacity(0.5);
        checkbox.setOnAction(e -> {
            if (checkbox.isSelected()) {
                checkbox.setOpacity(1);
            } else {
                checkbox.setOpacity(0.5);
            }
        });
        checkbox.setPadding(new Insets(0,0,10,0));

        return checkbox;
    }

    private Label createAliasLabel(PlatformProfileMapDto platformProfileMapDto) {

        Label aliasLabel = new Label(platformProfileMapDto.getAlias());
        aliasLabel.setPadding(new Insets(0,0,5,0));

        return aliasLabel;
    }

    private Label createMapNameLabel(PlatformProfileMapDto platformProfileMapDto) {
        String mapNameText;
        try {
            mapNameText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.name");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mapNameText = "Name";
        }

        Label mapNameLabel = new Label(mapNameText + ": " + platformProfileMapDto.getMapDto().getKey());
        mapNameLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 11;");

        return mapNameLabel;
    }

    private Label createReleaseDateLabel(PlatformProfileMapDto platformProfileMapDto) {

        String datePattern;
        String unknownStr;
        String releaseStr;
        try {
            datePattern = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.code.datePattern");
            unknownStr = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.unknown");
            releaseStr = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.release");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            datePattern = "yyyy/MM/dd";
            unknownStr = "Unknown";
            releaseStr = "Release";
        }

        String releaseDateStr = platformProfileMapDto.getReleaseDate() != null ? platformProfileMapDto.getReleaseDate().format(DateTimeFormatter.ofPattern(datePattern)): unknownStr;
        Label releaseDateLabel = new Label(releaseStr + ": " + releaseDateStr);
        releaseDateLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 11;");

        return releaseDateLabel;
    }

    private Label createImportedDateLabel(PlatformProfileMapDto platformProfileMapDto) {

        String importedStr;
        String dateHourPattern;
        try {
            dateHourPattern = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.code.dateHourPattern");
            importedStr = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.imported");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            dateHourPattern = "yyyy/MM/dd HH:mm";
            importedStr = "Imported";
        }

        Label importedDateText = new Label(importedStr + ": " + platformProfileMapDto.getImportedDate().format(DateTimeFormatter.ofPattern(dateHourPattern)));
        importedDateText.setMaxWidth(Double.MAX_VALUE);
        importedDateText.setStyle("-fx-text-fill: gray; -fx-font-size: 11;");

        return importedDateText;
    }

    private Label createMapModLabel(CustomMapModDto customMapMod) {
        Label mapModLabel;
        String isMapModLabelText;

        if (customMapMod.isMap() == null) {
            try {
                isMapModLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.unknownType");
            } catch (Exception e) {
                isMapModLabelText = "UNKOWN TYPE";
            }
        } else {
            if (customMapMod.isMap()) {
                try {
                    isMapModLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.isMap");
                } catch (Exception e) {
                    isMapModLabelText = "MAP";
                }
            } else {
                try {
                    isMapModLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.isMod");
                } catch (Exception e) {
                    isMapModLabelText = "MOD / MUTATOR";
                }
            }
        }
        mapModLabel = new Label(isMapModLabelText);
        mapModLabel.setStyle("-fx-text-fill: #5fbb3d; -fx-font-weight: bold;");
        mapModLabel.setAlignment(Pos.CENTER);

        return mapModLabel;
    }

    private Hyperlink createClickToDownloadMapLink(PlatformProfileMapDto platformProfileMapDto, Label downloadedStateLabel, Label isInMapsCycleLabel, Pos pos) {

        String clickToDownloadMapText = "";
        try {
            clickToDownloadMapText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.clickToDownloadMap");
        } catch (Exception e) {
            clickToDownloadMapText = "Click to download it!";
        }

        Hyperlink clickToDownloadMapLink = new Hyperlink(clickToDownloadMapText);
        clickToDownloadMapLink.setStyle("-fx-text-fill: #f03830; -fx-underline: true;");
        clickToDownloadMapLink.setMaxWidth(Double.MAX_VALUE);
        clickToDownloadMapLink.setAlignment(pos);
        clickToDownloadMapLink.setPadding(new Insets(10,0,10,0));

        clickToDownloadMapLink.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                try {
                    List<String> platformNameList = new ArrayList<String>();
                    platformNameList.add(platformProfileMapDto.getPlatformDto().getKey());
                    List<String> mapNameList = new ArrayList<String>();
                    mapNameList.add(platformProfileMapDto.getMapDto().getKey());
                    progressIndicator.setVisible(true);


                    Task<Void> task = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            facade.downloadMapListFromSteamCmd(platformNameList, mapNameList);

                            ListPlatformProfileMapFacadeResult listPlatformProfileMapFacadeResult = facade.getPlatformProfileMapList(profileSelect.getValue().getName());
                            steamPlatformProfileMapDtoList = listPlatformProfileMapFacadeResult.getSteamPlatformProfileMapDtoList();
                            epicPlatformProfileMapDtoList = listPlatformProfileMapFacadeResult.getEpicPlatformProfileMapDtoList();

                            return null;
                        }
                    };
                    task.setOnSucceeded(wse -> {
                        progressIndicator.setVisible(false);
                        CustomMapModDto customMapMod = (CustomMapModDto) platformProfileMapDto.getMapDto();
                        clickToDownloadMapLink.setVisible(false);
                        downloadedStateLabel.setVisible(true);
                        isInMapsCycleLabel.setVisible(customMapMod.isMap());
                    });
                    task.setOnFailed(wse -> {
                        progressIndicator.setVisible(false);
                    });
                    Thread thread = new Thread(task);
                    thread.start();

                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    Utils.errorDialog(ex.getMessage(), ex);
                }
            }
        });

        return clickToDownloadMapLink;
    }


    private Label createDownloadedLabel(PlatformProfileMapDto platformProfileMapDto, Pos pos) {

        String downloadedLabelText = "";
        Label downloadedLabel = new Label();
        try {
            downloadedLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.downloaded");
        } catch (Exception e) {
            downloadedLabelText = "DOWNLOADED";
        }

        downloadedLabel.setText(downloadedLabelText);
        downloadedLabel.setStyle("-fx-text-fill: gold; -fx-padding: 3; -fx-border-color: gold; -fx-border-radius: 5;");
        downloadedLabel.setAlignment(pos);
        downloadedLabel.setVisible(!platformProfileMapDto.getMapDto().isOfficial() && platformProfileMapDto.isDownloaded());

        return downloadedLabel;
    }

    private Label createIsInMapsCycleLabel(PlatformProfileMapDto platformProfileMapDto, Pos pos) {

        Label isInMapsCycleLabel = new Label();
        String isInMapsCycleText;
        if (platformProfileMapDto.isInMapsCycle()) {
            try {
                isInMapsCycleText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.inMapsCycle");
            } catch (Exception e) {
                isInMapsCycleText = "IN MAPS CYCLE";
            }
            isInMapsCycleLabel.setStyle("-fx-text-fill: #ef2828; -fx-font-weight: bold; -fx-padding: 3; -fx-border-color: #ef2828; -fx-border-radius: 5;");
        } else {
            try {
                isInMapsCycleText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.notInMapsCycle");
            } catch (Exception e) {
                isInMapsCycleText = "NOT IN MAPS CYCLE";
            }
            isInMapsCycleLabel.setStyle("-fx-text-fill: grey; -fx-font-weight: bold; -fx-padding: 3; -fx-border-color: grey; -fx-border-radius: 5;");
        }
        isInMapsCycleLabel.setText(isInMapsCycleText);
        isInMapsCycleLabel.setAlignment(pos);
        isInMapsCycleLabel.setVisible(
                platformProfileMapDto.getMapDto().isOfficial() ||
                (((CustomMapModDto) platformProfileMapDto.getMapDto()).isMap() && platformProfileMapDto.isDownloaded())
        );

        return isInMapsCycleLabel;
    }

    private VBox initializeVerticalNode(PlatformProfileMapDto platformProfileMapDto) {

        VBox vBox = new VBox();
        vBox.setPrefWidth(getWidthNodeByNumberOfColums());
        vBox.getStyleClass().add("mapCard");
        vBox.setAlignment(Pos.CENTER);

        Label platformNameText = new Label(platformProfileMapDto.getPlatformDto().getKey());
        platformNameText.setVisible(false);

        Label isOfficialText = new Label(String.valueOf(platformProfileMapDto.getMapDto().isOfficial()));
        isOfficialText.setVisible(false);
        vBox.getChildren().addAll(platformNameText, isOfficialText);

        StringBuffer tooltipText = new StringBuffer();
        if (!platformProfileMapDto.getMapDto().isOfficial()) {
            tooltipText.append("id WorkShop: ").append(((CustomMapModDto) platformProfileMapDto.getMapDto()).getIdWorkShop());
        }

        String urlPhoto = getUrlPhoto(platformProfileMapDto);

        if (StringUtils.isNotBlank(urlPhoto)) {
            if (StringUtils.isNotBlank(tooltipText)) {
                tooltipText.append("\n");
            }
            String message = "";
            try {
                message = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.urlInfo");
            } catch (Exception e) {
                message = "Url info";
            }
            tooltipText.append(message).append(": ").append(platformProfileMapDto.getUrlInfo());
        }
        if (platformProfileMapDto.getMapDto().isOfficial() || platformProfileMapDto.isDownloaded()) {
            if (StringUtils.isNotBlank(tooltipText)) {
                tooltipText.append("\n");
            }
            String message = "";
            try {
                message = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.photoLocation");
            } catch (Exception e) {
                message = "Photo location";
            }
            tooltipText.append(message).append(": ").append(urlPhoto);
        }

        Double tooltipDuration = 30.0;
        try {
            tooltipDuration = Double.parseDouble(
                    facade.findPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
            );
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        if (StringUtils.isNotBlank(tooltipText)) {
            Tooltip tooltip = new Tooltip(tooltipText.toString());
            tooltip.setShowDuration(Duration.seconds(tooltipDuration));
            Tooltip.install(vBox, tooltip);
        }

        if (StringUtils.isNotBlank(platformProfileMapDto.getUrlInfo())) {
            vBox.setCursor(Cursor.HAND);
            vBox.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Session.getInstance().setPpm(platformProfileMapDto);
                    loadNewContent("/views/mapWebInfo.fxml");
                    event.consume();
                }
            });
        }

        return vBox;
    }

    private void createMapNodeVerticalCard(PlatformProfileMapDto platformProfileMapDto, VBox vBox) {

        CheckBox checkBox = createCheckBox();

        Label aliasLabel = createAliasLabel(platformProfileMapDto);
        aliasLabel.setAlignment(Pos.BOTTOM_CENTER);

        Label mapNameLabel = createMapNameLabel(platformProfileMapDto);
        mapNameLabel.setAlignment(Pos.BOTTOM_CENTER);

        Label releaseDateLabel = createReleaseDateLabel(platformProfileMapDto);
        releaseDateLabel.setAlignment(Pos.BOTTOM_CENTER);

        Label importedDateText = createImportedDateLabel(platformProfileMapDto);
        importedDateText.setAlignment(Pos.BOTTOM_CENTER);

        vBox.getChildren().addAll(aliasLabel, checkBox, mapNameLabel, releaseDateLabel, importedDateText);

        if (platformProfileMapDto.getMapDto().isOfficial()) {
            importedDateText.setPadding(new Insets(0,0,10,0));
        }
    }


    private VBox createMapNodeCardUp(PlatformProfileMapDto platformProfileMapDto) {
        VBox vBox = initializeVerticalNode(platformProfileMapDto);
        createMapNodeVerticalCard(platformProfileMapDto, vBox);
        Label aliasLabel = (Label) vBox.getChildren().get(0);
        VBox.setMargin(aliasLabel, new Insets(-21,0,0,0));

        CustomMapModDto customMapMod = null;
        if (platformProfileMapDto.getMapDto().isOfficial()) {
            Label isInMapsCycleLabel = createIsInMapsCycleLabel(platformProfileMapDto, Pos.CENTER);
            VBox.setMargin(isInMapsCycleLabel, new Insets(0,0,10,0));
            vBox.getChildren().add(isInMapsCycleLabel);

        } else {
            customMapMod = (CustomMapModDto) platformProfileMapDto.getMapDto();
            Label mapModLabel = createMapModLabel(customMapMod);
            VBox.setMargin(mapModLabel, new Insets(10,0,0,0));
            Label downloadedStateLabel = createDownloadedLabel(platformProfileMapDto, Pos.CENTER);
            Label isInMapsCycleLabel = createIsInMapsCycleLabel(platformProfileMapDto, Pos.CENTER);

            Hyperlink clickToDownloadMapLink = new Hyperlink();
            if (!platformProfileMapDto.isDownloaded()) {
                clickToDownloadMapLink = createClickToDownloadMapLink(platformProfileMapDto, downloadedStateLabel, isInMapsCycleLabel, Pos.CENTER);
            }

            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.TOP_CENTER);
            stackPane.getChildren().addAll(downloadedStateLabel, isInMapsCycleLabel, clickToDownloadMapLink);
            StackPane.setMargin(isInMapsCycleLabel, new Insets(30,0,0,0));
            VBox.setMargin(stackPane, new Insets(10,0,10,0));

            vBox.getChildren().addAll(mapModLabel, stackPane);
        }

        String urlPhoto = getUrlPhoto(platformProfileMapDto);
        ImageView mapPreview = createMapPreview(platformProfileMapDto, urlPhoto, getWidthNodeByNumberOfColums());
        vBox.getChildren().add(mapPreview);
        VBox.setMargin(mapPreview, new Insets(3,3,3,3));

        return vBox;
    }

    private VBox createMapNodeCardDown(PlatformProfileMapDto platformProfileMapDto) {

        VBox vBox = initializeVerticalNode(platformProfileMapDto);
        String urlPhoto = getUrlPhoto(platformProfileMapDto);
        ImageView mapPreview = createMapPreview(platformProfileMapDto, urlPhoto, getWidthNodeByNumberOfColums());

        vBox.getChildren().add(mapPreview);
        createMapNodeVerticalCard(platformProfileMapDto, vBox);
        VBox.setMargin(mapPreview, new Insets(-21,3,3,3));

        CustomMapModDto customMapMod = null;
        if (platformProfileMapDto.getMapDto().isOfficial()) {
            Label isInMapsCycleLabel = createIsInMapsCycleLabel(platformProfileMapDto, Pos.CENTER);
            VBox.setMargin(isInMapsCycleLabel, new Insets(0,0,10,0));
            vBox.getChildren().add(isInMapsCycleLabel);

        } else {
            customMapMod = (CustomMapModDto) platformProfileMapDto.getMapDto();
            Label mapModLabel = createMapModLabel(customMapMod);
            VBox.setMargin(mapModLabel, new Insets(10,0,0,0));
            Label downloadedStateLabel = createDownloadedLabel(platformProfileMapDto, Pos.CENTER);
            VBox.setMargin(downloadedStateLabel, new Insets(10,0,10,0));
            Label isInMapsCycleLabel = createIsInMapsCycleLabel(platformProfileMapDto, Pos.CENTER);
            VBox.setMargin(isInMapsCycleLabel, new Insets(0,0,10,0));

            Hyperlink clickToDownloadMapLink = new Hyperlink();
            if (!platformProfileMapDto.isDownloaded()) {
                clickToDownloadMapLink = createClickToDownloadMapLink(platformProfileMapDto, downloadedStateLabel, isInMapsCycleLabel, Pos.CENTER);
            }

            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.TOP_CENTER);
            stackPane.getChildren().addAll(downloadedStateLabel, isInMapsCycleLabel, clickToDownloadMapLink);
            StackPane.setMargin(isInMapsCycleLabel, new Insets(30,0,0,0));
            VBox.setMargin(stackPane, new Insets(10,0,10,0));

            vBox.getChildren().addAll(mapModLabel, stackPane);
        }

        return vBox;
    }

    private VBox initializeVerticalDescriptions(PlatformProfileMapDto platformProfileMapDto, Pos pos) {

        Label platformNameText = new Label(platformProfileMapDto.getPlatformDto().getKey());
        platformNameText.setVisible(false);

        Label isOfficialText = new Label(String.valueOf(platformProfileMapDto.getMapDto().isOfficial()));
        isOfficialText.setVisible(false);

        VBox vBox = new VBox();
        vBox.setAlignment(pos);
        vBox.getChildren().addAll(platformNameText, isOfficialText);

        return vBox;
    }

    private HBox initializeHorizontalNodeCard(PlatformProfileMapDto platformProfileMapDto, String urlPhoto) {

        HBox hBox = new HBox();
        hBox.setPrefWidth(getWidthNodeByNumberOfColums()+10);
        hBox.getStyleClass().add("mapCard");
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setSpacing(2);

        StringBuffer tooltipText = new StringBuffer();
        if (!platformProfileMapDto.getMapDto().isOfficial()) {
            tooltipText.append("id WorkShop: ").append(((CustomMapModDto) platformProfileMapDto.getMapDto()).getIdWorkShop());
        }

        if (StringUtils.isNotBlank(urlPhoto)) {
            if (StringUtils.isNotBlank(tooltipText)) {
                tooltipText.append("\n");
            }
            String message = "";
            try {
                message = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.urlInfo");
            } catch (Exception e) {
                message = "Url info";
            }
            tooltipText.append(message).append(": ").append(platformProfileMapDto.getUrlInfo());
        }
        if (platformProfileMapDto.getMapDto().isOfficial() || platformProfileMapDto.isDownloaded()) {
            if (StringUtils.isNotBlank(tooltipText)) {
                tooltipText.append("\n");
            }
            String message = "";
            try {
                message = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.photoLocation");
            } catch (Exception e) {
                message = "Photo location";
            }
            tooltipText.append(message).append(": ").append(urlPhoto);
        }

        Double tooltipDuration = 30.0;
        try {
            tooltipDuration = Double.parseDouble(
                    facade.findPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
            );
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        if (StringUtils.isNotBlank(tooltipText)) {
            Tooltip tooltip = new Tooltip(tooltipText.toString());
            tooltip.setShowDuration(Duration.seconds(tooltipDuration));
            Tooltip.install(hBox, tooltip);
        }

        if (StringUtils.isNotBlank(platformProfileMapDto.getUrlInfo())) {
            hBox.setCursor(Cursor.HAND);
            hBox.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Session.getInstance().setPpm(platformProfileMapDto);
                    loadNewContent("/views/mapWebInfo.fxml");
                    event.consume();
                }
            });
        }

        return hBox;
    }

    private Node createMapNodeCardLeft(PlatformProfileMapDto platformProfileMapDto) {
        String urlPhoto = getUrlPhoto(platformProfileMapDto);
        ImageView mapPreview = createMapPreview(platformProfileMapDto, urlPhoto, Math.floor((getWidthNodeByNumberOfColums()-1) / 2));

        HBox hBox = initializeHorizontalNodeCard(platformProfileMapDto, urlPhoto);
        VBox vBox = initializeVerticalDescriptions(platformProfileMapDto, Pos.CENTER);
        vBox.setPrefWidth(Math.floor((getWidthNodeByNumberOfColums()-1) / 2));
        HBox.setMargin(vBox, new Insets(-21,3,3,3));

        hBox.getChildren().addAll(vBox, mapPreview);
        HBox.setMargin(mapPreview, new Insets(3,0,3,3));

        CheckBox checkBox = createCheckBox();

        Label aliasLabel = createAliasLabel(platformProfileMapDto);
        aliasLabel.setAlignment(Pos.CENTER);

        Label mapNameLabel = createMapNameLabel(platformProfileMapDto);
        mapNameLabel.setAlignment(Pos.CENTER);

        Label releaseDateLabel = createReleaseDateLabel(platformProfileMapDto);
        releaseDateLabel.setAlignment(Pos.CENTER);

        Label importedDateText = createImportedDateLabel(platformProfileMapDto);
        importedDateText.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(aliasLabel, checkBox, mapNameLabel, releaseDateLabel, importedDateText);

        CustomMapModDto customMapMod = null;
        if (platformProfileMapDto.getMapDto().isOfficial()) {
            importedDateText.setPadding(new Insets(0,0,10,0));
            Label isInMapsCycleLabel = createIsInMapsCycleLabel(platformProfileMapDto, Pos.CENTER);
            VBox.setMargin(isInMapsCycleLabel, new Insets(0,0,10,0));
            vBox.getChildren().add(isInMapsCycleLabel);

        } else {
            customMapMod = (CustomMapModDto) platformProfileMapDto.getMapDto();
            Label mapModLabel = createMapModLabel(customMapMod);
            VBox.setMargin(mapModLabel, new Insets(10,0,0,0));
            Label downloadedStateLabel = createDownloadedLabel(platformProfileMapDto, Pos.CENTER);
            VBox.setMargin(downloadedStateLabel, new Insets(10,0,10,0));
            Label isInMapsCycleLabel = createIsInMapsCycleLabel(platformProfileMapDto, Pos.CENTER);
            VBox.setMargin(isInMapsCycleLabel, new Insets(0,0,10,0));

            Hyperlink clickToDownloadMapLink = new Hyperlink();
            if (!platformProfileMapDto.isDownloaded()) {
                clickToDownloadMapLink = createClickToDownloadMapLink(platformProfileMapDto, downloadedStateLabel, isInMapsCycleLabel, Pos.CENTER);
            }

            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.TOP_CENTER);
            stackPane.getChildren().addAll(downloadedStateLabel, isInMapsCycleLabel, clickToDownloadMapLink);
            StackPane.setMargin(isInMapsCycleLabel, new Insets(30,0,0,0));
            VBox.setMargin(stackPane, new Insets(10,0,10,0));

            vBox.getChildren().addAll(mapModLabel, stackPane);
        }

        return hBox;
    }


    private Node createMapNodeCardRight(PlatformProfileMapDto platformProfileMapDto) {

        String urlPhoto = getUrlPhoto(platformProfileMapDto);
        ImageView mapPreview = createMapPreview(platformProfileMapDto, urlPhoto, Math.floor((getWidthNodeByNumberOfColums()-1) / 2));

        HBox hBox = initializeHorizontalNodeCard(platformProfileMapDto, urlPhoto);
        VBox vBox = initializeVerticalDescriptions(platformProfileMapDto, Pos.CENTER);
        vBox.setPrefWidth(Math.floor((getWidthNodeByNumberOfColums()-1) / 2));
        HBox.setMargin(vBox, new Insets(-21,3,3,3));

        hBox.getChildren().addAll(mapPreview, vBox);
        HBox.setMargin(mapPreview, new Insets(3,0,3,3));

        CheckBox checkBox = createCheckBox();

        Label aliasLabel = createAliasLabel(platformProfileMapDto);
        aliasLabel.setAlignment(Pos.CENTER);

        Label mapNameLabel = createMapNameLabel(platformProfileMapDto);
        mapNameLabel.setAlignment(Pos.CENTER);

        Label releaseDateLabel = createReleaseDateLabel(platformProfileMapDto);
        releaseDateLabel.setAlignment(Pos.CENTER);

        Label importedDateText = createImportedDateLabel(platformProfileMapDto);
        importedDateText.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(aliasLabel, checkBox, mapNameLabel, releaseDateLabel, importedDateText);

        CustomMapModDto customMapMod = null;
        if (platformProfileMapDto.getMapDto().isOfficial()) {
            importedDateText.setPadding(new Insets(0,0,10,0));
            Label isInMapsCycleLabel = createIsInMapsCycleLabel(platformProfileMapDto, Pos.CENTER);
            VBox.setMargin(isInMapsCycleLabel, new Insets(0,0,10,0));
            vBox.getChildren().add(isInMapsCycleLabel);

        } else {
            customMapMod = (CustomMapModDto) platformProfileMapDto.getMapDto();
            Label mapModLabel = createMapModLabel(customMapMod);
            VBox.setMargin(mapModLabel, new Insets(10,0,0,0));
            Label downloadedStateLabel = createDownloadedLabel(platformProfileMapDto, Pos.CENTER);
            VBox.setMargin(downloadedStateLabel, new Insets(10,0,10,0));
            Label isInMapsCycleLabel = createIsInMapsCycleLabel(platformProfileMapDto, Pos.CENTER);
            VBox.setMargin(isInMapsCycleLabel, new Insets(0,0,10,0));

            Hyperlink clickToDownloadMapLink = new Hyperlink();
            if (!platformProfileMapDto.isDownloaded()) {
                clickToDownloadMapLink = createClickToDownloadMapLink(platformProfileMapDto, downloadedStateLabel, isInMapsCycleLabel, Pos.CENTER);
            }

            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.TOP_CENTER);
            stackPane.getChildren().addAll(downloadedStateLabel, isInMapsCycleLabel, clickToDownloadMapLink);
            StackPane.setMargin(isInMapsCycleLabel, new Insets(30,0,0,0));
            VBox.setMargin(stackPane, new Insets(10,0,10,0));

            vBox.getChildren().addAll(mapModLabel, stackPane);
        }

        return hBox;
    }

    private Node createMapNodeDetailedCard(PlatformProfileMapDto platformProfileMapDto) {

        String urlPhoto = getUrlPhoto(platformProfileMapDto);
        HBox hBox = new HBox();
        hBox.setPrefWidth(getWidthNodeByNumberOfColums()+70);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setSpacing(50);
        hBox.getStyleClass().add("detailedCard");

        ImageView mapPreview = createMapPreview(platformProfileMapDto, urlPhoto, Math.floor(getWidthNodeDetailedCard(platformProfileMapDto.getMapDto().isOfficial())));

        CheckBox checkBox = createCheckBox();
        checkBox.setPadding(new Insets(1,0,0,0));

        Label aliasLabel = createAliasLabel(platformProfileMapDto);
        aliasLabel.setGraphic(checkBox);
        aliasLabel.setAlignment(Pos.CENTER_LEFT);

        Label downloadedStateLabel = createDownloadedLabel(platformProfileMapDto, Pos.CENTER_LEFT);
        Label isInMapsCycleLabel = createIsInMapsCycleLabel(platformProfileMapDto, Pos.CENTER_LEFT);
        Hyperlink clickToDownloadMapLink = new Hyperlink();
        if (!platformProfileMapDto.isDownloaded()) {
            clickToDownloadMapLink = createClickToDownloadMapLink(platformProfileMapDto, downloadedStateLabel, isInMapsCycleLabel, Pos.CENTER_LEFT);
        }

        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.TOP_LEFT);
        stackPane.getChildren().addAll(downloadedStateLabel, isInMapsCycleLabel, clickToDownloadMapLink);
        StackPane.setMargin(isInMapsCycleLabel, new Insets(30,0,0,0));
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER_LEFT);

        CustomMapModDto customMapMod = null;
        if (platformProfileMapDto.getMapDto().isOfficial()) {
            vBox.getChildren().addAll(aliasLabel, stackPane);
        } else {
            customMapMod = (CustomMapModDto) platformProfileMapDto.getMapDto();
            Label mapModLabel = createMapModLabel(customMapMod);
            VBox.setMargin(mapModLabel, new Insets(5,0,5,0));
            vBox.getChildren().addAll(aliasLabel, mapModLabel, stackPane);
        }

        VBox vBox2 = initializeVerticalDescriptions(platformProfileMapDto, Pos.CENTER_LEFT);
        HBox.setMargin(vBox2, new Insets(-21,3,3,3));
        HBox.setMargin(mapPreview, new Insets(3,0,3,3));

        Label mapNameLabel = createMapNameLabel(platformProfileMapDto);
        mapNameLabel.setAlignment(Pos.CENTER_LEFT);

        Label releaseDateLabel = createReleaseDateLabel(platformProfileMapDto);
        releaseDateLabel.setAlignment(Pos.CENTER_LEFT);

        Label importedDateText = createImportedDateLabel(platformProfileMapDto);
        importedDateText.setAlignment(Pos.CENTER_LEFT);

        vBox2.getChildren().addAll(checkBox, mapNameLabel, releaseDateLabel, importedDateText);

        VBox vBox3 = new VBox();
        vBox3.setAlignment(Pos.CENTER_LEFT);

        vBox.setMinWidth(150);
        vBox.setMaxWidth(150);
        vBox2.setMinWidth(150);
        vBox2.setMaxWidth(150);

        if (!platformProfileMapDto.getMapDto().isOfficial()) {
            Label idWorkShopLabel = new Label("id WorkShop: " + ((CustomMapModDto) platformProfileMapDto.getMapDto()).getIdWorkShop());
            idWorkShopLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 11;");
            vBox3.getChildren().add(idWorkShopLabel);
        }

        if (StringUtils.isNotBlank(urlPhoto)) {
            String message = StringUtils.EMPTY;
            try {
                message = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.urlInfo");
            } catch (Exception e) {
                message = "Url info";
            }
            Label urlInfoLabel = new Label(message + ": " + platformProfileMapDto.getUrlInfo());
            urlInfoLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 11;");
            vBox3.getChildren().add(urlInfoLabel);
        }

        if (platformProfileMapDto.getMapDto().isOfficial() || platformProfileMapDto.isDownloaded()) {
            String message = "";
            try {
                message = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.photoLocation");
            } catch (Exception e) {
                message = "Photo location";
            }
            Label urlPhotoLabel = new Label(message + ": " + urlPhoto);
            urlPhotoLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 11;");
            vBox3.getChildren().add(urlPhotoLabel);
        }

        hBox.getChildren().addAll(mapPreview, vBox, vBox2, vBox3);

        if (StringUtils.isNotBlank(platformProfileMapDto.getUrlInfo())) {
            hBox.setCursor(Cursor.HAND);
            hBox.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Session.getInstance().setPpm(platformProfileMapDto);
                    loadNewContent("/views/mapWebInfo.fxml");
                    event.consume();
                }
            });
        }

        return hBox;
    }

    private Node createMapNode(PlatformProfileMapDto platformProfileMapDto) {
        try {
            double sliderColumns = Double.parseDouble(facade.findPropertyValue("properties/config.properties", "prop.config.mapSliderValue"));
            double detailedCardSliderColumns = Double.parseDouble(facade.findPropertyValue("properties/config.properties", "prop.config.mapSliderDetailedCardValue"));

            steamOfficialMapsFlowPane.setAlignment(Pos.TOP_CENTER);
            steamCustomMapsFlowPane.setAlignment(Pos.TOP_CENTER);
            epicOfficialMapsFlowPane.setAlignment(Pos.TOP_CENTER);
            epicCustomMapsFlowPane.setAlignment(Pos.TOP_CENTER);
            steamOfficialMapsFlowPane.setVgap(50);
            steamCustomMapsFlowPane.setVgap(50);
            epicOfficialMapsFlowPane.setVgap(50);
            epicCustomMapsFlowPane.setVgap(50);

            switch (cardOrientation) {
                case RIGHT:
                    mapsSlider.setMin(2);
                    mapsSlider.setMax(10);
                    mapsSlider.setValue(sliderColumns);
                    return createMapNodeCardRight(platformProfileMapDto);

                case UP:
                    mapsSlider.setMin(2);
                    mapsSlider.setMax(10);
                    mapsSlider.setValue(sliderColumns);
                    return createMapNodeCardUp(platformProfileMapDto);

                case LEFT:
                    mapsSlider.setMin(2);
                    mapsSlider.setMax(10);
                    mapsSlider.setValue(sliderColumns);
                    return createMapNodeCardLeft(platformProfileMapDto);

                case DETAILED:
                    mapsSlider.setMin(1);
                    mapsSlider.setMax(2);
                    mapsSlider.setValue(detailedCardSliderColumns);
                    steamOfficialMapsFlowPane.setAlignment(Pos.TOP_LEFT);
                    steamOfficialMapsFlowPane.setVgap(10);
                    steamCustomMapsFlowPane.setAlignment(Pos.TOP_LEFT);
                    steamCustomMapsFlowPane.setVgap(10);
                    epicOfficialMapsFlowPane.setAlignment(Pos.TOP_LEFT);
                    epicOfficialMapsFlowPane.setVgap(10);
                    epicCustomMapsFlowPane.setAlignment(Pos.TOP_LEFT);
                    epicCustomMapsFlowPane.setVgap(10);
                    return createMapNodeDetailedCard(platformProfileMapDto);

                default:
                    mapsSlider.setMin(2);
                    mapsSlider.setMax(10);
                    mapsSlider.setValue(sliderColumns);
                    return createMapNodeCardDown(platformProfileMapDto);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
        return null;
    }


    private void loadNewContent(String fxmlFilePath) {
        try {
            GridPane templateContent = (GridPane) MainApplication.getTemplate().getNamespace().get("content");
            templateContent.getColumnConstraints().clear();
            templateContent.getRowConstraints().clear();
            templateContent.getChildren().clear();
            FXMLLoader content = new FXMLLoader(getClass().getResource(fxmlFilePath));
            content.setRoot(MainApplication.getTemplate().getNamespace().get("content"));
            content.load();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    private Double getWidthNodeByNumberOfColums() {
        return (MainApplication.getPrimaryStage().getWidth() - (50 * mapsSlider.getValue()) - 180) / mapsSlider.getValue();
    }

    private Double getWidthNodeDetailedCard(boolean isOfficial) {
        if (isOfficial) {
            return MainApplication.getPrimaryStage().getWidth() / 10;
        } else {
            return MainApplication.getPrimaryStage().getWidth() / 20;
        }
    }

    private void resizeNode(Node node) {
        ImageView mapPreview = null;
        VBox vBox = null;
        switch (cardOrientation) {
            case RIGHT:
                ((HBox) node).setPrefWidth(getWidthNodeByNumberOfColums()+10);
                vBox = (VBox) ((HBox) node).getChildren().get(1);
                vBox.setPrefWidth(Math.floor((getWidthNodeByNumberOfColums()-1) / 2));
                mapPreview = (ImageView) ((HBox) node).getChildren().get(0);

                mapPreview.setFitWidth(Math.floor((getWidthNodeByNumberOfColums()-1) / 2));
                if (!mapPreview.isPreserveRatio()) {
                    mapPreview.setFitHeight(mapPreview.getFitWidth());
                }
                break;

            case UP:
                ((VBox) node).setPrefWidth(getWidthNodeByNumberOfColums());
                Label isOfficialText = (Label) ((VBox) node).getChildren().get(1);
                if ("true".equals(isOfficialText.getText())) {
                    mapPreview = (ImageView) ((VBox) node).getChildren().get(8);
                } else {
                    mapPreview = (ImageView) ((VBox) node).getChildren().get(9);
                }

                mapPreview.setFitWidth(getWidthNodeByNumberOfColums());
                if (!mapPreview.isPreserveRatio()) {
                    mapPreview.setFitHeight(mapPreview.getFitWidth());
                }
                break;

            case LEFT:
                ((HBox) node).setPrefWidth(getWidthNodeByNumberOfColums()+10);
                vBox = (VBox) ((HBox) node).getChildren().get(0);
                vBox.setPrefWidth(Math.floor((getWidthNodeByNumberOfColums()-1) / 2));
                mapPreview = (ImageView) ((HBox) node).getChildren().get(1);

                mapPreview.setFitWidth(Math.floor((getWidthNodeByNumberOfColums()-1) / 2));
                if (!mapPreview.isPreserveRatio()) {
                    mapPreview.setFitHeight(mapPreview.getFitWidth());
                }
                break;

            case DETAILED:
                ((HBox) node).setPrefWidth(getWidthNodeByNumberOfColums()+70);
                mapPreview = (ImageView) ((HBox) node).getChildren().get(0);
                Label isOfficialLabel = (Label) ((VBox) ((HBox) node).getChildren().get(2)).getChildren().get(1);
                mapPreview.setFitWidth(Math.floor(getWidthNodeDetailedCard("true".equals(isOfficialLabel.getText()))));
                if (!mapPreview.isPreserveRatio()) {
                    mapPreview.setFitHeight(mapPreview.getFitWidth());
                }
                break;

            default:
                ((VBox) node).setPrefWidth(getWidthNodeByNumberOfColums());
                mapPreview = (ImageView) ((VBox) node).getChildren().get(2);

                mapPreview.setFitWidth(getWidthNodeByNumberOfColums());
                if (!mapPreview.isPreserveRatio()) {
                    mapPreview.setFitHeight(mapPreview.getFitWidth());
                }
                break;
        }
    }

    @FXML
    private void mapsSliderOnMouseClicked() {
        for (int index = 0; index < steamOfficialMapsFlowPane.getChildren().size(); index++) {
            Node node = steamOfficialMapsFlowPane.getChildren().get(index);
            resizeNode(node);
        }
        for (int index = 0; index < steamCustomMapsFlowPane.getChildren().size(); index++) {
            Node node = steamCustomMapsFlowPane.getChildren().get(index);
            resizeNode(node);
        }
        for (int index = 0; index < epicOfficialMapsFlowPane.getChildren().size(); index++) {
            Node node = epicOfficialMapsFlowPane.getChildren().get(index);
            resizeNode(node);
        }
        for (int index = 0; index < epicCustomMapsFlowPane.getChildren().size(); index++) {
            Node node = epicCustomMapsFlowPane.getChildren().get(index);
            resizeNode(node);
        }
        try {
            if (!EnumCardOrientation.DETAILED.equals(cardOrientation)) {
                facade.setConfigPropertyValue("prop.config.mapSliderValue", String.valueOf(mapsSlider.getValue()));
            } else {
                facade.setConfigPropertyValue("prop.config.mapSliderDetailedCardValue", String.valueOf(mapsSlider.getValue()));
            }

        } catch (Exception e) {
            String message = "Error setting maps slider value in config.properties file";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    @FXML
    private void searchMapsKeyReleased() {
        steamOfficialMapsFlowPane.getChildren().clear();
        steamCustomMapsFlowPane.getChildren().clear();
        epicOfficialMapsFlowPane.getChildren().clear();
        epicCustomMapsFlowPane.getChildren().clear();

        for (PlatformProfileMapDto platformProfileMapDto : steamPlatformProfileMapDtoList) {
            String mapName = StringUtils.upperCase(platformProfileMapDto.getMapDto().getKey());
            String alias = StringUtils.upperCase(platformProfileMapDto.getAlias());
            if (mapName.contains(StringUtils.upperCase(searchMaps.getText())) || alias.contains(StringUtils.upperCase(searchMaps.getText()))) {
                Node node = createMapNode(platformProfileMapDto);
                if (platformProfileMapDto.getMapDto().isOfficial()) {
                    steamOfficialMapsFlowPane.getChildren().add(node);
                } else {
                    steamCustomMapsFlowPane.getChildren().add(node);
                }
            }
        }

        for (PlatformProfileMapDto platformProfileMapDto : epicPlatformProfileMapDtoList) {
            String mapName = StringUtils.upperCase(platformProfileMapDto.getMapDto().getKey());
            String alias = StringUtils.upperCase(platformProfileMapDto.getAlias());
            if (mapName.contains(StringUtils.upperCase(searchMaps.getText())) || alias.contains(StringUtils.upperCase(searchMaps.getText()))) {
                Node node = createMapNode(platformProfileMapDto);
                if (platformProfileMapDto.getMapDto().isOfficial()) {
                    epicOfficialMapsFlowPane.getChildren().add(node);
                } else {
                    epicCustomMapsFlowPane.getChildren().add(node);
                }
            }
        }
    }

    @FXML
    private void addNewMapsOnAction() {
        List<AddMapsToPlatformProfile> addMapsToPlatformProfileList = new ArrayList<AddMapsToPlatformProfile>();

        try {
            if (profileSelect.getValue() == null) {
                logger.warn("No profiles defined. Add new maps aborted");
                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profileNotEmpty");
                Utils.warningDialog(headerText, contentText);
                return;
            }

            if (steamOfficialMapsTab.isSelected()) {
                SingleSelectionModel<Tab> selectionModel = mapsTabPane.getSelectionModel();
                selectionModel.select(steamCustomMapsTab);
            }
            String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.addCustomMaps");

            addMapsToPlatformProfileList = Utils.defineCustomMapsToAddPerProfile(headerText, profileSelect.getItems());

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }

        if (addMapsToPlatformProfileList != null && !addMapsToPlatformProfileList.isEmpty()) {
            progressIndicator.setVisible(true);
            StringBuffer success = new StringBuffer();
            StringBuffer errors = new StringBuffer();
            List<AddMapsToPlatformProfile> finalAddMapsToPlatformProfileList = addMapsToPlatformProfileList;

            Task<List<PlatformProfileMapDto>> task = new Task<List<PlatformProfileMapDto>>() {
                @Override
                protected List<PlatformProfileMapDto> call() throws Exception {

                    List<PlatformProfileMapDto> mapAddedListToActualPlatformProfile = new ArrayList<PlatformProfileMapDto>();
                    for (AddMapsToPlatformProfile addMapsToPlatformProfile: finalAddMapsToPlatformProfileList) {

                        List<String> platformNameList = new ArrayList<String>();
                        switch (addMapsToPlatformProfile.getPlatformName()) {
                            case "Steam":
                                platformNameList.add(EnumPlatform.STEAM.name());
                                if (facade.isCorrectInstallationFolder(EnumPlatform.STEAM.name())) {
                                    AddCustomMapsToProfileFacadeResult result = facade.addCustomMapsToProfile(
                                            platformNameList,
                                            addMapsToPlatformProfile.getProfileName(),
                                            addMapsToPlatformProfile.getMapList(),
                                            languageCode,
                                            profileSelect.getValue().getName()
                                    );
                                    List<PlatformProfileMapDto> mapAddedList = result.getPlatformProfileMapDtoList();
                                    success.append(result.getSuccess());
                                    errors.append(result.getErrors());

                                    if (addMapsToPlatformProfile.getProfileName().equals(profileSelect.getValue().getName())) {
                                        mapAddedListToActualPlatformProfile.addAll(mapAddedList);
                                    }
                                } else {
                                    String errorMessage = "The platform " + EnumPlatform.STEAM.getDescripcion() + " has not a correct installation folder";
                                    errors.append(errorMessage + "\n");
                                }
                                break;

                            case "Epic Games":
                                platformNameList.add(EnumPlatform.EPIC.name());
                                if (facade.isCorrectInstallationFolder(EnumPlatform.EPIC.name())) {
                                    AddCustomMapsToProfileFacadeResult result = facade.addCustomMapsToProfile(
                                            platformNameList,
                                            addMapsToPlatformProfile.getProfileName(),
                                            addMapsToPlatformProfile.getMapList(),
                                            languageCode,
                                            profileSelect.getValue().getName()
                                    );
                                    List<PlatformProfileMapDto> mapAddedList = result.getPlatformProfileMapDtoList();
                                    success.append(result.getSuccess());
                                    errors.append(result.getErrors());

                                    if (addMapsToPlatformProfile.getProfileName().equals(profileSelect.getValue().getName())) {
                                        mapAddedListToActualPlatformProfile.addAll(mapAddedList);
                                    }
                                } else {
                                    String errorMessage = "The platform " + EnumPlatform.EPIC.getDescripcion() + " has not a correct installation folder";
                                    errors.append(errorMessage + "\n");
                                }
                                break;

                            case "All platforms":
                                if (facade.isCorrectInstallationFolder(EnumPlatform.STEAM.name())) {
                                    platformNameList.add(EnumPlatform.STEAM.name());
                                } else {
                                    String errorMessage = "The platform " + EnumPlatform.STEAM.getDescripcion() + " has not a correct installation folder";
                                    errors.append(errorMessage + "\n");
                                }

                                if (facade.isCorrectInstallationFolder(EnumPlatform.EPIC.name())) {
                                    platformNameList.add(EnumPlatform.EPIC.name());
                                } else {
                                    String errorMessage = "The platform " + EnumPlatform.EPIC.getDescripcion() + " has not a correct installation folder";
                                    errors.append(errorMessage + "\n");
                                }

                                AddCustomMapsToProfileFacadeResult result = facade.addCustomMapsToProfile(
                                        platformNameList,
                                        addMapsToPlatformProfile.getProfileName(),
                                        addMapsToPlatformProfile.getMapList(),
                                        languageCode,
                                        profileSelect.getValue().getName()
                                );
                                List<PlatformProfileMapDto> mapAddedList = result.getPlatformProfileMapDtoList();
                                success.append(result.getSuccess());
                                errors.append(result.getErrors());

                                if (addMapsToPlatformProfile.getProfileName().equals(profileSelect.getValue().getName())) {
                                    mapAddedListToActualPlatformProfile.addAll(mapAddedList);
                                }
                                break;
                        }
                    }

                    return mapAddedListToActualPlatformProfile;
                }
            };

            task.setOnSucceeded(wse -> {
                List<PlatformProfileMapDto> mapAddedListToActualPlatformProfile = task.getValue();
                if (mapAddedListToActualPlatformProfile != null && !mapAddedListToActualPlatformProfile.isEmpty()) {

                    mapAddedListToActualPlatformProfile.stream().forEach(platformProfileMap -> {

                        Optional<PlatformProfileMapDto> firstSteamPlatformProfileMap = steamPlatformProfileMapDtoList.stream().
                                filter(steamPpm -> !steamPpm.getMapDto().isOfficial() &&
                                        EnumPlatform.STEAM.name().equals(steamPpm.getPlatformDto().getKey()) &&
                                        ((CustomMapModDto) steamPpm.getMapDto()).getIdWorkShop().equals(((CustomMapModDto) platformProfileMap.getMapDto()).getIdWorkShop())
                                ).findFirst();

                        if (!firstSteamPlatformProfileMap.isPresent()) {
                            try {
                                Optional<PlatformProfileMapDto> platformProfileMapDtoOptional = facade.findPlatformProfileMapDtoByName(EnumPlatform.STEAM.name(), profileSelect.getValue().getName(), platformProfileMap.getMapDto().getKey());
                                if (platformProfileMapDtoOptional.isPresent()) {
                                    steamPlatformProfileMapDtoList.add(platformProfileMapDtoOptional.get());
                                    Node node = createMapNode(platformProfileMapDtoOptional.get());
                                    steamCustomMapsFlowPane.getChildren().add(node);
                                }
                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                            }
                        }

                        Optional<PlatformProfileMapDto> firstEpicPlatformProfileMap = epicPlatformProfileMapDtoList.stream().
                                filter(epicPpm -> !epicPpm.getMapDto().isOfficial() &&
                                        EnumPlatform.EPIC.name().equals(epicPpm.getPlatformDto().getKey()) &&
                                        ((CustomMapModDto) epicPpm.getMapDto()).getIdWorkShop().equals(((CustomMapModDto) platformProfileMap.getMapDto()).getIdWorkShop())
                                ).findFirst();

                        if (!firstEpicPlatformProfileMap.isPresent()) {
                            try {
                                Optional<PlatformProfileMapDto> platformProfileMapDtoOptional = facade.findPlatformProfileMapDtoByName(EnumPlatform.EPIC.name(), profileSelect.getValue().getName(), platformProfileMap.getMapDto().getKey());
                                if (platformProfileMapDtoOptional.isPresent()) {
                                    epicPlatformProfileMapDtoList.add(platformProfileMapDtoOptional.get());
                                    Node node = createMapNode(platformProfileMapDtoOptional.get());
                                    epicCustomMapsFlowPane.getChildren().add(node);
                                }
                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                            }
                        }
                    });
                }

                progressIndicator.setVisible(false);

                try {
                    String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectProfiles");
                    if (StringUtils.isNotBlank(success)) {
                        String customMapsModsTabText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.customMaps");
                        steamCustomMapsTab.setGraphic(createTabTitle(EnumPlatform.STEAM, false, customMapsModsTabText, steamCustomMapsFlowPane.getChildren().size()));
                        epicCustomMapsTab.setGraphic(createTabTitle(EnumPlatform.EPIC, false, customMapsModsTabText, epicCustomMapsFlowPane.getChildren().size()));

                        headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.OperationDone");
                        Utils.infoDialog(headerText, success.toString());
                    }

                    if (StringUtils.isNotBlank(errors)) {
                        headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                        Utils.infoDialog(headerText, errors.toString());
                    }

                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            });
            task.setOnFailed(wse -> {
                progressIndicator.setVisible(false);
            });

            Thread thread = new Thread(task);
            thread.start();
        }
    }

    private Node getNodeIfSelected(Node node, StringBuffer message) {

        CardNode cardNode = new CardNode(cardOrientation, node);

        if (cardNode.getCheckbox().isSelected()) {
            String platformDescription = EnumPlatform.getByName(cardNode.getPlatformNameLabel().getText()).getDescripcion();
            message.append("[").append(platformDescription).append("] ").append(cardNode.getMapNameLabel().getText()).append("\n");
            return node;
        }
        return null;
    }

     @FXML
    private void removeMapsOnAction() {
        try {
            StringBuffer message = new StringBuffer();
            ObservableList<Node> nodeList = FXCollections.concat(steamOfficialMapsFlowPane.getChildren(), steamCustomMapsFlowPane.getChildren(), epicOfficialMapsFlowPane.getChildren(), epicCustomMapsFlowPane.getChildren());

            List<Node> removeList = new ArrayList<Node>();
            for (Node node : nodeList) {
                Node selectedNode = getNodeIfSelected(node, message);
                if (selectedNode != null) {
                    removeList.add(selectedNode);
                }
            }

            if (removeList.isEmpty()) {
                logger.warn("No selected maps/mods to delete. You must select at least one item to be deleted");
                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapsNotSelected");
                String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectItems");
                Utils.warningDialog(headerText, contentText);

            } else {
                String question = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.deleteMapsQuestion");
                Optional<ButtonType> result = Utils.questionDialog(question, message.toString());
                if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                    progressIndicator.setVisible(true);
                    String mapNameText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.name");

                    final StringBuffer[] errors = {new StringBuffer()};
                    Task<List<Node>> task = new Task<List<Node>>() {
                        @Override
                        protected List<Node> call() throws Exception {
                            List<Node> nodeToRemoveList = new ArrayList<Node>();
                            errors[0] = new StringBuffer();
                            for (Node node : removeList) {
                                try {
                                    CardNode cardNode = new CardNode(cardOrientation, node);
                                    String mapName = cardNode.getMapNameLabel().getText().replace(mapNameText + ": ", "");
                                    AbstractMapDto map = facade.deleteMapFromPlatformProfile(cardNode.getPlatformNameLabel().getText(), mapName, profileSelect.getValue().getName());
                                    if (map != null) {
                                        nodeToRemoveList.add(node);
                                    } else {
                                        errors[0].append(mapName).append("\n");
                                    }
                                } catch (Exception e) {
                                    logger.error(e.getMessage(), e);
                                    Utils.errorDialog(e.getMessage(), e);
                                }
                            }
                            return nodeToRemoveList;
                        }
                    };

                    task.setOnSucceeded(wse -> {
                        List<Node> nodeToRemoveList = task.getValue();
                        try {
                            if (!nodeToRemoveList.isEmpty()) {

                                for (Node node : nodeToRemoveList) {
                                    CardNode cardNode = new CardNode(cardOrientation, node);

                                    if (EnumPlatform.STEAM.name().equals(cardNode.getPlatformNameLabel().getText())) {
                                        if (Boolean.parseBoolean(cardNode.getIsOfficialLabel().getText())) {
                                            steamOfficialMapsFlowPane.getChildren().remove(node);
                                        } else {
                                            steamCustomMapsFlowPane.getChildren().remove(node);
                                        }
                                    }
                                    if (EnumPlatform.EPIC.name().equals(cardNode.getPlatformNameLabel().getText())) {
                                        if (Boolean.parseBoolean(cardNode.getIsOfficialLabel().getText())) {
                                            epicOfficialMapsFlowPane.getChildren().remove(node);
                                        } else {
                                            epicCustomMapsFlowPane.getChildren().remove(node);
                                        }
                                    }
                                }

                                ListPlatformProfileMapFacadeResult listPlatformProfileMapFacadeResult = facade.getPlatformProfileMapList(profileSelect.getValue().getName());
                                steamPlatformProfileMapDtoList.clear();
                                steamPlatformProfileMapDtoList = listPlatformProfileMapFacadeResult.getSteamPlatformProfileMapDtoList();
                                epicPlatformProfileMapDtoList.clear();
                                epicPlatformProfileMapDtoList = listPlatformProfileMapFacadeResult.getEpicPlatformProfileMapDtoList();

                                String officialMapsTabText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.officialMaps");
                                steamOfficialMapsTab.setGraphic(createTabTitle(EnumPlatform.STEAM, true, officialMapsTabText, steamOfficialMapsFlowPane.getChildren().size()));
                                String customMapsModsTabText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.customMaps");
                                steamCustomMapsTab.setGraphic(createTabTitle(EnumPlatform.STEAM, false, customMapsModsTabText, steamCustomMapsFlowPane.getChildren().size()));

                                epicOfficialMapsTab.setGraphic(createTabTitle(EnumPlatform.EPIC, true, officialMapsTabText, epicOfficialMapsFlowPane.getChildren().size()));
                                epicCustomMapsTab.setGraphic(createTabTitle(EnumPlatform.EPIC, false, customMapsModsTabText, epicCustomMapsFlowPane.getChildren().size()));

                        }
                            if (StringUtils.isNotBlank(errors[0].toString())) {
                                logger.warn("Next maps/mods could not be deleted: " + errors[0].toString());
                                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapsNotDeleted");
                                Utils.warningDialog(headerText, errors[0].toString());
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                            Utils.errorDialog(e.getMessage(), e);
                        }
                        progressIndicator.setVisible(false);
                    });

                    task.setOnFailed(wse -> {
                        progressIndicator.setVisible(false);
                    });

                    Thread thread = new Thread(task);
                    thread.start();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void importMapsFromServerOnAction() {

        try {
            if (profileSelect.getValue() == null) {
                logger.warn("No profiles defined. Import operation aborted");
                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profileNotEmpty");
                Utils.warningDialog(headerText, contentText);
                return;
            }

            PrepareImportMapsFromServerFacadeResult prepareResult = facade.prepareImportMapsFromServer(
                    profileSelect.getValue().getName()
            );

            String languageCode = facade.findPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            String selectPlatformProfilesHeaderText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectProfiles");
            List<PlatformProfileToDisplay> selectedPlatformProfileList = Utils.selectPlatformProfilesToRunDialog(
                    selectPlatformProfilesHeaderText + ":",
                    prepareResult.getPlatformProfileToDisplayList(),
                    prepareResult.getFullProfileNameList()
            );

            if (selectedPlatformProfileList.isEmpty()) {
                return;
            }


            List<PlatformProfileMapToImport> officialMapPpmToImportList = new ArrayList<PlatformProfileMapToImport>();
            List<PlatformProfileMapToImport> customMapPpmToImportList = new ArrayList<PlatformProfileMapToImport>();
            for (PlatformProfileToDisplay selectedPlatformProfile: selectedPlatformProfileList) {
                List<PlatformDto> platformList = new ArrayList<PlatformDto>();

                switch (selectedPlatformProfile.getPlatformName()) {
                    case "Steam":
                        platformList.add(prepareResult.getSteamPlatform());
                        break;

                    case "Epic Games":
                        platformList.add(prepareResult.getEpicPlatform());
                        break;

                    case "All platforms":
                        platformList.add(prepareResult.getSteamPlatform());
                        platformList.add(prepareResult.getEpicPlatform());
                        break;
                }

                for (PlatformDto platform: platformList) {
                    try {
                        String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectCustomMaps",
                                selectedPlatformProfile.getProfileName(),
                                platform.getValue());


                        // Not present Official Maps
                        List<MapToDisplay> notPresentOfficialMapList = new ArrayList<MapToDisplay>();
                        if (EnumPlatform.STEAM.name().equals(platform.getKey())) {
                            notPresentOfficialMapList = facade.getNotPresentOfficialMapList(prepareResult.getSteamOfficialMapNameList(), platform.getKey(), selectedPlatformProfile.getProfileName());
                        } else {
                            notPresentOfficialMapList = facade.getNotPresentOfficialMapList(prepareResult.getEpicOfficialMapNameList(), platform.getKey(), selectedPlatformProfile.getProfileName());
                        }

                        for (MapToDisplay map : notPresentOfficialMapList) {
                            officialMapPpmToImportList.add(
                                    new PlatformProfileMapToImport(
                                            platform.getKey(),
                                            selectedPlatformProfile.getProfileName(),
                                            map
                                    )
                            );
                        }

                        // Selected Custom Maps
                        List<MapToDisplay> selectedCustomMapModList = new ArrayList<MapToDisplay>();
                        if (EnumPlatform.STEAM.name().equals(platform.getKey())) {
                            selectedCustomMapModList = Utils.selectMapsDialog(headerText, prepareResult.getSteamCustomMapModList());
                        } else {
                            selectedCustomMapModList = Utils.selectMapsDialog(headerText, prepareResult.getEpicCustomMapModList());
                        }

                        for (MapToDisplay map : selectedCustomMapModList) {
                            customMapPpmToImportList.add(
                                    new PlatformProfileMapToImport(
                                            platform.getKey(),
                                            selectedPlatformProfile.getProfileName(),
                                            map
                                    )
                            );
                        }
                    } catch (Exception e) {
                        String message = "Error importing maps and mods from server to the launcher";
                        logger.error(message, e);
                    }
                }
            }

            String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.importOperation");
            String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.confirmImportOperation");
            Optional<ButtonType> result = Utils.questionDialog(headerText, contentText);

            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                progressIndicator.setVisible(true);
                Task<List<ImportMapResultToDisplay>> task = new Task<List<ImportMapResultToDisplay>>() {
                    @Override
                    protected List<ImportMapResultToDisplay> call() throws Exception {
                        logger.info("Starting the process to import maps and mods from the server to the launcher");
                        List<ImportMapResultToDisplay> importMapResultToDisplayList = importCustomMapsModsFromServer(customMapPpmToImportList);
                        importMapResultToDisplayList.addAll(importOfficialMapsFromServer(officialMapPpmToImportList));
                        return importMapResultToDisplayList;
                    }
                };

                task.setOnSucceeded(wse -> {
                    try {
                        List<ImportMapResultToDisplay> importMapResultToDisplayList = task.getValue();
                        steamOfficialMapsFlowPane.getChildren().clear();
                        steamCustomMapsFlowPane.getChildren().clear();
                        epicOfficialMapsFlowPane.getChildren().clear();
                        epicCustomMapsFlowPane.getChildren().clear();

                        steamPlatformProfileMapDtoList.stream().forEach(profileMapDto -> {
                            Node node = createMapNode(profileMapDto);
                            if (profileMapDto.getMapDto().isOfficial()) {
                                steamOfficialMapsFlowPane.getChildren().add(node);
                            } else {
                                steamCustomMapsFlowPane.getChildren().add(node);
                            }
                        });

                        epicPlatformProfileMapDtoList.stream().forEach(profileMapDto -> {
                            Node node = createMapNode(profileMapDto);
                            if (profileMapDto.getMapDto().isOfficial()) {
                                epicOfficialMapsFlowPane.getChildren().add(node);
                            } else {
                                epicCustomMapsFlowPane.getChildren().add(node);
                            }
                        });

                        String officialMapsTabText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.officialMaps");
                        String customMapsModsTabText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.customMaps");

                        steamOfficialMapsTab.setGraphic(createTabTitle(EnumPlatform.STEAM, true, officialMapsTabText, steamOfficialMapsFlowPane.getChildren().size()));
                        epicOfficialMapsTab.setGraphic(createTabTitle(EnumPlatform.EPIC, true, officialMapsTabText, epicOfficialMapsFlowPane.getChildren().size()));
                        steamCustomMapsTab.setGraphic(createTabTitle(EnumPlatform.STEAM, false, customMapsModsTabText, steamCustomMapsFlowPane.getChildren().size()));
                        epicCustomMapsTab.setGraphic(createTabTitle(EnumPlatform.EPIC, false, customMapsModsTabText, epicCustomMapsFlowPane.getChildren().size()));

                        progressIndicator.setVisible(false);
                        logger.info("The process to import maps and mods from the server to the launcher has finished.");


                        List<String> profileNameList = importMapResultToDisplayList.stream().map(ImportMapResultToDisplay::getProfileName).distinct().collect(Collectors.toList());
                        if (profileNameList == null || profileNameList.isEmpty()) {
                            String importMapsFromServerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.importMaps");
                            String noNewMapsFromServerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.noMapsImportedWarning");
                            Utils.infoDialog(importMapsFromServerText, noNewMapsFromServerText);

                        } else {
                            Optional<VBox> importDialogResult = Optional.empty();
                            Integer profileIndex = 0;
                            do {
                                importDialogResult = Utils.importMapsResultDialog(importMapResultToDisplayList, profileNameList.get(profileIndex));

                                if (importDialogResult.isPresent()) {
                                    String action = ((Text) importDialogResult.get().getChildren().get(5)).getText();
                                    if (action.equals("PREVIOUS")) {
                                        if (profileIndex > 0) {
                                            profileIndex--;
                                        } else {
                                            profileIndex = profileNameList.size() - 1;
                                        }
                                    }
                                    if (action.equals("NEXT")) {
                                        if (profileIndex < (profileNameList.size() - 1)) {
                                            profileIndex++;
                                        } else {
                                            profileIndex = 0;
                                        }
                                    }
                                }
                            } while (importDialogResult.isPresent());
                        }

                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                });
                task.setOnFailed(wse -> {
                    progressIndicator.setVisible(false);
                });
                new Thread(task).start();
            }

        } catch (Exception e) {
            String message = "Error importing maps and mods from server to the launcher";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    private void addOfficialMapToPlatformTabs(String platformName, String profileName, String mapName) throws Exception {

        if (EnumPlatform.STEAM.name().equals(platformName) &&
                !steamPlatformProfileMapDtoList.stream().
                        filter(ppm -> ppm.getMapDto().isOfficial() && ppm.getMapDto().getKey().equals(mapName)).
                        findFirst().
                        isPresent()) {

            Optional<PlatformProfileMapDto> profileMapDtoOptional = facade.findPlatformProfileMapDtoByName(
                    platformName,
                    profileName,
                    mapName
            );

            if (profileMapDtoOptional.isPresent()) {
                steamPlatformProfileMapDtoList.add(profileMapDtoOptional.get());
            }
        }

        if (EnumPlatform.EPIC.name().equals(platformName) &&
                !epicPlatformProfileMapDtoList.stream().
                        filter(ppm -> ppm.getMapDto().isOfficial() && ppm.getMapDto().getKey().equals(mapName)).
                        findFirst().
                        isPresent()) {

            Optional<PlatformProfileMapDto> profileMapDtoOptional = facade.findPlatformProfileMapDtoByName(
                    platformName,
                    profileName,
                    mapName
            );

            if (profileMapDtoOptional.isPresent()) {
                epicPlatformProfileMapDtoList.add(profileMapDtoOptional.get());
            }
        }
    }

    private void addCustomMapToPlatformTabs(String platformName, String profileName, String mapName, Long idWorkshop) throws Exception {

        if (EnumPlatform.STEAM.name().equals(platformName) &&
                !steamPlatformProfileMapDtoList.stream().
                        filter(ppm -> !ppm.getMapDto().isOfficial() && ((CustomMapModDto) ppm.getMapDto()).getIdWorkShop().equals(idWorkshop)).
                        findFirst().
                        isPresent()) {

            Optional<PlatformProfileMapDto> profileMapDtoOptional = facade.findPlatformProfileMapDtoByName(
                    platformName,
                    profileName,
                    mapName
            );

            if (profileMapDtoOptional.isPresent()) {
                steamPlatformProfileMapDtoList.add(profileMapDtoOptional.get());
            }
        }

        if (EnumPlatform.EPIC.name().equals(platformName) &&
                !epicPlatformProfileMapDtoList.stream().
                        filter(ppm -> !ppm.getMapDto().isOfficial() && ((CustomMapModDto) ppm.getMapDto()).getIdWorkShop().equals(idWorkshop)).
                        findFirst().
                        isPresent()) {

            Optional<PlatformProfileMapDto> profileMapDtoOptional = facade.findPlatformProfileMapDtoByName(
                    platformName,
                    profileName,
                    mapName
            );

            if (profileMapDtoOptional.isPresent()) {
                epicPlatformProfileMapDtoList.add(profileMapDtoOptional.get());
            }
        }
    }

    private List<ImportMapResultToDisplay> importOfficialMapsFromServer(List<PlatformProfileMapToImport> ppmToImportList) {
        if (ppmToImportList == null || ppmToImportList.isEmpty()) {
            return new ArrayList<ImportMapResultToDisplay>();
        }

        try {
            List<ImportMapResultToDisplay> importMapResultToDisplayList = facade.importOfficialMapsFromServer(ppmToImportList, profileSelect.getValue().getName());

            for (ImportMapResultToDisplay importMapResultToDisplay: importMapResultToDisplayList) {
                if (importMapResultToDisplay.getProfileName().equals(profileSelect.getValue().getName())) {
                    try {
                        addOfficialMapToPlatformTabs(
                                importMapResultToDisplay.getPlatformName(),
                                importMapResultToDisplay.getProfileName(),
                                importMapResultToDisplay.getMapName()
                        );
                    } catch (Exception e) {
                        String errorMessage = "Can not show the imported official map with name " + importMapResultToDisplay.getMapName() + " in the platform " + importMapResultToDisplay.getProfileName();
                        logger.error(errorMessage, e);
                    }
                }
            }

            return importMapResultToDisplayList;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return new ArrayList<ImportMapResultToDisplay>();
    }

    private List<ImportMapResultToDisplay> importCustomMapsModsFromServer(List<PlatformProfileMapToImport> ppmToImportList) {
        if (ppmToImportList == null || ppmToImportList.isEmpty()) {
            return new ArrayList<ImportMapResultToDisplay>();
        }

        try {
            List<ImportMapResultToDisplay> importMapResultToDisplayList = facade.importCustomMapsModsFromServer(ppmToImportList, profileSelect.getValue().getName());

            for (ImportMapResultToDisplay importMapResultToDisplay: importMapResultToDisplayList) {
                if (importMapResultToDisplay.getProfileName().equals(profileSelect.getValue().getName())) {
                    try {
                        addCustomMapToPlatformTabs(
                                importMapResultToDisplay.getPlatformName(),
                                importMapResultToDisplay.getProfileName(),
                                importMapResultToDisplay.getMapName(),
                                importMapResultToDisplay.getIdWorkshop()
                        );
                    } catch (Exception e) {
                        String errorMessage = "Can not show the imported official map with name " + importMapResultToDisplay.getMapName() + " in the platform " + importMapResultToDisplay.getProfileName();
                        logger.error(errorMessage, e);
                    }
                }
            }

            return importMapResultToDisplayList;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return new ArrayList<ImportMapResultToDisplay>();
    }


    @FXML
    private void searchInWorkShopOnAction() {
        Session.getInstance().setPpm(null);
        Session.getInstance().setMapsProfile(profileSelect.getValue());
        loadNewContent("/views/mapWebInfo.fxml");
    }

    @FXML
    private void profileOnAction() {
        try {
            steamCustomMapsFlowPane.getChildren().clear();
            steamOfficialMapsFlowPane.getChildren().clear();
            epicCustomMapsFlowPane.getChildren().clear();
            epicOfficialMapsFlowPane.getChildren().clear();

            ListPlatformProfileMapFacadeResult listPlatformProfileMapFacadeResult = facade.getPlatformProfileMapList(profileSelect.getValue().getName());
            steamPlatformProfileMapDtoList = listPlatformProfileMapFacadeResult.getSteamPlatformProfileMapDtoList();
            epicPlatformProfileMapDtoList = listPlatformProfileMapFacadeResult.getEpicPlatformProfileMapDtoList();

            for (PlatformProfileMapDto platformProfileMapDto : steamPlatformProfileMapDtoList) {
                Node node = createMapNode(platformProfileMapDto);
                if (platformProfileMapDto.getMapDto().isOfficial()) {
                    steamOfficialMapsFlowPane.getChildren().add(node);
                } else {
                    steamCustomMapsFlowPane.getChildren().add(node);
                }
            }

            for (PlatformProfileMapDto platformProfileMapDto : epicPlatformProfileMapDtoList) {
                Node node = createMapNode(platformProfileMapDto);
                if (platformProfileMapDto.getMapDto().isOfficial()) {
                    epicOfficialMapsFlowPane.getChildren().add(node);
                } else {
                    epicCustomMapsFlowPane.getChildren().add(node);
                }
            }

            String officialMapsTabText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.officialMaps");
            String customMapsModsTabText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.customMaps");

            steamOfficialMapsTab.setGraphic(createTabTitle(EnumPlatform.STEAM, true, officialMapsTabText, steamOfficialMapsFlowPane.getChildren().size()));
            steamCustomMapsTab.setGraphic(createTabTitle(EnumPlatform.STEAM, false, customMapsModsTabText, steamCustomMapsFlowPane.getChildren().size()));
            epicOfficialMapsTab.setGraphic(createTabTitle(EnumPlatform.EPIC, true, officialMapsTabText, epicOfficialMapsFlowPane.getChildren().size()));
            epicCustomMapsTab.setGraphic(createTabTitle(EnumPlatform.EPIC, false, customMapsModsTabText, epicCustomMapsFlowPane.getChildren().size()));

            Session.getInstance().setMapsProfile(profileSelect.getValue());
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void orderMapsByAliasOnAction() {
        orderType = EnumCardOrderType.BY_ALIAS;

        CheckBox steamOfficialTabCheckbox = (CheckBox) ((HBox) steamOfficialMapsTab.getGraphic()).getChildren().get(0);
        steamOfficialTabCheckbox.setSelected(false);
        CheckBox steamCustomTabCheckbox = (CheckBox) ((HBox) steamCustomMapsTab.getGraphic()).getChildren().get(0);
        steamCustomTabCheckbox.setSelected(false);
        CheckBox epicOfficialTabCheckbox = (CheckBox) ((HBox) epicOfficialMapsTab.getGraphic()).getChildren().get(0);
        epicOfficialTabCheckbox.setSelected(false);
        CheckBox epicCustomTabCheckbox = (CheckBox) ((HBox) epicCustomMapsTab.getGraphic()).getChildren().get(0);
        epicCustomTabCheckbox.setSelected(false);

        steamCustomMapsFlowPane.getChildren().clear();
        steamOfficialMapsFlowPane.getChildren().clear();
        epicOfficialMapsFlowPane.getChildren().clear();
        epicCustomMapsFlowPane.getChildren().clear();

        if (EnumSortedMapsCriteria.ALIAS_DESC.equals(Session.getInstance().getSortedMapsCriteria())) {
            steamPlatformProfileMapDtoList = steamPlatformProfileMapDtoList.stream().sorted((pm1, pm2) -> pm1.getAlias().compareTo(pm2.getAlias())).collect(Collectors.toList());
            epicPlatformProfileMapDtoList = epicPlatformProfileMapDtoList.stream().sorted((pm1, pm2) -> pm1.getAlias().compareTo(pm2.getAlias())).collect(Collectors.toList());
            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.ALIAS_ASC);
        } else {
            steamPlatformProfileMapDtoList = steamPlatformProfileMapDtoList.stream().sorted((pm1, pm2) -> pm2.getAlias().compareTo(pm1.getAlias())).collect(Collectors.toList());
            epicPlatformProfileMapDtoList = epicPlatformProfileMapDtoList.stream().sorted((pm1, pm2) -> pm2.getAlias().compareTo(pm1.getAlias())).collect(Collectors.toList());
            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.ALIAS_DESC);
        }

        for (PlatformProfileMapDto platformProfileMapDto : steamPlatformProfileMapDtoList) {
            Node node = createMapNode(platformProfileMapDto);
            if (platformProfileMapDto.getMapDto().isOfficial()) {
                steamOfficialMapsFlowPane.getChildren().add(node);
            } else {
                steamCustomMapsFlowPane.getChildren().add(node);
            }
        }

        for (PlatformProfileMapDto platformProfileMapDto : epicPlatformProfileMapDtoList) {
            Node node = createMapNode(platformProfileMapDto);
            if (platformProfileMapDto.getMapDto().isOfficial()) {
                epicOfficialMapsFlowPane.getChildren().add(node);
            } else {
                epicCustomMapsFlowPane.getChildren().add(node);
            }
        }
    }

    @FXML
    private void orderMapsByNameOnAction() {
        orderType = EnumCardOrderType.BY_NAME;

        CheckBox steamOfficialTabCheckbox = (CheckBox) ((HBox) steamOfficialMapsTab.getGraphic()).getChildren().get(0);
        steamOfficialTabCheckbox.setSelected(false);
        CheckBox steamCustomTabCheckbox = (CheckBox) ((HBox) steamCustomMapsTab.getGraphic()).getChildren().get(0);
        steamCustomTabCheckbox.setSelected(false);
        CheckBox epicOfficialTabCheckbox = (CheckBox) ((HBox) epicOfficialMapsTab.getGraphic()).getChildren().get(0);
        epicOfficialTabCheckbox.setSelected(false);
        CheckBox epicCustomTabCheckbox = (CheckBox) ((HBox) epicCustomMapsTab.getGraphic()).getChildren().get(0);
        epicCustomTabCheckbox.setSelected(false);

        steamOfficialMapsFlowPane.getChildren().clear();
        steamCustomMapsFlowPane.getChildren().clear();
        epicOfficialMapsFlowPane.getChildren().clear();
        epicCustomMapsFlowPane.getChildren().clear();

        if (EnumSortedMapsCriteria.NAME_DESC.equals(Session.getInstance().getSortedMapsCriteria())) {
            steamPlatformProfileMapDtoList = steamPlatformProfileMapDtoList.stream().sorted((pm1, pm2) -> pm1.getMapDto().getKey().compareTo(pm2.getMapDto().getKey())).collect(Collectors.toList());
            epicPlatformProfileMapDtoList = epicPlatformProfileMapDtoList.stream().sorted((pm1, pm2) -> pm1.getMapDto().getKey().compareTo(pm2.getMapDto().getKey())).collect(Collectors.toList());
            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.NAME_ASC);
        } else {
            steamPlatformProfileMapDtoList = steamPlatformProfileMapDtoList.stream().sorted((pm1, pm2) -> pm2.getMapDto().getKey().compareTo(pm1.getMapDto().getKey())).collect(Collectors.toList());
            epicPlatformProfileMapDtoList = epicPlatformProfileMapDtoList.stream().sorted((pm1, pm2) -> pm2.getMapDto().getKey().compareTo(pm1.getMapDto().getKey())).collect(Collectors.toList());
            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.NAME_DESC);
        }

        for (PlatformProfileMapDto platformProfileMapDto : steamPlatformProfileMapDtoList) {
            Node node = createMapNode(platformProfileMapDto);
                                         if (platformProfileMapDto.getMapDto().isOfficial()) {
                steamOfficialMapsFlowPane.getChildren().add(node);
            } else {
                steamCustomMapsFlowPane.getChildren().add(node);
            }
        }

        for (PlatformProfileMapDto platformProfileMapDto : epicPlatformProfileMapDtoList) {
            Node node = createMapNode(platformProfileMapDto);
            if (platformProfileMapDto.getMapDto().isOfficial()) {
                epicOfficialMapsFlowPane.getChildren().add(node);
            } else {
                epicCustomMapsFlowPane.getChildren().add(node);
            }
        }
    }

     @FXML
    private void orderMapsByReleaseDateOnAction() {
        orderType = EnumCardOrderType.BY_RELEASE_DATE;

         CheckBox steamOfficialTabCheckbox = (CheckBox) ((HBox) steamOfficialMapsTab.getGraphic()).getChildren().get(0);
         steamOfficialTabCheckbox.setSelected(false);
         CheckBox steamCustomTabCheckbox = (CheckBox) ((HBox) steamCustomMapsTab.getGraphic()).getChildren().get(0);
         steamCustomTabCheckbox.setSelected(false);
         CheckBox epicOfficialTabCheckbox = (CheckBox) ((HBox) epicOfficialMapsTab.getGraphic()).getChildren().get(0);
         epicOfficialTabCheckbox.setSelected(false);
         CheckBox epicCustomTabCheckbox = (CheckBox) ((HBox) epicCustomMapsTab.getGraphic()).getChildren().get(0);
         epicCustomTabCheckbox.setSelected(false);

        steamCustomMapsFlowPane.getChildren().clear();
        steamOfficialMapsFlowPane.getChildren().clear();
        epicOfficialMapsFlowPane.getChildren().clear();
        epicCustomMapsFlowPane.getChildren().clear();

        if (EnumSortedMapsCriteria.RELEASE_DATE_DESC.equals(Session.getInstance().getSortedMapsCriteria())) {
            List<PlatformProfileMapDto> steamSortedMapList = new ArrayList<PlatformProfileMapDto>(
                    steamPlatformProfileMapDtoList.stream()
                            .filter(pm -> pm.getReleaseDate() != null)
                            .sorted((pm1, pm2) -> pm1.getReleaseDate().compareTo(pm2.getReleaseDate()))
                            .collect(Collectors.toList())

            );
            List<PlatformProfileMapDto> steamMapWithoutReleaseDateList = new ArrayList<PlatformProfileMapDto>(
                    steamPlatformProfileMapDtoList.stream()
                            .filter(pm -> pm.getReleaseDate() == null)
                            .collect(Collectors.toList())
            );
            List<PlatformProfileMapDto> epicSortedMapList = new ArrayList<PlatformProfileMapDto>(
                    epicPlatformProfileMapDtoList.stream()
                            .filter(pm -> pm.getReleaseDate() != null)
                            .sorted((pm1, pm2) -> pm1.getReleaseDate().compareTo(pm2.getReleaseDate()))
                            .collect(Collectors.toList())

            );
            List<PlatformProfileMapDto> epicMapWithoutReleaseDateList = new ArrayList<PlatformProfileMapDto>(
                    epicPlatformProfileMapDtoList.stream()
                            .filter(pm -> pm.getReleaseDate() == null)
                            .collect(Collectors.toList())
            );

            steamSortedMapList.addAll(steamMapWithoutReleaseDateList);
            steamPlatformProfileMapDtoList = steamSortedMapList;

            epicSortedMapList.addAll(epicMapWithoutReleaseDateList);
            epicPlatformProfileMapDtoList = epicSortedMapList;

            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.RELEASE_DATE_ASC);
        } else {
            List<PlatformProfileMapDto> steamSortedMapList = new ArrayList<PlatformProfileMapDto>(
                    steamPlatformProfileMapDtoList.stream()
                            .filter(pm -> pm.getReleaseDate() != null)
                            .sorted((pm1, pm2) -> pm2.getReleaseDate().compareTo(pm1.getReleaseDate()))
                            .collect(Collectors.toList())
            );
            List<PlatformProfileMapDto> steamMapWithoutReleaseDateList = new ArrayList<PlatformProfileMapDto>(
                    steamPlatformProfileMapDtoList.stream()
                            .filter(pm -> pm.getReleaseDate() == null)
                            .collect(Collectors.toList())
            );
            List<PlatformProfileMapDto> epicSortedMapList = new ArrayList<PlatformProfileMapDto>(
                    epicPlatformProfileMapDtoList.stream()
                            .filter(pm -> pm.getReleaseDate() != null)
                            .sorted((pm1, pm2) -> pm2.getReleaseDate().compareTo(pm1.getReleaseDate()))
                            .collect(Collectors.toList())
            );
            List<PlatformProfileMapDto> epicMapWithoutReleaseDateList = new ArrayList<PlatformProfileMapDto>(
                    epicPlatformProfileMapDtoList.stream()
                            .filter(pm -> pm.getReleaseDate() == null)
                            .collect(Collectors.toList())
            );

            steamSortedMapList.addAll(steamMapWithoutReleaseDateList);
            steamPlatformProfileMapDtoList = steamSortedMapList;

            epicSortedMapList.addAll(epicMapWithoutReleaseDateList);
            epicPlatformProfileMapDtoList = epicSortedMapList;

            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.RELEASE_DATE_DESC);
        }

        for (PlatformProfileMapDto platformProfileMapDto : steamPlatformProfileMapDtoList) {
            Node node = createMapNode(platformProfileMapDto);
            if (platformProfileMapDto.getMapDto().isOfficial()) {
                steamOfficialMapsFlowPane.getChildren().add(node);
            } else {
                steamCustomMapsFlowPane.getChildren().add(node);
            }
        }

         for (PlatformProfileMapDto platformProfileMapDto : epicPlatformProfileMapDtoList) {
             Node node = createMapNode(platformProfileMapDto);
             if (platformProfileMapDto.getMapDto().isOfficial()) {
                 epicOfficialMapsFlowPane.getChildren().add(node);
             } else {
                 epicCustomMapsFlowPane.getChildren().add(node);
             }
         }
    }

    @FXML
    private void orderMapsByImportedDateOnAction() {
        orderType = EnumCardOrderType.BY_IMPORTED_DATE;

        CheckBox steamOfficialTabCheckbox = (CheckBox) ((HBox) steamOfficialMapsTab.getGraphic()).getChildren().get(0);
        steamOfficialTabCheckbox.setSelected(false);
        CheckBox steamCustomTabCheckbox = (CheckBox) ((HBox) steamCustomMapsTab.getGraphic()).getChildren().get(0);
        steamCustomTabCheckbox.setSelected(false);
        CheckBox epicOfficialTabCheckbox = (CheckBox) ((HBox) epicOfficialMapsTab.getGraphic()).getChildren().get(0);
        epicOfficialTabCheckbox.setSelected(false);
        CheckBox epicCustomTabCheckbox = (CheckBox) ((HBox) epicCustomMapsTab.getGraphic()).getChildren().get(0);
        epicCustomTabCheckbox.setSelected(false);

        steamCustomMapsFlowPane.getChildren().clear();
        steamOfficialMapsFlowPane.getChildren().clear();
        epicCustomMapsFlowPane.getChildren().clear();
        epicOfficialMapsFlowPane.getChildren().clear();

        if (EnumSortedMapsCriteria.IMPORTED_DATE_DESC.equals(Session.getInstance().getSortedMapsCriteria())) {
            steamPlatformProfileMapDtoList = steamPlatformProfileMapDtoList.stream().sorted((pm1, pm2) -> pm1.getImportedDate().compareTo(pm2.getImportedDate())).collect(Collectors.toList());
            epicPlatformProfileMapDtoList = epicPlatformProfileMapDtoList.stream().sorted((pm1, pm2) -> pm1.getImportedDate().compareTo(pm2.getImportedDate())).collect(Collectors.toList());
            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.IMPORTED_DATE_ASC);
        } else {
            steamPlatformProfileMapDtoList = steamPlatformProfileMapDtoList.stream().sorted((pm1, pm2) -> pm2.getImportedDate().compareTo(pm1.getImportedDate())).collect(Collectors.toList());
            epicPlatformProfileMapDtoList = epicPlatformProfileMapDtoList.stream().sorted((pm1, pm2) -> pm2.getImportedDate().compareTo(pm1.getImportedDate())).collect(Collectors.toList());
            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.IMPORTED_DATE_DESC);
        }

        for (PlatformProfileMapDto platformProfileMapDto : steamPlatformProfileMapDtoList) {
            Node node = createMapNode(platformProfileMapDto);
            if (platformProfileMapDto.getMapDto().isOfficial()) {
                steamOfficialMapsFlowPane.getChildren().add(node);
            } else {
                steamCustomMapsFlowPane.getChildren().add(node);
            }
        }

        for (PlatformProfileMapDto platformProfileMapDto : epicPlatformProfileMapDtoList) {
            Node node = createMapNode(platformProfileMapDto);
            if (platformProfileMapDto.getMapDto().isOfficial()) {
                epicOfficialMapsFlowPane.getChildren().add(node);
            } else {
                epicCustomMapsFlowPane.getChildren().add(node);
            }
        }
    }

    @FXML
    private void orderMapsByDownloadOnAction() {
        orderType = EnumCardOrderType.BY_DOWNLOAD;

        CheckBox steamOfficialTabCheckbox = (CheckBox) ((HBox) steamOfficialMapsTab.getGraphic()).getChildren().get(0);
        steamOfficialTabCheckbox.setSelected(false);
        CheckBox steamCustomTabCheckbox = (CheckBox) ((HBox) steamCustomMapsTab.getGraphic()).getChildren().get(0);
        steamCustomTabCheckbox.setSelected(false);
        CheckBox epicOfficialTabCheckbox = (CheckBox) ((HBox) epicOfficialMapsTab.getGraphic()).getChildren().get(0);
        epicOfficialTabCheckbox.setSelected(false);
        CheckBox epicCustomTabCheckbox = (CheckBox) ((HBox) epicCustomMapsTab.getGraphic()).getChildren().get(0);
        epicCustomTabCheckbox.setSelected(false);

        steamOfficialMapsFlowPane.getChildren().clear();
        epicOfficialMapsFlowPane.getChildren().clear();
        steamCustomMapsFlowPane.getChildren().clear();
        epicCustomMapsFlowPane.getChildren().clear();

        List<PlatformProfileMapDto> steamOfficialPlatformProfileMapDtoList = steamPlatformProfileMapDtoList.stream().
                filter(ppm -> ppm.getMapDto().isOfficial()).
                collect(Collectors.toList());
        List<PlatformProfileMapDto> epicOfficialPlatformProfileMapDtoList = epicPlatformProfileMapDtoList.stream().
                filter(ppm -> ppm.getMapDto().isOfficial()).
                collect(Collectors.toList());

        List<PlatformProfileMapDto> steamCustomPlatformProfileMapDtoList = new ArrayList<PlatformProfileMapDto>();
        List<PlatformProfileMapDto> epicCustomPlatformProfileMapDtoList = new ArrayList<PlatformProfileMapDto>();

        if (EnumSortedMapsCriteria.DOWNLOAD_DESC.equals(Session.getInstance().getSortedMapsCriteria())) {
            steamCustomPlatformProfileMapDtoList = steamPlatformProfileMapDtoList.stream().
                    filter(ppm -> !ppm.getMapDto().isOfficial()).
                    sorted((ppm1, ppm2) -> {
                return ppm1.isDownloaded().compareTo(ppm2.isDownloaded());
            }).collect(Collectors.toList());
            epicCustomPlatformProfileMapDtoList = epicPlatformProfileMapDtoList.stream().
                    filter(ppm -> !ppm.getMapDto().isOfficial()).
                    sorted((ppm1, ppm2) -> {
                        return ppm1.isDownloaded().compareTo(ppm2.isDownloaded());
                    }).collect(Collectors.toList());

            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.DOWNLOAD_ASC);
        } else {
            steamCustomPlatformProfileMapDtoList = steamPlatformProfileMapDtoList.stream().
                    filter(ppm -> !ppm.getMapDto().isOfficial()).
                    sorted((ppm1, ppm2) -> {
                    return ppm2.isDownloaded().compareTo(ppm1.isDownloaded());
            }).collect(Collectors.toList());
            epicCustomPlatformProfileMapDtoList = epicPlatformProfileMapDtoList.stream().
                    filter(ppm -> !ppm.getMapDto().isOfficial()).
                    sorted((ppm1, ppm2) -> {
                        Boolean map1Downloaded = ppm1.isDownloaded();
                        Boolean map2Downloaded = ppm2.isDownloaded();
                        return ppm2.isDownloaded().compareTo(ppm1.isDownloaded());
                    }).collect(Collectors.toList());

            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.DOWNLOAD_DESC);
        }

        for (PlatformProfileMapDto platformProfileMapDto : steamOfficialPlatformProfileMapDtoList) {
            Node node = createMapNode(platformProfileMapDto);
            steamOfficialMapsFlowPane.getChildren().add(node);
        }
        for (PlatformProfileMapDto platformProfileMapDto : epicOfficialPlatformProfileMapDtoList) {
            Node node = createMapNode(platformProfileMapDto);
            epicOfficialMapsFlowPane.getChildren().add(node);
        }
        for (PlatformProfileMapDto platformProfileMapDto : steamCustomPlatformProfileMapDtoList) {
            Node node = createMapNode(platformProfileMapDto);
            steamCustomMapsFlowPane.getChildren().add(node);
        }
        for (PlatformProfileMapDto platformProfileMapDto : epicCustomPlatformProfileMapDtoList) {
            Node node = createMapNode(platformProfileMapDto);
            epicCustomMapsFlowPane.getChildren().add(node);
        }
    }

    private List<PlatformProfileMapDto> getSteamEditList() throws Exception {

        List<PlatformProfileMapDto> editList = new ArrayList<PlatformProfileMapDto>();
        ObservableList<Node> nodeList = FXCollections.concat(steamOfficialMapsFlowPane.getChildren(), steamCustomMapsFlowPane.getChildren());
        String mapNameText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.name");

        if (nodeList != null && !nodeList.isEmpty()) {
            for (Node node : nodeList) {
                CardNode cardNode = new CardNode(cardOrientation, node);

                if (cardNode.getCheckbox().isSelected()) {
                    String mapName = cardNode.getMapNameLabel().getText().replace(mapNameText + ": ", "");
                    Optional<PlatformProfileMapDto> profileMapOptional = facade.findPlatformProfileMapDtoByName(
                            EnumPlatform.STEAM.name(),
                            profileSelect.getValue().getName(),
                            mapName
                    );
                    if (profileMapOptional.isPresent()) {
                        editList.add(profileMapOptional.get());
                    }
                }
            }
        }

        if (!editList.isEmpty() && !facade.isCorrectInstallationFolder(EnumPlatform.STEAM.name())) {
            String errorMessage = "Invalid Steam installation folder was found. Define in Install/Update section.";
            throw new RuntimeException(errorMessage);
        }

        return editList;
    }

    private List<PlatformProfileMapDto> getEpicEditList() throws Exception {

        List<PlatformProfileMapDto> editList = new ArrayList<PlatformProfileMapDto>();
        ObservableList<Node> nodeList = FXCollections.concat(epicOfficialMapsFlowPane.getChildren(), epicCustomMapsFlowPane.getChildren());
        String mapNameText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.name");

        if (nodeList != null && !nodeList.isEmpty()) {
            for (Node node : nodeList) {
                CardNode cardNode = new CardNode(cardOrientation, node);

                if (cardNode.getCheckbox().isSelected()) {
                    String mapName = cardNode.getMapNameLabel().getText().replace(mapNameText + ": ", "");

                    Optional<PlatformProfileMapDto> profileMapOptional = facade.findPlatformProfileMapDtoByName(
                            EnumPlatform.EPIC.name(),
                            profileSelect.getValue().getName(),
                            mapName
                    );
                    if (profileMapOptional.isPresent()) {
                        editList.add(profileMapOptional.get());
                    }
                }
            }
        }

        if (!editList.isEmpty() && !facade.isCorrectInstallationFolder(EnumPlatform.EPIC.name())) {
            String errorMessage = "Invalid Epic Games installation folder was found. Define in Install/Update section.";
            throw new RuntimeException(errorMessage);
        }

        return editList;
    }

    @FXML
    private void editMapsOnAction() {
        try {
            List<PlatformProfileMapDto> editList = getSteamEditList();
            editList.addAll(getEpicEditList());

            if (editList.isEmpty()) {
                logger.warn("No selected maps/mods to edit. You must select at least one item to be editted");
                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapsNotSelected");
                String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectItems");
                Utils.warningDialog(headerText, contentText);

            } else {
                Session.getInstance().setPlatformProfileMapList(editList);
                loadNewContent("/views/mapEdition.fxml");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void addToMapsCycleOnAction() {
        try {
            CheckBox steamOfficialTabCheckbox = (CheckBox) ((HBox) steamOfficialMapsTab.getGraphic()).getChildren().get(0);
            steamOfficialTabCheckbox.setSelected(false);
            StringBuffer message = new StringBuffer();
            List<Node> steamOfficialAddMapsCycleList = new ArrayList<Node>();
            for (Node node : steamOfficialMapsFlowPane.getChildren()) {
                Node selectedNode = getNodeIfSelected(node, message);
                if (selectedNode != null) {
                    steamOfficialAddMapsCycleList.add(selectedNode);
                }
            }

            CheckBox steamCustomTabCheckbox = (CheckBox) ((HBox) steamCustomMapsTab.getGraphic()).getChildren().get(0);
            steamCustomTabCheckbox.setSelected(false);
            List<Node> steamCustomAddMapsCycleList = new ArrayList<Node>();
            for (Node node : steamCustomMapsFlowPane.getChildren()) {
                Node selectedNode = getNodeIfSelected(node, message);
                if (selectedNode != null) {
                    steamCustomAddMapsCycleList.add(selectedNode);
                }
            }

            CheckBox epicOfficialTabCheckbox = (CheckBox) ((HBox) epicOfficialMapsTab.getGraphic()).getChildren().get(0);
            epicOfficialTabCheckbox.setSelected(false);
            List<Node> epicOfficialAddMapsCycleList = new ArrayList<Node>();
            for (Node node : epicOfficialMapsFlowPane.getChildren()) {
                Node selectedNode = getNodeIfSelected(node, message);
                if (selectedNode != null) {
                    epicOfficialAddMapsCycleList.add(selectedNode);
                }
            }

            CheckBox epicCustomTabCheckbox = (CheckBox) ((HBox) epicCustomMapsTab.getGraphic()).getChildren().get(0);
            epicCustomTabCheckbox.setSelected(false);
            List<Node> epicCustomAddMapsCycleList = new ArrayList<Node>();
            for (Node node : epicCustomMapsFlowPane.getChildren()) {
                Node selectedNode = getNodeIfSelected(node, message);
                if (selectedNode != null) {
                    epicCustomAddMapsCycleList.add(selectedNode);
                }
            }

            if (steamOfficialAddMapsCycleList.isEmpty() && steamCustomAddMapsCycleList.isEmpty() && epicOfficialAddMapsCycleList.isEmpty() && epicCustomAddMapsCycleList.isEmpty()) {
                logger.warn("No selected maps to add to maps cycle. You must select at least one item to be added");
                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapsNotSelected");
                String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectItems");
                Utils.warningDialog(headerText, contentText);

            } else {
                String mapNameText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.name");

                List<String> steamOfficialMapNameListToAddFromMapsCycle = steamOfficialAddMapsCycleList.stream().
                        map(node -> {
                            CardNode cardNode = new CardNode(cardOrientation, node);
                            return cardNode.getMapNameLabel().getText().replace(mapNameText + ": ", "");
                        }).
                        collect(Collectors.toList());

                List<String> steamCustomMapNameListToAddFromMapsCycle = steamCustomAddMapsCycleList.stream().
                        map(node -> {
                            CardNode cardNode = new CardNode(cardOrientation, node);
                            return cardNode.getMapNameLabel().getText().replace(mapNameText + ": ", "");
                        }).
                        collect(Collectors.toList());

                List<String> epicOfficialMapNameListToAddFromMapsCycle = epicOfficialAddMapsCycleList.stream().
                        map(node -> {
                            CardNode cardNode = new CardNode(cardOrientation, node);
                            return cardNode.getMapNameLabel().getText().replace(mapNameText + ": ", "");
                        }).
                        collect(Collectors.toList());

                List<String> epicCustomMapNameListToAddFromMapsCycle = epicCustomAddMapsCycleList.stream().
                        map(node -> {
                            CardNode cardNode = new CardNode(cardOrientation, node);
                            return cardNode.getMapNameLabel().getText().replace(mapNameText + ": ", "");
                        }).
                        collect(Collectors.toList());

                facade.updateMapsCycleFlagInMapList(
                        profileSelect.getValue().getName(),
                        steamOfficialMapNameListToAddFromMapsCycle,
                        steamCustomMapNameListToAddFromMapsCycle,
                        epicOfficialMapNameListToAddFromMapsCycle,
                        epicCustomMapNameListToAddFromMapsCycle,
                        true
                );

                for (Node node : steamOfficialAddMapsCycleList) {
                    setMapInMapsCycle(node, true);
                }
                for (Node node : steamCustomAddMapsCycleList) {
                    setMapInMapsCycle(node, true);
                }
                for (Node node : epicOfficialAddMapsCycleList) {
                    setMapInMapsCycle(node, true);
                }
                for (Node node : epicCustomAddMapsCycleList) {
                    setMapInMapsCycle(node, true);
                }

                ListPlatformProfileMapFacadeResult listPlatformProfileMapFacadeResult = facade.getPlatformProfileMapList(profileSelect.getValue().getName());
                steamPlatformProfileMapDtoList.clear();
                steamPlatformProfileMapDtoList = listPlatformProfileMapFacadeResult.getSteamPlatformProfileMapDtoList();
                epicPlatformProfileMapDtoList.clear();
                epicPlatformProfileMapDtoList = listPlatformProfileMapFacadeResult.getEpicPlatformProfileMapDtoList();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void removeFromMapsCycleOnAction() {
        try {
            CheckBox steamOfficialTabCheckbox = (CheckBox) ((HBox) steamOfficialMapsTab.getGraphic()).getChildren().get(0);
            steamOfficialTabCheckbox.setSelected(false);
            StringBuffer message = new StringBuffer();
            List<Node> steamOfficialRemoveMapsCycleList = new ArrayList<Node>();
            for (Node node : steamOfficialMapsFlowPane.getChildren()) {
                Node selectedNode = getNodeIfSelected(node, message);
                if (selectedNode != null) {
                    steamOfficialRemoveMapsCycleList.add(selectedNode);
                }
            }

            CheckBox steamCustomTabCheckbox = (CheckBox) ((HBox) steamCustomMapsTab.getGraphic()).getChildren().get(0);
            steamCustomTabCheckbox.setSelected(false);
            List<Node> steamCustomRemoveMapsCycleList = new ArrayList<Node>();
            for (Node node : steamCustomMapsFlowPane.getChildren()) {
                Node selectedNode = getNodeIfSelected(node, message);
                if (selectedNode != null) {
                    steamCustomRemoveMapsCycleList.add(selectedNode);
                }
            }

            CheckBox epicOfficialTabCheckbox = (CheckBox) ((HBox) epicOfficialMapsTab.getGraphic()).getChildren().get(0);
            epicOfficialTabCheckbox.setSelected(false);
            List<Node> epicOfficialRemoveMapsCycleList = new ArrayList<Node>();
            for (Node node : epicOfficialMapsFlowPane.getChildren()) {
                Node selectedNode = getNodeIfSelected(node, message);
                if (selectedNode != null) {
                    epicOfficialRemoveMapsCycleList.add(selectedNode);
                }
            }

            CheckBox epicCustomTabCheckbox = (CheckBox) ((HBox) epicCustomMapsTab.getGraphic()).getChildren().get(0);
            epicCustomTabCheckbox.setSelected(false);
            List<Node> epicCustomRemoveMapsCycleList = new ArrayList<Node>();
            for (Node node : epicCustomMapsFlowPane.getChildren()) {
                Node selectedNode = getNodeIfSelected(node, message);
                if (selectedNode != null) {
                    epicCustomRemoveMapsCycleList.add(selectedNode);
                }
            }

            if (steamOfficialRemoveMapsCycleList.isEmpty() && steamCustomRemoveMapsCycleList.isEmpty() && epicOfficialRemoveMapsCycleList.isEmpty() && epicCustomRemoveMapsCycleList.isEmpty()) {
                logger.warn("No selected maps to delete from maps cycle. You must select at least one item to be deleted");
                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapsNotSelected");
                String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectItems");
                Utils.warningDialog(headerText, contentText);

            } else {
                String mapNameText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.name");

                List<String> steamOfficialMapNameListToRemoveFromMapsCycle = steamOfficialRemoveMapsCycleList.stream().
                        map(node -> {
                            CardNode cardNode = new CardNode(cardOrientation, node);
                            return cardNode.getMapNameLabel().getText().replace(mapNameText + ": ", "");
                        }).
                        collect(Collectors.toList());

                List<String> steamCustomMapNameListToRemoveFromMapsCycle = steamCustomRemoveMapsCycleList.stream().
                        map(node -> {
                            CardNode cardNode = new CardNode(cardOrientation, node);
                            return cardNode.getMapNameLabel().getText().replace(mapNameText + ": ", "");
                        }).
                        collect(Collectors.toList());

                List<String> epicOfficialMapNameListToRemoveFromMapsCycle = epicOfficialRemoveMapsCycleList.stream().
                        map(node -> {
                            CardNode cardNode = new CardNode(cardOrientation, node);
                            return cardNode.getMapNameLabel().getText().replace(mapNameText + ": ", "");
                        }).
                        collect(Collectors.toList());

                List<String> epicCustomMapNameListToRemoveFromMapsCycle = epicCustomRemoveMapsCycleList.stream().
                        map(node -> {
                            CardNode cardNode = new CardNode(cardOrientation, node);
                            return cardNode.getMapNameLabel().getText().replace(mapNameText + ": ", "");
                        }).
                        collect(Collectors.toList());

                facade.updateMapsCycleFlagInMapList(
                        profileSelect.getValue().getName(),
                        steamOfficialMapNameListToRemoveFromMapsCycle,
                        steamCustomMapNameListToRemoveFromMapsCycle,
                        epicOfficialMapNameListToRemoveFromMapsCycle,
                        epicCustomMapNameListToRemoveFromMapsCycle,
                        false
                );

                for (Node node : steamOfficialRemoveMapsCycleList) {
                    setMapInMapsCycle(node, false);
                }
                for (Node node : steamCustomRemoveMapsCycleList) {
                    setMapInMapsCycle(node, false);
                }
                for (Node node : epicOfficialRemoveMapsCycleList) {
                    setMapInMapsCycle(node, false);
                }
                for (Node node : epicCustomRemoveMapsCycleList) {
                    setMapInMapsCycle(node, false);
                }

                ListPlatformProfileMapFacadeResult listPlatformProfileMapFacadeResult = facade.getPlatformProfileMapList(profileSelect.getValue().getName());
                steamPlatformProfileMapDtoList.clear();
                steamPlatformProfileMapDtoList = listPlatformProfileMapFacadeResult.getSteamPlatformProfileMapDtoList();
                epicPlatformProfileMapDtoList.clear();
                epicPlatformProfileMapDtoList = listPlatformProfileMapFacadeResult.getEpicPlatformProfileMapDtoList();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }


    private void setMapInMapsCycle(Node node, boolean inMapCycle) throws Exception {

        CardNode cardNode = new CardNode(cardOrientation, node);
        cardNode.getCheckbox().setSelected(false);

        try {
            String isInMapsCycleText = StringUtils.EMPTY;
            String color = StringUtils.EMPTY;
            if (inMapCycle) {
                try {
                    isInMapsCycleText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.inMapsCycle");
                } catch (Exception e) {
                    isInMapsCycleText = "IN MAPS CYCLE";
                }
                color = "#ef2828";
            } else {
                try {
                    isInMapsCycleText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.notInMapsCycle");
                } catch (Exception e) {
                    isInMapsCycleText = "NOT IN MAPS CYCLE";
                }
                color = "grey";
            }

            cardNode.getIsInMapsCycleLabel().setText(isInMapsCycleText);
            cardNode.getIsInMapsCycleLabel().setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold; -fx-padding: 3; -fx-border-color: " + color + "; -fx-border-radius: 5;");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @FXML
    private void orderMapsByMapsCycleOnAction() {
        orderType = EnumCardOrderType.BY_MAPS_CYCLE;

        CheckBox steamOfficialTabCheckbox = (CheckBox) ((HBox) steamOfficialMapsTab.getGraphic()).getChildren().get(0);
        steamOfficialTabCheckbox.setSelected(false);
        CheckBox steamCustomTabCheckbox = (CheckBox) ((HBox) steamCustomMapsTab.getGraphic()).getChildren().get(0);
        steamCustomTabCheckbox.setSelected(false);
        CheckBox epicOfficialTabCheckbox = (CheckBox) ((HBox) epicOfficialMapsTab.getGraphic()).getChildren().get(0);
        epicOfficialTabCheckbox.setSelected(false);
        CheckBox epicCustomTabCheckbox = (CheckBox) ((HBox) epicCustomMapsTab.getGraphic()).getChildren().get(0);
        epicCustomTabCheckbox.setSelected(false);

        steamCustomMapsFlowPane.getChildren().clear();
        steamOfficialMapsFlowPane.getChildren().clear();
        epicCustomMapsFlowPane.getChildren().clear();
        epicOfficialMapsFlowPane.getChildren().clear();

        if (EnumSortedMapsCriteria.MAPS_CYCLE_DESC.equals(Session.getInstance().getSortedMapsCriteria())) {
            steamPlatformProfileMapDtoList = steamPlatformProfileMapDtoList.stream().sorted((pm1, pm2) -> pm1.isInMapsCycle().compareTo(pm2.isInMapsCycle())).collect(Collectors.toList());
            epicPlatformProfileMapDtoList = epicPlatformProfileMapDtoList.stream().sorted((pm1, pm2) -> pm1.isInMapsCycle().compareTo(pm2.isInMapsCycle())).collect(Collectors.toList());
            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.MAPS_CYCLE_ASC);
        } else {
            steamPlatformProfileMapDtoList = steamPlatformProfileMapDtoList.stream().sorted((pm1, pm2) -> pm2.isInMapsCycle().compareTo(pm1.isInMapsCycle())).collect(Collectors.toList());
            epicPlatformProfileMapDtoList = epicPlatformProfileMapDtoList.stream().sorted((pm1, pm2) -> pm2.isInMapsCycle().compareTo(pm1.isInMapsCycle())).collect(Collectors.toList());
            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.MAPS_CYCLE_DESC);
        }

        for (PlatformProfileMapDto platformProfileMapDto : steamPlatformProfileMapDtoList) {
            Node node = createMapNode(platformProfileMapDto);
            if (platformProfileMapDto.getMapDto().isOfficial()) {
                steamOfficialMapsFlowPane.getChildren().add(node);
            } else {
                steamCustomMapsFlowPane.getChildren().add(node);
            }
        }

        for (PlatformProfileMapDto platformProfileMapDto : epicPlatformProfileMapDtoList) {
            Node node = createMapNode(platformProfileMapDto);
            if (platformProfileMapDto.getMapDto().isOfficial()) {
                epicOfficialMapsFlowPane.getChildren().add(node);
            } else {
                epicCustomMapsFlowPane.getChildren().add(node);
            }
        }
    }

    @FXML
    private void downloadMapsOnAction() {
        try {
            CheckBox steamCustomTabCheckbox = (CheckBox) ((HBox) steamCustomMapsTab.getGraphic()).getChildren().get(0);
            steamCustomTabCheckbox.setSelected(false);
            StringBuffer message = new StringBuffer();
            List<Node> steamCustomMapToDownloadList = new ArrayList<Node>();
            for (Node node : steamCustomMapsFlowPane.getChildren()) {
                Node selectedNode = getNodeIfSelected(node, message);
                if (selectedNode != null) {
                    steamCustomMapToDownloadList.add(selectedNode);
                }
            }

            CheckBox epicCustomTabCheckbox = (CheckBox) ((HBox) epicCustomMapsTab.getGraphic()).getChildren().get(0);
            epicCustomTabCheckbox.setSelected(false);
            List<Node> epicCustomMapToDownloadList = new ArrayList<Node>();
            for (Node node : epicCustomMapsFlowPane.getChildren()) {
                Node selectedNode = getNodeIfSelected(node, message);
                if (selectedNode != null) {
                    epicCustomMapToDownloadList.add(selectedNode);
                }
            }

            if (steamCustomMapToDownloadList.isEmpty() && epicCustomMapToDownloadList.isEmpty()) {
                logger.warn("No selected maps to download. You must select at least one item to be downloaded");
                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapsNotSelected");
                String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectItems");
                Utils.warningDialog(headerText, contentText);

            } else {
                progressIndicator.setVisible(true);
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        steamCustomMapToDownloadList.stream().forEach(node ->{
                            CardNode cardNode = new CardNode(cardOrientation, node);
                            Event.fireEvent(cardNode.getClickToDownloadMapLink(), new MouseEvent(MouseEvent.MOUSE_CLICKED,
                                    cardNode.getClickToDownloadMapLink().getLayoutX(), cardNode.getClickToDownloadMapLink().getLayoutY(), cardNode.getClickToDownloadMapLink().getLayoutX(), cardNode.getClickToDownloadMapLink().getLayoutY(), MouseButton.PRIMARY, 1,
                                    true, true, true, true, true, true, true, true, true, true, null));

                            cardNode.getCheckbox().setSelected(false);
                        });
                        epicCustomMapToDownloadList.stream().forEach(node ->{
                            CardNode cardNode = new CardNode(cardOrientation, node);
                            Event.fireEvent(cardNode.getClickToDownloadMapLink(), new MouseEvent(MouseEvent.MOUSE_CLICKED,
                                    cardNode.getClickToDownloadMapLink().getLayoutX(), cardNode.getClickToDownloadMapLink().getLayoutY(), cardNode.getClickToDownloadMapLink().getLayoutX(), cardNode.getClickToDownloadMapLink().getLayoutY(), MouseButton.PRIMARY, 1,
                                    true, true, true, true, true, true, true, true, true, true, null));

                            cardNode.getCheckbox().setSelected(false);
                        });
                        return null;
                    }
                };

                task.setOnSucceeded(wse -> {
                    progressIndicator.setVisible(false);
                });
                task.setOnFailed(wse -> {
                    progressIndicator.setVisible(false);
                });
                Thread thread = new Thread(task);
                thread.start();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void cardDownOnAction() {
        try {
            cardOrientation = EnumCardOrientation.DOWN;
            facade.setConfigPropertyValue("prop.config.lastUsedView", cardOrientation.name());
            if (!profileSelect.getItems().isEmpty()) {
                switch (orderType) {
                    case BY_ALIAS: orderMapsByAliasOnAction(); break;
                    case BY_RELEASE_DATE: orderMapsByReleaseDateOnAction(); break;
                    case BY_IMPORTED_DATE: orderMapsByImportedDateOnAction(); break;
                    case BY_DOWNLOAD: orderMapsByDownloadOnAction(); break;
                    case BY_MAPS_CYCLE: orderMapsByMapsCycleOnAction(); break;
                    default: orderMapsByNameOnAction(); break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void cardRightOnAction() {
        try {
            cardOrientation = EnumCardOrientation.RIGHT;
            facade.setConfigPropertyValue("prop.config.lastUsedView", cardOrientation.name());
            if (!profileSelect.getItems().isEmpty()) {
                switch (orderType) {
                    case BY_ALIAS: orderMapsByAliasOnAction(); break;
                    case BY_RELEASE_DATE: orderMapsByReleaseDateOnAction(); break;
                    case BY_IMPORTED_DATE: orderMapsByImportedDateOnAction(); break;
                    case BY_DOWNLOAD: orderMapsByDownloadOnAction(); break;
                    case BY_MAPS_CYCLE: orderMapsByMapsCycleOnAction(); break;
                    default: orderMapsByNameOnAction(); break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void cardUpOnAction() {
        try {
            cardOrientation = EnumCardOrientation.UP;
            facade.setConfigPropertyValue("prop.config.lastUsedView", cardOrientation.name());
            if (!profileSelect.getItems().isEmpty()) {
                switch (orderType) {
                    case BY_ALIAS: orderMapsByAliasOnAction(); break;
                    case BY_RELEASE_DATE: orderMapsByReleaseDateOnAction(); break;
                    case BY_IMPORTED_DATE: orderMapsByImportedDateOnAction(); break;
                    case BY_DOWNLOAD: orderMapsByDownloadOnAction(); break;
                    case BY_MAPS_CYCLE: orderMapsByMapsCycleOnAction(); break;
                    default: orderMapsByNameOnAction(); break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void cardLeftOnAction() {
        try {
            cardOrientation = EnumCardOrientation.LEFT;
            facade.setConfigPropertyValue("prop.config.lastUsedView", cardOrientation.name());
            if (!profileSelect.getItems().isEmpty()) {
                switch (orderType) {
                    case BY_ALIAS: orderMapsByAliasOnAction(); break;
                    case BY_RELEASE_DATE: orderMapsByReleaseDateOnAction(); break;
                    case BY_IMPORTED_DATE: orderMapsByImportedDateOnAction(); break;
                    case BY_DOWNLOAD: orderMapsByDownloadOnAction(); break;
                    case BY_MAPS_CYCLE: orderMapsByMapsCycleOnAction(); break;
                    default: orderMapsByNameOnAction(); break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void detailedCardOnAction() {
        try {
            cardOrientation = EnumCardOrientation.DETAILED;
            facade.setConfigPropertyValue("prop.config.lastUsedView", cardOrientation.name());
            if (!profileSelect.getItems().isEmpty()) {
                switch (orderType) {
                    case BY_ALIAS: orderMapsByAliasOnAction(); break;
                    case BY_RELEASE_DATE: orderMapsByReleaseDateOnAction(); break;
                    case BY_IMPORTED_DATE: orderMapsByImportedDateOnAction(); break;
                    case BY_DOWNLOAD: orderMapsByDownloadOnAction(); break;
                    case BY_MAPS_CYCLE: orderMapsByMapsCycleOnAction(); break;
                    default: orderMapsByNameOnAction(); break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }
}
