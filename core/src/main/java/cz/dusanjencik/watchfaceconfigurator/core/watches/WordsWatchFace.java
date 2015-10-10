package cz.dusanjencik.watchfaceconfigurator.core.watches;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

import cz.dusanjencik.watchfaceconfigurator.core.Configuration;
import cz.dusanjencik.watchfaceconfigurator.core.lang.LangFactory;
import cz.dusanjencik.watchfaceconfigurator.core.model.AccentString;
import cz.dusanjencik.watchfaceconfigurator.core.model.TextRow;
import cz.dusanjencik.watchfaceconfigurator.core.utils.PrefUtils;

/**
 * Words watch face implementation.
 *
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 26.09.15.
 */
public class WordsWatchFace extends ABaseWatchface {
	public static final  String   TAG                 = WordsWatchFace.class.getSimpleName();
	private static final Typeface NORMAL_TYPEFACE     =
			Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
	public static final  int      NEW_GENERATION_TIME = 60; // in sec
	private static final int[]    NUM_CHARS_ROUND     = new int[] {
			9, 11, 12, 13, 14, 14, 14, 14, 13, 12, 10
	};
	private static final int[]    NUM_CHARS_SQUARE    = new int[] {
			14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14
	};

	private Paint mBackgroundPaint;
	private Paint mTextPaint, mTextShadowPaint, mTextAccentPaint;
	private float          mTextSize;
	private TextRow[]      mTextRows;
	private AccentString[] mActualText;
	private boolean mShowFromTop = true;
	private Random mRandom;
	private long    mLastUpdate        = 0L;
	private boolean mIsInitializedSize = false;
	@Configuration.LangType
	private int mLang;
	@Configuration.ShapeType
	private int mShape;
	private boolean mIsEmulator = false;

	public WordsWatchFace(Context context, Calendar calendar, boolean isPeekCardShown, boolean isEmulator) {
		this(context, calendar, isPeekCardShown);
		mIsEmulator = isEmulator;
	}

	@SuppressWarnings ("ResourceType")
	public WordsWatchFace(Context context, Calendar calendar, boolean isPeekCardShown) {
		super(context, calendar);
		mTextSize = 22f;

		mBackgroundPaint = new Paint();
		mBackgroundPaint.setColor(PrefUtils.getBackgroundColor());

		mTextPaint = createTextPaint(PrefUtils.getTextColor());
		mTextShadowPaint = createTextPaint(PrefUtils.getShadowColor());
		mTextAccentPaint = createTextPaint(PrefUtils.getAccentColor());
		mLang = PrefUtils.getLang();
		mShape = PrefUtils.getShape();

		mRandom = new Random(System.currentTimeMillis());
		mShowFromTop = isPeekCardShown;
		generateNewDistribution();
	}

	@Configuration.LangType
	public int getLang() {
		return mLang;
	}

	@Configuration.ShapeType
	public int getShape() {
		return mShape;
	}

	public boolean isInitializedSize() {
		return mIsInitializedSize;
	}

	public void setIsShownPeekCard(boolean shown) {
		mShowFromTop = shown;
	}

	public void generateDistributionInNextDraw() {
		mLastUpdate = 0;
	}

	@Override
	public void onAmbientChanged(boolean isAmbient, boolean isLowBitAmbient) {
		super.onAmbientChanged(isAmbient, isLowBitAmbient);
		if (isLowBitAmbient) {
			mBackgroundPaint.setAntiAlias(!isAmbient);
			mTextPaint.setAntiAlias(!isAmbient);
			mTextShadowPaint.setAntiAlias(!isAmbient);
			mTextAccentPaint.setAntiAlias(!isAmbient);
		} else {
			mBackgroundPaint.setAntiAlias(true);
			mTextPaint.setAntiAlias(true);
			mTextShadowPaint.setAntiAlias(true);
			mTextAccentPaint.setAntiAlias(true);
		}
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

	@SuppressWarnings ("ResourceType")
	public void processConfigurationFor(DataItem item) {
		boolean regenerate = false;
		if (Configuration.PATH.equals(item.getUri().getPath())) {
			DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
			if (dataMap.containsKey(Configuration.KEY_BACKGROUND_COLOR)) {
				int value = dataMap.getInt(Configuration.KEY_BACKGROUND_COLOR, Configuration.DEFAULT_BACKGROUND_COLOR);
				PrefUtils.setBackgroundColor(value);
				updateBackgroundColourTo(value);
			}
			if (dataMap.containsKey(Configuration.KEY_TEXT_COLOR)) {
				int value = dataMap.getInt(Configuration.KEY_TEXT_COLOR, Configuration.DEFAULT_TEXT_COLOR);
				PrefUtils.setTextColor(value);
				updateTextColorTo(value);
			}
			if (dataMap.containsKey(Configuration.KEY_ACCENT_COLOR)) {
				int value = dataMap.getInt(Configuration.KEY_ACCENT_COLOR, Configuration.DEFAULT_ACCENT_COLOR);
				PrefUtils.setAccentColor(value);
				updateAccentColorTo(value);
			}
			if (dataMap.containsKey(Configuration.KEY_SHADOW_COLOR)) {
				int value = dataMap.getInt(Configuration.KEY_SHADOW_COLOR, Configuration.DEFAULT_SHADOW_COLOR);
				PrefUtils.setShadowColor(value);
				updateShadowColorTo(value);
			}
			if (dataMap.containsKey(Configuration.KEY_LANG)) {
				int value = dataMap.getInt(Configuration.KEY_LANG, Configuration.DEFAULT_LANG);
				PrefUtils.setLang(value);
				updateLang(value);
				regenerate = true;
			}
			if (dataMap.containsKey(Configuration.KEY_SHAPE)) {
				int value = dataMap.getInt(Configuration.KEY_SHAPE, Configuration.DEFAULT_SHAPE);
				PrefUtils.setShape(value);
				updateShape(value);
				regenerate = true;
			}
		}
		if (regenerate)
			generateDistributionInNextDraw();
	}

	public void updateBackgroundColourTo(int color) {
		mBackgroundPaint.setColor(color);
	}

	public void updateTextColorTo(int color) {
		mTextPaint.setColor(color);
	}

	public void updateAccentColorTo(int color) {
		mTextAccentPaint.setColor(color);
	}

	public void updateShadowColorTo(int color) {
		mTextShadowPaint.setColor(color);
	}

	public void updateLang(@Configuration.LangType int lang) {
		mLang = lang;
	}

	public void updateShape(@Configuration.ShapeType int shape) {
		mShape = shape;
	}

	private int[] getActualNumOfChars() {
		return mShape == Configuration.SHAPE_ROUND ? NUM_CHARS_ROUND : NUM_CHARS_SQUARE;
	}

	@Override
	public void onDraw(Canvas canvas, Rect bounds) {
		generateNewDistribution();

		if (!mIsInitializedSize) {
			mTextSize = bounds.width() / 14.54f;
			mTextPaint.setTextSize(mTextSize);
			mTextShadowPaint.setTextSize(mTextSize);
			mTextAccentPaint.setTextSize(mTextSize);
			mIsInitializedSize = true;
		}

		// Draw the background.
		if (!mIsEmulator) {
			canvas.drawRect(bounds.left, bounds.top, bounds.right, bounds.bottom, mBackgroundPaint);
		} else {
			if (mShape == Configuration.SHAPE_ROUND)
				canvas.drawCircle(bounds.centerX(), bounds.centerY(), bounds.width() / 2f + 1, mBackgroundPaint);
			else
				canvas.drawRoundRect(bounds.left, bounds.top, bounds.right, bounds.bottom,
						bounds.width() * 0.1f, bounds.width() * 0.1f, mBackgroundPaint);
		}

		float centerX = bounds.centerX();
		float offsetX = bounds.width() / 24f, offsetY = bounds.top + bounds.height() / 6f;

		boolean showText = true;
		int[] mNumOfChars = getActualNumOfChars();
		for (int y = 0, textUsed = 0; y < mNumOfChars.length; y++) {
			float offset = centerX - mNumOfChars[y] * mTextSize / 2f;
			boolean wasTextUsed = false;
			for (int x = 0, charUsed = 0; x < mNumOfChars[y]; x++) {
				if (showText && mTextRows[y].shouldShow && x >= mTextRows[y].startPos &&
						mActualText[textUsed].text.length() <= mNumOfChars[y] &&
						mActualText[textUsed].text.length() > charUsed) {
					canvas.drawText(String.valueOf(mActualText[textUsed].text.charAt(charUsed)),
							offsetX + offset + mTextSize * x, offsetY + mTextSize * y, mActualText[textUsed].isAccent ? mTextAccentPaint : mTextPaint);
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
		if ((mTime.getTimeInMillis() - mLastUpdate) / 1000L < NEW_GENERATION_TIME) {
			return false;
		}
		mLastUpdate = mTime.getTimeInMillis();
		int[] mNumOfChars = getActualNumOfChars();
		mTextRows = new TextRow[mNumOfChars.length];

		mActualText = LangFactory.parseDate(mTime, mContext.getResources(), mLang);

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
			if (showText && mActualText[textUsed].text.length() <= mNumOfChars[y])
				startPos = mRandom.nextInt(mNumOfChars[y] - mActualText[textUsed].text.length() + 1);
			for (int x = 0, charUsed = 0; x < mNumOfChars[y]; x++) {
				if (showText && shouldShow[y] && x >= startPos &&
						mActualText[textUsed].text.length() <= mNumOfChars[y] &&
						mActualText[textUsed].text.length() > charUsed) {
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
}
