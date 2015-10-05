package cz.dusanjencik.watchfaceconfigurator.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import cz.dusanjencik.watchfaceconfigurator.R;
import cz.dusanjencik.watchfaceconfigurator.adapters.view_holders.TextViewHolder;
import cz.dusanjencik.watchfaceconfigurator.core.Configuration;
import cz.dusanjencik.watchfaceconfigurator.events.OnLangItemEvent;
import cz.dusanjencik.watchfaceconfigurator.models.LangItem;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 04.10.15.
 */
public class LangAdapter extends ABaseAdapter<TextViewHolder, LangItem> {
	public static final String TAG = LangAdapter.class.getSimpleName();

	public LangAdapter(Context context, Pair<LangItem[], Integer> itemsPosPair, @Configuration.SettingsType int type) {
		super(context, itemsPosPair, type, new OnLangItemEvent());
	}

	@Override
	protected TextViewHolder onCreateInstanceViewHolder(View view) {
		return new TextViewHolder(view);
	}

	@Override
	protected void onRedraw(TextViewHolder holder, LangItem item, final int layoutPosition) {
		holder.button.setText(item.text);
	}
}
