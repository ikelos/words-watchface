package cz.dusanjencik.watchfaceconfigurator.core.model;

/**
 * Model class for positioning of text.
 *
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 20.09.15.
 */
public class TextRow {
	public final int     startPos;
	public final boolean shouldShow;

	public TextRow(int startPos, boolean shouldShow) {
		this.startPos = startPos;
		this.shouldShow = shouldShow;
	}
}
