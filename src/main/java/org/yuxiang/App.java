/*
 * Copyright (c)  2019-2020, YuxiangCao
 * File Name: App.java
 * Date: 4/22/20, 3:56 PM
 * Description: Main class for launch the app
 */

package org.yuxiang;

// javafx
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
// java
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // load fxml
        scene = new Scene(loadFXML("primary"));
        // set scene to stage
        stage.setScene(scene);
        stage.setTitle("Simple Number Recognition");
        stage.show();
    }

    /**
     * utility method for loading fxml file
     * @param fxml fxml file name
     * @return Parsed data
     * @throws IOException
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args)  {
        launch();
    }

}