package com.cyy.widgetkitsimple.simple.canvasviewsimple;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cyy.canvasview.ImageLoadDelegate;
import com.cyy.canvasview.LayerCanvas;
import com.cyy.canvasview.layers.StampLayer;
import com.cyy.widgetkitsimple.R;
import com.cyy.widgetkitsimple.base.BaseActivity;

public class CanvasViewSimpleActivity extends BaseActivity implements View.OnClickListener {

    protected LayerCanvas layerCanvas;
    protected Button penView;
    protected Button eraserView;
    protected Button rotateLeftBtn;
    protected Button rotateRightBtn;
    protected Button preBtn;
    protected Button nextBtn;
    protected Button stickerBtn;
    protected Button mergeBtn;

    private StampLayer stampLayer;

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
        layerCanvas.getCanvasView().setmLoadImage(new ImageLoadDelegate.LoadImage() {
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
        layerCanvas = (LayerCanvas) findViewById(R.id.layerCanvas);
        penView = (Button) findViewById(R.id.penView);
        penView.setOnClickListener(CanvasViewSimpleActivity.this);
        eraserView = (Button) findViewById(R.id.eraserView);
        eraserView.setOnClickListener(CanvasViewSimpleActivity.this);
        rotateLeftBtn = (Button) findViewById(R.id.rotateLeftBtn);
        rotateLeftBtn.setOnClickListener(CanvasViewSimpleActivity.this);
        rotateRightBtn = (Button) findViewById(R.id.rotateRightBtn);
        rotateRightBtn.setOnClickListener(CanvasViewSimpleActivity.this);
        preBtn = (Button) findViewById(R.id.preBtn);
        preBtn.setOnClickListener(CanvasViewSimpleActivity.this);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(CanvasViewSimpleActivity.this);
        stickerBtn = (Button) findViewById(R.id.stickerBtn);
        stickerBtn.setOnClickListener(CanvasViewSimpleActivity.this);
        mergeBtn = (Button) findViewById(R.id.mergeBtn);
        mergeBtn.setOnClickListener(CanvasViewSimpleActivity.this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.penView) {
            layerCanvas.getCanvasView().pen();
        } else if (view.getId() == R.id.eraserView) {
            layerCanvas.getCanvasView().eraser();
        } else if (view.getId() == R.id.rotateLeftBtn) {
            layerCanvas.getCanvasView().rotate(false);
        } else if (view.getId() == R.id.rotateRightBtn) {
            layerCanvas.getCanvasView().rotate(true);
        } else if (view.getId() == R.id.preBtn) {
            layerCanvas.getCanvasView().pre();
        } else if (view.getId() == R.id.nextBtn) {

        } else if (view.getId() == R.id.stickerBtn) {
            if (stampLayer == null) {
                stampLayer = new StampLayer(this);
            }
            if (!stampLayer.isBegin()) {
                stampLayer.begin(layerCanvas);
            }
            stampLayer.addStamp(BitmapFactory.decodeResource(getResources(), R.drawable.ico_sticker_smiling), true);
        } else if (view.getId() == R.id.mergeBtn) {
            stampLayer.end(layerCanvas);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("ViewPager");
        menu.add("设置");
        return super.onCreateOptionsMenu(menu);

    }
}
