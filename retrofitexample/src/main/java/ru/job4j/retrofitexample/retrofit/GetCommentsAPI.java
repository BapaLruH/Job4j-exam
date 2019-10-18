package ru.job4j.retrofitexample.retrofit;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Url;
import ru.job4j.retrofitexample.model.Comment;

public interface GetCommentsAPI {
    @GET
    Single<List<Comment>> getComments(@Url String url);
}
