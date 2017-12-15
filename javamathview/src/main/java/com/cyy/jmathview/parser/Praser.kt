package com.cyy.jmathview.parser

import android.util.Xml
import android.view.View
import com.cyy.jmathview.c_log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.StringReader


/**
 * Created by study on 17/10/21.
 *
 */



interface ViewDelegate{
    fun buildView(tag:String , attrs:Map<String , String> , text:String?): ViewModel
}


internal class XMLParser(view: ViewDelegate){

    private val mathView: ViewDelegate = view
    var closure:((tag:String , attrs:Map<String , String> , text:String?) -> Unit)? = null
    private var views = mutableListOf<ViewModel>()

    fun parser(xml: String): Boolean {

        val parser = Xml.newPullParser()
        try {
            parser.setInput(StringReader(xml))
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
            c_log("XmlPullParserException:$e")
        }
        try {
            var event = parser.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                when (event) {
                    XmlPullParser.START_DOCUMENT -> {
                    }
                    XmlPullParser.START_TAG -> {
                        val attrs = mutableMapOf<String , String>()
                        (0 until parser.attributeCount).forEach { i ->
                            attrs[parser.getAttributeName(i)] = parser.getAttributeValue(i)
                        }
                        val name = parser.name
                        val text = parser.nextText()
                        closure?.invoke(name , attrs , text)
                        buildView(name , attrs , text)
                    }
                    XmlPullParser.END_TAG -> {
                    }
                }
                event = parser.next()
            }
        } catch (e: Exception) {
            c_log("Exception:$e")
            e.printStackTrace()
            return false
        }
        return false
    }

    /**
     * 构建View
     */
    private fun buildView(tag:String , attrs:Map<String , String> , text:String?){
        views.add(mathView.buildView(tag , attrs , text))
    }

    internal fun getViews():List<ViewModel> = views.toList()
}

data class ViewModel(val view:View ,val data:String?)