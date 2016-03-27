package lei.buaa.leidrib.utils;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import lei.buaa.leidrib.LDApplication;
import lei.buaa.leidrib.config.KeyConfig;
import lei.buaa.leidrib.config.URLConfig;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpEngine;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lei on 3/19/16.
 * email: lileibh@gmail.com
 */
public class RetrofitUtils {
    private static Retrofit mRetrofit = null;
    private static Retrofit mOauthRetrofit = null;

    public static Retrofit getRetrofit() {
        if (mRetrofit == null) {
            Interceptor interceptor = new MyInterceptor();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(URLConfig.BASE_URL)
                    .client(new OkHttpClient.Builder().addInterceptor(interceptor).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    public static Retrofit getOauthRetrofit() {
        if (mOauthRetrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            mOauthRetrofit = new Retrofit.Builder()
                    .baseUrl(URLConfig.BASE_OAUTH_URL)
                    .client(new OkHttpClient.Builder().addInterceptor(interceptor).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mOauthRetrofit;
    }

    static class MyInterceptor implements Interceptor {
        private final Charset UTF8 = Charset.forName("UTF-8");

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (LDApplication.getUser() != null) {
                request = request.newBuilder().
                        addHeader(KeyConfig.AUTHORIZATION, "Bearer " + LDApplication.getUser().getAccess_token())
                        .build();
            }

            RequestBody requestBody = request.body();
            boolean hasRequestBody = requestBody != null;

            String requestStartMessage =
                    "--> " + request.method() + ' ' + request.url();
            Log.d("Retrofit_info", requestStartMessage);
            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                Log.d("Retrofit_info", name + ": " + headers.value(i));
            }

            if (!hasRequestBody) {
                Log.d("Retrofit_info", "--> END " + request.method());
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Log.d("Retrofit_info", "");
                Log.d("Retrofit_info", buffer.readString(UTF8));

                Log.d("Retrofit_info", "--> END " + request.method()
                        + " (" + requestBody.contentLength() + "-byte body)");
            }

            long startNs = System.nanoTime();
            Response response = chain.proceed(request);
            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

            ResponseBody responseBody = response.body();
            long contentLength = responseBody.contentLength();
            String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
            Log.d("Retrofit_info", "<-- " + response.code() + ' ' + response.message() + ' '
                    + response.request().url() + " (" + tookMs + "ms" + ", "
                    + bodySize + " body" + ')');

            headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                Log.d("Retrofit_info", headers.name(i) + ": " + headers.value(i));
            }

            if (!HttpEngine.hasBody(response)) {
                Log.d("Retrofit_info", "<-- END HTTP");
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                if (contentLength != 0) {
                    Log.d("Retrofit_info", "");
                    Log.d("Retrofit_info", buffer.clone().readString(UTF8));
                }

                Log.d("Retrofit_info", "<-- END HTTP (" + buffer.size() + "-byte body)");
            }


            return response;
        }
    }
}
