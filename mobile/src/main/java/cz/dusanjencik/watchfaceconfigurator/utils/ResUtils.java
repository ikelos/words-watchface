package cz.dusanjencik.watchfaceconfigurator.utils;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.util.Pair;

import cz.dusanjencik.watchfaceconfigurator.models.ColorItem;

/**
 * @author Dušan Jenčík dusan.jencik@etnetera.cz.
 * @created 04.10.15.
 */
public class ResUtils {
	public static Pair<ColorItem[], Integer> getArray(Context context, @ArrayRes int arrayRes, @ColorRes int selectedColor) {
		int[] colors = context.getResources().getIntArray(arrayRes);
		ColorItem[] items = new ColorItem[colors.length];
		int selectedPosition = 0;
		for (int i = 0, count = colors.length; i < count; i++) {
			items[i] = new ColorItem(colors[i]);
			if (colors[i] == selectedColor)
				selectedPosition = i;
		}
		return new Pair<>(items, selectedPosition);
	}
}
