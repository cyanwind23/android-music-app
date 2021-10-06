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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.javateam.muzik.R;
import com.javateam.muzik.adapter.AlbumCardAdapter;
import com.javateam.muzik.adapter.ArtistCardAdapter;
import com.javateam.muzik.adapter.JSONParser;
import com.javateam.muzik.adapter.SongListAdapter;
import com.javateam.muzik.config.AppConfig;
import com.javateam.muzik.entity.Album;
import com.javateam.muzik.entity.Artist;
import com.javateam.muzik.entity.Song;
import com.javateam.muzik.service.SongService;
import com.javateam.muzik.singleton.RequestSingleton;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        shimmerViewContainer = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_online_music);

        prepareDataList();
        return view;
    }


    private void prepareDataList() {
        // For Album RCV
        recyclerViewAlbum = (RecyclerView) view.findViewById(R.id.rcv_album);
        listAlbum = new ArrayList<>();

        recyclerViewAlbum.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerViewAlbum.setItemAnimator(new DefaultItemAnimator());
        albumCardAdapter = new AlbumCardAdapter(view.getContext(), listAlbum);
        recyclerViewAlbum.setAdapter(albumCardAdapter);

        albumListTitle = (TextView) view.findViewById(R.id.tv_rcv_album_header);
        albumListTitle.setText(R.string.title_rcv_album);

        // For Artist RCV
        recyclerViewArtist = (RecyclerView) view.findViewById(R.id.rcv_artist);
        listArtist = new ArrayList<>();

        recyclerViewArtist.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerViewArtist.setItemAnimator(new DefaultItemAnimator());
        artistCardAdapter = new ArtistCardAdapter(view.getContext(), listArtist);
        recyclerViewArtist.setAdapter(artistCardAdapter);

        artistListTitle = (TextView) view.findViewById(R.id.tv_rcv_artist_header);
        artistListTitle.setText(R.string.title_rcv_artist);

        // For Song RCV
        recyclerViewSong = (RecyclerView) view.findViewById(R.id.rcv_song);
        listSong = new ArrayList<>();

        recyclerViewSong.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerViewSong.setItemAnimator(new DefaultItemAnimator());
        songListAdapter = new SongListAdapter(view.getContext(), listSong);
        recyclerViewSong.setAdapter(songListAdapter);

        songListTitle = (TextView) view.findViewById(R.id.tv_rcv_song_header);
        songListTitle.setText(R.string.title_rcv_song);

        // Fetch API and display
        getOnlineData();
    }
    private void getOnlineData() {

        getAlbumData();
        getArtistData();
        getSongData();

    }

    private void getAlbumData() {
        StringRequest albumRequest = new StringRequest(Request.Method.GET, AppConfig.ALBUMS_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    listAlbum.addAll(JSONParser.parse(response, Album.class));
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }

                // refreshing recycler view
                albumCardAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                handleVolleyErrorMessage(volleyError);
            }
        });

        RequestSingleton.getInstance(view.getContext()).addToRequestQueue(albumRequest);
    }

    private void getArtistData() {
        StringRequest artistRequest = new StringRequest(Request.Method.GET, AppConfig.ARTISTS_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // TODO: Use map instead of list
                    listArtist.addAll(JSONParser.parse(response, Artist.class));
                } catch (Exception jsonException) {
                    jsonException.printStackTrace();
                }

                // refreshing recycler view
                artistCardAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                handleVolleyErrorMessage(volleyError);
            }
        });

        RequestSingleton.getInstance(view.getContext()).addToRequestQueue(artistRequest);
    }
    private void getSongData() {
        StringRequest songRequest = new StringRequest(Request.Method.GET, AppConfig.SONGS_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    listSong.addAll(SongService.parseWithArtists(response, listArtist));
                } catch (Exception jsonException) {
                    jsonException.printStackTrace();
                }

                listSong.size();
                // refreshing recycler view
                songListAdapter.notifyDataSetChanged();

                // stop animating Shimmer and hide the layout
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                handleVolleyErrorMessage(volleyError);
            }
        });

        RequestSingleton.getInstance(view.getContext()).addToRequestQueue(songRequest);
    }
    private void handleVolleyErrorMessage(VolleyError volleyError) {
        // TODO: Messages need to be changed accordingly
        String message = null;
        if (volleyError instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (volleyError instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (volleyError instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }
        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }

}