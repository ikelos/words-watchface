package cz.dusanjencik.watchfaceconfigurator.watches;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

import cz.dusanjencik.core.Configuration;
import cz.dusanjencik.watchfaceconfigurator.R;
import cz.dusanjencik.watchfaceconfigurator.lang.LangFactory;
import cz.dusanjencik.watchfaceconfigurator.model.TextRow;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 26.09.15.
 */
public class WordsWatchFace extends ABaseWatchface {
	public static final  String   TAG             = WordsWatchFace.class.getSimpleName();
	private static final Typeface NORMAL_TYPEFACE =
			Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

	private Paint mBackgroundPaint;
	private Paint mTextPaint, mTextShadowPaint;
	private float     mTextSize;
	private int[]     mNumOfChars;
	private TextRow[] mTextRows;
	private String[]  mActualText;
	private boolean mShowFromTop = true;
	private Random mRandom;
	private long mLastUpdate = 0L;

	public WordsWatchFace(Context context, Calendar calendar, boolean isPeekCardShown) {
		super(context, calendar);
		mTextSize = 22f;

		mBackgroundPaint = new Paint();
		mBackgroundPaint.setColor(ContextCompat.getColor(context, R.color.digital_background));

		mTextPaint = createTextPaint(ContextCompat.getColor(context, R.color.digital_text));
		mTextShadowPaint = createTextPaint(ContextCompat.getColor(context, R.color.digital_text_shadow));

		mNumOfChars = new int[] {
				9, 11, 12, 13, 14, 14, 14, 14, 13, 12, 10
		};

		mRandom = new Random(System.currentTimeMillis());
		mShowFromTop = isPeekCardShown;
		generateNewDistribution();
	}

	public void setIsShownPeekCard(boolean shown) {
		mShowFromTop = shown;
	}

	public void generateDistributionInNextDraw(){
		mLastUpdate = 0;
	}

	@Override
	public void onAmbientChanged(boolean isAmbient, boolean isLowBitAmbient) {
		super.onAmbientChanged(isAmbient, isLowBitAmbient);
		if(isLowBitAmbient)
			mTextPaint.setAntiAlias(!isAmbient);
		else
			mTextPaint.setAntiAlias(true);
	}

	private Paint createTextPaint(int textColor) {
		Paint paint = new Paint();
		paint.setColor(textColor);
		paint.setTypeface(NORMAL_TYPEFACE);
		paint.setAntiAlias(true);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setTextSize(mTextSize);
		return paint;
	}

	public void processConfigurationFor(DataItem item) {
		if (Configuration.PATH.equals(item.getUri().getPath())) {
			DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
			if (dataMap.containsKey(Configuration.KEY_BACKGROUND_COLOUR)) {
				String backgroundColour = dataMap.getString(Configuration.KEY_BACKGROUND_COLOUR);
				updateBackgroundColourTo(Color.parseColor(backgroundColour));
			}

			if (dataMap.containsKey(Configuration.KEY_DATE_TIME_COLOUR)) {
				String timeColour = dataMap.getString(Configuration.KEY_DATE_TIME_COLOUR);
				updateDateAndTimeColourTo(Color.parseColor(timeColour));
			}
		}
	}

	public void updateBackgroundColourTo(int colour) {
		mBackgroundPaint.setColor(colour);
	}

	public void updateDateAndTimeColourTo(int colour) {
		mTextPaint.setColor(colour);
	}

	@Override
	public void onDraw(Canvas canvas, Rect bounds) {
		generateNewDistribution();
		// Draw the background.
		canvas.drawRect(0, 0, bounds.width(), bounds.height(), mBackgroundPaint);

		float centerX = bounds.centerX(), centerY = bounds.centerY();
		float offsetX = centerX / 12f, offsetY = centerY / 3f;

		boolean showText = true;
		for (int y = 0, textUsed = 0; y < mNumOfChars.length; y++) {
			float offset = centerX - mNumOfChars[y] * mTextSize / 2f;
			boolean wasTextUsed = false;
			for (int x = 0, charUsed = 0; x < mNumOfChars[y]; x++) {
				if (showText && mTextRows[y].shouldShow && x >= mTextRows[y].startPos &&
						mActualText[textUsed].length() <= mNumOfChars[y] &&
						mActualText[textUsed].length() > charUsed) {
					canvas.drawText(String.valueOf(mActualText[textUsed].charAt(charUsed)),
							offsetX + offset + mTextSize * x, offsetY + mTextSize * y, mTextPaint);
					charUsed++;
					wasTextUsed = true;
				} else {
					if (!mIsAmbient) {
						canvas.drawText(String.valueOf((char) (65 + mRandom.nextInt(26))),
								offsetX + offset + mTextSize * x, offsetY + mTextSize * y, mTextShadowPaint);
					}
				}
			}

			if (wasTextUsed && textUsed < mActualText.length) textUsed++;
			if (textUsed == mActualText.length)
				showText = false;
		}
	}

	private boolean generateNewDistribution() {
		if ((mTime.getTimeInMillis() - mLastUpdate) / 1000L < 60) {
//				Log.e("TIME", (mTime.getTimeInMillis()-mLastUpdate)/1000L + " s");
			return false;
		}
		mLastUpdate = mTime.getTimeInMillis();
		mTextRows = new TextRow[mNumOfChars.length];

		mActualText = LangFactory.parseDate(mTime, mContext.getResources());


		boolean[] shouldShow = new boolean[mNumOfChars.length];
		Arrays.fill(shouldShow, 0, mActualText.length, true);
		if (!mShowFromTop) {
			for (int i = 0; i < shouldShow.length; i++) {
				int ran = i + mRandom.nextInt(shouldShow.length - i);
				// perform swap
				boolean temp = shouldShow[i];
				shouldShow[i] = shouldShow[ran];
				shouldShow[ran] = temp;
			}
		}

		boolean showText = true;
		for (int y = 0, textUsed = 0; y < mNumOfChars.length; y++) {
			boolean wasTextUsed = false;
			int startPos = 100;
			if (showText && mActualText[textUsed].length() <= mNumOfChars[y])
				startPos = mRandom.nextInt(mNumOfChars[y] - mActualText[textUsed].length() + 1);
			for (int x = 0, charUsed = 0; x < mNumOfChars[y]; x++) {
				if (showText && shouldShow[y] && x >= startPos &&
						mActualText[textUsed].length() <= mNumOfChars[y] &&
						mActualText[textUsed].length() > charUsed) {
					mTextRows[y] = new TextRow(startPos, shouldShow[y]);
					charUsed++;
					wasTextUsed = true;
				} else {
					mTextRows[y] = new TextRow(startPos, shouldShow[y]);
				}
			}

			if (wasTextUsed && textUsed < mActualText.length) textUsed++;
			if (textUsed == mActualText.length)
				showText = false;
		}
		return true;
	}

	private interface OnDisplayListener {
		void onShowTime(Canvas canvas);
		void onShowOther(Canvas canvas);
	}
}
