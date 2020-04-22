/*
 * ===================================================
 *                      contents
 * ===================================================
 * 00- Free draw
 * 01- rubber
 * 02- draw Line
 * 03- draw Rectangele
 * 04- draw Circle
 * 05- draw Ellipse
 * 06- Text
 *
 * ----------------------------------------------------
 *                     Features
 * ----------------------------------------------------
 * - the ability to change Line color
 * - the ability to change Fill color
 * - the ability to change Line width
 * - Undo & Redo
 * - Open Image && save Image
 *
 * ____________________________________________________
 * problems
 * - undo & redo : not working with free draw and rubber
 * - Line & Rect & Circ ... aren't be updated while drawing
 * ===================================================
 */
package org.yuxiang;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Paint extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        /* ----------Drow Canvas---------- */
        Canvas canvas = new Canvas(1080, 790);







        /*------- Undo & Redo ------*/


        /*------- Save & Open ------*/
        // Open
        open.setOnAction((e) -> {
            FileChooser openFile = new FileChooser();
            openFile.setTitle("Open File");
            File file = openFile.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    InputStream io = new FileInputStream(file);
                    Image img = new Image(io);
                    gc.drawImage(img, 0, 0);
                } catch (IOException ex) {
                    System.out.println("Error!");
                }
            }
        });

        // Save
        save.setOnAction((e) -> {
            FileChooser savefile = new FileChooser();
            savefile.setTitle("Save File");

            File file = savefile.showSaveDialog(primaryStage);
            if (file != null) {
                try {
                    WritableImage writableImage = new WritableImage(1080, 790);
                    canvas.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                } catch (IOException ex) {
                    System.out.println("Error!");
                }
            }

        });

        /* ----------STAGE & SCENE---------- */
        BorderPane pane = new BorderPane();
        pane.setLeft(btns);
        pane.setCenter(canvas);

        Scene scene = new Scene(pane, 1200, 800);

        primaryStage.setTitle("Paint");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}