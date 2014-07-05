package com.example.ufscps;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class Alarm extends Activity {
	public void Alarm (Context context){
	//Define Notification Manager
	NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

	//Define sound URI
	Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
	      //.setSmallIcon(icon)
	        .setContentTitle("title")
	        .setContentText("message")
	        .setSound(soundUri); //This sets the sound to play

	//Display notification
	//notificationManager.notify(0, mBuilder.build());
	}
}