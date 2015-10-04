package cz.dusanjencik.watchfaceconfigurator.core.lang;

import android.content.res.Resources;

import java.util.Calendar;

import cz.dusanjencik.watchfaceconfigurator.core.Configuration;
import cz.dusanjencik.watchfaceconfigurator.core.lang.parsers.CzechParser;
import cz.dusanjencik.watchfaceconfigurator.core.lang.parsers.EnglishParser;
import cz.dusanjencik.watchfaceconfigurator.core.model.AccentString;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com.
 * @created 22.09.15.
 */
public class LangFactory {

	public static AccentString[] parseDate(Calendar calendar, Resources res, @Configuration.LangType int lang) {
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);

		ALangParser parser;
		switch (lang) {
			case Configuration.LANG_CZECH:
				parser = new CzechParser(res, hour, minute);
				break;
			case Configuration.LANG_ENGLISH:
			default:
				parser = new EnglishParser(res, hour, minute);
				break;
		}

		return parseTime(parser, hour, minute);
	}

	private static AccentString[] parseTime(ALangParser parser, int hour, int minute) {
		// TODO: 04.10.15 Zkontrolovat rozmezí
		parser.is(3, 57);
		if (isIn(57, minute, 59) || isIn(0, minute, 3)) {
			int h = hour;
			if (minute >= 57) h++;
			parser.onFullOClock(h - 1);
		} else if (isIn(4, minute, 9)) {
			parser.onFivePast();
		} else if (isIn(10, minute, 13)) {
			parser.onFiveToQuarter();
		} else if (isIn(14, minute, 18)) {
			parser.onQuarterPast();
		} else if (isIn(19, minute, 23)) {
			parser.onTenToHalf();
		} else if (isIn(24, minute, 27)) {
			parser.onFiveToHalf();
		} else if (isIn(28, minute, 34)) {
			parser.onHalf();
		} else if (isIn(35, minute, 39)) {
			parser.onFivePastHalf();
		} else if (isIn(40, minute, 43)) {
			parser.onTwentyToFull();
		} else if (isIn(44, minute, 47)) {
			parser.onQuarterTo();
		} else if (isIn(48, minute, 52)) {
			parser.onTenToFull();
		} else if (isIn(53, minute, 56)) {
			parser.onFiveToFull();
		}
		return parser.build();
	}

	private static boolean isIn(int min, int value, int max) {
		return min <= value && value <= max;
	}
}
