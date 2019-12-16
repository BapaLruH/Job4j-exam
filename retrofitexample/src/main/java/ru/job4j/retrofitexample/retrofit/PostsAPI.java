package ru.job4j.retrofitexample.retrofit;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import ru.job4j.retrofitexample.model.Post;

public interface PostsAPI {
    @GET("posts")
    Single<Response<List<Post>>> getPosts();

    @GET("posts/{id}")
    Single<Response<Post>> getPostByID(@Path("id") int postId);

    @GET("posts")
    Single<Response<List<Post>>> getPost(@Query("id") int postId);

    @GET("posts")
    Single<Response<List<Post>>> getPost(@QueryMap Map<String, String> parameters);

    @POST("posts")
    Single<Response<Post>> createPost(@Body Post post);

    @FormUrlEncoded
    @POST("posts")
    Single<Response<Post>> createPost(@Field("userId") int userId, @Field("title") String title, @Field("body") String text);

    @FormUrlEncoded
    @POST("posts")
    Single<Response<Post>> createPost(@FieldMap Map<String, String> fields);

    @PUT("posts/{id}")
    Single<Response<Post>> putPost(@Path("id") int id, @Body Post post);

    @DELETE("posts/{id}")
    Single<Response<Void>> deletePost(@Path("id") int id);

    @PATCH("posts/{id}")
    Single<Response<Post>> patchPost(@Path("id") int id, @Body Post post);
}
