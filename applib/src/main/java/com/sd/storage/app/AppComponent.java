package com.sd.storage.app;

import android.app.Application;

import com.sd.storage.modules.AppModule;
import com.sd.storage.ui.MenuMainActivity;
import com.sd.storage.ui.main.addcommend.AddCommentActivity;
import com.sd.storage.ui.main.meunmanage.MeunManageActivity;
import com.sd.storage.ui.main.meunmanage.addnewvege.AddNewVegeActivity;
import com.sd.storage.ui.main.meunmanage.addweekmeun.AddWeekMeunActivity;
import com.sd.storage.ui.main.meunmanage.manageedit.MansageEditActivity;
import com.sd.storage.ui.main.meunmanage.managesearch.ManageSearchActivity;
import com.sd.storage.ui.main.meunorder.MeunOrderActivity;
import com.sd.storage.ui.main.search.SearchVageActivity;
import com.sd.storage.ui.main.settime.SetTimeActivity;
import com.sd.storage.ui.main.vagedetails.VageDetailsActivity;
import com.sd.storage.ui.main.votemanage.VoteManageActivity;
import com.sd.storage.ui.main.weekmeun.WeekMenuActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by MrZhou on 2017/5/13.
 */
@Singleton
@Component(
        modules = {AppModule.class}
)
public interface AppComponent {

    Application getApplication();

    final class Initializer {
        public static AppComponent init(StorageApplication app) {
            return DaggerAppComponent.builder().appModule(new AppModule(app)).build();
        }
    }

    void inject(StorageApplication app);



    void inject(WeekMenuActivity weekMenuActivity);

    void inject(AddCommentActivity addCommentActivity);

    void inject(VageDetailsActivity vageDetailsActivity);


    void inject(MeunOrderActivity meunOrderActivity);

    void inject(SearchVageActivity searchVageActivity);

    void inject(MeunManageActivity meunManageActivity);

    void inject(ManageSearchActivity manageSearchActivity);

    void inject(AddNewVegeActivity addNewVegeActivity);


    void inject(MansageEditActivity mansageEditActivity);

    void inject(VoteManageActivity voteManageActivity);

    void inject(AddWeekMeunActivity addWeekMeunActivity);

    void inject(SetTimeActivity setTimeActivity);

    void inject(MenuMainActivity mainActivity);


}
