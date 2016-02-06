package org.varaha.wifiscanner.main;

import org.varaha.wifiscanner.data.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;    
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;    
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;     
import android.content.IntentFilter;    
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;    
import android.net.wifi.WifiConfiguration;   
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;    
import android.os.Bundle;    
import android.util.Log;   
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;    
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;    
import android.widget.EditText;
import android.widget.ListView;    
import android.widget.TextView;    
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("DefaultLocale")
public class ScanActivity extends Activity 
 {      
    WifiManager wifiManager;
    long sec;
    AlertDialog autoscanDialog;
    TextView textStatus;
    TextView name_ssid;
    Button buttonScan;
    int size = 0;
    private Timer timer1 = new Timer();
    List<ScanResult> results;
    ListView scanListView_;
    CoreData coreData_;
    long time=500;
    ArrayList<ScanResultParam> scanList_;
    Context mContext_;
    boolean isNetworkConfigured_;
    ArrayList<ScanResultParam> scanNetList_;
    ArrayList<ListClass> listItem;
    public static List<ScanResult> scanResultList;
    String current_ssid,curr_bssid;
    WifiInfo wifiInfo;
    NetworkInfo mWifi;
    ProgressDialog myProgressDialog;
    BroadcastReceiver registerReceiver_;
    private SharedPreferences _sharedPrefs;
    private Editor _prefsEditor;
    private static final String APP_SHARED_PREFS = "Interval_Time_Prefs";
    public static final String KEY_PREFS_INTEVAL_TIME = "interval_time";
    Long get_time_interval;
    
    
    public ScanActivity()
    {
    		
    }
    /* Called when the activity is first created. */
    @SuppressLint({ "ParserError", "ParserError", "ParserError" })
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanall);
  
        			  Log.d("Inside onCreate().....", "Curreent SSID::" + current_ssid);
        mContext_ = getApplicationContext();
		scanList_=new ArrayList<ScanResultParam>();
		coreData_ = new CoreData();
		isNetworkConfigured_ = false;
		wifiManager = (WifiManager) mContext_.getSystemService(Context.WIFI_SERVICE);
        textStatus = (TextView) findViewById(R.id.textStatus);
        scanListView_ = (ListView) findViewById(R.id.scan_list);
       
        scanListView_.setOnCreateContextMenuListener(this);
		
		scanListView_.setBackgroundColor(Color.rgb(0, 0, 0));
		
		_sharedPrefs = getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
		_prefsEditor = _sharedPrefs.edit();

        if (wifiManager.isWifiEnabled() == false)
        {
            new AlertDialog.Builder(this)
            .setTitle("WiFi is Disable")
            .setMessage("Please click OK for enable WiFi")
            .setPositiveButton("OK", new OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                	wifiManager.setWifiEnabled(true);
                }
            }).
            setNegativeButton("Cancel", new OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                	System.exit(0);
                }
            }).create().show();
            
        }   
        else
        {
        	myProgressDialog =new ProgressDialog(ScanActivity.this);
			  myProgressDialog.setTitle("Please wait...");
			  myProgressDialog.setMessage("Application is getting started...");
			  myProgressDialog.show();
        }
       
        get_time_interval=_sharedPrefs.getLong(KEY_PREFS_INTEVAL_TIME,time);      
        timer1.schedule(new MyTimerTask(), 10, get_time_interval);
        Log.d("Inside timer 1","time::" + time);
       
        
        registerReceiver_ =new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context c, Intent intent) 
            {
            	String action=intent.getAction();
            	 int extraWifiState = 
            			 intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE , 
            			                     WifiManager.WIFI_STATE_UNKNOWN);
            	
            	
            	switch(extraWifiState){
                
               
                case WifiManager.WIFI_STATE_ENABLING:
                	myProgressDialog =new ProgressDialog(ScanActivity.this);
  				  myProgressDialog.setTitle("Please wait...");
  				  myProgressDialog.setMessage("Wifi Enabling...");
  				  
  				  myProgressDialog.show();
                    break;    
                    
                case WifiManager.WIFI_STATE_DISABLED:
                    Toast.makeText(getApplicationContext(), "WI-FI  DISABLED.", Toast.LENGTH_SHORT).show();
                    break;
                
                 default:
                    	break;
                }

            	if(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action))
            	{
            		
            		System.out.print("ACTION::" + action);	
            	scanResultList = wifiManager.getScanResults();
            	size=scanResultList.size();
            	showSSID();
            	 
            		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
         		mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
         		WifiManager wifi_ = (WifiManager) getSystemService(WIFI_SERVICE);
        		wifiInfo = wifi_.getConnectionInfo();
        		current_ssid=wifiInfo.getSSID();
        		 curr_bssid=wifiInfo.getBSSID();
        		myProgressDialog.dismiss();
                 
            	}
            }
        }; 
        Toast.makeText(getApplicationContext(), "Auto Scan after every " + get_time_interval + " mili-seconds.", Toast.LENGTH_SHORT).show();
        mContext_.registerReceiver(registerReceiver_, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        mContext_.registerReceiver(registerReceiver_, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        
       }
   
    private void showSSID() 
    {
    	
        scanList_ = getScanResultParam();
    	
        coreData_.scanNetList_ = scanList_;
		
        coreData_.listItem_ = formatScanResults(scanList_,curr_bssid);
       
        ArrayList<ListClass> listItem = coreData_.listItem_;		
    	
    		scanListView_.setAdapter(
    				new EfficientAdapter(getApplicationContext(),
    							listItem));    
    		scanListView_.setOnItemClickListener(new OnItemClickListener(){

    			public void onItemClick(AdapterView<?> arg0, View arg1, 
    					int position, long arg3) {
    					
    				Log.d("Inside OnClickListener::", "Position Value ::" + position);
    				showConfiguration(position);
    				
    			}
    			
    		});
		    }
    private class MyTimerTask extends TimerTask{

        @Override
        public void run() {        
            runOnUiThread(new Runnable() {              
                @Override
                public void run() {
                	wifiManager.startScan();
                    }
            });
        }       
    }
    
    
    private ArrayList<ScanResultParam> getScanResultParam() 
			throws NullPointerException{    	
    	
		Log.d("Core", "Inside startScanning()");
		ScanResultParam scanResultParam;
		ScanResult scanResult = null;
	 	
	   	ArrayList<Integer> level_ = new ArrayList<Integer>();
	   	
	   	for(int i = 0; i < scanResultList.size(); i++){
	   		level_.add(scanResultList.get(i).level);
	   	}
	   	
	   	HashSet<Integer> hs = new HashSet<Integer>();
		hs.addAll(level_);
		level_.clear();
		level_.addAll(hs);
	   	
	   	Comparator<Integer> comparator = Collections.reverseOrder();
	    Collections.sort(level_, comparator);
		
	    int f = 0;
		ArrayList<ScanResult> scanResultListFinal = new ArrayList<ScanResult>();
		
		for(int i = 0; i < level_.size(); i++){
			f = 0;
			for(int j = 0; j < scanResultList.size(); j++){
				f=0;
				
				if(level_.get(i).equals(scanResultList.get(j).level)){
					f++;
					if(f==1)
					scanResultListFinal.add(scanResultList.get(j));
					}
				
			}
		}
		
		scanNetList_ = new ArrayList<ScanResultParam>();
	   	for(int i=0;i<scanResultListFinal.size();i++)
	   	{	
	   		scanResultParam = new ScanResultParam();
			    		
	   		scanResult = scanResultListFinal.get(i);
	   		scanResultParam.setSsid(scanResult.SSID);
	   		scanResultParam.setBssid(scanResult.BSSID);
	   		scanResultParam.setFrequency(scanResult.frequency);
	   		scanResultParam.setSignalLevel(scanResult.level);
	   		scanResultParam.setCapabilities(scanResult.capabilities);
	   		
	   		scanNetList_.add(scanResultParam);  		
	   		
	   	}	   		   		   		
	   	
	   	return scanNetList_;
	}
	
	
	/**
	 * Returns the list of access point scan in the form of
	 * {@link ListClass} objects.
	 *  
	 * @param scanNetList
	 * @return the list of access point scan 
	 * in the form of {@link ListClass} objects.
	 */
	@SuppressLint("ParserError")
	public ArrayList<ListClass> formatScanResults(
    		final ArrayList<ScanResultParam> scanNetList,String bssid_)
    		{
		
    	 
		Log.d("Core", "Inside showScanResults()");
		
		try {
			
    	ListClass listClass;  	
    	listItem = new ArrayList<ListClass>();    	   	
    	for(int k = 0; k < scanNetList.size(); k++){
    		    		
    		listClass = new ListClass();
    		listClass.title = scanNetList.get(k).getSsid();
    		 String temp_bssid = scanNetList.get(k).getBssid();
    		listClass.sub = scanNetList.get(k).getBssid();
        		int slevel_=scanNetList.get(k).getSignalLevel();
    		listClass.Slevel=String.valueOf(slevel_);
    		//listClass.status = "Connected";
    		
    		if(temp_bssid.matches(bssid_))
    		{
    			listClass.status = "Connected" ;
    		}
    		else
    		{
    			listClass.status = "NotConnected" ;
    		}
    		
    		listItem.add(listClass);
    		listClass = null;
    		
    	} 	
    	Log.d("Core", "Attempting to put message in coreQueue: " +
    			"in showScanResults()");

    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    	return listItem;
	}
	
	/**
	 * Checks whether a network is configured in the
	 * supplicant or not. 
	 * 
	 * @param ssid
	 * @return {@link true} if network is configured, 
	 * {@link false} otherwise
	 */
	public boolean isNetworkConfigured(String ssid) {
		
		WifiManager wifiManager = (WifiManager) 
				mContext_.getSystemService(Context.WIFI_SERVICE);
  		List<WifiConfiguration> configList = 
		  				wifiManager.getConfiguredNetworks();
  		
  		for(int j = 0; j < configList.size(); j++){
  			if(configList.get(j).SSID.equalsIgnoreCase("\""+ssid+"\"")){
  				isNetworkConfigured_ = true;
  				break;
  			}
  			else{
  				isNetworkConfigured_ = false;
  			}
  		}
  		
  		return isNetworkConfigured_;
  	
  	}
 

    /**
     * Show configuration of the network selected.
     * 
     * @param position
     */
	
    
	
@SuppressLint("DefaultLocale")
public void showConfiguration(int position){
	Log.d("Inside showConfig().....", "Curreent SSID::" + current_ssid);
	String temp_ssid= coreData_.scanNetList_.get(position).getSsid();
	    ListView config_view = new ListView(this);
    	final AlertDialog.Builder alertDialog = 
    			new AlertDialog.Builder(this);
    	alertDialog.setTitle("Configuration");
    	
    	config_view.setBackgroundColor(Color.rgb(0, 0, 0));
    	
    	Configurationlist listClassCurr;
		ArrayList<Configurationlist> configListItem = 
				new ArrayList<Configurationlist>();
		listClassCurr = new Configurationlist();
		listClassCurr.title = "SSID";
		listClassCurr.sub = coreData_.scanNetList_.get(position).getSsid();
		configListItem.add(listClassCurr);
		
		if (temp_ssid.matches(current_ssid))
		{
		    // Do whatever
			int ipAddress=wifiInfo.getIpAddress();
			String ip = null;

			ip = String.format("%d.%d.%d.%d",
			(ipAddress & 0xff),
			(ipAddress >> 8 & 0xff),
			(ipAddress >> 16 & 0xff),
			(ipAddress >> 24 & 0xff));
			
			listClassCurr = new Configurationlist();
			listClassCurr.title = " Link Speed ";
			listClassCurr.sub = String.valueOf(wifiInfo.getLinkSpeed()) + " " + WifiInfo.LINK_SPEED_UNITS;
			configListItem.add(listClassCurr);
			
			listClassCurr = new Configurationlist();
			listClassCurr.title = " IP Address ";
			listClassCurr.sub = ip;
			configListItem.add(listClassCurr);
		
		}
		
		listClassCurr = new Configurationlist();
		listClassCurr.title = "BSSID";
		listClassCurr.sub = coreData_.scanNetList_.get(position).getBssid();
		configListItem.add(listClassCurr);
		
		
		listClassCurr = new Configurationlist();
		listClassCurr.title = "Signal Level";
		
		 int s_level= coreData_.scanNetList_.get(position).getSignalLevel();
		 if(s_level<0 && s_level>=-50)
			 listClassCurr.sub=" Exellent ";	
			else if(s_level<-50 && s_level>=-63)
				listClassCurr.sub=" Better ";
			else if(s_level<-63 && s_level>=-75)
				listClassCurr.sub=" Good ";
			else if(s_level<-75 && s_level>=-87)
				listClassCurr.sub=" Medium ";
			else if(s_level<-87 && s_level>=-100)
				listClassCurr.sub=" Low ";
			
		configListItem.add(listClassCurr);
		
		listClassCurr = new Configurationlist();
		listClassCurr.title = "Security";
		listClassCurr.sub = coreData_.scanNetList_.get(position).getCapabilities();
		configListItem.add(listClassCurr);
		
		listClassCurr = new Configurationlist();
		listClassCurr.title = "Frequency";
		listClassCurr.sub = String.valueOf(coreData_.scanNetList_.get(position).getFrequency());
		configListItem.add(listClassCurr);
	
		config_view.setAdapter(new 
				ConfigurationAdapter(this,configListItem));
		
		alertDialog.setNegativeButton("OK", 
   				new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
			
		});
		
		alertDialog.setView(config_view);   
    	alertDialog.show();
    }
	

	
@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	new AlertDialog.Builder(this)
    .setTitle("Exit")
    .setMessage("Are you sure you want to exit?")
    .setNegativeButton("No",null)
    .setPositiveButton("Yes", new OnClickListener(){
    	public void onClick(DialogInterface arg0, int arg1)
    	{
    		System.exit(0);
    	}
    }).create().show();
	
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// TODO Auto-generated method stub
	menu.add(0, 1, 1, "Exit").setIcon(R.drawable.exit);
	menu.add(0, 2, 2, "Settings").setIcon(R.drawable.settings);
	
	return super.onCreateOptionsMenu(menu);
	
}

private void openOptionsExitDialog()
 {
  new AlertDialog.Builder(this)
  .setTitle("Exit").setMessage("Are you sure you want to exit?")
  .setNegativeButton("No", null)
  .setPositiveButton("Yes",
   new DialogInterface.OnClickListener() {
    
    @Override
    public void onClick(DialogInterface dialog, int which) {
     // TODO Auto-generated method stub
     System.exit(0);
    }
   }
    )
  .show();
 }

private void openOptionsSettingsDialog()
{
	LayoutInflater inflater = (LayoutInflater) 
			 ScanActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
    View layout = inflater.inflate(R.layout.autoscan,
                    (ViewGroup) findViewById(R.id.autoscan_dialog));
    
    AlertDialog.Builder  builder = 
			 new AlertDialog.Builder(ScanActivity.this);
	builder.setView(layout);
	
	final EditText seconds=(EditText) layout.findViewById(R.id.secondText);
	get_time_interval=_sharedPrefs.getLong(KEY_PREFS_INTEVAL_TIME,time);  
	seconds.setText(String.valueOf(get_time_interval));
	seconds.setSelection(seconds.getText().length());
		builder.setPositiveButton("OK", 
				new DialogInterface.OnClickListener() {
		
			public void	 onClick(DialogInterface dialog, int which) {
				
			// TODO Auto-generated method stub
			//dismiss
			//timer.scheduleAtFixedRate(new My-TimerTask(),1,sec);
				timer1.cancel();
				//timer2.cancel();
				String sec_string;
				try{
				 sec_string=seconds.getEditableText().toString();
					Log.d("Inside OK", "Value of Seconds:: "+ sec_string);
					if(sec_string.matches(""))
					{
						sec=500;
					}
					else
					{
						sec=(Long.parseLong(seconds.getEditableText().toString()));
						if(sec<=500)
							sec=500;
					}
					
				}
				catch(NullPointerException e)
				{
					
					//sec=500;
					e.printStackTrace();
					
				}
				
				_prefsEditor.putLong(KEY_PREFS_INTEVAL_TIME, sec);
		         _prefsEditor.commit();
		         //double sec_print=sec/1000;
			
					Log.d("Inside timer 2","time::" + sec);
					
					
					Toast.makeText(getApplicationContext(), "Auto Scan after every " + sec + " mili-seconds.", Toast.LENGTH_SHORT).show();
					timer1=new Timer();
					timer1.schedule(new MyTimerTask(), 1, sec);
				
		}
	});
	
	autoscanDialog = builder.create();
    autoscanDialog.setTitle("AutoScan Interval");
	autoscanDialog.show();
	

}
 
@Override
 public boolean onOptionsItemSelected(MenuItem item) {
  // TODO Auto-generated method stub
  super.onOptionsItemSelected(item);
  
  switch(item.getItemId())
  {
  case 1:
   openOptionsExitDialog();
   break;
  case 2:
  openOptionsSettingsDialog();
  break;
 }
  return super.onOptionsItemSelected(item);
 }


}