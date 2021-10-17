package com.javateam.muzik.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.javateam.muzik.R;
import com.javateam.muzik.adapter.AlbumListAdapter;
import com.javateam.muzik.entity.Album;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private ShimmerFrameLayout shimmerViewContainer;

    public AlbumsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlbumsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumsFragment newInstance(String param1, String param2) {
        AlbumsFragment fragment = new AlbumsFragment();
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
        view = inflater.inflate(R.layout.fragment_albums, container, false);

        shimmerViewContainer = view.findViewById(R.id.shimmer_album);
        prepareData();
        return view;
    }

    private void prepareData() {

        RecyclerView recyclerViewAlbum = view.findViewById(R.id.rcv_album);
        Bundle bundle = getArguments();
        List<Album> listAlbum = (List<Album>) bundle.getSerializable("list_album");

        recyclerViewAlbum.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerViewAlbum.setItemAnimator(new DefaultItemAnimator());
        AlbumListAdapter albumListAdapter = new AlbumListAdapter(view.getContext(), listAlbum);
        recyclerViewAlbum.setAdapter(albumListAdapter);

        TextView albumListTitle = view.findViewById(R.id.tv_rcv_album_header);
        albumListTitle.setText(R.string.title_album_fragment);

        // TODO: handle shimmer when invalid listAlbum data - some error occurred
        if (listAlbum != null) {
            shimmerViewContainer.stopShimmer();
            shimmerViewContainer.setVisibility(View.GONE);
        }
    }
}