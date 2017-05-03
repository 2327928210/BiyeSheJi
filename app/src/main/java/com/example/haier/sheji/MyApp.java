package com.example.haier.sheji;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * Created by haier on 2017/4/8.
 */

public class MyApp extends Application {

    public static MyApp instance;
    private static MyApp app;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private Context context;
    private com.nostra13.universalimageloader.core.ImageLoader universalimagerloader;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        context = this;
        initVolley();
        initUniversalImageLoader();

        instance = this;

        AutoLayoutConifg.getInstance().useDeviceSize();

    }


    /**
     * 获取请求堆栈
     *
     * @return
     */
    public RequestQueue getRequestQueue()
    {
        if (requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return requestQueue;
    }

    public static MyApp getApp() {
        return instance;
    }



    public Context getContext() {
        return context;
    }




    private void initVolley() {
        requestQueue = Volley.newRequestQueue(this);

        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {

            LruCache<String,Bitmap> lru = new LruCache<String, Bitmap>(10<<20){
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes()*value.getHeight();
                }
            };
            @Override
            public Bitmap getBitmap(String url) {

                return lru.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

                lru.put(url,bitmap);
            }
        });
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
    private void initUniversalImageLoader() {

        universalimagerloader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();

        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)                //内存缓存
                .cacheOnDisk(true)                  //磁盘缓存
                .bitmapConfig(Bitmap.Config.RGB_565)//解码方式
                .resetViewBeforeLoading(true)       //加载前是否重置,为了放置图片错位
                .showImageOnLoading(R.mipmap.ic_launcher) //默认图片
                .showImageOnFail(R.mipmap.ic_launcher)    //失败图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)  //url地址为空的时候显示的图片
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT) //缩放类型
                .displayer(new FadeInBitmapDisplayer(10 * 1000))//显示前的处理【CircleBitmapDisplayer,FadeInBitmapDisplayer,RoundedBitmapDisplayer】
                .build();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(Runtime.getRuntime().availableProcessors()) //线程池大小
                .memoryCache(new LruMemoryCache(10 * 1024 * 1024))
                .defaultDisplayImageOptions(displayImageOptions)
                .writeDebugLogs()//是否打印日志信息
                .build();
        universalimagerloader.init(configuration);
    }

    public com.nostra13.universalimageloader.core.ImageLoader getUniversalimagerloader() {
        return universalimagerloader;
    }


}
