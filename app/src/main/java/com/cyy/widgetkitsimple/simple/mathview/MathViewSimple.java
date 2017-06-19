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
        mathView.setText(test);

        mathView.setMathRenderInterface(new MathRenderInterface() {
            @Override
            public void beginRender(MathView mathView) {
                Toast.makeText(MathViewSimple.this, "render begin", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void endRender(MathView mathView) {
                Toast.makeText(MathViewSimple.this, "render end", Toast.LENGTH_SHORT).show();
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
