package com.cyy.widgetkit.adapter;

import android.content.Context;

import com.cyy.widgetkit.R;

import java.util.List;

/**
 * Created by chenyuanyang on 2017/5/22.
 */

public class SimpleAdapter extends CommonBaseAdapter<String> {


    public SimpleAdapter(Context context, List<String> datas) {
        super(context, datas);
        setResource(R.layout.item_simple);
    }

    @Override
    public void convert(CommonBaseViewHolder holder, String bean, int position) {
        holder.setText(R.id.textView , bean);
    }
}
