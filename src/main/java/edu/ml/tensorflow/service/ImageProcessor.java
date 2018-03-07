package edu.ml.tensorflow.service;

import edu.ml.tensorflow.ApplicationProperties;
import edu.ml.tensorflow.model.analyzer.SetOfCards;
import edu.ml.tensorflow.model.recognition.BoxPosition;
import edu.ml.tensorflow.model.recognition.Recognition;
import edu.ml.tensorflow.util.IOUtil;
import edu.ml.tensorflow.util.ServiceException;
import groovy.lang.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Image processor class
 */
@Service
@Singleton
public class ImageProcessor {
    private final static Logger LOGGER = LoggerFactory.getLogger(ImageProcessor.class);
    private ApplicationProperties applicationProperties;

    public ImageProcessor(final ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        IOUtil.createDirIfNotExists(new File(applicationProperties.getOutputDir()));
    }

    /**
     * Label image with classes and predictions given by the ThensorFLow
     * @param image buffered image to label
     * @param recognitions list of recognized objects
     * @return location of the labeled image
     */
    public String labelImage(final byte[] image, final List<Recognition> recognitions, final String fileName) {
        BufferedImage bufferedImage = createImageFromBytes(image);
        float scaleX = (float) bufferedImage.getWidth() / (float) applicationProperties.getImageSize();
        float scaleY = (float) bufferedImage.getHeight() / (float) applicationProperties.getImageSize();
        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        graphics.setColor(Color.green);

        for (Recognition recognition: recognitions) {
            BoxPosition box = recognition.getScaledLocation(scaleX, scaleY);
            //draw text
            graphics.drawString(recognition.getTitle() + " " + recognition.getConfidence(), box.getLeft(), box.getTop() - 7);
            // draw bounding box
            graphics.drawRect(box.getLeftInt(),box.getTopInt(), box.getWidthInt(), box.getHeightInt());
        }

        graphics.dispose();
        return saveImage(bufferedImage, applicationProperties.getOutputDir() + "/" + fileName);
    }

    public String labelCards(final byte[] image, final SetOfCards setOfCards) {
        List<Recognition> recognitions = setOfCards.getCards().stream()
                .map((card) -> card.getRecognition()).collect(Collectors.toList());

        return labelImage(image, recognitions, UUID.randomUUID().toString() + ".jpg");
    }

    /**
     *
     * @param image to save
     * @param target to save image
     * @return location of the labeled image
     */
    public String saveImage(final BufferedImage image, final String target) {
        try {
            ImageIO.write(image,"jpg", new File(target));
            return target;
        } catch (IOException ex) {
            LOGGER.error("Unagle to save image {}!", target);
            throw new RuntimeException(ex);
        }
    }

    private BufferedImage createImageFromBytes(final byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException ex) {
            throw new ServiceException("Unable to create image from bytes!", ex);
        }
    }
}
