package cz.dusanjencik.watchfaceconfigurator.core.utils;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.lang.ref.WeakReference;

import cz.dusanjencik.watchfaceconfigurator.core.App;
import cz.dusanjencik.watchfaceconfigurator.core.Configuration;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com.
 * @created 28.09.15.
 */
public class PrefUtils {
	public static final String TAG = PrefUtils.class.getSimpleName();

	public static final int NONE_COLOR = -1;

	private static WeakReference<SharedPreferences> sPrefs;

	private static SharedPreferences getPrefs() {
		if (sPrefs == null || sPrefs.get() == null) {
			sPrefs = new WeakReference<>(PreferenceManager.getDefaultSharedPreferences(App.getAppContext()));
		}
		return sPrefs.get();
	}

	@SuppressLint ("CommitPrefEdits")
	private static SharedPreferences.Editor getEditorAndParseValue(String key, Object value) {
		SharedPreferences.Editor editor = getPrefs().edit();
		if (value instanceof String) editor.putString(key, (String) value);
		else if (value instanceof Integer) editor.putInt(key, (Integer) value);
		else if (value instanceof Boolean) editor.putBoolean(key, (Boolean) value);
		else if (value instanceof Long) editor.putLong(key, (Long) value);
		else
			DebugLog.logE(TAG, "Cannot save " + (value == null ? "null" : (value.getClass().getSimpleName())));
		return editor;
	}

	private static SharedPreferences.Editor getEditorAndParseValue(SharedPreferences.Editor editor, String key, Object value) {
		if (value instanceof String) editor.putString(key, (String) value);
		else if (value instanceof Integer) editor.putInt(key, (Integer) value);
		else if (value instanceof Boolean) editor.putBoolean(key, (Boolean) value);
		else if (value instanceof Long) editor.putLong(key, (Long) value);
		else
			DebugLog.logE(TAG, "Cannot save " + (value == null ? "null" : (value.getClass().getSimpleName())));
		return editor;
	}

	/**
	 * Used for batch saving.
	 */
	private static SharedPreferences.Editor save(SharedPreferences.Editor editor, String key, Object value) {
		return getEditorAndParseValue(editor, key, value);
	}

	private static void saveImmediatelySync(String key, Object value) {
		getEditorAndParseValue(key, value).apply();
	}

	private static void saveImmediatelyAsync(String key, Object value) {
		getEditorAndParseValue(key, value).commit();
	}

	public static void setBackgroundColor(int color) {
		saveImmediatelySync(Configuration.KEY_BACKGROUND_COLOR, color);
	}

	public static int getBackgroundColor() {
		return getPrefs().getInt(Configuration.KEY_BACKGROUND_COLOR,
				Configuration.DETAULT_BACKGROUND_COLOR);
	}

	public static void setTextColor(int color) {
		saveImmediatelySync(Configuration.KEY_TEXT_COLOR, color);
	}

	public static int getTextColor() {
		return getPrefs().getInt(Configuration.KEY_TEXT_COLOR,
				Configuration.DEFAULT_TEXT_COLOR);
	}

	public static void setAccentColor(int color) {
		saveImmediatelySync(Configuration.KEY_ACCENT_COLOR, color);
	}

	public static int getAccentColor() {
		return getPrefs().getInt(Configuration.KEY_ACCENT_COLOR,
				Configuration.DETAULT_ACCENT_COLOR);
	}

	public static void setShadowColor(int color) {
		saveImmediatelySync(Configuration.KEY_SHADOW_COLOR, color);
	}

	public static int getShadowColor() {
		return getPrefs().getInt(Configuration.KEY_SHADOW_COLOR,
				Configuration.DETAULT_SHADOW_COLOR);
	}

	public static void setLang(@Configuration.LangType int lang) {
		saveImmediatelySync(Configuration.KEY_LANG, lang);
	}

	public static int getLang() {
		return getPrefs().getInt(Configuration.KEY_LANG, Configuration.DEFAULT_LANG);
	}
}
