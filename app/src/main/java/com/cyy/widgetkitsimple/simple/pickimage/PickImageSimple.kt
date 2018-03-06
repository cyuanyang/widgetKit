package com.cyy.widgetkitsimple.simple.pickimage

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cyy.widgetkitsimple.R
import com.permission.Permission
import com.tal.imagepicker.PickerImage
import com.tal.imagepicker.model.ImageItem
import kotlinx.android.synthetic.main.activity_pick_image_simple.*

class PickImageSimple : AppCompatActivity(),PickerImage.PickedCompleteListener {

    var mPickerImage:PickerImage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_image_simple)

        val list = listOf(Manifest.permission.READ_EXTERNAL_STORAGE);
        Permission.with(this)
                .permissions(list)
                .onDenied {
                    Log.e("TAG" ,"onDenied")
                }
                .onGranted {
                    Log.e("TAG" ,"onGranted")
                }
                .start()

        singlePickBtn.setOnClickListener {
            pick()
        }
    }

    private fun pick(){
        val builder = PickerImage.Builder()
        builder.setPickedCompleteListener(this)

        builder.setModel(PickerImage.PICK_MODE_SINGLE)
//                    builder.setModel(PickerImage.PICK_MODE_MULTIPLE)
//                    builder.setMax(imageCount)


            mPickerImage = builder.build()
             mPickerImage?.pick(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mPickerImage?.let {
            it.onActivityResult(requestCode , resultCode , data)
        }
    }

    override fun picked(pickedItems: MutableList<ImageItem>?, model: Int, isOrigin: Boolean) {
        Log.e("TAG" , "picked ---- ")
    }

    override fun cancel() {
        Log.e("TAG" , "cancel ---- ")
    }
}
