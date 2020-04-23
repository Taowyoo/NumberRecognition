/*
 * Copyright (c)  2019-2020, YuxiangCao
 * File Name: PrimaryController.java
 * Date: 4/22/20, 3:53 PM
 * Description: Conrtoller for main window
 */

package org.yuxiang;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.image.BufferedImage;
import java.util.stream.Collectors;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;

public class PrimaryController {
    // logger for printing log
    private final static Logger LOGGER = Logger.getLogger(PrimaryController.class.getName());
    // javafx Controls
    public Button btnChoose;
    public ImageView imageView;
    public Button btnSubmitFile;
    public Label labelResult1File;
    public Label labelResult1PossibilityFile;
    public Label labelResult2File;
    public Label labelResult2PossibilityFile;
    public Canvas canvasDraw;
    public ToggleButton btnDraw;
    public Button btnClear;
    public Button btnSubmit;
    public Label labelResult1;
    public Label labelResult1Possibility;
    public Label labelResult2;
    public Label labelResult2Possibility;
    public Slider slider;
    public Label labelLineWidth;
    public Button btnSave;

    GraphicsContext gc;  // for drawing on canvas
    NumRecognitionDL4j model; // Neural Net Model for recognize number
    final double DefaultLineWidth = 34;

    /**
     * initialize model, slider and canvas
     */
    @FXML
    public void initialize() {
        // Neutral net model initialization
        model = NumRecognitionDL4j.getInstance();
        // Draw Canvas
        gc = canvasDraw.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvasDraw.getWidth(), canvasDraw.getHeight());
        // set default line width for painting brush
        gc.setLineWidth(DefaultLineWidth);
        labelLineWidth.setText(String.format("%.1f", DefaultLineWidth));
        // set callbacks for mouse event to implement painting
        canvasDraw.setOnMousePressed(e -> {
            if (btnDraw.isSelected()) {
                gc.setStroke(Color.BLACK);
                gc.beginPath();
                gc.lineTo(e.getX(), e.getY());
            }
        });
        canvasDraw.setOnMouseDragged(e -> {
            if (btnDraw.isSelected()) {
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
            }
        });
        canvasDraw.setOnMouseReleased(e -> {
            if (btnDraw.isSelected()) {
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
                gc.closePath();
            }
        });

        // slider
        slider.valueProperty().addListener(e -> {
            double width = slider.getValue();
            labelLineWidth.setText(String.format("%.1f", width));
            gc.setLineWidth(width);
        });

    }

    /**
     * Callback for clear the canvas
     *
     * @param actionEvent click event on the button
     */
    public void btnClearOnAction(ActionEvent actionEvent) {
        gc.clearRect(0, 0, canvasDraw.getWidth(), canvasDraw.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvasDraw.getWidth(), canvasDraw.getHeight());
    }

    /**
     * Callback for submit painting to model and get the recognition result
     *
     * @param actionEvent click event on the button
     */
    public void btnSubmitOnAction(ActionEvent actionEvent) {
        WritableImage writableImage = new WritableImage((int)canvasDraw.getWidth(), (int)canvasDraw.getHeight());
        canvasDraw.snapshot(null, writableImage);
        try {
            Map<Integer, Float> integerFloatMap = identifyNum(writableImage);
            ArrayList<String> numbers = new ArrayList<String>();
            ArrayList<String> probabilities = new ArrayList<String>();
            Iterator<Map.Entry<Integer, Float>> it = integerFloatMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, Float> entry = it.next();
                numbers.add(String.valueOf(entry.getKey()));
                probabilities.add(String.format("%.1f", entry.getValue()*100)+"%");
            }
            labelResult1.setText(numbers.get(0));
            labelResult1Possibility.setText(probabilities.get(0));
            labelResult2.setText(numbers.get(1));
            labelResult2Possibility.setText(probabilities.get(1));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,"Model read image failed!");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Model read image failed!", ButtonType.CLOSE);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.CLOSE) {
                alert.close();
            }
        }


    }
    /**
     * Callback for choose image file to load
     *
     * @param actionEvent click event on the button
     */
    public void btnChooseOnAction(ActionEvent actionEvent) {
        FileChooser openFile = new FileChooser();
        openFile.setTitle("Open Image");
        openFile.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image", Arrays.asList("*.png","*.jpg","*.bmp","*.gif")),
                new FileChooser.ExtensionFilter("All", "*.*")
        );
        File file = openFile.showOpenDialog(((Node)actionEvent.getTarget()).getScene().getWindow());
        if (file != null) {
            try {
                InputStream io = new FileInputStream(file);
                Image img = new Image(io);
                imageView.setImage(img);
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING,"Fail to open image");
                Alert alert = new Alert(Alert.AlertType.ERROR, "Open image failed!", ButtonType.CLOSE);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.CLOSE) {
                    alert.close();
                }
            }
        }
    }
    /**
     * Callback for submit loaded image to be recognized and get result
     *
     * @param actionEvent click event on the button
     */
    public void btnSubmitFileOnAction(ActionEvent actionEvent) {
        try {
            Map<Integer, Float> integerFloatMap = identifyNum(imageView.getImage());
            ArrayList<String> numbers = new ArrayList<String>();
            ArrayList<String> probabilities = new ArrayList<String>();
            Iterator<Map.Entry<Integer, Float>> it = integerFloatMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, Float> entry = it.next();
                numbers.add(String.valueOf(entry.getKey()));
                probabilities.add(String.format("%.1f", entry.getValue()*100)+"%");
            }
            labelResult1File.setText(numbers.get(0));
            labelResult1PossibilityFile.setText(probabilities.get(0));
            labelResult2File.setText(numbers.get(1));
            labelResult2PossibilityFile.setText(probabilities.get(1));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,"Model read image failed!");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Model read image failed!", ButtonType.CLOSE);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.CLOSE) {
                alert.close();
            }
        }
    }
    /**
     * Callback for save painting into img file
     *
     * @param actionEvent click event on the button
     */
    public void btnSaveOnAction(ActionEvent actionEvent) {
        FileChooser saveFile = new FileChooser();
        saveFile.setTitle("Save As");
        saveFile.setInitialFileName("myPaintNum.png");
        saveFile.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image", "*.png"),
                new FileChooser.ExtensionFilter("All", "*.*")
        );
        File file = saveFile.showSaveDialog(((Node)actionEvent.getTarget()).getScene().getWindow());
        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage((int)canvasDraw.getWidth(), (int)canvasDraw.getHeight());
                canvasDraw.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING,"Fail to save image");
                Alert alert = new Alert(Alert.AlertType.ERROR, "Save image failed!", ButtonType.CLOSE);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.CLOSE) {
                    alert.close();
                }
            }
        }
    }

    /**
     * Transform javafx image format to BufferedImage and call the model to recognize the Image
     * and return the result in a LinkedHashMap
     *
     * @param fxImg Image in Javafx format
     * @return sorted probabilities with number labels in Map
     * @throws IOException
     */
    private Map<Integer,Float> identifyNum(Image fxImg) throws IOException {
        BufferedImage image = SwingFXUtils.fromFXImage(fxImg, null);
        float[] result = model.recognize(image);
        List<Integer> labels = model.getLabelList();
        // put result probabilities and babels into a Hashmap
        Map<Integer,Float> ret = new HashMap<>();
        if (result.length == labels.size()) {
            for (int i = 0; i < result.length; i++) {
                ret.put(labels.get(i),Float.valueOf(result[i]));
            }
        }
        // sort hashmap in descending order
        final Map<Integer, Float> sortedRet = ret.entrySet()
                .stream()
                .sorted((Map.Entry.<Integer, Float>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return sortedRet;
    }

}
