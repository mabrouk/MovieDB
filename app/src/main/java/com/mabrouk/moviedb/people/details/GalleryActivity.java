package com.mabrouk.moviedb.people.details;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.Utils.BitmapUtils;
import com.mabrouk.moviedb.common.Utils.ExternalStorageUtil;
import com.mabrouk.moviedb.common.ScreenConstants;
import com.mabrouk.moviedb.network.MediaUrlBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoViewAttacher;

public class GalleryActivity extends AppCompatActivity {
    private static final String EXTRA_URL_PATH = "path";
    private static final String EXTRA_BLUR_URL = "blur_image_url";

    public static void startInstance(Activity activity, ProfileImage image, String url) {
        Intent intent = new Intent(activity, GalleryActivity.class);
        intent.putExtra(EXTRA_URL_PATH, image.filePath);
        intent.putExtra(EXTRA_BLUR_URL, url);
        activity.startActivity(intent);
    }

    DownloadAsyncTask task;

    String path;
    ProgressBar progressBar;
    ImageView imageView;
    File file;
    FloatingActionButton saveFab;
    PhotoViewAttacher photoViewAttacher;
    View errorLayout;
    boolean imageLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gellery);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imageView = (ImageView) findViewById(R.id.image_view);
        saveFab = (FloatingActionButton) findViewById(R.id.save_fab);
        saveFab.setOnClickListener(this::saveImage);

        errorLayout = findViewById(R.id.error_layout);
        errorLayout.setOnClickListener(view -> downloadImage());

        photoViewAttacher = new PhotoViewAttacher(imageView);
        photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);

        path = getIntent().getStringExtra(EXTRA_URL_PATH);
        file = new File(getCacheDir(), path.substring(1));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(0xff000000);

        downloadImage();
        displayBlurImage();
    }

    void downloadImage() {
        errorLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        task = new DownloadAsyncTask();
        task.execute(path);
    }

    void saveImage(View fab) {
        RxPermissions permissions = new RxPermissions(this);
        if (permissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            saveImage();
        }else {
            permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if(granted)
                            saveImage();
                        else
                            Toast.makeText(this, "Couldn't save the image file due to permission revoke", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    void displayBlurImage() {
        String blurUrl = getIntent().getStringExtra(EXTRA_BLUR_URL);
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                blurBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {}

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        };
        imageView.setTag(target);
        Picasso.with(this).load(blurUrl).into(target);
    }

    void blurBitmap(Bitmap bitmap) {
        Observable.fromCallable(() -> BitmapUtils.blurBitmap(bitmap))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(blurdBitmap -> {
                    imageView.setTag(null);
                    if(!imageLoaded) {
                        imageView.setImageBitmap(blurdBitmap);
                        photoViewAttacher.update();
                    }
                });
    }

    void saveImage() {
        if(ExternalStorageUtil.copyImageIntoExternalImageDirectory(file)) {
            Toast.makeText(this, "Image saved !", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Couldn't save image!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImage() {
        loadDownloadedBitmap();
        saveFab.setVisibility(View.VISIBLE);
        imageLoaded = true;

        imageView.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
                animateFab(true);
            else if (event.getAction() == MotionEvent.ACTION_UP)
                animateFab(false);
            photoViewAttacher.onTouch(view, event);
            return true;
        });
    }

    void animateFab(boolean down) {
        ScaleAnimation animation = down ? new ScaleAnimation(1, 0, 1, 0, saveFab.getWidth() / 2, saveFab.getHeight()/2)
                : new ScaleAnimation(0, 1, 0, 1, saveFab.getWidth() / 2, saveFab.getHeight()/2);
        animation.setDuration(150);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setFillAfter(true);
        saveFab.startAnimation(animation);
    }

    void loadDownloadedBitmap() {
        BitmapFactory.Options options = BitmapUtils.bitmapOptionsForSize(file, ScreenConstants.getScreenWidth(),
                ScreenConstants.getScreenHeight());
        loadNormalModeBitmap(options);
    }

    void loadNormalModeBitmap(BitmapFactory.Options options) {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        imageView.setImageBitmap(bitmap);
        photoViewAttacher.update();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        task.cancel(true);
        //delete file, clear cache
        file.delete();
    }

    class DownloadAsyncTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String url = new MediaUrlBuilder(path).build();
            try {
                file.createNewFile();
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                byte[] bytes = new byte[32 * 1024];
                int read;
                while ((read = inputStream.read(bytes)) != -1) {
                    if (isCancelled())
                        return null;
                    outputStream.write(bytes, 0, read);
                }
                outputStream.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            progressBar.setVisibility(View.GONE);
            if (b)
                loadImage();
            else {
                errorLayout.setVisibility(View.VISIBLE);
            }
        }
    }
}
