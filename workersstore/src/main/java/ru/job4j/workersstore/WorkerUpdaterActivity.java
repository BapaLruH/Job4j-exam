package ru.job4j.workersstore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Calendar;

public class WorkerUpdaterActivity extends AppCompatActivity {
    private Profession profession;
    private Worker worker;
    private FragmentManager fm;
    private static SecureRandom random = new SecureRandom();
    private boolean containsPhoto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_worker_updater_activity);

        fm = getSupportFragmentManager();
        Fragment cUFragment = fm.findFragmentById(R.id.worker_updater_fragment_container);
        worker = (Worker) getIntent().getSerializableExtra("worker");
        profession = (Profession) getIntent().getSerializableExtra("profession");
        if (cUFragment == null) {
            cUFragment = getFragment(false);
            fm.beginTransaction()
                    .add(R.id.worker_updater_fragment_container, cUFragment)
                    .commit();
        }
    }

    public void onClickSave(View view) {
        Intent data = new Intent();
        setResult(RESULT_CANCELED);
        if (profession != null) {
            String firstName = ((EditText) findViewById(R.id.e_first_name)).getText().toString();
            String lastName = ((EditText) findViewById(R.id.e_last_name)).getText().toString();
            String textTime = ((TextView) findViewById(R.id.e_birth_day)).getText().toString();
            long time = parseDateFromText(textTime);
            int id = 0;
            String name;
            if (worker != null) {
                id = worker.getId();
                name = worker.getLinkToPhoto();
                if (worker.getLinkToPhoto().isEmpty()) {
                    name = random.nextLong() + worker.getFirstName() + ".jpg";
                }
            } else {
                name = random.nextLong() + firstName + ".jpg";
            }
            if (containsPhoto) {
                saveLocalCopyImg(name);
            }
            String link = name;
            data.putExtra("worker", new Worker(id, firstName, lastName, time, link, profession));
            setResult(RESULT_OK, data);
        }
        finish();
    }

    public void onClickChange(View view) {
        Fragment uFragment = getFragment(true);
        fm.beginTransaction()
                .replace(R.id.worker_updater_fragment_container, uFragment)
                .addToBackStack(null)
                .commit();
        
    }

    public void onImgSelect(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Bitmap bitmap = null;
        ImageView photo = findViewById(R.id.img_photo_u);
        if (resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                containsPhoto = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            photo.setImageBitmap(bitmap);
        }
    }

    public void onClickCancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    private long parseDateFromText(String text) {
        String[] strings = text.split("\\.");
        long time = 0;
        if (strings.length > 2) {
            Calendar cal = Calendar.getInstance();
            cal.set(
                    Integer.parseInt(strings[2]),
                    Integer.parseInt(strings[1]) - 1,
                    Integer.parseInt(strings[0])
            );
            time = cal.getTime().getTime();
        }
        return time;
    }

    private Fragment getFragment(boolean shouldBeEdited) {
        Fragment fragment = new WorkerUpdaterFragment();
        if (worker != null && !shouldBeEdited){
            fragment = new WorkerViewerFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("profession", profession);
        bundle.putSerializable("worker", worker);
        if (worker != null && !worker.getLinkToPhoto().isEmpty()) {
            Bitmap bitmap = loadLocalCopyImg(worker.getLinkToPhoto());
            bundle.putParcelable("img", bitmap);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    private void saveLocalCopyImg(String name) {
        ImageView imageView = findViewById(R.id.img_photo_u);
        try {
            FileOutputStream bw = openFileOutput(name, MODE_PRIVATE);
            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bw);
            bw.flush();
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadLocalCopyImg(String name) {
        Bitmap bitmap = null;
        try {
            FileInputStream iStream = openFileInput(name);
            bitmap = BitmapFactory.decodeStream(iStream);
            iStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
