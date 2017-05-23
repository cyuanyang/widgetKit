package com.cyy.widgetkit;

import android.app.Application;

/**
 * Created by study on 17/5/23.
 */

public class WidgetKit {

    private static WidgetKit widgetKit;
    private Application app;
    private WidgetKit(){

    }

    public static WidgetKit getWidgetKit(){
        if (widgetKit == null){
            synchronized (WidgetKit.class){
                if (widgetKit==null){
                    widgetKit = new WidgetKit();
                }
            }
        }
        return widgetKit;
    }

    public void init(Application application){
        app = application;
    }

    public Application getApp() {
        return app;
    }
}
