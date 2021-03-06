package cz.dusanjencik.watchfaceconfigurator.core.lang.parsers;

import android.content.res.Resources;

import cz.dusanjencik.core.R;
import cz.dusanjencik.watchfaceconfigurator.core.lang.ALangParser;

/**
 * Class for parsing English language.
 *
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
		addBasicMinutes(4);
		mBuilder.add(R.string.en_past);
		addBasic(mHour - 1, true);
	}

	@Override
	public void onFiveToQuarter() {
		addBasicMinutes(9);
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
		addBasicMinutes(19, true);
		mBuilder.add(R.string.en_past);
		addBasic(mHour - 1, true);
	}

	@Override
	public void onFiveToHalf() {
		addBasicMinutes(24, true);
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
		addBasicMinutes(24, true);
		mBuilder.add(R.string.en_to);
		addBasic(mHour, true);
	}

	@Override
	public void onTwentyToFull() {
		addBasicMinutes(19, true);
		mBuilder.add(R.string.en_to);
		addBasic(mHour, true);
	}

	@Override
	public void onQuarterTo() {
		mBuilder.add(R.string.en_quarter, true);
		mBuilder.add(R.string.en_to);
		addBasic(mHour, true);
	}

	@Override
	public void onTenToFull() {
		addBasicMinutes(9);
		mBuilder.add(R.string.en_to);
		addBasic(mHour, true);
	}

	@Override
	public void onFiveToFull() {
		addBasicMinutes(4);
		mBuilder.add(R.string.en_to);
		addBasic(mHour, true);
	}
}
