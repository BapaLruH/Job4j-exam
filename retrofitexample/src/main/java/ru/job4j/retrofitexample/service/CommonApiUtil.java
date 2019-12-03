package ru.job4j.retrofitexample.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import ru.job4j.retrofitexample.model.Comment;
import ru.job4j.retrofitexample.model.Post;
import ru.job4j.retrofitexample.retrofit.CommentsAPI;
import ru.job4j.retrofitexample.retrofit.PostsAPI;
import ru.job4j.retrofitexample.retrofit.RetrofitSingleton;

public class CommonApiUtil {
    private static CommonApiUtil instance;
    private PostsAPI postApi;
    private CommentsAPI commentsAPI;

    private CommonApiUtil() {
        postApi = RetrofitSingleton.getInstance().getPostsAPI();
        commentsAPI = RetrofitSingleton.getInstance().getCommentsAPI();
    }

    public static CommonApiUtil getInstance() {
        CommonApiUtil localInstance = instance;
        if (localInstance == null) {
            synchronized (CommonApiUtil.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new CommonApiUtil();
                }
            }
        }
        return localInstance;
    }

    private Single<List<Post>> getPosts(Single<List<Post>> single) {
        return single.subscribeOn(Schedulers.io());
    }

    public Single<List<Post>> getPosts() {
        return getPosts(postApi.getPosts());
    }

    public Single<List<Post>> getPostById(int id) {
        return postApi.getPostByID(id).subscribeOn(Schedulers.io()).flatMap(post -> Single.just(Collections.singletonList(post)));
    }

    public Single<List<Post>> getPostByQuery(int id) {
        return getPosts(postApi.getPost(id));
    }

    public Single<List<Post>> getPostByQueryMap(Map<String, String> ids) {
        return getPosts(postApi.getPost(ids));
    }

    public Single<Response<Post>> createPost(Post newPost) {
        return postApi.createPost(newPost).subscribeOn(Schedulers.io());
    }

    public Single<Response<Post>> putPost(Post updPost) {
        return postApi.putPost(updPost.getId(), updPost).subscribeOn(Schedulers.io());
    }

    public Single<Response<Post>> patchPost(Post updPost) {
        return postApi.patchPost(updPost.getId(), updPost).subscribeOn(Schedulers.io());
    }

    public Single<Response<Void>> deletePost(int id) {
        return postApi.deletePost(id).subscribeOn(Schedulers.io());
    }

    public Single<List<Comment>> getCommentsById(int id) {
        return commentsAPI.getComments(String.format("comments?postId=%s", id)).subscribeOn(Schedulers.io());
    }
}
