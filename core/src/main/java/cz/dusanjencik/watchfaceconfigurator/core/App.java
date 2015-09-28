package cz.dusanjencik.watchfaceconfigurator.core;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com.
 * @created 28.09.15.
 */
public class App extends Application {
	public static final String APP_TAG = App.class.getSimpleName();

	private static App sInstance = null;

	public static Context getAppContext() {
		if (sInstance == null) {
			throw new RuntimeException("Context is not known!");
		}
		return sInstance.getBaseContext();
	}

	@Override
	public void onCreate() {

		//Strict mode settings
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectAll()
				.penaltyFlashScreen()
				.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects()
				.detectLeakedClosableObjects()
				.penaltyLog()
//				.penaltyDeath()
				.build());

		super.onCreate();

		sInstance = this;
		onLoaded();
	}

	private void onLoaded() {
	}
}
