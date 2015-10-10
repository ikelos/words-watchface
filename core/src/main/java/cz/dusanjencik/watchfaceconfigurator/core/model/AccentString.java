package cz.dusanjencik.watchfaceconfigurator.core.model;

/**
 * Model class for accented string. The accented string has special color.
 *
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 28.09.15.
 */
public class AccentString {
	public final String  text;
	public final boolean isAccent;

	public AccentString(String text, boolean isAccent) {
		this.text = text.toUpperCase();
		this.isAccent = isAccent;
	}
}
