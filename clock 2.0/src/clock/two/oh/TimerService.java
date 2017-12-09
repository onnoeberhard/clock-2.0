package clock.two.oh;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class TimerService extends Service{
	
	public int mFullTime;
	public int mCurrentTime;
	
	public static final String CURRENT_TIME = "current_time";
	
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		mCurrentTime = intent.getExtras().getInt(CURRENT_TIME);
		AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		alarm.set(AlarmManager.RTC_WAKEUP, 0, getPI()); 
	}
	
	public PendingIntent getPI() {
		Intent intent = new Intent(getApplicationContext(), TimerBroadcast.class);
		intent.putExtra(CURRENT_TIME, mCurrentTime);
		PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
		return pi;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
