package com.cyy.mathview_mathjax;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.x5.template.Chunk;
import com.x5.template.Theme;
import com.x5.template.providers.AndroidTemplates;

public class MathView extends WebView {

    public final static int STATUS_END = 0 ;
    public final static int STATUS_BEGIN = 1 ;

    private String mText;
    private String mConfig;

    private MathRenderInterface mathRenderInterface;

    public MathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getSettings().setJavaScriptEnabled(true);
        addJavascriptInterface(new BaseJSInterface(this), "androidJS");
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        setBackgroundColor(Color.TRANSPARENT);

        TypedArray mTypeArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MathView,
                0, 0
        );

        try {
            setText(mTypeArray.getString(R.styleable.MathView_text));
        } finally {
            mTypeArray.recycle();
        }
    }

    private Chunk getChunk() {
        String TEMPLATE_MATHJAX = "mathjax";
        AndroidTemplates loader = new AndroidTemplates(getContext());
        return new Theme(loader).makeChunk(TEMPLATE_MATHJAX);
    }

    public void setText(String text) {
        mText = text;
        Chunk chunk = getChunk();
        String TAG_FORMULA = "formula";
        String TAG_CONFIG = "config";
        chunk.set(TAG_FORMULA, mText);
        chunk.set(TAG_CONFIG, mConfig);
        this.loadDataWithBaseURL(null, chunk.toString(), "text/html", "utf-8", "about:blank");
    }

    /**
     * 渲染结束
     */
    protected void renderStatus(int status){
        if (status == STATUS_END){
            if (mathRenderInterface!=null)mathRenderInterface.endRender(this);
        }else if (status == STATUS_BEGIN){
            if (mathRenderInterface!=null)mathRenderInterface.beginRender(this);
        }
    }

    /**
     * 设置回调函数
     */
    public void setMathRenderInterface(MathRenderInterface mathRenderInterface) {
        this.mathRenderInterface = mathRenderInterface;
    }

    public String getText() {
        return mText;
    }

    /**
     * Tweak the configuration of MathJax.
     * The `config` string is a call statement for MathJax.Hub.Config().
     * For example, to enable auto line breaking, you can call:
     * config.("MathJax.Hub.Config({
     *      CommonHTML: { linebreaks: { automatic: true } },
     *      "HTML-CSS": { linebreaks: { automatic: true } },
     *      SVG: { linebreaks: { automatic: true } }
     *  });");
     *
     * This method should be call BEFORE setText() and AFTER setEngine().
     * PLEASE PAY ATTENTION THAT THIS METHOD IS FOR MATHJAX ONLY.
     * @param config
     */
    public void config(String config) {
        this.mConfig = config;
    }

    public static class BaseJSInterface{

        private MathView mathView;
        private MyRunnable runnable = new MyRunnable();

        public BaseJSInterface(MathView mathView){
            this.mathView = mathView;
        }

        @JavascriptInterface
        public void mathCallback(final int status){
            runnable.status = status;
            mathView.post(runnable);
        }

        class MyRunnable implements Runnable {
            public int status = -1;
            @Override
            public void run() {
                if (mathView!=null)
                    mathView.renderStatus(status);
            }
        }

        public void destory(){
            mathView.removeCallbacks(runnable);
            runnable = null;
            mathView = null;
        }
    }
}