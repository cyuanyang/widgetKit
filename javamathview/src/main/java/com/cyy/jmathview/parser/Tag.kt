package com.cyy.jmathview.parser

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.cyy.jmathview.view.JLatexView

/**
 * Created by cyy on 17/10/21.
 *
 */

interface Tag{
    /**
     * 返回标签
     */
    fun tag():String

    fun buildView(parent: LinearLayout , attrs:Map<String , String> , text:String?): View
}


class ImageTag: Tag {
    override fun tag(): String {
        return "img"
    }

    override fun buildView(parent: LinearLayout, attrs:Map<String , String> , text:String?): View {
        return ImageView(parent.context).apply {
            setImageResource(android.R.drawable.presence_busy)
        }
    }
}

class TextTag: Tag {
    override fun tag(): String {
        return "text"
    }
    
    override fun buildView(parent: LinearLayout, attrs:Map<String , String> , text:String?): View {
        return JLatexView(parent.context).apply {
            this.setTextColor(android.graphics.Color.BLACK)
            this.latex = text
        }
    }

}

