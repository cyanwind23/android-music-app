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
import com.javateam.muzik.entity.Album;
import com.javateam.muzik.entity.Song;

import java.util.ArrayList;
import java.util.List;

public class AlbumListAdapter extends RecyclerView.Adapter<ListViewHolder> implements Filterable {
    private final Context context;
    private List<Album> listAlbum;
    private final List<Album> listAlbumFull;

    public AlbumListAdapter(Context context, List<Album> listAlbum) {
        this.context = context;
        this.listAlbum = listAlbum;
        this.listAlbumFull = listAlbum;
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
        final Album album = listAlbum.get(position);
        holder.name.setText(album.getName());
        String subtitle = album.getSongs().size() + " bài hát";
        holder.subtitle.setText(subtitle);
        holder.name.setSelected(true);
        holder.subtitle.setSelected(true);
        Glide.with(context)
                .load(album.getImgUrl())
                .into(holder.thumbnail);

        holder.setItemClickListener((view, position1, isLongClick) -> {
            if (isLongClick) {
                Toast.makeText(context, "Long click:" + listAlbum.get(position1).toString(), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(context, ThumbnailListActivity.class);
                intent.putExtra(ThumbnailListActivity.IK_TYPE, ThumbnailListActivity.IK_TYPE_ALBUM);
                intent.putExtra(ThumbnailListActivity.IK_TYPE_ALBUM, listAlbum.get(position));
                context.startActivity(intent);
//                Toast.makeText(context, "Click:" + listAlbum.get(position1).toString(), Toast.LENGTH_SHORT).show();
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String keyword = charSequence.toString();
                List<Album> list = new ArrayList<>();

                if (!keyword.isEmpty()) {
                    for (Album album : listAlbumFull) {
                        if (album.getName().toLowerCase().contains(keyword.toLowerCase())) {
                            list.add(album);
                        }
                    }
                } else {
                    list = listAlbumFull;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listAlbum = (List<Album>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}