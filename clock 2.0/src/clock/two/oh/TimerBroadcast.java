package clock.two.oh;

import clock.two.oh.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class TimerBroadcast extends BroadcastReceiver {

	public static int ms;
	NotificationCompat.Builder mBuilder;
	public long ct;
	public PendingIntent piMain;
	
	@Override
	public void onReceive(final Context context, Intent intent) {
		this.abortBroadcast();
		final int mID = (int) System.currentTimeMillis();
		final NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotifyManager.cancelAll();
		ms = intent.getExtras().getInt(TimerService.CURRENT_TIME);
		ms = Clock.mClock.mCurrentTime;
		final int max = ms;
		final Intent iMain = new Intent(context, Clock.class);
		final Bundle iMainBundle = new Bundle();
		iMain.putExtra("timerNotifExtra", 1);
		iMain.putExtra("timerNotifExtraCurTime", ms);
		piMain = PendingIntent.getActivity(context, mID, iMain, PendingIntent.FLAG_ONE_SHOT + PendingIntent.FLAG_UPDATE_CURRENT);
//		Intent iPP = new Intent(context, Clock.class);
//		iPP.putExtra("timerNotifExtra", 2);
//		PendingIntent piPP = PendingIntent.getActivity(context, 0, iPP, 0);
//		Intent iStop = new Intent(context, Clock.class);
//		iStop.putExtra("timerNotifExtra", 3);
//		PendingIntent piStop = PendingIntent.getActivity(context, 0, iStop, 0);
		mBuilder = new NotificationCompat.Builder(context)
			.setContentIntent(piMain)
			.setSmallIcon(R.drawable.notif_timer);
//			.addAction(R.drawable.icon, "Pause", piPP)
//			.addAction(R.drawable.icon, "Stop", piStop);
		new Thread(
			new Runnable() {
				@Override
				public void run() {
			        while(ms > 0) {
			        	ct = System.currentTimeMillis();
			        	String headerText = "00:00:00";
			    		int h = ms / 60 / 60 / 1000;
			    		int m = ms / 60 / 1000 - h * 60;
			    		int s = ms / 1000 - h * 60 * 60 - m * 60;
			    		String hstr = Integer.toString(h);
			    		if (hstr.length() < 2)
			    			hstr = "0" + hstr;
			    		String mstr = Integer.toString(m);
			    		if (mstr.length() < 2)
			    			mstr = "0" + mstr;
			    		String sstr = Integer.toString(s);
			    		if (sstr.length() < 2)
			    			sstr = "0" + sstr;
			    		if (!hstr.equals("00"))
			    			headerText = hstr + ":" + mstr + ":" + sstr;
			    		else
			    			headerText = mstr + ":" + sstr;
			    		iMainBundle.putInt("timerNotifExtra", 1);
			    		iMainBundle.putInt("timerNotifExtraCurTime", ms);
			    		iMain.replaceExtras(iMainBundle);
			    		piMain = PendingIntent.getActivity(context, mID, iMain, PendingIntent.FLAG_ONE_SHOT + PendingIntent.FLAG_UPDATE_CURRENT);
		                mBuilder.setProgress(max, ms, false)
		                		.setContentTitle(headerText)
		                		.setContentIntent(piMain);
		                Notification notif = mBuilder.build();
						notif.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_NO_CLEAR;
		                mNotifyManager.notify(mID, notif);
		                try {
		                    Thread.sleep(50);
		                } catch (InterruptedException e) {
		                }
		                ms -= (System.currentTimeMillis() - ct);
			        } if(ms > -1000) {
			        	mBuilder.setContentText("Time's up!")
			        			.setProgress(0, 0, false);
			        	Uri notification = RingtoneManager
								.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
						Ringtone r = RingtoneManager.getRingtone(
								context, notification);
						r.play();
						Notification notif = mBuilder.build();
						notif.flags |= Notification.FLAG_AUTO_CANCEL;
			        	mNotifyManager.notify(mID, notif);
			        } else {
			        	mNotifyManager.cancel(mID);
			        }
				}
			}).start();
	}
}
