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
import com.javateam.muzik.entity.Album;
import com.javateam.muzik.entity.Artist;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThumbnailListDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThumbnailListDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private Bundle bundle;

    private TextView name;
    private TextView numSong;
    private TextView description;

    public ThumbnailListDetailFragment() {
        // Required empty public constructor
    }

    public ThumbnailListDetailFragment(Bundle bundle) {
        this.bundle = bundle;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThumbnailListDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThumbnailListDetailFragment newInstance(String param1, String param2) {
        ThumbnailListDetailFragment fragment = new ThumbnailListDetailFragment();
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
        view = inflater.inflate(R.layout.fragment_thumbnail_list_detail, container, false);
        prepareData();
        return view;
    }

    private void prepareData() {
        name = view.findViewById(R.id.tldf_name);
        numSong = view.findViewById(R.id.tldf_num_song);
        description = view.findViewById(R.id.tldf_description);

        if (bundle.getString("type").equals("album")) {
            Album album = (Album) bundle.getSerializable("album");
            name.setText(album.getName());
            numSong.setText(album.getSongs().size() + " bài hát");
            description.setText(album.getDescription());
        } else {
            Artist artist = (Artist) bundle.getSerializable("artist");
            name.setText(artist.getName());
            numSong.setText(artist.getSongs().size() + " bài hát");
            description.setText(artist.getDescription());
        }
    }


}