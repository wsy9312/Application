package com.sd.storage.common;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * 网络延迟重试，作用于retryWhen
 * Created by MrZhou on 2016/10/29.
 */
public class RetryWhenProcess implements Func1<Observable<? extends Throwable>, Observable<?>>{

    // 延迟的秒数
    private long mInterval;


    /**
     * 次方秒后执行
     * @param interval
     */
    public RetryWhenProcess(long interval) {
        mInterval = interval;
    }


    @Override
    public Observable<?> call(final Observable<? extends Throwable> observable) {

        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                // 网络不可用
                if (throwable instanceof UnknownHostException)
                    return Observable.error(throwable);

                return Observable.just(throwable).zipWith(Observable.range(1, 3), new Func2<Throwable, Integer, Integer>() {
                    @Override
                    public Integer call(Throwable throwable, Integer i) {
                        return i;
                    }
                }).flatMap(new Func1<Integer, Observable<?>>() {
                    @Override
                    public Observable<?> call(Integer retryCount) {
                        //重试三次，并且每一次的重试时间都是5 ^ retryCount，仅仅通过一些操作符的组合就帮助我们实现了指数退避算法
                        return Observable.timer((long)Math.pow(mInterval, retryCount), TimeUnit.SECONDS);
                    }
                });
            }
        });
    }
}
