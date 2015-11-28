package com.ryblade.openbikebcn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryblade.openbikebcn.Model.Station;

import java.util.ArrayList;

public class StationArrayAdapter extends ArrayAdapter<String> {
  private final Context context;
  private final ArrayList<Station> stations;

  public StationArrayAdapter(Context context, ArrayList<Station> stations) {
    super(context, R.layout.station_fav_item);
    this.context = context;
    this.stations = stations;
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

    public static PlaceHolder generate(View convertView) {
      PlaceHolder placeHolder = new PlaceHolder();
      placeHolder.id = (TextView) convertView.findViewById(R.id.stationId);
      placeHolder.address = (TextView) convertView.findViewById(R.id.stationAddress);
      placeHolder.bikes = (TextView) convertView.findViewById(R.id.stationBikes);
      placeHolder.slots = (TextView) convertView.findViewById(R.id.stationSlots);
      return placeHolder;
    }

  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    PlaceHolder placeHolder;
    if (convertView == null) {
      convertView = View.inflate(context, R.layout.station_fav_item, null);
      placeHolder = PlaceHolder.generate(convertView);
      convertView.setTag(placeHolder);
    } else {
      placeHolder = (PlaceHolder) convertView.getTag();
    }

    Station station = stations.get(position);

    placeHolder.id.setText(station.getId());
    placeHolder.address.setText(String.format("%s, %s", station.getStreetName(), station.getStreetNumber()));
    placeHolder.bikes.setText(String.valueOf(station.getBikes()));
    placeHolder.slots.setText(String.valueOf(station.getSlots()));

    return convertView;
  }
} 