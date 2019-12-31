package ru.job4j.retrofitexample.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.job4j.retrofitexample.R;
import ru.job4j.retrofitexample.db.models.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private List<Comment> comments;

    public CommentAdapter() {
        this.comments = new ArrayList<>();
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.info_comment, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder commentHolder, int i) {
        final Comment comment = this.comments.get(i);
        commentHolder.tvCommentId.setText(String.format("id: %s", comment.get_id()));
        commentHolder.tvCommentName.setText(comment.getName());
        commentHolder.tvCommentEmail.setText(comment.getEmail());
        commentHolder.tvCommentText.setText(comment.getText());
    }

    @Override
    public int getItemCount() {
        return this.comments.size();
    }

    public static class CommentHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tvCommentId;
        TextView tvCommentName;
        TextView tvCommentEmail;
        TextView tvCommentText;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tvCommentId = view.findViewById(R.id.tv_comment_id);
            tvCommentName = view.findViewById(R.id.tv_comment_name);
            tvCommentEmail = view.findViewById(R.id.tv_comment_email);
            tvCommentText = view.findViewById(R.id.tv_comment_text);
        }
    }
}
