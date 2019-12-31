package ru.job4j.retrofitexample.service;

import android.content.Context;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import ru.job4j.retrofitexample.db.models.Comment;
import ru.job4j.retrofitexample.db.models.Post;
import ru.job4j.retrofitexample.retrofit.Api;
import ru.job4j.retrofitexample.retrofit.RetrofitSingleton;

public class CommonApiUtil {
    private static CommonApiUtil instance;
    private Api api;

    private CommonApiUtil(Context context) {
        api = RetrofitSingleton.getInstance(context).getApi();
    }

    public static CommonApiUtil getInstance(Context context) {
        CommonApiUtil localInstance = instance;
        if (localInstance == null) {
            synchronized (CommonApiUtil.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new CommonApiUtil(context);
                }
            }
        }
        return localInstance;
    }

    private Single<Response<List<Post>>> getPosts(Single<Response<List<Post>>>  single) {
        return single.subscribeOn(Schedulers.io());
    }

    public Single<Response<List<Post>>>  getPosts() {
        return getPosts(api.getPosts());
    }

    public Single<Response<Post>>  getPostById(int id) {
        return api.getPostByID(id).subscribeOn(Schedulers.io());
    }

    public Single<Response<List<Post>>>  getPostByQuery(int id) {
        return getPosts(api.getPost(id));
    }

    public Single<Response<List<Post>>>  getPostByQueryMap(Map<String, String> ids) {
        return getPosts(api.getPost(ids));
    }

    public Single<Response<Post>> createPost(Post newPost) {
        return api.createPost(newPost).subscribeOn(Schedulers.io());
    }

    public Single<Response<Post>> putPost(Post updPost) {
        return api.putPost(updPost.get_id(), updPost).subscribeOn(Schedulers.io());
    }

    public Single<Response<Post>> patchPost(Post updPost) {
        return api.patchPost(updPost.get_id(), updPost).subscribeOn(Schedulers.io());
    }

    public Single<Response<Void>> deletePost(int id) {
        return api.deletePost(id).subscribeOn(Schedulers.io());
    }

    public Single<Response<List<Comment>>> getCommentsById(int id) {
        return api.getComments(String.format("comments?postId=%s", id)).subscribeOn(Schedulers.io());
    }
}
