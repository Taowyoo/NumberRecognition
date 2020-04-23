/*
 * Copyright (c)  2019-2020, YuxiangCao
 * File Name: NumRecognition.java
 * Date: 4/22/20, 3:54 PM
 * Description: Abstract Class for number recognition classes for different implement of model utility
 */

package org.yuxiang;

import java.awt.image.BufferedImage;
import java.util.List;

public abstract class NumRecognition {
    protected   int height;  // target image height for the input of Neural Net
    protected  int width;  // target image width for the input of Neural Net
    protected  int channels;  // target image channels for the input of Neural Net

    protected  List<Integer> labelList;  // label list for number Recognition: 0-9

    /**
     * recogniz number image by Using Neural Net
     * @param img input number image
     * @return the probability array of labels
     * @throws Exception any exception while process
     */
    public abstract float[] recognize(BufferedImage img) throws Exception;

    public abstract List<Integer> getLabelList();
}
