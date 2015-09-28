package cz.dusanjencik.watchfaceconfigurator.core.events;

import com.google.android.gms.wearable.DataItem;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 28.09.15.
 */
public class OnUpdateSettings implements IEvent {
	public final DataItem dataItem;

	public OnUpdateSettings(DataItem dataItem) {
		this.dataItem = dataItem;
	}
}
