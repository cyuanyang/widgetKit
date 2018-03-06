package com.tal.imagepicker.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tal.imagepicker.LoadImageDelegate;
import com.tal.imagepicker.NormalLoadImage;
import com.tal.imagepicker.R;
import com.tal.imagepicker.utils.ScreenUtils;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by cyy on 2016/7/6.
 *
 *
 */
public class SinglePreviewImageFragment extends Fragment {

    private String url;

    private LoadImageDelegate delegate;

    private ImageEventListener imageEventListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString(PreviewImageFragment.PREVIEW_IMAGE_URL);

        delegate = new NormalLoadImage();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT ,
                ViewGroup.LayoutParams.MATCH_PARENT);
        PhotoView imageView = new PhotoView(getContext());
        imageView.setLayoutParams(lp);
        imageView.setId(R.id.preview_image);
//        imageView.setImageResource(R.mipmap.pic_default);
        return imageView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final PhotoView imageView = (PhotoView) view.findViewById(R.id.preview_image);
        ScreenUtils.Size size = ScreenUtils.getSceenSize(this.getContext());
        delegate.loadImageBigger(imageView , url , size.width ,size.height );

        imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                if (imageEventListener!=null)imageEventListener.onViewTap(view);
            }
        });
    }

    public void setImageEventListener(ImageEventListener imageEventListener) {
        this.imageEventListener = imageEventListener;
    }

    interface ImageEventListener{
        void onViewTap(View view);
    }
}
