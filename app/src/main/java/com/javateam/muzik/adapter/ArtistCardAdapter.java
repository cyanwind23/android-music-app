package com.javateam.muzik.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.javateam.muzik.R;
import com.javateam.muzik.entity.Artist;
import com.javateam.muzik.listener.ItemClickListener;

import java.util.List;

public class ArtistCardAdapter extends RecyclerView.Adapter<CardViewHolder> {
    private Context context;
    private List<Artist> listArtist;

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
//        holder.songUrl.setText(song.getSongUrl().substring(0, 50));

        Glide.with(context)
                .load(artist.getImgUrl())
                .into(holder.thumbnail);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(context, "Long click:" + listArtist.get(position).toString(), Toast.LENGTH_SHORT).show();
                } else {
////                    Intent intent = new Intent(context, MusicPlayerActivity.class);
//                    Intent intent = new Intent(context, ExoPlayerTestActivity.class);
//                    intent.putExtra("listArtist", (Serializable) listArtist);
//                    Log.e("ArtistCardAdapter", listArtist.size() + "");
//                    intent.putExtra("songUrl", listArtist.get(position).getSongUrl());
//                    intent.putExtra("songName", listArtist.get(position).getName());
//                    intent.putExtra("position", position);
//                    context.startActivity(intent);
                    Toast.makeText(context, "Click: " + listArtist.get(position).toString(), Toast.LENGTH_SHORT).show();
                }
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
