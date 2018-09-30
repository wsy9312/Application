package com.sd.storage.modules;



import android.util.Log;

import com.sd.storage.BuildConfig;
import com.sd.storage.UrlManager;
import com.sd.storage.api.ApiService;
import com.sd.storage.common.SDGsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;


@Module
public class ApiServiceModule {

    final String HTTP_ROOT= UrlManager.HTTP_ROOT;

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60 * 1000, TimeUnit.MILLISECONDS);
        builder.readTimeout(60 * 1000, TimeUnit.MILLISECONDS);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                if(BuildConfig.DEBUG){
                    Log.i("URL", "intercept: "+original.url());
                }
                return chain.proceed(original);
            }
        });
        return builder.build();
    }

    @Provides
    @Singleton
    Retrofit.Builder provideRestAdapter(OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(okHttpClient);
        builder.addConverterFactory(SDGsonConverterFactory.create());
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        return builder;
    }

    @Provides
    @Singleton
    ApiService provideApiService(Retrofit.Builder builder) {
        Retrofit retrofit = builder.baseUrl(HTTP_ROOT).build();
        return retrofit.create(ApiService.class);
    }

}
