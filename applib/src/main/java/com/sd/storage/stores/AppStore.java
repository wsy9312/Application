package com.sd.storage.stores;


import com.google.gson.Gson;
import com.sd.storage.actions.AppAction;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.dlib.utils.PreferencePlugin;


import javax.inject.Inject;


public class AppStore extends Store<AppAction> {


    private PreferencePlugin mPrefernce;

  //  private UserModel userModel;
    private Gson gson = new Gson();

    @Inject
    public AppStore(PreferencePlugin prefernce) {
        mPrefernce = prefernce;
    }


    @Override
    public void doAction(AppAction action) {

    }


    /**
     * 保存用户的信息
     *
     * @param userModel
     */
  /*  public void setUserModle(UserModel userModel) {
        if (null != userModel) {
            this.userModel = userModel;
            String jsonStr = gson.toJson(userModel); //将对象转换成Json
            mPrefernce.commitString(LoginAction.USERMODEL, jsonStr);
        }
    }*/

    /**
     * 获得用户信息
     *
     * @return
     */
  /*  public UserModel getUserModle() {
        String jsonStr = mPrefernce.getString(LoginAction.USERMODEL, null);
        if (null != jsonStr) {
            userModel = gson.fromJson(jsonStr, UserModel.class); //将json字符串转换成 people对象
        }
        return userModel;
    }*/




}
