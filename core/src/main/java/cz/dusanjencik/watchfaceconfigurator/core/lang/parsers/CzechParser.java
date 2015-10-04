package cz.dusanjencik.watchfaceconfigurator.core.lang.parsers;

import android.content.res.Resources;

import cz.dusanjencik.core.R;
import cz.dusanjencik.watchfaceconfigurator.core.lang.ALangParser;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 03.10.15.
 */
public class CzechParser extends ALangParser {
	public static final String TAG = CzechParser.class.getSimpleName();

	public CzechParser(Resources res, int hour, int minute) {
		super(res, hour, minute, R.array.cs_numbers, R.array.cs_ordinalNumbers, R.array.cs_hours);
	}

	@Override
	public void is(int minMinute, int maxMinute) {
		int h = mHour;
		if (mMinute > minMinute) h++;
		if (1 < h && h < 5 && (mMinute >= maxMinute || mMinute <= minMinute))
			mBuilder.add(R.string.cs_is2);
		else
			mBuilder.add(R.string.cs_is1);
	}

	@Override
	public void onFullOClock(int h) {
		addBasic(h, true);
		addHours(h+1, true);
	}

	@Override
	public void onFivePast() {
		addBasic(4);
		mBuilder.add(R.string.cs_minute);
		mBuilder.add(R.string.cs_after);
		addOrdinal(mHour - 1, true);
	}

	@Override
	public void onFiveToQuarter() {
		mBuilder.add(R.string.cs_to1);
		addBasic(4);
		mBuilder.add(R.string.cs_minute);
		mBuilder.add(R.string.cs_quarter1, true);
		mBuilder.add(R.string.cs_to2);
		addBasic(mHour, true);
	}

	@Override
	public void onQuarterPast() {
		mBuilder.add(R.string.cs_quarter1, true);
		mBuilder.add(R.string.cs_to2);
		addBasic(mHour, true);
	}

	@Override
	public void onTenToHalf() {
		mBuilder.add(R.string.cs_to1);
		addBasic(9);
		mBuilder.add(R.string.cs_half, true);
		addOrdinal(mHour, true);
	}

	@Override
	public void onFiveToHalf() {
		mBuilder.add(R.string.cs_to1);
		addBasic(4);
		mBuilder.add(R.string.cs_half, true);
		addOrdinal(mHour, true);
	}

	@Override
	public void onHalf() {
		mBuilder.add(R.string.cs_half, true);
		addOrdinal(mHour, true);
	}

	@Override
	public void onFivePastHalf() {
		addBasic(4);
		mBuilder.add(R.string.cs_minute);
		mBuilder.add(R.string.cs_after);
		mBuilder.add(R.string.cs_half, true);
		addOrdinal(mHour, true);
	}

	@Override
	public void onTwentyToFull() {
		mBuilder.add(R.string.cs_to1);
		addBasic(4);
		mBuilder.add(R.string.cs_minute);
		mBuilder.add(R.string.cs_quarter2, true);
		mBuilder.add(R.string.cs_to2);
		addBasic(mHour, true);
	}

	@Override
	public void onQuarterTo() {
		mBuilder.add(R.string.cs_quarter2, true);
		mBuilder.add(R.string.cs_to2);
		addBasic(mHour, true);
	}

	@Override
	public void onTenToFull() {
		mBuilder.add(R.string.cs_to1);
		addBasic(9);
		mBuilder.add(R.string.cs_minute);
		addBasic(mHour, true);
	}

	@Override
	public void onFiveToFull() {
		mBuilder.add(R.string.cs_to1);
		addBasic(4);
		mBuilder.add(R.string.cs_minute);
		addBasic(mHour, true);
	}
}
