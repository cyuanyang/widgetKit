package com.tal.imagepicker.ui.pop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.tal.imagepicker.LoadImageDelegate;
import com.tal.imagepicker.NormalLoadImage;
import com.tal.imagepicker.R;
import com.tal.imagepicker.model.DirPopModel;

import java.util.List;

/**
 * Created by cyy on 2016/7/7.
 *
 *
 */
public class DirPopAdapter extends BaseAdapter {
    private Context context;
    private List<DirPopModel> dirItems;

    private LoadImageDelegate delegate;

    public DirPopAdapter(Context context , List<DirPopModel> dirItems){
        this.context = context;
        this.dirItems = dirItems;
        delegate = new NormalLoadImage();
    }
    @Override
    public int getCount() {
        return dirItems.size();
    }

    @Override
    public Object getItem(int position) {
        return dirItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        DirPopModel model = dirItems.get(position);
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.dir_pop_item_layout , null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.dirNameView.setText(model.getDirName());
        viewHolder.imageCount.setText("("+model.getImageCount()+")");
        delegate.loadImage(viewHolder.dirPopImageView ,model.getImagePath());
        viewHolder.setBadge(model.getSelectCount());
        return convertView;
    }

    static

    class ViewHolder {
        protected ImageView dirPopImageView;
        protected TextView dirNameView;
        protected TextView imageCount;
        protected TextView badgeView;

        ViewHolder(View rootView) {
            initView(rootView);
        }

        private void initView(View rootView) {
            dirPopImageView = (ImageView) rootView.findViewById(R.id.dir_pop_imageView);
            dirNameView = (TextView) rootView.findViewById(R.id.dir_nameView);
            imageCount = (TextView) rootView.findViewById(R.id.image_count);
            badgeView = (TextView) rootView.findViewById(R.id.badge_view);
        }

        public void setBadge(String numStr){
            if (numStr== null ||"0".equals(numStr)){
                badgeView.setVisibility(View.GONE);
            }else {
                badgeView.setText(numStr);
                badgeView.setVisibility(View.VISIBLE);
            }
        }
    }
}
