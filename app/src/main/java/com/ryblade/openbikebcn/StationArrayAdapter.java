package com.ryblade.openbikebcn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryblade.openbikebcn.Fragments.FavoritesFragment;
import com.ryblade.openbikebcn.Model.Station;
import com.ryblade.openbikebcn.data.DBContract;

import java.util.ArrayList;

public class StationArrayAdapter extends ArrayAdapter<String> {
  private final Context context;
  private final ArrayList<Station> stations;
  private FavoritesFragment favoritesFragment;

  public StationArrayAdapter(Context context, ArrayList<Station> stations, FavoritesFragment favoritesFragment) {
    super(context, R.layout.station_fav_item);
    this.context = context;
    this.stations = stations;
    this.favoritesFragment = favoritesFragment;
  }

  @Override
  public int getCount() {
    return stations.size();
  }

  private static class PlaceHolder {

    TextView id;
    TextView address;
    TextView bikes;
    TextView slots;
    ImageButton deleteButton;
    Station station;

    public static PlaceHolder generate(View convertView) {
      PlaceHolder placeHolder = new PlaceHolder();
      placeHolder.id = (TextView) convertView.findViewById(R.id.stationId);
      placeHolder.address = (TextView) convertView.findViewById(R.id.stationAddress);
      placeHolder.bikes = (TextView) convertView.findViewById(R.id.stationBikes);
      placeHolder.slots = (TextView) convertView.findViewById(R.id.stationSlots);
      placeHolder.deleteButton = (ImageButton) convertView.findViewById(R.id.deleteStation);
      return placeHolder;
    }

  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    final PlaceHolder placeHolder;
    if (convertView == null) {
      convertView = View.inflate(context, R.layout.station_fav_item, null);
      placeHolder = PlaceHolder.generate(convertView);
      convertView.setTag(placeHolder);
    } else {
      placeHolder = (PlaceHolder) convertView.getTag();
    }

    final Station station = stations.get(position);

    placeHolder.station = station;
    placeHolder.id.setText(String.valueOf(station.getId()));
    if(station.getStreetNumber().equals(""))
      placeHolder.address.setText(station.getStreetName());
    else
      placeHolder.address.setText(String.format("%s, %s", station.getStreetName(), station.getStreetNumber()));
    placeHolder.bikes.setText(String.valueOf(station.getBikes()));
    placeHolder.slots.setText(String.valueOf(station.getSlots()));
    placeHolder.deleteButton.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Utils.getInstance().deleteFavourite(context, placeHolder.station);
            favoritesFragment.updateFavorites();
            return false;
        }
    });

    return convertView;
  }
} 