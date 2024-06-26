package stories.maps;

import dtos.*;
import entities.CustomMapMod;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    @FXML ProgressIndicator progressIndicator;
    @FXML private MenuItem addToMapsCycle;
    @FXML private MenuItem removeFromMapsCycle;
    @FXML private MenuItem downloadMaps;

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
    }

    private void setLabelText() throws Exception {
        Double tooltipDuration = Double.parseDouble(
                facade.findPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
        );
        languageCode = facade.findPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");

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
        contentPane.getChildren().addAll(tabCheckbox, stackPane, title);

        return contentPane;
    }

    private GridPane createMapGridPane(PlatformProfileMapDto platformProfileMapDto) {

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

        GridPane gridpane = new GridPane();
        gridpane.setPrefWidth(getWidthGridPaneByNumberOfColums());
        gridpane.getStyleClass().add("gridPane");
        gridpane.setAlignment(Pos.CENTER);

        if (platformProfileMapDto.getMapDto().isOfficial()) {
            mapPreview.setPreserveRatio(true);
            mapPreview.setFitWidth(gridpane.getPrefWidth());
        } else {
            mapPreview.setPreserveRatio(false);
            mapPreview.setFitWidth(gridpane.getPrefWidth());
            mapPreview.setFitHeight(mapPreview.getFitWidth());
        }
        gridpane.add(mapPreview, 1, 1);
        GridPane.setMargin(mapPreview, new Insets(3,3,0,3));
        GridPane.setHalignment(mapPreview, HPos.CENTER);
        CheckBox checkbox = new CheckBox();
        checkbox.setOpacity(0.5);
        checkbox.setOnAction(e -> {
            if (checkbox.isSelected()) {
                checkbox.setOpacity(1);
            } else {
                checkbox.setOpacity(0.5);
            }
        });

        Label aliasLabel = new Label(platformProfileMapDto.getAlias(), checkbox);
        aliasLabel.setMinHeight(20);
        aliasLabel.setMaxWidth(Double.MAX_VALUE);
        aliasLabel.setAlignment(Pos.BOTTOM_CENTER);
        aliasLabel.setPadding(new Insets(10,0,0,0));
        gridpane.add(aliasLabel, 1, 2);

        String datePattern;
        String unknownStr;
        String releaseStr;
        String importedStr;
        String mapNameText;
        String dateHourPattern;
        Double tooltipDuration = 30.0;
        try {
            datePattern = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.code.datePattern");
            dateHourPattern = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.code.dateHourPattern");
            unknownStr = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.unknown");
            releaseStr = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.release");
            importedStr = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.imported");
            mapNameText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.name");
            tooltipDuration = Double.parseDouble(
                    facade.findPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
            );
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            datePattern = "yyyy/MM/dd";
            dateHourPattern = "yyyy/MM/dd HH:mm";
            unknownStr = "Unknown";
            releaseStr = "Release";
            importedStr = "Imported";
            mapNameText = "Name";
        }

        Text mapNameGraphic = new Text(mapNameText + ": ");
        mapNameGraphic.setFill(Color.GRAY);
        Label mapNameLabel = new Label(platformProfileMapDto.getMapDto().getKey(), mapNameGraphic);
        mapNameLabel.setMaxWidth(Double.MAX_VALUE);
        mapNameLabel.setAlignment(Pos.BOTTOM_CENTER);
        mapNameLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 11;");
        mapNameLabel.setPadding(new Insets(10,0,0,0));
        gridpane.add(mapNameLabel, 1, 3);


        Text releaseDateGraphic = new Text(releaseStr + ": ");
        releaseDateGraphic.setFill(Color.GRAY);
        String releaseDateStr = platformProfileMapDto.getReleaseDate() != null ? platformProfileMapDto.getReleaseDate().format(DateTimeFormatter.ofPattern(datePattern)): unknownStr;
        Label releaseDateLabel = new Label(releaseDateStr, releaseDateGraphic);
        releaseDateLabel.setMaxWidth(Double.MAX_VALUE);
        releaseDateLabel.setAlignment(Pos.BOTTOM_CENTER);
        releaseDateLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 11;");
        gridpane.add(releaseDateLabel, 1, 4);

        Text importedDateGraphic = new Text(importedStr + ": ");
        importedDateGraphic.setFill(Color.GRAY);

        Label importedDateText = new Label(platformProfileMapDto.getImportedDate().format(DateTimeFormatter.ofPattern(dateHourPattern)), importedDateGraphic);
        importedDateText.setMaxWidth(Double.MAX_VALUE);
        importedDateText.setAlignment(Pos.BOTTOM_CENTER);
        importedDateText.setStyle("-fx-text-fill: gray; -fx-font-size: 11;");
        gridpane.add(importedDateText, 1, 5);

        Label platformNameText = new Label(platformProfileMapDto.getPlatformDto().getKey());
        platformNameText.setVisible(false);
        gridpane.add(platformNameText, 1, 1);

        Label isOfficialText = new Label(String.valueOf(platformProfileMapDto.getMapDto().isOfficial()));
        isOfficialText.setVisible(false);
        gridpane.add(isOfficialText, 1, 1);

        int rowIndex = 6;
        CustomMapModDto customMapMod = null;
        if (platformProfileMapDto.getMapDto().isOfficial()) {
            importedDateText.setPadding(new Insets(0,0,10,0));
        } else {
            customMapMod = (CustomMapModDto) platformProfileMapDto.getMapDto();
            Label stateLabel;
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
            stateLabel = new Label(isMapModLabelText);
            stateLabel.setStyle("-fx-text-fill: #5fbb3d; -fx-font-weight: bold;");
            HBox statePane = new HBox();
            statePane.getChildren().addAll(stateLabel);
            statePane.setMaxWidth(Double.MAX_VALUE);
            statePane.setAlignment(Pos.CENTER);
            statePane.setPadding(new Insets(10,0,0,0));
            gridpane.add(statePane,1, rowIndex);
            rowIndex++;

            String downloadedLabelText = "";
            if (platformProfileMapDto.isDownloaded()) {
                try {
                    downloadedLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.downloaded");
                } catch (Exception e) {
                    downloadedLabelText = "DOWNLOADED";
                }

                stateLabel = new Label(downloadedLabelText);
                stateLabel.setStyle("-fx-text-fill: gold; -fx-padding: 3; -fx-border-color: gold; -fx-border-radius: 5;");
                statePane = new HBox();
                statePane.getChildren().addAll(stateLabel);
                statePane.setMaxWidth(Double.MAX_VALUE);
                statePane.setAlignment(Pos.CENTER);
                statePane.setPadding(new Insets(10,0,10,0));
                gridpane.add(statePane,1, rowIndex);

            } else {
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
                clickToDownloadMapLink.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        try {
                            List<String> platformNameList = new ArrayList<String>();
                            platformNameList.add(platformProfileMapDto.getPlatformDto().getKey());
                            List<String> mapNameList = new ArrayList<String>();
                            mapNameList.add(platformProfileMapDto.getMapDto().getKey());

                            facade.downloadMapListFromSteamCmd(platformNameList, mapNameList);

                        } catch (Exception ex) {
                            logger.error(ex.getMessage(), ex);
                            Utils.errorDialog(ex.getMessage(), ex);
                        }
                    }
                });
                gridpane.add(clickToDownloadMapLink,1, rowIndex);
            }
            rowIndex++;
        }

        if (customMapMod == null || platformProfileMapDto.isDownloaded() && customMapMod.isMap()) {
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
            gridpane.add(isInMapsCyclePane,1, rowIndex);
        }

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

        if (StringUtils.isNotBlank(tooltipText)) {
            Tooltip tooltip = new Tooltip(tooltipText.toString());
            tooltip.setShowDuration(Duration.seconds(tooltipDuration));
            Tooltip.install(gridpane, tooltip);
        }

        if (StringUtils.isNotBlank(platformProfileMapDto.getUrlInfo())) {
            gridpane.setCursor(Cursor.HAND);
            gridpane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Session.getInstance().setPpm(platformProfileMapDto);
                    loadNewContent("/views/mapWebInfo.fxml");
                    event.consume();
                }
            });
        }

        return gridpane;
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

    private Double getWidthGridPaneByNumberOfColums() {
        return (MainApplication.getPrimaryStage().getWidth() - (50 * mapsSlider.getValue()) - 180) / mapsSlider.getValue();
    }

    private void resizeGridPane(GridPane gridPane) {
        gridPane.setPrefWidth(getWidthGridPaneByNumberOfColums());
        ImageView mapPreview = (ImageView) gridPane.getChildren().get(0);
        mapPreview.setFitWidth(gridPane.getPrefWidth());
        if (!mapPreview.isPreserveRatio()) {
            mapPreview.setFitHeight(mapPreview.getFitWidth());
        }
        Label mapNameLabel = (Label) gridPane.getChildren().get(2);
        mapNameLabel.setMaxWidth(mapPreview.getFitWidth() - 25);
    }

    @FXML
    private void mapsSliderOnMouseClicked() {
        for (int index = 0; index < steamOfficialMapsFlowPane.getChildren().size(); index++) {
            GridPane gridPane = (GridPane) steamOfficialMapsFlowPane.getChildren().get(index);
            resizeGridPane(gridPane);
        }
        for (int index = 0; index < steamCustomMapsFlowPane.getChildren().size(); index++) {
            GridPane gridPane = (GridPane) steamCustomMapsFlowPane.getChildren().get(index);
            resizeGridPane(gridPane);
        }
        for (int index = 0; index < epicOfficialMapsFlowPane.getChildren().size(); index++) {
            GridPane gridPane = (GridPane) epicOfficialMapsFlowPane.getChildren().get(index);
            resizeGridPane(gridPane);
        }
        for (int index = 0; index < epicCustomMapsFlowPane.getChildren().size(); index++) {
            GridPane gridPane = (GridPane) epicCustomMapsFlowPane.getChildren().get(index);
            resizeGridPane(gridPane);
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
                GridPane gridpane = createMapGridPane(platformProfileMapDto);
                if (platformProfileMapDto.getMapDto().isOfficial()) {
                    steamOfficialMapsFlowPane.getChildren().add(gridpane);
                } else {
                    steamCustomMapsFlowPane.getChildren().add(gridpane);
                }
            }
        }

        for (PlatformProfileMapDto platformProfileMapDto : epicPlatformProfileMapDtoList) {
            String mapName = StringUtils.upperCase(platformProfileMapDto.getMapDto().getKey());
            String alias = StringUtils.upperCase(platformProfileMapDto.getAlias());
            if (mapName.contains(StringUtils.upperCase(searchMaps.getText())) || alias.contains(StringUtils.upperCase(searchMaps.getText()))) {
                GridPane gridpane = createMapGridPane(platformProfileMapDto);
                if (platformProfileMapDto.getMapDto().isOfficial()) {
                    epicOfficialMapsFlowPane.getChildren().add(gridpane);
                } else {
                    epicCustomMapsFlowPane.getChildren().add(gridpane);
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
                                    GridPane gridpane = createMapGridPane(platformProfileMapDtoOptional.get());
                                    steamCustomMapsFlowPane.getChildren().add(gridpane);
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
                                    GridPane gridpane = createMapGridPane(platformProfileMapDtoOptional.get());
                                    epicCustomMapsFlowPane.getChildren().add(gridpane);
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
                            GridPane gridpane = createMapGridPane(profileMapDto);
                            if (profileMapDto.getMapDto().isOfficial()) {
                                steamOfficialMapsFlowPane.getChildren().add(gridpane);
                            } else {
                                steamCustomMapsFlowPane.getChildren().add(gridpane);
                            }
                        });

                        epicPlatformProfileMapDtoList.stream().forEach(profileMapDto -> {
                            GridPane gridpane = createMapGridPane(profileMapDto);
                            if (profileMapDto.getMapDto().isOfficial()) {
                                epicOfficialMapsFlowPane.getChildren().add(gridpane);
                            } else {
                                epicCustomMapsFlowPane.getChildren().add(gridpane);
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
                GridPane gridpane = createMapGridPane(platformProfileMapDto);
                if (platformProfileMapDto.getMapDto().isOfficial()) {
                    steamOfficialMapsFlowPane.getChildren().add(gridpane);
                } else {
                    steamCustomMapsFlowPane.getChildren().add(gridpane);
                }
            }

            for (PlatformProfileMapDto platformProfileMapDto : epicPlatformProfileMapDtoList) {
                GridPane gridpane = createMapGridPane(platformProfileMapDto);
                if (platformProfileMapDto.getMapDto().isOfficial()) {
                    epicOfficialMapsFlowPane.getChildren().add(gridpane);
                } else {
                    epicCustomMapsFlowPane.getChildren().add(gridpane);
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
            GridPane gridpane = createMapGridPane(platformProfileMapDto);
            if (platformProfileMapDto.getMapDto().isOfficial()) {
                steamOfficialMapsFlowPane.getChildren().add(gridpane);
            } else {
                steamCustomMapsFlowPane.getChildren().add(gridpane);
            }
        }

        for (PlatformProfileMapDto platformProfileMapDto : epicPlatformProfileMapDtoList) {
            GridPane gridpane = createMapGridPane(platformProfileMapDto);
            if (platformProfileMapDto.getMapDto().isOfficial()) {
                epicOfficialMapsFlowPane.getChildren().add(gridpane);
            } else {
                epicCustomMapsFlowPane.getChildren().add(gridpane);
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
            GridPane gridpane = createMapGridPane(platformProfileMapDto);
                                         if (platformProfileMapDto.getMapDto().isOfficial()) {
                steamOfficialMapsFlowPane.getChildren().add(gridpane);
            } else {
                steamCustomMapsFlowPane.getChildren().add(gridpane);
            }
        }

        for (PlatformProfileMapDto platformProfileMapDto : epicPlatformProfileMapDtoList) {
            GridPane gridpane = createMapGridPane(platformProfileMapDto);
            if (platformProfileMapDto.getMapDto().isOfficial()) {
                epicOfficialMapsFlowPane.getChildren().add(gridpane);
            } else {
                epicCustomMapsFlowPane.getChildren().add(gridpane);
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
            GridPane gridpane = createMapGridPane(platformProfileMapDto);
            if (platformProfileMapDto.getMapDto().isOfficial()) {
                steamOfficialMapsFlowPane.getChildren().add(gridpane);
            } else {
                steamCustomMapsFlowPane.getChildren().add(gridpane);
            }
        }

         for (PlatformProfileMapDto platformProfileMapDto : epicPlatformProfileMapDtoList) {
             GridPane gridpane = createMapGridPane(platformProfileMapDto);
             if (platformProfileMapDto.getMapDto().isOfficial()) {
                 epicOfficialMapsFlowPane.getChildren().add(gridpane);
             } else {
                 epicCustomMapsFlowPane.getChildren().add(gridpane);
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
            GridPane gridpane = createMapGridPane(platformProfileMapDto);
            if (platformProfileMapDto.getMapDto().isOfficial()) {
                steamOfficialMapsFlowPane.getChildren().add(gridpane);
            } else {
                steamCustomMapsFlowPane.getChildren().add(gridpane);
            }
        }

        for (PlatformProfileMapDto platformProfileMapDto : epicPlatformProfileMapDtoList) {
            GridPane gridpane = createMapGridPane(platformProfileMapDto);
            if (platformProfileMapDto.getMapDto().isOfficial()) {
                epicOfficialMapsFlowPane.getChildren().add(gridpane);
            } else {
                epicCustomMapsFlowPane.getChildren().add(gridpane);
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
            GridPane gridpane = createMapGridPane(platformProfileMapDto);
            steamCustomMapsFlowPane.getChildren().add(gridpane);
        }
        for (PlatformProfileMapDto platformProfileMapDto : epicPlatformProfileMapDtoList) {
            GridPane gridpane = createMapGridPane(platformProfileMapDto);
            epicCustomMapsFlowPane.getChildren().add(gridpane);
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
            GridPane gridpane = createMapGridPane(platformProfileMapDto);
            if (platformProfileMapDto.getMapDto().isOfficial()) {
                steamOfficialMapsFlowPane.getChildren().add(gridpane);
            } else {
                steamCustomMapsFlowPane.getChildren().add(gridpane);
            }
        }

        for (PlatformProfileMapDto platformProfileMapDto : epicPlatformProfileMapDtoList) {
            GridPane gridpane = createMapGridPane(platformProfileMapDto);
            if (platformProfileMapDto.getMapDto().isOfficial()) {
                epicOfficialMapsFlowPane.getChildren().add(gridpane);
            } else {
                epicCustomMapsFlowPane.getChildren().add(gridpane);
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
                List<String> steamCustomMapNameListToDownload = steamCustomMapToDownloadList.stream().
                        map(node -> {
                            GridPane gridpane = (GridPane) node;
                            Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                            return mapNameLabel.getText();
                        }).
                        collect(Collectors.toList());

                List<String> platformNameList = new ArrayList<String>();
                platformNameList.add(EnumPlatform.STEAM.name());
                facade.downloadMapListFromSteamCmd(platformNameList, steamCustomMapNameListToDownload);

                List<String> epicCustomMapNameListToDownload = epicCustomMapToDownloadList.stream().
                        map(node -> {
                            GridPane gridpane = (GridPane) node;
                            Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                            return mapNameLabel.getText();
                        }).
                        collect(Collectors.toList());

                platformNameList.clear();
                platformNameList.add(EnumPlatform.EPIC.name());
                facade.downloadMapListFromSteamCmd(platformNameList, epicCustomMapNameListToDownload);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }
}
