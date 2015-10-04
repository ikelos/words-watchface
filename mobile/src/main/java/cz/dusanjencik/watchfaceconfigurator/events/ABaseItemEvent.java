package cz.dusanjencik.watchfaceconfigurator.events;

import cz.dusanjencik.watchfaceconfigurator.core.Configuration;
import cz.dusanjencik.watchfaceconfigurator.core.events.IEvent;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 04.10.15.
 */
public abstract class ABaseItemEvent <T> implements IEvent {
	public                             T   item;
	@Configuration.SettingsType public int type;

	public ABaseItemEvent setItem(T item) {
		this.item = item;
		return this;
	}

	public ABaseItemEvent setType(int type) {
		this.type = type;
		return this;
	}
}