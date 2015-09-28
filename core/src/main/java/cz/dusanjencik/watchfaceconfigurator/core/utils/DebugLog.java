package cz.dusanjencik.watchfaceconfigurator.core.utils;

import android.support.annotation.IntDef;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cz.dusanjencik.watchfaceconfigurator.core.Configuration;

/**
 * @author Dušan Jenčík dusan.jencik@etnetera.cz.
 * @created 28.09.15.
 */
public class DebugLog {
	public static final String TAG = DebugLog.class.getSimpleName();

	@IntDef ( {ALL, ERROR_PLUS_WARNING, ERROR, NONE})
	@Retention (RetentionPolicy.SOURCE)
	public @interface DebugLevel {
	}

	public static final int ALL                = 3;
	public static final int ERROR_PLUS_WARNING = 2;
	public static final int ERROR              = 1;
	public static final int NONE               = 0;

	@DebugLevel public static final int DEBUG_LEVEL = Configuration.DEBUG ? ALL : NONE;

	/**
	 * Log debug
	 */
	public static void log(final String TAG, final String message) {
		if (DEBUG_LEVEL > 2) Log.d(TAG, message);
	}

	/**
	 * Log warning
	 */
	public static void logW(final String TAG, final String message) {
		if (DEBUG_LEVEL > 1) Log.w(TAG, message);
	}

	/**
	 * Log Error
	 */
	public static void logE(final String TAG, final String message) {
		if (DEBUG_LEVEL > 0) Log.e(TAG, message);
	}
}
