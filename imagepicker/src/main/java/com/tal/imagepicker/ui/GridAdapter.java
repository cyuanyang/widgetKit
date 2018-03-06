package com.tal.imagepicker.ui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.tal.imagepicker.LoadImageDelegate;
import com.tal.imagepicker.PickerImage;
import com.tal.imagepicker.R;
import com.tal.imagepicker.model.ImageItem;
import com.tal.imagepicker.ui.views.ToggleView;

import java.util.List;

/**
 * Created by cyy on 2016/7/4.
 *
 */
public class GridAdapter extends BaseAdapter {

    private Context context ;
    private List<ImageItem> imageItems;
    private LoadImageDelegate delegate;
    private int model;

    private ColorDrawable colorDrawable ;

    public GridAdapter(Context context , List<ImageItem> imagesItem , LoadImageDelegate delegate,
                       int model){
        this.context = context;
        this.imageItems = imagesItem;
        this.delegate = delegate;
        this.model = model;

        colorDrawable = new ColorDrawable(context.getResources().getColor(R.color.grid_bg_color));
    }

    @Override
    public int getCount() {
        return imageItems.size();
    }

    @Override
    public Object getItem(int position) {
        return imageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageItem imageItem = imageItems.get(position);
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.image_item_layout , null);
            holder = new ViewHolder(convertView , model);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.toggleView.select(imageItem.isSelect());
        holder.toggleView.setOnSelectedChangedListener(new ToggleView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged(View v, boolean select) {
                if (pickDelegate!=null) pickDelegate.onPickedChanged((ToggleView) v,imageItem , position ,  select);
            }
        });

        holder.imageView.setImageDrawable(colorDrawable);
        delegate.loadImage(holder.imageView ,imageItem.getImagePath() );

        return convertView;
    }

    class ViewHolder{
        public ImageView imageView;

        public FrameLayout multipleLayout;
        public ToggleView toggleView;
        public View coverView;

        public ViewHolder(View convertView , int model){
            coverView = convertView.findViewById(R.id.coverView);
            imageView = (ImageView) convertView.findViewById(R.id.image);
            toggleView = (ToggleView) convertView.findViewById(R.id.pick_image_btn);

            multipleLayout = (FrameLayout) convertView.findViewById(R.id.multiple_layout);
            if (model != PickerImage.PICK_MODE_MULTIPLE){
                multipleLayout.setVisibility(View.GONE);
            }
        }
    }

    /**  设置监听回调*/
    private OnSelectedChagedDelegate pickDelegate;
    public void setPickDelegate(OnSelectedChagedDelegate d){
        this.pickDelegate = d;
    }

    public interface OnSelectedChagedDelegate{
        void onPickedChanged(ToggleView toggleView, ImageItem imageItem, int position, boolean select);
    }
}
