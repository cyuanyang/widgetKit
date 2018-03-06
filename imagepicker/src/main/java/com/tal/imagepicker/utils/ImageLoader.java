package com.tal.imagepicker.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.tal.imagepicker.Attach;
import com.tal.imagepicker.R;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by cyy on 2016/7/4.
 *
 *
 * 加载宽高有问题
 * 加载图片 单利模式
 *
 *
 * todo 图片加载的问题最好能有上层决定
 */
public class ImageLoader {

    private static ImageLoader instance;

    public enum Type{
        FIFO , LIFO
    }

    /**
     *  加载图片的线程
     */
    private HandlerThread mLoadImageThread;
    private Handler loadImageHandler;
    private Type mType = Type.LIFO;

    private Semaphore loadImageSemaphore;

    /**
     * 下载图片的线程池
     */
    private ExecutorService mExecutorService;
    /**
     * 任务数组
     */
    private LinkedList<Runnable> mTasks = new LinkedList<>();

    private ImageCache mCache;

    private ImageLoader(){
        init();
    }

    public static ImageLoader getInstance(){

        if (instance == null){
            synchronized (ImageLoader.class){
                if (instance == null){
                    instance = new ImageLoader();
                }
            }
        }

        return instance;
    }

    private void init(){
        mLoadImageThread = new HandlerThread("loadImage");
        mLoadImageThread.start();
        loadImageHandler = new Handler(mLoadImageThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1)
                mExecutorService.execute(getTask());
                try {
                    loadImageSemaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        mExecutorService = Executors.newFixedThreadPool(Attach.ThreadNumber);
        mCache = ImageCache.getInstance();
        loadImageSemaphore = new Semaphore(Attach.ThreadNumber);
        this.mType = Attach.Type;
    }

    private void addTask(Runnable runnable){
        mTasks.add(runnable);
    }

    private @NonNull
    Runnable getTask(){
        if (mType == Type.FIFO){
            return mTasks.removeFirst();
        }else if (mType == Type.LIFO){
            return mTasks.removeLast();
        }
        else {
            return  null;
        }
    }

    /**
     * UIHandler
     */
    private Handler UIHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 999){
                ImageBean bean = (ImageBean) msg.obj;
                if (bean.bitmap == null){
                    bean.imageView.setImageResource(R.mipmap.ic_error);
                }else
                    bean.imageView.setImageBitmap(bean.bitmap);
            }
        }
    };
    /**
     * 加载图片
     * 先从缓存中找 没有则去加载 默认压缩 300*300
     * @param uriStr 本地的图片的路径
     * 加载宽高有问题
     */
    public void loadImage(final String uriStr , final ImageView imageView ){
        this.loadImage(uriStr , false ,imageView , 250 , 250);
    }

    /**
     * 大图不加入缓存当中
     */
    public void loadImage(final String uriStr , final boolean big , final ImageView imageView , final int reqWidth , final int reqHeight ){
        imageView.setTag(R.id.yyloadimage_tag , uriStr);
        //设置占位图
//        imageView.setImageResource(R.mipmap.pic_default);
        if (mCache.get(uriStr)!= null && !big){
            imageView.setImageBitmap(mCache.get(uriStr));
        }else {
            addTask(new Runnable() {
                @Override
                public void run() {
                    //根据url设置tag 确定唯一需要显示的是哪一个view
                    Object tag = imageView.getTag(R.id.yyloadimage_tag);
                    if (tag!=null && uriStr.equals(tag.toString())){
                        final Bitmap bitmap = ImageUtils.decodeSampleImageFromUri(uriStr ,reqWidth , reqHeight );
                        if (!big){
                            mCache.put(uriStr , bitmap);
                        }
                        ImageBean imageBean = new ImageBean();
                        imageBean.imageView = imageView ;
                        imageBean.bitmap = bitmap;
                        Message message = Message.obtain();
                        message.what = 999;
                        message.obj = imageBean;
                        UIHandler.sendMessage(message);
                    }
                    //释放一个信号量
                    loadImageSemaphore.release();
                }
            });

            loadImageHandler.sendEmptyMessage(1);
        }
    }


    class ImageBean{
        public ImageView imageView;
        public Bitmap bitmap;

    }
}
