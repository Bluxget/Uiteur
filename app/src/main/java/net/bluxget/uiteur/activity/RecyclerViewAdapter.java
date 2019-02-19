package net.bluxget.uiteur.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bluxget.uiteur.R;
import net.bluxget.uiteur.data.PlayItem;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<PlayItem> mPlayitemList;

    public RecyclerViewAdapter(List<PlayItem> playitemList) {
        mPlayitemList = playitemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_row, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PlayItem playItem = mPlayitemList.get(i);

        viewHolder.name.setText(playItem.getName());
        viewHolder.author.setText(playItem.getAuthor());
        viewHolder.record.setText(playItem.getRecord());
    }

    @Override
    public int getItemCount() {
        return mPlayitemList.size();
    }
}
