package com.cyy.widgetkit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;
/**
 * 使用范例：
 * public class NewAdapter extends CommonAdapter<Bean> {
 *      public NewAdapter(Context context, List<Bean> datas) {
 *          super(context, datas);
 *          setResource(R.layout.item);//设置布局资源
 *      }
 *
 *  @Override
 *  public void convert(CommonViewHolder holder, Bean t) {
 *       链式设置文本
 *      holder.setText(R.id.tv_name, t.tv1).setText(R.id.tv_num, t.tv2);
 * 	    ((TextView)holder.getView(R.id.tv_name)).setText(t.tv1);
 * 	    ((TextView)holder.getView(R.id.tv_num)).setText(t.tv2);
 * }
 */

/**
 * Created by cyy
 * Time：2015/9/14 22:53
 * Description：CommonBaseAdater抽象类,使用该类可以使adapter中的代码得到最大的精简
 *              编写adapter时，请尽量继续该类(CommonBaseAdapter)，而非直接继承BaseAdapter
 *              (特殊情况,过于简单的Adapter无需继承该类,例如无需使用Model类作为数据封装类)
 */

public abstract class CommonBaseAdapter<T> extends BaseAdapter {

    protected Context mContext;

    /**item资源文件**/
    private int resource;

    /**数据源**/
    protected List<T> mDatas;

    public CommonBaseAdapter(Context context, List<T> datas) {
        this.mContext = context;
        this.mDatas = datas;

    }

    /**返回总数**/
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取通用Holder
        CommonBaseViewHolder holder = CommonBaseViewHolder.getViewHolder(mContext, convertView, position, resource, parent);
        //获取数据Bean
        T bean =  mDatas.get(position);
        //设置给控件数据
        convert(holder, bean , position);
        return holder.getmConvertView();
    }

    /**
     * 更新数据
     */
    public void updateDatas(List<T> datas){
        mDatas=datas;
        notifyDataSetChanged();
    }

    /**
     * 子类实现该该方法实现绑定数据
     * @param holder
     * @param bean
     */
    public abstract void convert(CommonBaseViewHolder holder, T bean , int position);

    /**
     * 设置item资源文件的方法
     * @param resource the resource to set
     */
    public void setResource(int resource) {
        this.resource = resource;
    }

}
