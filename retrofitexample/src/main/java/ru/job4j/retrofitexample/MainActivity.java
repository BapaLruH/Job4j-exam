package ru.job4j.retrofitexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import retrofit2.Response;
import ru.job4j.retrofitexample.adapter.PostAdapter;
import ru.job4j.retrofitexample.model.Post;
import ru.job4j.retrofitexample.service.CommonApiUtil;

public class MainActivity extends AppCompatActivity implements FragmentGetMethods.OnFragmentButtonClickListener, FragmentEditMethods.OnFragmentButtonClickListener, PostAdapter.OnClickPostListener {
    private static final String TAG = String.valueOf(MainActivity.class);
    static final String POST_EXTRAS = "Post";
    private CommonApiUtil apiUtil;
    private PostAdapter postAdapter;
    private RecyclerView rvPosts;
    private TextView statusText;
    private FragmentManager fManager;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fManager = getSupportFragmentManager();
        apiUtil = CommonApiUtil.getInstance();
        postAdapter = new PostAdapter(this, this);
        rvPosts = findViewById(R.id.rv_posts);
        rvPosts.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvPosts.setAdapter(postAdapter);
        statusText = findViewById(R.id.status_text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment mFragment = fManager.findFragmentById(R.id.fragments_container);
        if (item.getItemId() == R.id.get_requests) {
            if (fragment == null || !(mFragment instanceof FragmentGetMethods)) {
                fragment = FragmentGetMethods.getInstance();
            }
            addOrReplaceFragment(mFragment);
            return true;
        } else if (item.getItemId() == R.id.post_request) {
            if (fragment == null
                    || !(mFragment instanceof FragmentEditMethods)
                    || ((FragmentEditMethods) mFragment).needChangeFields(new Post())) {
                fragment = new FragmentEditMethods();
            }
            addOrReplaceFragment(mFragment);
        } else if (item.getItemId() == R.id.close) {
            if (mFragment != null) {
                removeFragment();
                if (fragment instanceof FragmentEditMethods) {
                    ((FragmentEditMethods) fragment).clearFields();
                }
            }
            postAdapter.removeAllPosts();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetPostsClicked() {
        getResponseHandling(apiUtil.getPosts());
    }

    @Override
    public void onGetPostByIdClicked(int id) {
        if (id > 0) {
            getResponseHandling(apiUtil.getPostById(id));
        }
    }

    @Override
    public void onGetPostByQueryClicked(int id) {
        if (id > 0) {
            getResponseHandling(apiUtil.getPostByQuery(id));
        }
    }

    @Override
    public void onGetPostByMapQueryClicked(Map<String, String> ids) {
        if (!ids.isEmpty()) {
            getResponseHandling(apiUtil.getPostByQueryMap(ids));
        }
    }

    @Override
    public void onSaveButtonClick(Post post) {
        if (post.getId() == null) {
            updResponseHandling(apiUtil.createPost(post), "added");
        } else {
            updResponseHandling(apiUtil.putPost(post), "updated");
        }
    }

    @Override
    public void onPatchButtonClick(Post post) {
        if (post.getId() != null) {
            updResponseHandling(apiUtil.patchPost(post), "updated");
        }
    }

    @Override
    public void onClickDelete(int id) {
        if (id > 0) {
            delResponseHandling(apiUtil.deletePost(id), id);
        }
    }

    @Override
    public void onClickPost(Post post) {
        Intent intent = new Intent(getApplicationContext(), CommentsActivity.class);
        intent.putExtra("id", post.getId());
        startActivity(intent);
    }

    @Override
    public void onClickEdit(Post post) {
        if (post != null) {
            Fragment mFragment = fManager.findFragmentById(R.id.fragments_container);
            if (fragment == null || !(mFragment instanceof FragmentEditMethods) || ((FragmentEditMethods) fragment).needChangeFields(post)) {
                fragment = new FragmentEditMethods();
                Bundle args = new Bundle();
                args.putSerializable(POST_EXTRAS, post);
                fragment.setArguments(args);
            }
            addOrReplaceFragment(mFragment);
        }
    }

    private void removeFragment() {
        fManager.beginTransaction().remove(fragment).commit();
    }

    private void addOrReplaceFragment(Fragment mFragment) {
        if (mFragment == null) {
            addFragment();
        } else {
            replaceFragment();
        }
    }

    private void replaceFragment() {
        fManager.beginTransaction().replace(R.id.fragments_container, fragment).commit();
    }

    private void addFragment() {
        fManager.beginTransaction().add(R.id.fragments_container, fragment).commit();
    }

    private void getResponseHandling(Single<List<Post>> single) {
        single.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<Post>>() {
                    @Override
                    public void onSuccess(List<Post> posts) {
                        postAdapter.setPosts(posts);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }
                });
    }

    private void updResponseHandling(Single<Response<Post>> single, String operation) {
        single.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Response<Post>>() {
                    @Override
                    public void onSuccess(Response<Post> response) {
                        removeFragment();
                        setStatusText(String.format("Status code: %s, post id: %s", response.code(), operation));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }
                });
    }

    private void delResponseHandling(Single<Response<Void>> single, int id) {
        single.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Response<Void>>() {
                               @Override
                               public void onSuccess(Response<Void> voidResponse) {
                                   setStatusText(String.format("Status code: %s, post id:%s deleted", voidResponse.code(), id));
                               }

                               @Override
                               public void onError(Throwable e) {
                                   Log.e(TAG, e.getLocalizedMessage());
                               }
                           }
                );
    }

    private void setStatusText(String text) {
        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(aLong -> aLong.intValue() == 5)
                .doOnSubscribe(disposable -> {
                    statusText.setVisibility(View.VISIBLE);
                    statusText.setText(text);
                })
                .doOnComplete(() -> {
                    statusText.setVisibility(View.GONE);
                    statusText.setText("");
                })
                .subscribe();

    }
}
