package org.scilab.forge.jlatexmath.core;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import org.scilab.forge.jlatexmath.Internal;

public class AjLatexMath {

	//true 返回错误的字符提示 false 不显示
	public static final boolean IS_SHOW_ERROR_MSG = false;

	private static Context mContext;
	private static Paint st;
	private static Internal internal;

	public static void init(Context context) {
		mContext = context;
		internal = Internal.DEFAULT;
		st = new Paint(Paint.ANTI_ALIAS_FLAG);
		st.setStyle(Style.FILL_AND_STROKE);
		st.setColor(Color.BLACK);
		new TeXFormula();
	}

	public static AssetManager getAssetManager() {
		AssetManager mng = mContext.getAssets();
		return mng;
	}

	public static Context getContext() {
		return mContext;
	}

	public static Paint getPaint() {
		return st;
	}
	
	public static float getLeading(float textSize){
		st.setTextSize(textSize);
		return st.getFontSpacing();
	}

	public static Internal internal(){
		return internal;
	}
}
