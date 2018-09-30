package com.sd.storage.common.glide;


/**
 * Created by MrZhou on 2017/3/4.
 */
public class GlideOptionUtil {

    /**
     * 删除缓存
     */
    public static void clearGlide(){
        GlideCatchUtil.getInstance().cleanCatchDisk();
        GlideCatchUtil.getInstance().clearCacheMemory();
    }


}
