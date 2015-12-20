/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ryblade.openbikebcn.Wearable;

import android.app.Notification;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.ArrayAdapter;

import com.ryblade.openbikebcn.Model.Station;
import com.ryblade.openbikebcn.R;

import java.util.ArrayList;

/**
 * Base class for notification preset generators.
 */
public class NotificationPreset extends NamedPreset {
    public final int titleResId;
    public final int textResId;
    private ArrayList<Station> stations;

    public NotificationPreset(int nameResId, int titleResId, int textResId, ArrayList<Station> stations) {
        super(nameResId);
        this.titleResId = titleResId;
        this.textResId = textResId;
        this.stations = stations;
    }

    public static class BuildOptions {
        public final CharSequence titlePreset;
        public final CharSequence textPreset;
        public final PriorityPreset priorityPreset;
        public final ActionsPreset actionsPreset;
        public final boolean includeLargeIcon;
        public final boolean isLocalOnly;
        public final boolean hasContentIntent;
        public final boolean vibrate;

        public BuildOptions(CharSequence titlePreset, CharSequence textPreset,
                PriorityPreset priorityPreset, ActionsPreset actionsPreset,
                boolean includeLargeIcon, boolean isLocalOnly, boolean hasContentIntent,
                boolean vibrate) {
            this.titlePreset = titlePreset;
            this.textPreset = textPreset;
            this.priorityPreset = priorityPreset;
            this.actionsPreset = actionsPreset;
            this.includeLargeIcon = includeLargeIcon;
            this.isLocalOnly = isLocalOnly;
            this.hasContentIntent = hasContentIntent;
            this.vibrate = vibrate;
        }
    }

    /** Build a notification with this preset and the provided options */
    public Notification[] buildNotifications(Context context, BuildOptions options){
        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        for(int i = 0; i < stations.size(); ++i) {
            Station station = stations.get(i);
            if(station.getStreetNumber().equals(""))
                style.addLine(station.getStreetName());
            else
                style.addLine(station.getStreetName() + ", " + station.getStreetNumber());
            style.addLine("Bikes: " + station.getBikes());
            style.addLine("Slots: " + station.getSlots());
            if(i < stations.size()-1)
                style.addLine("-----------------");
        }
        style.setBigContentTitle(context.getString(R.string.app_name));
        // style.setSummaryText(context.getString(R.string.app_name));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setStyle(style);
        NotificationCompat.WearableExtender wearableOptions =
                new NotificationCompat.WearableExtender();
        applyBasicOptions(context, builder, wearableOptions, options);
        builder.extend(wearableOptions);
        return new Notification[] { builder.build() };
    }

    /** Whether actions are required to use this preset. */
    public boolean actionsRequired() {
        return false;
    }

    /** Number of background pickers required */
    public int countBackgroundPickersRequired() {
        return 0;
    }

    private static NotificationCompat.Builder applyBasicOptions(Context context,
                                                                NotificationCompat.Builder builder, NotificationCompat.WearableExtender wearableOptions,
                                                                NotificationPreset.BuildOptions options) {
        builder.setContentTitle(options.titlePreset)
                .setContentText(options.textPreset)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDeleteIntent(NotificationUtil.getExamplePendingIntent(
                        context, /*R.string.example_notification_deleted*/R.string.action_settings));
        options.actionsPreset.apply(context, builder, wearableOptions);
        options.priorityPreset.apply(builder, wearableOptions);
        if (options.includeLargeIcon) {
            builder.setLargeIcon(BitmapFactory.decodeResource(
                    context.getResources(), R.drawable.previous));
        }
        if (options.isLocalOnly) {
            builder.setLocalOnly(true);
        }
        if (options.hasContentIntent) {
            builder.setContentIntent(NotificationUtil.getExamplePendingIntent(context,
                    /*R.string.content_intent_clicked*/R.string.action_settings));
        }
        if (options.vibrate) {
            builder.setVibrate(new long[] {0, 100, 50, 100} );
        }
        return builder;
    }
}
