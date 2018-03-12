package edu.tensorflow.client.camera;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.SystemClock;
import android.util.Log;
import android.util.Size;

import java.util.Vector;

import edu.tensorflow.client.R;
import edu.tensorflow.client.camera.view.OverlayView;
import edu.tensorflow.client.util.ImageUtils;

import static edu.tensorflow.client.Config.INPUT_SIZE;
import static edu.tensorflow.client.Config.LOGGING_TAG;

/**
 * Classifier activity class
 * Modified by Zoltan Szabo
 */
public class ClassifierActivity extends CameraActivity implements OnImageAvailableListener {
    private boolean MAINTAIN_ASPECT = true;

    private Integer sensorOrientation;
    private int previewWidth = 0;
    private int previewHeight = 0;
    private byte[][] yuvBytes;
    private int[] rgbBytes = null;
    private Bitmap rgbFrameBitmap = null;
    private Bitmap croppedBitmap = null;
    private boolean computing = false;
    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;
    private OverlayView overlayView;
    private long lastProcessingTimeMs;

    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {
        overlayView = findViewById(R.id.overlay);
        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        final int screenOrientation = getWindowManager().getDefaultDisplay().getRotation();

        Log.i(LOGGING_TAG, String.format("Sensor orientation: %d, Screen orientation: %d",
                rotation, screenOrientation));

        sensorOrientation = rotation + screenOrientation;

        Log.i(LOGGING_TAG, String.format("Initializing at size %dx%d", previewWidth, previewHeight));
        rgbBytes = new int[previewWidth * previewHeight];
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
        croppedBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Config.ARGB_8888);

        frameToCropTransform = ImageUtils.getTransformationMatrix(previewWidth, previewHeight,
                INPUT_SIZE, INPUT_SIZE, sensorOrientation, MAINTAIN_ASPECT);

        cropToFrameTransform = new Matrix();
        frameToCropTransform.invert(cropToFrameTransform);

        yuvBytes = new byte[3][];

        addCallback((final Canvas canvas) -> renderAdditionalInformation(canvas));
    }

    @Override
    public void onImageAvailable(final ImageReader reader) {
        Image image = null;

        try {
            image = reader.acquireLatestImage();

            if (image == null) {
                return;
            }

            if (computing) {
                image.close();
                return;
            }
            computing = true;

            final Plane[] planes = image.getPlanes();
            fillBytes(planes, yuvBytes);

            final int yRowStride = planes[0].getRowStride();
            final int uvRowStride = planes[1].getRowStride();
            final int uvPixelStride = planes[1].getPixelStride();
            ImageUtils.convertYUV420ToARGB8888(
                    yuvBytes[0],
                    yuvBytes[1],
                    yuvBytes[2],
                    previewWidth,
                    previewHeight,
                    yRowStride,
                    uvRowStride,
                    uvPixelStride,
                    rgbBytes);

            image.close();
        } catch (final Exception ex) {
            if (image != null) {
                image.close();
            }
            Log.e("Exception: ", ex.getMessage());
            return;
        }

        rgbFrameBitmap.setPixels(rgbBytes, 0, previewWidth, 0, 0, previewWidth, previewHeight);
        new Canvas(croppedBitmap).drawBitmap(rgbFrameBitmap, frameToCropTransform, null);

        runInBackground(() -> {
            final long startTime = SystemClock.uptimeMillis();
            //final List<Recognition> results = recognizer.recognizeImage(croppedBitmap);
            lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;
            overlayView.setResults();
            requestRender();
            computing = false;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void renderAdditionalInformation(final Canvas canvas) {
        final Vector<String> rows = new Vector();
        rows.add("Frame: " + previewWidth + "x" + previewHeight);
        rows.add("View: " + canvas.getWidth() + "x" + canvas.getHeight());
        rows.add("Rotation: " + sensorOrientation);
        rows.add("Inference time: " + lastProcessingTimeMs + "ms");
    }
}
