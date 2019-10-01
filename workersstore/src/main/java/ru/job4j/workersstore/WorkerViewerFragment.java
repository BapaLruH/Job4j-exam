package ru.job4j.workersstore;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class WorkerViewerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker_viewer, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            Worker worker = (Worker) arguments.getSerializable("worker");
            Profession profession = (Profession) arguments.getSerializable("profession");
            Bitmap imgPhoto = arguments.getParcelable("img");
            if (worker != null && profession != null) {
                ((TextView) view.findViewById(R.id.tvWorkerNameV)).setText(worker.getFirstName());
                ((TextView) view.findViewById(R.id.tvWorkerLastNameV)).setText(worker.getLastName());
                ((TextView) view.findViewById(R.id.tvWorkerBirthdayV)).setText(parseTextFromDate(worker.getDateOfBirth()));
                ((TextView) view.findViewById(R.id.tvProfessionV)).setText(profession.getName());
                if (imgPhoto != null) {
                    ((ImageView) view.findViewById(R.id.img_photo_v)).setImageBitmap(imgPhoto);
                }
            }
        }
        return view;
    }

    private String parseTextFromDate(long time) {
        String txtTime = "";
        if (time > 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(time);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            txtTime = String.format(Locale.getDefault(), "%02d.%02d.%d", day, month, year);
        }
        return txtTime;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
