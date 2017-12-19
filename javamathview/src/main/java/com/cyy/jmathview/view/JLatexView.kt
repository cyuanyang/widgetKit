package com.cyy.jmathview.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.util.AttributeSet
import android.widget.TextView
import com.cyy.jmathview.CenterImageSpan
import com.cyy.jmathview.LatexToken
import org.scilab.forge.jlatexmath.core.AjLatexMath
import org.scilab.forge.jlatexmath.core.Insets
import org.scilab.forge.jlatexmath.core.TeXConstants
import org.scilab.forge.jlatexmath.core.TeXFormula
import java.util.regex.Pattern

/**
 * Created by cyy on 17/10/19.
 * 数学解析的View
 *
 *
 */

class JLatexView : TextView {

    private val FORMULA_REG1 = Pattern.compile("(?i)\\$\\$?((.|\\n)+?)\\$\\$?")
    private val FORMULA_REG2 = Pattern.compile("(?i)\\\\[(\\[]((.|\\n)*?)\\\\[\\])]")
    private val FORMULA_REG3 = Pattern.compile("(?i)\\[tex]((.|\\n)*?)\\[/tex]")
    private val FORMULA_REG4 = Pattern.compile("(?i)\\\\begin\\{.*?\\}(.|\\n)*?\\\\end\\{.*?\\}")
    // private static final Pattern FORMULA_REG5 = Pattern.compile("(?i)\\$\\$(.+?)\\$\\$");
    private val PATTERNS = arrayOf(FORMULA_REG1, FORMULA_REG2, FORMULA_REG3, FORMULA_REG4)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    //math view 的宽度 用于计算latexview的宽度
    internal  var mathViewWidth = 0
    // －1 宽度足够  不需要设置最大宽度
    private var latexMaxWidth = -1

    var latex:String? = null
        set(value) {
            field = value
            parse()
        }

    private val latexTokenList = mutableListOf<LatexToken>()

    /**
     *  解析 latex
     */
    private fun parse(){
        latexTokenList.clear()
        PATTERNS.indices.map { PATTERNS[it].matcher(latex) }
                .forEach {
                    while (it.find()) {
                        val content = it.group()
                        val latexToken = LatexToken(content, it.start(), it.end())
                        latexTokenList.add(latexToken)
                    }
                }

        bindLatex()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var pWidth = mathViewWidth
        if (latexMaxWidth>mathViewWidth){
            pWidth = latexMaxWidth;
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(pWidth , MeasureSpec.EXACTLY), heightMeasureSpec)
    }

    private fun bindLatex(){
        val spannable = SpannableString(latex)

        var max = 0
        //latex
        latexTokenList.forEach {
            val bitmap = getBitmap(it.latex)
            val start = it.start
            val end = it.end
            val imageSpan = CenterImageSpan(context, bitmap)
            max = bitmap.width
            latexMaxWidth = Math.max(max , latexMaxWidth)

            spannable.setSpan(imageSpan , start , end , Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        text = spannable
    }

    private fun getBitmap(latex:String): Bitmap {
        //初始化AjLatexMath
        if (AjLatexMath.getContext()==null){
            AjLatexMath.init(context.applicationContext)
        }

        AjLatexMath.getPaint().color = currentTextColor
        val teXFormula = TeXFormula.getPartialTeXFormula(latex)
        val icon = teXFormula.TeXIconBuilder()
                .setStyle(TeXConstants.STYLE_DISPLAY)
                .setSize(paint.textSize / paint.density)
                .setWidth(TeXConstants.UNIT_SP, paint.textSize / paint.density, TeXConstants.ALIGN_LEFT)
                .setIsMaxWidth(true)
                .setInterLineSpacing(TeXConstants.UNIT_SP,
                        AjLatexMath.getLeading(paint.textSize / paint.density))
                .build()
        icon.insets = Insets(5, 5, 5, 5)

        val image = Bitmap.createBitmap(icon.iconWidth, icon.iconHeight,
                Bitmap.Config.ARGB_8888)

        val g2 = Canvas(image)
        g2.drawColor(Color.TRANSPARENT)
        icon.paintIcon(g2, 0, 0)
        return image
    }
}

