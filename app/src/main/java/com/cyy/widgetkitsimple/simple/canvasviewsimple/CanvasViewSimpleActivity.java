package com.cyy.widgetkitsimple.simple.canvasviewsimple;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cyy.canvasview.CanvasView;
import com.cyy.canvasview.ImageLoadDelegate;
import com.cyy.widgetkitsimple.R;
import com.cyy.widgetkitsimple.base.BaseActivity;

public class CanvasViewSimpleActivity extends BaseActivity implements View.OnClickListener {

    protected CanvasView canvasView;
    protected Button penView;
    protected Button eraserView;
    protected Button rotateLeftBtn;
    protected Button rotateRightBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_canvas_view_simple);
        initView();
        getSupportActionBar().setTitle("CanvasViewSimpleActivity");

        String url = "http://attach.bbs.miui.com/forum/201705/24/152612pyffi5ii5qirfmrh.jpg";

        loadWithGlide(url);
    }

    private void loadWithGlide(String url) {
        canvasView.setmLoadImage(new ImageLoadDelegate.LoadImage() {
            @Override
            public void loadImage(Context context, String url, final ImageLoadDelegate.LoadImageCallback callback) {
                Glide.with(context).asBitmap()
                        .load(url)
                        .into(new SimpleTarget<Bitmap>() {

                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                callback.imageCallback(resource);
                            }
                        });
            }
        }, url);
    }

    private void initView() {
        canvasView = (CanvasView) findViewById(R.id.canvasView);
        penView = (Button) findViewById(R.id.penView);
        penView.setOnClickListener(CanvasViewSimpleActivity.this);
        eraserView = (Button) findViewById(R.id.eraserView);
        eraserView.setOnClickListener(CanvasViewSimpleActivity.this);
        rotateLeftBtn = (Button) findViewById(R.id.rotateLeftBtn);
        rotateLeftBtn.setOnClickListener(CanvasViewSimpleActivity.this);
        rotateRightBtn = (Button) findViewById(R.id.rotateRightBtn);
        rotateRightBtn.setOnClickListener(CanvasViewSimpleActivity.this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.penView) {
            canvasView.pen();
        } else if (view.getId() == R.id.eraserView) {
            canvasView.eraser();
        } else if (view.getId() == R.id.rotateLeftBtn) {
            canvasView.rotate(false);
        } else if (view.getId() == R.id.rotateRightBtn) {
            canvasView.rotate(true);
        }
    }
}
