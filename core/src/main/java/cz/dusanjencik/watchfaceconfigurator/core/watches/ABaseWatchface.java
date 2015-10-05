package cz.dusanjencik.watchfaceconfigurator.core.watches;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Calendar;

import cz.dusanjencik.watchfaceconfigurator.core.Configuration;
import cz.dusanjencik.watchfaceconfigurator.core.utils.PrefUtils;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 26.09.15.
 */
public abstract class ABaseWatchface {
	public static final String TAG = ABaseWatchface.class.getSimpleName();

	protected Calendar mTime;
	protected boolean  mIsAmbient, mIsLowBitAmbient, mIsCircle;
	protected Context mContext;

	public ABaseWatchface(Context context, Calendar calendar) {
		mContext = context;
		mTime = calendar;
	}

	public void onAmbientChanged(boolean isAmbient, boolean isLowBitAmbient) {
		mIsAmbient = isAmbient;
		mIsLowBitAmbient = isLowBitAmbient;
	}

	public void setIsCircle(boolean isCircle) {
		mIsCircle = isCircle;
		PrefUtils.setShape(mIsCircle ? Configuration.SHAPE_ROUND : Configuration.SHAPE_SQUARE);
	}

	public boolean isCircle() {
		return mIsCircle;
	}

	public boolean isAmbient() {
		return mIsAmbient;
	}

	public abstract void onDraw(Canvas canvas, Rect bounds);

}
