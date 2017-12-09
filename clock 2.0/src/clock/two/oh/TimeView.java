package clock.two.oh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class TimeView extends RelativeLayout{

	boolean activated = false;
	boolean last_one = false;
	
	public TimeView(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs, "");
	}

	public TimeView(Context context, AttributeSet attrs) {
		this(context, attrs, "");
	}

	public TimeView(Context context) {
		this(context, null, "");
	}

	String mText = "";
	
	public AutoResizeTextView tv;
	
	public TimeView(Context context, AttributeSet attrs, String text) {
		super(context, attrs);
		if(attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TimeView);
			mText = a.getString(R.styleable.TimeView_text);
			a.recycle();
		} else mText = text;
		tv = new AutoResizeTextView(getContext());
		addView(tv);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		int rowlength = 1;
		int rowitms = 5;
		if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			if(((LinearLayout)this.getParent()).getId() == R.id.row1) {rowlength = getContext().getResources().getInteger(R.integer.row1p); rowitms = 2;}
			if(((LinearLayout)this.getParent()).getId() == R.id.row2) {rowlength = getContext().getResources().getInteger(R.integer.row2p); rowitms = 3;}
			if(((LinearLayout)this.getParent()).getId() == R.id.row3) {rowlength = getContext().getResources().getInteger(R.integer.row3p); rowitms = 2;}
			if(((LinearLayout)this.getParent()).getId() == R.id.row4) {rowlength = getContext().getResources().getInteger(R.integer.row4p); rowitms = 3;}
			if(((LinearLayout)this.getParent()).getId() == R.id.row5) {rowlength = getContext().getResources().getInteger(R.integer.row5p); rowitms = 2;}
			if(((LinearLayout)this.getParent()).getId() == R.id.row6) {rowlength = getContext().getResources().getInteger(R.integer.row6p); rowitms = 3;}
			if(((LinearLayout)this.getParent()).getId() == R.id.row7) {rowlength = getContext().getResources().getInteger(R.integer.row7p); rowitms = 2;}
			if(((LinearLayout)this.getParent()).getId() == R.id.row8) {rowlength = getContext().getResources().getInteger(R.integer.row8p); rowitms = 3;}
			if(((LinearLayout)this.getParent()).getId() == R.id.row9) {rowlength = getContext().getResources().getInteger(R.integer.row9p); rowitms = 2;}
			if(((LinearLayout)this.getParent()).getId() == R.id.row10) {rowlength = getContext().getResources().getInteger(R.integer.row10p); rowitms = 3;}
			if(((LinearLayout)this.getParent()).getId() == R.id.row11) {rowlength = getContext().getResources().getInteger(R.integer.row11p); rowitms = 2;}
		} if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			if(((LinearLayout)this.getParent()).getId() == R.id.row1) rowlength = getContext().getResources().getInteger(R.integer.row1);
			if(((LinearLayout)this.getParent()).getId() == R.id.row2) rowlength = getContext().getResources().getInteger(R.integer.row2);
			if(((LinearLayout)this.getParent()).getId() == R.id.row3) rowlength = getContext().getResources().getInteger(R.integer.row3);
			if(((LinearLayout)this.getParent()).getId() == R.id.row4) rowlength = getContext().getResources().getInteger(R.integer.row4);
			if(((LinearLayout)this.getParent()).getId() == R.id.row5) rowlength = getContext().getResources().getInteger(R.integer.row5);
			if(((LinearLayout)this.getParent()).getId() == R.id.row6) {rowlength = getContext().getResources().getInteger(R.integer.row6); rowitms = 2;}
		}
		float f1 = rowitms*mText.length();
		float weight = f1/rowlength;
		this.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, weight));
		tv.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
		tv.setTextSize(100);
		tv.setText(mText);
		deactivate();
		LayoutParams tvlp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		tvlp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		tvlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		tv.setPadding(0, 5, 0, 5);
		tv.setSingleLine(true);
		tv.setGravity(Gravity.CENTER);
		tv.setLayoutParams(tvlp);
		tv.resizeText();
		if(last_one)
			Clock.mClock.clockTimer();
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	public void activate() {
		activated = true;
		tv.setTextColor(0xffff4444);
	}
	
	public void deactivate() {
		activated = false;
		tv.setTextColor(Clock.prefs.getBoolean("Light_Theme", true) ? 0x28000000 : 0x28ffffff);
	}
	
	public void styleUpdate() {
		if(activated)
			activate();
		else
			deactivate();
	}
}