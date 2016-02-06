package org.varaha.wifiscanner.main;



import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
 
public class MainActivity extends TabActivity {
    /** Called when the activity is first created. */
	
	 private TabHost mTabHost;
     
     private void setupTabHost() {
 		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
 		mTabHost.setup();
 	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupTabHost();
		mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
		
		setupTab(new TextView(this), "Scan", "ScanActivity.class");
		setupTab(new TextView(this), "SSID", "SelectSSIDActivity.class");
		setupTab(new TextView(this), "Diagnostics", "DiagnosticsActivity.class");

	//	mTabHost.setOnTabChangedListener(this);
		
		
       }
    
   /* public void onTabChanged(String tabId) {
    	TabHost tabHost=getTabHost();
    	LocalActivityManager manager = getLocalActivityManager();
    	String currentTag = tabHost.getCurrentTabTag();
    	Class<? extends Activity> currentClass = manager.getCurrentActivity().getClass();
    	manager.destroyActivity(currentTag, true);
    	manager.startActivity(currentTag, new Intent(this, currentClass));
        }
*/
    
    
	private void setupTab(final View view, final String tag, final String className) {
		View tabview = createTabView(mTabHost.getContext(), tag);
				getTabHost();    
				Intent intent;
				intent = new Intent().setClass(this, ScanActivity.class);
				if (className.equals("ScanActivity.class")) {
				intent = new Intent().setClass(this, ScanActivity.class);}
				if (className.equals("SelectSSIDActivity.class")) {
				intent = new Intent().setClass(this, SelectSSIDActivity.class);}
				if (className.equals("DiagnosticsActivity.class")) {
					intent = new Intent().setClass(this, DiagnosticsActivity.class);}

	TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent); {

};
mTabHost.addTab(setContent);
}
    
    private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}

    
    
}
