package com.javateam.muzik.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.javateam.muzik.R;
import com.javateam.muzik.ThumbnailListActivity;
import com.javateam.muzik.entity.Album;
import com.javateam.muzik.entity.Artist;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThumbnailListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThumbnailListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private Bundle bundle;

    private ImageView thumbnail;
    private TextView name;
    private TextView subtitle;

    public ThumbnailListFragment() {
        // Required empty public constructor
    }

    public ThumbnailListFragment(Bundle bundle) {
        this.bundle = bundle;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThumbnailListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThumbnailListFragment newInstance(String param1, String param2) {
        ThumbnailListFragment fragment = new ThumbnailListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_thumbnail_list, container, false);
        prepareData();
        return view;
    }

    private void prepareData() {
        thumbnail = view.findViewById(R.id.tlf_thumbnail);
        name = view.findViewById(R.id.tlf_name);
        subtitle = view.findViewById(R.id.tlf_subtitle);

        if (bundle.getString(ThumbnailListActivity.IK_TYPE).equals(ThumbnailListActivity.IK_TYPE_ALBUM)) {
            Album album = (Album) bundle.getSerializable(ThumbnailListActivity.IK_TYPE_ALBUM);
            name.setText(album.getName());
            subtitle.setText(album.getSongs().size() + " bài hát");
            loadImg(album.getImgUrl());
        } else {
            Artist artist = (Artist) bundle.getSerializable(ThumbnailListActivity.IK_TYPE_ARTIST);
            name.setText(artist.getName());
            subtitle.setText(artist.getSongs().size() + " bài hát");
            loadImg(artist.getImgUrl());
        }
    }

    private void loadImg(String url) {
        Glide.with(view.getContext())
                .load(url)
                .into(thumbnail);
    }
}