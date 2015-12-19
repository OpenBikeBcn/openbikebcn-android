package com.ryblade.openbikebcn.Service;

import android.app.IntentService;
import android.app.Notification;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;

import com.ryblade.openbikebcn.FetchAPITask;
import com.ryblade.openbikebcn.Model.Station;
import com.ryblade.openbikebcn.R;
import com.ryblade.openbikebcn.Utils;
import com.ryblade.openbikebcn.Wearable.ActionsPreset;
import com.ryblade.openbikebcn.Wearable.NotificationIntentReceiver;
import com.ryblade.openbikebcn.Wearable.NotificationPreset;
import com.ryblade.openbikebcn.Wearable.PriorityPreset;
import com.ryblade.openbikebcn.Widget.StationWidgetProvider;
import com.ryblade.openbikebcn.Widget.WidgetService;

import java.util.ArrayList;
import java.util.Date;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UpdateIntentService extends IntentService implements OnLoadStations, Handler.Callback {

    private static final int MSG_POST_NOTIFICATIONS = 0;
    private static final long POST_NOTIFICATIONS_DELAY_MS = 200;

    private int postedNotificationCount = 0;
    private Handler mHandler;

    public UpdateIntentService() {
        super("UpdateIntentService");
        mHandler = new Handler(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Log.i("MyTestService", "Service running");
            handleFetchFromAPI();
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void handleFetchFromAPI() {
        new FetchAPITask(this, FetchAPITask.BICING_API_URL, this).execute();
    }

    public void OnStationsLoaded() {

        if (Utils.getInstance().appInBackground) {
            updateNotifications(false, Utils.getInstance().getNotificationStations(this));
        }
        updateWidget();
    }

    private void updateWidget() {
        ComponentName thisWidget = new ComponentName(this, StationWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        int appWidgetIds [] = manager.getAppWidgetIds(thisWidget);

//        for (int i = 0; i < appWidgetIds.length; ++i) {
//            RemoteViews remoteViews = updateWidgetListView(this, appWidgetIds[i]);
//            manager.updateAppWidget(appWidgetIds[i], remoteViews);
//        }

        manager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.favouritesWidgetList);
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_layout);

        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, WidgetService.class);
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(R.id.favouritesWidgetList, svcIntent);
        //setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.favouritesWidgetList, R.id.empty_view);
        return remoteViews;
    }

    private void updateNotifications(boolean cancelExisting, ArrayList<Station> stations) {
        // Disable messages to skip notification deleted messages during cancel.
        sendBroadcast(new Intent(NotificationIntentReceiver.ACTION_DISABLE_MESSAGES)
                .setClass(this, NotificationIntentReceiver.class));

        if (cancelExisting) {
            // Cancel all existing notifications to trigger fresh-posting behavior: For example,
            // switching from HIGH to LOW priority does not cause a reordering in Notification Shade.
            NotificationManagerCompat.from(this).cancelAll();
            postedNotificationCount = 0;

            // Post the updated notifications on a delay to avoid a cancel+post race condition
            // with notification manager.
            mHandler.removeMessages(MSG_POST_NOTIFICATIONS);
            mHandler.sendEmptyMessageDelayed(MSG_POST_NOTIFICATIONS, POST_NOTIFICATIONS_DELAY_MS);
        } else {
            postNotifications(stations);
        }
    }

    /**
     * Post the sample notification(s) using current options.
     */
    private void postNotifications(ArrayList<Station> stations) {
        sendBroadcast(new Intent(NotificationIntentReceiver.ACTION_ENABLE_MESSAGES)
                .setClass(this, NotificationIntentReceiver.class));

        NotificationPreset preset = new NotificationPreset(0,0,0,stations);
        CharSequence titlePreset = "OpenBikeBcn";
        CharSequence textPreset = "";
        PriorityPreset priorityPreset = new PriorityPreset(R.string.addToFavourites);
        ActionsPreset actionsPreset = new ActionsPreset(R.string.app_name);
        NotificationPreset.BuildOptions options = new NotificationPreset.BuildOptions(
                titlePreset,
                textPreset,
                priorityPreset,
                actionsPreset,
                false,
                false,
                true,
                true);
        Notification[] notifications = preset.buildNotifications(this, options);

        // Post new notifications
        for (int i = 0; i < notifications.length; i++) {
            NotificationManagerCompat.from(this).notify(i, notifications[i]);
        }
        // Cancel any that are beyond the current count.
        for (int i = notifications.length; i < postedNotificationCount; i++) {
            NotificationManagerCompat.from(this).cancel(i);
        }
        postedNotificationCount = notifications.length;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_POST_NOTIFICATIONS:
//                postNotifications();
                return true;
        }
        return false;
    }
}
