package cz.dusanjencik.watchfaceconfigurator.models;

import android.support.annotation.ColorInt;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 28.09.15.
 */
public class ColorItem {
	@ColorInt public final int color;

	public ColorItem(@ColorInt int color) {
		this.color = color;
	}
}
