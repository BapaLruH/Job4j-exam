package ru.job4j.retrofitexample.retrofit;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import ru.job4j.retrofitexample.model.Post;

public interface GetPostsAPI {
    @GET("posts")
    Single<List<Post>> getPosts();

    @GET("posts/{id}")
    Single<Post> getPostByID(@Path("id") int postId);

    @GET("posts")
    Single<List<Post>> getPost(@Query("id") int postId);

    @GET("posts")
    Single<List<Post>> getPost(@QueryMap Map<String, String> parameters);
}
