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
import com.javateam.muzik.entity.Artist;

import java.util.List;

public class ArtistCardAdapter extends RecyclerView.Adapter<CardViewHolder> {
    private final Context context;
    private final List<Artist> listArtist;

    public ArtistCardAdapter(Context context, List<Artist> listArtist) {
        this.context = context;
        this.listArtist = listArtist;
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
        final Artist artist = listArtist.get(position);
        holder.name.setText(artist.getName());
        holder.name.setSelected(true);

        Glide.with(context)
                .load(artist.getImgUrl())
                .into(holder.thumbnail);

        holder.setItemClickListener((view, position1, isLongClick) -> {
            if (isLongClick) {
                Toast.makeText(context, "Long click:" + listArtist.get(position1).toString(), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(context, ThumbnailListActivity.class);
                intent.putExtra(ThumbnailListActivity.IK_TYPE, ThumbnailListActivity.IK_TYPE_ARTIST);
                intent.putExtra(ThumbnailListActivity.IK_TYPE_ARTIST, listArtist.get(position1));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listArtist != null) {
            return listArtist.size();
        }
        return 0;
    }
}
