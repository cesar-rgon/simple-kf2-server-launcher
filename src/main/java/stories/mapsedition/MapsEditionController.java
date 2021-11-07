package stories.mapsedition;

import dtos.*;
import entities.ProfileMap;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.AddMapsToProfile;
import pojos.ImportMapResultToDisplay;
import pojos.MapToDisplay;
import pojos.enums.EnumMasTab;
import pojos.enums.EnumSortedMapsCriteria;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import pojos.session.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import start.MainApplication;
import utils.Utils;

import javax.swing.text.html.Option;
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
    private String installationFolder;
    private List<ProfileMapDto> profileMapDtoList;
    private boolean selectMaps;

    @FXML private Slider mapsSlider;
    @FXML private FlowPane officialMapsFlowPane;
    @FXML private FlowPane customMapsFlowPane;
    @FXML private TextField searchMaps;
    @FXML private MenuItem addNewMaps;
    @FXML private MenuItem searchInWorkShop;
    @FXML private MenuItem removeMaps;
    @FXML private CheckBox selectAllMaps;
    @FXML private MenuItem importMapsFromServer;
    @FXML private TabPane mapsModsTabPane;
    @FXML private Tab customMapsModsTab;
    @FXML private Tab officialMapsTab;
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
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            selectMaps = true;
            installationFolder = facade.findConfigPropertyValue("prop.config.installationFolder");

            String profileLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.profile");
            profileLabel.setText(profileLabelText);

            String actionsLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.actions");
            actionsLabel.setText(actionsLabelText);

            ObservableList<ProfileDto> profileOptions = facade.listAllProfiles();
            profileSelect.setItems(profileOptions);
            if (!profileOptions.isEmpty()) {
                profileSelect.setValue(Session.getInstance().getMapsProfile() != null?
                        Session.getInstance().getMapsProfile():
                        Session.getInstance().getActualProfile() != null?
                                Session.getInstance().getActualProfile():
                                profileSelect.getItems().get(0));

                profileMapDtoList = facade.listProfileMaps(profileSelect.getValue().getName());;
                orderMapsByNameOnAction();
            } else {
                profileSelect.setValue(null);
                profileMapDtoList = null;
            }
            Session.getInstance().setMapsProfile(profileSelect.getValue());

            String officialMapsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.officiaslMaps");
            officialMapsTab.setGraphic(createTabTitle(officialMapsTabText, officialMapsFlowPane.getChildren().size()));
            officialMapsTab.setText("");

            String customMapsModsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.customMaps");
            customMapsModsTab.setGraphic(createTabTitle(customMapsModsTabText, customMapsFlowPane.getChildren().size()));
            customMapsModsTab.setText("");

            SingleSelectionModel<Tab> selectionModel = mapsModsTabPane.getSelectionModel();
            selectionModel.select(EnumMasTab.OFFICIAL_MAPS_TAB.equals(Session.getInstance().getSelectedMapTab()) ? officialMapsTab: customMapsModsTab);

            Tooltip searchTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.searchMaps"));
            searchMaps.setTooltip(searchTooltip);
            Tooltip.install(searchImg, searchTooltip);

            String addNewMapsText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.addMaps");
            addNewMaps.setText(addNewMapsText);

            String searchInWorkShopText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.searchInWorkShop");
            searchInWorkShop.setText(searchInWorkShopText);

            String removeMapsText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.removeMaps");
            removeMaps.setText(removeMapsText);

            String selectAllMapsText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.selectMaps");
            selectAllMaps.setText(selectAllMapsText);
            selectAllMaps.setTooltip(new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.selectAllMaps")));

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
            sliderLabel.setTooltip(sliderTooltip);
            Double sliderColumns = Double.parseDouble(facade.findConfigPropertyValue("prop.config.mapSliderValue"));
            mapsSlider.setValue(sliderColumns);
            mapsSlider.setTooltip(sliderTooltip);
            mapsSliderOnMouseClicked();

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
    }


    private HBox createTabTitle(String text, int numberOfMaps) {

        Circle circle = new Circle(0,0, 12, Color.LIGHTBLUE);
        Text numberOfMapsText = new Text(String.valueOf(numberOfMaps));
        numberOfMapsText.setFont(Font.loadFont(getClass().getClassLoader().getResource("fonts/Ubuntu-B.ttf").toExternalForm(), 11));
        numberOfMapsText.setFill(Color.DARKBLUE);
        StackPane stackPane = new StackPane(circle, numberOfMapsText);

        Text title = new Text(text);
        title.setFill(Color.WHITE);

        HBox separator = new HBox();
        separator.setPrefWidth(5);

        HBox contentPane = new HBox();
        contentPane.getChildren().addAll(title,separator,stackPane);

        return contentPane;
    }

    private GridPane createMapGridPane(ProfileMapDto profileMapDto) {

        ImageView mapPreview = new ImageView();
        try {
            Image image = null;
            if (facade.isCorrectInstallationFolder(installationFolder) && StringUtils.isNotBlank(profileMapDto.getUrlPhoto())) {
                image = new Image("file:" + installationFolder + "/" + profileMapDto.getUrlPhoto());
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

        Label aliasLabel = new Label(profileMapDto.getAlias(), checkbox);
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
        try {
            datePattern = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.code.datePattern");
            dateHourPattern = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.code.dateHourPattern");
            unknownStr = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.unknown");
            releaseStr = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.release");
            importedStr = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.imported");
            mapNameText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.name");
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
        Label mapNameLabel = new Label(profileMapDto.getMapDto().getKey(), mapNameGraphic);
        mapNameLabel.setMaxWidth(Double.MAX_VALUE);
        mapNameLabel.setAlignment(Pos.BOTTOM_CENTER);
        mapNameLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 11;");
        mapNameLabel.setPadding(new Insets(10,0,0,0));
        gridpane.add(mapNameLabel, 1, 3);


        Text releaseDateGraphic = new Text(releaseStr + ": ");
        releaseDateGraphic.setFill(Color.GRAY);
        String releaseDateStr = profileMapDto.getReleaseDate() != null ? profileMapDto.getReleaseDate().format(DateTimeFormatter.ofPattern(datePattern)): unknownStr;
        Label releaseDateLabel = new Label(releaseDateStr, releaseDateGraphic);
        releaseDateLabel.setMaxWidth(Double.MAX_VALUE);
        releaseDateLabel.setAlignment(Pos.BOTTOM_CENTER);
        releaseDateLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 11;");
        gridpane.add(releaseDateLabel, 1, 4);

        Text importedDateGraphic = new Text(importedStr + ": ");
        importedDateGraphic.setFill(Color.GRAY);

        Label importedDateText = new Label(profileMapDto.getImportedDate().format(DateTimeFormatter.ofPattern(dateHourPattern)), importedDateGraphic);
        importedDateText.setMaxWidth(Double.MAX_VALUE);
        importedDateText.setAlignment(Pos.BOTTOM_CENTER);
        importedDateText.setStyle("-fx-text-fill: gray; -fx-font-size: 11;");
        gridpane.add(importedDateText, 1, 5);

        int rowIndex = 6;
        if (profileMapDto.getMapDto().isOfficial()) {
            importedDateText.setPadding(new Insets(0,0,10,0));
        } else {
            String labelText = "";
            if (((CustomMapModDto) profileMapDto.getMapDto()).isDownloaded()) {
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
                                    "< " + new Date() + " - Run Server >\n" + facade.runServer(profileSelect.getValue().getName()));
                        } catch (SQLException ex) {
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
        if (!profileMapDto.getMapDto().isOfficial()) {
            tooltipText.append("id WorkShop: ").append(((CustomMapModDto)profileMapDto.getMapDto()).getIdWorkShop());
        }
        if (StringUtils.isNotBlank(profileMapDto.getUrlPhoto())) {
            if (StringUtils.isNotBlank(tooltipText)) {
                tooltipText.append("\n");
            }
            String message = "";
            try {
                message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.urlInfo");
            } catch (Exception e) {
                message = "Url info";
            }
            tooltipText.append(message).append(": ").append(profileMapDto.getUrlInfo());
        }
        if (profileMapDto.getMapDto().isOfficial() || ((CustomMapModDto) profileMapDto.getMapDto()).isDownloaded()) {
            if (StringUtils.isNotBlank(tooltipText)) {
                tooltipText.append("\n");
            }
            String message = "";
            try {
                message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.photoLocation");
            } catch (Exception e) {
                message = "Photo location";
            }
            tooltipText.append(message).append(": ").append(installationFolder).append(profileMapDto.getUrlPhoto());
        }

        if (StringUtils.isNotBlank(tooltipText)) {
            Tooltip tooltip = new Tooltip(tooltipText.toString());
            Tooltip.install(gridpane, tooltip);
        }

        if (StringUtils.isNotBlank(profileMapDto.getUrlInfo())) {
            gridpane.setCursor(Cursor.HAND);
            gridpane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Session.getInstance().setMap(profileMapDto.getMapDto());
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
        for (int index = 0; index < officialMapsFlowPane.getChildren().size(); index++) {
            GridPane gridPane = (GridPane)officialMapsFlowPane.getChildren().get(index);
            resizeGridPane(gridPane);
        }
        for (int index = 0; index < customMapsFlowPane.getChildren().size(); index++) {
            GridPane gridPane = (GridPane)customMapsFlowPane.getChildren().get(index);
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
        if (StringUtils.isNotBlank(searchMaps.getText())) {
            selectAllMaps.setSelected(false);
            selectAllMapsOnAction();
            selectAllMaps.setVisible(false);
        } else {
            selectAllMaps.setVisible(true);
        }
        officialMapsFlowPane.getChildren().clear();
        customMapsFlowPane.getChildren().clear();
        for (ProfileMapDto profileMapDto: profileMapDtoList) {
            String mapName = StringUtils.upperCase(profileMapDto.getMapDto().getKey());
            String alias = StringUtils.upperCase(profileMapDto.getAlias());
            if (mapName.contains(StringUtils.upperCase(searchMaps.getText())) || alias.contains(StringUtils.upperCase(searchMaps.getText()))) {
                GridPane gridpane = createMapGridPane(profileMapDto);
                if (profileMapDto.getMapDto().isOfficial()) {
                    officialMapsFlowPane.getChildren().add(gridpane);
                } else {
                    customMapsFlowPane.getChildren().add(gridpane);
                }
            }
        }
    }

    @FXML
    private void addNewMapsOnAction() {
        List<AddMapsToProfile> addMapsToProfileList = new ArrayList<AddMapsToProfile>();

        try {
            if (!facade.isCorrectInstallationFolder(installationFolder)) {
                logger.warn("Installation folder can not be empty and can not contains whitespaces. Define in Install/Update section: " + installationFolder);
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installationFolderNotValid");
                Utils.warningDialog(headerText, contentText);
                return;
            }
            if (profileSelect.getValue() == null) {
                logger.warn("No profiles defined. Add new maps aborted");
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profileNotEmpty");
                Utils.warningDialog(headerText, contentText);
                return;
            }

            if (officialMapsTab.isSelected()) {
                SingleSelectionModel<Tab> selectionModel = mapsModsTabPane.getSelectionModel();
                selectionModel.select(customMapsModsTab);
            }
            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.addCustomMaps");

            addMapsToProfileList = Utils.defineCustomMapsToAddPerProfile(headerText, profileSelect.getItems());

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }

        if (addMapsToProfileList != null && !addMapsToProfileList.isEmpty()) {
            progressIndicator.setVisible(true);
            StringBuffer success = new StringBuffer();
            StringBuffer errors = new StringBuffer();
            List<AddMapsToProfile> finalAddMapsToProfileList = addMapsToProfileList;

            Task<List<AbstractMapDto>> task = new Task<List<AbstractMapDto>>() {
                @Override
                protected List<AbstractMapDto> call() throws Exception {
                    List<AbstractMapDto> mapAddedListToActualProfile = new ArrayList<AbstractMapDto>();

                    for (AddMapsToProfile addMapsToProfile: finalAddMapsToProfileList) {
                        List<AbstractMapDto> mapAddedList = facade.addCustomMapsToProfile(
                                addMapsToProfile.getProfileName(),
                                addMapsToProfile.getMapList(),
                                languageCode,
                                installationFolder,
                                profileSelect.getValue().getName(),
                                success,
                                errors);

                        if (addMapsToProfile.getProfileName().equals(profileSelect.getValue().getName())) {
                            mapAddedListToActualProfile.addAll(mapAddedList);
                        }
                    }
                    return mapAddedListToActualProfile;
                }
            };

            task.setOnSucceeded(wse -> {
                List<AbstractMapDto> mapAddedListToActualProfile = task.getValue();
                if (mapAddedListToActualProfile != null && !mapAddedListToActualProfile.isEmpty()) {
                    mapAddedListToActualProfile.stream().forEach(customMapMod -> {
                        if (!profileMapDtoList.stream().filter(pm -> !pm.getMapDto().isOfficial() && ((CustomMapModDto) pm.getMapDto()).getIdWorkShop().equals(((CustomMapModDto) customMapMod).getIdWorkShop())).findFirst().isPresent()) {
                            try {
                                Optional<ProfileMapDto> profileMapDtoOptional = facade.findProfileMapDtoByNames(profileSelect.getValue().getName(), customMapMod.getKey());
                                if (profileMapDtoOptional.isPresent()) {
                                    profileMapDtoList.add(profileMapDtoOptional.get());
                                    GridPane gridpane = createMapGridPane(profileMapDtoOptional.get());
                                    customMapsFlowPane.getChildren().add(gridpane);
                                }
                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                            }
                        }
                    });
                }

                progressIndicator.setVisible(false);

                try {
                    if (StringUtils.isNotBlank(success)) {
                        String customMapsModsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.customMaps");
                        customMapsModsTab.setGraphic(createTabTitle(customMapsModsTabText, customMapsFlowPane.getChildren().size()));
                        String message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapsAdded");
                        Utils.infoDialog(message + ":", success.toString());
                    } else {
                        String mapsNotAddedText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapsNotAdded");
                        String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.checkUrlIdWorkshop");
                        Utils.infoDialog(mapsNotAddedText, contentText);
                    }
                    if (StringUtils.isNotBlank(errors)) {
                        logger.warn("Error adding next maps/mods to the launcher: " + errors.toString());
                        String message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapsNotAddedError");
                        Utils.warningDialog(message + ":", errors.toString());
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

    @FXML
    private void removeMapsOnAction() {
        try {
            if (!facade.isCorrectInstallationFolder(installationFolder)) {
                logger.warn("Installation folder can not be empty and can not contains whitespaces. Define in Install/Update section: " + installationFolder);
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installationFolderNotValid");
                Utils.warningDialog(headerText, contentText);
                return;
            }
            List<Node> removeList = new ArrayList<Node>();
            StringBuffer message = new StringBuffer();
            ObservableList<Node> officialNodes = officialMapsFlowPane.getChildren();
            ObservableList<Node> customNodes = customMapsFlowPane.getChildren();

            for (Node node : officialNodes) {
                GridPane gridpane = (GridPane) node;
                Label aliasLabel = (Label) gridpane.getChildren().get(1);
                Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                CheckBox checkbox = (CheckBox) aliasLabel.getGraphic();

                if (checkbox.isSelected()) {
                    removeList.add(gridpane);
                    message.append(mapNameLabel.getText()).append("\n");
                }
            }

            for (Node node : customNodes) {
                GridPane gridpane = (GridPane) node;
                Label aliasLabel = (Label) gridpane.getChildren().get(1);
                Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                CheckBox checkbox = (CheckBox) aliasLabel.getGraphic();

                if (checkbox.isSelected()) {
                    removeList.add(gridpane);
                    message.append(mapNameLabel.getText()).append("\n");
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
                    List<AbstractMapDto> mapsToRemove = new ArrayList<AbstractMapDto>();
                    StringBuffer errors = new StringBuffer();
                    for (Node node : removeList) {
                        try {
                            GridPane gridpane = (GridPane) node;
                            Label mapNameLabel = (Label) gridpane.getChildren().get(2);

                            if (profileSelect.getValue().getMap() != null && mapNameLabel.getText().equalsIgnoreCase(profileSelect.getValue().getMap().getKey())) {
                                facade.unselectProfileMap(profileSelect.getValue().getName());
                            }
                            AbstractMapDto map = facade.deleteMapFromProfile(mapNameLabel.getText(), profileSelect.getValue().getName(), installationFolder);
                            if (map != null) {
                                mapsToRemove.add(map);
                                if (map.isOfficial()) {
                                    officialMapsFlowPane.getChildren().remove(gridpane);
                                } else {
                                    customMapsFlowPane.getChildren().remove(gridpane);
                                }
                            } else {
                                errors.append(mapNameLabel.getText()).append("\n");
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                            Utils.errorDialog(e.getMessage(), e);
                        }
                    }

                    if (!mapsToRemove.isEmpty()) {
                        try {
                            profileMapDtoList.clear();
                            profileMapDtoList = facade.listProfileMaps(profileSelect.getValue().getName());;
                            String officialMapsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.officiaslMaps");
                            officialMapsTab.setGraphic(createTabTitle(officialMapsTabText, officialMapsFlowPane.getChildren().size()));
                            String customMapsModsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.customMaps");
                            customMapsModsTab.setGraphic(createTabTitle(customMapsModsTabText, customMapsFlowPane.getChildren().size()));
                        } catch (SQLException e) {
                            logger.error(e.getMessage(), e);
                            Utils.errorDialog(e.getMessage(), e);
                        }
                    }
                    if (StringUtils.isNotBlank(errors.toString())) {
                        logger.warn("Next maps/mods could not be deleted: " + errors.toString());
                        String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapsNotDeleted");
                        Utils.warningDialog(headerText, errors.toString());
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void importMapsFromServerOnAction() {

        List<String> officialMapNameList = new ArrayList<String>();
        List<MapToDisplay> selectedCustomMapModList = new ArrayList<MapToDisplay>();
        List<String> selectedProfileNameList = new ArrayList<String>();
        Optional<ButtonType> result = Optional.empty();

        try {
            if (!facade.isCorrectInstallationFolder(installationFolder)) {
                logger.warn("Installation folder can not be empty and can not contains whitespaces. Define in Install/Update section: " + installationFolder);
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installationFolderNotValid");
                Utils.warningDialog(headerText, contentText);
                return;
            }
            if (profileSelect.getValue() == null) {
                logger.warn("No profiles defined. Import operation aborted");
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profileNotEmpty");
                Utils.warningDialog(headerText, contentText);
                return;
            }

            selectedProfileNameList = facade.selectProfilesToImport(profileSelect.getValue().getName());
            if (selectedProfileNameList == null || selectedProfileNameList.isEmpty()) {
                return;
            }

            if (!Files.exists(Paths.get(installationFolder + "/KFGame/Cache"))) {
                Files.createDirectory(Paths.get(installationFolder + "/KFGame/Cache"));
            }

            // CUSTOM MAP LIST
            Kf2Common kf2Common = Kf2Factory.getInstance();
            List<MapToDisplay> customMapModList = Files.walk(Paths.get(installationFolder + "/KFGame/Cache"))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().toUpperCase().startsWith("KF-"))
                    .filter(path -> path.getFileName().toString().toUpperCase().endsWith(".KFM"))
                    .map(path -> {
                        String filenameWithExtension = path.getFileName().toString();
                        String[] array = filenameWithExtension.split("\\.");
                        String mapName = array[0];
                        Long idWorkShop = kf2Common.getIdWorkShopFromPath(path.getParent(), installationFolder);
                        return new MapToDisplay(idWorkShop, mapName);
                    })
                    .collect(Collectors.toList());

            // MOD LIST
            File[] cacheFolderList = new File(installationFolder + "/KFGame/Cache").listFiles();
            if (cacheFolderList != null && cacheFolderList.length > 0) {
                List<Long> idWorkShopCustomMapList = customMapModList.stream().map(MapToDisplay::getIdWorkShop).collect(Collectors.toList());
                customMapModList.addAll(
                        Arrays.stream(cacheFolderList)
                        .filter(file -> file.isDirectory())
                        .map(file -> file.toPath())
                        .filter(path -> !idWorkShopCustomMapList.contains(kf2Common.getIdWorkShopFromPath(path, installationFolder)))
                        .map(path -> {
                            Long idWorkShop = Long.parseLong(path.getFileName().toString());
                            return new MapToDisplay(idWorkShop, "MOD [" + idWorkShop + "]");
                        })
                        .collect(Collectors.toList())
                );
            }

            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectCustomMaps");
            selectedCustomMapModList = Utils.selectMapsDialog(headerText, customMapModList);

            officialMapNameList = Files.walk(Paths.get(installationFolder + "/KFGame/BrewedPC/Maps"))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().toUpperCase().startsWith("KF-"))
                    .filter(path -> path.getFileName().toString().toUpperCase().endsWith(".KFM"))
                    .map(path -> {
                        String filenameWithExtension = path.getFileName().toString();
                        String[] array = filenameWithExtension.split("\\.");
                        return array[0];
                    })
                    .collect(Collectors.toList());

            headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.importOperation");
            String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.confirmImportOperation");
            result = Utils.questionDialog(headerText, contentText);

        } catch (Exception e) {
            String message = "Error importing maps and mods from server to the launcher";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }

        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            progressIndicator.setVisible(true);
            List<ImportMapResultToDisplay> importMapResultToDisplayList = new ArrayList<ImportMapResultToDisplay>();

            List<MapToDisplay> finalSelectedCustomMapModList = selectedCustomMapModList;
            List<String> finalSelectedProfileNameList = selectedProfileNameList;
            List<String> finalOfficialMapNameList = officialMapNameList;

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    logger.info("Starting the process to import maps and mods from the server to the launcher");
                    importCustomMapsModsFromServer(finalSelectedCustomMapModList, finalSelectedProfileNameList, importMapResultToDisplayList);
                    importOfficialMapsFromServer(finalOfficialMapNameList, finalSelectedProfileNameList, importMapResultToDisplayList);
                    return null;
                }
            };
            task.setOnSucceeded(wse -> {
                try {
                    officialMapsFlowPane.getChildren().clear();
                    customMapsFlowPane.getChildren().clear();
                    profileMapDtoList.stream().forEach(profileMapDto -> {
                        GridPane gridpane = createMapGridPane(profileMapDto);
                        if (profileMapDto.getMapDto().isOfficial()) {
                            officialMapsFlowPane.getChildren().add(gridpane);
                        } else {
                            customMapsFlowPane.getChildren().add(gridpane);
                        }
                    });

                    String officialMapsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.officiaslMaps");
                    officialMapsTab.setGraphic(createTabTitle(officialMapsTabText, officialMapsFlowPane.getChildren().size()));
                    String customMapsModsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.customMaps");
                    customMapsModsTab.setGraphic(createTabTitle(customMapsModsTabText, customMapsFlowPane.getChildren().size()));

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

    }

    private void importOfficialMapsFromServer(List<String> officialMapNameList, List<String> selectedProfileNameList, List<ImportMapResultToDisplay> importMapResultToDisplayList) {
        if (officialMapNameList == null || officialMapNameList.isEmpty()) {
            return;
        }
        String mapNameLabel = "";
        try {
            mapNameLabel = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapName");
        } catch (Exception e) {
            mapNameLabel = "Map name";
        }

        for (String officialMapName: officialMapNameList) {
            OfficialMapDto officialMapImportedDto = facade.importOfficialMapFromServer(
                    officialMapName,
                    selectedProfileNameList,
                    profileSelect.getValue().getName(),
                    mapNameLabel,
                    importMapResultToDisplayList
            );


            if (selectedProfileNameList.contains(profileSelect.getValue().getName()) &&
                    !profileMapDtoList.stream().filter(pm -> pm.getMapDto().isOfficial() && pm.getMapDto().getKey().equalsIgnoreCase(officialMapImportedDto.getKey())).findFirst().isPresent()) {
                try {
                    Optional<ProfileMapDto> profileMapDtoOptional = facade.findProfileMapDtoByNames(profileSelect.getValue().getName(), officialMapImportedDto.getKey());
                    if (profileMapDtoOptional.isPresent()) {
                        profileMapDtoList.add(profileMapDtoOptional.get());
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    private void importCustomMapsModsFromServer(List<MapToDisplay> customMapModList, List<String> selectedProfileNameList, List<ImportMapResultToDisplay> importMapResultToDisplayList) {
        if (customMapModList == null || customMapModList.isEmpty()) {
            return;
        }
        String mapNameLabel = "";
        try {
            mapNameLabel = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapName");
        } catch (Exception e) {
            mapNameLabel = "Map name";
        }

        for (MapToDisplay customMapModToDisplay: customMapModList) {
            CustomMapModDto customMapModImportedDto = facade.importCustomMapModFromServer(
                    mapNameLabel,
                    customMapModToDisplay.getIdWorkShop(),
                    customMapModToDisplay.getCommentary(),
                    installationFolder,
                    selectedProfileNameList,
                    profileSelect.getValue().getName(),
                    importMapResultToDisplayList
            );

            if (selectedProfileNameList.contains(profileSelect.getValue().getName()) &&
                    !profileMapDtoList.stream().filter(pm -> !pm.getMapDto().isOfficial() && ((CustomMapModDto) pm.getMapDto()).getIdWorkShop().equals(customMapModImportedDto.getIdWorkShop())).findFirst().isPresent()) {
                try {
                    Optional<ProfileMapDto> profileMapDtoOptional = facade.findProfileMapDtoByNames(profileSelect.getValue().getName(), customMapModImportedDto.getKey());
                    if (profileMapDtoOptional.isPresent()) {
                        profileMapDtoList.add(profileMapDtoOptional.get());
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }


    @FXML
    private void searchInWorkShopOnAction() {
        Session.getInstance().setMap(null);
        Session.getInstance().setMapsProfile(profileSelect.getValue());
        loadNewContent("/views/mapWebInfo.fxml");
    }

    @FXML
    private void selectAllMapsOnAction() {
        ObservableList<Node> nodes = null;
        if (selectAllMaps.isSelected()) {
            if (officialMapsTab.isSelected()) {
                nodes = officialMapsFlowPane.getChildren();
            } else {
                nodes = customMapsFlowPane.getChildren();
            }
        } else {
            nodes = FXCollections.concat(officialMapsFlowPane.getChildren(), customMapsFlowPane.getChildren());
        }

        for (Node node: nodes) {
            GridPane gridpane = (GridPane) node;
            Label aliasLabel = (Label) gridpane.getChildren().get(1);
            CheckBox checkbox = (CheckBox) aliasLabel.getGraphic();
            checkbox.setSelected(selectMaps);
            if (checkbox.isSelected()) {
                checkbox.setOpacity(1);
            } else {
                checkbox.setOpacity(0.5);
            }
        }
        try {
            String labelText = "";
            if (selectMaps) {
                labelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.unselectMaps");
            } else {
                labelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.selectMaps");
            }
            selectMaps = !selectMaps;
            selectAllMaps.setText(labelText);
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void profileOnAction() {
        try {
            customMapsFlowPane.getChildren().clear();
            officialMapsFlowPane.getChildren().clear();
            profileMapDtoList = facade.listProfileMaps(profileSelect.getValue().getName());
            for (ProfileMapDto profileMapDto : profileMapDtoList) {
                GridPane gridpane = createMapGridPane(profileMapDto);
                if (profileMapDto.getMapDto().isOfficial()) {
                    officialMapsFlowPane.getChildren().add(gridpane);
                } else {
                    customMapsFlowPane.getChildren().add(gridpane);
                }
            }

            String officialMapsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.officiaslMaps");
            officialMapsTab.setGraphic(createTabTitle(officialMapsTabText, officialMapsFlowPane.getChildren().size()));
            String customMapsModsTabText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.customMaps");
            customMapsModsTab.setGraphic(createTabTitle(customMapsModsTabText, customMapsFlowPane.getChildren().size()));

            if (selectAllMaps.isSelected()) {
                selectAllMaps.setSelected(false);
                selectAllMapsOnAction();
            }
            Session.getInstance().setMapsProfile(profileSelect.getValue());
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void orderMapsByAliasOnAction() {
        customMapsFlowPane.getChildren().clear();
        officialMapsFlowPane.getChildren().clear();

        if (EnumSortedMapsCriteria.ALIAS_DESC.equals(Session.getInstance().getSortedMapsCriteria())) {
            profileMapDtoList = profileMapDtoList.stream().sorted((pm1, pm2) -> pm1.getAlias().compareTo(pm2.getAlias())).collect(Collectors.toList());
            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.ALIAS_ASC);
        } else {
            profileMapDtoList = profileMapDtoList.stream().sorted((pm1, pm2) -> pm2.getAlias().compareTo(pm1.getAlias())).collect(Collectors.toList());
            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.ALIAS_DESC);
        }

        for (ProfileMapDto profileMapDto : profileMapDtoList) {
            GridPane gridpane = createMapGridPane(profileMapDto);
            if (profileMapDto.getMapDto().isOfficial()) {
                officialMapsFlowPane.getChildren().add(gridpane);
            } else {
                customMapsFlowPane.getChildren().add(gridpane);
            }
        }
    }

    @FXML
    private void orderMapsByNameOnAction() {
        customMapsFlowPane.getChildren().clear();
        officialMapsFlowPane.getChildren().clear();

        if (EnumSortedMapsCriteria.NAME_DESC.equals(Session.getInstance().getSortedMapsCriteria())) {
            profileMapDtoList = profileMapDtoList.stream().sorted((pm1, pm2) -> pm1.getMapDto().getKey().compareTo(pm2.getMapDto().getKey())).collect(Collectors.toList());
            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.NAME_ASC);
        } else {
            profileMapDtoList = profileMapDtoList.stream().sorted((pm1, pm2) -> pm2.getMapDto().getKey().compareTo(pm1.getMapDto().getKey())).collect(Collectors.toList());
            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.NAME_DESC);
        }

        for (ProfileMapDto profileMapDto : profileMapDtoList) {
            GridPane gridpane = createMapGridPane(profileMapDto);
            if (profileMapDto.getMapDto().isOfficial()) {
                officialMapsFlowPane.getChildren().add(gridpane);
            } else {
                customMapsFlowPane.getChildren().add(gridpane);
            }
        }
    }

    @FXML
    private void officialMapsTabOnSelectionChanged() {
        if (customMapsModsTab != null && !officialMapsTab.isSelected()) {
            Session.getInstance().setSelectedMapTab(EnumMasTab.CUSTOM_MAPS_TAB);
        }
        if (selectAllMaps != null && selectAllMaps.isSelected()) {
            selectAllMaps.setSelected(false);
            selectAllMapsOnAction();
        }
    }

    @FXML
    private void customMapsModsTabOnSelectionChanged() {
        if (officialMapsTab != null && !customMapsModsTab.isSelected()) {
            Session.getInstance().setSelectedMapTab(EnumMasTab.OFFICIAL_MAPS_TAB);
        }
        if (selectAllMaps != null && selectAllMaps.isSelected()) {
            selectAllMaps.setSelected(false);
            selectAllMapsOnAction();
        }
    }

    @FXML
    private void orderMapsByReleaseDateOnAction() {
        customMapsFlowPane.getChildren().clear();
        officialMapsFlowPane.getChildren().clear();

        if (EnumSortedMapsCriteria.RELEASE_DATE_DESC.equals(Session.getInstance().getSortedMapsCriteria())) {
            List<ProfileMapDto> sortedMapList = new ArrayList<ProfileMapDto>(
                    profileMapDtoList.stream()
                            .filter(pm -> pm.getReleaseDate() != null)
                            .sorted((pm1, pm2) -> pm1.getReleaseDate().compareTo(pm2.getReleaseDate()))
                            .collect(Collectors.toList())
            );
            List<ProfileMapDto> mapWithoutReleaseDateList = new ArrayList<ProfileMapDto>(
                    profileMapDtoList.stream()
                            .filter(pm -> pm.getReleaseDate() == null)
                            .collect(Collectors.toList())
            );

            sortedMapList.addAll(mapWithoutReleaseDateList);
            profileMapDtoList = sortedMapList;
            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.RELEASE_DATE_ASC);
        } else {
            List<ProfileMapDto> sortedMapList = new ArrayList<ProfileMapDto>(
                    profileMapDtoList.stream()
                            .filter(pm -> pm.getReleaseDate() != null)
                            .sorted((pm1, pm2) -> pm2.getReleaseDate().compareTo(pm1.getReleaseDate()))
                            .collect(Collectors.toList())
            );
            List<ProfileMapDto> mapWithoutReleaseDateList = new ArrayList<ProfileMapDto>(
                    profileMapDtoList.stream()
                            .filter(pm -> pm.getReleaseDate() == null)
                            .collect(Collectors.toList())
            );

            sortedMapList.addAll(mapWithoutReleaseDateList);
            profileMapDtoList = sortedMapList;
            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.RELEASE_DATE_DESC);
        }

        for (ProfileMapDto profileMapDto : profileMapDtoList) {
            GridPane gridpane = createMapGridPane(profileMapDto);
            if (profileMapDto.getMapDto().isOfficial()) {
                officialMapsFlowPane.getChildren().add(gridpane);
            } else {
                customMapsFlowPane.getChildren().add(gridpane);
            }
        }
    }

    @FXML
    private void orderMapsByImportedDateOnAction() {
        customMapsFlowPane.getChildren().clear();
        officialMapsFlowPane.getChildren().clear();

        if (EnumSortedMapsCriteria.IMPORTED_DATE_DESC.equals(Session.getInstance().getSortedMapsCriteria())) {
            profileMapDtoList = profileMapDtoList.stream().sorted((pm1, pm2) -> pm1.getImportedDate().compareTo(pm2.getImportedDate())).collect(Collectors.toList());
            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.IMPORTED_DATE_ASC);
        } else {
            profileMapDtoList = profileMapDtoList.stream().sorted((pm1, pm2) -> pm2.getImportedDate().compareTo(pm1.getImportedDate())).collect(Collectors.toList());
            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.IMPORTED_DATE_DESC);
        }

        for (ProfileMapDto profileMapDto : profileMapDtoList) {
            GridPane gridpane = createMapGridPane(profileMapDto);
            if (profileMapDto.getMapDto().isOfficial()) {
                officialMapsFlowPane.getChildren().add(gridpane);
            } else {
                customMapsFlowPane.getChildren().add(gridpane);
            }
        }
    }

    @FXML
    private void orderMapsByDownloadOnAction() {
        if (officialMapsTab.isSelected()) {
            return;
        }
        customMapsFlowPane.getChildren().clear();

        if (EnumSortedMapsCriteria.DOWNLOAD_DESC.equals(Session.getInstance().getSortedMapsCriteria())) {
            profileMapDtoList = profileMapDtoList.stream().
                    filter(pm -> !pm.getMapDto().isOfficial()).
                    sorted((pm1, pm2) -> {
                Boolean map1Downloaded = ((CustomMapModDto) pm1.getMapDto()).isDownloaded();
                Boolean map2Downloaded = ((CustomMapModDto) pm2.getMapDto()).isDownloaded();
                return map1Downloaded.compareTo(map2Downloaded);
            }).collect(Collectors.toList());
            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.DOWNLOAD_ASC);
        } else {
            profileMapDtoList = profileMapDtoList.stream().
                    filter(pm -> !pm.getMapDto().isOfficial()).
                    sorted((pm1, pm2) -> {
                Boolean map1Downloaded = ((CustomMapModDto) pm1.getMapDto()).isDownloaded();
                Boolean map2Downloaded = ((CustomMapModDto) pm2.getMapDto()).isDownloaded();
                return map2Downloaded.compareTo(map1Downloaded);
            }).collect(Collectors.toList());
            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.DOWNLOAD_DESC);
        }

        for (ProfileMapDto profileMapDto : profileMapDtoList) {
            GridPane gridpane = createMapGridPane(profileMapDto);
            customMapsFlowPane.getChildren().add(gridpane);
        }
    }

    @FXML
    private void editMapsOnAction() {
        try {
            if (!facade.isCorrectInstallationFolder(installationFolder)) {
                logger.warn("Installation folder can not be empty and can not contains whitespaces. Define in Install/Update section: " + installationFolder);
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installationFolderNotValid");
                Utils.warningDialog(headerText, contentText);
                return;
            }

            ObservableList<Node> nodeList = null;
            List<ProfileMap> editList = new ArrayList<ProfileMap>();
            if (officialMapsTab.isSelected()) {
                nodeList = officialMapsFlowPane.getChildren();
            } else {
                nodeList = customMapsFlowPane.getChildren();
            }

            for (Node node : nodeList) {
                GridPane gridpane = (GridPane) node;
                Label aliasLabel = (Label) gridpane.getChildren().get(1);
                Label mapNameLabel = (Label) gridpane.getChildren().get(2);
                CheckBox checkbox = (CheckBox) aliasLabel.getGraphic();

                if (checkbox.isSelected()) {
                    String mapName = mapNameLabel.getText();
                    Optional<ProfileMap> profileMapOptional = facade.findProfileMapByNames(profileSelect.getValue().getName(), mapName);
                    if (profileMapOptional.isPresent()) {
                        editList.add(profileMapOptional.get());
                    }
                }
            }

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
