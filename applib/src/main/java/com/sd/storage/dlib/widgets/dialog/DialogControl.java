package com.sd.storage.dlib.widgets.dialog;

/** 
 * dialog控制接口
* @author wangzhiqiang
* @date 2015-2-24 下午8:58:29 
*  
*/
public interface DialogControl {

	/** 
	* 隐藏dialog
	*/ 
	public abstract void hideWaitDialog();

	/** 
	* 显示正在加载的dialog
	* @return
	*/ 
	public abstract void showWaitDialog();

	/** 
	* 显示文字的dialog
	* @param resid
	* @return
	*/ 
	public abstract void showWaitDialog(int resid);

	/** 
	* 显示dialog
	* @param text
	* @return
	*/ 
	public abstract void showWaitDialog(String text);
	
}
