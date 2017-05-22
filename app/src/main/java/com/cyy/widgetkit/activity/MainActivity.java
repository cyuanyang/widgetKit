package com.cyy.widgetkit.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.cyy.widgetkit.R;
import com.cyy.widgetkit.base.BaseActivity;

public class MainActivity extends BaseActivity {

    protected ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listView);
    }
}
