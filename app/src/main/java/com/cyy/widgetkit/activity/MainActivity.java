package com.cyy.widgetkit.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.cyy.widgetkit.R;
import com.cyy.widgetkit.adapter.SimpleAdapter;
import com.cyy.widgetkit.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    protected ListView listView;

    private SimpleAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();

        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add("d" + i);
        }

        adapter = new SimpleAdapter(this ,datas );
        listView.setAdapter(adapter);
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listView);
    }
}
