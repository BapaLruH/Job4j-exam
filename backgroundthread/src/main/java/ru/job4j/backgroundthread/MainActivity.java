package ru.job4j.backgroundthread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Dev_tag";
    private Thread testThread;
    private Thread testRunnable;
    private TextView tvCount;
    private ImageView imgV, imgV2;
    private String[] imgsUrl;
    private AtomicInteger index = new AtomicInteger(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createUrlList();
        tvCount = findViewById(R.id.tvCount);
        imgV = findViewById(R.id.imgV);
        imgV2 = findViewById(R.id.imgV2);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void createUrlList() {
        this.imgsUrl = new String[]{
                "http://www.fonstola.ru/pic/201604/2560x1440/fonstola.ru-229435.jpg",
                "http://www.kartinkijane.ru/pic/201304/1440x900/kartinkijane.ru-16213.jpg",
                "http://www.hqwallpapers.ru/wallpapers/nature/skala-i-okean.jpg"
        };
    }

    public void startThread(View view) {
        testThread = new TestThread(10);
        testThread.start();
        testRunnable = new Thread(new TestRunnable(10));
        testRunnable.start();
    }

    public void stopThread(View view) {
        testThread.interrupt();
        testRunnable.interrupt();
    }

    public void loadImage(View view) {
        new Thread(new ImagesLoader(imgV)).start();
        new Thread(new ImagesLoader(imgV2)).start();
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

    class ImagesLoader implements Runnable {
        private final ImageView imageView;

        ImagesLoader(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        public void run() {
            if (index.get() >= imgsUrl.length) index.set(0);
            final Bitmap img = loadImageFromNetwork(imgsUrl[index.getAndIncrement()]);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(img);
                }
            });
        }

        private Bitmap loadImageFromNetwork(String url) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage());
            }
            return bitmap;
        }
    }

    class TestThread extends Thread {
        private int times;

        TestThread(int times) {
            this.times = times;
        }

        @Override
        public void run() {
            getRunMethod(times, getClass().getSimpleName());
        }
    }

    class TestRunnable implements Runnable {
        private int times;

        TestRunnable(int times) {
            this.times = times;
        }

        @Override
        public void run() {
            getRunMethod(times, getClass().getSimpleName());
        }
    }

    private void getRunMethod(int times, String sourceName) {
        int count = 0;
        while (count != times && !Thread.currentThread().isInterrupted()) {
            final int finalCount = count;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvCount.setText(String.format(Locale.getDefault(), "%d%%", finalCount * 10));
                }
            });
            Log.d(LOG_TAG, "start" + sourceName + ": " + count);
            count++;
            if (Thread.currentThread().isInterrupted()) {
                Log.d(LOG_TAG, sourceName + " is interrupted");
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(LOG_TAG, Arrays.toString(e.getStackTrace()));
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (testThread != null && testRunnable !=null) {
            testThread.interrupt();
            testRunnable.interrupt();
        }
    }
}


