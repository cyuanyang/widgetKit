package com.cyy.widgetkit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyy.widgetkit.base.BaseActivity;


/**
 * Created by cyy
 * Time：2015/9/14 22:54
 * Description：通用ViewHolder类,通过该类可以极致简化Adapter中的代码量,因此
 *              在编写ViewHolder时请尽量使用该类,请尽量避免在Adapter中创建ViewHolder的内部类
 */

public class CommonBaseViewHolder {
    /**定义一个存放子view的容器**/
    private SparseArray<View> mViews;

    /**上下文**/
    private Context mContext;

    /**当前第几行位置**/
    private int mPosition;

    /**getView方法中的复用View**/
    private View mConvertView;

    /**
     * 构造方法
     * @param context 上下文
     * @param position 当前位置
     * @param resource item条目的资源文件
     * @param parent 父类
     */
    public CommonBaseViewHolder(Context context,int position,int resource,ViewGroup parent){
        this.mContext=context;
        this.mPosition=position;
        //创建存放View的容器
        mViews = new SparseArray<View>();
        //获取item的布局资源文件，并转化为View
        mConvertView= LayoutInflater.from(context).inflate(resource,parent,false);
        //给mConvertView设置Tag,以便复用Holder类
        mConvertView.setTag(this);
    }

    /**
     * 暴露给外界获取holder的方法
     * @param context context 上下文
     * @param mConvertView 复用View
     * @param position 位置
     * @param resource item资源文件
     * @param parent
     * @return
     */
    public static CommonBaseViewHolder getViewHolder(Context context,View mConvertView,int position,int resource,ViewGroup parent){
        if(null==mConvertView){
            return  new CommonBaseViewHolder(context,position,resource,parent);
        }else{
            //从复用view中通过Tag获取holder
            CommonBaseViewHolder holder = (CommonBaseViewHolder) mConvertView.getTag();
            holder.mPosition=position;//更新当前位置
            return holder;
        }
    }

    /**
     * 提供给外部获取item布局（类似Holder中存放复用view）中子view的方法
     * @param viewId view的Id
     * @param <T>
     * @return
     */
    public <T extends View>T getView(int viewId){
        //从容器中获取View
        View view = mViews.get(viewId);

        if(null==view){
            view=mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }

        return (T)view;
    }

    /**
     * 返回复用view的方法
     * @return the mConvertView
     */
    public View getmConvertView() {
        return mConvertView;
    }

    public int getmPosition(){
        return mPosition;
    }

    //==========为了让代码更加简洁以下可以封装一些把数据填充到view的方法,可以自行拓展其他方法==============

    /**
     * TextView设置文本的方法
     * @param viewId TextView对应的ID
     * @param text  文本
     * @return
     */
    public CommonBaseViewHolder setText(int viewId,CharSequence text){
        if (text==null){
            text = "";
        }
        TextView tv =getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * TextView设置文本颜色的方法
     * @param viewId TextView对应的ID
     * @param color  颜色
     * @return
     */
    public CommonBaseViewHolder setTextColor(int viewId,int color){
        if (color==0)return this;
        TextView tv =getView(viewId);
        tv.setTextColor(color);
        return this;
    }

    /**
     * ImageView设置图片Bitmap
     * @param viewId ImageView对应的ID
     * @param bm
     * @return
     */
    public CommonBaseViewHolder setImageView(int viewId,Bitmap bm){
        if (bm == null)return this;
        ImageView imageView=getView(viewId);
        imageView.setImageBitmap(bm);
        return this;
    }

    public CommonBaseViewHolder setImageView(int viewId,int resId){
        if (resId == 0)return this;
        ImageView imageView=getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }

    public CommonBaseViewHolder setImageViewWithUrl(int viewId , BaseActivity activity,
                                                    int defaultResId , String url,
                                                    int width , int height){
        if (url==null){
            ImageView imageView = getView(viewId);
            imageView.setImageResource(defaultResId);
            return this;
        }
        ImageView imageView = getView(viewId);
        ImageLoader.loadNoAnimate(activity , imageView , defaultResId , url , width , height);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public CommonBaseViewHolder setImageResource(int viewId, int drawableId)
    {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);

        return this;
    }
}
