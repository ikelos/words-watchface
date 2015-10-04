package cz.dusanjencik.watchfaceconfigurator.core.data;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import cz.dusanjencik.watchfaceconfigurator.core.Configuration;
import cz.dusanjencik.watchfaceconfigurator.core.events.OnShouldRedraw;
import cz.dusanjencik.watchfaceconfigurator.core.events.OnUpdateSettings;
import cz.dusanjencik.watchfaceconfigurator.core.utils.PrefUtils;
import de.greenrobot.event.EventBus;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 28.09.15.
 */
public class DataLayer implements GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener {
	public static final String TAG = DataLayer.class.getSimpleName();

	private GoogleApiClient mGoogleApiClient;

	public DataLayer(Context context) {
		mGoogleApiClient = new GoogleApiClient.Builder(context)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(Wearable.API)
				.build();
	}

	public void connect() {
		if (mGoogleApiClient != null && !mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting())
			mGoogleApiClient.connect();
	}

	public void disconnect() {
		if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
			Wearable.DataApi.removeListener(mGoogleApiClient, onDataChangedListener);
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	public void onConnected(Bundle bundle) {
		Wearable.DataApi.addListener(mGoogleApiClient, onDataChangedListener);
		Wearable.DataApi.getDataItems(mGoogleApiClient).setResultCallback(onConnectedResultCallback);
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

	}

	public void postToWearable(@Configuration.SettingsType int tag, int data) {
		PutDataMapRequest putDataMapReq = PutDataMapRequest.create(Configuration.PATH);

		switch (tag) {
			case Configuration.BACKGROUND_COLOR: {
				PrefUtils.setBackgroundColor(data);
				putDataMapReq.getDataMap().putInt(Configuration.KEY_BACKGROUND_COLOR, data);
				break;
			}
			case Configuration.TEXT_COLOR: {
				PrefUtils.setTextColor(data);
				putDataMapReq.getDataMap().putInt(Configuration.KEY_TEXT_COLOR, data);
				break;
			}
			case Configuration.ACCENT_COLOR: {
				PrefUtils.setAccentColor(data);
				putDataMapReq.getDataMap().putInt(Configuration.KEY_ACCENT_COLOR, data);
				break;
			}
			case Configuration.SHADOW_COLOR: {
				PrefUtils.setShadowColor(data);
				putDataMapReq.getDataMap().putInt(Configuration.KEY_SHADOW_COLOR, data);
				break;
			}
			case Configuration.LANG: {
				PrefUtils.setLang(data);
				putDataMapReq.getDataMap().putInt(Configuration.KEY_LANG, data);
			}
		}
		PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
		Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
	}

	private final ResultCallback<DataItemBuffer> onConnectedResultCallback = new ResultCallback<DataItemBuffer>() {
		@Override
		public void onResult(DataItemBuffer dataItems) {
			for (DataItem item : dataItems) {
				EventBus.getDefault().post(new OnUpdateSettings(item));
			}
			dataItems.release();
			EventBus.getDefault().post(new OnShouldRedraw());
		}
	};

	private final DataApi.DataListener onDataChangedListener = new DataApi.DataListener() {
		@Override
		public void onDataChanged(DataEventBuffer dataEvents) {
			for (DataEvent event : dataEvents) {
				if (event.getType() == DataEvent.TYPE_CHANGED) {
					EventBus.getDefault().post(new OnUpdateSettings(event.getDataItem()));
				}
			}
			dataEvents.release();
			EventBus.getDefault().post(new OnShouldRedraw());
		}
	};
}
