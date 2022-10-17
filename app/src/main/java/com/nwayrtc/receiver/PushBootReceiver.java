package com.nwayrtc.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class PushBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent)
	{
		/* 自动启动Service */
		//PhoneService.startService(context);

		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			Log.i("PushBootReceiver", "手机开机了!");

			Intent intent1 = new Intent(context, Alarmreceiver.class);
			intent1.setAction("nwayrtc.alarm.action");
			PendingIntent sender = PendingIntent.getBroadcast(context, 0,
					intent1, 0);
			long firstime = SystemClock.elapsedRealtime();
			AlarmManager am = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);

			// 10秒一个周期，不停的发送广播
			am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime,
					10 * 1000, sender);
		} else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
			Log.e("PushBootReceiver", "系统正在关闭!");
		} else if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
			Log.i("PushBootReceiver", "新安装了应用程序!");
		} else if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
			Log.e("PushBootReceiver", "用户解锁了手机!");
            Intent intent1 = new Intent(context, Alarmreceiver.class);
            intent1.setAction("nwayrtc.alarm.action");
            PendingIntent sender = PendingIntent.getBroadcast(context, 0,
                    intent1, 0);
            long firstime = SystemClock.elapsedRealtime();
            AlarmManager am = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);

            // 10秒一个周期，不停的发送广播
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime,
                    10 * 1000, sender);
		}
	}
}
