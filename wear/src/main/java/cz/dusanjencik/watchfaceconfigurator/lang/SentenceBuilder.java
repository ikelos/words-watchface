package cz.dusanjencik.watchfaceconfigurator.lang;

import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.util.Log;

import cz.dusanjencik.watchfaceconfigurator.R;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 26.09.15.
 */
public class SentenceBuilder {
	public static final  String TAG   = SentenceBuilder.class.getSimpleName();
	private static final String SPACE = " ";

	private StringBuilder mFinalSentence;
	private Resources     mRes;
	private String[]      basicNumbers;
	private String[]      ordinalNumbers;
	private String[]      isWords;
	private String[]      hourWords;

	public SentenceBuilder(Resources res) {
		mFinalSentence = new StringBuilder("");
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
			mFinalSentence.append(isWords[1]);
		else
			mFinalSentence.append(isWords[0]);
	}

	public void add(@StringRes int string) {
		mFinalSentence.append(SPACE);
		mFinalSentence.append(mRes.getString(string));
	}

	public void add(String string) {
		mFinalSentence.append(SPACE);
		mFinalSentence.append(string);
	}

	public void addBasic(int index) {
		mFinalSentence.append(SPACE);
		mFinalSentence.append(basicNumbers[index >= 12 ? index - 12 : index]);
	}

	public void addOrdinal(int index) {
		mFinalSentence.append(SPACE);
		mFinalSentence.append(ordinalNumbers[index >= 12 ? index - 12 : index]);
	}

	public void addHours(int index) {
		mFinalSentence.append(SPACE);
		if (index == 1)
			mFinalSentence.append(hourWords[0]);
		else if (1 < index && index < 5)
			mFinalSentence.append(hourWords[1]);
		else
			mFinalSentence.append(hourWords[2]);
	}

	public String[] build() {
		return mFinalSentence.toString().toUpperCase().split(SPACE);
	}
}
