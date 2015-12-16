package com.ryblade.openbikebcn.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ryblade.openbikebcn.Model.Station;
import com.ryblade.openbikebcn.R;
import com.ryblade.openbikebcn.Utils;

import java.util.ArrayList;

/**
 * Created by Pau on 29/11/2015.
 */
public class ListProvider implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<Station> stations = new ArrayList();
    private Context context = null;
    private int appWidgetId;

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        populateList();
    }

    private void populateList() {
        //stations = Utils.getInstance().getFavouriteStations(context);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return stations.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_row_layout);
        Station station = stations.get(position);
        remoteView.setTextViewText(R.id.stationId, String.valueOf(station.getId()));
        remoteView.setTextViewText(R.id.stationAddress, station.getStreetName() + ", "+ station.getStreetNumber());
        remoteView.setTextViewText(R.id.stationBikes, String.valueOf(station.getBikes()));
        remoteView.setTextViewText(R.id.stationSlots, String.valueOf(station.getSlots()));

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return stations.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
