/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cs93;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.cs93.DataManager.*;

/**
 *
 * @author schen
 */
public class AssetManager {
    private static AssetManager instance;

    private ArrayList<Image> originalAssetList;
    private ArrayList<Image> oneHundredAssetList;
    private ArrayList<Image> fourtyAssetList;
    private ArrayList<Image> previewAssetList;

    private ArrayList<String> assetNameList;


    private AssetManager() {
        this.setOriginalAssetList(new ArrayList<>());
        this.setOneHundredAssetList(new ArrayList<>());
        this.setFourtyAssetList(new ArrayList<>());
        this.setPreviewAssetList(new ArrayList<>());

        this.setAssetNameList(new ArrayList<>());
    }

    public static AssetManager getInstance() {
        if(instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }


    //for new map
    public void loadAsset(File folder) {
        AssetManager.getInstance().resetAssetStore();


        int i = 0;
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                i++;
            }
        }

        DataManager.getInstance().setAssetButtons(new ArrayList<>());


        for (final File fileEntry : folder.listFiles()) {

            if (!fileEntry.isDirectory()) {
                //System.out.println(fileEntry.());

                try (
                    FileInputStream input = new FileInputStream(folder.getAbsolutePath() + File.separator + fileEntry.getName());
                ){
                    Image image = new Image(input);
                    ImageView imageView = new ImageView(image);

                    String fileName = fileEntry.getName().replace(".png", "");
                    this.getAssetNameList().add(fileName);

                    this.getOriginalAssetList().add(image);

                    SnapshotParameters param = new SnapshotParameters();
                    param.setFill(Color.TRANSPARENT);

                    imageView.setFitWidth(40);
                    imageView.setFitHeight(40);
                    imageView.setPreserveRatio(true);
                    this.getFourtyAssetList().add(imageView.snapshot(param, null));


                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);
                    imageView.setPreserveRatio(true);
                    this.getOneHundredAssetList().add(imageView.snapshot(param, null));

                    imageView.setFitWidth(0.4 * image.getWidth());
                    imageView.setFitHeight(0.4 * image.getHeight());
                    imageView.setPreserveRatio(true);
                    this.getPreviewAssetList().add(imageView.snapshot(param, null));


                }catch(FileNotFoundException fnfe) {
                    fnfe.printStackTrace();
                }catch(IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }



    //for import map
    public void reloadAsset(File folder) {
        AssetManager.getInstance().resetAssetStore();


        //read data file
        try {
            FileReader fileReader = new FileReader(folder.getAbsolutePath() + File.separator + DataManager.MAPFOLDER + File.separator + folder.getName() + DATAFILENAME);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] keyValues = line.split(" ");
                switch(keyValues[0]) {
                    case "mapName" :
                        if(keyValues.length > 1)
                            DataManager.getInstance().setMapName(keyValues[1]);
                        else
                            DataManager.getInstance().setMapName("");
                        break;
                    case "rowAboveGround" :
                        if(keyValues.length > 1)
                            DataManager.getInstance().setAboveGroundRows(Integer.parseInt(keyValues[1]));
                        else
                            DataManager.getInstance().setMapName("");
                        break;
                    case "0" :
                        if(keyValues.length > 1)
                            DataManager.getInstance().setStartColor(keyValues[1]);
                        else
                            DataManager.getInstance().setStartColor("");
                        break;
                    case "1" :
                        if(keyValues.length > 1)
                            DataManager.getInstance().setEndColor(keyValues[1]);
                        else
                            DataManager.getInstance().setEndColor("");
                        break;
                    case "2" :
                        if(keyValues.length > 1)
                            DataManager.getInstance().setColor1(keyValues[1]);
                        else
                            DataManager.getInstance().setColor1("");
                        break;
                    case "3" :
                        if(keyValues.length > 1)
                            DataManager.getInstance().setColor2(keyValues[1]);
                        else
                            DataManager.getInstance().setColor2("");
                        break;
                    case "4" :
                        if(keyValues.length > 1)
                            DataManager.getInstance().setColor1Pos(keyValues[1]);
                        else
                            DataManager.getInstance().setColor1Pos("");
                        break;
                    case "5" :
                        if(keyValues.length > 1)
                            DataManager.getInstance().setColor2Pos(keyValues[1]);
                        else
                            DataManager.getInstance().setColor2Pos("");
                        break;
                }

            }

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString(), ButtonType.CANCEL);
            alert.showAndWait();

            e.printStackTrace();
        }



        //read assets file
        try (
            FileReader fileReader = new FileReader(folder.getAbsolutePath() + File.separator + DataManager.MAPFOLDER + File.separator + folder.getName() + ASSETFILENAME);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
        ){


            String line;
            while ((line = bufferedReader.readLine()) != null) {
                AssetManager.getInstance().getAssetNameList().add(line);

                System.out.println("reloadAsset() name " + line);

                FileInputStream input = new FileInputStream(folder.getAbsolutePath() + File.separator + MAPFOLDER + File.separator + "imgs" + File.separator + line + ".png");
                Image image = new Image(input);
                ImageView imageView = new ImageView(image);

                this.getOriginalAssetList().add(image);

                SnapshotParameters param = new SnapshotParameters();
                param.setFill(Color.TRANSPARENT);
                imageView.setFitWidth(40);
                imageView.setFitHeight(40);
                imageView.setPreserveRatio(true);
                this.getFourtyAssetList().add(imageView.snapshot(param, null));

                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                this.getOneHundredAssetList().add(imageView.snapshot(param, null));

                imageView.setFitWidth(0.4 * image.getWidth());
                imageView.setFitHeight(0.4 * image.getHeight());
                imageView.setPreserveRatio(true);
                this.getPreviewAssetList().add(imageView.snapshot(param, null));



                input.close();
            }

            DataManager.getInstance().setAssetButtons(new ArrayList<>());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    public void addAsset(File folder) {

        int i = 0;
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                i++;
            }
        }

        //all buttons
        Button[] newAssetButtons = new Button[DataManager.getInstance().getAssetButtons().size() + i];


        for (final File fileEntry : folder.listFiles()) {

            if (!fileEntry.isDirectory()) {
                //System.out.println(fileEntry.());

                try (
                    FileInputStream input = new FileInputStream(folder.getAbsolutePath() + File.separator + fileEntry.getName());
                ){
                    Image image = new Image(input);
                    ImageView imageView = new ImageView(image);

                    String fileName = fileEntry.getName().replace(".png", "");
                    this.getAssetNameList().add(fileName);

                    this.getOriginalAssetList().add(image);

                    SnapshotParameters param = new SnapshotParameters();
                    param.setFill(Color.TRANSPARENT);

                    imageView.setFitWidth(40);
                    imageView.setFitHeight(40);
                    imageView.setPreserveRatio(true);
                    this.getFourtyAssetList().add(imageView.snapshot(param, null));


                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);
                    imageView.setPreserveRatio(true);
                    this.getOneHundredAssetList().add(imageView.snapshot(param, null));

                    imageView.setFitWidth(0.4 * image.getWidth());
                    imageView.setFitHeight(0.4 * image.getHeight());
                    imageView.setPreserveRatio(true);
                    this.getPreviewAssetList().add(imageView.snapshot(param, null));


                }catch(FileNotFoundException fnfe) {
                    fnfe.printStackTrace();
                }catch(IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }


        //for (Map.Entry<Integer, Image> entry  : AssetManager.getInstance().getOneHundredAssetList().entrySet()) {
        DataManager.getInstance().getAssetButtons().clear();
        for(int k=0; k<AssetManager.getInstance().getOneHundredAssetList().size(); k++) {
            //System.out.println(fileEntry.());
            Button button = new Button("", new ImageView(AssetManager.getInstance().getOneHundredAssetList().get(k)));
            button.setMaxSize(MyApplication.ASSETBUTTONWIDTH, MyApplication.ASSETBUTTONHEIGHT);
            button.setPrefSize(MyApplication.ASSETBUTTONWIDTH, MyApplication.ASSETBUTTONHEIGHT);
            button.setMinSize(MyApplication.ASSETBUTTONWIDTH, MyApplication.ASSETBUTTONHEIGHT);
            Tooltip tooltip = new Tooltip();
            tooltip.setGraphic(new ImageView(AssetManager.getInstance().getPreviewAssetList().get(k)));
            tooltip.setText(AssetManager.getInstance().getAssetNameList().get(k) + " - " + AssetManager.getInstance().getOriginalAssetList().get(k).getWidth() + " x " + AssetManager.getInstance().getOriginalAssetList().get(k).getHeight());
            button.setTooltip(tooltip);

            button.setUserData(k);

            button.setOnAction(action -> {
                //System.out.println(((Button)action.getSource()).getUserData());
                String indexString = ((Button) action.getSource()).getUserData().toString();
                DataManager.getInstance().setCurrentAssetIndex(Integer.parseInt(indexString));
            });

            newAssetButtons[k] = button;
            //DataManager.getInstance().setAssetPath(this.getMapFolder().getAbsolutePath());
        }

        DataManager.getInstance().getAssetButtons().addAll(Arrays.asList(newAssetButtons));
    }





    public void exportAssets(File exportFolder) {

        String gameImgsFolderPath = exportFolder + File.separator + GAMEFOLDER + File.separator + "imgs";
        Path path = Paths.get(gameImgsFolderPath);
        try{
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //for(Map.Entry<Integer, Image> entry : this.getOriginalAssetList().entrySet()) {
        for(int i=0; i<this.getOriginalAssetList().size(); i++) {
            String format = "png" ;
            //copy assets to map\imgs folder
            File toMapFolderFile = new File(exportFolder.getAbsolutePath() + File.separator + MAPFOLDER + File.separator + "imgs" + File.separator + this.getAssetNameList().get(i) + ".png");
            try{
                ImageIO.write(SwingFXUtils.fromFXImage(this.getOriginalAssetList().get(i), null), format, toMapFolderFile);
            } catch (Exception e) {
                System.out.println("copy asset index " + i + ", " + this.getOriginalAssetList().get(i));
                e.printStackTrace();
            }


            //copy assets to game\imgs folder, and append "_stand_right_0" to name
            //resize image based on zoom ratio
            File toGameFolderFile = new File(exportFolder.getAbsolutePath() + File.separator + GAMEFOLDER + File.separator + "imgs" + File.separator + this.getAssetNameList().get(i) + "_stand_right_0.png");
            try{
                SnapshotParameters param = new SnapshotParameters();
                param.setFill(Color.TRANSPARENT);

                ImageView imageView = new ImageView(this.getOriginalAssetList().get(i));
                imageView.setFitWidth(DataManager.getInstance().getZoomRatio() * this.getOriginalAssetList().get(i).getWidth());
                imageView.setFitHeight(DataManager.getInstance().getZoomRatio() * this.getOriginalAssetList().get(i).getHeight());
                imageView.setPreserveRatio(true);


                ImageIO.write(SwingFXUtils.fromFXImage(imageView.snapshot(param, null), null), format, toGameFolderFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    //FIXME add resize feature
    public void deployAssets() {
        String drawablePath = DataManager.getInstance().getGameResFolder() + File.separator + "drawable-nodpi";
        Path path = Paths.get(drawablePath);

        //for(Map.Entry<Integer, Image> entry : this.getOriginalAssetList().entrySet()) {
        for(int i=0; i<this.getOriginalAssetList().size(); i++) {
            String format = "png" ;
            //copy assets to game\imgs folder, and append "_stand_right_0" to name
            File drawableFolderFile = new File(path + File.separator + this.getAssetNameList().get(i) + "_stand_right_0.png");
            try{
                // exclude enemy character asset
                // exclude door asset
                if(this.getAssetNameList().get(i).indexOf("enemymodel") == -1 && this.getAssetNameList().get(i).indexOf("door") == -1) {

                    SnapshotParameters param = new SnapshotParameters();
                    param.setFill(Color.TRANSPARENT);

                    ImageView imageView = new ImageView(this.getOriginalAssetList().get(i));
                    imageView.setFitWidth(DataManager.getInstance().getZoomRatio() * this.getOriginalAssetList().get(i).getWidth());
                    imageView.setFitHeight(DataManager.getInstance().getZoomRatio() * this.getOriginalAssetList().get(i).getHeight());
                    imageView.setPreserveRatio(true);

                    ImageIO.write(SwingFXUtils.fromFXImage(imageView.snapshot(param, null), null), format, drawableFolderFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void resetAssetStore() {
        this.getOriginalAssetList().clear();
        this.getPreviewAssetList().clear();
        this.getOneHundredAssetList().clear();
        this.getFourtyAssetList().clear();
        this.getAssetNameList().clear();
    }

    public void copyFile(File source, File dest) {

        try (
            InputStream is = new FileInputStream(source);
            OutputStream os = new FileOutputStream(dest);
        ){
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Integer> getAssetsByName(String[] keys) {
        boolean containsAll;
        ArrayList<Integer> validAssetsIndex = new ArrayList<>();
        //for(Map.Entry<Integer, String> entry : this.getAssetNameList().entrySet()) {
        for(int i=0; i<this.getAssetNameList().size(); i++) {
            containsAll = true;
            for (int k = 0; k < keys.length; k++) {
                if (!this.getAssetNameList().get(i).contains(keys[k])) {
                    containsAll = false;
                    break;
                }
            }
            if (containsAll) {
                validAssetsIndex.add(i);
            }
        }

        return validAssetsIndex;
    }


    public ArrayList<Image> getOriginalAssetList() {
        return originalAssetList;
    }

    public void setOriginalAssetList(ArrayList<Image> originalAssetList) {
        this.originalAssetList = originalAssetList;
    }

    public ArrayList<Image> getOneHundredAssetList() {
        return oneHundredAssetList;
    }

    public void setOneHundredAssetList(ArrayList<Image> oneHundredAssetList) {
        this.oneHundredAssetList = oneHundredAssetList;
    }

    public ArrayList<Image> getFourtyAssetList() {
        return fourtyAssetList;
    }

    public void setFourtyAssetList(ArrayList<Image> fourtyAssetList) {
        this.fourtyAssetList = fourtyAssetList;
    }

    public ArrayList<Image> getPreviewAssetList() {
        return previewAssetList;
    }

    public void setPreviewAssetList(ArrayList<Image> previewAssetList) {
        this.previewAssetList = previewAssetList;
    }

    public ArrayList<String> getAssetNameList() {
        return assetNameList;
    }

    public void setAssetNameList(ArrayList<String> assetNameList) {
        this.assetNameList = assetNameList;
    }
}
