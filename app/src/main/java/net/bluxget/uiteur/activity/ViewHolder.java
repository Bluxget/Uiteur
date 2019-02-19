package net.bluxget.uiteur.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.bluxget.uiteur.R;

/**
 * ViewHolder
 *
 * @author Jonathan B.
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView name, author, record;

    public ViewHolder(View view) {
        super(view);

        name = view.findViewById(R.id.name);
        author = view.findViewById(R.id.author);
        record = view.findViewById(R.id.record);
    }
}
