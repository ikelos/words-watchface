package cz.dusanjencik.watchfaceconfigurator.adapters.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.dusanjencik.watchfaceconfigurator.R;

/**
 * Parent class for ViewHolders.
 *
 * @author Dušan Jenčík dusanjencik@gmail.com
 * @created 04.10.15.
 */
public abstract class ABaseViewHolder extends RecyclerView.ViewHolder {
	@Bind (R.id.selected_view) public View selectedView;

	public ABaseViewHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);
	}

	public abstract View getClickableView();
}
