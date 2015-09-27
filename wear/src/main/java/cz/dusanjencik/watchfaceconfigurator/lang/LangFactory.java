package cz.dusanjencik.watchfaceconfigurator.lang;

import android.content.res.Resources;

import java.util.Calendar;

import cz.dusanjencik.watchfaceconfigurator.R;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com.
 * @created 22.09.15.
 */
public class LangFactory {

	public static String[] parseDate(Calendar calendar, Resources res) {
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		SentenceBuilder builder = new SentenceBuilder(res);

		builder = parseCzech(hour, minute, builder);

		return builder.build();
	}

	private static SentenceBuilder parseCzech(int hour, int minute, SentenceBuilder builder) {
		// is
		builder.is(hour, minute, 3, 57);

		if (isIn(57, minute, 59) || isIn(0, minute, 3)) {
			int h = hour;
			if (minute >= 57) h++;
			builder.addBasic(h-1);
			builder.addHours(h);
		} else
		if(isIn(4, minute, 9)){
			builder.addBasic(4);
			builder.add(R.string.minute);
			builder.add(R.string.after);
			builder.addOrdinal(hour-1);
		}else
		if(isIn(10, minute, 13)){
			builder.add(R.string.to1);
			builder.addBasic(4);
			builder.add(R.string.minute);
			builder.add(R.string.quoter1);
			builder.add(R.string.to2);
			builder.addBasic(hour);
		}else
		if (isIn(14, minute, 19)) {
			builder.add(R.string.quoter1);
			builder.add(R.string.to2);
			builder.addBasic(hour);
		}else
		if (isIn(20, minute, 24)) {
			builder.add(R.string.to1);
			builder.addBasic(9);
			builder.add(R.string.half);
			builder.addOrdinal(hour);
		}else
		if (isIn(25, minute, 34)) {
			builder.add(R.string.half);
			builder.addOrdinal(hour);
		}else
		if (isIn(35, minute, 39)) {
			builder.addBasic(4);
			builder.add(R.string.minute);
			builder.add(R.string.after);
			builder.add(R.string.half);
			builder.addOrdinal(hour);
		}else
		if (isIn(40, minute, 43)) {
			builder.add(R.string.to1);
			builder.addBasic(4);
			builder.add(R.string.minute);
			builder.add(R.string.quoter2);
			builder.add(R.string.to2);
			builder.addBasic(hour);
		}else
		if (isIn(44, minute, 47)) {
			builder.add(R.string.quoter2);
			builder.add(R.string.to2);
			builder.addBasic(hour);
		}else
		if (isIn(48, minute, 52)) {
			builder.add(R.string.to1);
			builder.addBasic(9);
			builder.add(R.string.minute);
			builder.addBasic(hour);
		}else
		if (isIn(53, minute, 56)) {
			builder.add(R.string.to1);
			builder.addBasic(4);
			builder.add(R.string.minute);
			builder.addBasic(hour);
		}

		return builder;
	}

	private static boolean isIn(int min, int value, int max) {
		return min <= value && value <= max;
	}
}
