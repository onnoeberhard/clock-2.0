package clock.two.oh;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import clock.two.oh.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class ToolTip extends Activity{

	public static SharedPreferences prefs;
	
	Timer t = new Timer();
	TimerTask tt;
	TextView tv;
	Handler handler = new Handler();
	DateFormat timeFormat;
	DateFormat dateFormat;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		setContentView(R.layout.tooltip);
//		if (prefs.getBoolean("Light_Theme", true)) {
//			((RelativeLayout)findViewById(R.id.mainid)).setBackgroundColor(Color.WHITE);
//		}
		tv = (TextView)findViewById(R.id.zeit);
		tv.setText("time");
		DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());
		String localDatePattern  = ((SimpleDateFormat)dateFormatter).toLocalizedPattern();
		DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.DEFAULT, Locale.getDefault());
		String localTimePattern  = ((SimpleDateFormat)timeFormatter).toLocalizedPattern();
		timeFormat = new SimpleDateFormat(localTimePattern);
		dateFormat = new SimpleDateFormat(localDatePattern);
		timer();
	}
	
	public void timer()
    {
	    tt = new TimerTask() {
	    	public void run() {
	    		handler.post(new Runnable() {
	    			public void run() {
	    		        Date date = new Date();
	    		        String s = (" " + timeFormat.format(date) + " " +  "\n" + " " +  dateFormat.format(date) + " ");
	    				tv.setText(s);
	                }
	    		});
	    	}
	    };
	    t.schedule(tt, 0, 100);
    }

	
	public void close(View v)
	{
		tt.cancel();
		finish();
	}
	
}
