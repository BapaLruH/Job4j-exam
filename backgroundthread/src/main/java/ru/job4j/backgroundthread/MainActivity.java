package ru.job4j.backgroundthread;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Dev_tag";
    private Thread testThread;
    private Thread testRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
}
