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

package com.example.openbikebcnwearable;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

/**
 * Base class for notification preset generators.
 */
public class NotificationPreset {
    public final int nameResId;

    public NotificationPreset(int nameResId) {
        this.nameResId = nameResId;
    }

    public Notification buildNotification(Context context) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);

        Notification page2 = buildBasicNotification(context)
                .extend(new Notification.WearableExtender()
                        .setHintShowBackgroundOnly(true)
                        .setBackground(BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.common_google_signin_btn_icon_dark)))
                .build();

        Notification page3 = buildBasicNotification(context)
                .setContentTitle(/*context.getString(R.string.third_page)*/"Hola5")
                .setContentText(null)
                .extend(new Notification.WearableExtender()
                        .setContentAction(0 /* action A */))
                .build();

        SpannableStringBuilder choice2 = new SpannableStringBuilder(
                "This choice is best");
        choice2.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 5, 11, 0);

        return buildBasicNotification(context)
                .extend(new Notification.WearableExtender()
                        .addAction(new Notification.Action(R.mipmap.ic_launcher,
                                /*context.getString(R.string.action_a)*/"Action A", pendingIntent))
                        .addAction(new Notification.Action.Builder(R.mipmap.ic_launcher,
                                /*context.getString(R.string.reply)*/"Reply", pendingIntent)
                                .addRemoteInput(new RemoteInput.Builder(MainActivity.KEY_REPLY)
                                        .setChoices(new CharSequence[] {
                                                /*context.getString(R.string.choice_1)*/"Choice1",
                                                choice2 })
                                        .build())
                                .build())
                        .addPage(page2)
                        .addPage(page3))
                .build();
    }

    private static Notification.Builder buildBasicNotification(Context context) {
        return new Notification.Builder(context)
                .setContentTitle(/*context.getString(R.string.example_content_title)*/"Hola1")
                .setContentText(/*context.getString(R.string.example_content_text)*/"Hola2")
                        // Set a content intent to return to this sample
                .setContentIntent(PendingIntent.getActivity(context, 0,
                        new Intent(context, MainActivity.class), 0))
                .setSmallIcon(R.mipmap.ic_launcher);
    }
}
