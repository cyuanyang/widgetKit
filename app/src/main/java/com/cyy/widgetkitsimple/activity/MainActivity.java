package com.cyy.widgetkitsimple.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cyy.widgetkitsimple.R;
import com.cyy.widgetkitsimple.adapter.SimpleAdapter;
import com.cyy.widgetkitsimple.base.BaseActivity;
import com.cyy.widgetkitsimple.canvasviewsimple.CanvasViewSimpleActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    protected ListView listView;

    private SimpleAdapter adapter ;

    private List<Simple> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();
        initData();
        adapter = new SimpleAdapter(this ,datas );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void initData(){
        datas.add(new Simple("声音", null));
        datas.add(new Simple("PagerSlidingTabStrip" , null));
        datas.add(new Simple("canvas view simple" , CanvasViewSimpleActivity.class));
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Simple simple = (Simple) parent.getItemAtPosition(position);
        if (simple.clazz!=null){
            startActivity(new Intent(this , simple.clazz));
        }
    }

    public static class Simple{
        public Class clazz;
        public String name;

        public Simple(String name , Class clazz){
            this.clazz = clazz;
            this.name = name;
        }
    }

}
