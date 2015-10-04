package cz.dusanjencik.watchfaceconfigurator.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.ArrayRes;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.dusanjencik.watchfaceconfigurator.R;
import cz.dusanjencik.watchfaceconfigurator.core.Configuration;
import cz.dusanjencik.watchfaceconfigurator.events.OnColorClickEvent;
import cz.dusanjencik.watchfaceconfigurator.models.ColorItem;
import de.greenrobot.event.EventBus;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com.
 * @created 28.09.15.
 */
public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {
	public static final String TAG = ColorAdapter.class.getSimpleName();

	private                             ColorItem[]    mItems;
	private                             LayoutInflater mLayoutInflater;
	private                             Context        mContext;
	@Configuration.SettingsType private int            mSettingsType;
	private                             int            mSelectedPosition;

	public ColorAdapter(Context context, @ArrayRes int arrayRes, @Configuration.SettingsType int type, int selectedColor) {
		this.mContext = context;
		this.mLayoutInflater = LayoutInflater.from(mContext);
		this.mSettingsType = type;
		int[] colors = mContext.getResources().getIntArray(arrayRes);
		this.mItems = new ColorItem[colors.length];
		for (int i = 0, count = colors.length; i < count; i++) {
			mItems[i] = new ColorItem(colors[i]);
			if (colors[i] == selectedColor)
				mSelectedPosition = i;
		}
	}

	@Override
	public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = mLayoutInflater.inflate(R.layout.color_view, parent, false);
		return new ColorViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ColorViewHolder holder, int position) {
		ColorItem colorItem = mItems[position];
		holder.button.setSupportBackgroundTintList(new ColorStateList(new int[][] {new int[0]}, new int[] {colorItem.color}));
		holder.selected_view.setBackgroundColor(colorItem.color);

		if (position == mSelectedPosition)
			holder.selected_view.setVisibility(View.VISIBLE);
		else
			holder.selected_view.setVisibility(View.GONE);
	}

	@Override
	public int getItemCount() {
		return mItems.length;
	}

	public class ColorViewHolder extends RecyclerView.ViewHolder {
		@Bind (R.id.button) public        AppCompatButton button;
		@Bind (R.id.selected_view) public View            selected_view;

		public ColorViewHolder(View v) {
			super(v);
			ButterKnife.bind(this, v);
		}

		@OnClick (R.id.button)
		public void onClick(View v) {
			int lastSelected = mSelectedPosition;
			mSelectedPosition = getLayoutPosition();
			notifyItemChanged(lastSelected);
			notifyItemChanged(mSelectedPosition);
			EventBus.getDefault().post(new OnColorClickEvent(mItems[mSelectedPosition], mSettingsType));
		}
	}
}
