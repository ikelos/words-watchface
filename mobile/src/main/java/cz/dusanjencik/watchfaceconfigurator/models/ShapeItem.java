package cz.dusanjencik.watchfaceconfigurator.models;

import cz.dusanjencik.watchfaceconfigurator.core.Configuration;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 04.10.15.
 */
public class ShapeItem {
	public final String text;
	@Configuration.ShapeType
	public final int    shape;

	public ShapeItem(String text, @Configuration.ShapeType int shape) {
		this.text = text;
		this.shape = shape;
	}
}
