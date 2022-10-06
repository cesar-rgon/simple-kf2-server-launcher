package stories.mapsedition;

import dtos.*;
import entities.PlatformProfileMap;
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
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import pojos.session.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import start.MainApplication;
import utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MapsEditionController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MapsEditionController.class);

    private final MapsEditionFacade facade;
    private final PropertyService propertyService;
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
    @FXML private ImageView searchImg;
    @FXML private ComboBox<ProfileDto> profileSelect;
    @FXML private Label profileLabel;
    @FXML private Label actionsLabel;
    @FXML private Menu orderMaps;
    @FXML private MenuItem orderMapsByAlias;
    @FXML private MenuItem orderMapsByName;
    @FXML private MenuItem orderMapsByReleaseDate;
    @FXML private MenuItem orderMapsByImportedDate;
    @FXML private MenuItem orderMapsByDownload;
    @FXML private MenuItem editMaps;
    @FXML ProgressIndicator progressIndicator;

    public MapsEditionController() {
        super();
        facade = new MapsEditionFacadeImpl();
        propertyService = new PropertyServiceImpl();
        steamPlatformProfileMapDtoList = new ArrayList<PlatformProfileMapDto>();
        epicPlatformProfileMapDtoList = new ArrayList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            progressIndicator.setVisible(true);
            ObservableList<ProfileDto> profileOptions = facade.listAllProfiles();
            profileSelect.setItems(profileOptions);
            if (!profileOptions.isEmpty()) {
                profileSelect.setValue(Session.getInstance().getMapsProfile() != null?
                        Session.getInstance().getMapsProfile():
                        Session.getInstance().getActualProfile() != null?
                                Session.getInstance().getActualProfile():
                                profileSelect.getItems().get(0));
            }

            Task<Void> task = new Task<Void>() {

                @Override
                protected Void call() throws Exception {

                    languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
                    String profileLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.profile");
                    profileLabel.setText(profileLabelText);

                    String actionsLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.actions");
                    actionsLabel.setText(actionsLabelText);

                    if (!profileOptions.isEmpty()) {
                        if (facade.isCorrectInstallationFolder(EnumPlatform.STEAM.name())) {
                            steamPlatformProfileMapDtoList = facade.listPlatformProfileMaps(
                                    EnumPlatform.STEAM.name(),
                                    profileSelect.getValue().getName()
                            );
                        }

                        if (facade.isCorrectInstallationFolder(EnumPlatform.EPIC.name())) {
                            epicPlatformProfileMapDtoList = facade.listPlatformProfileMaps(
                                    EnumPlatform.EPIC.name(),
                                    profileSelect.getValue().getName()
                            );
                        }
                    } else {
                        profileSelect.setValue(null);
                        steamPlatformProfileMapDtoList = null;
                        epicPlatformProfileMapDtoList = null;
                    }
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


                    Double tooltipDuration = Double.parseDouble(
                            propertyService.getPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
                    );

                    Tooltip searchTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.searchMaps"));
                    searchTooltip.setShowDuration(Duration.seconds(tooltipDuration));
                    searchMaps.setTooltip(searchTooltip);
                    Tooltip.install(searchImg, searchTooltip);

                    String addNewMapsText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.addMaps");
                    addNewMaps.setText(addNewMapsText);

                    String searchInWorkShopText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.searchInWorkShop");
                    searchInWorkShop.setText(searchInWorkShopText);

                    String removeMapsText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.removeMaps");
                    removeMaps.setText(removeMapsText);

                    String importMapsFromServerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.importMaps");
                    importMapsFromServer.setText(importMapsFromServerText);

                    String orderMapsText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.orderMaps");
                    orderMaps.setText(orderMapsText);

                    String orderMapsByAliasText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.orderMapsByAlias");
                    orderMapsByAlias.setText(orderMapsByAliasText);

                    String orderMapsByNameText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.orderMapsByName");
                    orderMapsByName.setText(orderMapsByNameText);

                    String orderMapsByReleaseDateText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.orderMapsByReleaseDate");
                    orderMapsByReleaseDate.setText(orderMapsByReleaseDateText);

                    String orderMapsByImportedDateText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.orderMapsByImportedDate");
                    orderMapsByImportedDate.setText(orderMapsByImportedDateText);

                    String orderMapsByDownloadText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.orderMapsByDownload");
                    orderMapsByDownload.setText(orderMapsByDownloadText);

                    String editMapsText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.editMaps");
                    editMaps.setText(editMapsText);

                    String sliderLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.slider");
                    sliderLabel.setText(sliderLabelText);
                    Tooltip sliderTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.slide"));
                    sliderTooltip.setShowDuration(Duration.seconds(tooltipDuration));
                    sliderLabel.setTooltip(sliderTooltip);
                    Double sliderColumns = Double.parseDouble(facade.findConfigPropertyValue("prop.config.mapSliderValue"));
                    mapsSlider.setValue(sliderColumns);
                    mapsSlider.setTooltip(sliderTooltip);

                    return null;
                }
            };

            task.setOnSucceeded(wse -> {
                if (!profileSelect.getItems().isEmpty()) {
                    orderMapsByNameOnAction();
                }

                try {
                    String officialMapsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.officiaslMaps");
                    String customMapsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.customMaps");

                    if (facade.isCorrectInstallationFolder(EnumPlatform.STEAM.name())) {
                        steamOfficialMapsTab.setDisable(false);
                        steamCustomMapsTab.setDisable(false);

                        steamOfficialMapsTab.setGraphic(createTabTitle(EnumPlatform.STEAM, true, officialMapsTabText, steamOfficialMapsFlowPane.getChildren().size()));
                        steamOfficialMapsTab.setText("");

                        steamCustomMapsTab.setGraphic(createTabTitle(EnumPlatform.STEAM, false, customMapsTabText, steamCustomMapsFlowPane.getChildren().size()));
                        steamCustomMapsTab.setText("");

                    } else {
                        steamOfficialMapsTab.setDisable(true);
                        steamCustomMapsTab.setDisable(true);
                    }

                    if (facade.isCorrectInstallationFolder(EnumPlatform.EPIC.name())) {
                        steamOfficialMapsTab.setDisable(false);
                        steamCustomMapsTab.setDisable(false);

                        epicOfficialMapsTab.setGraphic(createTabTitle(EnumPlatform.EPIC, true, officialMapsTabText, epicOfficialMapsFlowPane.getChildren().size()));
                        epicOfficialMapsTab.setText("");

                        epicCustomMapsTab.setGraphic(createTabTitle(EnumPlatform.EPIC, false, customMapsTabText, epicCustomMapsFlowPane.getChildren().size()));
                        epicCustomMapsTab.setText("");
                    } else {
                        epicOfficialMapsTab.setDisable(true);
                        epicCustomMapsTab.setDisable(true);
                    }

                    mapsSliderOnMouseClicked();
                    progressIndicator.setVisible(false);

                } catch (Exception e) {
                    String message = "Error getting map list";
                    logger.error(message, e);
                    Utils.errorDialog(message, e);
                }
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
        } catch (Exception e) {
            String message = "Error getting map list";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }


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

        ImageView mapPreview = new ImageView();
        String installationFolder = platformProfileMapDto.getPlatformDto().getInstallationFolder();
        try {
            Image image = null;
            if (facade.isCorrectInstallationFolder(platformProfileMapDto.getPlatformDto().getKey()) && StringUtils.isNotBlank(platformProfileMapDto.getUrlPhoto())) {
                image = new Image("file:" + installationFolder + "/" + platformProfileMapDto.getUrlPhoto());
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

        mapPreview.setPreserveRatio(false);
        mapPreview.setFitWidth(gridpane.getPrefWidth());
        mapPreview.setFitHeight(gridpane.getPrefWidth()/2);
        gridpane.add(mapPreview, 1, 1);

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
            datePattern = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.code.datePattern");
            dateHourPattern = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.code.dateHourPattern");
            unknownStr = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.unknown");
            releaseStr = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.release");
            importedStr = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.imported");
            mapNameText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.name");
            tooltipDuration = Double.parseDouble(
                    propertyService.getPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
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
        if (platformProfileMapDto.getMapDto().isOfficial()) {
            importedDateText.setPadding(new Insets(0,0,10,0));
        } else {
            String labelText = "";
            if (platformProfileMapDto.isDownloaded()) {
                try {
                    labelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.downloaded");
                } catch (Exception e) {
                    labelText = "DOWNLOADED";
                }

                Label stateLabel = new Label(labelText);
                stateLabel.setStyle("-fx-text-fill: gold; -fx-padding: 3; -fx-border-color: gold; -fx-border-radius: 5;");
                HBox statePane = new HBox();
                statePane.getChildren().addAll(stateLabel);
                statePane.setMaxWidth(Double.MAX_VALUE);
                statePane.setAlignment(Pos.CENTER);
                statePane.setPadding(new Insets(10,0,10,0));
                gridpane.add(statePane,1, rowIndex);

            } else {
                try {
                    labelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.startServer");
                } catch (Exception e) {
                    labelText = "Start server to download it";
                }

                Hyperlink startServerLink = new Hyperlink(labelText);
                startServerLink.setStyle("-fx-text-fill: #f03830; -fx-underline: true;");
                startServerLink.setMaxWidth(Double.MAX_VALUE);
                startServerLink.setAlignment(Pos.CENTER);
                startServerLink.setPadding(new Insets(10,0,10,0));
                startServerLink.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        try {
                            Session.getInstance().setConsole((StringUtils.isNotBlank(Session.getInstance().getConsole())? Session.getInstance().getConsole() + "\n\n" : "") +
                                    "< " + new Date() + " - Run Server >\n" + facade.runServer(platformProfileMapDto.getPlatformDto().getKey(), profileSelect.getValue().getName()));
                        } catch (Exception ex) {
                            logger.error(ex.getMessage(), ex);
                            Utils.errorDialog(ex.getMessage(), ex);
                        }
                    }
                });
                gridpane.add(startServerLink,1, rowIndex);
            }
            rowIndex++;
        }

        StringBuffer tooltipText = new StringBuffer();
        if (!platformProfileMapDto.getMapDto().isOfficial()) {
            tooltipText.append("id WorkShop: ").append(((CustomMapModDto) platformProfileMapDto.getMapDto()).getIdWorkShop());
        }
        if (StringUtils.isNotBlank(platformProfileMapDto.getUrlPhoto())) {
            if (StringUtils.isNotBlank(tooltipText)) {
                tooltipText.append("\n");
            }
            String message = "";
            try {
                message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.urlInfo");
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
                message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.photoLocation");
            } catch (Exception e) {
                message = "Photo location";
            }
            tooltipText.append(message).append(": ").append(installationFolder).append(platformProfileMapDto.getUrlPhoto());
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
                    Session.getInstance().setMap(platformProfileMapDto.getMapDto());
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
        return (MainApplication.getPrimaryStage().getWidth() - (50 * mapsSlider.getValue()) - 98) / mapsSlider.getValue();
    }

    private void resizeGridPane(GridPane gridPane) {
        gridPane.setPrefWidth(getWidthGridPaneByNumberOfColums());
        ImageView mapPreview = (ImageView) gridPane.getChildren().get(0);
        mapPreview.setFitWidth(gridPane.getPrefWidth());
        mapPreview.setFitHeight(gridPane.getPrefWidth()/2);
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
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profileNotEmpty");
                Utils.warningDialog(headerText, contentText);
                return;
            }

            if (steamOfficialMapsTab.isSelected()) {
                SingleSelectionModel<Tab> selectionModel = mapsTabPane.getSelectionModel();
                selectionModel.select(steamCustomMapsTab);
            }
            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.addCustomMaps");

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
                                    List<PlatformProfileMapDto> mapAddedList = facade.addCustomMapsToProfile(
                                            platformNameList,
                                            addMapsToPlatformProfile.getProfileName(),
                                            addMapsToPlatformProfile.getMapList(),
                                            languageCode,
                                            profileSelect.getValue().getName(),
                                            success,
                                            errors);

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
                                    List<PlatformProfileMapDto> mapAddedList = facade.addCustomMapsToProfile(
                                            platformNameList,
                                            addMapsToPlatformProfile.getProfileName(),
                                            addMapsToPlatformProfile.getMapList(),
                                            languageCode,
                                            profileSelect.getValue().getName(),
                                            success,
                                            errors);

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

                                List<PlatformProfileMapDto> mapAddedList = facade.addCustomMapsToProfile(
                                        platformNameList,
                                        addMapsToPlatformProfile.getProfileName(),
                                        addMapsToPlatformProfile.getMapList(),
                                        languageCode,
                                        profileSelect.getValue().getName(),
                                        success,
                                        errors);

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
                                Optional<PlatformProfileMapDto> platformProfileMapDtoOptional = facade.findPlatformProfileMapDtoByNames(EnumPlatform.STEAM.name(), profileSelect.getValue().getName(), platformProfileMap.getMapDto().getKey());
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
                                Optional<PlatformProfileMapDto> platformProfileMapDtoOptional = facade.findPlatformProfileMapDtoByNames(EnumPlatform.EPIC.name(), profileSelect.getValue().getName(), platformProfileMap.getMapDto().getKey());
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
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectProfiles");
                    if (StringUtils.isNotBlank(success)) {
                        String steamCustomMapsModsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.steamCustomMaps");
                        steamCustomMapsTab.setGraphic(createTabTitle(EnumPlatform.STEAM, false, steamCustomMapsModsTabText, steamCustomMapsFlowPane.getChildren().size()));

                        String epicCustomMapsModsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.epicCustomMaps");
                        epicCustomMapsTab.setGraphic(createTabTitle(EnumPlatform.EPIC, false, epicCustomMapsModsTabText, epicCustomMapsFlowPane.getChildren().size()));

                        headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.OperationDone");
                        Utils.infoDialog(headerText, success.toString());
                    }

                    if (StringUtils.isNotBlank(errors)) {
                        headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
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
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapsNotSelected");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectItems");
                Utils.warningDialog(headerText, contentText);

            } else {
                String question = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.deleteMapsQuestion");
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

                                steamPlatformProfileMapDtoList.clear();
                                steamPlatformProfileMapDtoList = facade.listPlatformProfileMaps(EnumPlatform.STEAM.name(), profileSelect.getValue().getName());
                                epicPlatformProfileMapDtoList.clear();
                                epicPlatformProfileMapDtoList = facade.listPlatformProfileMaps(EnumPlatform.EPIC.name(), profileSelect.getValue().getName());

                                String officialMapsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.officiaslMaps");
                                steamOfficialMapsTab.setGraphic(createTabTitle(EnumPlatform.STEAM, true, officialMapsTabText, steamOfficialMapsFlowPane.getChildren().size()));
                                String customMapsModsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.customMaps");
                                steamCustomMapsTab.setGraphic(createTabTitle(EnumPlatform.STEAM, false, customMapsModsTabText, steamCustomMapsFlowPane.getChildren().size()));

                                epicOfficialMapsTab.setGraphic(createTabTitle(EnumPlatform.EPIC, true, officialMapsTabText, epicOfficialMapsFlowPane.getChildren().size()));
                                epicCustomMapsTab.setGraphic(createTabTitle(EnumPlatform.EPIC, false, customMapsModsTabText, epicCustomMapsFlowPane.getChildren().size()));

                        }
                            if (StringUtils.isNotBlank(errors[0].toString())) {
                                logger.warn("Next maps/mods could not be deleted: " + errors[0].toString());
                                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapsNotDeleted");
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
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profileNotEmpty");
                Utils.warningDialog(headerText, contentText);
                return;
            }

            List<PlatformDto> allPlatforms = facade.listAllPlatforms();

            boolean invalidInstallationFolderFound = false;
            for (PlatformDto platform: allPlatforms) {
                if (facade.isCorrectInstallationFolder(platform.getKey())) {
                    if (!Files.exists(Paths.get(platform.getInstallationFolder() + "/KFGame/Cache"))) {
                        Files.createDirectory(Paths.get(platform.getInstallationFolder() + "/KFGame/Cache"));
                    }
                } else {
                    logger.warn("Invalid " + platform.getValue() + " installation folder was found. Define in Install/Update section:" + platform.getInstallationFolder());
                    invalidInstallationFolderFound = true;
                }

            }
            if (invalidInstallationFolderFound) {
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installationFolderNotValid");
                Utils.warningDialog(headerText, contentText);
                return;
            }

            List<PlatformProfileToDisplay> selectedPlatformProfileList = facade.selectProfilesToImport(profileSelect.getValue().getName());
            if (selectedPlatformProfileList == null || selectedPlatformProfileList.isEmpty()) {
                return;
            }

            Optional<PlatformDto> steamPlatformDtoOptional = allPlatforms.stream().filter(p -> p.getKey().equals(EnumPlatform.STEAM.name())).findFirst();
            Optional<PlatformDto> epicPlatformDtoOptional = allPlatforms.stream().filter(p -> p.getKey().equals(EnumPlatform.EPIC.name())).findFirst();
            if (!steamPlatformDtoOptional.isPresent() || !epicPlatformDtoOptional.isPresent()) {
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.PlatformNotFound");
                Utils.warningDialog(headerText, contentText);
                return;
            }

            PlatformDto steamPlatform = steamPlatformDtoOptional.get();
            PlatformDto epicPlatform = epicPlatformDtoOptional.get();
            Kf2Common steamKf2Common = facade.getKf2Common(steamPlatform.getKey());
            Kf2Common epicKf2Common = facade.getKf2Common(epicPlatform.getKey());

            // CUSTOM MAP / MOD LIST FOR STEAM
            List<MapToDisplay> steamCustomMapModList = Files.walk(Paths.get(steamPlatform.getInstallationFolder() + "/KFGame/Cache"))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().toUpperCase().startsWith("KF-"))
                    .filter(path -> path.getFileName().toString().toUpperCase().endsWith(".KFM"))
                    .map(path -> {
                        String filenameWithExtension = path.getFileName().toString();
                        String[] array = filenameWithExtension.split("\\.");
                        String mapName = array[0];
                        Long idWorkShop = steamKf2Common.getIdWorkShopFromPath(path.getParent());
                        return new MapToDisplay(idWorkShop, mapName);
                    })
                    .collect(Collectors.toList());

            File[] steamCacheFolderList = new File(steamPlatform.getInstallationFolder() + "/KFGame/Cache").listFiles();
            if (steamCacheFolderList != null && steamCacheFolderList.length > 0) {
                List<Long> idWorkShopCustomMapList = steamCustomMapModList.stream().map(MapToDisplay::getIdWorkShop).collect(Collectors.toList());
                steamCustomMapModList.addAll(
                        Arrays.stream(steamCacheFolderList)
                                .filter(file -> file.isDirectory())
                                .map(file -> file.toPath())
                                .filter(path -> !idWorkShopCustomMapList.contains(steamKf2Common.getIdWorkShopFromPath(path)))
                                .map(path -> {
                                    Long idWorkShop = Long.parseLong(path.getFileName().toString());
                                    return new MapToDisplay(idWorkShop, "MOD [" + idWorkShop + "]");
                                })
                                .collect(Collectors.toList())
                );
            }

            // CUSTOM MAP / MOD LIST FOR EPIC
            List<MapToDisplay> epicCustomMapModList = Files.walk(Paths.get(epicPlatform.getInstallationFolder() + "/KFGame/Cache"))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().toUpperCase().startsWith("KF-"))
                    .filter(path -> path.getFileName().toString().toUpperCase().endsWith(".KFM"))
                    .map(path -> {
                        String filenameWithExtension = path.getFileName().toString();
                        String[] array = filenameWithExtension.split("\\.");
                        String mapName = array[0];
                        Long idWorkShop = epicKf2Common.getIdWorkShopFromPath(path.getParent());
                        return new MapToDisplay(idWorkShop, mapName);
                    })
                    .collect(Collectors.toList());

            File[] epicCacheFolderList = new File(epicPlatform.getInstallationFolder() + "/KFGame/Cache").listFiles();
            if (epicCacheFolderList != null && epicCacheFolderList.length > 0) {
                List<Long> idWorkShopCustomMapList = epicCustomMapModList.stream().map(MapToDisplay::getIdWorkShop).collect(Collectors.toList());
                epicCustomMapModList.addAll(
                        Arrays.stream(epicCacheFolderList)
                                .filter(file -> file.isDirectory())
                                .map(file -> file.toPath())
                                .filter(path -> !idWorkShopCustomMapList.contains(epicKf2Common.getIdWorkShopFromPath(path)))
                                .map(path -> {
                                    Long idWorkShop = Long.parseLong(path.getFileName().toString());
                                    return new MapToDisplay(idWorkShop, "MOD [" + idWorkShop + "]");
                                })
                                .collect(Collectors.toList())
                );
            }

            // OFFICIAL MAP LIST FOR STEAM
            List<String> steamOfficialMapNameList = Files.walk(Paths.get( steamPlatform.getInstallationFolder() + "/KFGame/BrewedPC/Maps"))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().toUpperCase().startsWith("KF-"))
                    .filter(path -> path.getFileName().toString().toUpperCase().endsWith(".KFM"))
                    .map(path -> {
                        String filenameWithExtension = path.getFileName().toString();
                        String[] array = filenameWithExtension.split("\\.");
                        return array[0];
                    })
                    .collect(Collectors.toList());

            // OFFICIAL MAP LIST FOR EPIC
            List<String> epicOfficialMapNameList = Files.walk(Paths.get(epicPlatform.getInstallationFolder() + "/KFGame/BrewedPC/Maps"))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().toUpperCase().startsWith("KF-"))
                    .filter(path -> path.getFileName().toString().toUpperCase().endsWith(".KFM"))
                    .map(path -> {
                        String filenameWithExtension = path.getFileName().toString();
                        String[] array = filenameWithExtension.split("\\.");
                        return array[0];
                    })
                    .collect(Collectors.toList());


            List<PlatformProfileMapToImport> officialMapPpmToImportList = new ArrayList<PlatformProfileMapToImport>();
            List<PlatformProfileMapToImport> customMapPpmToImportList = new ArrayList<PlatformProfileMapToImport>();
            for (PlatformProfileToDisplay selectedPlatformProfile: selectedPlatformProfileList) {
                List<PlatformDto> platformList = new ArrayList<PlatformDto>();

                switch (selectedPlatformProfile.getPlatformName()) {
                    case "Steam":
                        if (steamPlatformDtoOptional.isPresent()) {
                            platformList.add(steamPlatformDtoOptional.get());
                        }
                        break;

                    case "Epic Games":
                        if (epicPlatformDtoOptional.isPresent()) {
                            platformList.add(epicPlatformDtoOptional.get());
                        }
                        break;

                    case "All platforms":
                        if (steamPlatformDtoOptional.isPresent()) {
                            platformList.add(steamPlatformDtoOptional.get());
                        }
                        if (epicPlatformDtoOptional.isPresent()) {
                            platformList.add(epicPlatformDtoOptional.get());
                        }
                        break;
                }

                for (PlatformDto platform: platformList) {
                    try {
                        String headerText = facade.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectCustomMaps",
                                selectedPlatformProfile.getProfileName(),
                                platform.getValue());


                        // Not present Official Maps
                        List<MapToDisplay> notPresentOfficialMapList = new ArrayList<MapToDisplay>();
                        if (EnumPlatform.STEAM.name().equals(platform.getKey())) {
                            notPresentOfficialMapList = facade.getNotPresentOfficialMapList(steamOfficialMapNameList, platform.getKey(), selectedPlatformProfile.getProfileName());
                        } else {
                            notPresentOfficialMapList = facade.getNotPresentOfficialMapList(epicOfficialMapNameList, platform.getKey(), selectedPlatformProfile.getProfileName());
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
                            selectedCustomMapModList = Utils.selectMapsDialog(headerText, steamCustomMapModList);
                        } else {
                            selectedCustomMapModList = Utils.selectMapsDialog(headerText, epicCustomMapModList);
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

            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.importOperation");
            String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.confirmImportOperation");
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

                        String officialMapsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.officiaslMaps");
                        String customMapsModsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.customMaps");

                        steamOfficialMapsTab.setGraphic(createTabTitle(EnumPlatform.STEAM, true, officialMapsTabText, steamOfficialMapsFlowPane.getChildren().size()));
                        epicOfficialMapsTab.setGraphic(createTabTitle(EnumPlatform.EPIC, true, officialMapsTabText, epicOfficialMapsFlowPane.getChildren().size()));
                        steamCustomMapsTab.setGraphic(createTabTitle(EnumPlatform.STEAM, false, customMapsModsTabText, steamCustomMapsFlowPane.getChildren().size()));
                        epicCustomMapsTab.setGraphic(createTabTitle(EnumPlatform.EPIC, false, customMapsModsTabText, epicCustomMapsFlowPane.getChildren().size()));

                        progressIndicator.setVisible(false);
                        logger.info("The process to import maps and mods from the server to the launcher has finished.");


                        List<String> profileNameList = importMapResultToDisplayList.stream().map(ImportMapResultToDisplay::getProfileName).distinct().collect(Collectors.toList());
                        if (profileNameList == null || profileNameList.isEmpty()) {
                            String importMapsFromServerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.importMaps");
                            String noNewMapsFromServerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.noMapsImportedWarning");
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

    private void addOfficialMapToPlatformTabs(PlatformProfileMapToImport importedPpm, String mapName) throws SQLException {

        if (EnumPlatform.STEAM.name().equals(importedPpm.getPlatformName()) &&
                !steamPlatformProfileMapDtoList.stream().
                        filter(ppm -> ppm.getMapDto().isOfficial() && ppm.getMapDto().getKey().equals(mapName)).
                        findFirst().
                        isPresent()) {

            Optional<PlatformProfileMapDto> profileMapDtoOptional = facade.findPlatformProfileMapDtoByNames(
                    importedPpm.getPlatformName(),
                    importedPpm.getProfileName(),
                    mapName
            );

            if (profileMapDtoOptional.isPresent()) {
                steamPlatformProfileMapDtoList.add(profileMapDtoOptional.get());
            }
        }

        if (EnumPlatform.EPIC.name().equals(importedPpm.getPlatformName()) &&
                !epicPlatformProfileMapDtoList.stream().
                        filter(ppm -> ppm.getMapDto().isOfficial() && ppm.getMapDto().getKey().equals(mapName)).
                        findFirst().
                        isPresent()) {

            Optional<PlatformProfileMapDto> profileMapDtoOptional = facade.findPlatformProfileMapDtoByNames(
                    importedPpm.getPlatformName(),
                    importedPpm.getProfileName(),
                    mapName
            );

            if (profileMapDtoOptional.isPresent()) {
                epicPlatformProfileMapDtoList.add(profileMapDtoOptional.get());
            }
        }
    }

    private void addCustomMapToPlatformTabs(PlatformProfileMapToImport importedPpm, String mapName) throws SQLException {

        PlatformProfileMapToImport finalImportedPpm = importedPpm;
        if (EnumPlatform.STEAM.name().equals(importedPpm.getPlatformName()) &&
                !steamPlatformProfileMapDtoList.stream().
                        filter(ppm -> !ppm.getMapDto().isOfficial() && ((CustomMapModDto) ppm.getMapDto()).getIdWorkShop().equals(finalImportedPpm.getMapToDisplay().getIdWorkShop())).
                        findFirst().
                        isPresent()) {

            Optional<PlatformProfileMapDto> profileMapDtoOptional = facade.findPlatformProfileMapDtoByNames(
                    importedPpm.getPlatformName(),
                    importedPpm.getProfileName(),
                    mapName
            );

            if (profileMapDtoOptional.isPresent()) {
                steamPlatformProfileMapDtoList.add(profileMapDtoOptional.get());
            }
        }

        if (EnumPlatform.EPIC.name().equals(importedPpm.getPlatformName()) &&
                !epicPlatformProfileMapDtoList.stream().
                        filter(ppm -> !ppm.getMapDto().isOfficial() && ((CustomMapModDto) ppm.getMapDto()).getIdWorkShop().equals(finalImportedPpm.getMapToDisplay().getIdWorkShop())).
                        findFirst().
                        isPresent()) {

            Optional<PlatformProfileMapDto> profileMapDtoOptional = facade.findPlatformProfileMapDtoByNames(
                    importedPpm.getPlatformName(),
                    importedPpm.getProfileName(),
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

        PlatformProfileMapToImport importedPpm = null;

        AbstractMapDto mapDto = null;
        List<ImportMapResultToDisplay> importMapResultToDisplayList = new ArrayList<ImportMapResultToDisplay>();
        for (PlatformProfileMapToImport ppmToImport: ppmToImportList) {
            try {
                importedPpm = facade.importOfficialMapFromServer(
                        ppmToImport,
                        profileSelect.getValue().getName()
                );

                mapDto = facade.getOfficialMapByName(importedPpm.getMapToDisplay().getCommentary());
                if (mapDto == null) {
                    String errorMessage = "Can not find the map with name: " + importedPpm.getMapToDisplay().getCommentary();
                    logger.error(errorMessage);
                    throw new RuntimeException(errorMessage);
                }

                importMapResultToDisplayList.add(new ImportMapResultToDisplay(
                        importedPpm.getProfileName(),
                        importedPpm.getPlatformName(),
                        mapDto.getKey(),
                        mapDto.isOfficial(),
                        new Date(),
                        StringUtils.EMPTY
                ));

            } catch (Exception e) {
                String errorMessage = "Error importing the official map with name: " + ppmToImport.getMapToDisplay().getCommentary() + " for platform " + ppmToImport.getPlatformName() + " and profile " + ppmToImport.getProfileName();
                logger.error(errorMessage, e);
                importMapResultToDisplayList.add(new ImportMapResultToDisplay(
                        ppmToImport.getProfileName(),
                        ppmToImport.getPlatformName(),
                        ppmToImport.getMapToDisplay().getCommentary(),
                        true,
                        null,
                        errorMessage
                ));
                continue;
            }

            if (importedPpm.getProfileName().equals(profileSelect.getValue().getName())) {
                try {
                    addOfficialMapToPlatformTabs(importedPpm, mapDto.getKey());
                } catch (Exception e) {
                    String errorMessage = "Can not show the imported official map with name " + importedPpm.getMapToDisplay().getCommentary() + " in the platform " + importedPpm.getPlatformName();
                    logger.error(errorMessage, e);
                }
            }
        }

        return importMapResultToDisplayList;
    }

    private List<ImportMapResultToDisplay> importCustomMapsModsFromServer(List<PlatformProfileMapToImport> ppmToImportList) {
        if (ppmToImportList == null || ppmToImportList.isEmpty()) {
            return new ArrayList<ImportMapResultToDisplay>();
        }
        PlatformProfileMapToImport importedPpm = null;

        AbstractMapDto mapDto = null;
        List<ImportMapResultToDisplay> importMapResultToDisplayList = new ArrayList<ImportMapResultToDisplay>();
        for (PlatformProfileMapToImport ppmToImport: ppmToImportList) {
            try {
                importedPpm = facade.importCustomMapModFromServer(
                        ppmToImport,
                        profileSelect.getValue().getName()
                );

                mapDto = facade.getMapByIdWorkShop(importedPpm.getMapToDisplay().getIdWorkShop());
                if (mapDto == null) {
                    String errorMessage = "Can not find the map with idWorkShop: " + importedPpm.getMapToDisplay().getIdWorkShop();
                    logger.error(errorMessage);
                    throw new RuntimeException(errorMessage);
                }

                importMapResultToDisplayList.add(new ImportMapResultToDisplay(
                        importedPpm.getProfileName(),
                        importedPpm.getPlatformName(),
                        mapDto.getKey(),
                        mapDto.isOfficial(),
                        new Date(),
                        StringUtils.EMPTY
                ));

            } catch (Exception e) {
                String errorMessage = "Error importing the custom map/mod with idWorkShop: " + ppmToImport.getMapToDisplay().getIdWorkShop() + " for platform " + ppmToImport.getPlatformName() + " and profile " + ppmToImport.getProfileName();
                logger.error(errorMessage, e);
                importMapResultToDisplayList.add(new ImportMapResultToDisplay(
                        ppmToImport.getProfileName(),
                        ppmToImport.getPlatformName(),
                        ppmToImport.getMapToDisplay().getCommentary(),
                        false,
                        null,
                        errorMessage
                ));
                continue;
            }

            if (importedPpm.getProfileName().equals(profileSelect.getValue().getName())) {
                try {
                    addCustomMapToPlatformTabs(importedPpm, mapDto.getKey());
                } catch (Exception e) {
                    String errorMessage = "Can not show the imported custom map / mod with name " + importedPpm.getMapToDisplay().getCommentary() + " in the platform " + importedPpm.getPlatformName();
                    logger.error(errorMessage, e);
                }
            }
        }

        return importMapResultToDisplayList;
    }


    @FXML
    private void searchInWorkShopOnAction() {
        Session.getInstance().setMap(null);
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

            if (facade.isCorrectInstallationFolder(EnumPlatform.STEAM.name())) {
                steamPlatformProfileMapDtoList = facade.listPlatformProfileMaps(
                        EnumPlatform.STEAM.name(),
                        profileSelect.getValue().getName()
                );
            }

            if (facade.isCorrectInstallationFolder(EnumPlatform.EPIC.name())) {
                epicPlatformProfileMapDtoList = facade.listPlatformProfileMaps(
                        EnumPlatform.EPIC.name(),
                        profileSelect.getValue().getName()
                );
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

            String officialMapsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.officiaslMaps");
            String customMapsModsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.customMaps");

            steamOfficialMapsTab.setGraphic(createTabTitle(EnumPlatform.STEAM, true, officialMapsTabText, steamOfficialMapsFlowPane.getChildren().size()));
            steamCustomMapsTab.setGraphic(createTabTitle(EnumPlatform.STEAM, false, customMapsModsTabText, steamCustomMapsFlowPane.getChildren().size()));
            epicOfficialMapsTab.setGraphic(createTabTitle(EnumPlatform.EPIC, true, officialMapsTabText, steamOfficialMapsFlowPane.getChildren().size()));
            epicCustomMapsTab.setGraphic(createTabTitle(EnumPlatform.EPIC, true, customMapsModsTabText, steamCustomMapsFlowPane.getChildren().size()));

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
                Boolean map1Downloaded = ppm1.isDownloaded();
                Boolean map2Downloaded = ppm2.isDownloaded();
                return map1Downloaded.compareTo(map2Downloaded);
            }).collect(Collectors.toList());
            epicPlatformProfileMapDtoList = epicPlatformProfileMapDtoList.stream().
                    filter(ppm -> !ppm.getMapDto().isOfficial()).
                    sorted((ppm1, ppm2) -> {
                        Boolean map1Downloaded = ppm1.isDownloaded();
                        Boolean map2Downloaded = ppm2.isDownloaded();
                        return map1Downloaded.compareTo(map2Downloaded);
                    }).collect(Collectors.toList());

            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.DOWNLOAD_ASC);
        } else {
            steamPlatformProfileMapDtoList = steamPlatformProfileMapDtoList.stream().
                    filter(ppm -> !ppm.getMapDto().isOfficial()).
                    sorted((ppm1, ppm2) -> {
                Boolean map1Downloaded = ppm1.isDownloaded();
                Boolean map2Downloaded = ppm2.isDownloaded();
                return map2Downloaded.compareTo(map1Downloaded);
            }).collect(Collectors.toList());
            epicPlatformProfileMapDtoList = epicPlatformProfileMapDtoList.stream().
                    filter(ppm -> !ppm.getMapDto().isOfficial()).
                    sorted((ppm1, ppm2) -> {
                        Boolean map1Downloaded = ppm1.isDownloaded();
                        Boolean map2Downloaded = ppm2.isDownloaded();
                        return map2Downloaded.compareTo(map1Downloaded);
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

    private List<PlatformProfileMap> getSteamEditList() throws SQLException {

        List<PlatformProfileMap> editList = new ArrayList<PlatformProfileMap>();
        ObservableList<Node> nodeList = FXCollections.concat(steamOfficialMapsFlowPane.getChildren(), steamCustomMapsFlowPane.getChildren());

        if (nodeList != null && !nodeList.isEmpty()) {
            for (Node node : nodeList) {
                GridPane gridpane = (GridPane) node;
                Label aliasLabel = (Label) gridpane.getChildren().get(1);
                Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                CheckBox checkbox = (CheckBox) aliasLabel.getGraphic();

                if (checkbox.isSelected()) {
                    String mapName = mapNameLabel.getText();
                    Optional<PlatformProfileMap> profileMapOptional = facade.findPlatformProfileMapByNames(
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

    private List<PlatformProfileMap> getEpicEditList() throws Exception {

        List<PlatformProfileMap> editList = new ArrayList<PlatformProfileMap>();
        ObservableList<Node> nodeList = FXCollections.concat(epicOfficialMapsFlowPane.getChildren(), epicCustomMapsFlowPane.getChildren());

        if (nodeList != null && !nodeList.isEmpty()) {
            for (Node node : nodeList) {
                GridPane gridpane = (GridPane) node;
                Label aliasLabel = (Label) gridpane.getChildren().get(1);
                Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                CheckBox checkbox = (CheckBox) aliasLabel.getGraphic();

                if (checkbox.isSelected()) {
                    String mapName = mapNameLabel.getText();
                    Optional<PlatformProfileMap> profileMapOptional = facade.findPlatformProfileMapByNames(
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
            List<PlatformProfileMap> editList = getSteamEditList();
            editList.addAll(getEpicEditList());

            if (editList.isEmpty()) {
                logger.warn("No selected maps/mods to edit. You must select at least one item to be editted");
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapsNotSelected");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectItems");
                Utils.warningDialog(headerText, contentText);

            } else {
                Session.getInstance().setProfileMapList(editList);
                loadNewContent("/views/mapEdition.fxml");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }
}
