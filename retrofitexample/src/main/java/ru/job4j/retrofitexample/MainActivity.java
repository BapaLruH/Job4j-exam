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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import ru.job4j.retrofitexample.adapter.PostAdapter;
import ru.job4j.retrofitexample.db.AppContentResolverWrapper;
import ru.job4j.retrofitexample.db.DBManager;
import ru.job4j.retrofitexample.db.dao.PostDao;
import ru.job4j.retrofitexample.db.models.Post;
import ru.job4j.retrofitexample.service.CommonApiUtil;

public class MainActivity extends AppCompatActivity implements FragmentGetMethods.OnFragmentButtonClickListener, FragmentEditMethods.OnFragmentButtonClickListener, PostAdapter.OnClickPostListener {
    private static final String TAG = String.valueOf(MainActivity.class);
    static final String POST_EXTRAS = "Post";
    private CommonApiUtil apiUtil;
    private PostDao postDao;
    private PostAdapter postAdapter;
    private RecyclerView rvPosts;
    private FragmentManager fManager;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fManager = getSupportFragmentManager();
        apiUtil = CommonApiUtil.getInstance(this);
        postAdapter = new PostAdapter(this, this);
        rvPosts = findViewById(R.id.rv_posts);
        rvPosts.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvPosts.setAdapter(postAdapter);
        DBManager.init(new AppContentResolverWrapper(getApplicationContext()));
        postDao = DBManager.getInstance().getPostDao();
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
        responseHandler(apiUtil.getPosts(), null, null);
    }

    @Override
    public void onGetPostByIdClicked(int id) {
        if (id > 0) {
            storageHandler(apiUtil.getPostById(id), id);
        }
    }

    @Override
    public void onGetPostByQueryClicked(int id) {
        if (id > 0) {
            storageHandler(apiUtil.getPostByQuery(id), id);
        }
    }

    @Override
    public void onGetPostByMapQueryClicked(Map<String, String> ids) {
        if (!ids.isEmpty()) {
            storageHandler(apiUtil.getPostByQueryMap(ids), Integer.parseInt(ids.get("id")));
        }
    }

    @Override
    public void onSaveButtonClick(Post post) {
        if (post.get_id() == null) {
            responseHandler(apiUtil.createPost(post), "added", null);
        } else {
            responseHandler(apiUtil.putPost(post), "updated", null);
        }
    }

    @Override
    public void onPatchButtonClick(Post post) {
        if (post.get_id() != null) {
            responseHandler(apiUtil.patchPost(post), "patched", null);
        }
    }

    @Override
    public void onClickDelete(int id) {
        if (id > 0) {
            responseHandler(apiUtil.deletePost(id), "deleted", id);
        }
    }

    private void responseHandler(Single<? extends Response> single, String operation, Integer id) {

        single.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Response>() {
                    @Override
                    public void onSuccess(Response response) {
                        String rsl = "";
                        if (response.isSuccessful()) {
                            Object object = response.body();
                            if (object instanceof List) {
                                List<Post> posts = (List<Post>) object;
                                postDao.updateList(posts).subscribeOn(Schedulers.io()).subscribe();
                                postAdapter.setPosts(posts);
                            } else if (object instanceof Post) {
                                Post post = (Post) object;
                                if (operation != null) {
                                    postDao.updatePost(post).subscribeOn(Schedulers.io()).subscribe();
                                    postAdapter.updateItems(Collections.singletonList(post));
                                }
                                rsl = getStringByOperation(operation, post.get_id());
                            } else {
                                postDao.deletePost(String.valueOf(id)).subscribeOn(Schedulers.io()).subscribe();
                                postAdapter.removeItem(id);
                                rsl = getStringByOperation(operation, id);
                            }
                        }
                        setStatusText(rsl);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }
                });
    }

    private void storageHandler(Single<? extends Response> single, int id) {
        Single<List<Post>> postDb = postDao.getPostById(String.valueOf(id)).subscribeOn(Schedulers.io());
        Single<List<Post>> postNetwork = single.flatMap(postResponse -> {
            List<Post> postList = new ArrayList<>();
            if (postResponse.isSuccessful()) {
                Object object = postResponse.body();
                if (object instanceof Post) {
                    Post post = (Post) object;
                    postList.add(post);
                    postDao.createPost(post).subscribeOn(Schedulers.io()).subscribe();
                } else if (object instanceof List) {
                    postList = (List<Post>) object;
                    postDao.updateList(postList).subscribeOn(Schedulers.io()).subscribe();
                }
            }
            return Single.just(postList);
        });
        Single.concat(postDb, postNetwork)
                .filter(list -> !list.isEmpty())
                .first(Collections.emptyList())
                .observeOn(AndroidSchedulers.mainThread())
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

    private String getStringByOperation(String operation, Integer id) {
        String rsl = null;
        if (operation != null && id != null && id > 0) {
            rsl = String.format("Post id:%s %s", id, operation);
        }
        return rsl;
    }

    private void setStatusText(String text) {
        if (text != null && !text.isEmpty()) {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickPost(Post post) {
        Intent intent = new Intent(getApplicationContext(), CommentsActivity.class);
        intent.putExtra("id", post.get_id());
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
}
