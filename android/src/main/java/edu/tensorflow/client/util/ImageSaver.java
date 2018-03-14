package edu.tensorflow.client.util;

/**
 * Class to save image in JPEG format.
 * Created by Zoltan Szabo on 3/8/18.
 */

import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static edu.tensorflow.client.Config.LOGGING_TAG;

/**
 * Saves a JPEG {@link Image} into the specified {@link File}.
 */
public class ImageSaver implements Runnable {
    private final PersistImageTask persistImageTask;

    public ImageSaver(final PersistImageTask persistImageTask) {
        this.persistImageTask = persistImageTask;
    }

    @Override
    public void run() {
        try (FileOutputStream output = new FileOutputStream(persistImageTask.getFile())) {
            persistImageTask.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, output);
            persistImageTask.getActionListener().actionPerformed();
        } catch (IOException ex) {
            Log.e(LOGGING_TAG, ex.getMessage());
        }
    }
}
