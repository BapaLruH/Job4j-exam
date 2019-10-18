package ru.job4j.retrofitexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import ru.job4j.retrofitexample.adapter.CommentAdapter;
import ru.job4j.retrofitexample.model.Comment;
import ru.job4j.retrofitexample.retrofit.RetrofitSingleton;

public class CommentsActivity extends AppCompatActivity {
    private static final String TAG = CommentsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        int id = getIntent().getIntExtra("id", 0);
        if (id > 0) {
            TextView tvTitle = findViewById(R.id.tv_comment_page_title);
            tvTitle.setText(String.format(getString(R.string.label_title_comments_by_post), String.valueOf(id)));
            CommentAdapter commentAdapter = new CommentAdapter();
            RecyclerView rvComments = findViewById(R.id.rv_comments);
            rvComments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rvComments.setAdapter(commentAdapter);
            RetrofitSingleton
                    .getInstance()
                    .getCommentsAPI()
                    .getComments(String.format("comments?postId=%s", id))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<List<Comment>>() {
                        @Override
                        public void onSuccess(List<Comment> comments) {
                            commentAdapter.setComments(comments);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, e.getMessage());
                        }
                    });
        }

    }
}
