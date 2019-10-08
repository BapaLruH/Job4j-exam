package ru.job4j.okhttpexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "dev_log";
    private TextView tvResult;
    private EditText eLogin;
    private EditText ePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = findViewById(R.id.tvResult);
        eLogin = findViewById(R.id.eLogin);
        ePassword = findViewById(R.id.ePassword);

        OkHttpClient client = new OkHttpClient();
        String url = "https://jsonplaceholder.typicode.com/posts/1";
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(LOG_TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String strResponse = response.body().string();
                    MainActivity.this.runOnUiThread(() -> tvResult.setText(strResponse));
                }
            }
        });
    }

    public void authentification(View view) {
        OkHttpClient client = getUnsafeOkHttpClient();
        String url = "https://job4j.ru/TrackStudio/LoginAction.do";
        String json = String.format("{method:login, login:%s, password:%s}", eLogin.getText(), ePassword.getText());
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(LOG_TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String loginResult = response.body().string();
                    MainActivity.this.runOnUiThread(() -> {
                        Intent intent = new Intent(MainActivity.this, RespActivity.class);
                        intent.putExtra("authResult", new String[]{response.message(), String.valueOf(response.code()), loginResult});
                        startActivity(intent);
                    });
                }
            }
        });
    }

    /**
     * Получаем клиент OkHttpClient, устойчивый к ошибкам SSL.
     * (Источник https://gist.github.com/chalup/8706740 - работает до версии OkHttp 3.1.1)
     * Метод работает для версии com.squareup.okhttp3:okhttp:3.9.1
     * (Версии okHttp выше 3.9.1 требуют Android API >= 21. В моём случае - API 19)
     */
    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain
                                , String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain
                                , String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[0];
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier((hostname, session) -> true)
                    .build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
