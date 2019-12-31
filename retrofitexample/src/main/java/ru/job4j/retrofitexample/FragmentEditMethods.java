package ru.job4j.retrofitexample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ru.job4j.retrofitexample.db.models.Post;

public class FragmentEditMethods extends Fragment {

    private OnFragmentButtonClickListener mListener;
    EditText id;
    EditText userId;
    EditText title;
    EditText text;

    public FragmentEditMethods() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_methods, container, false);
        id = view.findViewById(R.id.e_id);
        userId = view.findViewById(R.id.e_user_id);
        title = view.findViewById(R.id.e_title);
        text = view.findViewById(R.id.e_text);
        Bundle args = getArguments();
        if (args != null) {
            Post post = (Post) args.getSerializable(MainActivity.POST_EXTRAS);
            if (post != null) {
                id.setText(String.valueOf(post.get_id()));
                userId.setText(String.valueOf(post.getUser_id()));
                title.setText(post.getTitle());
                text.setText(post.getText());
            }
        }
        view.findViewById(R.id.btn_save).setOnClickListener(v -> mListener.onSaveButtonClick(
                establishPostParameters()
        ));
        view.findViewById(R.id.btn_patch).setOnClickListener(v -> mListener.onPatchButtonClick(
                establishPostParameters()
        ));
        return view;
    }

    public void clearFields() {
        id.setText("");
        userId.setText("");
        title.setText("");
        text.setText("");
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

    private Post establishPostParameters() {
        String textId = id.getText().toString();
        String textUserId = userId.getText().toString();
        String textTitle = title.getText().toString();
        String textBody = text.getText().toString();
        return new Post(
                getIntegerOrNull(textId),
                getIntegerOrNull(textUserId),
                getStringOrNull(textTitle),
                getStringOrNull(textBody)
        );
    }

    private Integer getIntegerOrNull(String txtId) {
        return !txtId.isEmpty() ? Integer.parseInt(txtId) : null;
    }

    private String getStringOrNull(String text) {
        return !text.isEmpty() ? text : null;
    }

    public boolean needChangeFields(Post post) {
        return !post.equals(establishPostParameters());
    }

    public interface OnFragmentButtonClickListener {
        void onSaveButtonClick(Post post);

        void onPatchButtonClick(Post post);
    }
}
