package com.cyy.widgetkitsimple.simple.slidefinish

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cyy.slidefinish.SlideFinishLayout
import com.cyy.slidefinish.SlideFinishLayout.SlideCallback
import com.cyy.widgetkitsimple.R
import kotlinx.android.synthetic.main.activity_slide_finish_simple.*


/**
 * Slide finish Simple
 */
class SlideFinishSimple : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_finish_simple)

        nextBtn.setOnClickListener {
            Log.e("TAg" , "setOnClickListener")
            startActivity(Intent(this , TwoActivity::class.java))
        }

    }

}
