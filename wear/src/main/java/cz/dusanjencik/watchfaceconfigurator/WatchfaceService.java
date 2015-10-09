/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.dusanjencik.watchfaceconfigurator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.WindowInsets;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import cz.dusanjencik.watchfaceconfigurator.core.data.DataLayer;
import cz.dusanjencik.watchfaceconfigurator.core.events.OnShouldRedrawEvent;
import cz.dusanjencik.watchfaceconfigurator.core.events.OnUpdateSettingsEvent;
import cz.dusanjencik.watchfaceconfigurator.core.watches.WordsWatchFace;
import de.greenrobot.event.EventBus;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 20.09.15.
 */
public class WatchfaceService extends CanvasWatchFaceService {

	/**
	 * Update rate in milliseconds for interactive mode.
	 */
	private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.MILLISECONDS.toMillis(1000); // 300

	/**
	 * Handler message id for updating the time periodically in interactive mode.
	 */
	private static final int    MSG_UPDATE_TIME = 0;
	private static final String TAG             = WatchfaceService.class.getSimpleName();

	@Override
	public Engine onCreateEngine() {
		return new Engine();
	}

	private class Engine extends CanvasWatchFaceService.Engine {
		final Handler mUpdateTimeHandler = new EngineHandler(this);

		final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				mTime.clear(Calendar.ZONE_OFFSET);
				mTime.setTimeInMillis(System.currentTimeMillis());
			}
		};

		boolean mRegisteredTimeZoneReceiver = false;

		boolean mAmbient;

		Calendar mTime;

		boolean mIsRound;
		float   mXOffset;
		float   mYOffset;
		int     mChinSize;

		DataLayer mDataLayer;

		WordsWatchFace mWatchFace;

		/**
		 * Whether the display supports fewer bits for each color in ambient mode. When true, we
		 * disable anti-aliasing in ambient mode.
		 */
		boolean mLowBitAmbient;

		@Override
		public void onCreate(SurfaceHolder holder) {
			super.onCreate(holder);

			setWatchFaceStyle(new WatchFaceStyle.Builder(WatchfaceService.this)
					.setCardPeekMode(WatchFaceStyle.PEEK_MODE_VARIABLE)
					.setAmbientPeekMode(WatchFaceStyle.AMBIENT_PEEK_MODE_HIDDEN)
					.setHotwordIndicatorGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
					.setStatusBarGravity(Gravity.TOP)
					.setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
					.setShowSystemUiTime(false)
					.build());
			Resources resources = WatchfaceService.this.getResources();
			mYOffset = resources.getDimension(R.dimen.digital_y_offset);

			mTime = new GregorianCalendar();
			mDataLayer = new DataLayer(WatchfaceService.this);

			mWatchFace = new WordsWatchFace(WatchfaceService.this, mTime, getPeekCardPosition().top != 0);
			mWatchFace.setIsShownPeekCard(getPeekCardPosition().top != 0);
		}

		@Override
		public void onDestroy() {
			mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
			releaseGoogleApiClient();
			super.onDestroy();
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);

			if (visible) {
				registerReceiver();
				mDataLayer.connect();
				// Update time zone in case it changed while we weren't visible.
				mTime.clear(Calendar.ZONE_OFFSET);
				mTime.setTimeInMillis(System.currentTimeMillis());
			} else {
				unregisterReceiver();
				releaseGoogleApiClient();
			}

			// Whether the timer should be running depends on whether we're visible (as well as
			// whether we're in ambient mode), so we may need to start or stop the timer.
			updateTimer();
		}

		private void releaseGoogleApiClient() {
			mDataLayer.disconnect();
		}

		public void onEvent(OnUpdateSettingsEvent event) {
			mWatchFace.processConfigurationFor(event.dataItem);
		}

		public void onEvent(OnShouldRedrawEvent event) {
			invalidateIfNecessary();
		}

		private void invalidateIfNecessary() {
			if (isVisible() && !isInAmbientMode()) {
				invalidate();
			}
		}

		private void registerReceiver() {
			if (mRegisteredTimeZoneReceiver) {
				return;
			}
			mRegisteredTimeZoneReceiver = true;
			EventBus.getDefault().register(this);
			IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
			WatchfaceService.this.registerReceiver(mTimeZoneReceiver, filter);
		}

		private void unregisterReceiver() {
			if (!mRegisteredTimeZoneReceiver) {
				return;
			}
			EventBus.getDefault().unregister(this);
			mRegisteredTimeZoneReceiver = false;
			WatchfaceService.this.unregisterReceiver(mTimeZoneReceiver);
		}

		@Override
		public void onApplyWindowInsets(WindowInsets insets) {
			super.onApplyWindowInsets(insets);

			// Load resources that have alternate values for round watches.
			Resources resources = WatchfaceService.this.getResources();
			mIsRound = insets.isRound();
			mXOffset = resources.getDimension(mIsRound
					? R.dimen.digital_x_offset_round : R.dimen.digital_x_offset);
			mChinSize = insets.getSystemWindowInsetBottom();
			mWatchFace.setIsCircle(mIsRound);
		}

		@Override
		public void onPropertiesChanged(Bundle properties) {
			super.onPropertiesChanged(properties);
			mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
		}

		@Override
		public void onPeekCardPositionUpdate(Rect rect) {
			super.onPeekCardPositionUpdate(rect);
			mWatchFace.setIsShownPeekCard(rect.top != 0);
			mWatchFace.generateDistributionInNextDraw();
			invalidate();
		}

		@Override
		public void onTimeTick() {
			super.onTimeTick();
			invalidate();
		}

		@Override
		public void onAmbientModeChanged(boolean inAmbientMode) {
			super.onAmbientModeChanged(inAmbientMode);
			if (mAmbient != inAmbientMode) {
				mAmbient = inAmbientMode;
				mWatchFace.onAmbientChanged(mAmbient, mLowBitAmbient);
				invalidate();
			}

			// Whether the timer should be running depends on whether we're visible (as well as
			// whether we're in ambient mode), so we may need to start or stop the timer.
			updateTimer();
		}

		@Override
		public void onDraw(Canvas canvas, Rect bounds) {
			mTime.setTimeInMillis(System.currentTimeMillis());
			mWatchFace.onDraw(canvas, bounds);
		}

		/**
		 * Starts the {@link #mUpdateTimeHandler} timer if it should be running and isn't currently
		 * or stops it if it shouldn't be running but currently is.
		 */
		private void updateTimer() {
			mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
			if (shouldTimerBeRunning()) {
				mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
			}
		}

		/**
		 * Returns whether the {@link #mUpdateTimeHandler} timer should be running. The timer should
		 * only run when we're visible and in interactive mode.
		 */
		private boolean shouldTimerBeRunning() {
			return isVisible() && !isInAmbientMode();
		}

		/**
		 * Handle updating the time periodically in interactive mode.
		 */
		private void handleUpdateTimeMessage() {
			invalidate();
			if (shouldTimerBeRunning()) {
				long timeMs = System.currentTimeMillis();
				long delayMs = INTERACTIVE_UPDATE_RATE_MS
						- (timeMs % INTERACTIVE_UPDATE_RATE_MS);
				mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
			}
		}
	}

	private static class EngineHandler extends Handler {
		private final WeakReference<WatchfaceService.Engine> mWeakReference;

		public EngineHandler(WatchfaceService.Engine reference) {
			mWeakReference = new WeakReference<>(reference);
		}

		@Override
		public void handleMessage(Message msg) {
			WatchfaceService.Engine engine = mWeakReference.get();
			if (engine != null) {
				switch (msg.what) {
					case MSG_UPDATE_TIME:
						engine.handleUpdateTimeMessage();
						break;
				}
			}
		}
	}
}
