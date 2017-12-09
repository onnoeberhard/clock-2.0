package clock.two.oh;

import clock.two.oh.R;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class Settings extends PreferenceActivity{

//	PreferenceCategory appPC;
	CheckBoxPreference bonw;
//	PreferenceCategory widgetPC;
//	CheckBoxPreference newaw;
	
	@SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPreferenceScreen(createPreferenceHierarchy());
    }
	
	@SuppressWarnings({ "deprecation"})
	public PreferenceScreen createPreferenceHierarchy(){
    	PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);

//    	appPC = new PreferenceCategory(this);
//    	appPC.setTitle(R.string.app);
//        root.addPreference(appPC);
//    	
    	
    	bonw = new CheckBoxPreference(this);
        bonw.setKey("Light_Theme");
        bonw.setTitle(R.string.Light_Theme);
        bonw.setDefaultValue(true);
        root.addPreference(bonw);
    	
//        widgetPC = new PreferenceCategory(this);
//        widgetPC.setTitle(R.string.widget);
//        root.addPreference(widgetPC);
//
//    	newaw = new CheckBoxPreference(this);
//    	newaw.setKey("new_aw");
//    	newaw.setTitle(R.string.new_aw);
//    	newaw.setDefaultValue(true);
//    	widgetPC.addPreference(newaw);
        
        return root;
	}
	
}
