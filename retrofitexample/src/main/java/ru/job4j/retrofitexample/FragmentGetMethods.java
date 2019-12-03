package ru.job4j.retrofitexample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class FragmentGetMethods extends Fragment {
    private static FragmentGetMethods instance =  new FragmentGetMethods();
    private OnFragmentButtonClickListener mListener;

    public static FragmentGetMethods getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_methods, container, false);
        EditText etId = view.findViewById(R.id.e_id);
        view.findViewById(R.id.get_post_by_id).setOnClickListener((v) -> {
            String textId = etId.getText().toString();
            mListener.onGetPostByIdClicked(textId.isEmpty() ? 0 : Integer.parseInt(textId));
        });
        view.findViewById(R.id.get_post_query).setOnClickListener((v) -> {
            String textId = etId.getText().toString();
            mListener.onGetPostByQueryClicked(textId.isEmpty() ? 0 : Integer.parseInt(textId));
        });
        view.findViewById(R.id.get_post_map).setOnClickListener((v) -> {
            String textId = etId.getText().toString();
            Map<String, String> ids = new HashMap<>();
            ids.put("id", textId.isEmpty() ? "0" : textId);
            mListener.onGetPostByMapQueryClicked(ids);
        });
        Button btnGetAll = view.findViewById(R.id.btn_get_all);
        btnGetAll.setOnClickListener((v) -> mListener.onGetPostsClicked());
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnFragmentButtonClickListener)) {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mListener = (OnFragmentButtonClickListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentButtonClickListener {
        void onGetPostsClicked();

        void onGetPostByIdClicked(int id);

        void onGetPostByQueryClicked(int id);

        void onGetPostByMapQueryClicked(Map<String, String> ids);
    }
}
