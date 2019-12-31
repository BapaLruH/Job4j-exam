package ru.job4j.retrofitexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import ru.job4j.retrofitexample.adapter.CommentAdapter;
import ru.job4j.retrofitexample.db.DBManager;
import ru.job4j.retrofitexample.db.dao.CommentDao;
import ru.job4j.retrofitexample.db.models.Comment;
import ru.job4j.retrofitexample.service.CommonApiUtil;

public class CommentsActivity extends AppCompatActivity {
    private CommentAdapter commentAdapter;
    private static final String TAG = CommentsActivity.class.getSimpleName();
    private CommentDao commentDao;
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        postId = getIntent().getIntExtra("id", 0);
        if (postId > 0) {
            commentDao = DBManager.getInstance().getCommentDao();
            init(postId);
            fillCommentsHandler(postId);
        }
    }

    private void init(int id) {
        TextView tvTitle = findViewById(R.id.tv_comment_page_title);
        tvTitle.setText(String.format(getString(R.string.label_title_comments_by_post), String.valueOf(id)));
        commentAdapter = new CommentAdapter();
        RecyclerView rvComments = findViewById(R.id.rv_comments);
        rvComments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvComments.setAdapter(commentAdapter);
    }

    private void fillComments(int id) {
        CommonApiUtil.getInstance(this).getCommentsById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Response<List<Comment>>>() {
                    @Override
                    public void onSuccess(Response<List<Comment>> response) {
                        if (response.isSuccessful()) {
                            commentAdapter.setComments(response.body());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }
                });
    }

    private void fillCommentsHandler(int id) {
        Single<List<Comment>> commentsDb = commentDao.getCommentsByPostId(String.valueOf(id)).subscribeOn(Schedulers.io());
        Single<List<Comment>> commentsNetwork = CommonApiUtil.getInstance(this)
                .getCommentsById(id)
                .flatMap(listResponse -> {
                    List<Comment> comments = Collections.emptyList();
                    if (listResponse.isSuccessful()) {
                        comments = listResponse.body();
                        commentDao.insertList(comments).subscribeOn(Schedulers.io()).subscribe();
                    }
                    return Single.just(comments);
                });
        Single.concat(commentsDb, commentsNetwork)
                .filter(v -> !v.isEmpty())
                .first(Collections.emptyList())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<Comment>>() {
                    @Override
                    public void onSuccess(List<Comment> comments) {
                        commentAdapter.setComments(comments);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }
                });
    }
}
