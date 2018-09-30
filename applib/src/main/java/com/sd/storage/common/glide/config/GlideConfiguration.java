package com.sd.storage.common.glide.config;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by MrZhou on 2017/3/7.
 */
public class GlideConfiguration implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // 自定义缓存目录
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, GlideCatchConfig.GLIDE_CARCH_DIR, GlideCatchConfig.GLIDE_CATCH_SIZE));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
