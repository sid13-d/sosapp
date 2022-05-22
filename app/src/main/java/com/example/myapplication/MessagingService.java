package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

public class MessagingService extends FirebaseMessagingService {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Uri soundUri = Uri.parse("android.resource://"+getApplicationContext().getPackageName() + "/" + R.raw.sound);
        Map<String, String> data = message.getData();

        String location=data.get("location");
        String latitude = data.get("lat");
        String longitude = data.get("long");

        String goeUri = "http://maps.google.com/maps?q="+latitude+","+longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(goeUri));

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        String channelId = "Default";
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(message.getNotification().getTitle())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message.getNotification().getBody()))
//                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" +getApplicationContext().getPackageName()+"/"+R.raw.sound))
                .setContentText(message.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_HIGH);
            Vibrator v =(Vibrator) this.getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            v.vibrate(2000);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel.setSound(soundUri, audioAttributes);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                try {
                    CameraManager cam = (CameraManager) getSystemService(CAMERA_SERVICE);
                    for (int i=0; i<5; i++){
                        cam.setTorchMode("0", true);
                        Thread.sleep(500);
                        cam.setTorchMode("0", false);
                        Thread.sleep(500);
                    }
                } catch (CameraAccessException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}