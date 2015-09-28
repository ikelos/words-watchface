package cz.dusanjencik.watchfaceconfigurator.core.watches;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Calendar;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 26.09.15.
 */
public abstract class ABaseWatchface {
	public static final String TAG = ABaseWatchface.class.getSimpleName();

	protected Calendar mTime;
	protected boolean mIsAmbient, mIsLowBitAmbient;
	protected Context mContext;

	public ABaseWatchface(Context context, Calendar calendar) {
		mContext = context;
		mTime = calendar;
	}

	public void onAmbientChanged(boolean isAmbient, boolean isLowBitAmbient) {
		mIsAmbient = isAmbient;
		mIsLowBitAmbient = isLowBitAmbient;
	}

	public boolean isAmbient() {
		return mIsAmbient;
	}

	public abstract void onDraw(Canvas canvas, Rect bounds);

}
