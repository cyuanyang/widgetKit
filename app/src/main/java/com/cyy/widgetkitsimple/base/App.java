package com.cyy.widgetkitsimple.base;

import android.app.Application;

import com.cyy.slidefinish.Slide;
import com.cyy.sound.WidgetKit;

/**
 * Created by study on 17/5/22.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        WidgetKit.getWidgetKit().init(this);

        Slide.Companion.getInstance().init(this);
    }
}
