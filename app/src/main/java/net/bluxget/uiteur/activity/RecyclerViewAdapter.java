package net.bluxget.uiteur.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bluxget.uiteur.R;
import net.bluxget.uiteur.data.PlayItem;
import net.bluxget.uiteur.service.MediaPlayerService;

import java.util.List;

/**
 * RecyclerViewAdapter to PlayList
 *
 * @author Jonathan B.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private MediaPlayerActivity mActivity;
    private List<PlayItem> mPlayitemList;

    public RecyclerViewAdapter(MediaPlayerActivity activity, List<PlayItem> playitemList) {
        mActivity = activity;
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
        final PlayItem playItem = mPlayitemList.get(i);

        viewHolder.name.setText(playItem.getName());
        viewHolder.author.setText(playItem.getAuthor());
        viewHolder.record.setText(playItem.getRecord());

        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // Todo: intent to service music
                Intent intent = new Intent(MediaPlayerService.ACTION_PLAY);

                intent.putExtra("playid", playItem.getId());

                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlayitemList.size();
    }
}
