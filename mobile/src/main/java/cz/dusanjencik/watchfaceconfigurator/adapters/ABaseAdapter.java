package cz.dusanjencik.watchfaceconfigurator.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cz.dusanjencik.watchfaceconfigurator.R;
import cz.dusanjencik.watchfaceconfigurator.adapters.view_holders.ABaseViewHolder;
import cz.dusanjencik.watchfaceconfigurator.core.Configuration;
import cz.dusanjencik.watchfaceconfigurator.events.ABaseItemEvent;
import de.greenrobot.event.EventBus;

/**
 * Template class for recyclerView adapter.
 *
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 04.10.15.
 */
public abstract class ABaseAdapter <VH extends ABaseViewHolder, T> extends RecyclerView.Adapter<VH> {

	protected final T[] mItems;

	protected LayoutInflater    mLayoutInflater;
	protected int               mSelectedPosition;
	@Configuration.SettingsType
	protected int               mSettingsType;
	protected ABaseItemEvent<T> mItemEvent;

	public ABaseAdapter(Context context, Pair<T[], Integer> itemsPosPair,
						@Configuration.SettingsType int type, ABaseItemEvent<T> itemEvent) {
		this.mLayoutInflater = LayoutInflater.from(context);
		this.mItems = itemsPosPair.first;
		this.mSelectedPosition = itemsPosPair.second;
		this.mSettingsType = type;
		this.mItemEvent = itemEvent;
	}

	@Override
	public VH onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = mLayoutInflater.inflate(R.layout.button_view, parent, false);
		return onCreateInstanceViewHolder(v);
	}

	protected abstract VH onCreateInstanceViewHolder(View view);

	@Override
	public void onBindViewHolder(VH holder, final int position) {
		T item = mItems[position];
		holder.selectedView.setVisibility(position == mSelectedPosition ? View.VISIBLE : View.GONE);
		onRedraw(holder, item, position);
		holder.getClickableView().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ABaseAdapter.this.onClick(position);
			}
		});
	}

	@Override
	public int getItemCount() {
		return mItems.length;
	}

	private void onClick(int layoutPosition) {
		int lastSelected = mSelectedPosition;
		mSelectedPosition = layoutPosition;
		notifyItemChanged(lastSelected);
		notifyItemChanged(mSelectedPosition);
		EventBus.getDefault().post(mItemEvent.setItem(mItems[mSelectedPosition]).setType(mSettingsType));
	}

	protected abstract void onRedraw(VH holder, T item, final int layoutPosition);

}
