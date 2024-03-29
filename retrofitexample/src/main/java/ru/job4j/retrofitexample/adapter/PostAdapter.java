package ru.job4j.retrofitexample.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.job4j.retrofitexample.R;
import ru.job4j.retrofitexample.db.models.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    private List<Post> posts;
    private OnClickPostListener listener;
    private Context mCtx;

    public PostAdapter(Context mCtx, OnClickPostListener listener) {
        posts = new ArrayList<>();
        this.listener = listener;
        this.mCtx = mCtx;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    public void removeAllPosts() {
        posts = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void updateItems(List<Post> posts) {
        for (Post post : posts) {
            updatePost(post);
        }
    }

    public void updatePost(Post post) {
        int position = getPositionByID(post.get_id());
        if (position !=  -1) {
            posts.set(position, post);
            notifyItemChanged(position, post);
        }
    }

    public void removeItem(int id) {
        int position = getPositionByID(id);
        if (position !=  -1) {
            posts.remove(position);
            notifyItemRemoved(position);
        }
    }

    private int getPositionByID(int id) {
        int position = -1;
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).get_id().equals(id)) {
                position = i;
                break;
            }
        }
        return position;
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
        holder.tvComments.setOnClickListener(v -> listener.onClickPost(post));
        holder.tvUserId.setText(String.format("User id: %s", post.getUser_id()));
        holder.tvPostId.setText(String.format("id: %s", post.get_id()));
        holder.tvPostText.setText(post.getText());
        holder.tvPostTitle.setText(post.getTitle());
        holder.tvBtnOptions.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(mCtx, holder.tvBtnOptions);
            popup.inflate(R.menu.popup_menu);
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.p_edit:
                        listener.onClickEdit(post);
                        break;
                    case R.id.p_delete:
                        listener.onClickDelete(post.get_id());
                        break;
                }
                return false;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tvUserId;
        TextView tvPostId;
        TextView tvPostText;
        TextView tvPostTitle;
        TextView tvComments;
        TextView tvBtnOptions;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tvUserId = view.findViewById(R.id.tv_post_user_id);
            tvPostId = view.findViewById(R.id.tv_post_id);
            tvPostText = view.findViewById(R.id.tv_post_text);
            tvPostTitle = view.findViewById(R.id.tv_post_title);
            tvComments = view.findViewById(R.id.tv_comments);
            tvBtnOptions = view.findViewById(R.id.tv_options);
        }
    }

    public interface OnClickPostListener {
        void onClickPost(Post post);

        void onClickEdit(Post post);

        void onClickDelete(int id);
    }
}
