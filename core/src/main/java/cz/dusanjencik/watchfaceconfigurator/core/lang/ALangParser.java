package cz.dusanjencik.watchfaceconfigurator.core.lang;

import android.content.res.Resources;
import android.support.annotation.ArrayRes;

import cz.dusanjencik.watchfaceconfigurator.core.model.AccentString;

/**
 * Parent class as language parser controller.
 *
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 03.10.15.
 */
public abstract class ALangParser {

	protected       SentenceBuilder mBuilder;
	protected final int             mHour, mMinute;
	protected final String[] basicNumbers;
	protected final String[] ordinalNumbers;
	protected final String[] hourWords;

	public ALangParser(Resources res, int hour, int minute,
					   @ArrayRes int basicNumberRes,
					   @ArrayRes int ordinalNumberRes,
					   @ArrayRes int hoursWordsRes) {
		this.mBuilder = new SentenceBuilder(res);
		this.mHour = hour;
		this.mMinute = minute;
		basicNumbers = res.getStringArray(basicNumberRes);
		ordinalNumbers = res.getStringArray(ordinalNumberRes);
		hourWords = res.getStringArray(hoursWordsRes);
	}

	public abstract void is(int minMinute, int maxMinute);

	public abstract void onFullOClock(int h);

	public abstract void onFivePast();

	public abstract void onFiveToQuarter();

	public abstract void onQuarterPast();

	public abstract void onTenToHalf();

	public abstract void onFiveToHalf();

	public abstract void onHalf();

	public abstract void onFivePastHalf();

	public abstract void onTwentyToFull();

	public abstract void onQuarterTo();

	public abstract void onTenToFull();

	public abstract void onFiveToFull();

	public AccentString[] build() {
		return mBuilder.build();
	}

	protected int repairHourIndex(int index) {
		if (index >= 12) return index - 12;
		if (index < 0) return index + 12;
		return index;
	}

	protected void addBasic(int index, boolean isAccent) {
		assert basicNumbers != null;
		mBuilder.add(basicNumbers[repairHourIndex(index)], isAccent);
	}

	protected void addBasic(int index) {
		addBasic(index, false);
	}

	protected void addOrdinal(int index, boolean isAccent) {
		assert ordinalNumbers != null;
		mBuilder.add(ordinalNumbers[repairHourIndex(index)], isAccent);
	}

	protected void addOrdinal(int index) {
		addOrdinal(index, false);
	}

	protected void addHours(int index, boolean isAccent) {
		assert hourWords != null;
		if (index == 1)
			mBuilder.add(hourWords[0], isAccent);
		else if (1 < index && index < 5)
			mBuilder.add(hourWords[1], isAccent);
		else
			mBuilder.add(hourWords[2], isAccent);
	}

	protected void addHours(int index) {
		addHours(index, false);
	}
}
