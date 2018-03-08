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
    private final static Double proportion = 0.75d;
    private ApplicationProperties applicationProperties;

    public ImageProcessor(final ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        IOUtil.createDirIfNotExists(new File(applicationProperties.getOutputDir()));
    }

    /**
     * Label image with classes and predictions given by the ThensorFLow
     * @param image to process
     * @param recognitions list of recognized objects
     * @return location of the labeled image
     */
    public String labelImage(final BufferedImage image, final List<Recognition> recognitions, final String fileName) {
        BufferedImage bufferedImage = cloneImage(image);
        float scaleX = (float) bufferedImage.getWidth() / (float) applicationProperties.getImageSize();
        float scaleY = (float) bufferedImage.getHeight() / (float) applicationProperties.getImageSize();
        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        graphics.setColor(Color.green);

        for (Recognition recognition: recognitions) {
            BoxPosition box = recognition.getScaledLocation(scaleX, scaleY);
            //draw text
            graphics.drawString(recognition.getTitle(), box.getLeft(), box.getTop() - 7);
            // draw bounding box
            graphics.drawRect(box.getLeftInt(),box.getTopInt(), box.getWidthInt(), box.getHeightInt());
        }

        graphics.dispose();
        return saveImage(bufferedImage, applicationProperties.getOutputDir() + "/" + fileName);
    }

    /**
     * Label the image with cards which compose a SET
     * @param image to process
     * @param setOfCards to process
     * @return string - the path to the image
     */
    public String labelCards(final BufferedImage image, final SetOfCards setOfCards) {
        BufferedImage bufferedImage = cloneImage(image);
        List<Recognition> recognitions = setOfCards.getCards().stream()
                .map((card) -> card.getRecognition()).collect(Collectors.toList());

        return labelImage(bufferedImage, recognitions, UUID.randomUUID().toString() + ".jpg");
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

    /**
     * Scales the image progressively
     * @param originalImage
     * @param width
     * @param height
     * @return rescaled image
     */
    public BufferedImage progressiveScaleImage(final BufferedImage originalImage, final Integer width, final Integer height) {
        BufferedImage resizedImage = cloneImage(originalImage);

        int newWidth = (int) ((float) originalImage.getWidth() * proportion);
        int newHeight = (int) ((float) originalImage.getHeight() * proportion);

        while (newWidth > width || newHeight > height) {
            resizedImage = scaleImage(resizedImage, newWidth, newHeight);
            newWidth = (int) ((float) newWidth * proportion);
            newHeight = (int) ((float) newHeight * proportion);
        }

        return resizedImage;
    }

    public BufferedImage cloneImage(final BufferedImage imageToClone) {
        BufferedImage clone = new BufferedImage(imageToClone.getWidth(), imageToClone.getHeight(), imageToClone.getType());
        Graphics2D cloneGraphics = (Graphics2D) clone.getGraphics();
        cloneGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        cloneGraphics.drawImage(imageToClone, 0, 0, null);
        cloneGraphics.dispose();
        return clone;
    }

    private BufferedImage scaleImage(final BufferedImage originalImage, final Integer width, final Integer height) {
        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D graphics = resizedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.drawImage(originalImage, 0, 0, width, height, null);
        graphics.dispose();
        return resizedImage;
    }

    public BufferedImage createImageFromBytes(final byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException ex) {
            throw new ServiceException("Unable to create image from bytes!", ex);
        }
    }
}
