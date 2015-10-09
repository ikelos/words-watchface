package cz.dusanjencik.watchfaceconfigurator.core.events;

import com.google.android.gms.wearable.DataItem;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 28.09.15.
 */
public class OnUpdateSettingsEvent implements IEvent {
	public final DataItem dataItem;

	public OnUpdateSettingsEvent(DataItem dataItem) {
		this.dataItem = dataItem;
	}
}
