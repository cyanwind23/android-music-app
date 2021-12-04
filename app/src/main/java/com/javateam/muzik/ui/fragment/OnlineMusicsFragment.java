package com.javateam.muzik.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.javateam.muzik.Home;
import com.javateam.muzik.R;
import com.javateam.muzik.adapter.AlbumCardAdapter;
import com.javateam.muzik.adapter.ArtistCardAdapter;
import com.javateam.muzik.adapter.SongListAdapter;
import com.javateam.muzik.entity.Album;
import com.javateam.muzik.entity.Artist;
import com.javateam.muzik.entity.Song;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnlineMusicsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnlineMusicsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;
    ShimmerFrameLayout shimmerViewContainer;

    List<Album> listAlbum;
    TextView albumListTitle;
    RecyclerView recyclerViewAlbum;
    AlbumCardAdapter albumCardAdapter;

    List<Artist> listArtist;
    TextView artistListTitle;
    RecyclerView recyclerViewArtist;
    ArtistCardAdapter artistCardAdapter;

    List<Song> listSong;
    TextView songListTitle;
    RecyclerView recyclerViewSong;
    SongListAdapter songListAdapter;

    public OnlineMusicsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnlineMusicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OnlineMusicsFragment newInstance(String param1, String param2) {
        OnlineMusicsFragment fragment = new OnlineMusicsFragment();
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
        view = inflater.inflate(R.layout.fragment_online_musics, container, false);
//        textView = (TextView) view.findViewById(R.id.tv_online_musics);
        shimmerViewContainer = view.findViewById(R.id.shimmer_online_music);

        Bundle bundle = getArguments();
        listAlbum = (List<Album>) bundle.getSerializable(Home.IK_LIST_ALBUM);
        listArtist = (List<Artist>) bundle.getSerializable(Home.IK_LIST_ARTIST);
        listSong = (List<Song>) bundle.getSerializable(Home.IK_LIST_SONG);
        prepareDataList();
        return view;
    }


    private void prepareDataList() {
        // For Album RCV
        recyclerViewAlbum = view.findViewById(R.id.rcv_album);

        recyclerViewAlbum.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerViewAlbum.setItemAnimator(new DefaultItemAnimator());
        albumCardAdapter = new AlbumCardAdapter(view.getContext(), listAlbum);
        recyclerViewAlbum.setAdapter(albumCardAdapter);

        albumListTitle = view.findViewById(R.id.tv_rcv_album_header);
        albumListTitle.setText(R.string.title_rcv_album);

        // For Artist RCV
        recyclerViewArtist = view.findViewById(R.id.rcv_artist);
//        listArtist = new ArrayList<>();

        recyclerViewArtist.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerViewArtist.setItemAnimator(new DefaultItemAnimator());
        artistCardAdapter = new ArtistCardAdapter(view.getContext(), listArtist);
        recyclerViewArtist.setAdapter(artistCardAdapter);

        artistListTitle = view.findViewById(R.id.tv_rcv_artist_header);
        artistListTitle.setText(R.string.title_rcv_artist);

        // For Song RCV
        recyclerViewSong = view.findViewById(R.id.rcv_song);
//        listSong = new ArrayList<>();

        recyclerViewSong.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerViewSong.setItemAnimator(new DefaultItemAnimator());
        songListAdapter = new SongListAdapter(view.getContext(), listSong);
        recyclerViewSong.setAdapter(songListAdapter);

        songListTitle = view.findViewById(R.id.tv_rcv_song_header);
        songListTitle.setText(R.string.title_rcv_song);

        // stop animating Shimmer and hide the layout
        if (listSong != null && listArtist != null && listAlbum != null) {
            shimmerViewContainer.stopShimmer();
            shimmerViewContainer.setVisibility(View.GONE);
        }

    }

}