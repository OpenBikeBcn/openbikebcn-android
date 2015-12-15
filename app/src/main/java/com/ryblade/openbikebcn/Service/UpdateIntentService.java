package com.ryblade.openbikebcn.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.ryblade.openbikebcn.FetchAPITask;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UpdateIntentService extends IntentService {



//    public static void startActionAPI(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, UpdateIntentService.class);
//        context.startService(intent);
//    }



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
        new FetchAPITask(this, FetchAPITask.BICING_API_URL).execute();
    }
}
