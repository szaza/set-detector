package edu.tensorflow.client.util;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Image persisting task. It is necessary because we cannot do it on the main thread so,
 * we have to use a background thread.
 * Created by Zoltan Szabo on 3/12/18.
 */

public class PersistImageTask {
    private Bitmap bitmap;
    private File file;
    private ActionListener actionListener;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public ActionListener getActionListener() {
        return actionListener;
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }
}
