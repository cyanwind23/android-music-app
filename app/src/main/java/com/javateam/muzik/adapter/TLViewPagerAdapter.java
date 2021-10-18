package com.javateam.muzik.adapter;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.javateam.muzik.ui.fragment.ThumbnailListDetailFragment;
import com.javateam.muzik.ui.fragment.ThumbnailListFragment;

public class TLViewPagerAdapter extends FragmentStateAdapter {
    private final Intent intent;

    public TLViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, Intent intent) {
        super(fragmentActivity);
        this.intent = intent;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = intent.getExtras();
        if (position == 1) {
            return new ThumbnailListDetailFragment(bundle);
        } else {
            return new ThumbnailListFragment(bundle);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
