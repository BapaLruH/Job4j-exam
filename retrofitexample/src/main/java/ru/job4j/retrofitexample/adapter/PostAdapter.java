package ru.job4j.retrofitexample.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.job4j.retrofitexample.R;
import ru.job4j.retrofitexample.model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    private List<Post> posts;
    private OnClickPostListener listener;

    public PostAdapter(OnClickPostListener listener) {
        posts = new ArrayList<>();
        this.listener = listener;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    public void removeAllPosts() {
        posts = new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.info_post, parent, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int i) {
        final Post post = this.posts.get(i);
        TextView tvUserId = holder.view.findViewById(R.id.tv_post_user_id);
        TextView tvPostId = holder.view.findViewById(R.id.tv_post_id);
        TextView tvPostText = holder.view.findViewById(R.id.tv_post_text);
        TextView tvPostTitle = holder.view.findViewById(R.id.tv_post_title);
        TextView tvComments = holder.view.findViewById(R.id.tv_comments);
        tvComments.setOnClickListener(v -> listener.onClickPost(post));

        tvUserId.setText(String.format("User id: %s", post.getUserId()));
        tvPostId.setText(String.format("id: %s", post.getId()));
        tvPostText.setText(post.getText());
        tvPostTitle.setText(post.getTitle());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostHolder extends RecyclerView.ViewHolder {
        private View view;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }
    }

    public interface OnClickPostListener {
        void onClickPost(Post post);
    }
}
