package ru.job4j.asynctask;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "DEV_LOG";
    private Button btnStart;
    private Disposable sbr;
    private ProgressBar prBar;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        boolean recreated = bundle != null;
        int progressValue = recreated ? bundle.getInt("progress", 0) : 0;
        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(v -> startTask(progressValue));
        prBar = findViewById(R.id.prBar);
        if (recreated) {
            startTask(progressValue);
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("progress", prBar.getProgress());
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

    public void startTask(int startValue) {
        //new SimpleAsyncTask(this).execute(10);
        int period = prBar.getMax();
        this.sbr = Observable.interval(period, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(aLong -> aLong.intValue() == period)
                .doOnSubscribe(disposable -> btnStart.setClickable(false))
                .doOnComplete(() -> {
                    btnStart.setClickable(true);
                    prBar.setProgress(0);
                    this.sbr.dispose();
                })
                .subscribe(v -> prBar.setProgress(startValue + v.intValue()));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.sbr != null && !this.sbr.isDisposed()) {
            this.sbr.dispose();
        }
    }

    public void onClickStop(View view) {
        if (this.sbr != null && !this.sbr.isDisposed()) {
            this.sbr.dispose();
            btnStart.setClickable(true);
            prBar.setProgress(0);
        }
    }

    private static class SimpleAsyncTask extends AsyncTask<Integer, Integer, String> {
        private WeakReference<MainActivity> activityWeakReference;

        private SimpleAsyncTask(MainActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.prBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            int count = 0;
            while (count < integers[0]) {
                publishProgress((count * 100) / integers[0]);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
                count++;
            }
            return "finish";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.prBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            activity.prBar.setProgress(0);
            activity.prBar.setVisibility(View.INVISIBLE);
        }
    }
}
