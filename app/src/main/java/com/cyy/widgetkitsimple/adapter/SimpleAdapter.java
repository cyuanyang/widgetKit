package com.cyy.widgetkitsimple.adapter;

import android.content.Context;


import com.cyy.widgetkitsimple.R;
import com.cyy.widgetkitsimple.activity.MainActivity;

import java.util.List;

/**
 * Created by chenyuanyang on 2017/5/22.
 */

public class SimpleAdapter extends CommonBaseAdapter<MainActivity.Simple> {


    public SimpleAdapter(Context context, List<MainActivity.Simple> datas) {
        super(context, datas);
        setResource(R.layout.item_simple);
    }

    @Override
    public void convert(CommonBaseViewHolder holder, MainActivity.Simple bean, int position) {
        holder.setText(R.id.textView , bean.name);
    }
}
