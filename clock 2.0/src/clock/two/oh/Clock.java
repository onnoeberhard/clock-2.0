package clock.two.oh;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import clock.two.oh.R;
import clock.two.oh.cube.Cube;
import clock.two.oh.cube.GLColor;
import clock.two.oh.cube.GLShape;
import clock.two.oh.cube.GLWorld;
import clock.two.oh.cube.KubeRenderer;
import clock.two.oh.cube.Layer;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

import de.passsy.holocircularprogressbar.HoloCircularProgressBar;

public class Clock extends Activity implements KubeRenderer.AnimationCallback {

	public static SharedPreferences prefs;

	public boolean timering = false;
	public boolean lightTheme = false;

	public boolean wfced = false;
	
	public Timer t;
	public TimerTask tt;

	public Timer t2;
	public TimerTask tt2;

	public static Clock mClock;

	public RelativeLayout mainView;
	public LinearLayout clockView;
	public RelativeLayout timerView;
	public LinearLayout timerInput;
	public LinearLayout timerMainView;
	public RelativeLayout timerVisualMain;

	public int rotateTimes;

	public HoloCircularProgressBar timerProgress;
	public TextView timerTV;
	public ObjectAnimator progressBarAnimator;
	
	public Bundle extras;
	
	public int mFullTime;
	public int mCurrentTime;
	public int totalCubeRotations;
	public int doneCubeRotations;
	public ArrayList<Integer> rotatedLayers = new ArrayList<Integer>();

	public int _hour, _min, _ampm;

	public TimeView[] timeViews = new TimeView[27];
	public TimeView almost, around, shortly, five1, ten1, quarter, twenty,
			half, to, past, before, after, one, two, three, four, five, six,
			seven, eight, nine, ten, eleven, twelve, oclock, am, pm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(null);
		wfced = false;
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if (prefs.getBoolean("Light_Theme", true)) {
			setTheme(R.style.themeLight);
			lightTheme = true;
		}
		setContentView(R.layout.main);
		mClock = this;
		mainView = (RelativeLayout) findViewById(R.id.mainView);
		mainView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mainDialog();
			}
		});
		if (prefs.getBoolean("Light_Theme", true) && android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			mainView.setBackgroundColor(Color.WHITE);
		} else if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			((TextView)findViewById(R.id.ti_text)).setTextColor(Color.WHITE);
			((TextView)findViewById(R.id.ti_1)).setTextColor(Color.WHITE);
			((TextView)findViewById(R.id.ti_2)).setTextColor(Color.WHITE);
			((TextView)findViewById(R.id.ti_3)).setTextColor(Color.WHITE);
			((TextView)findViewById(R.id.ti_4)).setTextColor(Color.WHITE);
			((TextView)findViewById(R.id.ti_5)).setTextColor(Color.WHITE);
			((TextView)findViewById(R.id.ti_6)).setTextColor(Color.WHITE);
			((TextView)findViewById(R.id.ti_7)).setTextColor(Color.WHITE);
			((TextView)findViewById(R.id.ti_8)).setTextColor(Color.WHITE);
			((TextView)findViewById(R.id.ti_9)).setTextColor(Color.WHITE);
			((TextView)findViewById(R.id.ti_0)).setTextColor(Color.WHITE);
			((TextView)findViewById(R.id.ti_del)).setTextColor(Color.WHITE);
			((TextView)findViewById(R.id.ti_start)).setTextColor(Color.WHITE);
		}
		clockView = (LinearLayout) findViewById(R.id.clockView);
		timerView = (RelativeLayout) findViewById(R.id.timerView);
		timerInput = (LinearLayout) findViewById(R.id.timerInput);
		timerMainView = (LinearLayout) findViewById(R.id.timerMainView);
		timerVisualMain = (RelativeLayout) findViewById(R.id.timerVisualMain);
		timerProgress = (HoloCircularProgressBar) findViewById(R.id.timerProgress);
		timerTV = (TextView) findViewById(R.id.timerTV);
		timeViews[0] = (TimeView) findViewById(R.id.almost);
		almost = timeViews[0];
		timeViews[1] = (TimeView) findViewById(R.id.around);
		around = timeViews[1];
		timeViews[2] = (TimeView) findViewById(R.id.shortly);
		shortly = timeViews[2];
		timeViews[3] = (TimeView) findViewById(R.id.five1);
		five1 = timeViews[3];
		timeViews[4] = (TimeView) findViewById(R.id.ten1);
		ten1 = timeViews[4];
		timeViews[5] = (TimeView) findViewById(R.id.quarter);
		quarter = timeViews[5];
		timeViews[6] = (TimeView) findViewById(R.id.twenty);
		twenty = timeViews[6];
		timeViews[7] = (TimeView) findViewById(R.id.half);
		half = timeViews[7];
		timeViews[8] = (TimeView) findViewById(R.id.to);
		to = timeViews[8];
		timeViews[9] = (TimeView) findViewById(R.id.past);
		past = timeViews[9];
		timeViews[10] = (TimeView) findViewById(R.id.before);
		before = timeViews[10];
		timeViews[11] = (TimeView) findViewById(R.id.after);
		after = timeViews[11];
		timeViews[12] = (TimeView) findViewById(R.id.one);
		one = timeViews[12];
		timeViews[13] = (TimeView) findViewById(R.id.two);
		two = timeViews[13];
		timeViews[14] = (TimeView) findViewById(R.id.three);
		three = timeViews[14];
		timeViews[15] = (TimeView) findViewById(R.id.four);
		four = timeViews[15];
		timeViews[16] = (TimeView) findViewById(R.id.five);
		five = timeViews[16];
		timeViews[17] = (TimeView) findViewById(R.id.six);
		six = timeViews[17];
		timeViews[18] = (TimeView) findViewById(R.id.seven);
		seven = timeViews[18];
		timeViews[19] = (TimeView) findViewById(R.id.eight);
		eight = timeViews[19];
		timeViews[20] = (TimeView) findViewById(R.id.nine);
		nine = timeViews[20];
		timeViews[21] = (TimeView) findViewById(R.id.ten);
		ten = timeViews[21];
		timeViews[22] = (TimeView) findViewById(R.id.eleven);
		eleven = timeViews[22];
		timeViews[23] = (TimeView) findViewById(R.id.twelve);
		twelve = timeViews[23];
		timeViews[24] = (TimeView) findViewById(R.id.oclock);
		oclock = timeViews[24];
		timeViews[25] = (TimeView) findViewById(R.id.am);
		am = timeViews[25];
		timeViews[26] = (TimeView) findViewById(R.id.pm);
		pm = timeViews[26];
		pm.last_one = true;
		extras = getIntent().getExtras();
	}

	public void mainDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setItems(R.array.mainDialog,
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if (which == 0)
						clock();
					if (which == 1)
						timer();
					if (which == 2)
						settings();
				}
			}).show();
	}

	public void clock() {
		clockView.setVisibility(View.VISIBLE);
		timerView.setVisibility(View.GONE);
		if(mView != null)
			((View)mView).setVisibility(View.GONE);
	}

	int i_for_ti;
	String mTimerTime = "00:00:00";

	public void timer() {
		i_for_ti = 0;
		timerView.setVisibility(View.VISIBLE);
		clockView.setVisibility(View.GONE);
		if (!timering) {
			mTimerTime = "00:00:00";
			rotateTimes = totalCubeRotations = doneCubeRotations = mFullTime = mCurrentTime = 0;
			rotatedLayers.clear();
			timerMainView.setVisibility(View.GONE);
			timerInput.setVisibility(View.VISIBLE);
			if(mView != null)
				((View)mView).setVisibility(View.GONE);
			final TextView tv = (TextView) findViewById(R.id.ti_text);
			final Button[] ti_b = new Button[10];
			ti_b[0] = (Button) findViewById(R.id.ti_0);
			ti_b[1] = (Button) findViewById(R.id.ti_1);
			ti_b[2] = (Button) findViewById(R.id.ti_2);
			ti_b[3] = (Button) findViewById(R.id.ti_3);
			ti_b[4] = (Button) findViewById(R.id.ti_4);
			ti_b[5] = (Button) findViewById(R.id.ti_5);
			ti_b[6] = (Button) findViewById(R.id.ti_6);
			ti_b[7] = (Button) findViewById(R.id.ti_7);
			ti_b[8] = (Button) findViewById(R.id.ti_8);
			ti_b[9] = (Button) findViewById(R.id.ti_9);
			Button ti_del = (Button) findViewById(R.id.ti_del);
			ti_del.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					mTimerTime = "00:00:00";
					tv.setText(mTimerTime);
				}
			});
			Button ti_start = (Button) findViewById(R.id.ti_start);
			ti_start.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					int msh = Integer.parseInt(mTimerTime.split(":")[0]) * 60 * 60 * 1000;
					int msm = Integer.parseInt(mTimerTime.split(":")[1]) * 60 * 1000;
					int mss = Integer.parseInt(mTimerTime.split(":")[2]) * 1000;
					int ms = msh + msm + mss;
					totalCubeRotations = ms / 1000 * 2 - ms / 1000 / 30;
					startTimering(ms);
				}
			});
			for (i_for_ti = 0; i_for_ti < ti_b.length; i_for_ti++) {
				ti_b[i_for_ti].setTextSize(oclock.tv.getTextSize());
				ti_b[i_for_ti]
						.setOnLongClickListener(new OnLongClickListener() {
							public boolean onLongClick(View v) {
								mainDialog();
								return true;
							}
						});
				ti_b[i_for_ti].setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (mTimerTime.split(":")[0].toCharArray()[0] == '0') {
							String s0 = String.valueOf(mTimerTime.split(":")[0]
									.toCharArray()[1]);
							String s1 = String.valueOf(mTimerTime.split(":")[1]
									.toCharArray()[0]);
							String s2 = String.valueOf(mTimerTime.split(":")[1]
									.toCharArray()[1]);
							String s3 = String.valueOf(mTimerTime.split(":")[2]
									.toCharArray()[0]);
							String s4 = String.valueOf(mTimerTime.split(":")[2]
									.toCharArray()[1]);
							String s5 = String.valueOf(((Button) v).getText()
									.toString().toCharArray()[0]);
							mTimerTime = s0 + s1 + ":" + s2 + s3 + ":" + s4
									+ s5;
						}
						tv.setText(mTimerTime);
					}
				});
			}
			tv.setTextSize(oclock.tv.getTextSize());
			tv.setPadding(0, 5, 0, 5);
			ti_start.setTextSize(oclock.tv.getTextSize());
			ti_start.setOnLongClickListener(new OnLongClickListener() {
				public boolean onLongClick(View v) {
					mainDialog();
					return true;
				}
			});
			ti_del.setTextSize(oclock.tv.getTextSize());
			ti_del.setOnLongClickListener(new OnLongClickListener() {
				public boolean onLongClick(View v) {
					mainDialog();
					return true;
				}
			});
			tv.setText(mTimerTime);
		}
		else if(mView != null)
			((View)mView).setVisibility(View.VISIBLE);
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	private void startTimering(int time) {
		mFullTime = time;
		timering = true;
		if (!Build.VERSION.SDK.equals("1") && !Build.VERSION.SDK.equals("2")) {
			mView = null;
			mRenderer = null;
			mView = new GLSurfaceView(getApplication());
			mRenderer = new KubeRenderer(makeGLWorld(), this);
			mView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
			mView.setRenderer(mRenderer);
			mView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
			mView.setZOrderOnTop(true);
			timerVisualMain.addView(mView);
		}
		timerInput.setVisibility(View.GONE);
		timerMainView.setVisibility(View.VISIBLE);
		timerProgress.setProgress(1);
		timerTV.setTextSize(oclock.tv.getTextSize());
		timerThis(time);
	}

	private void timerThis(final int time) {
		progressBarAnimator = ObjectAnimator.ofFloat(
				timerProgress, "progress", -1);
		progressBarAnimator.setInterpolator(new LinearInterpolator());
		progressBarAnimator.setDuration(time);
		progressBarAnimator.addListener(new AnimatorListener() {
			@Override
			public void onAnimationCancel(final Animator animation) {
			}

			@Override
			public void onAnimationEnd(final Animator animation) {
				timering = false;
				Uri notification = RingtoneManager
						.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				Ringtone r = RingtoneManager.getRingtone(
						getApplicationContext(), notification);
				r.play();
			}

			@Override
			public void onAnimationRepeat(final Animator animation) {
			}

			@Override
			public void onAnimationStart(final Animator animation) {
				timeringTimer();
			}
		});
		timerProgress.setMarkerEnabled(false);
		progressBarAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(final ValueAnimator animation) {
				int ms = (int) ((time * (Float) animation.getAnimatedValue()
						/ (-1) - time) * (-1));
				mCurrentTime = ms;
				int h = ms / 60 / 60 / 1000;
				int m = ms / 60 / 1000 - h * 60;
				int s = ms / 1000 - h * 60 * 60 - m * 60;
				int hs = ms / 10 - s * 100 - h * 60 * 60 * 100 - m * 60 * 100;
				String hstr = Integer.toString(h);
				if (hstr.length() < 2)
					hstr = "0" + hstr;
				String mstr = Integer.toString(m);
				if (mstr.length() < 2)
					mstr = "0" + mstr;
				String sstr = Integer.toString(s);
				if (sstr.length() < 2)
					sstr = "0" + sstr;
				String hsstr = Integer.toString(hs);
				if (hsstr.length() < 2)
					hsstr = "0" + hsstr;
				if (!hstr.equals("00"))
					timerTV.setText(hstr + ":" + mstr + ":" + sstr);
				else if (!mstr.equals("00"))
					timerTV.setText(mstr + ":" + sstr);
				else
					timerTV.setText(sstr + ":" + hsstr);
			}
		});
		progressBarAnimator.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_MENU) {
			mainDialog();
		} return super.onKeyDown(keyCode, event);
	}

	public void settings() {
		Intent i = new Intent(this, Settings.class);
		startActivity(i);
	}

	public void timeringTimer() {
		final Handler handler = new Handler();
		t2 = null;
		t2 = new Timer();
		tt2 = null;
		tt2 = new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						rotateTimes++;
					}
				});
			}
		};
		t2.schedule(tt2, 0, 20);
	}

	public void clockTimer() {
		final Handler handler = new Handler();
		t = null;
		t = new Timer();
		tt = null;
		tt = new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						Calendar cal = Calendar.getInstance();
						int hour = cal.get(Calendar.HOUR);
						int min = cal.get(Calendar.MINUTE);
						int ampm = cal.get(Calendar.AM_PM);
						if (!(hour == _hour && min == _min && ampm == _ampm)) {
							_hour = hour;
							_min = min;
							_ampm = ampm;
							updateTime(_hour, _min, _ampm);
						}
					}
				});
			}
		};
		t.schedule(tt, 0, 2000);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(!wfced) {
			wfced = true;
			if(extras != null) {
				if(extras.containsKey("timerNotifExtra")) {
					if(extras.getInt("timerNotifExtra") == 1) {
						int ms = extras.getInt("timerNotifExtraCurTime");
						totalCubeRotations = ms / 1000 * 2 - ms / 1000 / 30;
						startTimering(ms);
						timer();
					}
				}
			}
		}
	}

	public void updateTime(int hour, int min, int ampm) {
		for (int i = 0; i < timeViews.length; i++)
			timeViews[i].deactivate();
		if (ampm == Calendar.AM)
			am.activate();
		if (ampm == Calendar.PM)
			pm.activate();
		if (min < getResources().getInteger(R.integer.maxThisHourMin))
			hour(hour).activate();
		else
			hour(hour + 1).activate();
		if (min == 0)
			oclock.activate();
		else {
			if (min <= 3 || min >= 57) {
				shortly.activate();
				if (min < getResources().getInteger(R.integer.maxThisHourMin))
					after.activate();
				else
					before.activate();
			} else if (getResources().getInteger(R.integer.maxThisHourMin) >= 30
					|| !(min >= 26 && min <= 34)) {
				if (min < getResources().getInteger(R.integer.maxThisHourMin))
					past.activate();
				else
					to.activate();
			}
			if ((min >= 4 && min <= 7) || (min >= 53 && min <= 56)) {
				five1.activate();
				if (min != 5 && min != 55) {
					if (min == 4 || min == 53 || min == 54)
						almost.activate();
					else
						around.activate();
				}
			}
			if ((min >= 8 && min <= 12) || (min >= 48 && min <= 52)) {
				ten1.activate();
				if (min != 10 && min != 50) {
					if (min == 8 || min == 9 || min == 48 || min == 49)
						almost.activate();
					else
						around.activate();
				}
			}
			if ((min >= 13 && min <= 17) || (min >= 43 && min <= 47)) {
				quarter.activate();
				if (min != 15 && min != 45) {
					if (min == 13 || min == 14 || min == 43 || min == 44)
						almost.activate();
					else
						around.activate();
				}
			}
			if ((min >= 18 && min <= 25) || (min >= 35 && min <= 42)) {
				twenty.activate();
				if (min != 20 && min != 40) {
					if (min == 18 || min == 19 || (min >= 35 && min <= 39))
						almost.activate();
					else
						around.activate();
				}
			}
			if (min >= 26 && min <= 34) {
				half.activate();
				if (min != 30) {
					if (min >= 26 && min <= 29)
						almost.activate();
					else
						around.activate();
				}
			}
		}
	}

	public TimeView hour(int hour_) {
		if (hour_ == 1 || _hour == 13)
			return one;
		else if (hour_ == 2 || _hour == 14)
			return two;
		else if (hour_ == 3 || _hour == 15)
			return three;
		else if (hour_ == 4 || _hour == 16)
			return four;
		else if (hour_ == 5 || _hour == 17)
			return five;
		else if (hour_ == 6 || _hour == 18)
			return six;
		else if (hour_ == 7 || _hour == 19)
			return seven;
		else if (hour_ == 8 || _hour == 20)
			return eight;
		else if (hour_ == 9 || _hour == 21)
			return nine;
		else if (hour_ == 10 || _hour == 22)
			return ten;
		else if (hour_ == 11 || _hour == 23)
			return eleven;
		return twelve;
	}

	private GLWorld makeGLWorld() {
		GLWorld world = new GLWorld();

		int one = 0x10000;
		int half = 0x08000;
		GLColor red = new GLColor(one, 0, 0);
		GLColor green = new GLColor(0, one, 0);
		GLColor blue = new GLColor(0, 0, one);
		GLColor yellow = new GLColor(one, one, 0);
		GLColor orange = new GLColor(one, half, 0);
		GLColor white = new GLColor(one, one, one);
		GLColor black = new GLColor(0, 0, 0);

		float c0 = -1.0f;
		float c1 = -0.38f;
		float c2 = -0.32f;
		float c3 = 0.32f;
		float c4 = 0.38f;
		float c5 = 1.0f;

		mCubes[0] = new Cube(world, c0, c4, c0, c1, c5, c1);
		mCubes[1] = new Cube(world, c2, c4, c0, c3, c5, c1);
		mCubes[2] = new Cube(world, c4, c4, c0, c5, c5, c1);
		mCubes[3] = new Cube(world, c0, c4, c2, c1, c5, c3);
		mCubes[4] = new Cube(world, c2, c4, c2, c3, c5, c3);
		mCubes[5] = new Cube(world, c4, c4, c2, c5, c5, c3);
		mCubes[6] = new Cube(world, c0, c4, c4, c1, c5, c5);
		mCubes[7] = new Cube(world, c2, c4, c4, c3, c5, c5);
		mCubes[8] = new Cube(world, c4, c4, c4, c5, c5, c5);
		mCubes[9] = new Cube(world, c0, c2, c0, c1, c3, c1);
		mCubes[10] = new Cube(world, c2, c2, c0, c3, c3, c1);
		mCubes[11] = new Cube(world, c4, c2, c0, c5, c3, c1);
		mCubes[12] = new Cube(world, c0, c2, c2, c1, c3, c3);
		mCubes[13] = null;
		mCubes[14] = new Cube(world, c4, c2, c2, c5, c3, c3);
		mCubes[15] = new Cube(world, c0, c2, c4, c1, c3, c5);
		mCubes[16] = new Cube(world, c2, c2, c4, c3, c3, c5);
		mCubes[17] = new Cube(world, c4, c2, c4, c5, c3, c5);
		mCubes[18] = new Cube(world, c0, c0, c0, c1, c1, c1);
		mCubes[19] = new Cube(world, c2, c0, c0, c3, c1, c1);
		mCubes[20] = new Cube(world, c4, c0, c0, c5, c1, c1);
		mCubes[21] = new Cube(world, c0, c0, c2, c1, c1, c3);
		mCubes[22] = new Cube(world, c2, c0, c2, c3, c1, c3);
		mCubes[23] = new Cube(world, c4, c0, c2, c5, c1, c3);
		mCubes[24] = new Cube(world, c0, c0, c4, c1, c1, c5);
		mCubes[25] = new Cube(world, c2, c0, c4, c3, c1, c5);
		mCubes[26] = new Cube(world, c4, c0, c4, c5, c1, c5);

		int i, j;
		for (i = 0; i < 27; i++) {
			Cube cube = mCubes[i];
			if (cube != null)
				for (j = 0; j < 6; j++)
					cube.setFaceColor(j, black);
		}

		for (i = 0; i < 9; i++)
			mCubes[i].setFaceColor(Cube.kTop, orange);
		for (i = 18; i < 27; i++)
			mCubes[i].setFaceColor(Cube.kBottom, red);
		for (i = 0; i < 27; i += 3)
			mCubes[i].setFaceColor(Cube.kLeft, yellow);
		for (i = 2; i < 27; i += 3)
			mCubes[i].setFaceColor(Cube.kRight, white);
		for (i = 0; i < 27; i += 9)
			for (j = 0; j < 3; j++)
				mCubes[i + j].setFaceColor(Cube.kBack, blue);
		for (i = 6; i < 27; i += 9)
			for (j = 0; j < 3; j++)
				mCubes[i + j].setFaceColor(Cube.kFront, green);

		for (i = 0; i < 27; i++)
			if (mCubes[i] != null)
				world.addShape(mCubes[i]);

		mPermutation = new int[27];
		for (i = 0; i < mPermutation.length; i++)
			mPermutation[i] = i;

		createLayers();
		updateLayers();

		world.generate();

		for (int ii = 0; ii < totalCubeRotations; ii++) {
			int layerID = mRandom.nextInt(9);
			mCurrentLayerPermutation = mLayerPermutations[layerID];
			mLayers[layerID].startAnimation();
			mLayers[layerID].setAngle(-((float) Math.PI) / 2f);
			mLayers[layerID].endAnimation();
			int[] newPermutation = new int[27];
			for (int iii = 0; iii < 27; iii++)
				newPermutation[iii] = mPermutation[mCurrentLayerPermutation[iii]];
			mPermutation = newPermutation;
			rotatedLayers.add(Integer.valueOf(layerID));
			updateLayers();
		}

		return world;
	}

	private void createLayers() {
		mLayers[kUp] = new Layer(Layer.kAxisY);
		mLayers[kDown] = new Layer(Layer.kAxisY);
		mLayers[kLeft] = new Layer(Layer.kAxisX);
		mLayers[kRight] = new Layer(Layer.kAxisX);
		mLayers[kFront] = new Layer(Layer.kAxisZ);
		mLayers[kBack] = new Layer(Layer.kAxisZ);
		mLayers[kMiddle] = new Layer(Layer.kAxisX);
		mLayers[kEquator] = new Layer(Layer.kAxisY);
		mLayers[kSide] = new Layer(Layer.kAxisZ);
	}

	private void updateLayers() {
		Layer layer;
		GLShape[] shapes;
		int i, j, k;

		layer = mLayers[kUp];
		shapes = layer.mShapes;
		for (i = 0; i < 9; i++)
			shapes[i] = mCubes[mPermutation[i]];
		layer = mLayers[kDown];
		shapes = layer.mShapes;
		for (i = 18, k = 0; i < 27; i++)
			shapes[k++] = mCubes[mPermutation[i]];
		layer = mLayers[kLeft];
		shapes = layer.mShapes;
		for (i = 0, k = 0; i < 27; i += 9)
			for (j = 0; j < 9; j += 3)
				shapes[k++] = mCubes[mPermutation[i + j]];
		layer = mLayers[kRight];
		shapes = layer.mShapes;
		for (i = 2, k = 0; i < 27; i += 9)
			for (j = 0; j < 9; j += 3)
				shapes[k++] = mCubes[mPermutation[i + j]];
		layer = mLayers[kFront];
		shapes = layer.mShapes;
		for (i = 6, k = 0; i < 27; i += 9)
			for (j = 0; j < 3; j++)
				shapes[k++] = mCubes[mPermutation[i + j]];
		layer = mLayers[kBack];
		shapes = layer.mShapes;
		for (i = 0, k = 0; i < 27; i += 9)
			for (j = 0; j < 3; j++)
				shapes[k++] = mCubes[mPermutation[i + j]];
		layer = mLayers[kMiddle];
		shapes = layer.mShapes;
		for (i = 1, k = 0; i < 27; i += 9)
			for (j = 0; j < 9; j += 3)
				shapes[k++] = mCubes[mPermutation[i + j]];
		layer = mLayers[kEquator];
		shapes = layer.mShapes;
		for (i = 9, k = 0; i < 18; i++)
			shapes[k++] = mCubes[mPermutation[i]];
		layer = mLayers[kSide];
		shapes = layer.mShapes;
		for (i = 3, k = 0; i < 27; i += 9)
			for (j = 0; j < 3; j++)
				shapes[k++] = mCubes[mPermutation[i + j]];
	}

	public void animate() {
		mRenderer.setAngle(mRenderer.getAngle() + 1.2f);
		if (doneCubeRotations < totalCubeRotations && rotateTimes > 0) {
			while(rotateTimes > 0 && (totalCubeRotations - doneCubeRotations) > 0) {
				if (mCurrentLayer == null) {
					int layerID = rotatedLayers.get(totalCubeRotations
							- doneCubeRotations - 1);
					mCurrentLayer = mLayers[layerID];
					mCurrentLayerPermutation = mAntiLayerPermutations[layerID];
					mCurrentLayer.startAnimation();
					mCurrentAngle = 0;
					mAngleIncrement = ((float) Math.PI) / 50;
					mEndAngle =((float) Math.PI) / 2;
				}
				mCurrentAngle += mAngleIncrement;
				if (mCurrentAngle >= mEndAngle) {
					mCurrentLayer.setAngle(mEndAngle);
					mCurrentLayer.endAnimation();
					mCurrentLayer = null;
					int[] newPermutation = new int[27];
					for (int i = 0; i < 27; i++)
						newPermutation[i] = mPermutation[mCurrentLayerPermutation[i]];
					mPermutation = newPermutation;
					doneCubeRotations++;
					updateLayers();
				} else {
					mCurrentLayer.setAngle(mCurrentAngle);
				}
				rotateTimes--;
			}
		} else if (doneCubeRotations == totalCubeRotations){
			doneCubeRotations++;
			t2.cancel();
			tt2.cancel();
		}
	}

	GLSurfaceView mView;
	KubeRenderer mRenderer;
	Cube[] mCubes = new Cube[27];
	Layer[] mLayers = new Layer[9];
	static int[][] mLayerPermutations = {
			{ 2, 5, 8, 1, 4, 7, 0, 3, 6, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
					19, 20, 21, 22, 23, 24, 25, 26 },
			{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 20,
					23, 26, 19, 22, 25, 18, 21, 24 },
			{ 6, 1, 2, 15, 4, 5, 24, 7, 8, 3, 10, 11, 12, 13, 14, 21, 16, 17,
					0, 19, 20, 9, 22, 23, 18, 25, 26 },
			{ 0, 1, 8, 3, 4, 17, 6, 7, 26, 9, 10, 5, 12, 13, 14, 15, 16, 23,
					18, 19, 2, 21, 22, 11, 24, 25, 20 },
			{ 0, 1, 2, 3, 4, 5, 24, 15, 6, 9, 10, 11, 12, 13, 14, 25, 16, 7,
					18, 19, 20, 21, 22, 23, 26, 17, 8 },
			{ 18, 9, 0, 3, 4, 5, 6, 7, 8, 19, 10, 1, 12, 13, 14, 15, 16, 17,
					20, 11, 2, 21, 22, 23, 24, 25, 26 },
			{ 0, 7, 2, 3, 16, 5, 6, 25, 8, 9, 4, 11, 12, 13, 14, 15, 22, 17,
					18, 1, 20, 21, 10, 23, 24, 19, 26 },
			{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 11, 14, 17, 10, 13, 16, 9, 12, 15, 18,
					19, 20, 21, 22, 23, 24, 25, 26 },
			{ 0, 1, 2, 21, 12, 3, 6, 7, 8, 9, 10, 11, 22, 13, 4, 15, 16, 17,
					18, 19, 20, 23, 14, 5, 24, 25, 26 } };
	static int[][] mAntiLayerPermutations = {
			{ 6, 3, 0, 7, 4, 1, 8, 5, 2, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
					19, 20, 21, 22, 23, 24, 25, 26 },
			{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 24,
					21, 18, 25, 22, 19, 26, 23, 20 },
			{ 18, 1, 2, 9, 4, 5, 0, 7, 8, 21, 10, 11, 12, 13, 14, 3, 16, 17,
					24, 19, 20, 15, 22, 23, 6, 25, 26 },
			{ 0, 1, 20, 3, 4, 11, 6, 7, 2, 9, 10, 23, 12, 13, 14, 15, 16, 5,
					18, 19, 26, 21, 22, 17, 24, 25, 8 },
			{ 0, 1, 2, 3, 4, 5, 8, 17, 26, 9, 10, 11, 12, 13, 14, 7, 16, 25,
					18, 19, 20, 21, 22, 23, 6, 15, 24 },
			{ 2, 11, 20, 3, 4, 5, 6, 7, 8, 1, 10, 19, 12, 13, 14, 15, 16, 17,
					0, 9, 18, 21, 22, 23, 24, 25, 26 },
			{ 0, 19, 2, 3, 10, 5, 6, 1, 8, 9, 22, 11, 12, 13, 14, 15, 4, 17,
					18, 25, 20, 21, 16, 23, 24, 7, 26 },
			{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 15, 12, 9, 16, 13, 10, 17, 14, 11, 18,
					19, 20, 21, 22, 23, 24, 25, 26 },
			{ 0, 1, 2, 5, 14, 23, 6, 7, 8, 9, 10, 11, 4, 13, 22, 15, 16, 17,
					18, 19, 20, 3, 12, 21, 24, 25, 26 } };

	int[] mPermutation;
	Random mRandom = new Random(System.currentTimeMillis());
	Layer mCurrentLayer = null;
	float mCurrentAngle, mEndAngle;
	float mAngleIncrement;
	int[] mCurrentLayerPermutation;

	static final int kUp = 0;
	static final int kDown = 1;
	static final int kLeft = 2;
	static final int kRight = 3;
	static final int kFront = 4;
	static final int kBack = 5;
	static final int kMiddle = 6;
	static final int kEquator = 7;
	static final int kSide = 8;

	@Override
	protected void onResume() {
		super.onResume();
		TimerBroadcast.ms = -1000;
		/*if (prefs != null
				&& prefs.getBoolean("Light_Theme", true) != lightTheme) {
			finish();
			startActivity(getIntent());
		}*/
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if(timering) {
			Intent intent = new Intent(this, TimerService.class);
			intent.putExtra(TimerService.CURRENT_TIME, mCurrentTime);
			timering = false;
			tt2.cancel();
			t2.cancel();
			progressBarAnimator.removeAllListeners();
			progressBarAnimator.removeAllUpdateListeners();
			progressBarAnimator.cancel();
			startService(intent);
			TimerBroadcast.ms = mCurrentTime;
		}
	}
}