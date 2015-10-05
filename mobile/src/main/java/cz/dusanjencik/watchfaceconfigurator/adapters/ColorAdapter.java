package cz.dusanjencik.watchfaceconfigurator.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.ArrayRes;
import android.view.View;

import cz.dusanjencik.watchfaceconfigurator.adapters.view_holders.ColorViewHolder;
import cz.dusanjencik.watchfaceconfigurator.adapters.view_holders.TextViewHolder;
import cz.dusanjencik.watchfaceconfigurator.core.Configuration;
import cz.dusanjencik.watchfaceconfigurator.events.OnColorItemEvent;
import cz.dusanjencik.watchfaceconfigurator.models.ColorItem;
import cz.dusanjencik.watchfaceconfigurator.utils.ResUtils;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com.
 * @created 28.09.15.
 */
public class ColorAdapter extends ABaseAdapter<ColorViewHolder, ColorItem> {
	public static final String TAG = ColorAdapter.class.getSimpleName();

	public ColorAdapter(Context context, @ArrayRes int arrayRes, @Configuration.SettingsType int type, int selectedColor) {
		super(context, ResUtils.getArray(context, arrayRes, selectedColor), type, new OnColorItemEvent());
	}

	@Override
	protected ColorViewHolder onCreateInstanceViewHolder(View view) {
		return new ColorViewHolder(view);
	}

	@Override
	protected void onRedraw(ColorViewHolder holder, ColorItem item, final int layoutPosition) {
		holder.selectedView.setBackgroundColor(item.color);
		holder.button.setSupportBackgroundTintList(new ColorStateList(new int[][] {new int[0]}, new int[] {item.color}));
	}
}
