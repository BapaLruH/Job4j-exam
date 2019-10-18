package ru.job4j.retrofitexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import ru.job4j.retrofitexample.adapter.PostAdapter;
import ru.job4j.retrofitexample.model.Post;
import ru.job4j.retrofitexample.retrofit.RetrofitSingleton;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = String.valueOf(MainActivity.class);
    private RetrofitSingleton model;
    private PostAdapter postAdapter;
    private RecyclerView rvPosts;
    private EditText eID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        model = RetrofitSingleton.getInstance();
        eID = findViewById(R.id.et_id);
        PostAdapter.OnClickPostListener onClickPostListener = post -> {
            Intent intent = new Intent(getApplicationContext(), CommentsActivity.class);
            intent.putExtra("id", post.getId());
            startActivity(intent);
        };
        postAdapter = new PostAdapter(onClickPostListener);
        rvPosts = findViewById(R.id.rv_posts);
        rvPosts.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvPosts.setAdapter(postAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.get_posts) {
            getPosts(model.getPostsAPI().getPosts());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPosts(Single<List<Post>> single) {
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<Post>>() {
                    @Override
                    public void onSuccess(List<Post> posts) {
                        postAdapter.setPosts(posts);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }
                });
    }

    public void onButtonClick(View view) {
        String tId;
        int id;
        switch (view.getId()) {
            case R.id.get_post_by_id:
                tId = eID.getText().toString();
                id = tId.isEmpty() ? 0 : Integer.parseInt(tId);
                if (id > 0) {
                    model.getPostsAPI().getPostByID(id).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DisposableSingleObserver<Post>() {
                                @Override
                                public void onSuccess(Post post) {
                                    postAdapter.setPosts(Collections.singletonList(post));
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(TAG, e.getMessage());
                                }
                            });
                }
                break;
            case R.id.get_post_query:
                tId = eID.getText().toString();
                id = tId.isEmpty() ? 0 : Integer.parseInt(tId);
                if (id > 0) {
                    getPosts(model.getPostsAPI().getPost(id));
                }
                break;
            case R.id.get_post_map:
                tId = eID.getText().toString();
                id = tId.isEmpty() ? 0 : Integer.parseInt(tId);
                if (id > 0) {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("id", String.valueOf(id));
                    getPosts(model.getPostsAPI().getPost(parameters));
                }
                break;
        }

    }
}
