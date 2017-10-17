package com.mygdx.game.android;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimplePixmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

/**
 * Created by Administrator on 2016/12/1.
 */

public class TTApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initImageCache(this);
        //初始化okhttp
//        OkHttpClientManager.initClient(this);
    }


    private void initImageCache(Context context) {

        /** 磁盘缓存大小 */
        int mDiskCacheSize = 1024 * 1024 * 50; // 50M
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCache(getMemoryCache(context))
                .diskCache(getDiskCache(context))//本地缓存配置
                .denyCacheImageMultipleSizesInMemory()
                .discCacheSize(mDiskCacheSize)
                .threadPoolSize(5)//图片线程池大小
                .threadPriority(Thread.NORM_PRIORITY - 2)//线程级别 3
                .discCacheFileCount(60) //缓存的文件数量
//                .denyCacheImageMultipleSizesInMemory()//一个URL对应一个图片
                .imageDownloader(new BaseImageDownloader(context))
                .tasksProcessingOrder(QueueProcessingType.FIFO)//任务队列执行顺序 后进先出
                .writeDebugLogs()
                .defaultDisplayImageOptions(createDefDisplayOptions())
                .build();

        ImageLoader.getInstance().init(config);

    }

    /**
     * 作为ImageLoader的默认配置使用
     */
    private static DisplayImageOptions createDefDisplayOptions() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(false)
                .displayer(new SimplePixmapDisplayer())
                .build();
    }


    /**
     * 内存缓存配置
     */
    public static LruMemoryCache getMemoryCache(Context context) {
        int cacheSize = 4 * 1024 * 1024;
        try {
            int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();

            int availableSize = memClass >> 3;
            cacheSize = 1024 * 1024 * (availableSize == 0 ? 4 : availableSize / 2);
        } catch (Exception exception) {
        }


        //test
        System.out.println("memoryCache size = "+cacheSize);

        LruMemoryCache memoryCache = new LruMemoryCache(cacheSize);
        return memoryCache;
    }

    /**
     * 磁盘缓存
     * 命名：MD5加密
     * 最大大小：50兆
     *
     * @param context
     */
    public static DiskCache getDiskCache(Context context) {
        File cacheFile;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cacheFile = context.getExternalCacheDir();
            if (cacheFile == null) {
                cacheFile = new File(Environment.getExternalStorageDirectory().getPath() + "/test/img/");
            }
        } else {
            cacheFile = context.getCacheDir();
        }

        /**
         * note:UnlimitedDiscCache is pretty faster than other limited disk cache implementations
         */
        DiskCache diskCache = new UnlimitedDiskCache(cacheFile, null, new Md5FileNameGenerator());
        return diskCache;
    }

}

