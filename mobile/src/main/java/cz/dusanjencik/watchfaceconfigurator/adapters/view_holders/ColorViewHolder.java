package cz.dusanjencik.watchfaceconfigurator.adapters.view_holders;

import android.support.v7.widget.AppCompatButton;
import android.view.View;

import butterknife.Bind;
import cz.dusanjencik.watchfaceconfigurator.R;

/**
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 04.10.15.
 */
public class ColorViewHolder extends ABaseViewHolder {
	@Bind (R.id.button) public AppCompatButton button;

	public ColorViewHolder(View v) {
		super(v);
	}

	@Override
	public View getClickableView() {
		return button;
	}
}
