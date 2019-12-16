package ru.job4j.retrofitexample.retrofit;

import android.content.Context;
import android.content.Intent;

import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.job4j.retrofitexample.BuildConfig;
import ru.job4j.retrofitexample.ErrorActivity;

public class RetrofitSingleton {
    private static RetrofitSingleton instance;
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private Retrofit mRetrofit;
    private PostsAPI postsAPI;
    private CommentsAPI commentsAPI;

    private RetrofitSingleton(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new OkHttpProfilerInterceptor());
        } else {
            builder.addInterceptor(chain -> {
                Request request = chain.request();
                Response response = chain.proceed(request);
                if (response.code() >= 400 && response.code() <= 599) {
                    Intent intent = new Intent(context, ErrorActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("code", response.code());
                    context.startActivity(intent);
                    return response;
                }
                return response;
            });
        }
        OkHttpClient client = builder.build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        postsAPI = mRetrofit.create(PostsAPI.class);
        commentsAPI = mRetrofit.create(CommentsAPI.class);
    }

    public static RetrofitSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitSingleton(context);
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
