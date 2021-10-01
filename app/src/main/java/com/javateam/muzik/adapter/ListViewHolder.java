package com.javateam.muzik.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.javateam.muzik.R;
import com.javateam.muzik.listener.ItemClickListener;

public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public TextView name;
    public TextView subtitle;
    public ImageView thumbnail;

    private ItemClickListener itemClickListener;

    public ListViewHolder(View view) {
        super(view);
        name = view.findViewById(R.id.tv_list_name);
        subtitle = view.findViewById(R.id.tv_list_subtitle);
        thumbnail = view.findViewById(R.id.iv_list_thumbnail);

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getBindingAdapterPosition(), false);
    }

    @Override
    public boolean onLongClick(View view) {
        itemClickListener.onClick(view, getBindingAdapterPosition(), true);
        return true;
    }
}