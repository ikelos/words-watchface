package cz.dusanjencik.watchfaceconfigurator.models;

import cz.dusanjencik.watchfaceconfigurator.core.Configuration;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 04.10.15.
 */
public class LangItem {
	public final String text;
	@Configuration.LangType
	public final int    lang;

	public LangItem(String text, @Configuration.LangType int lang) {
		this.text = text;
		this.lang = lang;
	}
}
