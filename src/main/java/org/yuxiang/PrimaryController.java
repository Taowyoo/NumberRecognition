package org.yuxiang;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class PrimaryController {

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
    GraphicsContext gc;
    NumRecognitionDL4j model;
    @FXML
    public void initialize() {
        // Neutral net model initialization
        model = NumRecognitionDL4j.getInstance();

        // Draw Canvas

        gc = canvasDraw.getGraphicsContext2D();
        gc.setLineWidth(10);
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



    public void btnClearOnAction(ActionEvent actionEvent) {
        gc.clearRect(0, 0, canvasDraw.getWidth(), canvasDraw.getHeight());
    }

    public void btnSubmitOnAction(ActionEvent actionEvent) {
    }

    public void btnChooseOnAction(ActionEvent actionEvent) {
    }

    public void btnSubmitFileOnAction(ActionEvent actionEvent) {
    }
}
