package com.cyy.widgetkitsimple.simple;

import android.os.Bundle;

import com.cyy.jmathview.tools.LatexHelper;
import com.cyy.jmathview.view.NewMathView;
import com.cyy.widgetkitsimple.R;
import com.cyy.widgetkitsimple.base.BaseActivity;

public class JavaMathSimple extends BaseActivity {

    protected NewMathView mathView;
    String test = "\\(\\begin{split}{S_n} &= {a_1} + {a_2} + {a_3} + \\cdot \\cdot \\cdot +{a_n} \\\\&= \\left( {1 - \\dfrac{1}{2}} \\right) + \\left( {\\dfrac{1}{2} - \\dfrac{1}{3}} \\right) + \\cdot \\cdot \\cdot + \\left( {\\dfrac{1}{n} - \\dfrac{1}{n + 1}} \\right)\\\\ &= 1 - \\dfrac{1}{n + 1}.\\end{split}\\) 所以 \\({S_n} = 1 - \\dfrac{1}{n + 1}=\\dfrac{10}{11}\\)，解得：\\(n = 10\\)．";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_java_math_simple);
        initView();
//        test = "$f$";
        mathView.setText(new LatexHelper().getText(test , "text"));
    }

    private void initView() {
        mathView = (NewMathView) findViewById(R.id.mathView);
    }
}
