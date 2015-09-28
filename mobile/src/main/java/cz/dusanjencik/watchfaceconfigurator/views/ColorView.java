package cz.dusanjencik.watchfaceconfigurator.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import cz.dusanjencik.watchfaceconfigurator.R;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 28.09.15.
 */
public class ColorView extends View {
	public static final String TAG = ColorView.class.getSimpleName();

	private Paint mBackgroundPaint;
	private int     mColor;
	private float   mCenterX;
	private float   mCenterY;
	private Bitmap  mSelectedBitmap;
	private boolean mIsSelected;
	private boolean mIsMeasured = false;
	private RectF mMeasuredRect;

	public ColorView(Context context) {
		super(context);
		init();
	}

	public ColorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ColorView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public ColorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init() {
		mColor = Color.WHITE;
		mIsSelected = false;
		mSelectedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_24dp);
		mBackgroundPaint = new Paint();
		mBackgroundPaint.setColor(mColor);
		mBackgroundPaint.setAntiAlias(true);
		mBackgroundPaint.setStyle(Paint.Style.FILL);
		mBackgroundPaint.setShadowLayer(2.0f, 1.5f, 1.5f, ContextCompat.getColor(getContext(), R.color.shadow));
	}

	public void setColor(int color) {
		this.mColor = color;
		mBackgroundPaint.setColor(mColor);
		postInvalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();

		mCenterX = width / 2f;
		mCenterY = height / 2f;

		if (!mIsMeasured) {
			float scale = ((float) width) / (float) mSelectedBitmap.getWidth();
			mSelectedBitmap = Bitmap.createScaledBitmap(mSelectedBitmap,
					(int) (mSelectedBitmap.getWidth() * scale / 3),
					(int) (mSelectedBitmap.getHeight() * scale / 3), true);
			mMeasuredRect = new RectF(0, 0, width, height);
			mIsMeasured = true;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRoundRect(mMeasuredRect, 60, 60, mBackgroundPaint);

		if (mIsSelected)
			canvas.drawBitmap(mSelectedBitmap, mCenterX - (mSelectedBitmap.getWidth() / 2f), mCenterY - (mSelectedBitmap.getHeight() / 2f), null);

		canvas.save();
		canvas.restore();
	}

	public void setSelected(boolean isSelected) {
		this.mIsSelected = isSelected;
		postInvalidate();
	}

	public boolean isSelected() {
		return mIsSelected;
	}
}