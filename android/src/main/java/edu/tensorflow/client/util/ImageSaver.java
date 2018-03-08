package edu.tensorflow.client.util;

/**
 * Created by Zoltan Szabo on 3/8/18.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Saves a JPEG {@link Image} into the specified {@link File}.
 */
public class ImageSaver implements Runnable {
    /**
     * The JPEG image
     */
    private final Image image;
    /**
     * The file we save the image into.
     */
    private final File file;

    public ImageSaver(Image image, File file) {
        this.image = image;
        this.file = file;
    }

    @Override
    public void run() {
        Bitmap bitmap = convertImageToBitmap(image);
        try (FileOutputStream output = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, output);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            image.close();
        }
    }

    private Bitmap convertImageToBitmap(Image image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
    }

    public Bitmap resizeBitmap(Bitmap bitmap, int size) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int largestSide = (width > height)
                ? width : height;
        float scale = (float) size / (float) largestSide;
        // Create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // Resize the bit map
        matrix.postScale(scale, scale);
        // Recreate the new Bitmap
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
    }
}
