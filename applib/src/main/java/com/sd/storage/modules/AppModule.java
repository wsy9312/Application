package com.sd.storage.modules;

import android.app.Application;
import android.content.res.Resources;


import com.sd.storage.dlib.dispatcher.Dispatcher;
import com.sd.storage.dlib.utils.PreferencePlugin;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by  on 2016/9/26.
 */

@Singleton
@Module(includes={ApiServiceModule.class})
public class AppModule {

    private Application application;

    public AppModule(Application application){
        this.application = application;
    }

    @Provides
    @Singleton
    public Application provideApplication(){
        return application;
    }

    @Provides
    @Singleton
    Resources provideResources() {
        return application.getResources();
    }

    @Provides
    Dispatcher provideDispatcher(){
        return new Dispatcher();
    }

    @Provides
    @Singleton
    PreferencePlugin providePreferencePlugin(){
        return PreferencePlugin.getInstance(application);
    }

   /* @Provides
    @Singleton
    TencentLocationRequest provideTencentLocationRequest(Application application){
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_POI);
        request.setAllowCache(true);
        request.setInterval(100000);
        request.setAllowDirection(true);

        return request;
    }*/

}
