package com.cyy.widgetkitsimple.simple.slidefinish

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cyy.widgetkitsimple.R
import com.cyy.widgetkitsimple.base.BaseActivity
import kotlinx.android.synthetic.main.activity_two.*

class TwoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two)

        val index = intent?.getStringExtra("index")

        nextBtn.setOnClickListener {
            startActivity(Intent(this@TwoActivity , TwoActivity::class.java))
            overridePendingTransition(R.anim.enter, 0)
        }

        slideFinishLayout.finishListener = {
            finish()
            overridePendingTransition(0, 0)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.e(" TwoActivity" , "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.e(" TwoActivity" , "onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(" TwoActivity" , "onDestroy")
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.exit)
    }
}
