package cz.dusanjencik.watchfaceconfigurator.adapters.view_holders;

import android.view.View;
import android.view.ViewGroup;

import cz.dusanjencik.watchfaceconfigurator.R;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 04.10.15.
 */
public class TextViewHolder extends ColorViewHolder {

	public TextViewHolder(View v) {
		super(v);
		ViewGroup.LayoutParams params = v.getLayoutParams();
		params.width = v.getContext().getResources().getDimensionPixelSize(R.dimen.lang_line_width);
		v.setLayoutParams(params);
	}
}
