package ru.job4j.workersstore;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class WorkerUpdaterFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker_updater, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            Worker worker = (Worker) arguments.getSerializable("worker");
            Profession profession = (Profession) arguments.getSerializable("profession");
            Bitmap imgPhoto = arguments.getParcelable("img");
            TextView tvBirthday = view.findViewById(R.id.e_birth_day);
            EditText firstName = view.findViewById(R.id.e_first_name);
            EditText lastName = view.findViewById(R.id.e_last_name);
            TextView tvProfession = view.findViewById(R.id.tv_w_c_profession);
            ImageView photo = view.findViewById(R.id.img_photo_u);
            tvBirthday.setOnClickListener(this::onSelectDate);
            if (worker != null) {
                firstName.setText(worker.getFirstName());
                lastName.setText(worker.getLastName());
                tvBirthday.setText(parseTextFromDate(worker.getDateOfBirth()));
            }
            if (imgPhoto != null) {
                photo.setImageBitmap(imgPhoto);
            }
            if (profession != null) {
                tvProfession.setText(profession.getName());
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

    private void onSelectDate(View view) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        TextView tvView = (TextView) view;
        DatePickerDialog picker = new DatePickerDialog(tvView.getContext(), (dPView, year1, month1, dayOfMonth) -> tvView.setText(
                String.format(Locale.getDefault(), "%02d.%02d.%d", dayOfMonth, month1 + 1, year1)), year, month, day);
        picker.show();
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
