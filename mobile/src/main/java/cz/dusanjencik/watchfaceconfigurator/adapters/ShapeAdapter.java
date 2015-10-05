package cz.dusanjencik.watchfaceconfigurator.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.View;

import cz.dusanjencik.watchfaceconfigurator.adapters.view_holders.TextViewHolder;
import cz.dusanjencik.watchfaceconfigurator.core.Configuration;
import cz.dusanjencik.watchfaceconfigurator.events.OnShapeItemEvent;
import cz.dusanjencik.watchfaceconfigurator.models.ShapeItem;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 04.10.15.
 */
public class ShapeAdapter extends ABaseAdapter<TextViewHolder, ShapeItem> {
	public static final String TAG = ShapeAdapter.class.getSimpleName();

	public ShapeAdapter(Context context, Pair<ShapeItem[], Integer> itemsPosPair, @Configuration.SettingsType int type) {
		super(context, itemsPosPair, type, new OnShapeItemEvent());
	}

	@Override
	protected TextViewHolder onCreateInstanceViewHolder(View view) {
		return new TextViewHolder(view);
	}

	@Override
	protected void onRedraw(TextViewHolder holder, ShapeItem item, final int layoutPosition) {
		holder.button.setText(item.text);
	}
}
