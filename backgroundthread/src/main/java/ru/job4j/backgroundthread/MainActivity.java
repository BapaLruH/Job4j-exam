package ru.job4j.backgroundthread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Dev_tag";
    private ImageView imgV;
    private ProgressBar prBar;
    private Button btnLoadImg;
    private String[] imgsUrl;
    private AtomicInteger index = new AtomicInteger(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createUrlList();
        imgV = findViewById(R.id.imgV);
        prBar = findViewById(R.id.prBarLoadImg);
        btnLoadImg = findViewById(R.id.btnLoadImg);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    private void createUrlList() {
        this.imgsUrl = new String[]{
                "http://www.fonstola.ru/pic/201604/2560x1440/fonstola.ru-229435.jpg",
                "http://www.kartinkijane.ru/pic/201304/1440x900/kartinkijane.ru-16213.jpg",
                "http://www.hqwallpapers.ru/wallpapers/nature/skala-i-okean.jpg",
                "https://w-dog.ru/wallpapers/9/7/496404812485632.jpg",
                "https://look.com.ua/pic/201209/2560x1440/look.com.ua-45399.jpg"
        };
    }

    public void loadImage(View view) {
        if (index.get() >= imgsUrl.length) index.set(0);
        new ImagesLoaderAsyncTask(this).execute(imgsUrl[index.getAndIncrement()]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class ImagesLoaderAsyncTask extends AsyncTask<String, Integer, Bitmap> {
        private WeakReference<MainActivity> mainActivityWeakReference;

        private ImagesLoaderAsyncTask(MainActivity activity) {
            this.mainActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            BufferedInputStream is = null;
            int fileSize, count, total, lastNumber;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                URL fileUrl = new URL(strings[0]);
                URLConnection connection = fileUrl.openConnection();
                connection.connect();
                fileSize = connection.getContentLength();
                is = new BufferedInputStream(fileUrl.openStream(), 8192);
                byte[] data = new byte[4096];
                count = is.read(data);
                total = lastNumber = 0;
                while (count != -1) {
                    total += count;
                    baos.write(data, 0, count);
                    int currentNumber = (total * 100) / fileSize;
                    if (lastNumber != currentNumber) {
                        publishProgress(currentNumber);
                        lastNumber = currentNumber;
                    }
                    count = is.read(data);
                }
                baos.flush();
                byte[] array = baos.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage());
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity activity = mainActivityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.prBar.setVisibility(View.VISIBLE);
            activity.btnLoadImg.setClickable(false);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            MainActivity activity = mainActivityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.imgV.setImageBitmap(bitmap);
            activity.prBar.setVisibility(View.INVISIBLE);
            activity.prBar.setProgress(0);
            activity.btnLoadImg.setClickable(true);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            MainActivity activity = mainActivityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.prBar.setProgress(values[0]);
        }
    }
}


