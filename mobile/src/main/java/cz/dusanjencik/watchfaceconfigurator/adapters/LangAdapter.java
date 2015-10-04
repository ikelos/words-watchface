package cz.dusanjencik.watchfaceconfigurator.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import cz.dusanjencik.watchfaceconfigurator.R;
import cz.dusanjencik.watchfaceconfigurator.adapters.view_holders.LangViewHolder;
import cz.dusanjencik.watchfaceconfigurator.core.Configuration;
import cz.dusanjencik.watchfaceconfigurator.events.OnLangItemEvent;
import cz.dusanjencik.watchfaceconfigurator.models.LangItem;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 04.10.15.
 */
public class LangAdapter extends ABaseAdapter<LangViewHolder, LangItem> {
	public static final String TAG = LangAdapter.class.getSimpleName();

	public LangAdapter(Context context, Pair<LangItem[], Integer> itemsPosPair, @Configuration.SettingsType int type) {
		super(context, itemsPosPair, type, new OnLangItemEvent());
	}

	@Override
	protected void onRedraw(LangViewHolder holder, LangItem item, final int layoutPosition) {
		holder.button.setText(item.text);
		holder.button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LangAdapter.super.onClick(layoutPosition);
			}
		});
	}

	@Override
	public LangViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = mLayoutInflater.inflate(R.layout.button_view, parent, false);
		return new LangViewHolder(v);
	}
}
