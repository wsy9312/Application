package com.sd.storage.dlib.utils;

import android.content.Intent;
import android.net.Uri;

public class IntentUtils {
	/**
	 * 打开指定QQ信息
	 * @param qqNum
	 * @return
	 */
	public static Intent getOpenQQIntent(String qqNum){
		String url="mqqwpa://im/chat?chat_type=wpa&uin="+qqNum;
		return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	}
	
	/**
	 * 拨打电话
	 * @param tel
	 * @return
	 */
	public static Intent getCallPhoneIntent(String tel){
		if(StringUtils.isBlank(tel)){
			return null;
		}
		return new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+tel));
	}
}
