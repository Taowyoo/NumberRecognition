package org.yuxiang;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.io.ClassPathResource;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class NumRecognitionDL4j extends NumRecognition{
    // Logger for this class
    private final static Logger LOGGER = Logger.getLogger(NumRecognitionDL4j.class.getName());
    private final NativeImageLoader loader;  // for load Image into specific format
    private final ImagePreProcessingScaler scaler;  // for normalize image
    MultiLayerNetwork model;
    // private instance, so that it can be
    // accessed by only by getInstance() method
    private static NumRecognitionDL4j instance;
    // synchronized method to control simultaneous access
    synchronized public static NumRecognitionDL4j getInstance()
    {
        if (instance == null)
        {
            // if instance is null, initialize
            instance = new NumRecognitionDL4j();
        }
        return instance;
    }

    private NumRecognitionDL4j() {
        // load the model
        LOGGER.log(Level.INFO, "Loading HDF5 model exported from python:");
        try {
            String simpleMlp = new ClassPathResource("org/yuxiang/model.h5").getFile().getPath();
            model = KerasModelImport.importKerasSequentialModelAndWeights(simpleMlp);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKerasConfigurationException e) {
            e.printStackTrace();
        } catch (UnsupportedKerasConfigurationException e) {
            e.printStackTrace();
        }
        LOGGER.log(Level.INFO, "Loading succeeded:");
        LOGGER.log(Level.INFO,"Loading succeeded:");
        LOGGER.log(Level.INFO,"Model Summary:\n"+model.summary());
        height = 28;
        width = 28;
        channels = 1;
        labelList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        loader = new NativeImageLoader(height, width, channels);
        scaler = new ImagePreProcessingScaler(0, 1);

    }
    public float[] recognize(Image img) throws IOException {
        // het the image into an INDArray
        INDArray  image = loader.asMatrix(img);
        // normalize echo pixel in the image from 0-255 to 0-1
        scaler.transform(image);
        // pass through to neural Net
        INDArray output = model.output(image);
        float[] res = output.toFloatVector();
        LOGGER.log(Level.INFO,"LabelList:");
        LOGGER.log(Level.INFO,labelList.toString());
        LOGGER.log(Level.INFO,"Predict Result(Probability):");
        LOGGER.log(Level.INFO,res.toString());
        return res;
    }

    @Override
    public List<Integer> getLabelList() {
        return labelList;
    }
}
