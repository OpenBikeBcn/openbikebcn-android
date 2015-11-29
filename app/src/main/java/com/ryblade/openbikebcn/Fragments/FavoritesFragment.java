package com.ryblade.openbikebcn.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ryblade.openbikebcn.MainActivity;
import com.ryblade.openbikebcn.Model.Station;
import com.ryblade.openbikebcn.R;
import com.ryblade.openbikebcn.StationArrayAdapter;
import com.ryblade.openbikebcn.Utils;

import java.util.ArrayList;

/**
 * Created by alexmorral on 25/11/15.
 */
public class FavoritesFragment extends Fragment {

    public FavoritesFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorites_fragment, container, false);
        init(rootView);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void init(View v) {
        ListView lv = ((ListView) v.findViewById(R.id.favouritesList));
        ArrayList<Station> stations = Utils.getInstance().getFavouriteStations(getActivity());
        StationArrayAdapter saa = new StationArrayAdapter(getContext(), stations, this);
        lv.setAdapter(saa);
    }
}
