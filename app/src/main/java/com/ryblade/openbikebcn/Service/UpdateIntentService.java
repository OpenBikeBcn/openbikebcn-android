package com.ryblade.openbikebcn.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.ryblade.openbikebcn.FetchAPITask;
import com.ryblade.openbikebcn.Utils;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UpdateIntentService extends IntentService implements OnLoadStations{


    public UpdateIntentService() {
        super("UpdateIntentService");
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

            //send notification!!

        }
    }
}
