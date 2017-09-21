package com.cyy.widgetkitsimple.simple.mathview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.cyy.mathview_mathjax.MathRenderInterface;
import com.cyy.mathview_mathjax.MathView;
import com.cyy.widgetkitsimple.R;

public class MathViewSimple extends AppCompatActivity {

    protected MathView mathView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_math_view_simple);
        initView();

        String test = "\\( \\begin{split}{S_n} &= {a_1} + {a_2} + {a_3} + \\cdot \\cdot \\cdot +{a_n} \\\\&= \\left( {1 - \\dfrac{1}{2}} \\right) + \\left( {\\dfrac{1}{2} - \\dfrac{1}{3}} \\right) + \\cdot \\cdot \\cdot + \\left( {\\dfrac{1}{n} - \\dfrac{1}{n + 1}} \\right)\\\\ &= 1 - \\dfrac{1}{n + 1}.\\end{split}\\) 所以 \\({S_n} = 1 - \\dfrac{1}{n + 1}=\\dfrac{10}{11}\\)，解得：\\(n = 10\\)．";
        test = "下列发生的现象与摩擦起电有关的有($\\quad$) <br/>\n" +
                "a. 秋冬干燥季节，脱毛衣会看到电火花 $\\quad$ b. 擦黑板时粉笔灰四处飘落<br/>\n" +
                "c. 油罐车后面都有一条拖地的铁链 $\\quad$ d. 用塑料梳子梳干燥的头发，越梳越蓬松<br/>\n" +
                "e. 通信卫星采用硅光电池板提供电能 $\\quad$ f. 寒冷的冬天用湿手摸户外的金属单杠，手会被粘上<br/>\n" +
                "g. 两个铅柱底面削平挤压后能粘在一起$\\quad$ h. 电视里讲解棋类比赛时，棋子可以粘在竖直的棋盘上 ";
        test = "甲、乙两车在同一地点同时沿同一方向做直线运动,其$v-t$图象如图所示,则___________.↵<br/>" +
                "<img onclick='imgAction(event)' src='http://image01.cdn.klxuexi.com/tr/item/2017sqsmgywld8jkhzy/2017070895432ce87eed490296cdff0611a7e5a7?imageMogr2/format/jpg'/><br/><br/>↵A.甲的加速度大于乙的加速度 <br/>↵B.计时开始甲的加速度为零 <br/>↵C.$t_1$时刻,甲与乙在同一位置    <br/>↵D.$t_1$时刻,甲的速度大于乙的速度\n";
        mathView.setText(test);

        mathView.setMathRenderInterface(new MathRenderInterface() {
            @Override
            public void beginRender(MathView mathView) {
//                Toast.makeText(MathViewSimple.this, "render begin", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void endRender(MathView mathView) {
//                Toast.makeText(MathViewSimple.this, "render end", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        mathView = (MathView) findViewById(R.id.mathView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mathView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mathView.onResume();
    }
}
