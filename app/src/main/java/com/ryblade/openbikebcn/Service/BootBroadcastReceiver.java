package com.ryblade.openbikebcn.Service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by alexmorral on 15/12/15.
 */
public class BootBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Launch the specified service when this message is received
        Intent startServiceIntent = new Intent(context, UpdateIntentService.class);
        context.startService(startServiceIntent);
    }
}