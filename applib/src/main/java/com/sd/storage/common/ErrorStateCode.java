package com.sd.storage.common;

/**
 * Created by MrZhou on 2016/10/14.
 */
public interface ErrorStateCode {
    // 服务器错误 失效
    int SERVER_ERROR = 500;

    // 密码错误
    int PASSWORD_ERROR = 400;
    // 用户名错误
    int USER_NAME_ERROR = 702;
    //正确
    int RESULTOK = 200;
    //第三方登录的时候 用户未注册
    int USERNOTRIGISTER = 700;

    // 数据为空
    int DATA_NULL_ERROR = 240;

    // 数据为空
    int OLD_DATA_NULL_ERROR = 400;

    // tokenId 失效
    int TOKEN_ID_LOSE = 300;

    // 网络错误
    int NETWORK_ERROR = 606;
    // 用户电话没有绑定
    int PHONE_NO_BIND_ERROR = 700;
}
