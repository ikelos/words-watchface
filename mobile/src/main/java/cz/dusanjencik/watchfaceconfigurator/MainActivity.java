package cz.dusanjencik.watchfaceconfigurator;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import cz.dusanjencik.watchfaceconfigurator.adapters.ColorAdapter;
import cz.dusanjencik.watchfaceconfigurator.core.Configuration;
import cz.dusanjencik.watchfaceconfigurator.core.data.DataLayer;
import cz.dusanjencik.watchfaceconfigurator.core.events.OnShouldRedraw;
import cz.dusanjencik.watchfaceconfigurator.core.events.OnUpdateSettings;
import cz.dusanjencik.watchfaceconfigurator.core.utils.DebugLog;
import cz.dusanjencik.watchfaceconfigurator.core.utils.PrefUtils;
import cz.dusanjencik.watchfaceconfigurator.core.watches.WordsWatchFace;
import cz.dusanjencik.watchfaceconfigurator.events.OnColorClickEvent;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

	public static final String TAG = MainActivity.class.getSimpleName();

	private static final int LONG_DELAY = 1000;

	@Bind (R.id.watch_surface)            SurfaceView  mWatchSurface;
	@Bind (R.id.background_recycler_view) RecyclerView mBackgroundRecyclerView;
	@Bind (R.id.text_recycler_view)       RecyclerView mTextRecyclerView;
	@Bind (R.id.accent_recycler_view)     RecyclerView mAccentRecyclerView;
	@Bind (R.id.shadow_recycler_view)     RecyclerView mShadowRecyclerView;
	@Bind (R.id.reveal_view)              View         mRevealView;
	@Bind (R.id.btnLangCS)                Button       mBtnLangCS;
	@Bind (R.id.btnLangEN)                Button       mBtnLangEN;

	private WordsWatchFace mWatchface;
	private Calendar       mCalendar;
	private DataLayer      mDataLayer;
	private Handler        mPeriodicHandler;
	private Rect           mWatchSize;
	private int            mWatchfaceSizeX;
	private boolean        mIsRevealInAnim;
	private int            mRefreshDelay;
	private int            touchX, touchY;
	private Vibrator 	   mVibrator;

	private Paint mBackgroundPaint, mWatchFramePaint, mWatchBeltPaint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null)
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		initPaints();

		mWatchfaceSizeX = getResources().getDimensionPixelSize(R.dimen.watch_face_size);
		mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		mCalendar = new GregorianCalendar();
		mWatchface = new WordsWatchFace(this, mCalendar, false);

		final int backgroundColor = PrefUtils.getBackgroundColor();
		mWatchface.updateBackgroundColourTo(backgroundColor);
		ColorAdapter backgroundAdapter = new ColorAdapter(this, R.array.color_set_background,
				Configuration.BACKGROUND_COLOR, backgroundColor);
		initRecyclerView(mBackgroundRecyclerView, backgroundAdapter);

		final int textColor = PrefUtils.getTextColor();
		mWatchface.updateTextColorTo(textColor);
		ColorAdapter textAdapter = new ColorAdapter(this, R.array.color_set_accent_and_text,
				Configuration.TEXT_COLOR, textColor);
		initRecyclerView(mTextRecyclerView, textAdapter);

		final int accentColor = PrefUtils.getAccentColor();
		mWatchface.updateAccentColorTo(accentColor);
		ColorAdapter accentAdapter = new ColorAdapter(this, R.array.color_set_accent_and_text,
				Configuration.ACCENT_COLOR, accentColor);
		initRecyclerView(mAccentRecyclerView, accentAdapter);

		final int shadowColor = PrefUtils.getShadowColor();
		mWatchface.updateShadowColorTo(shadowColor);
		ColorAdapter shadowAdapter = new ColorAdapter(this, R.array.color_set_shadow_text,
				Configuration.SHADOW_COLOR, shadowColor);
		initRecyclerView(mShadowRecyclerView, shadowAdapter);

		final int lang = PrefUtils.getLang();

		mDataLayer = new DataLayer(this) {
			@Override
			public void onConnected(Bundle bundle) {
				super.onConnected(bundle);
				// Sync values in watch with phone.
				mDataLayer.postToWearable(Configuration.BACKGROUND_COLOR, backgroundColor);
				mDataLayer.postToWearable(Configuration.TEXT_COLOR, textColor);
				mDataLayer.postToWearable(Configuration.ACCENT_COLOR, accentColor);
				mDataLayer.postToWearable(Configuration.SHADOW_COLOR, shadowColor);
				mDataLayer.postToWearable(Configuration.LANG, lang);
			}
		};
		mPeriodicHandler = new Handler();
		mIsRevealInAnim = false;
		mRevealView.setAlpha(0);
	}

	@OnTouch (R.id.reveal_view)
	public boolean onRevealTouch(View v, MotionEvent event) {
		touchX = (int) event.getX();
		touchY = (int) event.getY();
		return false;
	}

	@OnClick ( {R.id.btnLangCS, R.id.btnLangEN})
	public void onLanguageClick(View v) {
		@Configuration.LangType int lang;
		if (v.getId() == R.id.btnLangCS) {
			lang = Configuration.LANG_CZECH;
		} else {
			lang = Configuration.LANG_ENGLISH;
		}
		if (mWatchface.getLang() == lang) return;
//		mWatchface.updateLang(lang);
		mDataLayer.postToWearable(Configuration.LANG, lang);
//		mWatchface.generateDistributionInNextDraw();
		redraw();
	}

	@OnTouch (R.id.watch_surface)
	public boolean onWatchSurfaceTouch(View v, MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		int width = mWatchSurface.getWidth();
		int height = mWatchSurface.getHeight();
		if (x > width / 2f + mWatchfaceSizeX / 2f - mWatchfaceSizeX / 10f &&
				x < width / 2f + mWatchfaceSizeX / 2f + mWatchfaceSizeX / 5f &&
				y > height / 2f - mWatchfaceSizeX / 5f &&
				y < height / 2f - mWatchfaceSizeX / 5f + mWatchfaceSizeX / 2.5f &&
				event.getAction() == MotionEvent.ACTION_DOWN) {
			Snackbar.make(mWatchSurface, getString(R.string.snackbar_ambient) + (mWatchface.isAmbient() ? getString(R.string.ambient_on) : getString(R.string.ambient_off)), Snackbar.LENGTH_SHORT).show();
			mWatchface.onAmbientChanged(!mWatchface.isAmbient(), false);
			mVibrator.vibrate(50);
			tryDrawing(mWatchSurface.getHolder());
			return true;
		}
		return false;
	}

	private void startRevealAnim(final int color) {
		mIsRevealInAnim = true;
		int finalRadius = Math.max(touchX, touchY);
		Animator anim =
				ViewAnimationUtils.createCircularReveal(mRevealView, touchX, touchY, 0, finalRadius);
		anim.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				mRevealView.setAlpha(1);
				mRevealView.setBackgroundColor(color);
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mIsRevealInAnim = false;
				tryDrawing(mWatchSurface.getHolder());
				mRevealView.animate().alpha(0);
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});
		anim.start();
	}

	private void initPaints() {
		mBackgroundPaint = new Paint();
		mBackgroundPaint.setColor(ContextCompat.getColor(this, R.color.background_color));
		mBackgroundPaint.setAntiAlias(true);
		mBackgroundPaint.setTextSize(40);
		mBackgroundPaint.setTextAlign(Paint.Align.CENTER);

		mWatchFramePaint = new Paint();
		mWatchFramePaint.setColor(ContextCompat.getColor(this, R.color.watch_frame));
		mWatchFramePaint.setAntiAlias(true);

		mWatchBeltPaint = new Paint();
		mWatchBeltPaint.setColor(ContextCompat.getColor(this, R.color.belt_color));
		mWatchBeltPaint.setAntiAlias(true);
	}

	private void initRecyclerView(RecyclerView recyclerView, ColorAdapter adapter) {
		recyclerView.setHasFixedSize(true);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(adapter);
	}

	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			mPeriodicHandler.removeCallbacks(this);
			redraw();
			if (mPeriodicHandler != null) {
				mPeriodicHandler.postDelayed(this, mRefreshDelay);
			}
		}
	};

	private void tryDrawing(SurfaceHolder holder) {
		if (mIsRevealInAnim) return;
		Canvas canvas = holder.lockCanvas();
		if (canvas == null) {
			DebugLog.logE(TAG, "Cannot draw onto the canvas as it's null");
		} else {

			int height = holder.getSurfaceFrame().height();
			int width = holder.getSurfaceFrame().width();
			if (mWatchSize == null) {
				mWatchSize = new Rect((int) (width / 2f - mWatchfaceSizeX / 2f), (int) (height / 2f - mWatchfaceSizeX / 2f),
						(int) (width / 2f + mWatchfaceSizeX / 2f), (int) (height / 2f + mWatchfaceSizeX / 2f));
				mRefreshDelay = LONG_DELAY;
			}

			canvas.drawRect(0, 0, width, height, mBackgroundPaint);
			canvas.drawRect(width / 2f + mWatchfaceSizeX / 2f, height / 2f - mWatchfaceSizeX / 30f,
					width / 2f + mWatchfaceSizeX / 2f + mWatchfaceSizeX / 15f,
					height / 2f - mWatchfaceSizeX / 30f + mWatchfaceSizeX / 15f, mWatchBeltPaint);
			canvas.drawRect(width / 2f - mWatchfaceSizeX / 5f, 0, width / 2f + mWatchfaceSizeX / 5f, height, mWatchBeltPaint);
			canvas.drawCircle(width / 2f, height / 2, mWatchfaceSizeX * 0.55f, mWatchFramePaint);
			mWatchface.onDraw(canvas, mWatchSize);
			holder.unlockCanvasAndPost(canvas);
		}
	}

	public void onEvent(OnColorClickEvent event) {
		startRevealAnim(event.item.color);
		mDataLayer.postToWearable(event.type, event.item.color);
	}

	public void onEvent(OnUpdateSettings event) {
		mWatchface.processConfigurationFor(event.dataItem);
	}

	public void onEvent(OnShouldRedraw event) {
		// redraw immediately after setting up
		tryDrawing(mWatchSurface.getHolder());
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mPeriodicHandler == null) mPeriodicHandler = new Handler();
		mRunnable.run();
	}

	@Override
	public void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
		mDataLayer.connect();
	}

	@Override
	public void onStop() {
		if (mPeriodicHandler != null)
			mPeriodicHandler.removeCallbacks(mRunnable);
		mPeriodicHandler = null;
		EventBus.getDefault().unregister(this);
		mDataLayer.disconnect();
		super.onStop();
	}

	private void redraw() {
		mCalendar.setTimeInMillis(System.currentTimeMillis());
		tryDrawing(mWatchSurface.getHolder());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
//		if (id == R.id.action_settings) {
//			return true;
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
