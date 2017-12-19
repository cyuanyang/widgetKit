package com.cyy.widgetkitsimple.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cyy.widgetkitsimple.R;
import com.cyy.widgetkitsimple.adapter.SimpleAdapter;
import com.cyy.widgetkitsimple.base.BaseActivity;
import com.cyy.widgetkitsimple.simple.JavaMathSimple;
import com.cyy.widgetkitsimple.simple.pagerindicator.PagerIndicatorSimple;
import com.cyy.widgetkitsimple.simple.canvasviewsimple.CanvasViewSimpleActivity;
import com.cyy.widgetkitsimple.simple.mathview.MathViewSimple;
import com.cyy.widgetkitsimple.simple.slidefinish.SlideFinishSimple;
import com.cyy.widgetkitsimple.simple.soundsimple.SoundSimpleActivity;

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //初始化数据源
    private void initData(){
        datas.add(new Simple("声音", SoundSimpleActivity.class));
        datas.add(new Simple("PagerIndicatorSimple" , PagerIndicatorSimple.class));
        datas.add(new Simple("canvas view simple" , CanvasViewSimpleActivity.class));
        datas.add(new Simple("math view simple" , MathViewSimple.class));
        datas.add(new Simple("Slide Finish View Simple" , SlideFinishSimple.class));
        datas.add(new Simple("New Math View Simple" , JavaMathSimple.class));
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
