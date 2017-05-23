package com.cyy.widgetkitsimple.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.cyy.widgetkitsimple.R;
import com.cyy.widgetkitsimple.adapter.SimpleAdapter;
import com.cyy.widgetkitsimple.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    protected ListView listView;

    private SimpleAdapter adapter ;

    private List<String> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();


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
