package ru.job4j.retrofitexample.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {
    private static RetrofitSingleton instance;
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private Retrofit mRetrofit;
    private PostsAPI postsAPI;
    private CommentsAPI commentsAPI;

    private RetrofitSingleton() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        postsAPI = mRetrofit.create(PostsAPI.class);
        commentsAPI = mRetrofit.create(CommentsAPI.class);
    }

    public static RetrofitSingleton getInstance() {
        if (instance == null) {
            instance = new RetrofitSingleton();
        }
        return instance;
    }

    public PostsAPI getPostsAPI() {
        return postsAPI;
    }

    public CommentsAPI getCommentsAPI() {
        return commentsAPI;
    }
}
