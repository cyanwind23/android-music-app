package com.javateam.muzik.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.javateam.muzik.Home;
import com.javateam.muzik.R;
import com.javateam.muzik.adapter.ArtistCardAdapter;
import com.javateam.muzik.entity.Artist;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArtistsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private ShimmerFrameLayout shimmerViewContainer;

    private List<Artist> listArtist;
    private TextView artistListTitle;
    private RecyclerView recyclerViewArtist;
    private ArtistCardAdapter artistCardAdapter;

    public ArtistsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArtistsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArtistsFragment newInstance(String param1, String param2) {
        ArtistsFragment fragment = new ArtistsFragment();
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
        view = inflater.inflate(R.layout.fragment_artists, container, false);

        shimmerViewContainer = view.findViewById(R.id.shimmer_artist);
        prepareData();
        return view;
    }

    private void prepareData() {

        recyclerViewArtist = view.findViewById(R.id.rcv_artist);
        Bundle bundle = getArguments();
        listArtist = (List<Artist>) bundle.getSerializable(Home.IK_LIST_ARTIST);

        recyclerViewArtist.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
        recyclerViewArtist.setItemAnimator(new DefaultItemAnimator());
        artistCardAdapter = new ArtistCardAdapter(view.getContext(), listArtist);
        recyclerViewArtist.setAdapter(artistCardAdapter);

        artistListTitle = view.findViewById(R.id.tv_rcv_artist_header);
        artistListTitle.setText(R.string.title_artist_fragment);

        // TODO: handle shimmer when invalid listArtist data - some error occurred
        if (listArtist != null) {
            shimmerViewContainer.stopShimmer();
            shimmerViewContainer.setVisibility(View.GONE);
        }
    }
}