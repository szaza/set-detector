package edu.tensorflow.client.camera;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.View;

import java.io.File;
import java.nio.ByteBuffer;

import edu.tensorflow.client.R;
import edu.tensorflow.client.api.SETDetectorService;
import edu.tensorflow.client.api.dto.ResultDTO;
import edu.tensorflow.client.util.ImageSaver;
import edu.tensorflow.client.util.ImageUtils;
import edu.tensorflow.client.util.PersistImageTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static edu.tensorflow.client.Config.INPUT_SIZE;
import static edu.tensorflow.client.Config.LOGGING_TAG;

/**
 * Classifier activity class
 * Modified by Zoltan Szabo
 */
public class ClassifierActivity extends CameraActivity implements OnImageAvailableListener {
    private int previewWidth = 0;
    private int previewHeight = 0;
    private Bitmap rgbFrameBitmap = null;
    private Bitmap croppedBitmap = null;
    private Matrix frameToCropTransform;
    private HandlerThread backgroundThread;
    private Handler backgroundHandler;
    private SETDetectorService setDetectorService;
    private File file;
    private boolean compute;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDetectorService = new SETDetectorService();
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        startBackgroundThread();

        file = new File(this.getExternalFilesDir(null), "pic.jpg");
        findViewById(R.id.send).setOnClickListener((View v) -> {
            if (!isNetworkConnected()) {
                createAlertDialog(getString(R.string.no_connection_title),
                        getString(R.string.no_connection_message));
            } else {
                if (croppedBitmap != null) {
                    compute = true;
                    backgroundHandler.post(new ImageSaver(createPersistImageTask()));
                    createProgressDialog();
                }
            }
        });
    }

    @Override
    public void onPause() {
        stopBackgroundThread();
        super.onPause();
    }

    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {
        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        final int screenOrientation = getWindowManager().getDefaultDisplay().getRotation();

        Log.i(LOGGING_TAG, String.format("Sensor orientation: %d, Screen orientation: %d",
                rotation, screenOrientation));

        Log.i(LOGGING_TAG, String.format("Initializing at size %dx%d", previewWidth, previewHeight));
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
        croppedBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Config.ARGB_8888);

        frameToCropTransform = ImageUtils.getTransformationMatrix(previewWidth, previewHeight,
                INPUT_SIZE, INPUT_SIZE, rotation + screenOrientation, true);

        Matrix cropToFrameTransform = new Matrix();
        frameToCropTransform.invert(cropToFrameTransform);
    }

    @Override
    public void onImageAvailable(final ImageReader reader) {
        Image image = null;

        try {
            image = reader.acquireLatestImage();

            if (image == null) {
                return;
            }

            if (!compute) {
                rgbFrameBitmap.setPixels(convertYUVToARGB(image), 0, previewWidth, 0, 0,
                        previewWidth, previewHeight);
                new Canvas(croppedBitmap).drawBitmap(rgbFrameBitmap, frameToCropTransform, null);
            }
            image.close();
        } catch (final Exception ex) {
            if (image != null) {
                image.close();
            }
            Log.e(LOGGING_TAG, ex.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Starts a background thread and its {@link Handler}.
     */
    private void startBackgroundThread() {
        backgroundThread = new HandlerThread("DetectThread");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopBackgroundThread() {
        backgroundThread.quitSafely();
        try {
            backgroundThread.join();
            backgroundThread = null;
            backgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int[] convertYUVToARGB(final Image image) {
        byte[][] yuvBytes = new byte[3][];
        final Plane[] planes = image.getPlanes();
        fillBytes(planes, yuvBytes);

        final int yRowStride = planes[0].getRowStride();
        final int uvRowStride = planes[1].getRowStride();
        final int uvPixelStride = planes[1].getPixelStride();
        return ImageUtils.convertYUV420ToARGB8888(yuvBytes[0], yuvBytes[1], yuvBytes[2], previewWidth,
                previewHeight, yRowStride, uvRowStride, uvPixelStride);
    }

    private void fillBytes(final Plane[] planes, final byte[][] yuvBytes) {
        for (int i = 0; i < planes.length; ++i) {
            final ByteBuffer buffer = planes[i].getBuffer();
            if (yuvBytes[i] == null) {
                yuvBytes[i] = new byte[buffer.capacity()];
            }
            buffer.get(yuvBytes[i]);
        }
    }

    private void startListActivity(final ResultDTO resultDTO) {
        Intent intent = new Intent(this, ListResultActivity.class);
        intent.putExtra("result", resultDTO);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private PersistImageTask createPersistImageTask() {
        PersistImageTask persistImageTask = new PersistImageTask();
        persistImageTask.setBitmap(croppedBitmap);
        persistImageTask.setFile(file);
        persistImageTask.setActionListener(() ->
            setDetectorService.detect(file).enqueue(new Callback<ResultDTO>() {
                @Override
                public void onResponse(Call<ResultDTO> call, Response<ResultDTO> response) {
                    compute = false;
                    closeProgressDialog();
                    startListActivity(response.body());
                }

                @Override
                public void onFailure(Call<ResultDTO> call, Throwable t) {
                    Log.e(LOGGING_TAG, "Error: " + t.getMessage());
                    compute = false;
                    closeProgressDialog();
                    createAlertDialog(getString(R.string.processing_error_title),
                            getString(R.string.processing_error_message));
                }
            }));

        return persistImageTask;
    }

    private void createProgressDialog() {
        compute = true;
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle(getString(R.string.progress_title));
        progressDialog.setMessage(getString(R.string.progress_message));
        progressDialog.show();
    }

    private void createAlertDialog(final String title, final String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
               (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    private boolean isNetworkConnected() {
        return ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo() != null;
    }

    private void closeProgressDialog() {
        progressDialog.hide();
        progressDialog.dismiss();
    }
}
