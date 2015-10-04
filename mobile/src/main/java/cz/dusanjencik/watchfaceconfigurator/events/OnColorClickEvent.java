package cz.dusanjencik.watchfaceconfigurator.events;

import cz.dusanjencik.watchfaceconfigurator.core.Configuration;
import cz.dusanjencik.watchfaceconfigurator.core.events.IEvent;
import cz.dusanjencik.watchfaceconfigurator.models.ColorItem;

/**
 * @author Dušan Jenčík dusan.jencik@etnetera.cz.
 * @created 28.09.15.
 */
public class OnColorClickEvent implements IEvent {
	public final                             ColorItem item;
	@Configuration.SettingsType public final int       type;

	public OnColorClickEvent(ColorItem item, @Configuration.SettingsType int type) {
		this.item = item;
		this.type = type;
	}
}
