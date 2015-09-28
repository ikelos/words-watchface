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
	private static final String SPACE = " ";

	private ArrayList<AccentString> mFinalSentence;
	private Resources     mRes;
	private String[]      basicNumbers;
	private String[]      ordinalNumbers;
	private String[]      isWords;
	private String[]      hourWords;

	public SentenceBuilder(Resources res) {
		mFinalSentence = new ArrayList<>();
		mRes = res;
		basicNumbers = res.getStringArray(R.array.numbers);
		ordinalNumbers = res.getStringArray(R.array.ordinalNumbers);
		isWords = res.getStringArray(R.array.is);
		hourWords = res.getStringArray(R.array.hours);
	}

	public void is(int hour, int minute, int minMinute, int maxMinute) {
		int h = hour;
		if (minute > minMinute) h++;
		if (1 < h && h < 5 && (minute >= maxMinute || minute <= minMinute))
			mFinalSentence.add(new AccentString(isWords[1], false));
		else
			mFinalSentence.add(new AccentString(isWords[0], false));
	}

	public void add(@StringRes int string, boolean isAccent) {
		mFinalSentence.add(new AccentString(mRes.getString(string), isAccent));
	}

	public void add(String string, boolean isAccent) {
		mFinalSentence.add(new AccentString(string, isAccent));
	}

	public void addBasic(int index, boolean isAccent) {
		mFinalSentence.add(new AccentString(basicNumbers[index >= 12 ? index - 12 : index], isAccent));
	}

	public void addOrdinal(int index, boolean isAccent) {
		mFinalSentence.add(new AccentString(ordinalNumbers[index >= 12 ? index - 12 : index], isAccent));
	}

	public void add(@StringRes int string) {
		add(string, false);
	}

	public void add(String string) {
		add(string, false);
	}

	public void addBasic(int index) {
		addBasic(index, false);
	}

	public void addOrdinal(int index) {
		addOrdinal(index, false);
	}

	public void addHours(int index, boolean isAccent) {
		if (index == 1)
			mFinalSentence.add(new AccentString(hourWords[0], isAccent));
		else if (1 < index && index < 5)
			mFinalSentence.add(new AccentString(hourWords[1], isAccent));
		else
			mFinalSentence.add(new AccentString(hourWords[2], isAccent));
	}

	public void addHours(int index) {
		addHours(index, false);
	}

	public AccentString[] build() {
		return mFinalSentence.toArray(new AccentString[mFinalSentence.size()]);
	}
}
