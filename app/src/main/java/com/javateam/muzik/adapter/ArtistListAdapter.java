package com.javateam.muzik.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.javateam.muzik.R;
import com.javateam.muzik.ThumbnailListActivity;
import com.javateam.muzik.entity.Artist;
import com.javateam.muzik.entity.Song;

import java.util.ArrayList;
import java.util.List;

public class ArtistListAdapter extends RecyclerView.Adapter<ListViewHolder> implements Filterable {
    private final Context context;
    private List<Artist> listArtist;
    private final List<Artist> listArtistFull;

    public ArtistListAdapter(Context context, List<Artist> listArtist) {
        this.context = context;
        this.listArtist = listArtist;
        this.listArtistFull = listArtist;
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
        final Artist artist = listArtist.get(position);
        holder.name.setText(artist.getName());
        String subtitle = artist.getSongs().size() + " bài hát";
        holder.subtitle.setText(subtitle);
        holder.name.setSelected(true);
        holder.subtitle.setSelected(true);
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String keyword = charSequence.toString();
                List<Artist> list = new ArrayList<>();

                if (!keyword.isEmpty()) {
                    for (Artist artist : listArtistFull) {
                        if (artist.getName().toLowerCase().contains(keyword.toLowerCase())) {
                            list.add(artist);
                        }
                    }
                } else {
                    list = listArtistFull;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listArtist = (List<Artist>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}