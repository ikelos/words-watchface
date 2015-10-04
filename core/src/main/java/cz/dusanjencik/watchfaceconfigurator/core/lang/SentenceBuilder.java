package cz.dusanjencik.watchfaceconfigurator.core.lang;

import android.content.res.Resources;
import android.support.annotation.StringRes;

import java.util.ArrayList;

import cz.dusanjencik.core.R;
import cz.dusanjencik.watchfaceconfigurator.core.model.AccentString;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 26.09.15.
 */
public class SentenceBuilder {
	public static final  String TAG   = SentenceBuilder.class.getSimpleName();

	private ArrayList<AccentString> mFinalSentence;
	private Resources               mRes;

	public SentenceBuilder(Resources res) {
		mFinalSentence = new ArrayList<>();
		mRes = res;
	}

	public void add(@StringRes int string, boolean isAccent) {
		mFinalSentence.add(new AccentString(mRes.getString(string), isAccent));
	}

	public void add(String string, boolean isAccent) {
		mFinalSentence.add(new AccentString(string, isAccent));
	}

	public void add(@StringRes int string) {
		add(string, false);
	}

	public void add(String string) {
		add(string, false);
	}

	public AccentString[] build() {
		return mFinalSentence.toArray(new AccentString[mFinalSentence.size()]);
	}
}
