/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schenplayground.fight_map_editor;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import javax.swing.text.View;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.schenplayground.fight_map_editor.DataManager.*;
import static com.schenplayground.fight_map_editor.ViewManager.*;

/**
 *
 * @author schen
 * image assets related change goes to this class
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


    //for import map
    public void reloadAsset(File folder) {
        this.resetAssetStore();


        //read data file
        try {
            FileReader fileReader = new FileReader(folder.getAbsolutePath() + File.separator + MAPFOLDER + File.separator + folder.getName() + DATAFILENAME);
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
            FileReader fileReader = new FileReader(folder.getAbsolutePath() + File.separator + MAPFOLDER + File.separator + folder.getName() + ASSETFILENAME);
            BufferedReader bufferedReader = new BufferedReader(fileReader)
        ){


            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if(!this.getAssetNameList().contains(line)) {
                    this.getAssetNameList().add(line);
                }

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


        for (final File fileEntry : folder.listFiles()) {

            if (!fileEntry.isDirectory()) {
                //System.out.println(fileEntry.());

                try (
                    FileInputStream input = new FileInputStream(folder.getAbsolutePath() + File.separator + fileEntry.getName())
                ){
                    Image image = new Image(input);
                    ImageView imageView = new ImageView(image);

                    String fileName = fileEntry.getName().replace(".png", "");

                    if(fileName.contains("_")) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "invalid asset name, please verify and retry");
                        alert.setHeaderText("import asset");
                        ButtonType buttonTypeCancel = new ButtonType( "OK", ButtonBar.ButtonData.CANCEL_CLOSE );
                        alert.getButtonTypes().setAll(buttonTypeCancel);
                        alert.show(); //.showAndWait() block the current thread, show() does not
                        break;
                    }

                    if(!this.getAssetNameList().contains(fileName)) {
                        this.getAssetNameList().add(fileName);
                    }

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


        ViewManager.getInstance().updateAssetButtons(i);
        ViewManager.getInstance().initializeAssetPane();

    }


    public void exportAssets(File exportFolder) {

        String mapImgsFolderPath = exportFolder + File.separator + MAPFOLDER + File.separator + "imgs";
        Path mapFolderPath = Paths.get(mapImgsFolderPath);
        try{
            Files.createDirectories(mapFolderPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String gameImgsFolderPath = exportFolder + File.separator + GAMEFOLDER + File.separator + "imgs";
        Path gameFolderPath = Paths.get(gameImgsFolderPath);
        try{
            Files.createDirectories(gameFolderPath);
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
            if(DataManager.getInstance().getUpdatedImageNameList().contains(this.getAssetNameList().get(i))) {
                File toGameFolderFile = new File(exportFolder.getAbsolutePath() + File.separator + GAMEFOLDER + File.separator + "imgs" + File.separator + this.getAssetNameList().get(i) + "_stand_right_0.png");
                try {
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
    }


    public boolean isCompletelyTransparent(Image image) {
        PixelReader pixelReader = image.getPixelReader();
        int totalAlpha = 0;
        for(int i=0; i<image.getHeight(); i++) {
            for(int j=0; j<image.getWidth(); j++) {
                int argb = pixelReader.getArgb(j, i);
                int alpha = (argb >> 24) & 0xFF;
                totalAlpha += alpha;
/*        int red = (argb >> 16) & 0xFF;
        int green = (argb >> 8) & 0xFF;
        int blue = argb & 0xFF;*/
            }
        }
        return totalAlpha == 0 ? true : false;
    }



    //FIXME add resize feature
    public void deployAssets() {
        Path path = Paths.get(DataManager.getInstance().getGameImageAssetsFolder());
        String format = "png";
        //for(Map.Entry<Integer, Image> entry : this.getOriginalAssetList().entrySet()) {
        for(int i=0; i<this.getOriginalAssetList().size(); i++) {
            if(DataManager.getInstance().getUpdatedImageNameList().contains(this.getAssetNameList().get(i))) {
                //copy assets to game\imgs folder, and append "_stand_right_0" to name
                File imageAssetFile = new File(path + File.separator + this.getAssetNameList().get(i) + "_stand_right_0.png");
                try {
                    // exclude enemy character asset
                    // exclude door asset
                    if (this.getAssetNameList().get(i).indexOf("enemymodel") == -1 && this.getAssetNameList().get(i).indexOf("door") == -1) {

                        SnapshotParameters param = new SnapshotParameters();
                        param.setFill(Color.TRANSPARENT);

                        ImageView imageView = new ImageView(this.getOriginalAssetList().get(i));
                        imageView.setFitWidth(DataManager.getInstance().getZoomRatio() * this.getOriginalAssetList().get(i).getWidth());
                        imageView.setFitHeight(DataManager.getInstance().getZoomRatio() * this.getOriginalAssetList().get(i).getHeight());
                        imageView.setPreserveRatio(true);

                        ImageIO.write(SwingFXUtils.fromFXImage(imageView.snapshot(param, null), null), format, imageAssetFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
            OutputStream os = new FileOutputStream(dest)
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


    public ArrayList<Integer> getAssetsIndexByName(String[] keys) {
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


    public void importAssets() {
        File assetsFolder = new File(ViewManager.getInstance().getDirectoryChooser().showDialog(ViewManager.getInstance().getPrimaryStage()).getAbsolutePath());
        DataManager.getInstance().setAssetPath(assetsFolder.getAbsolutePath());
        this.addAsset(assetsFolder);
    }

    public void batchDeleteAssets() {
        Dialog dialog = new Dialog<>();
        dialog.setTitle("batch delete assets");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField assetPrefix = new TextField();
        assetPrefix.setPromptText("e.g. dune1-layer2");

        gridPane.add(new Label("asset name: "), 0, 0);
        gridPane.add(assetPrefix, 1, 0);


        dialog.getDialogPane().setContent(gridPane);

        Optional result = dialog.showAndWait();
        if (result.isPresent() && !assetPrefix.getText().isEmpty()) {
            System.out.println("assetPrefix: " + assetPrefix.getText());

            List<Integer> assetsIndex = AssetManager.getInstance().getAssetsIndexByName(new String[]{assetPrefix.getText()});
            int assetTotal = assetsIndex.size();
            System.out.println("assetTotal: " + assetTotal);

            for(int i=0; i<assetsIndex.size(); i++) {
                //delete will update all assets after the current one, so always delete the first
                /*
                0, 1, 2, 3
                delete 0
                0, 1, 2
                delete 0
                0, 1
                delete 0
                1
                 */
                deleteAsset(assetsIndex.get(0));
            }
        } else {
            System.out.println("Result not present => Cancel might have been pressed");
        }
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
        ViewManager.getInstance().getAssetPane().getChildren().clear();
        for(int i=0; i<DataManager.getInstance().getAssetButtons().size(); i++) {
            System.out.println(i + " - " + DataManager.getInstance().getAssetButtons().get(i));
            ViewManager.getInstance().getAssetPane().getChildren().add(DataManager.getInstance().getAssetButtons().get(i));
        }

        //delete asset png from game\img folder



        //update datamanager property
        if(DataManager.getInstance().getCurrentAssetIndex() == assetIndex) {
            DataManager.getInstance().setCurrentAssetIndex(-1);
        }

        //update assetmanager property
        this.getOriginalAssetList().remove(assetIndex);
        this.getOneHundredAssetList().remove(assetIndex);
        this.getFourtyAssetList().remove(assetIndex);
        this.getPreviewAssetList().remove(assetIndex);
        this.getAssetNameList().remove(assetIndex);
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
