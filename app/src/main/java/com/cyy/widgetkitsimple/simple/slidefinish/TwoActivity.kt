package com.cyy.widgetkitsimple.simple.slidefinish

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.cyy.widgetkitsimple.R
import kotlinx.android.synthetic.main.activity_slide_finish_simple.*

class TwoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two)

        nextBtn.setOnClickListener {
            startActivity(Intent(this , TwoActivity::class.java))
        }
    }
}
