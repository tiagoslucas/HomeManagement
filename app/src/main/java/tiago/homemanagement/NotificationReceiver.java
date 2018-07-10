package tiago.homemanagement;

import android.content.Intent;
import android.content.Context;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(100, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        String notificationText = "";
        switch (intent.getIntExtra("activity",0)){
            case MainActivity.DISHES_SETTID:
                notificationText = context.getString(R.string.dishes_notification);
                break;
            case MainActivity.FLOOR_SETTID:
                notificationText = context.getString(R.string.floor_notification);
                break;
            case MainActivity.LAUNDRY_SETTID:
                notificationText = context.getString(R.string.laundry_notification);
                break;
        }

        Notification notification = builder.setContentTitle(context.getString(R.string.notification_title))
                .setContentText(notificationText)
                .setSmallIcon(R.drawable.homemanagement)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}