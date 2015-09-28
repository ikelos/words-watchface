package cz.dusanjencik.watchfaceconfigurator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.ButterKnife;
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

	@Bind (R.id.watch_surface)            SurfaceView  mWatchSurface;
	@Bind (R.id.background_recycler_view) RecyclerView mBackgroundRecyclerView;
	@Bind (R.id.text_recycler_view)       RecyclerView mTextRecyclerView;
	@Bind (R.id.accent_recycler_view)     RecyclerView mAccentRecyclerView;
	@Bind (R.id.shadow_recycler_view)     RecyclerView mShadowRecyclerView;

	private WordsWatchFace mWatchface;
	private Calendar       mCalendar;
	private SurfaceView    mSurfaceView;
	private DataLayer      mDataLayer;
	private Handler        mPeriodicHandler;
	private Rect           mWatchSize;
	private int            mWatchfaceSizeX;

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

		mCalendar = new GregorianCalendar();
		mWatchface = new WordsWatchFace(this, mCalendar, false);

		mSurfaceView = (SurfaceView) findViewById(R.id.watch_surface);
		mSurfaceView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Snackbar.make(mWatchSurface, "Ambient mode is " + (mWatchface.isAmbient() ? "on" : "off"), Snackbar.LENGTH_SHORT).show();
				mWatchface.onAmbientChanged(!mWatchface.isAmbient(), false);
			}
		});

		int backgroundColor = PrefUtils.getBackgroundColor();
		mWatchface.updateBackgroundColourTo(backgroundColor);
		ColorAdapter backgroundAdapter = new ColorAdapter(this, R.array.color_set,
				Configuration.BACKGROUND_COLOR, backgroundColor);
		initRecyclerView(mBackgroundRecyclerView, backgroundAdapter);

		int textColor = PrefUtils.getTextColor();
		mWatchface.updateTextColorTo(textColor);
		ColorAdapter textAdapter = new ColorAdapter(this, R.array.color_set,
				Configuration.TEXT_COLOR, textColor);
		initRecyclerView(mTextRecyclerView, textAdapter);

		int accentColor = PrefUtils.getAccentColor();
		mWatchface.updateAccentColorTo(accentColor);
		ColorAdapter accentAdapter = new ColorAdapter(this, R.array.color_set,
				Configuration.ACCENT_COLOR, accentColor);
		initRecyclerView(mAccentRecyclerView, accentAdapter);

		int shadowColor = PrefUtils.getShadowColor();
		mWatchface.updateShadowColorTo(accentColor);
		ColorAdapter shadowAdapter = new ColorAdapter(this, R.array.color_set,
				Configuration.SHADOW_COLOR, shadowColor);
		initRecyclerView(mShadowRecyclerView, shadowAdapter);

		mDataLayer = new DataLayer(this);
		mPeriodicHandler = new Handler();
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
			if (mPeriodicHandler != null)
				mPeriodicHandler.postDelayed(this, 1000);
		}
	};

	private void tryDrawing(SurfaceHolder holder) {
		DebugLog.log(TAG, "Trying to draw...");

		Canvas canvas = holder.lockCanvas();
		if (canvas == null) {
			DebugLog.logE(TAG, "Cannot draw onto the canvas as it's null");
		} else {
			int height = holder.getSurfaceFrame().height();
			int width = holder.getSurfaceFrame().width();
			if (mWatchSize == null) {
				mWatchSize = new Rect((int) (width / 2f - mWatchfaceSizeX / 2f), (int) (height / 2f - mWatchfaceSizeX / 2f),
						(int) (width / 2f + mWatchfaceSizeX / 2f), (int) (height / 2f + mWatchfaceSizeX / 2f));
			}

			canvas.drawRect(0, 0, width, height, mBackgroundPaint);
			canvas.drawRect(width / 2f - mWatchfaceSizeX / 5f, 0, width / 2f + mWatchfaceSizeX / 5f, height, mWatchBeltPaint);
			canvas.drawCircle(width / 2f, height / 2, mWatchfaceSizeX * 0.55f, mWatchFramePaint);
			mWatchface.onDraw(canvas, mWatchSize);
			holder.unlockCanvasAndPost(canvas);
		}
	}

	public void onEvent(OnColorClickEvent event) {
		mDataLayer.postToWearable(event.type, event.item.color);
	}

	public void onEvent(OnUpdateSettings event) {
		mWatchface.processConfigurationFor(event.dataItem);
	}

	public void onEvent(OnShouldRedraw event) {
		// redraw immediately after setting up
//		tryDrawing(mSurfaceView.getHolder());
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
		tryDrawing(mSurfaceView.getHolder());
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
