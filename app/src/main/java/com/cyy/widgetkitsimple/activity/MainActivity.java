package com.cyy.widgetkitsimple.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
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




        adapter = new SimpleAdapter(this ,datas );
        listView.setAdapter(adapter);
    }

    private void initData(){
        datas.add("声音");
        datas.add("PagerSlidingTabStrip");
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listView);
    }
}
