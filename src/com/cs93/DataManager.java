/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cs93;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import static com.cs93.MyApplication.*;

/**
 *
 * @author schen
 */
public class DataManager {
    private static DataManager instance;

    private Button[][] mapButtons;
    private ArrayList<Button> assetButtons;

    private int currentAssetIndex = -1;
    private String mapName;
    private int row, column;
    private String assetPath;
    private double mouseX, mouseY;

    private String startColor, endColor, color1, color2, color1Pos, color2Pos;
    private int startRow, endRow, startColumn, endColumn, rowInterval, columnInterval, aboveGroundRows;
    private boolean randomPosition, inOrder;
    private String assetNameKeys;
    private String gameResFolder;
    private boolean deployData, deployImage;
    private float zoomRatio; // = baseBitmap / fhd

    private boolean exportFinish;

    public static final String SEPERATOR = "oxoxo";
    public static final String MAPFOLDER = "mapProject", GAMEFOLDER = "gameProject", MATRIXFILENAME = "matrix.txt", ASSETFILENAME = "assets.txt", ACTORFILENAME = "actors.txt", DATAFILENAME = "data.txt", ENUMSKINEDPROPSFILENAME = "enumskinedprops.txt", MISSIONSKINEDPROPSFILENAME = "missionskinedprops.txt";

    /*
    anchor is a node in matrix
    anchors {
    [first anchor] = {0 = row in matrix, 1 = column in matrix}
    [second anchor] = {0 = row in matrix, 1 = column in matrix}
    }
     */
    private int[][] anchors;

    private String[][] matrix;

    private HashMap<String, String> actorDataMap = new HashMap<>();

    private HashSet<String> missionEnemySet = new HashSet<>();

    private DataManager() {}

    public static DataManager getInstance() {
        if(instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void exportMap(Stage primaryStage) {
        System.out.println("DataManager.exportMap() 1111 ");

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("src"));
        File exportFolder = new File(directoryChooser.showDialog(primaryStage).getAbsolutePath());

        Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "exporting mission files, please wait...");
        alert1.setHeaderText("");
        //in order to close alert1 programmatically, it must has a CANCEL_CLOSE type button
        ButtonType buttonTypeCancel = new ButtonType( "OK", ButtonBar.ButtonData.CANCEL_CLOSE );
        alert1.getButtonTypes().setAll(buttonTypeCancel);
        alert1.show(); //.showAndWait() block the current thread, show() does not

        Path rawPath = Paths.get(this.getGameResFolder() + File.separator + "raw");

        //write matrix ===================================================================
        //map folder
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<this.getMapButtons().length; i++) {
            for(int j=0; j<this.getMapButtons()[0].length; j++) {
                String s = this.getMapButtons()[i][j].getUserData().toString();
                //if button has extra data, e.g. door/fighttrigger
                if(s.indexOf('>') > -1) {
                    String[] ss = s.split(">");
                    sb.append(ss[0].length() == 1 ? " " + ss[0] : ss[0]);
                    if (j < this.getMapButtons()[0].length - 1) {
                        sb.append(",");
                    }

                    //System.out.println("Log - doors put " + ss[0] + " " + ss[1]);
                } else {
                    sb.append(s.length() == 1 ? " " + s : s);
                    if (j < this.getMapButtons()[0].length - 1) {
                        sb.append(",");
                    }
                }
            }
            sb.append("=");
        }


        String[] lines = sb.toString().split("=");   // pipe does not work here

        System.out.println("lines size " + lines.length);

        try {
            String matrixFolderPath = exportFolder+ File.separator + MAPFOLDER;
            Path path = Paths.get(matrixFolderPath);
            Files.createDirectories(path);

            String matrixFilePath = matrixFolderPath + File.separator + this.getMapName() + MATRIXFILENAME;
            File file = new File(matrixFilePath);
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for(int i=0; i<lines.length; i++) {
                //System.out.println("line " + i + " : " + lines[i]);
                writer.write(lines[i]);
                writer.newLine();
            }

            writer.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }




        //game folder
        //HashMap<Integer, String> doors = new HashMap<>();

        StringBuilder sb1 = new StringBuilder();
        for(int i=0; i<this.getMapButtons().length; i++) {
            for(int j=0; j<this.getMapButtons()[0].length; j++) {
                String s = this.getMapButtons()[i][j].getUserData().toString();
                //if button has extra data, e.g. door/fighttrigger
                if(s.indexOf('>') > -1) {
                    String[] ss = s.split(">");
                    sb1.append(ss[0].length() == 1 ? " " + ss[0] : ss[0]);
                    if (j < this.getMapButtons()[0].length - 1) {
                        sb1.append(",");
                    }

/*                    System.out.println("Log - doors put " + ss[0] + " " + ss[1]);
                    doors.put(Integer.parseInt(ss[0]), ss[1]);*/
                } else {
                    sb1.append(s.length() == 1 ? " " + s : s);
                    if (j < this.getMapButtons()[0].length - 1) {
                        sb1.append(",");
                    }
                }
            }
            sb1.append("=");
        }


        String[] lines1 = sb1.toString().split("=");   // pipe does not work here
        try {
            String matrixFolderPath = exportFolder+ File.separator + GAMEFOLDER;
            Path path = Paths.get(matrixFolderPath);
            Files.createDirectories(path);

            String matrixFilePath = matrixFolderPath + File.separator + this.getMapName() + MATRIXFILENAME;
            File file = new File(matrixFilePath);
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for(int i=0; i<lines1.length; i++) {
                //System.out.println("line " + i + " : " + lines[i]);
                writer.write(lines1[i]);
                writer.newLine();
            }

            writer.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }



        //deploy to res/raw
        if(this.isDeployData()) {
            try {

                String matrixFilePath = rawPath + File.separator + this.getMapName() + MATRIXFILENAME;
                File file = new File(matrixFilePath);
                file.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                for (int i = 0; i < lines1.length; i++) {
                    //System.out.println("line " + i + " : " + lines[i]);
                    writer.write(lines1[i]);
                    writer.newLine();
                }

                writer.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }






        //write assets ====================================================
        //map folder
        try {
            String assetFolderPath = exportFolder + File.separator + MAPFOLDER;

            String assetFilePath = assetFolderPath + File.separator + this.getMapName() + ASSETFILENAME;
            File file = new File(assetFilePath);
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            //for(Map.Entry<Integer, String> entry : AssetManager.getInstance().getAssetNameList().entrySet()) {
            for(String name : AssetManager.getInstance().getAssetNameList()) {
                writer.write(name);
                writer.newLine();
            }

            writer.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }


        //game folder
        String assetFilePath = exportFolder + File.separator + GAMEFOLDER + File.separator + this.getMapName() + ASSETFILENAME;
        this.buildAssetsDataForGame(assetFilePath);



        //deploy to res/raw
        if(this.isDeployData()) {
            String assetsFilePath = rawPath + File.separator + this.getMapName() + ASSETFILENAME;
            this.buildAssetsDataForGame(assetsFilePath);
        }



        //write mission specific person and background skinedprops ====================================================
        //only to game folder
        try {
            String missionSkinedPropsFilePath = exportFolder + File.separator + GAMEFOLDER + File.separator + this.getMapName() + MISSIONSKINEDPROPSFILENAME;
            File file = new File(missionSkinedPropsFilePath);
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for(String name : AssetManager.getInstance().getAssetNameList()) {
                writer.write(name);
                writer.newLine();
            }

            for(String name : this.getMissionEnemySet()) {
                writer.write(name);
                writer.newLine();
            }

            writer.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }



        //deploy to res/raw
        if(this.isDeployData()) {
            try {
                String missionSkinedPropsFilePath = rawPath + File.separator + this.getMapName() + MISSIONSKINEDPROPSFILENAME;
                File file = new File(missionSkinedPropsFilePath);
                file.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                for(String name : AssetManager.getInstance().getAssetNameList()) {
                    writer.write(name);
                    writer.newLine();
                }

                for(String name : this.getMissionEnemySet()) {
                    writer.write(name);
                    writer.newLine();
                }

                writer.close();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }



        //write actors ======================================================
        //map folder
        try {
            String actorFilePath = exportFolder + File.separator + MAPFOLDER + File.separator + this.getMapName() + ACTORFILENAME;
            File file = new File(actorFilePath);
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for(int i=0; i<this.getMapButtons().length; i++) {
                for(int j=0; j<this.getMapButtons()[0].length; j++) {
                    if (DataManager.getInstance().getActorDataMap().containsKey(i + "," + j)) {
                        writer.write(i + "," + j + "=" + DataManager.getInstance().getActorDataMap().get(i + "," + j));
                        writer.newLine();
                    }
                    //System.out.println("Log - doors put " + ss[0] + " " + ss[1]);
                }
            }

            writer.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }






        //game folder
        //TODO scale escalator move distance by zoom ratio before export to actor file
        try {
            String actorFilePath = exportFolder + File.separator + GAMEFOLDER + File.separator + this.getMapName() + ACTORFILENAME;
            File file = new File(actorFilePath);
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for(int i=0; i<this.getMapButtons().length; i++) {
                for(int j=0; j<this.getMapButtons()[0].length; j++) {
                    if (DataManager.getInstance().getActorDataMap().containsKey(i + "," + j)) {
                        writer.write(i + "," + j + "=" + DataManager.getInstance().getActorDataMap().get(i + "," + j));
                        writer.newLine();
                    }
                    //System.out.println("Log - doors put " + ss[0] + " " + ss[1]);
                }
            }

            writer.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }



        //deploy to res/raw
        //TODO scale escalator move distance by zoom ratio before export to actor file
        if(this.isDeployData()) {
            try {
                String actorFilePath = rawPath + File.separator + this.getMapName() + ACTORFILENAME;
                File file = new File(actorFilePath);
                file.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                for(int i=0; i<this.getMapButtons().length; i++) {
                    for(int j=0; j<this.getMapButtons()[0].length; j++) {
                        if (DataManager.getInstance().getActorDataMap().containsKey(i + "," + j)) {
                            writer.write(i + "," + j + "=" + DataManager.getInstance().getActorDataMap().get(i + "," + j));
                            writer.newLine();
                        }
                        //System.out.println("Log - doors put " + ss[0] + " " + ss[1]);
                    }
                }


                writer.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }




        //write data ==================================================================================
        //map folder
        try {
            String dataFilePath = exportFolder + File.separator + MAPFOLDER + File.separator + this.getMapName() + DATAFILENAME;
            File file = new File(dataFilePath);
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            writer.write("mapName " + this.getMapName());
            writer.newLine();
            writer.write("rowAboveGround " + this.getAboveGroundRows());
            writer.newLine();
            writer.write("0 " + this.getStartColor());
            writer.newLine();
            writer.write("1 " + this.getEndColor());
            writer.newLine();
            writer.write("2 " + this.getColor1());
            writer.newLine();
            writer.write("3 " + this.getColor2());
            writer.newLine();
            writer.write("4 " + this.getColor1Pos());
            writer.newLine();
            writer.write("5 " + this.getColor2Pos());
            writer.newLine();
            writer.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }



        //game folder
        try {
            String dataFilePath = exportFolder + File.separator + GAMEFOLDER + File.separator + this.getMapName() + DATAFILENAME;
            File file = new File(dataFilePath);
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            writer.write("mapName " + this.getMapName());
            writer.newLine();
            writer.write("rowAboveGround " + this.getAboveGroundRows());
            writer.newLine();
            writer.write("0 " + this.getStartColor());
            writer.newLine();
            writer.write("1 " + this.getEndColor());
            writer.newLine();
            writer.write("2 " + this.getColor1());
            writer.newLine();
            writer.write("3 " + this.getColor2());
            writer.newLine();
            writer.write("4 " + (int)(Integer.parseInt(this.getColor1Pos()) * this.getZoomRatio()));  //basebitmap is half size of fhd
            writer.newLine();
            writer.write("5 " + (int)(Integer.parseInt(this.getColor2Pos()) * this.getZoomRatio()));  //basebitmap is half size of fhd
            writer.newLine();
            writer.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }



        //deploy to res/raw
        if(this.isDeployData()) {
            try {
                String dataFilePath = rawPath + File.separator + this.getMapName() + DATAFILENAME;
                File file = new File(dataFilePath);
                file.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                writer.write("mapName " + this.getMapName());
                writer.newLine();
                writer.write("rowAboveGround " + this.getAboveGroundRows());
                writer.newLine();
                writer.write("0 " + this.getStartColor());
                writer.newLine();
                writer.write("1 " + this.getEndColor());
                writer.newLine();
                writer.write("2 " + this.getColor1());
                writer.newLine();
                writer.write("3 " + this.getColor2());
                writer.newLine();
                writer.write("4 " + (int)(Integer.parseInt(this.getColor1Pos()) * this.getZoomRatio()));  //basebitmap is half size of fhd
                writer.newLine();
                writer.write("5 " + (int)(Integer.parseInt(this.getColor2Pos()) * this.getZoomRatio()));  //basebitmap is half size of fhd
                writer.newLine();
                writer.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }




        //write SkinedProps enum ================================================
        //only to game folder
        //the goal of this file is you can copy its content(new skinedprops) into StageSkinData file
        try {
            String gameFolderPath = exportFolder + File.separator + GAMEFOLDER;

            String skinedPropsFilePath = gameFolderPath + File.separator + this.getMapName() + ENUMSKINEDPROPSFILENAME;
            File file = new File(skinedPropsFilePath);
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            //for(Map.Entry<Integer, String> entry : AssetManager.getInstance().getAssetNameList().entrySet()) {
            for(String name : AssetManager.getInstance().getAssetNameList()) {
                if(name.indexOf("model") == -1) { //not person
                    if(name.indexOf("ladder") > -1 ||name.indexOf("monkeybar") > -1 ||name.indexOf("door") > -1) {
                        writer.write(String.format("%s(\"%s\", '\\u1103', SkinPostureMapping.INTERACTABLE, null, null, 0f),", name.toUpperCase(), name.toLowerCase()));
                    } else {
                        writer.write(String.format("%s(\"%s\", '\\u1103', SkinPostureMapping.MAPSCENARY, null, null, 0f),", name.toUpperCase(), name.toLowerCase()));
                    }
                    writer.newLine();
                }
            }

            writer.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }




        //copy images================================================================================================
        //to map/game imgs folder
        AssetManager.getInstance().exportAssets(exportFolder);

        //deploy images to res/drawable-nodpi

        if(this.isDeployImage()) {
            AssetManager.getInstance().deployAssets();
        }
        //System.out.println("DataManager.exportMap() 2222 ");



        //create preview=========================================================================================
        //TODO create preview

        alert1.setContentText("export finish!");

    }


    public void buildAssetsDataForGame(String path) {
        try {
            File file = new File(path);
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for (int i = 0; i < AssetManager.getInstance().getAssetNameList().size(); i++) {

                if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("land") > -1) {
                    String[] values = AssetManager.getInstance().getAssetNameList().get(i).split(SEPERATOR);
                    int width = (int)(Integer.parseInt(values[2]) * this.getZoomRatio());
                    int height = (int)(Integer.parseInt(values[1]) * this.getZoomRatio());
                    writer.write(String.format("com.cs93.startpoint.actor.land.Land = 0, 0, 0, " + width + ", " + height + ", 100, RECTANGULAR, " + width + ", " + height + ", 0, EMPTY, %s, UP-DOWN-LEFT-RIGHT", DataManager.getInstance().getMapName().toUpperCase()));
                } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("plfm") > -1) { //hidden platform with variable width
                    String[] values = AssetManager.getInstance().getAssetNameList().get(i).split(SEPERATOR);
                    int width = (int)(Integer.parseInt(values[2]) * this.getZoomRatio());
                    int height = (int)(Integer.parseInt(values[1]) * this.getZoomRatio());
                    writer.write(String.format("com.cs93.startpoint.actor.land.Platform = 0, 0, 0, " + width + ", " + height + ", 100, RECTANGULAR, " + width + ", " + height + ", 0, %s, %s, UP", DataManager.getInstance().getMapName().toUpperCase(), AssetManager.getInstance().getAssetNameList().get(i).toUpperCase()));
                } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("empty") > -1) {
                    writer.write(String.format("com.cs93.startpoint.actor.land.Land = 0, 0, 0, " + 400 * this.getZoomRatio() + ", " + 100 * this.getZoomRatio() + ", 100, RECTANGULAR, " + 400 * this.getZoomRatio() + ", " + 100 * this.getZoomRatio() + ", 0, EMPTY, %s, UP-DOWN-LEFT-RIGHT", DataManager.getInstance().getMapName().toUpperCase()));
                } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("end") > -1) {
                    writer.write(String.format("com.cs93.startpoint.actor.land.Land = 0, 0, 0, " + 100 * this.getZoomRatio() + ", " + 400 * this.getZoomRatio() + ", 100, RECTANGULAR, " + 100 * this.getZoomRatio() + ", " + 400 * this.getZoomRatio() + ", 0, EMPTY, %s, LEFT-RIGHT", DataManager.getInstance().getMapName().toUpperCase()));
                } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("floor") > -1) {
                    writer.write(String.format("com.cs93.startpoint.actor.land.Land = 0, 0, 0, " + 600 * this.getZoomRatio() + ", " + 100 * this.getZoomRatio() + ", 100, RECTANGULAR, " + 600 * this.getZoomRatio() + ", " + 100 * this.getZoomRatio() + ", 0, %s, %s, UP-DOWN-LEFT-RIGHT", AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(), DataManager.getInstance().getMapName().toUpperCase()));
                } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("bigrock") > -1 && AssetManager.getInstance().getAssetNameList().get(i).indexOf("layer2") > -1 ) {
                    float w = (float)AssetManager.getInstance().getOriginalAssetList().get(i).getWidth() - 100;
                    writer.write(String.format("com.cs93.startpoint.actor.land.Platform = 0, 0, 0, " + w * this.getZoomRatio() + ", " + 100 * this.getZoomRatio() + ", 100, RECTANGULAR, " + w * this.getZoomRatio() + ", " + 100 * this.getZoomRatio() + ", 0, %s, %s, UP", DataManager.getInstance().getMapName().toUpperCase(), AssetManager.getInstance().getAssetNameList().get(i).toUpperCase()));
                } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("crate") > -1 && (AssetManager.getInstance().getAssetNameList().get(i).indexOf("layer2") > -1 || AssetManager.getInstance().getAssetNameList().get(i).indexOf("layer1") > -1)) {
                    float w = (float)AssetManager.getInstance().getOriginalAssetList().get(i).getWidth();
                    writer.write(String.format("com.cs93.startpoint.actor.land.Platform = 0, 0, 0, " + w * this.getZoomRatio() + ", " + 100 * this.getZoomRatio() + ", 100, RECTANGULAR, " + w * this.getZoomRatio() + ", " + 100 * this.getZoomRatio() + ", 0, %s, %s, UP", DataManager.getInstance().getMapName().toUpperCase(), AssetManager.getInstance().getAssetNameList().get(i).toUpperCase()));
                } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("platform") > -1 ) { //visible platform with fixed width 400
                    writer.write(String.format("com.cs93.startpoint.actor.land.Platform = 0, 0, 0, " + 400 * this.getZoomRatio() + ", " + 100 * this.getZoomRatio() + ", 100, RECTANGULAR, " + 400 * this.getZoomRatio() + ", " + 100 * this.getZoomRatio() + ", 0, %s, %s, UP", DataManager.getInstance().getMapName().toUpperCase(), AssetManager.getInstance().getAssetNameList().get(i).toUpperCase()));
                } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("escalator") > -1 ) {
                                                                                                            /*
                                                                                                         0 networkUniqueID
                                                                                                         1 initialXInMap, smaller end
                                                                                                         2 initialYInMap, smaller end
                                                                                                         3 rectw
                                                                                                         4 recth
                                                                                                         5 gy
                                                                                                         6 weight
                                                                                                         7 skinedprops
                                                                                                         8 shapetype
                                                                                                         9 width
                                                                                                         10 height
                                                                                                         11 radius
                                                                                                         12 map
                                                                                                         13 multipleSkin
                                                                                                          */
                    int rectW = (int)AssetManager.getInstance().getOriginalAssetList().get(i).getWidth();
                    writer.write(String.format("com.cs93.startpoint.actor.land.MovingLand = 0, 0, 0, " + rectW * this.getZoomRatio() + ", " + 100 * this.getZoomRatio() + ", 0, 100, %s, RECTANGULAR, " + rectW * this.getZoomRatio() + ", " + 100 * this.getZoomRatio() + ", 0, %s, false", AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(), DataManager.getInstance().getMapName().toUpperCase()));
                } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("ladder") > -1) {
                    writer.write(String.format("com.cs93.startpoint.svgProps.Ladder = 0, 0, 0, " + 2 * this.getZoomRatio() + ", " + 310 * this.getZoomRatio() + ", %s, %s, RECTANGULAR, " + 2 * this.getZoomRatio() + ", " + 310 * this.getZoomRatio() + ", 0", AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(), DataManager.getInstance().getMapName().toUpperCase()));
                } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("monkeybar") > -1) {
                                                                /*
                                                                0 networkUniqueID
                                                                1 initX
                                                                2 initY
                                                                3 rectW
                                                                4 rectH
                                                                5 vx
                                                                6 vy
                                                                7 skinedprops
                                                                8 mapname
                                                                9 multipleSkin
                                                                 */
                    writer.write(String.format("com.cs93.startpoint.svgProps.Monkeybar = 0, 0, 0, " + 200 * this.getZoomRatio() + ", " + 200 * this.getZoomRatio() + ", 0, 0, %s, %s, false", AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(), DataManager.getInstance().getMapName().toUpperCase()));
                } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("unfollow") > -1) {
                    writer.write(String.format("com.cs93.startpoint.svgProps.UnfollowArea = 0, 0, 0, " + 400 * this.getZoomRatio() + ", " + 400 * this.getZoomRatio() + ", %s, RECTANGULAR, " + 400 * this.getZoomRatio() + ", " + 400 * this.getZoomRatio() + ", 0", DataManager.getInstance().getMapName().toUpperCase()));
                } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("finishpoint") > -1) {
                    writer.write(String.format("com.cs93.startpoint.svgProps.MissionDoor = 0, 0, 0, " + 400 * this.getZoomRatio() + ", " + 400 * this.getZoomRatio() + ", %s, RECTANGULAR, " + 400 * this.getZoomRatio() + ", " + 400 * this.getZoomRatio() + ", 0, FINISHPOINT, null", DataManager.getInstance().getMapName().toUpperCase()));
                } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("fighttrigger") > -1) {
                    writer.write(String.format("com.cs93.startpoint.stageProps.FightTrigger"));
                } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("movablecrate") > -1) {
                    //due to 45 deg side view, rectW set to 60px less than the asset width
                    int rectW = (int)AssetManager.getInstance().getOriginalAssetList().get(i).getWidth() - 60;
                    int rectH = (int)AssetManager.getInstance().getOriginalAssetList().get(i).getHeight();
                    writer.write(String.format("com.cs93.startpoint.actor.movableObject.MovableObject = 0, 0, 0, " + rectW * this.getZoomRatio() + ", " + rectH * this.getZoomRatio() + ", 0, 0, 1, 100, RECTANGULAR, " + rectW * this.getZoomRatio() + ", " + rectH * this.getZoomRatio() + ", 0, %s, %s, METAL, false", AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(), DataManager.getInstance().getMapName().toUpperCase()));
                } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("supplybox") > -1) {
                    /*
                    0 networkid
                    1 initx
                    2 inity
                    3 persondata
                    4 npcdata
                    5 map
                    6 posture
                    7 directionradian
                    8 merchantdata
                     */
                    writer.write(String.format("com.cs93.startpoint.actor.person.SupplyBox = 0, 0, 0, SUPPLYBOX1, SUPPLYBOX1, %s, STAND, 0, SUPPLYBOX1", DataManager.getInstance().getMapName().toUpperCase()));
                } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("door") > -1) { //door
                    writer.write(String.format("com.cs93.startpoint.svgProps.MapDoor = 0, 0, 0, " + 700 * this.getZoomRatio() + ", " + 700 * this.getZoomRatio() + ", %s, %s, RECTANGULAR, " + 700 * this.getZoomRatio() + ", " + 700 * this.getZoomRatio() + ", 0", AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(), DataManager.getInstance().getMapName().toUpperCase()));

                } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("enemymodel") > -1) {
                    try {
                        if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("fly") > -1) {
                            writer.write(
                                    String.format(
                                            "com.cs93.startpoint.actor.person.EnemyModel%s = 0, 0, 0, %s, %s, %s, HOVER",
                                            AssetManager.getInstance().getAssetNameList().get(i).substring(10, 11).toUpperCase() + AssetManager.getInstance().getAssetNameList().get(i).substring(11),
                                            AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(),
                                            AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(),
                                            DataManager.getInstance().getMapName().toUpperCase()
                                    )
                            );
                        } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("walk") > -1) {
                            writer.write(
                                    String.format(
                                            "com.cs93.startpoint.actor.person.EnemyModel%s = 0, 0, 0, %s, %s, %s, STAND",
                                            AssetManager.getInstance().getAssetNameList().get(i).substring(10, 11).toUpperCase() + AssetManager.getInstance().getAssetNameList().get(i).substring(11),
                                            AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(),
                                            AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(),
                                            DataManager.getInstance().getMapName().toUpperCase()
                                    )
                            );
                        } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("wheel") > -1) {
                            writer.write(
                                    String.format(
                                            "com.cs93.startpoint.actor.person.EnemyModel%s = 0, 0, 0, %s, %s, %s, STAND",
                                            AssetManager.getInstance().getAssetNameList().get(i).substring(10, 11).toUpperCase() + AssetManager.getInstance().getAssetNameList().get(i).substring(11),
                                            AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(),
                                            AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(),
                                            DataManager.getInstance().getMapName().toUpperCase()
                                    )
                            );
                        } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("crawl") > -1) {
                            writer.write(
                                    String.format(
                                            "com.cs93.startpoint.actor.person.EnemyModel%s = 0, 0, 0, %s, %s, %s, STAND",
                                            AssetManager.getInstance().getAssetNameList().get(i).substring(10, 11).toUpperCase() + AssetManager.getInstance().getAssetNameList().get(i).substring(11),
                                            AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(),
                                            AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(),
                                            DataManager.getInstance().getMapName().toUpperCase()
                                    )
                            );
                        }
                    } catch (StringIndexOutOfBoundsException siobe) {
                        System.out.println("buildAssetsDataForGame() " + AssetManager.getInstance().getAssetNameList().get(i));
                        siobe.printStackTrace();
                    }
                } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("npc") > -1) {
                    if(AssetManager.getInstance().getAssetNameList().get(i).indexOf("npcmodelwalk8.") > -1) {
                        try {
                            writer.write(
                                    String.format(
                                            "com.cs93.startpoint.actor.person.NPCPrisoner = 0, 0, 0, %s, %s, %s, STAND, 0, %s",
                                            AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(), //persondata
                                            AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(), //npcdata
                                            DataManager.getInstance().getMapName().toUpperCase(),
                                            AssetManager.getInstance().getAssetNameList().get(i).toUpperCase() //merchantdata
                                    )
                            );
                        } catch (StringIndexOutOfBoundsException siobe) {
                            System.out.println("buildAssetsDataForGame() " + AssetManager.getInstance().getAssetNameList().get(i));
                            siobe.printStackTrace();
                        }
                    } else {
                        try {
                            writer.write(
                                    String.format(
                                            "com.cs93.startpoint.actor.person.NPC = 0, 0, 0, %s, %s, %s, STAND, 0, %s",
                                            AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(), //persondata
                                            AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(), //npcdata
                                            DataManager.getInstance().getMapName().toUpperCase(),
                                            AssetManager.getInstance().getAssetNameList().get(i).toUpperCase() //merchantdata
                                    )
                            );
                        } catch (StringIndexOutOfBoundsException siobe) {
                            System.out.println("buildAssetsDataForGame() " + AssetManager.getInstance().getAssetNameList().get(i));
                            siobe.printStackTrace();
                        }
                    }
                } else if (AssetManager.getInstance().getAssetNameList().get(i).indexOf("foreground") > -1) {// foreground
                    /*
                        0 networkUniqueID,
                        1 initialXInMap,
                        2 initialYInMap,
                        3 vX,
                        4 vY,
                        5 gY,
                        6 skinedProps,
                        7 mapNames,
                        8 shapeType,
                        9 width,
                        10 height,
                        11 radius,
                        12 postureType,
                        13 directionRadian
                        14 multipleSkin
                        15 moving
                     */
                    writer.write(String.format("com.cs93.startpoint.stageProps.foreground.ConfigurableForeground = 0, 0, 0, 0, 0, 0, %s, %s, RECTANGULAR, 182, 200, 0, STAND, %c, false, false", AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(), DataManager.getInstance().getMapName().toUpperCase(), '0'));
                } else {// background
                    //get layer
                    int index = AssetManager.getInstance().getAssetNameList().get(i).indexOf("layer");
                    if (index > 0) { //with layer
                        char layer = AssetManager.getInstance().getAssetNameList().get(i).charAt(index + 5);
                        writer.write(String.format("com.cs93.startpoint.stageProps.background.ConfigurableBackground = 0, 0, 0, 0, 0, 0, %s, %s, RECTANGULAR, 182, 200, 0, STAND, %c, false, false", AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(), DataManager.getInstance().getMapName().toUpperCase(), layer));
                    } else {
                        writer.write(String.format("com.cs93.startpoint.stageProps.background.ConfigurableBackground = 0, 0, 0, 0, 0, 0, %s, %s, RECTANGULAR, 182, 200, 0, STAND, %c, false, false", AssetManager.getInstance().getAssetNameList().get(i).toUpperCase(), DataManager.getInstance().getMapName().toUpperCase(), '1'));
                    }
                }
                writer.newLine();
            }
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    public void autoGenerateBackground() {
        if(
            this.getStartRow() > this.getEndRow() ||
            this.getStartColumn() > this.getEndColumn() ||
            this.getStartRow() > this.getRow() ||
            this.getEndRow() > this.getRow() ||
            this.getStartColumn() > this.getColumn() ||
            this.getEndColumn() > this.getColumn()
        ) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "make sure start < end, and both do not exceed map dimension", ButtonType.CANCEL);
            alert.show();
        } else {
            //clear last step content
            //this.clearLastAutoGenerateContent();

            //generate anchor points
            int rowSpan;
            if(this.getStartRow() == this.getEndRow())
                rowSpan = 1;
            else
                rowSpan = this.getEndRow() - this.getStartRow();
            int rowAnchorAmount = (int)Math.ceil(rowSpan * 1f / this.getRowInterval());

            int columnSpan;
            if(this.getStartColumn() == this.getEndColumn())
                columnSpan = 1;
            else
                columnSpan = this.getEndColumn() - this.getStartColumn();
            int columnAnchorAmount = (int)Math.ceil(columnSpan * 1f / this.getColumnInterval());

            int totalAnchorsAmount = rowAnchorAmount * columnAnchorAmount;

            this.setAnchors(new int[totalAnchorsAmount][2]);

            int anchorIndex = 0;
            for(int i=0; i<rowAnchorAmount; i++) {
                for(int j=0; j<columnAnchorAmount; j++) {
                    this.getAnchors()[anchorIndex][0] = startRow + i * this.getRowInterval();
                    this.getAnchors()[anchorIndex++][1] = startColumn + j * this.getColumnInterval();
                }
            }

            //shift position randomly, only on column
            System.out.println("anchors size " + this.getAnchors().length);
            if(this.isRandomPosition()) {
                for (int i = 0; i < this.getAnchors().length; i++) {
                    this.getAnchors()[i][1] += Util.getRandomInt(-1 * this.getColumnInterval(), this.getColumnInterval());
                    //in case random ends up in minors value
                    if(this.getAnchors()[i][1] < 0)
                        this.getAnchors()[i][1] = 0;
                }
            }


            //collect valid assets
            String[] keys = DataManager.getInstance().getAssetNameKeys().split("-");
            //System.out.println("AssetNameKeys " + DataManager.getInstance().getAssetNameKeys());
            ArrayList<Integer> validAssetsIndex = AssetManager.getInstance().getAssetsByName(keys);
            if(validAssetsIndex.size() == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "asset not found", ButtonType.CANCEL);
                alert.showAndWait();
            }


            //popup matrix and map buttons
            int validIndex = 0;
            for(int i=0; i<this.getAnchors().length; i++) {
                System.out.println("matrix length" + this.getMatrix().length);
                System.out.println("validAssetsIndex size is " + validAssetsIndex.size());
                if(this.getMatrix()[this.getAnchors()[i][0]][this.getAnchors()[i][1]].equals("-1")) {
                    if(this.isInOrder()) {
                        this.getMatrix()[this.getAnchors()[i][0]][this.getAnchors()[i][1]] = validAssetsIndex.get(validIndex++ % validAssetsIndex.size()).toString();
                    } else {
                        this.getMatrix()[this.getAnchors()[i][0]][this.getAnchors()[i][1]] = validAssetsIndex.get(Util.getRandomInt(0, validAssetsIndex.size() - 1)).toString();
                    }

                    this.getMapButtons()[this.getAnchors()[i][0]][this.getAnchors()[i][1]].setStyle("-fx-text-fill: " + BUTTONACTIVETEXTCOLOR + ";" + "-fx-font-size: " + BUTTONDEFAULTTEXTSIZE + ";");

                    this.getMapButtons()[this.getAnchors()[i][0]][this.getAnchors()[i][1]].setUserData(this.getMatrix()[this.getAnchors()[i][0]][this.getAnchors()[i][1]]);

                    BackgroundImage backgroundImage = new BackgroundImage(AssetManager.getInstance().getFourtyAssetList().get(Integer.parseInt(this.getMatrix()[this.getAnchors()[i][0]][this.getAnchors()[i][1]])), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
                    Background background = new Background(backgroundImage);
                    this.getMapButtons()[this.getAnchors()[i][0]][this.getAnchors()[i][1]].setBackground(background);
                }
            }


        }
    }


    //clear contents which is generated in last step
    public void clearLastAutoGenerateContent() {
        //System.out.println("clearLastAutoGenerateContent anchors size is " + this.getAnchors().length);
        if(this.getAnchors() != null && this.getAnchors().length > 0) {

            //collect valid assets
            String[] keys = DataManager.getInstance().getAssetNameKeys().split("-");
            ArrayList<Integer> validAssetsIndex = AssetManager.getInstance().getAssetsByName(keys);

            for (int i = 0; i < this.getAnchors().length; i++) {

                if (validAssetsIndex.contains(Integer.parseInt(this.getMatrix()[this.getAnchors()[i][0]][this.getAnchors()[i][1]]))) {
                    this.getMatrix()[this.getAnchors()[i][0]][this.getAnchors()[i][1]] = "-1";

                    this.getMapButtons()[this.getAnchors()[i][0]][this.getAnchors()[i][1]].setUserData("-1");
                    //BackgroundImage backgroundImage = new BackgroundImage(AssetManager.getInstance().getFourtyAssetStore().get(this.getMatrix()[anchors[i][0]][anchors[i][1]]), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
                    //Background background = new Background(backgroundImage);
                    this.getMapButtons()[this.getAnchors()[i][0]][this.getAnchors()[i][1]].setStyle("-fx-background-color: " + BUTTONDEFAULTBGCOLOR + ";" + "-fx-text-fill: " + BUTTONDEFAULTTEXTCOLOR + ";" + "-fx-font-size: " + BUTTONDEFAULTTEXTSIZE + ";");

                    this.getMapButtons()[this.getAnchors()[i][0]][this.getAnchors()[i][1]].setBackground(null);
                }
            }
        }
    }


    public void initMatrix() {
        this.setMatrix(new String[this.getRow()][this.getColumn()]);
        for(int i=0; i<this.getRow(); i++) {
            for(int j=0; j<this.getColumn(); j++) {
                this.getMatrix()[i][j] = "-1";
            }
        }
    }


    public void loadMatrix(File mapFolder) {
        System.out.println("loadMatrix");
        try {
            BufferedReader sizeBufferedReader = new BufferedReader(new FileReader(mapFolder.getAbsolutePath() + File.separator + DataManager.MAPFOLDER + File.separator + mapFolder.getName() + MATRIXFILENAME));
            int row = 0, column = 0;
            String line = sizeBufferedReader.readLine();
            while(line != null) {
                String[] idArray = line.split(",");
                column = idArray.length;
                row++;

                line = sizeBufferedReader.readLine();
            }

            DataManager.getInstance().setMatrix(new String[row][column]);
            DataManager.getInstance().setRow(row);
            DataManager.getInstance().setColumn(column);
            System.out.println("matrix size : row " + row + ", column " + column);


            BufferedReader valueBufferedReader = new BufferedReader(new FileReader(mapFolder.getAbsolutePath() + File.separator + DataManager.MAPFOLDER + File.separator + mapFolder.getName()+ MATRIXFILENAME));
            int i=0;
            line = valueBufferedReader.readLine();
            while(line != null) {
                String[] idArray = line.split(",");
                for(int j=0; j<column; j++) {
                    DataManager.getInstance().getMatrix()[i][j] = idArray[j].trim();
                    System.out.print(DataManager.getInstance().getMatrix()[i][j] + ", ");
                }
                i++;
                line = valueBufferedReader.readLine();

                System.out.println();
            }

        }catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    public void loadActor(File mapFolder) {
        System.out.println("loadActor");
        try {
            BufferedReader sizeBufferedReader = new BufferedReader(new FileReader(mapFolder.getAbsolutePath() + File.separator + DataManager.MAPFOLDER + File.separator + mapFolder.getName() + ACTORFILENAME));
            String line = sizeBufferedReader.readLine();
            while(line != null) {
                String[] keyValue = line.split("=");
                this.getActorDataMap().put(keyValue[0], keyValue[1]);
                System.out.println("load actor data, [" + keyValue[0] + "] > " + keyValue[1]);


                String[] values = keyValue[1].split(">");
                for(int i=0; i<values.length; i++) {
                    if (values[i].indexOf(":") > 0) {
                        String[] keyValues = values[i].split(":");
                        if (!keyValues[0].isEmpty()) {
                            this.getMissionEnemySet().add(keyValues[0]);
                        }
                    }
                }

                line = sizeBufferedReader.readLine();
            }



        }catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION, e.toString());
            alert1.setHeaderText("");
            //in order to close alert1 programmatically, it must has a CANCEL_CLOSE type button
            ButtonType buttonTypeCancel = new ButtonType( "OK", ButtonBar.ButtonData.CANCEL_CLOSE );
            alert1.getButtonTypes().setAll(buttonTypeCancel);
            alert1.show(); //.showAndWait() block the current thread, show() does not
        }
    }


    public Button[][] getMapButtons() {
        return mapButtons;
    }

    public void setMapButtons(Button[][] mapButtons) {
        this.mapButtons = mapButtons;
    }

    public ArrayList<Button> getAssetButtons() {
        return assetButtons;
    }

    public void setAssetButtons(ArrayList<Button> assetButtons) {
        this.assetButtons = assetButtons;
    }

    public int getCurrentAssetIndex() {
        return currentAssetIndex;
    }

    public void setCurrentAssetIndex(int currentAssetIndex) {
        this.currentAssetIndex = currentAssetIndex;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getAssetPath() {
        return assetPath;
    }

    public void setAssetPath(String assetPath) {
        this.assetPath = assetPath;
    }

    public double getMouseX() {
        return mouseX;
    }

    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }

    public String[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(String[][] matrix) {
        this.matrix = matrix;
    }

    public String getStartColor() {
        return startColor;
    }

    public void setStartColor(String startColor) {
        this.startColor = startColor;
    }

    public String getEndColor() {
        return endColor;
    }

    public void setEndColor(String endColor) {
        this.endColor = endColor;
    }

    public String getColor1() {
        return color1;
    }

    public void setColor1(String color1) {
        this.color1 = color1;
    }

    public String getColor2() {
        return color2;
    }

    public void setColor2(String color2) {
        this.color2 = color2;
    }

    public String getColor1Pos() {
        return color1Pos;
    }

    public void setColor1Pos(String color1Pos) {
        this.color1Pos = color1Pos;
    }

    public String getColor2Pos() {
        return color2Pos;
    }

    public void setColor2Pos(String color2Pos) {
        this.color2Pos = color2Pos;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public void setStartColumn(int startColumn) {
        this.startColumn = startColumn;
    }

    public int getEndColumn() {
        return endColumn;
    }

    public void setEndColumn(int endColumn) {
        this.endColumn = endColumn;
    }

    public int getRowInterval() {
        return rowInterval;
    }

    public void setRowInterval(int rowInterval) {
        this.rowInterval = rowInterval;
    }

    public int getColumnInterval() {
        return columnInterval;
    }

    public void setColumnInterval(int columnInterval) {
        this.columnInterval = columnInterval;
    }

    public String getAssetNameKeys() {
        return assetNameKeys;
    }

    public void setAssetNameKeys(String assetNameKeys) {
        this.assetNameKeys = assetNameKeys;
    }

    public boolean isRandomPosition() {
        return randomPosition;
    }

    public void setRandomPosition(boolean randomPosition) {
        this.randomPosition = randomPosition;
    }

    public int[][] getAnchors() {
        return anchors;
    }

    public void setAnchors(int[][] anchors) {
        this.anchors = anchors;
    }

    public boolean isInOrder() {
        return inOrder;
    }

    public void setInOrder(boolean inOrder) {
        this.inOrder = inOrder;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public int getAboveGroundRows() {
        return aboveGroundRows;
    }

    public void setAboveGroundRows(int aboveGroundRows) {
        this.aboveGroundRows = aboveGroundRows;
    }

    public String getGameResFolder() {
        return gameResFolder;
    }

    public void setGameResFolder(String gameResFolder) {
        this.gameResFolder = gameResFolder;
    }

    public boolean isDeployData() {
        return deployData;
    }

    public void setDeployData(boolean deployData) {
        this.deployData = deployData;
    }

    public HashMap<String, String> getActorDataMap() {
        return actorDataMap;
    }

    public void setActorDataMap(HashMap<String, String> actorDataMap) {
        this.actorDataMap = actorDataMap;
    }

    public boolean isExportFinish() {
        return exportFinish;
    }

    public void setExportFinish(boolean exportFinish) {
        this.exportFinish = exportFinish;
    }

    public HashSet<String> getMissionEnemySet() {
        return missionEnemySet;
    }

    public void setMissionEnemySet(HashSet<String> missionEnemySet) {
        this.missionEnemySet = missionEnemySet;
    }

    public boolean isDeployImage() {
        return deployImage;
    }

    public void setDeployImage(boolean deployImage) {
        this.deployImage = deployImage;
    }

    public float getZoomRatio() {
        return zoomRatio;
    }

    public void setZoomRatio(float zoomRatio) {
        this.zoomRatio = zoomRatio;
    }
}
