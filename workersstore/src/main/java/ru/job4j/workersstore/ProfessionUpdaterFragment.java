package ru.job4j.workersstore;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class ProfessionUpdaterFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profession_updater, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            Profession profession = (Profession) arguments.getSerializable("profession");
            if (profession != null) {
                ((TextView) view.findViewById(R.id.tv_prof_label)).setText(profession.getName());
                ((EditText) view.findViewById(R.id.e_prof_name)).setText(profession.getName());
                ((EditText) view.findViewById(R.id.e_prof_specialty_code)).setText(String.valueOf(profession.getSpecialtyCode()));
            }
        }
        return view;
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
