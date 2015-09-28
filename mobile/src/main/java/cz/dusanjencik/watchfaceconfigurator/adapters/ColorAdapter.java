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
import cz.dusanjencik.watchfaceconfigurator.R;
import cz.dusanjencik.watchfaceconfigurator.core.Configuration;
import cz.dusanjencik.watchfaceconfigurator.core.utils.PrefUtils;
import cz.dusanjencik.watchfaceconfigurator.events.OnColorClickEvent;
import cz.dusanjencik.watchfaceconfigurator.models.ColorItem;
import de.greenrobot.event.EventBus;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com.
 * @created 28.09.15.
 */
public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {
	public static final String TAG = ColorAdapter.class.getSimpleName();

	private                          ColorItem[]    mItems;
	private                          LayoutInflater mLayoutInflater;
	private                          Context        mContext;
	@Configuration.ColorType private int            mColorType;
	private                          int            mSelectedItem;

	public ColorAdapter(Context context, @ArrayRes int arrayRes, @Configuration.ColorType int type, int selectedColor) {
		this.mContext = context;
		this.mLayoutInflater = LayoutInflater.from(mContext);
		this.mColorType = type;
		int[] colors = mContext.getResources().getIntArray(arrayRes);
		this.mItems = new ColorItem[colors.length];
		for (int i = 0, count = colors.length; i < count; i++) {
			mItems[i] = new ColorItem(colors[i]);
			if (colors[i] == selectedColor)
				mSelectedItem = i;
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
	}

	@Override
	public int getItemCount() {
		return mItems.length;
	}

	public void setSelected(int color) {
		if (color != PrefUtils.NONE_COLOR) {
			for (int i = 0, count = mItems.length; i < count; i++) {
				if (mItems[i].color == color) {
					mSelectedItem = i;
					return;
				}
			}
		}
		switch (mColorType) {
			case Configuration.BACKGROUND_COLOR:
				mSelectedItem = 0;
				break;
			case Configuration.TEXT_COLOR:
				mSelectedItem = 1;
				break;
			case Configuration.ACCENT_COLOR:
				mSelectedItem = 3;
				break;
			case Configuration.SHADOW_COLOR:
				mSelectedItem = 12;
		}
	}

	public class ColorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@Bind (R.id.button) public AppCompatButton button;

		public ColorViewHolder(View v) {
			super(v);
			ButterKnife.bind(this, v);
			button.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			EventBus.getDefault().post(new OnColorClickEvent(mItems[getLayoutPosition()], mColorType));
		}
	}
}
