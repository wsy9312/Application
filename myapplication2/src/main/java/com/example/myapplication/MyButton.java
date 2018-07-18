package com.example.myapplication;

/**
 * Created by HGTXxgl on 2018/7/9.
 */

public class MyButton {
    private MyOnClickListener listener;

    /**
     * 设置具体点击监听器
     * @param listener 点击监听器实现类
     */
    public void setOnClickListener(MyOnClickListener listener) {
        this.listener = listener;
    }

    /**
     * 按钮被点击
     */
    public void doClick() {
        listener.onClick();
    }

}
