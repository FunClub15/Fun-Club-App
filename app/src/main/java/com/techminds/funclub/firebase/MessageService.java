package com.techminds.funclub.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.techminds.funclub.R;
import com.techminds.funclub.ui.activity.DashBoardActivity;
import com.techminds.funclub.ui.activity.LoginActivity;
import com.techminds.funclub.ui.activity.SignUpActivity;
import com.techminds.funclub.utils.config.SessionManager;
import com.techminds.funclub.utils.config.SharedPrefSocialIcons;

public class MessageService extends FirebaseMessagingService
{
        Context context;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        context = this;
        Intent intent;

        try{

          //  String  title = remoteMessage.getNotification().getTitle();
            String  message = remoteMessage.getNotification().getBody();
            Bitmap bitmap;

            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

            intent = new Intent(this,SignUpActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

            //notificationBuilder.setContentTitle("FunClub");
            notificationBuilder.setContentText(message);
             notificationBuilder.setLargeIcon(largeIcon);


            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // only for gingerbread and newer versions

                notificationBuilder.setSmallIcon(R.drawable.appicontransparent);


            }else {

                notificationBuilder.setSmallIcon(R.drawable.appicon);

            }


          //  notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));

            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSound(defaultSoundUri);
            notificationBuilder.setContentIntent(pendingIntent);



            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0,notificationBuilder.build());





        }catch (Exception e){
            e.printStackTrace();
        }




    }

//    private void sendNotification(String messageBody) {
////        Intent intent = new Intent(this, ActivitySplash.class);
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, new Intent(),
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(getResources().getString(R.string.app_name))
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }

}
