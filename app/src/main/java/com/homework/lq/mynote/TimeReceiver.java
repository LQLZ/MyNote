package com.homework.lq.mynote;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;

/**
 * Created by ASUS on 2018/6/4.
 */

public class TimeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        context.startService(new Intent(context,NotifyService.class));
    }
}

