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
import pojos.enums.EnumCardOrientation;
import pojos.enums.EnumMapsTab;
import pojos.enums.EnumPlatform;
import pojos.enums.EnumSortedMapsCriteria;
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
    @FXML private MenuItem cardDown;
    @FXML private MenuItem cardRight;

    public MapsController() {
        super();
        MapsInitializeModelContext mapsInitializeModelContext = new MapsInitializeModelContext(
                Session.getInstance().getActualProfileName()
        );
        facade = new MapsManagerFacadeImpl(mapsInitializeModelContext);
        steamPlatformProfileMapDtoList = new ArrayList<PlatformProfileMapDto>();
        epicPlatformProfileMapDtoList = new ArrayList<>();
        cardOrientation = EnumCardOrientation.DOWN;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        progressIndicator.setVisible(true);
        try {
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

        String sliderLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.slider");
        sliderLabel.setText(sliderLabelText);
        Tooltip sliderTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.slide"));
        sliderTooltip.setShowDuration(Duration.seconds(tooltipDuration));
        sliderLabel.setTooltip(sliderTooltip);
        double sliderColumns = Double.parseDouble(facade.findPropertyValue("properties/config.properties", "prop.config.mapSliderValue"));
        mapsSlider.setValue(sliderColumns);
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
                GridPane gridpane = (GridPane) node;
                Label aliasLabel = (Label) gridpane.getChildren().get(1);
                CheckBox checkbox = (CheckBox) aliasLabel.getGraphic();
                checkbox.setSelected(tabCheckbox.isSelected());
                if (checkbox.isSelected()) {
                    checkbox.setOpacity(1);
                } else {
                    checkbox.setOpacity(0.5);
                }
            }
        });

        Circle circle = new Circle(0,0, 10, Color.LIGHTBLUE);
        Text numberOfMapsText = new Text(String.valueOf(numberOfMaps));
        numberOfMapsText.setFont(Font.loadFont(getClass().getClassLoader().getResource("fonts/Ubuntu-B.ttf").toExternalForm(), 11));
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

    private ImageView createMapPreview(String urlPhoto) {

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
        aliasLabel.setMinHeight(20);
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

    private HBox createMapModPane(CustomMapModDto customMapMod) {
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
        HBox mapModPane = new HBox();
        mapModPane.getChildren().addAll(mapModLabel);
        mapModPane.setAlignment(Pos.CENTER);
        mapModPane.setPadding(new Insets(10,0,0,0));

        return mapModPane;
    }

    private Hyperlink createClickToDownloadMapLink(PlatformProfileMapDto platformProfileMapDto) {

        String downloadedLabelText = "";
        Label downloadedLabel = new Label();

        try {
            downloadedLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.clickToDownloadMap");
        } catch (Exception e) {
            downloadedLabelText = "Click to download it!";
        }

        Hyperlink clickToDownloadMapLink = new Hyperlink(downloadedLabelText);
        clickToDownloadMapLink.setStyle("-fx-text-fill: #f03830; -fx-underline: true;");
        clickToDownloadMapLink.setMaxWidth(Double.MAX_VALUE);
        clickToDownloadMapLink.setAlignment(Pos.CENTER);
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
                            return null;
                        }
                    };
                    task.setOnSucceeded(wse -> {
                        progressIndicator.setVisible(false);
                        if (EnumPlatform.STEAM.name().equals(platformProfileMapDto.getPlatformDto().getKey())) {
                            String downloadedLabelText = StringUtils.EMPTY;
                            try {
                                downloadedLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.downloaded");
                            } catch (Exception ex) {
                                downloadedLabelText = "DOWNLOADED";
                            }
                            downloadedLabel.setText(downloadedLabelText);
                            downloadedLabel.setStyle("-fx-text-fill: gold; -fx-padding: 3; -fx-border-color: gold; -fx-border-radius: 5;");
                            clickToDownloadMapLink.setVisible(false);
                        }
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


    private HBox createDownloadedPane(PlatformProfileMapDto platformProfileMapDto) {

        String downloadedLabelText = "";
        Label downloadedLabel = new Label();

        if (platformProfileMapDto.isDownloaded()) {
            try {
                downloadedLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.downloaded");
            } catch (Exception e) {
                downloadedLabelText = "DOWNLOADED";
            }
            downloadedLabel.setText(downloadedLabelText);
            downloadedLabel.setStyle("-fx-text-fill: gold; -fx-padding: 3; -fx-border-color: gold; -fx-border-radius: 5;");
        }

        HBox downloadedStatePane = new HBox();
        downloadedStatePane.getChildren().addAll(downloadedLabel);
        downloadedStatePane.setMaxWidth(Double.MAX_VALUE);
        downloadedStatePane.setAlignment(Pos.CENTER);
        downloadedStatePane.setPadding(new Insets(10,0,10,0));

        return downloadedStatePane;
    }

    private HBox createIsInMapsCyclePane(PlatformProfileMapDto platformProfileMapDto) {

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
        HBox isInMapsCyclePane = new HBox();
        isInMapsCyclePane.getChildren().addAll(isInMapsCycleLabel);
        isInMapsCyclePane.setMaxWidth(Double.MAX_VALUE);
        isInMapsCyclePane.setAlignment(Pos.CENTER);
        isInMapsCyclePane.setPadding(new Insets(0,0,10,0));

        return isInMapsCyclePane;
    }

    private VBox initializeNodeCardDown(PlatformProfileMapDto platformProfileMapDto) {

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

    private VBox createMapNodeCardDown(PlatformProfileMapDto platformProfileMapDto) {

        VBox vBox = initializeNodeCardDown(platformProfileMapDto);

        String urlPhoto = getUrlPhoto(platformProfileMapDto);
        ImageView mapPreview = createMapPreview(urlPhoto);

        CheckBox checkBox = createCheckBox();

        Label aliasLabel = createAliasLabel(platformProfileMapDto);
        aliasLabel.setAlignment(Pos.BOTTOM_CENTER);

        Label mapNameLabel = createMapNameLabel(platformProfileMapDto);
        mapNameLabel.setAlignment(Pos.BOTTOM_CENTER);

        Label releaseDateLabel = createReleaseDateLabel(platformProfileMapDto);
        releaseDateLabel.setAlignment(Pos.BOTTOM_CENTER);

        Label importedDateText = createImportedDateLabel(platformProfileMapDto);
        importedDateText.setAlignment(Pos.BOTTOM_CENTER);

        if (platformProfileMapDto.getMapDto().isOfficial()) {
            mapPreview.setPreserveRatio(true);
            mapPreview.setFitWidth(vBox.getPrefWidth());
        } else {
            mapPreview.setPreserveRatio(false);
            mapPreview.setFitWidth(vBox.getPrefWidth());
            mapPreview.setFitHeight(mapPreview.getFitWidth());
        }

        vBox.getChildren().addAll(mapPreview, aliasLabel, checkBox, mapNameLabel, releaseDateLabel, importedDateText);
        VBox.setMargin(mapPreview, new Insets(-21,3,3,3));

        CustomMapModDto customMapMod = null;
        if (platformProfileMapDto.getMapDto().isOfficial()) {
            importedDateText.setPadding(new Insets(0,0,10,0));
        } else {
            customMapMod = (CustomMapModDto) platformProfileMapDto.getMapDto();
            HBox mapModPane = createMapModPane(customMapMod);
            HBox downloadedStatePane = createDownloadedPane(platformProfileMapDto);
            vBox.getChildren().addAll(mapModPane, downloadedStatePane);

            if (!platformProfileMapDto.isDownloaded()) {
                Hyperlink clickToDownloadMapLink = createClickToDownloadMapLink(platformProfileMapDto);
                vBox.getChildren().add(clickToDownloadMapLink);
            }
        }

        if (customMapMod == null || platformProfileMapDto.isDownloaded() && customMapMod.isMap()) {
            HBox isInMapsCyclePane = createIsInMapsCyclePane(platformProfileMapDto);
            vBox.getChildren().add(isInMapsCyclePane);
        }

        return vBox;
    }

    private HBox initializeNodeCardRight(PlatformProfileMapDto platformProfileMapDto) {

        HBox hBox = new HBox();
        hBox.setPrefWidth(getWidthNodeByNumberOfColums()+10);
        hBox.getStyleClass().add("mapCard");
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setSpacing(5);

        String urlPhoto = getUrlPhoto(platformProfileMapDto);
        ImageView mapPreview = createMapPreview(urlPhoto);
        if (platformProfileMapDto.getMapDto().isOfficial()) {
            mapPreview.setPreserveRatio(true);
            mapPreview.setFitWidth(Math.floor((hBox.getPrefWidth() - 11) / 2));
        } else {
            mapPreview.setPreserveRatio(false);
            mapPreview.setFitWidth(Math.floor((hBox.getPrefWidth() - 11) / 2));
            mapPreview.setFitHeight(mapPreview.getFitWidth());
        }

        Label platformNameText = new Label(platformProfileMapDto.getPlatformDto().getKey());
        platformNameText.setVisible(false);

        Label isOfficialText = new Label(String.valueOf(platformProfileMapDto.getMapDto().isOfficial()));
        isOfficialText.setVisible(false);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPrefWidth(Math.floor((hBox.getPrefWidth() - 11) / 2));
        vBox.getChildren().addAll(platformNameText, isOfficialText);

        hBox.getChildren().addAll(mapPreview, vBox);
        HBox.setMargin(mapPreview, new Insets(3,0,3,3));
        HBox.setMargin(vBox, new Insets(-21,3,3,0));

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

    private Node createMapNodeCardRight(PlatformProfileMapDto platformProfileMapDto) {

        HBox hBox = initializeNodeCardRight(platformProfileMapDto);

        CheckBox checkBox = createCheckBox();

        Label aliasLabel = createAliasLabel(platformProfileMapDto);
        aliasLabel.setAlignment(Pos.CENTER);

        Label mapNameLabel = createMapNameLabel(platformProfileMapDto);
        mapNameLabel.setAlignment(Pos.CENTER);

        Label releaseDateLabel = createReleaseDateLabel(platformProfileMapDto);
        releaseDateLabel.setAlignment(Pos.CENTER);

        Label importedDateText = createImportedDateLabel(platformProfileMapDto);
        importedDateText.setAlignment(Pos.CENTER);

        VBox vBox = (VBox) hBox.getChildren().get(1);
        vBox.getChildren().addAll(aliasLabel, checkBox, mapNameLabel, releaseDateLabel, importedDateText);

        CustomMapModDto customMapMod = null;
        if (platformProfileMapDto.getMapDto().isOfficial()) {
            importedDateText.setPadding(new Insets(0,0,10,0));
        } else {
            customMapMod = (CustomMapModDto) platformProfileMapDto.getMapDto();
            HBox mapModPane = createMapModPane(customMapMod);
            HBox downloadedStatePane = createDownloadedPane(platformProfileMapDto);
            vBox.getChildren().addAll(mapModPane, downloadedStatePane);

            if (!platformProfileMapDto.isDownloaded()) {
                Hyperlink clickToDownloadMapLink = createClickToDownloadMapLink(platformProfileMapDto);
                vBox.getChildren().add(clickToDownloadMapLink);
            }
        }

        if (customMapMod == null || platformProfileMapDto.isDownloaded() && customMapMod.isMap()) {
            HBox isInMapsCyclePane = createIsInMapsCyclePane(platformProfileMapDto);
            vBox.getChildren().add(isInMapsCyclePane);
        }

        return hBox;
    }

    private GridPane createMapGridPaneCardRight(PlatformProfileMapDto platformProfileMapDto) {

        // GridPane gridpane = initializeGridPane(platformProfileMapDto);
        // String urlPhoto = getUrlPhoto(platformProfileMapDto);
        // ImageView mapPreview = createMapPreview(urlPhoto);

        /*
        Label aliasLabel = createAliasLabel(platformProfileMapDto);
        aliasLabel.setAlignment(Pos.CENTER_LEFT);

        Label mapNameLabel = createMapNameLabel(platformProfileMapDto);
        mapNameLabel.setAlignment(Pos.CENTER_LEFT);

        Label releaseDateLabel = createReleaseDateLabel(platformProfileMapDto);
        releaseDateLabel.setAlignment(Pos.CENTER_LEFT);

        Label importedDateText = createImportedDateLabel(platformProfileMapDto);
        importedDateText.setAlignment(Pos.CENTER_LEFT);

        if (platformProfileMapDto.getMapDto().isOfficial()) {
            mapPreview.setPreserveRatio(true);
            mapPreview.setFitWidth(gridpane.getPrefWidth() / 2);
        } else {
            mapPreview.setPreserveRatio(false);
            mapPreview.setFitWidth(gridpane.getPrefWidth() / 2);
            mapPreview.setFitHeight(mapPreview.getFitWidth());
        }

        gridpane.setAlignment(Pos.CENTER_LEFT);
        gridpane.add(mapPreview, 1, 1);
        GridPane.setMargin(mapPreview, new Insets(3,10,3,3));
        gridpane.add(aliasLabel, 2, 1);
        gridpane.add(mapNameLabel, 2, 2);
        gridpane.add(releaseDateLabel, 2, 3);
        gridpane.add(importedDateText, 2, 4);
        */

        /*
        int rowIndex = 5;
        CustomMapModDto customMapMod = null;
        if (platformProfileMapDto.getMapDto().isOfficial()) {
            importedDateText.setPadding(new Insets(0,0,10,0));
        } else {
            customMapMod = (CustomMapModDto) platformProfileMapDto.getMapDto();
            HBox mapModPane = createMapModPane(customMapMod);
            gridpane.add(mapModPane,2, rowIndex);
            rowIndex++;

            HBox downloadedStatePane = createDownloadedPane(platformProfileMapDto);
            gridpane.add(downloadedStatePane,2, rowIndex);

            if (!platformProfileMapDto.isDownloaded()) {
                Hyperlink clickToDownloadMapLink = createClickToDownloadMapLink(platformProfileMapDto);
                gridpane.add(clickToDownloadMapLink,2, rowIndex);
            }
            rowIndex++;
        }

        if (customMapMod == null || platformProfileMapDto.isDownloaded() && customMapMod.isMap()) {
            HBox isInMapsCyclePane = createIsInMapsCyclePane(platformProfileMapDto);
            gridpane.add(isInMapsCyclePane,2, rowIndex);
        }

        GridPane.setRowSpan(mapPreview, rowIndex);

        return gridpane;
         */

        return null;
    }

    private Node createMapNode(PlatformProfileMapDto platformProfileMapDto) {
        switch (cardOrientation) {
            case DOWN: return createMapNodeCardDown(platformProfileMapDto);
            case RIGHT: return createMapNodeCardRight(platformProfileMapDto);
            default: return createMapNodeCardDown(platformProfileMapDto);
        }
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

    private void resizeNode(Node node) {
        ImageView mapPreview = null;
        Label mapNameLabel = null;
        switch (cardOrientation) {
            case DOWN:
                ((VBox) node).setPrefWidth(getWidthNodeByNumberOfColums());
                mapPreview = (ImageView) ((VBox) node).getChildren().get(2);
                mapNameLabel = (Label) ((VBox) node).getChildren().get(5);

                mapPreview.setFitWidth(getWidthNodeByNumberOfColums());
                if (!mapPreview.isPreserveRatio()) {
                    mapPreview.setFitHeight(mapPreview.getFitWidth());
                }
                break;
            case RIGHT:
                ((HBox) node).setPrefWidth(getWidthNodeByNumberOfColums());
                mapPreview = (ImageView) ((StackPane) ((HBox) node).getChildren().get(0)).getChildren().get(2);
                mapNameLabel = (Label) ((VBox) ((HBox) node).getChildren().get(1)).getChildren().get(2);

                mapPreview.setFitWidth(getWidthNodeByNumberOfColums() / 2);
                if (!mapPreview.isPreserveRatio()) {
                    mapPreview.setFitHeight(mapPreview.getFitWidth());
                }
                break;
            default:
                ((VBox) node).setPrefWidth(getWidthNodeByNumberOfColums());
                mapPreview = (ImageView) ((VBox) node).getChildren().get(2);
                mapNameLabel = (Label) ((VBox) node).getChildren().get(5);

                mapPreview.setFitWidth(getWidthNodeByNumberOfColums());
                if (!mapPreview.isPreserveRatio()) {
                    mapPreview.setFitHeight(mapPreview.getFitWidth());
                }
                break;
        }

        mapNameLabel.setMaxWidth(mapPreview.getFitWidth() - 25);
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
            facade.setConfigPropertyValue("prop.config.mapSliderValue", String.valueOf(mapsSlider.getValue()));
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
        GridPane gridpane = (GridPane) node;
        Label aliasLabel = (Label) gridpane.getChildren().get(1);
        CheckBox checkbox = (CheckBox) aliasLabel.getGraphic();
        Label mapNameLabel = (Label) gridpane.getChildren().get(2);
        Label platformNameLabel = (Label) gridpane.getChildren().get(5);

        if (checkbox.isSelected()) {
            String platformDescription = EnumPlatform.getByName(platformNameLabel.getText()).getDescripcion();
            message.append("[" + platformDescription + "] " + mapNameLabel.getText()).append("\n");
            return gridpane;
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

                    final StringBuffer[] errors = {new StringBuffer()};
                    Task<List<GridPane>> task = new Task<List<GridPane>>() {
                        @Override
                        protected List<GridPane> call() throws Exception {
                            List<GridPane> gridpaneToRemoveList = new ArrayList<GridPane>();
                            errors[0] = new StringBuffer();
                            for (Node node : removeList) {
                                try {
                                    GridPane gridpane = (GridPane) node;
                                    Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                                    Label platformNameLabel = (Label) gridpane.getChildren().get(5);

                                    AbstractMapDto map = facade.deleteMapFromPlatformProfile(platformNameLabel.getText(), mapNameLabel.getText(), profileSelect.getValue().getName());
                                    if (map != null) {
                                        gridpaneToRemoveList.add(gridpane);
                                    } else {
                                        errors[0].append(mapNameLabel.getText()).append("\n");
                                    }
                                } catch (Exception e) {
                                    logger.error(e.getMessage(), e);
                                    Utils.errorDialog(e.getMessage(), e);
                                }
                            }
                            return gridpaneToRemoveList;
                        }
                    };

                    task.setOnSucceeded(wse -> {
                        List<GridPane> gridpaneToRemoveList = task.getValue();
                        try {
                            if (!gridpaneToRemoveList.isEmpty()) {

                                for (Node node : gridpaneToRemoveList) {
                                    GridPane gridpane = (GridPane) node;
                                    Label platformNameLabel = (Label) gridpane.getChildren().get(5);
                                    Label isOfficialMapLabel = (Label) gridpane.getChildren().get(6);

                                    if (EnumPlatform.STEAM.name().equals(platformNameLabel.getText())) {
                                        if (Boolean.parseBoolean(isOfficialMapLabel.getText())) {
                                            steamOfficialMapsFlowPane.getChildren().remove(node);
                                        } else {
                                            steamCustomMapsFlowPane.getChildren().remove(node);
                                        }
                                    }
                                    if (EnumPlatform.EPIC.name().equals(platformNameLabel.getText())) {
                                        if (Boolean.parseBoolean(isOfficialMapLabel.getText())) {
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
            List<PlatformProfileToDisplay> selectedPlatformProfileList = Utils.selectPlatformProfilesDialog(
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
        if (steamOfficialMapsTab.isSelected() || epicOfficialMapsTab.isSelected()) {
            return;
        }
        steamCustomMapsFlowPane.getChildren().clear();
        epicCustomMapsFlowPane.getChildren().clear();

        if (EnumSortedMapsCriteria.DOWNLOAD_DESC.equals(Session.getInstance().getSortedMapsCriteria())) {
            steamPlatformProfileMapDtoList = steamPlatformProfileMapDtoList.stream().
                    filter(ppm -> !ppm.getMapDto().isOfficial()).
                    sorted((ppm1, ppm2) -> {
                return ppm1.isDownloaded().compareTo(ppm2.isDownloaded());
            }).collect(Collectors.toList());
            epicPlatformProfileMapDtoList = epicPlatformProfileMapDtoList.stream().
                    filter(ppm -> !ppm.getMapDto().isOfficial()).
                    sorted((ppm1, ppm2) -> {
                        return ppm1.isDownloaded().compareTo(ppm2.isDownloaded());
                    }).collect(Collectors.toList());

            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.DOWNLOAD_ASC);
        } else {
            steamPlatformProfileMapDtoList = steamPlatformProfileMapDtoList.stream().
                    filter(ppm -> !ppm.getMapDto().isOfficial()).
                    sorted((ppm1, ppm2) -> {
                    return ppm2.isDownloaded().compareTo(ppm1.isDownloaded());
            }).collect(Collectors.toList());
            epicPlatformProfileMapDtoList = epicPlatformProfileMapDtoList.stream().
                    filter(ppm -> !ppm.getMapDto().isOfficial()).
                    sorted((ppm1, ppm2) -> {
                        Boolean map1Downloaded = ppm1.isDownloaded();
                        Boolean map2Downloaded = ppm2.isDownloaded();
                        return ppm2.isDownloaded().compareTo(ppm1.isDownloaded());
                    }).collect(Collectors.toList());

            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.DOWNLOAD_DESC);
        }

        for (PlatformProfileMapDto platformProfileMapDto : steamPlatformProfileMapDtoList) {
            Node node = createMapNode(platformProfileMapDto);
            steamCustomMapsFlowPane.getChildren().add(node);
        }
        for (PlatformProfileMapDto platformProfileMapDto : epicPlatformProfileMapDtoList) {
            Node node = createMapNode(platformProfileMapDto);
            epicCustomMapsFlowPane.getChildren().add(node);
        }
    }

    private List<PlatformProfileMapDto> getSteamEditList() throws Exception {

        List<PlatformProfileMapDto> editList = new ArrayList<PlatformProfileMapDto>();
        ObservableList<Node> nodeList = FXCollections.concat(steamOfficialMapsFlowPane.getChildren(), steamCustomMapsFlowPane.getChildren());

        if (nodeList != null && !nodeList.isEmpty()) {
            for (Node node : nodeList) {
                GridPane gridpane = (GridPane) node;
                Label aliasLabel = (Label) gridpane.getChildren().get(1);
                Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                CheckBox checkbox = (CheckBox) aliasLabel.getGraphic();

                if (checkbox.isSelected()) {
                    String mapName = mapNameLabel.getText();
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

        if (nodeList != null && !nodeList.isEmpty()) {
            for (Node node : nodeList) {
                GridPane gridpane = (GridPane) node;
                Label aliasLabel = (Label) gridpane.getChildren().get(1);
                Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                CheckBox checkbox = (CheckBox) aliasLabel.getGraphic();

                if (checkbox.isSelected()) {
                    String mapName = mapNameLabel.getText();
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
            StringBuffer message = new StringBuffer();
            List<Node> steamOfficialAddMapsCycleList = new ArrayList<Node>();
            for (Node node : steamOfficialMapsFlowPane.getChildren()) {
                Node selectedNode = getNodeIfSelected(node, message);
                if (selectedNode != null) {
                    steamOfficialAddMapsCycleList.add(selectedNode);
                }
            }

            List<Node> steamCustomAddMapsCycleList = new ArrayList<Node>();
            for (Node node : steamCustomMapsFlowPane.getChildren()) {
                Node selectedNode = getNodeIfSelected(node, message);
                if (selectedNode != null) {
                    steamCustomAddMapsCycleList.add(selectedNode);
                }
            }

            List<Node> epicOfficialAddMapsCycleList = new ArrayList<Node>();
            for (Node node : epicOfficialMapsFlowPane.getChildren()) {
                Node selectedNode = getNodeIfSelected(node, message);
                if (selectedNode != null) {
                    epicOfficialAddMapsCycleList.add(selectedNode);
                }
            }

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

                List<String> steamOfficialMapNameListToAddFromMapsCycle = steamOfficialAddMapsCycleList.stream().
                        map(node -> {
                            GridPane gridpane = (GridPane) node;
                            Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                            return mapNameLabel.getText();
                        }).
                        collect(Collectors.toList());

                List<String> steamCustomMapNameListToAddFromMapsCycle = steamCustomAddMapsCycleList.stream().
                        map(node -> {
                            GridPane gridpane = (GridPane) node;
                            Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                            return mapNameLabel.getText();
                        }).
                        collect(Collectors.toList());

                List<String> epicOfficialMapNameListToAddFromMapsCycle = epicOfficialAddMapsCycleList.stream().
                        map(node -> {
                            GridPane gridpane = (GridPane) node;
                            Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                            return mapNameLabel.getText();
                        }).
                        collect(Collectors.toList());

                List<String> epicCustomMapNameListToAddFromMapsCycle = epicCustomAddMapsCycleList.stream().
                        map(node -> {
                            GridPane gridpane = (GridPane) node;
                            Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                            return mapNameLabel.getText();
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
                    setMapInMapsCycle(node, true, 7);
                }
                for (Node node : steamCustomAddMapsCycleList) {
                    setMapInMapsCycle(node, true, 9);
                }
                for (Node node : epicOfficialAddMapsCycleList) {
                    setMapInMapsCycle(node, true, 7);
                }
                for (Node node : epicCustomAddMapsCycleList) {
                    setMapInMapsCycle(node, true, 9);
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
            StringBuffer message = new StringBuffer();
            List<Node> steamOfficialRemoveMapsCycleList = new ArrayList<Node>();
            for (Node node : steamOfficialMapsFlowPane.getChildren()) {
                Node selectedNode = getNodeIfSelected(node, message);
                if (selectedNode != null) {
                    steamOfficialRemoveMapsCycleList.add(selectedNode);
                }
            }

            List<Node> steamCustomRemoveMapsCycleList = new ArrayList<Node>();
            for (Node node : steamCustomMapsFlowPane.getChildren()) {
                Node selectedNode = getNodeIfSelected(node, message);
                if (selectedNode != null) {
                    steamCustomRemoveMapsCycleList.add(selectedNode);
                }
            }

            List<Node> epicOfficialRemoveMapsCycleList = new ArrayList<Node>();
            for (Node node : epicOfficialMapsFlowPane.getChildren()) {
                Node selectedNode = getNodeIfSelected(node, message);
                if (selectedNode != null) {
                    epicOfficialRemoveMapsCycleList.add(selectedNode);
                }
            }

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
                List<String> steamOfficialMapNameListToRemoveFromMapsCycle = steamOfficialRemoveMapsCycleList.stream().
                        map(node -> {
                            GridPane gridpane = (GridPane) node;
                            Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                            return mapNameLabel.getText();
                        }).
                        collect(Collectors.toList());

                List<String> steamCustomMapNameListToRemoveFromMapsCycle = steamCustomRemoveMapsCycleList.stream().
                        map(node -> {
                            GridPane gridpane = (GridPane) node;
                            Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                            return mapNameLabel.getText();
                        }).
                        collect(Collectors.toList());

                List<String> epicOfficialMapNameListToRemoveFromMapsCycle = epicOfficialRemoveMapsCycleList.stream().
                        map(node -> {
                            GridPane gridpane = (GridPane) node;
                            Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                            return mapNameLabel.getText();
                        }).
                        collect(Collectors.toList());

                List<String> epicCustomMapNameListToRemoveFromMapsCycle = epicCustomRemoveMapsCycleList.stream().
                        map(node -> {
                            GridPane gridpane = (GridPane) node;
                            Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                            return mapNameLabel.getText();
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
                    setMapInMapsCycle(node, false, 7);
                }
                for (Node node : steamCustomRemoveMapsCycleList) {
                    setMapInMapsCycle(node, false, 9);
                }
                for (Node node : epicOfficialRemoveMapsCycleList) {
                    setMapInMapsCycle(node, false, 7);
                }
                for (Node node : epicCustomRemoveMapsCycleList) {
                    setMapInMapsCycle(node, false, 9);
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


    private void setMapInMapsCycle(Node node, boolean inMapCycle, int row) throws Exception {
        GridPane gridpane = (GridPane) node;
        Label aliasLabel = (Label) gridpane.getChildren().get(1);
        CheckBox checkbox = (CheckBox) aliasLabel.getGraphic();
        checkbox.setSelected(false);

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

            Label isInMapsCycleLabel = (Label) ((HBox) gridpane.getChildren().get(row)).getChildren().get(0);
            isInMapsCycleLabel.setText(isInMapsCycleText);
            isInMapsCycleLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold; -fx-padding: 3; -fx-border-color: " + color + "; -fx-border-radius: 5;");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @FXML
    private void orderMapsByMapsCycleOnAction() {
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
            StringBuffer message = new StringBuffer();
            List<Node> steamCustomMapToDownloadList = new ArrayList<Node>();
            for (Node node : steamCustomMapsFlowPane.getChildren()) {
                Node selectedNode = getNodeIfSelected(node, message);
                if (selectedNode != null) {
                    steamCustomMapToDownloadList.add(selectedNode);
                }
            }

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
                            GridPane gridpane = (GridPane) node;
                            Hyperlink clickToDownloadMapLink = (Hyperlink) gridpane.getChildren().get(9);
                            Event.fireEvent(clickToDownloadMapLink, new MouseEvent(MouseEvent.MOUSE_CLICKED,
                                    clickToDownloadMapLink.getLayoutX(), clickToDownloadMapLink.getLayoutY(), clickToDownloadMapLink.getLayoutX(), clickToDownloadMapLink.getLayoutY(), MouseButton.PRIMARY, 1,
                                    true, true, true, true, true, true, true, true, true, true, null));
                            Label aliasLabel = (Label) gridpane.getChildren().get(1);
                            CheckBox selectedMap = (CheckBox) aliasLabel.getGraphic();
                            selectedMap.setSelected(false);
                        });
                        epicCustomMapToDownloadList.stream().forEach(node ->{
                            GridPane gridpane = (GridPane) node;
                            Hyperlink clickToDownloadMapLink = (Hyperlink) gridpane.getChildren().get(9);
                            Event.fireEvent(clickToDownloadMapLink, new MouseEvent(MouseEvent.MOUSE_CLICKED,
                                    clickToDownloadMapLink.getLayoutX(), clickToDownloadMapLink.getLayoutY(), clickToDownloadMapLink.getLayoutX(), clickToDownloadMapLink.getLayoutY(), MouseButton.PRIMARY, 1,
                                    true, true, true, true, true, true, true, true, true, true, null));
                            Label aliasLabel = (Label) gridpane.getChildren().get(1);
                            CheckBox selectedMap = (CheckBox) aliasLabel.getGraphic();
                            selectedMap.setSelected(false);
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
        cardOrientation = EnumCardOrientation.DOWN;
        if (!profileSelect.getItems().isEmpty()) {
            orderMapsByNameOnAction();
        }
    }

    @FXML
    private void cardRightOnAction() {
        cardOrientation = EnumCardOrientation.RIGHT;
        if (!profileSelect.getItems().isEmpty()) {
            orderMapsByNameOnAction();
        }
    }
}
