/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schenplayground.fight_map_editor;

import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.stage.Stage;

/**
 *
 * @author schen
 */
public class MyApplication extends Application {

    public static void main(String[] args) {
        launch(args); //invokes start()
    }

    @Override
    public void start(Stage primaryStage) {
        ViewManager.getInstance().init(primaryStage);
    }
}
