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
import com.javateam.muzik.R;
import com.javateam.muzik.ThumbnailListActivity;
import com.javateam.muzik.entity.Album;
import com.javateam.muzik.listener.ItemClickListener;

import java.util.List;

public class AlbumCardAdapter extends RecyclerView.Adapter<CardViewHolder> {
    private Context context;
    private List<Album> listAlbum;

    public AlbumCardAdapter(Context context, List<Album> listAlbum) {
        this.context = context;
        this.listAlbum = listAlbum;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.square_item_layout, parent, false);

        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        final Album album = listAlbum.get(position);
        holder.name.setText(album.getName());
        holder.name.setSelected(true);
//        holder.songUrl.setText(song.getSongUrl().substring(0, 50));

        Glide.with(context)
                .load(album.getImgUrl())
                .into(holder.thumbnail);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(context, "Long click:" + listAlbum.get(position).toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(context, ThumbnailListActivity.class);
                    intent.putExtra("type", "album");
                    intent.putExtra("album", listAlbum.get(position));
                    context.startActivity(intent);
//                    Toast.makeText(context, "Click: " + listAlbum.get(position).toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listAlbum != null) {
            return listAlbum.size();
        }
        return 0;
    }
}
