package cz.dusanjencik.watchfaceconfigurator.core.lang.parsers;

import android.content.res.Resources;

import cz.dusanjencik.core.R;
import cz.dusanjencik.watchfaceconfigurator.core.lang.ALangParser;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 03.10.15.
 */
public class EnglishParser extends ALangParser {
	public static final String TAG = EnglishParser.class.getSimpleName();

	public EnglishParser(Resources res, int hour, int minute) {
		super(res, hour, minute, R.array.en_numbers, R.array.en_ordinalNumbers, R.array.en_hours);
	}

	@Override
	public void is(int minMinute, int maxMinute) {
		mBuilder.add(R.string.en_it);
		mBuilder.add(R.string.en_is);
	}

	@Override
	public void onFullOClock(int h) {
		addBasic(h, true);
		addHours(h + 1, true);
	}

	@Override
	public void onFivePast() {
		addBasic(4);
		mBuilder.add(R.string.en_past);
		addBasic(mHour - 1, true);
	}

	@Override
	public void onFiveToQuarter() {
		addBasic(9);
		mBuilder.add(R.string.en_past);
		addBasic(mHour - 1, true);
	}

	@Override
	public void onQuarterPast() {
		mBuilder.add(R.string.en_quarter, true);
		mBuilder.add(R.string.en_past);
		addBasic(mHour - 1, true);
	}

	@Override
	public void onTenToHalf() {
		addBasic(9);
		mBuilder.add(R.string.en_to);
		mBuilder.add(R.string.en_half, true);
		mBuilder.add(R.string.en_past);
		addBasic(mHour - 1, true);
	}

	@Override
	public void onFiveToHalf() {
		addBasic(4);
		mBuilder.add(R.string.en_to);
		mBuilder.add(R.string.en_half, true);
		mBuilder.add(R.string.en_past);
		addBasic(mHour - 1, true);
	}

	@Override
	public void onHalf() {
		mBuilder.add(R.string.en_half, true);
		mBuilder.add(R.string.en_past);
		addBasic(mHour - 1, true);
	}

	@Override
	public void onFivePastHalf() {
		addBasic(4);
		mBuilder.add(R.string.en_past);
		mBuilder.add(R.string.en_half, true);
		mBuilder.add(R.string.en_past);
		addBasic(mHour - 1, true);
	}

	@Override
	public void onTwentyToFull() {
		addBasic(9);
		mBuilder.add(R.string.en_past);
		mBuilder.add(R.string.en_half, true);
		mBuilder.add(R.string.en_past);
		addBasic(mHour - 1, true);
	}

	@Override
	public void onQuarterTo() {
		mBuilder.add(R.string.en_quarter, true);
		mBuilder.add(R.string.en_to);
		addBasic(mHour, true);
	}

	@Override
	public void onTenToFull() {
		addBasic(9);
		mBuilder.add(R.string.en_to);
		addBasic(mHour, true);
	}

	@Override
	public void onFiveToFull() {
		addBasic(4);
		mBuilder.add(R.string.en_to);
		addBasic(mHour, true);
	}
}
