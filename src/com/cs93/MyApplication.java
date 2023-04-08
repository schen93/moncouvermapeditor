/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cs93;

import java.io.File;

import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;


import javafx.stage.DirectoryChooser;

import java.io.IOException;
import java.util.Optional;

import javafx.scene.layout.Background;

import javax.imageio.ImageIO;

/**
 *
 * @author schen
 */
public class MyApplication extends Application {
    static final double BASEWIDTH = 1920, BASEHEIGHT = 1080;
    static final double MAPWIDTH = 1500, MAPHEIGHT = 800;
    static final double ASSETWIDTH = 400, ASSETHEIGHT = 800;
/*    static final double BASEWIDTH = 800, BASEHEIGHT = 600;
    static final double MAPWIDTH = 500, MAPHEIGHT = 600;
    static final double ASSETWIDTH = 300, ASSETHEIGHT = 600;*/

    static final double MAPBUTTONWIDTH = 40, MAPBUTTONHEIGHT = 40;
    static final double ASSETBUTTONWIDTH = 100, ASSETBUTTONHEIGHT = 100;
    static final String BUTTONDEFAULTBGCOLOR = "#111111", BUTTONACTIVETEXTCOLOR = "#111111", BUTTONDEFAULTTEXTCOLOR = "#cccccc";
    static final Color CANVASBGCOLOR = Color.rgb(4, 4, 4, 1);
    static final int BUTTONDEFAULTTEXTSIZE = 8;
    static final Color BGCOLOR = Color.rgb(100, 100, 100, 1);
    private Stage primaryStage;
    private Scene baseScene;
    private DirectoryChooser directoryChooser;



    private SplitPane basePane;
    private TilePane mapTilePane, assetPane;
    private Canvas mapPreviewCanvas;
    private StackPane mapStackPane;
    VBox leftControl, rightControl;
    private File mapFolder;
    private Stop[] gradientStops = new Stop[]{};
    private LinearGradient linearGradient;

    private TextField mapName = new TextField();
    private TextField row = new TextField();
    private TextField column = new TextField();
    private TextField startColor = new TextField();
    private TextField color1 = new TextField();
    private TextField color2 = new TextField();
    private TextField endColor = new TextField();
    private TextField color1Pos = new TextField();
    private TextField color2Pos = new TextField();

    private TextField autoStartRow = new TextField();
    private TextField autoEndRow = new TextField();
    private TextField autoStartColumn = new TextField();
    private TextField autoEndColumn = new TextField();
    private TextField autoRowInterval = new TextField();
    private TextField autoColumnInterval = new TextField();
    private TextField autoAssetNameKey = new TextField();
    private CheckBox autoRandomPosition = new CheckBox();
    private CheckBox autoInOrder = new CheckBox();

    public static void main(String[] args) {
        launch(args); //invokes start()
    }

    @Override
    public void start(Stage primaryStage) {
        this.setPrimaryStage(primaryStage);
        this.getPrimaryStage().setTitle("Editor");
        this.getPrimaryStage().setWidth(BASEWIDTH);
        this.getPrimaryStage().setHeight(BASEHEIGHT);
        this.getPrimaryStage().setMaximized(true);


        this.setBasePane(new SplitPane());
        this.getBasePane().setPrefSize(BASEWIDTH, BASEHEIGHT);
        this.getBasePane().setMaxSize(BASEWIDTH, BASEHEIGHT);
        this.getBasePane().setMinSize(BASEWIDTH, BASEHEIGHT);


        this.setDirectoryChooser(new DirectoryChooser());
        this.getDirectoryChooser().setInitialDirectory(new File("src"));


        this.setMapStackPane(new StackPane());
        this.setMapPreviewCanvas(new Canvas());
        this.getMapPreviewCanvas().setVisible(false);
        this.setMapTilePane(new TilePane());
        this.setAssetPane(new TilePane());


        this.getMapStackPane().getChildren().add(this.getMapTilePane());
        this.getMapStackPane().getChildren().add(this.getMapPreviewCanvas());


/*        StringBuilder previewTipStyle = new StringBuilder();
        previewTipStyle.append("-fx-text-fill: white;");
        previewTipStyle.append("-fx-background-color: rgba(0,0,0,0);");
        previewTipStyle.append("-fx-background-radius: 0px;");
        previewTipStyle.append("-fx-background-insets: 0;");
        previewTipStyle.append("-fx-padding: 0px 0px 0px 0px;");
        previewTipStyle.append("-fx-font-size: 8px;");*/


        baseScene = new Scene(this.getBasePane(), BASEWIDTH, BASEHEIGHT, BGCOLOR);


        //left side ******************************************************************************************
        // ******************************************************************************************
        this.setLeftControl(new VBox());
        this.getLeftControl().setAlignment(Pos.TOP_LEFT);
        this.getLeftControl().setSpacing(20);




        //top panel ******************************************************************************************
        Label mapNameLabel = new Label("Map Name");
        this.getMapName().setMaxSize(200, 24);
        this.getMapName().setMinSize(200, 24);


        Button export = new Button("export map");
        export.setOnAction(actionEvent -> {
            if(DataManager.getInstance().getAssetPath() == null) {
                Alert alert = new Alert(AlertType.ERROR, "map is empty", ButtonType.CANCEL);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.CANCEL) {}
            } else {

/*                TextInputDialog dialog = new TextInputDialog();
                dialog.setHeaderText("Above ground rows");

                final Button ok = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
                ok.addEventFilter(ActionEvent.ACTION, event ->
                        System.out.println("OK was pressed")
                );

                final Button cancel = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                cancel.addEventFilter(ActionEvent.ACTION, event ->
                        System.out.println("Cancel was pressed")
                );

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    System.out.println("Result: " + result.get());
                    DataManager.getInstance().setAboveGroundRows(Integer.parseInt(result.get()));
                    DataManager.getInstance().exportMap(this.getPrimaryStage());

                } else {
                    System.out.println("Result not present => Cancel might have been pressed");
                }*/



                Dialog dialog = new Dialog<>();
                dialog.setTitle("Export map");

                // Set the button types.
                ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

                GridPane gridPane = new GridPane();
                gridPane.setHgap(10);
                gridPane.setVgap(10);
                gridPane.setPadding(new Insets(20, 150, 10, 10));

                TextField aboveGroundRows = new TextField();
                aboveGroundRows.setPromptText("above ground rows");
                CheckBox deployData = new CheckBox();
                CheckBox deployImage = new CheckBox();
                TextField gameResPath = new TextField();
                gameResPath.setText("C:\\game\\StartPoint\\app\\src\\main\\res");
                gameResPath.setPromptText("game res folder");
                TextField zoomRatio = new TextField();

                gridPane.add(new Label("above ground rows:"), 0, 0);
                gridPane.add(aboveGroundRows, 1, 0);
                gridPane.add(new Label("deploy data:"), 0, 1);
                gridPane.add(deployData, 1, 1);
                gridPane.add(new Label("deploy image:"), 0, 2);
                gridPane.add(deployImage, 1, 2);
                gridPane.add(new Label("game res folder:"), 0, 3);
                gridPane.add(gameResPath, 1, 3);
                gridPane.add(new Label("zoom ratio (base/fhd):"), 0, 4);
                gridPane.add(zoomRatio, 1, 4);

                dialog.getDialogPane().setContent(gridPane);

                Optional result = dialog.showAndWait();
                if (result.isPresent() && !aboveGroundRows.getText().isEmpty()) {
                    System.out.println("aboveGroundRow: " + aboveGroundRows.getText());
                    System.out.println("gameResFolder: " + gameResPath.getText());
                    try {
                        DataManager.getInstance().setAboveGroundRows(Integer.parseInt(aboveGroundRows.getText()));
                    } catch(NumberFormatException nfe) {
                        DataManager.getInstance().setAboveGroundRows(0);
                        nfe.printStackTrace();
                    }
                    DataManager.getInstance().setDeployData(deployData.isSelected());
                    DataManager.getInstance().setDeployImage(deployImage.isSelected());
                    DataManager.getInstance().setGameResFolder(gameResPath.getText());
                    DataManager.getInstance().setZoomRatio(Float.parseFloat(zoomRatio.getText()));
                    DataManager.getInstance().exportMap(primaryStage);


                } else {
                    System.out.println("Result not present => Cancel might have been pressed");
                }

            }
        });

        Button importMap = new Button("import map");
        //this key word refer to MyApplication instance, not anonymous class here, see note lambda this
        importMap.setOnAction(actionEvent -> {
            Alert alert = new Alert(AlertType.CONFIRMATION, "import map?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {

                MyApplication.this.importMap();

                MyApplication.this.getMapName().setText(DataManager.getInstance().getMapName());
                MyApplication.this.getRow().setText(String.valueOf(DataManager.getInstance().getRow()));
                MyApplication.this.getColumn().setText(String.valueOf(DataManager.getInstance().getColumn()));
                MyApplication.this.getStartColor().setText(String.valueOf(DataManager.getInstance().getStartColor()));
                MyApplication.this.getEndColor().setText(String.valueOf(DataManager.getInstance().getEndColor()));
                MyApplication.this.getColor1().setText(String.valueOf(DataManager.getInstance().getColor1()));
                MyApplication.this.getColor2().setText(String.valueOf(DataManager.getInstance().getColor2()));
                MyApplication.this.getColor1Pos().setText(String.valueOf(DataManager.getInstance().getColor1Pos()));
                MyApplication.this.getColor2Pos().setText(String.valueOf(DataManager.getInstance().getColor2Pos()));


            }


        });



        Button showHidePreview = new Button("show/hide preview");
        //this key word refer to MyApplication instance, not anonymous class here, see note lambda this
        showHidePreview.setOnAction(actionEven-> {
            updateGradient();
            showHidePreview();
        });
        //add shortcuts
        baseScene.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.V && keyEvent.isShiftDown()) {
                updateGradient();
                showHidePreview();
            }
        });


        Button savePreview = new Button("save preview");
        //this key word refer to MyApplication instance, not anonymous class here, see note lambda this
        savePreview.setOnAction(actionEvent -> {
            saveCanvasPreviewFile();
        });


        HBox leftButtonPanel = new HBox();
        leftButtonPanel.setAlignment(Pos.BASELINE_LEFT);
        leftButtonPanel.setSpacing(15);
        leftButtonPanel.getChildren().add(mapNameLabel);
        leftButtonPanel.getChildren().add(mapName);
        leftButtonPanel.getChildren().add(export);
        leftButtonPanel.getChildren().add(importMap);
        leftButtonPanel.getChildren().add(showHidePreview);
        leftButtonPanel.getChildren().add(savePreview);

        this.getLeftControl().getChildren().add(leftButtonPanel);


        //input panel ******************************************************************************************
        Label rowLabel = new Label("Row");
        this.getRow().setMaxSize(30, 24);
        this.getRow().setMinSize(30, 24);
        Label colLabel = new Label("Column");
        this.getColumn().setMaxSize(30, 24);
        this.getColumn().setMinSize(30, 24);


        Label startColorLabel = new Label("Start Color");
        this.getStartColor().setMaxSize(80, 24);
        this.getStartColor().setMinSize(80, 24);
        Label color1Label = new Label("Color 1");
        this.getColor1().setMaxSize(80, 24);
        this.getColor1().setMinSize(80, 24);
        Label color2Label = new Label("Color 2");
        this.getColor2().setMaxSize(80, 24);
        this.getColor2().setMinSize(80, 24);
        Label endColorLabel = new Label("End Color");
        this.getEndColor().setMaxSize(80, 24);
        this.getEndColor().setMinSize(80, 24);

        Label color1PosLabel = new Label("Color 1 Position");
        this.getColor1Pos().setMaxSize(30, 24);
        this.getColor1Pos().setMinSize(30, 24);
        Label color2PosLabel = new Label("Color 2 Position");
        this.getColor2Pos().setMaxSize(30, 24);
        this.getColor2Pos().setMinSize(30, 24);



        Button generateMap = new Button("generate map");
        //this key word refer to MyApplication instance, not anonymous class here, see note lambda this
        generateMap.setOnAction(actionEvent -> {
            Alert alert = new Alert(AlertType.CONFIRMATION, "generate new Map?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                DataManager.getInstance().setMapName(mapName.getText());
                DataManager.getInstance().setRow(Integer.parseInt(row.getText()));
                DataManager.getInstance().setColumn(Integer.parseInt(column.getText()));
                DataManager.getInstance().setStartColor(startColor.getText());
                DataManager.getInstance().setEndColor(endColor.getText());
                DataManager.getInstance().setColor1(color1.getText());
                DataManager.getInstance().setColor2(color2.getText());
                DataManager.getInstance().setColor1Pos(color1Pos.getText());
                DataManager.getInstance().setColor2Pos(color2Pos.getText());

                DataManager.getInstance().initMatrix();

                this.initializeMapButtons();
                this.initializeMapTilePane();
                this.generateLinearGradientStops(startColor.getText(), color1.getText(), color2.getText(), endColor.getText(), color1Pos.getText(), color2Pos.getText(), Integer.parseInt(row.getText()));
                this.setLinearGradient(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, this.getGradientStops()));
                this.initializeMapPreviewCanvas();
            }
        });


        Button updateGradientBackground = new Button("update gradient");

        updateGradientBackground.setOnAction(actionEvent -> {
            Alert alert = new Alert(AlertType.CONFIRMATION, "update gradient?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                this.updateGradient();
            }
        });



        HBox inputPanel = new HBox();
        inputPanel.setAlignment(Pos.BASELINE_LEFT);
        inputPanel.setSpacing(15);
        inputPanel.getChildren().add(rowLabel);
        inputPanel.getChildren().add(row);
        inputPanel.getChildren().add(colLabel);
        inputPanel.getChildren().add(column);
        inputPanel.getChildren().add(startColorLabel);
        inputPanel.getChildren().add(startColor);
        inputPanel.getChildren().add(color1Label);
        inputPanel.getChildren().add(color1);
        inputPanel.getChildren().add(color1PosLabel);
        inputPanel.getChildren().add(color1Pos);
        inputPanel.getChildren().add(color2Label);
        inputPanel.getChildren().add(color2);
        inputPanel.getChildren().add(color2PosLabel);
        inputPanel.getChildren().add(color2Pos);
        inputPanel.getChildren().add(endColorLabel);
        inputPanel.getChildren().add(endColor);
        inputPanel.getChildren().add(generateMap);
        inputPanel.getChildren().add(updateGradientBackground);

        this.getLeftControl().getChildren().add(inputPanel);


        //auto panel ******************************************************************************************
        HBox leftAutomatePanel = new HBox();
        leftAutomatePanel.setAlignment(Pos.BASELINE_LEFT);
        leftAutomatePanel.setSpacing(15);

        Label autoStartRowLabel = new Label("Row Start");
        this.getAutoStartRow().setMaxSize(30, 24);
        this.getAutoStartRow().setMinSize(30, 24);
        Label autoEndRowLabel = new Label("Row End");
        this.getAutoEndRow().setMaxSize(30, 24);
        this.getAutoEndRow().setMinSize(30, 24);
        Label autoStartColumnLabel = new Label("Column Start");
        this.getAutoStartColumn().setMaxSize(30, 24);
        this.getAutoStartColumn().setMinSize(30, 24);
        Label autoEndColumnLabel = new Label("Column End");
        this.getAutoEndColumn().setMaxSize(30, 24);
        this.getAutoEndColumn().setMinSize(30, 24);
        Label autoRowIntervalLabel = new Label("Row Interval(min 1)");
        this.getAutoRowInterval().setMaxSize(30, 24);
        this.getAutoRowInterval().setMinSize(30, 24);
        Label autoColumnIntervalLabel = new Label("Column Interval(min 1)");
        this.getAutoColumnInterval().setMaxSize(30, 24);
        this.getAutoColumnInterval().setMinSize(30, 24);
        Label autoAssetNameKeyLabel = new Label("Asset Name Key (rock-layer1-...)");
        this.getAutoAssetNameKey().setMaxSize(100, 24);
        this.getAutoAssetNameKey().setMinSize(100, 24);
        Label autoRandomPositionLabel = new Label("Shift Position");
        Label autoInOrderLabel = new Label("In Order");

        Button autoGenerate = new Button("auto generate");
        autoGenerate.setOnAction(actionEvent -> {
            Alert alert = new Alert(AlertType.CONFIRMATION, "automatically generate background?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                DataManager.getInstance().setStartRow(Integer.parseInt(autoStartRow.getText()));
                DataManager.getInstance().setEndRow(Integer.parseInt(autoEndRow.getText()));
                DataManager.getInstance().setStartColumn(Integer.parseInt(autoStartColumn.getText()));
                DataManager.getInstance().setEndColumn(Integer.parseInt(autoEndColumn.getText()));
                DataManager.getInstance().setRowInterval(Integer.parseInt(autoRowInterval.getText()));
                DataManager.getInstance().setColumnInterval(Integer.parseInt(autoColumnInterval.getText()));
                DataManager.getInstance().setAssetNameKeys(autoAssetNameKey.getText());
                DataManager.getInstance().setRandomPosition(autoRandomPosition.isSelected());
                DataManager.getInstance().setInOrder(autoInOrder.isSelected());
                DataManager.getInstance().autoGenerateBackground();
            }
        });

        Button autoRevert = new Button("revert");
        autoRevert.setOnAction(actionEvent -> {
            Alert alert = new Alert(AlertType.CONFIRMATION, "clear contents which are generated in last step?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                DataManager.getInstance().clearLastAutoGenerateContent();
            }
        });

        leftAutomatePanel.getChildren().add(autoStartRowLabel);
        leftAutomatePanel.getChildren().add(this.getAutoStartRow());
        leftAutomatePanel.getChildren().add(autoEndRowLabel);
        leftAutomatePanel.getChildren().add(this.getAutoEndRow());
        leftAutomatePanel.getChildren().add(autoStartColumnLabel);
        leftAutomatePanel.getChildren().add(this.getAutoStartColumn());
        leftAutomatePanel.getChildren().add(autoEndColumnLabel);
        leftAutomatePanel.getChildren().add(this.getAutoEndColumn());
        leftAutomatePanel.getChildren().add(autoRowIntervalLabel);
        leftAutomatePanel.getChildren().add(this.getAutoRowInterval());
        leftAutomatePanel.getChildren().add(autoColumnIntervalLabel);
        leftAutomatePanel.getChildren().add(this.getAutoColumnInterval());
        leftAutomatePanel.getChildren().add(autoAssetNameKeyLabel);
        leftAutomatePanel.getChildren().add(this.getAutoAssetNameKey());
        leftAutomatePanel.getChildren().add(autoRandomPositionLabel);
        leftAutomatePanel.getChildren().add(this.getAutoRandomPosition());
        leftAutomatePanel.getChildren().add(autoInOrderLabel);
        leftAutomatePanel.getChildren().add(this.getAutoInOrder());
        leftAutomatePanel.getChildren().add(autoGenerate);
        leftAutomatePanel.getChildren().add(autoRevert);




        leftControl.getChildren().add(leftAutomatePanel);




        //map scroll panel ******************************************************************************************
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setMaxSize(MAPWIDTH, MAPHEIGHT);
        scrollPane.setMinSize(MAPWIDTH, MAPHEIGHT);
        scrollPane.setContent(this.getMapStackPane());
        this.getMapStackPane().setAlignment(Pos.CENTER);
        scrollPane.setPannable(true);
        this.getLeftControl().getChildren().add(scrollPane);











        //right control ******************************************************************************************
        // ******************************************************************************************


        this.setRightControl(new VBox());

        Button loadAsset = new Button("load assets");
        loadAsset.setOnAction(actionEvent -> {
            this.setMapFolder(new File(this.getDirectoryChooser().showDialog(this.getPrimaryStage()).getAbsolutePath()));
            AssetManager.getInstance().loadAsset(this.getMapFolder());
            this.initializeAssetButtons();
            this.initializeAssetPane();
        });

        Button addAssets = new Button("add assets");
        //this key word refer to MyApplication instance, not anonymous class here, see note lambda this
        addAssets.setOnAction(actionEvent -> {
            this.addNewAssets();
        });


        HBox rightButtonPanel = new HBox();
        rightButtonPanel.setAlignment(Pos.BASELINE_LEFT);
        rightButtonPanel.setSpacing(15);
        rightButtonPanel.getChildren().add(loadAsset);
        rightButtonPanel.getChildren().add(addAssets);

        this.getRightControl().getChildren().add(rightButtonPanel);


        ScrollPane assetScrollPane = new ScrollPane();
        assetScrollPane.setMaxSize(ASSETWIDTH, ASSETHEIGHT);
        assetScrollPane.setMinSize(ASSETWIDTH, ASSETHEIGHT);
        assetScrollPane.setContent(assetPane);
        assetPane.setAlignment(Pos.CENTER);
        assetScrollPane.setPannable(true);
        this.getRightControl().getChildren().add(assetScrollPane);


        this.getBasePane().getItems().addAll(leftControl, rightControl);

        primaryStage.setScene(baseScene);
        primaryStage.show();
    }


    private void showHidePreview() {
        if(this.getMapPreviewCanvas().isVisible()) {
            this.getMapPreviewCanvas().setVisible(false);
            this.getMapTilePane().setVisible(true);
        } else {
            this.getMapPreviewCanvas().setVisible(true);
            this.getMapTilePane().setVisible(false);
            this.redrawPreview();

        }
    }


    private void updateGradient() {
        DataManager.getInstance().setStartColor(startColor.getText());
        DataManager.getInstance().setEndColor(endColor.getText());
        DataManager.getInstance().setColor1(color1.getText());
        DataManager.getInstance().setColor2(color2.getText());
        DataManager.getInstance().setColor1Pos(color1Pos.getText());
        DataManager.getInstance().setColor2Pos(color2Pos.getText());
        this.generateLinearGradientStops(startColor.getText(), color1.getText(), color2.getText(), endColor.getText(), color1Pos.getText(), color2Pos.getText(), Integer.parseInt(row.getText()));
        this.setLinearGradient(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, this.getGradientStops()));

    }


    public void initializeMapButtons() {
        System.out.println("initializeMapButtons");
        DataManager.getInstance().setMapButtons(new Button[DataManager.getInstance().getRow()][DataManager.getInstance().getColumn()]);
        for(int i=0; i<DataManager.getInstance().getRow(); i++) {
            for(int j=0; j<DataManager.getInstance().getColumn(); j++) {
                //System.out.println("new button, i " + i + ", j " + j);
                Button button = new Button(i + "," + j);
                button.setMaxSize(MAPBUTTONWIDTH, MAPBUTTONHEIGHT);
                button.setPrefSize(MAPBUTTONWIDTH, MAPBUTTONHEIGHT);
                button.setMinSize(MAPBUTTONWIDTH, MAPBUTTONHEIGHT);
                button.setStyle("-fx-background-color: " + BUTTONDEFAULTBGCOLOR + ";" + "-fx-text-fill: " + BUTTONDEFAULTTEXTCOLOR + ";" + "-fx-font-size: " + BUTTONDEFAULTTEXTSIZE + ";");
                button.setTooltip(new Tooltip(button.getText()));

                button.setUserData(-1);
/*                button.setOnAction(actionEvent -> {
                    if(DataManager.getInstance().getCurrentAssetIndex() != -1) {
                        button.setStyle("-fx-text-fill: " + BUTTONACTIVETEXTCOLOR + ";" + "-fx-font-size: " + BUTTONDEFAULTTEXTSIZE + ";");


                        BackgroundImage backgroundImage = new BackgroundImage(AssetManager.getInstance().getFourtyAssetStore().get(DataManager.getInstance().getCurrentAssetIndex()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
                        Background background = new Background(backgroundImage);
                        button.setBackground(background);
                        button.setUserData(DataManager.getInstance().getCurrentAssetIndex());

                    }
                });*/


                button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                            if(mouseEvent.getClickCount() == 1) {
                                if(DataManager.getInstance().getCurrentAssetIndex() != -1) {
                                    button.setStyle("-fx-text-fill: " + BUTTONACTIVETEXTCOLOR + ";" + "-fx-font-size: " + BUTTONDEFAULTTEXTSIZE + ";");


                                    BackgroundImage backgroundImage = new BackgroundImage(AssetManager.getInstance().getFourtyAssetList().get(DataManager.getInstance().getCurrentAssetIndex()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
                                    Background background = new Background(backgroundImage);
                                    button.setBackground(background);
                                    button.setUserData(DataManager.getInstance().getCurrentAssetIndex());

                                }
                            } else if(mouseEvent.getClickCount() == 2){//double click to reset
                                //button.setBackground(null);
                                button.setUserData(null);
                                button.setStyle("-fx-background-color: " + BUTTONDEFAULTBGCOLOR + ";" + "-fx-text-fill: " + BUTTONDEFAULTTEXTCOLOR + ";" + "-fx-font-size: " + BUTTONDEFAULTTEXTSIZE + ";");
                                button.setUserData(-1);
                                if (DataManager.getInstance().getActorDataMap().containsKey(button.getText())) {
                                    DataManager.getInstance().getActorDataMap().remove(button.getText());
                                }
                            }
                        } else if(mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                            int buttonDataId = -1;
                            if(button.getUserData().toString().indexOf(">") > -1) {
                                String[] ss = button.getUserData().toString().split(">");
                                buttonDataId = Integer.parseInt(ss[0]);
                            } else {
                                buttonDataId = Integer.parseInt(button.getUserData().toString());
                            }
                            if(buttonDataId != -1) {
                                if(AssetManager.getInstance().getAssetNameList().get(buttonDataId).contains("door")) {
                                    Dialog dialog = new Dialog<>();
                                    dialog.setTitle("Specify target map and key");

                                    // Set the button types.
                                    ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                                    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

                                    GridPane gridPane = new GridPane();
                                    gridPane.setHgap(10);
                                    gridPane.setVgap(10);
                                    gridPane.setPadding(new Insets(20, 150, 10, 10));

                                    TextField targetMap = new TextField();
                                    TextField key = new TextField();


                                    gridPane.add(new Label("target map"), 0, 0);
                                    gridPane.add(new Label("key"), 0, 1);
                                    gridPane.add(targetMap, 1, 0);
                                    gridPane.add(key, 1, 1);

                                    //popup values if available
                                    if(DataManager.getInstance().getActorDataMap().containsKey(button.getText())) {
                                        String[] values = DataManager.getInstance().getActorDataMap().get(button.getText()).split(">");

                                        if(values[0].length() > 0) {
                                            targetMap.setText(values[0]);
                                            if (values.length > 1 && values[1].length() > 0) {
                                                key.setText(values[1]);
                                            }
                                        }
                                    }

                                    dialog.getDialogPane().setContent(gridPane);

                                    /*
                                    sample
                                    dataId>model1:3>model2:2>:>y
                                     */

                                    Optional result = dialog.showAndWait();
                                    if (result.isPresent()) {
                                        String data = targetMap.getText() + ">" + key.getText();
                                        System.out.println("door " + data);

                                        DataManager.getInstance().getActorDataMap().put(button.getText(), data);

                                    } else {
                                        System.out.println("Result not present => Cancel might have been pressed");
                                    }
                                } else if(AssetManager.getInstance().getAssetNameList().get(buttonDataId).equalsIgnoreCase("fighttrigger")) {

                                    Dialog dialog = new Dialog<>();
                                    dialog.setTitle("Specify model and amount");

                                    // Set the button types.
                                    ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                                    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

                                    GridPane gridPane = new GridPane();
                                    gridPane.setHgap(10);
                                    gridPane.setVgap(10);
                                    gridPane.setPadding(new Insets(20, 150, 10, 10));

                                    TextField model1 = new TextField();
                                    TextField amount1 = new TextField();
                                    TextField model2 = new TextField();
                                    TextField amount2 = new TextField();
                                    TextField model3 = new TextField();
                                    TextField amount3 = new TextField();

                                    TextField topTriggerDistance = new TextField("-1");

                                    CheckBox withDefault = new CheckBox();

                                    CheckBox bossFight = new CheckBox();

                                    gridPane.add(new Label("model"), 0, 0);
                                    gridPane.add(new Label("amount"), 1, 0);
                                    gridPane.add(model1, 0, 1);
                                    gridPane.add(amount1, 1, 1);
                                    gridPane.add(model2, 0, 2);
                                    gridPane.add(amount2, 1, 2);
                                    gridPane.add(model3, 0, 3);
                                    gridPane.add(amount3, 1, 3);
                                    gridPane.add(new Label("with default team"), 0, 4);
                                    gridPane.add(withDefault, 1, 4);
                                    gridPane.add(new Label("top trigger distance"), 0, 5);
                                    gridPane.add(topTriggerDistance, 1, 5);
                                    gridPane.add(new Label("boss fight"), 0, 6);
                                    gridPane.add(bossFight, 1, 6);


                                    //popup values if available
                                    if(DataManager.getInstance().getActorDataMap().containsKey(button.getText())) {

                                        /*
                                        sample
                                        model1:3>model2:2>:>y>-1>y
                                         */
                                        String[] values = DataManager.getInstance().getActorDataMap().get(button.getText()).split(">");

                                        if(values[0].indexOf(":") > 0) {
                                            String[] keyValues = values[0].split(":");
                                            model1.setText(keyValues[0]);
                                            amount1.setText(keyValues[1]);
                                        }

                                        if(values[1].indexOf(":") > 0) {
                                            String[] keyValues = values[1].split(":");
                                            model2.setText(keyValues[0]);
                                            amount2.setText(keyValues[1]);
                                        }

                                        if(values[2].indexOf(":") > 0) {
                                            String[] keyValues = values[2].split(":");
                                            model3.setText(keyValues[0]);
                                            amount3.setText(keyValues[1]);
                                        }

                                        if(values[3].equalsIgnoreCase("y")) {
                                            withDefault.setSelected(true);
                                        }

                                        if(!values[4].equalsIgnoreCase("-1")) {
                                            topTriggerDistance.setText(values[4]);
                                        }

                                        if(values[5].equalsIgnoreCase("y")) {
                                            bossFight.setSelected(true);
                                        }
                                    }

                                    dialog.getDialogPane().setContent(gridPane);

                                    /*
                                    sample
                                    dataId>model1:3>model2:2>:>y>-1>y
                                     */

                                    Optional result = dialog.showAndWait();
                                    if (result.isPresent()/* && !model1.getText().isEmpty()*/) { //allow empty team
                                        String wd =  withDefault.isSelected()?"y":"n";
                                        String bf =  bossFight.isSelected()?"y":"n";

                                        String data = model1.getText() + ":" + amount1.getText() + ">" + model2.getText() + ":" + amount2.getText() + ">" + model3.getText() + ":" + amount3.getText() + ">" + wd + ">" + topTriggerDistance.getText() + ">" + bf;
                                        System.out.println("fighttrigger " + data);

                                        if(!model1.getText().isEmpty()) {
                                            DataManager.getInstance().getMissionEnemySet().add(model1.getText());
                                        }
                                        if(!model2.getText().isEmpty()) {
                                            DataManager.getInstance().getMissionEnemySet().add(model2.getText());
                                        }
                                        if(!model3.getText().isEmpty()) {
                                            DataManager.getInstance().getMissionEnemySet().add(model3.getText());
                                        }
                                        DataManager.getInstance().getActorDataMap().put(button.getText(), data);

                                    } else {
                                        System.out.println("Result not present => Cancel might have been pressed");
                                    }
                                } else if(AssetManager.getInstance().getAssetNameList().get(buttonDataId).contains("escalator")) {
                                    Dialog dialog = new Dialog<>();
                                    dialog.setTitle("Specify moving distance, absolute value");

                                    // Set the button types.
                                    ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                                    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

                                    GridPane gridPane = new GridPane();
                                    gridPane.setHgap(10);
                                    gridPane.setVgap(10);
                                    gridPane.setPadding(new Insets(20, 150, 10, 10));

                                    TextField vX = new TextField();
                                    TextField vY = new TextField();
                                    TextField xDistance = new TextField();
                                    TextField yDistance = new TextField();
                                    TextField waitingDuration = new TextField();


                                    gridPane.add(new Label("vX"), 0, 0);
                                    gridPane.add(new Label("vY"), 0, 1);
                                    gridPane.add(new Label("xDistance"), 0, 2);
                                    gridPane.add(new Label("yDistance"), 0, 3);
                                    gridPane.add(new Label("waitingDuration"), 0, 4);

                                    gridPane.add(vX, 1, 0);
                                    gridPane.add(vY, 1, 1);
                                    gridPane.add(xDistance, 1, 2);
                                    gridPane.add(yDistance, 1, 3);
                                    gridPane.add(waitingDuration, 1, 4);

                                    //popup values if available
                                    if(DataManager.getInstance().getActorDataMap().containsKey(button.getText())) {
                                        String[] values = DataManager.getInstance().getActorDataMap().get(button.getText()).split(">");

                                        if(values[0].length() > 0) {
                                            vX.setText(values[0]);
                                        } else {
                                            vX.setText("0");
                                        }

                                        if(values[1].length() > 0) {
                                            vY.setText(values[1]);
                                        } else {
                                            vY.setText("0");
                                        }

                                        if(values[2].length() > 0) {
                                            xDistance.setText(values[2]);
                                        } else {
                                            xDistance.setText("0");
                                        }

                                        if(values[3].length() > 0) {
                                            yDistance.setText(values[3]);
                                        } else {
                                            yDistance.setText("0");
                                        }

                                        if(values[4].length() > 0) {
                                            waitingDuration.setText(values[4]);
                                        } else {
                                            waitingDuration.setText("0");
                                        }
                                    }

                                    dialog.getDialogPane().setContent(gridPane);

                                    /*
                                    sample
                                    dataId>vX>vY>xDistance>yDistance>waitingDuration
                                     */

                                    Optional result = dialog.showAndWait();
                                    if (result.isPresent()) {
                                        String data = vX.getText() + ">" + vY.getText() + ">" + xDistance.getText() + ">" + yDistance.getText() + ">" + waitingDuration.getText();
                                        System.out.println("escalator " + data);

                                        DataManager.getInstance().getActorDataMap().put(button.getText(), data);

                                    } else {
                                        System.out.println("Result not present => Cancel might have been pressed");
                                    }
                                }
                            }
                        }
                    }
                });

                button.setOnMouseMoved(mouseEvent -> {
                    int index = -1;
                    if(button.getUserData().toString().indexOf(">") > -1) { //door
                        index = Integer.parseInt(button.getUserData().toString().split(">")[0]);
                    } else {
                        index = Integer.parseInt(button.getUserData().toString());
                    }

                    if(index > -1) {
                        button.getTooltip().setGraphic(new ImageView(AssetManager.getInstance().getPreviewAssetList().get(index)));
                        button.getTooltip().setText(AssetManager.getInstance().getOriginalAssetList().get(index).getWidth() + " x " + AssetManager.getInstance().getOriginalAssetList().get(index).getHeight());
                    }
                });

                //for doors, need extra input, allow right click to show popup

                DataManager.getInstance().getMapButtons()[i][j] = button;
            }
        }
    }



    public void initializeMapTilePane() {
        System.out.println("initializeMapTilePane");
        this.getMapTilePane().setMaxSize(DataManager.getInstance().getColumn() * MAPBUTTONWIDTH, DataManager.getInstance().getRow() * MAPBUTTONHEIGHT);
        this.getMapTilePane().setMinSize(DataManager.getInstance().getColumn() * MAPBUTTONWIDTH, DataManager.getInstance().getRow() * MAPBUTTONHEIGHT);

        if(this.getMapTilePane().getChildren().size() > 0) {
            this.getMapTilePane().getChildren().clear();
        }

        for(int i=0; i<DataManager.getInstance().getRow(); i++) {
            for(int j=0; j<DataManager.getInstance().getColumn(); j++) {
                this.getMapTilePane().getChildren().add(DataManager.getInstance().getMapButtons()[i][j]);
            }
        }

        //this.getMapPane().setTileAlignment(Pos.TOP_LEFT);

    }



    public void generateLinearGradientStops(String startColor, String color1, String color2, String endColor, String color1Pos, String color2Pos, int row ) {
        //due to map button size is 40 instead of 100, so we have to do some scale here
        double mapHeight = row * 40; //40 is map button size
        double gradientStartPos = 0;
        if(mapHeight > 1080 * 0.4) {
            gradientStartPos = mapHeight - 1080 * 0.4;
            System.out.println(gradientStartPos);
        }


        if(endColor.equalsIgnoreCase("")) {
            this.setGradientStops(new Stop[]{
                new Stop(0, Color.valueOf(startColor)),
                new Stop(mapHeight, Color.valueOf(startColor))
            });
        } else {
            if (mapHeight > 1080 * 0.4) {
                if (!color1.equalsIgnoreCase("") && !color2.equalsIgnoreCase("")) {
                    this.setGradientStops(new Stop[]{
                        new Stop(0, Color.valueOf(startColor)),
                        new Stop(gradientStartPos/mapHeight, Color.valueOf(startColor)),
                        new Stop((Double.valueOf(color1Pos) * 0.4 + gradientStartPos)/mapHeight, Color.valueOf(color1)),
                        new Stop((Double.valueOf(color2Pos) * 0.4 + gradientStartPos)/mapHeight, Color.valueOf(color2)),
                        new Stop(1, Color.valueOf(endColor))
                    });
                } else if (!color1.equalsIgnoreCase("") && color2.equalsIgnoreCase("")) {
                    this.setGradientStops(new Stop[]{
                        new Stop(0, Color.valueOf(startColor)),
                        new Stop(gradientStartPos/mapHeight, Color.valueOf(startColor)),
                        new Stop((Double.valueOf(color1Pos) * 0.4 + gradientStartPos)/mapHeight, Color.valueOf(color1)),
                        new Stop(1, Color.valueOf(endColor))
                    });
                } else if (color1.equalsIgnoreCase("") && !color2.equalsIgnoreCase("")) {
                    this.setGradientStops(new Stop[]{
                        new Stop(0, Color.valueOf(startColor)),
                        new Stop(gradientStartPos/mapHeight, Color.valueOf(startColor)),
                        new Stop((Double.valueOf(color2Pos) * 0.4 + gradientStartPos)/mapHeight, Color.valueOf(color2)),
                        new Stop(1, Color.valueOf(endColor))
                    });
                } else {
                    System.out.println(startColor + ", " + endColor + ", " + gradientStartPos);

                    this.setGradientStops(new Stop[]{
                        new Stop(0, Color.valueOf(startColor)),
                        new Stop(gradientStartPos/mapHeight, Color.valueOf(startColor)),
                        new Stop(1, Color.valueOf(endColor))
                    });
                }
            } else {
                if (!color1.equalsIgnoreCase("") && !color2.equalsIgnoreCase("")) {
                    this.setGradientStops(new Stop[]{
                            new Stop(0, Color.valueOf(startColor)),
                            new Stop((Double.valueOf(color1Pos) * 0.4 + gradientStartPos)/mapHeight, Color.valueOf(color1)),
                            new Stop((Double.valueOf(color2Pos) * 0.4 + gradientStartPos)/mapHeight, Color.valueOf(color2)),
                            new Stop(1, Color.valueOf(endColor))
                    });
                } else if (!color1.equalsIgnoreCase("") && color2.equalsIgnoreCase("")) {
                    this.setGradientStops(new Stop[]{
                            new Stop(0, Color.valueOf(startColor)),
                            new Stop((Double.valueOf(color1Pos) * 0.4 + gradientStartPos)/mapHeight, Color.valueOf(color1)),
                            new Stop(1, Color.valueOf(endColor))
                    });
                } else if (color1.equalsIgnoreCase("") && !color2.equalsIgnoreCase("")) {
                    this.setGradientStops(new Stop[]{
                            new Stop(0, Color.valueOf(startColor)),
                            new Stop((Double.valueOf(color2Pos) * 0.4 + gradientStartPos)/mapHeight, Color.valueOf(color2)),
                            new Stop(1, Color.valueOf(endColor))
                    });
                } else {
                    this.setGradientStops(new Stop[]{
                            new Stop(0, Color.valueOf(startColor)),
                            new Stop(1, Color.valueOf(endColor))
                    });
                }
            }

        }
    }



    public void initializeMapPreviewCanvas() {
        System.out.println("initializeMapPreviewCanvas");
        this.getMapPreviewCanvas().setWidth(DataManager.getInstance().getColumn() * MAPBUTTONWIDTH);
        this.getMapPreviewCanvas().setHeight(DataManager.getInstance().getRow() * MAPBUTTONHEIGHT);

        GraphicsContext gc = this.getMapPreviewCanvas().getGraphicsContext2D();


        gc.setFill(this.getLinearGradient());
        //gc.setFill(CANVASBGCOLOR);
        gc.fillRect(0, 0, this.getMapPreviewCanvas().getWidth(), this.getMapPreviewCanvas().getHeight());

    }




    public void initializeAssetButtons() {

        //for (Map.Entry<Integer, Image> entry  : AssetManager.getInstance().getOneHundredAssetList().entrySet()) {
        for(int i=0; i<AssetManager.getInstance().getOneHundredAssetList().size(); i++) {
            //System.out.println(fileEntry.());
            Button button = new Button("", new ImageView(AssetManager.getInstance().getOneHundredAssetList().get(i)));
            button.setMaxSize(ASSETBUTTONWIDTH, ASSETBUTTONHEIGHT);
            button.setPrefSize(ASSETBUTTONWIDTH, ASSETBUTTONHEIGHT);
            button.setMinSize(ASSETBUTTONWIDTH, ASSETBUTTONHEIGHT);
            Tooltip tooltip = new Tooltip();
            tooltip.setGraphic(new ImageView(AssetManager.getInstance().getPreviewAssetList().get(i)));
            tooltip.setText(AssetManager.getInstance().getAssetNameList().get(i) + " - " +AssetManager.getInstance().getOriginalAssetList().get(i).getWidth() + " x " + AssetManager.getInstance().getOriginalAssetList().get(i).getHeight());
            button.setTooltip(tooltip);

            button.setUserData(i);

            button.setOnAction(action -> {
                //System.out.println(((Button)action.getSource()).getUserData());
                String indexString = ((Button)action.getSource()).getUserData().toString();
                DataManager.getInstance().setCurrentAssetIndex(Integer.parseInt(indexString));
            });



            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        //System.out.println(((Button)action.getSource()).getUserData());
                        String indexString = button.getUserData().toString();
                        DataManager.getInstance().setCurrentAssetIndex(Integer.parseInt(indexString));
                    } else if(mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                        Alert alert = new Alert(AlertType.CONFIRMATION, "delete asset?", ButtonType.OK, ButtonType.CANCEL);
                        alert.showAndWait();
                        if (alert.getResult() == ButtonType.OK) {
                            deleteAsset(Integer.parseInt(button.getUserData().toString()));
                        }
                    }
                }
            });




             System.out.println("initializeAssetButtons() " + i + " - " +button);
            DataManager.getInstance().getAssetButtons().add(button);

            DataManager.getInstance().setAssetPath(this.getMapFolder().getAbsolutePath());

        }
    }


    public void addNewAssets() {
        File newAssetsFolder = new File(this.getDirectoryChooser().showDialog(this.getPrimaryStage()).getAbsolutePath());
        AssetManager.getInstance().addAsset(newAssetsFolder);

        this.getAssetPane().getChildren().clear();
        this.initializeAssetPane();
    }


    public void deleteAsset(int assetIndex) {
        System.out.println("delete asset index " + assetIndex);
        //update matrix
        for(int i=0; i<DataManager.getInstance().getMatrix().length; i++) {
            for(int j=0; j<DataManager.getInstance().getMatrix()[0].length; j++) {
                if(DataManager.getInstance().getMatrix()[i][j].indexOf(">") > -1) { //door
                    int index = Integer.parseInt(DataManager.getInstance().getMatrix()[i][j].split(">")[0]);
                    String data = DataManager.getInstance().getMatrix()[i][j].split(">")[1];
                    if(index == assetIndex) {
                        DataManager.getInstance().getMatrix()[i][j] = "-1";
                    } else if(index > assetIndex) {
                        DataManager.getInstance().getMatrix()[i][j] = (index - 1) + ">" + data;
                    }
                } else {
                    if(Integer.parseInt(DataManager.getInstance().getMatrix()[i][j]) == assetIndex) {
                        DataManager.getInstance().getMatrix()[i][j] = "-1";
                    } else if(Integer.parseInt(DataManager.getInstance().getMatrix()[i][j]) > assetIndex) {
                        DataManager.getInstance().getMatrix()[i][j] = (Integer.parseInt(DataManager.getInstance().getMatrix()[i][j]) - 1) + "";
                    }
                }
            }
        }


        //update map buttons
        for(int i=0; i<DataManager.getInstance().getMapButtons().length; i++) {
            for(int j=0; j<DataManager.getInstance().getMapButtons()[0].length; j++) {
                if(DataManager.getInstance().getMapButtons()[i][j].getUserData().toString().indexOf(">") > -1) { //door
                    int index = Integer.parseInt(DataManager.getInstance().getMapButtons()[i][j].getUserData().toString().split(">")[0]);
                    String data = DataManager.getInstance().getMapButtons()[i][j].getUserData().toString().split(">")[1];
                    if(index == assetIndex) {
                        DataManager.getInstance().getMapButtons()[i][j].setUserData(null);
                        DataManager.getInstance().getMapButtons()[i][j].setStyle("-fx-background-color: " + BUTTONDEFAULTBGCOLOR + ";" + "-fx-text-fill: " + BUTTONDEFAULTTEXTCOLOR + ";" + "-fx-font-size: " + BUTTONDEFAULTTEXTSIZE + ";");
                        DataManager.getInstance().getMapButtons()[i][j].setUserData(-1);
                    } else if (index > assetIndex) {
                        DataManager.getInstance().getMapButtons()[i][j].setUserData((index - 1) + ">" + data);
                    }
                } else {
                    if(Integer.parseInt(DataManager.getInstance().getMapButtons()[i][j].getUserData().toString()) == assetIndex) {
                        DataManager.getInstance().getMapButtons()[i][j].setUserData(null);
                        DataManager.getInstance().getMapButtons()[i][j].setStyle("-fx-background-color: " + BUTTONDEFAULTBGCOLOR + ";" + "-fx-text-fill: " + BUTTONDEFAULTTEXTCOLOR + ";" + "-fx-font-size: " + BUTTONDEFAULTTEXTSIZE + ";");
                        DataManager.getInstance().getMapButtons()[i][j].setUserData(-1);
                    } else if (Integer.parseInt(DataManager.getInstance().getMapButtons()[i][j].getUserData().toString()) > assetIndex) {
                        DataManager.getInstance().getMapButtons()[i][j].setUserData(Integer.parseInt(DataManager.getInstance().getMapButtons()[i][j].getUserData().toString()) - 1);
                    }
                }
            }
        }


        //update asset buttons
        DataManager.getInstance().getAssetButtons().remove(assetIndex);
        for(Button button : DataManager.getInstance().getAssetButtons()) {
            int index = Integer.parseInt(button.getUserData().toString());
            if(index > assetIndex) {
                button.setUserData(index - 1);
            }
        }


        //update asset pane
        this.getAssetPane().getChildren().clear();
        for(int i=0; i<DataManager.getInstance().getAssetButtons().size(); i++) {
            System.out.println(i + " - " + DataManager.getInstance().getAssetButtons().get(i));
            this.getAssetPane().getChildren().add(DataManager.getInstance().getAssetButtons().get(i));
        }

        //delete asset png from game\img folder



        //update datamanager property
        if(DataManager.getInstance().getCurrentAssetIndex() == assetIndex) {
            DataManager.getInstance().setCurrentAssetIndex(-1);
        }

        //update assetmanager property
        AssetManager.getInstance().getOriginalAssetList().remove(assetIndex);
        AssetManager.getInstance().getOneHundredAssetList().remove(assetIndex);
        AssetManager.getInstance().getFourtyAssetList().remove(assetIndex);
        AssetManager.getInstance().getPreviewAssetList().remove(assetIndex);
        AssetManager.getInstance().getAssetNameList().remove(assetIndex);


    }


    public void initializeAssetPane() {
        this.getAssetPane().setPrefColumns(4);
        this.getAssetPane().setPrefRows(10);

        for(int i=0; i<DataManager.getInstance().getAssetButtons().size(); i++) {
            System.out.println(i + " - " + DataManager.getInstance().getAssetButtons().get(i));
            this.getAssetPane().getChildren().add(DataManager.getInstance().getAssetButtons().get(i));
        }

        this.getAssetPane().setTileAlignment(Pos.TOP_LEFT);

    }




    public void importMap() {
        this.setMapFolder(new File(this.getDirectoryChooser().showDialog(this.getPrimaryStage()).getAbsolutePath()));

        Alert alert1 = new Alert(AlertType.INFORMATION, "importing mission files, please wait...");
        alert1.setHeaderText("");
        //in order to close alert1 programmatically, it must has a CANCEL_CLOSE type button
        ButtonType buttonTypeCancel = new ButtonType( "OK", ButtonBar.ButtonData.CANCEL_CLOSE );
        alert1.getButtonTypes().setAll(buttonTypeCancel);
        alert1.show(); //.showAndWait() block the current thread, show() does not

        System.out.println("importMap() 000 ");
        //reset panes
        this.getAssetPane().getChildren().removeAll();
        this.getMapTilePane().getChildren().removeAll();
        //load asset
        DataManager.getInstance().setMapName(this.getMapFolder().getName());
        AssetManager.getInstance().reloadAsset(this.getMapFolder());

        this.initializeAssetButtons();
        this.initializeAssetPane();


        //popup map
        this.constructMap();

        this.initializeMapPreviewCanvas();

        alert1.close();

    }


    public void constructMap() {
        System.out.println("constructMap");

        DataManager.getInstance().loadMatrix(this.getMapFolder());
        DataManager.getInstance().loadActor(this.getMapFolder());

        //load map
        this.initializeMapButtons();
        this.initializeMapTilePane();

        for(int row=0; row<DataManager.getInstance().getMatrix().length; row++) {
            for(int column=0; column<DataManager.getInstance().getMatrix()[0].length; column++) {
                Button button = DataManager.getInstance().getMapButtons()[row][column];
                button.setUserData(DataManager.getInstance().getMatrix()[row][column]);

                int id = Integer.parseInt(DataManager.getInstance().getMatrix()[row][column].split(">")[0]);
                if(id > -1) {
                    button.setStyle("-fx-text-fill: " + BUTTONACTIVETEXTCOLOR + ";" + "-fx-font-size: " + BUTTONDEFAULTTEXTSIZE + ";");

                    BackgroundImage backgroundImage = new BackgroundImage(AssetManager.getInstance().getFourtyAssetList().get(id), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
                    Background background = new Background(backgroundImage);
                    button.setBackground(background);
                }
            }
        }
    }



    private void redrawPreview() {
        //System.out.println("redrawPreview() " + this.getMapPreviewCanvas().getWidth() + ", " + this.getMapPreviewCanvas().getHeight());
        this.getMapPreviewCanvas().getGraphicsContext2D().clearRect(0, 0, this.getMapPreviewCanvas().getWidth(), this.getMapPreviewCanvas().getHeight());
        this.setLinearGradient(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, this.getGradientStops()));
        this.getMapPreviewCanvas().getGraphicsContext2D().setFill(this.getLinearGradient());
        this.getMapPreviewCanvas().getGraphicsContext2D().fillRect(0, 0, this.getMapPreviewCanvas().getWidth(), this.getMapPreviewCanvas().getHeight());
        //this.getMapPreviewCanvas().getGraphicsContext2D().setGlobalAlpha(0.5);
        //this.getMapPreviewCanvas().getGraphicsContext2D().setGlobalBlendMode(BlendMode.SCREEN); //this will cause clearRect() stop working


        for(int i=5; i>=0; i--) {// start from the bottom layer
            //System.out.println("layer " + i);
            for (int row = 0; row < DataManager.getInstance().getMapButtons().length; row++) {
                for (int column = 0; column < DataManager.getInstance().getMapButtons()[0].length; column++) {
                    Button button = DataManager.getInstance().getMapButtons()[row][column];
                    int assetIndex = -1;
                    if(button.getUserData().toString().indexOf(">") > -1) { //door
                        String[] s = button.getUserData().toString().split(">");
                        assetIndex = Integer.parseInt(s[0]);
                    } else {
                        assetIndex = Integer.parseInt(button.getUserData().toString());
                    }
                    if (assetIndex > -1) {
                        //System.out.println("redrawPreview() row " + row + ", col " + column + ", image " + AssetManager.getInstance().getIndexNameMap().get(assetIndex));
                        if(AssetManager.getInstance().getAssetNameList().get(assetIndex).contains("layer" + i))
                            this.getMapPreviewCanvas().getGraphicsContext2D().drawImage(AssetManager.getInstance().getPreviewAssetList().get(assetIndex), column * MAPBUTTONWIDTH, row * MAPBUTTONHEIGHT);
                    }
                }
            }
        }

        for (int row = 0; row < DataManager.getInstance().getMapButtons().length; row++) {
            for (int column = 0; column < DataManager.getInstance().getMapButtons()[0].length; column++) {
                Button button = DataManager.getInstance().getMapButtons()[row][column];
                int assetIndex = -1;
                if(button.getUserData().toString().indexOf(">") > -1) { //door
                    String[] s = button.getUserData().toString().split(">");
                    assetIndex = Integer.parseInt(s[0]);
                } else {
                    assetIndex = Integer.parseInt(button.getUserData().toString());
                }
                if (assetIndex > -1) {
                    //System.out.println("redrawPreview() row " + row + ", col " + column + ", image " + AssetManager.getInstance().getIndexNameMap().get(assetIndex));
                    if(!AssetManager.getInstance().getAssetNameList().get(assetIndex).contains("layer"))
                        this.getMapPreviewCanvas().getGraphicsContext2D().drawImage(AssetManager.getInstance().getPreviewAssetList().get(assetIndex), column * MAPBUTTONWIDTH, row * MAPBUTTONHEIGHT);
                }
            }
        }

    }



    private void saveCanvasPreviewFile(){
        //show preview first, or saved file will be empty
        this.getMapPreviewCanvas().setVisible(true);
        this.getMapTilePane().setVisible(false);
        this.redrawPreview();

        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(this.getMapFolder());
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG","*.png"));
        fc.setTitle("Save Preview");
        fc.setInitialFileName(DataManager.getInstance().getMapName());
        File file = fc.showSaveDialog(primaryStage);
        if(file != null){
            SnapshotParameters sp = new SnapshotParameters();
            sp.setFill(Color.TRANSPARENT);
            WritableImage wi = new WritableImage((int)this.getMapPreviewCanvas().getWidth(), (int)this.getMapPreviewCanvas().getHeight());
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(this.getMapPreviewCanvas().snapshot(null,wi),null),"png",file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    public TilePane getAssetPane() {
        return assetPane;
    }

    public void setAssetPane(TilePane assetPane) {
        this.assetPane = assetPane;
    }

    public SplitPane getBasePane() {
        return basePane;
    }

    public void setBasePane(SplitPane basePane) {
        this.basePane = basePane;
    }

    public TilePane getMapTilePane() {
        return mapTilePane;
    }

    public void setMapTilePane(TilePane mapTilePane) {
        this.mapTilePane = mapTilePane;
    }

    public Scene getBaseScene() {
        return baseScene;
    }

    public void setBaseScene(Scene baseScene) {
        this.baseScene = baseScene;
    }

    public VBox getLeftControl() {
        return leftControl;
    }

    public void setLeftControl(VBox leftControl) {
        this.leftControl = leftControl;
    }

    public VBox getRightControl() {
        return rightControl;
    }

    public void setRightControl(VBox rightControl) {
        this.rightControl = rightControl;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public File getMapFolder() {
        return mapFolder;
    }

    public void setMapFolder(File mapFolder) {
        this.mapFolder = mapFolder;
    }

    public DirectoryChooser getDirectoryChooser() {
        return directoryChooser;
    }

    public void setDirectoryChooser(DirectoryChooser directoryChooser) {
        this.directoryChooser = directoryChooser;
    }

    public Canvas getMapPreviewCanvas() {
        return mapPreviewCanvas;
    }

    public void setMapPreviewCanvas(Canvas mapPreviewCanvas) {
        this.mapPreviewCanvas = mapPreviewCanvas;
    }

    public StackPane getMapStackPane() {
        return mapStackPane;
    }

    public void setMapStackPane(StackPane mapStackPane) {
        this.mapStackPane = mapStackPane;
    }

    public Stop[] getGradientStops() {
        return gradientStops;
    }

    public void setGradientStops(Stop[] gradientStops) {
        this.gradientStops = gradientStops;
    }

    public LinearGradient getLinearGradient() {
        return linearGradient;
    }

    public void setLinearGradient(LinearGradient linearGradient) {
        this.linearGradient = linearGradient;
    }

    public TextField getRow() {
        return row;
    }

    public void setRow(TextField row) {
        this.row = row;
    }

    public TextField getColumn() {
        return column;
    }

    public void setColumn(TextField column) {
        this.column = column;
    }

    public TextField getStartColor() {
        return startColor;
    }

    public void setStartColor(TextField startColor) {
        this.startColor = startColor;
    }

    public TextField getColor1() {
        return color1;
    }

    public void setColor1(TextField color1) {
        this.color1 = color1;
    }

    public TextField getColor2() {
        return color2;
    }

    public void setColor2(TextField color2) {
        this.color2 = color2;
    }

    public TextField getEndColor() {
        return endColor;
    }

    public void setEndColor(TextField endColor) {
        this.endColor = endColor;
    }

    public TextField getColor1Pos() {
        return color1Pos;
    }

    public void setColor1Pos(TextField color1Pos) {
        this.color1Pos = color1Pos;
    }

    public TextField getColor2Pos() {
        return color2Pos;
    }

    public void setColor2Pos(TextField color2Pos) {
        this.color2Pos = color2Pos;
    }


    public TextField getAutoStartRow() {
        return autoStartRow;
    }

    public void setAutoStartRow(TextField autoStartRow) {
        this.autoStartRow = autoStartRow;
    }

    public TextField getAutoEndRow() {
        return autoEndRow;
    }

    public void setAutoEndRow(TextField autoEndRow) {
        this.autoEndRow = autoEndRow;
    }

    public TextField getAutoStartColumn() {
        return autoStartColumn;
    }

    public void setAutoStartColumn(TextField autoStartColumn) {
        this.autoStartColumn = autoStartColumn;
    }

    public TextField getAutoEndColumn() {
        return autoEndColumn;
    }

    public void setAutoEndColumn(TextField autoEndColumn) {
        this.autoEndColumn = autoEndColumn;
    }

    public TextField getAutoColumnInterval() {
        return autoColumnInterval;
    }

    public void setAutoColumnInterval(TextField autoColumnInterval) {
        this.autoColumnInterval = autoColumnInterval;
    }

    public TextField getAutoAssetNameKey() {
        return autoAssetNameKey;
    }

    public void setAutoAssetNameKey(TextField autoAssetNameKey) {
        this.autoAssetNameKey = autoAssetNameKey;
    }

    public TextField getAutoRowInterval() {
        return autoRowInterval;
    }

    public void setAutoRowInterval(TextField autoRowInterval) {
        this.autoRowInterval = autoRowInterval;
    }

    public CheckBox getAutoRandomPosition() {
        return autoRandomPosition;
    }

    public void setAutoRandomPosition(CheckBox autoRandomPosition) {
        this.autoRandomPosition = autoRandomPosition;
    }

    public CheckBox getAutoInOrder() {
        return autoInOrder;
    }

    public void setAutoInOrder(CheckBox autoInOrder) {
        this.autoInOrder = autoInOrder;
    }

    public TextField getMapName() {
        return mapName;
    }

    public void setMapName(TextField mapName) {
        this.mapName = mapName;
    }
}
