package cz.dusanjencik.watchfaceconfigurator.core.model;

/**
 * @author Dušan Jenčík dusan.jencik@etnetera.cz.
 * @created 28.09.15.
 */
public class AccentString {
	public final String text;
	public final boolean isAccent;

	public AccentString(String text, boolean isAccent) {
		this.text = text.toUpperCase();
		this.isAccent = isAccent;
	}
}
