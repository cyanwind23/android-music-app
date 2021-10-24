package com.javateam.muzik.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.javateam.muzik.PlayActivity;
import com.javateam.muzik.R;
import com.javateam.muzik.entity.Song;
import com.javateam.muzik.listener.ItemClickListener;

import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<ListViewHolder> {
    private Context context;
    private List<Song> listSong;

    public SongListAdapter(Context context, List<Song> listSong) {
        this.context = context;
        this.listSong = listSong;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_layout, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        final Song song = listSong.get(position);
        holder.name.setText(song.getName());
        holder.subtitle.setText(song.getArtistsName());
        holder.name.setSelected(true);
        holder.subtitle.setSelected(true);
        Glide.with(context)
                .load(song.getImgUrl())
                .into(holder.thumbnail);

        holder.setItemClickListener((view, position1, isLongClick) -> {
            if (isLongClick) {
                Toast.makeText(context, "Long click:" + listSong.get(position1).toString(), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(context, PlayActivity.class);
                intent.putExtra("type", "song");
                intent.putExtra("song", listSong.get(position1));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listSong != null) {
            return listSong.size();
        }
        return 0;
    }
}