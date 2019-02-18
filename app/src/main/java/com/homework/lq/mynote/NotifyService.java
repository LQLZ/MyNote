package com.homework.lq.mynote;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ASUS on 2018/6/4.
 */

public class NotifyService extends Service {
    private NotificationManager notificationManager;
    private boolean isRec = false;
    private boolean isFirst = true;
    private String ACTION = Intent.ACTION_TIME_CHANGED;
    private IntentFilter ifter;
    private TimeReceiver receiver;
    private MyDatabaseHelper dbHelper;
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        dbHelper = new MyDatabaseHelper(this,"MyNote.db",null,2);

        Log.d("NotifyService", "onCreate executed");
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        ifter = new IntentFilter();
        ifter.addAction(ACTION);
        receiver = new TimeReceiver();
        if (isRec == false) {
            registerReceiver(receiver, ifter);
            isRec = true;
        }

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("NotifyService", "onDestroy executed");
        unregisterReceiver(receiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("NotifyService", "onStartCommand executed");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       // String curTime = sdf.format(new Date());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("note", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {

                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                String jutitime = hour+"时"+minute+"分";
                Log.d("NotifyService ",jutitime);


                String curTime = sdf.format(new Date());
                Log.d("NotifyService", curTime);
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                Log.d("NotifyService ",time);
                String content = cursor.getString(cursor.getColumnIndex("content"));
                if (curTime.equals(date) && time.equals(jutitime)) {
                    String text = content;
                    PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
                    Notification notification = new NotificationCompat.Builder(this)
                            .setContentTitle(date)
                            .setContentText(text)
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                    R.mipmap.ic_launcher))
                            .setContentIntent(pi)
                            .build();
                    startForeground(1, notification);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return super.onStartCommand(intent, flags, startId);
    }
}
