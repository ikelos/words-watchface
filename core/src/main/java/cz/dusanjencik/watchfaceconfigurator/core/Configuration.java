package cz.dusanjencik.watchfaceconfigurator.core;

import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

import cz.dusanjencik.core.R;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com.
 * @created 26.09.15.
 */
public class Configuration {

	private Configuration() {
	}

	public static final boolean DEBUG                = App.getAppContext().getResources().getBoolean(R.bool.show_debug_log);
	public static final String  PATH                 = "/words_watch_face_config";
	public static final String  KEY_BACKGROUND_COLOR = "KEY_BACKGROUND_COLOR";
	public static final String  KEY_TEXT_COLOR       = "KEY_TEXT_COLOR";
	public static final String  KEY_ACCENT_COLOR     = "KEY_ACCENT_COLOR";
	public static final String  KEY_SHADOW_COLOR     = "KEY_SHADOW_COLOR";
	public static final String  KEY_LANG             = "KEY_LANG";
	public static final String  KEY_SHAPE            = "KEY_SHAPE";

	@IntDef ( {BACKGROUND_COLOR, TEXT_COLOR, ACCENT_COLOR, SHADOW_COLOR, LANG, SHAPE})
	@Retention (RetentionPolicy.SOURCE)
	public @interface SettingsType {
	}

	public static final int BACKGROUND_COLOR = 0;
	public static final int TEXT_COLOR       = 1;
	public static final int ACCENT_COLOR     = 2;
	public static final int SHADOW_COLOR     = 3;
	public static final int LANG             = 4;
	public static final int SHAPE            = 5;

	@IntDef ( {LANG_CZECH, LANG_ENGLISH})
	@Retention (RetentionPolicy.SOURCE)
	public @interface LangType {
	}

	public static final int LANG_ENGLISH = 0;
	public static final int LANG_CZECH   = 1;

	@IntDef ( {SHAPE_ROUND, SHAPE_SQUARE})
	@Retention (RetentionPolicy.SOURCE)
	public @interface ShapeType {
	}

	public static final int SHAPE_ROUND  = 0;
	public static final int SHAPE_SQUARE = 1;

	@LangType
	public static final int DEFAULT_LANG             = parseDefaultLang();
	public static final int DETAULT_BACKGROUND_COLOR = ContextCompat.getColor(App.getAppContext(), R.color.color11);
	public static final int DEFAULT_TEXT_COLOR       = ContextCompat.getColor(App.getAppContext(), R.color.color0);
	public static final int DETAULT_ACCENT_COLOR     = ContextCompat.getColor(App.getAppContext(), R.color.color2);
	public static final int DETAULT_SHADOW_COLOR     = ContextCompat.getColor(App.getAppContext(), R.color.color12);
	public static final int DEFAULT_SHAPE            = SHAPE_ROUND;

	@LangType
	private static int parseDefaultLang() {
		String locale = Locale.getDefault().getLanguage();
		if ("cs_CZ".equals(locale))
			return LANG_CZECH;
		return LANG_ENGLISH;
	}

}
