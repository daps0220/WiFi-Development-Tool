package org.varaha.wifiscanner.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Timer;
import org.varaha.wifiscanner.data.Configurationlist;
import org.varaha.wifiscanner.data.CoreData;
import org.varaha.wifiscanner.data.ListClass;
import org.varaha.wifiscanner.data.ScanResultParam;
import org.varaha.wifiscanner.main.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint({ "ParserError", "ParserError" })
public class SelectSSIDActivity extends Activity implements TextWatcher{

	String curr_ssid,get_ssid,curr_bssid;
    ScanActivity scan_act = new ScanActivity();
    CoreData coreData_ = new CoreData();
    ArrayList<ScanResultParam> scanNetListDefault;
    ArrayList<ScanResultParam> scanList_ = new ArrayList<ScanResultParam>();
    ListView  scanListView_;
    String specified_ssid = "";
    TextView ap_text,title,blank,connectedSSID,txt1;
    AutoCompleteTextView choose_ssid;
    Timer t=new Timer();
	WifiInfo wifiInfo;
    NetworkInfo mWifi,mWifi_;
    Context context;

	String[] list_ssid;
    BroadcastReceiver defaultReceiver_;
    
    
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectssid);
		
		
		scanListView_ = (ListView) findViewById(R.id.ssid_list);
		
	    context = getApplicationContext();
	    scanListView_.setOnCreateContextMenuListener(this);
		
		scanListView_.setBackgroundColor(Color.rgb(0, 0, 0));
		
		ap_text=(TextView) findViewById(R.id.value_ssid);
		choose_ssid = (AutoCompleteTextView)findViewById(R.id.editSSID);
		 
		final Button ok=(Button) findViewById(R.id.bOK);
				
		title=(TextView) findViewById(R.id.title);
		blank=(TextView) findViewById(R.id.blnk);
		connectedSSID = (TextView) findViewById(R.id.connectedSSID);
		txt1=(TextView) findViewById(R.id.textMsg1);
		
		
		checkConnection();
		ap_text.setText(curr_ssid);
		showDefaultSSID(ap_text.getText().toString());
        defaultReceiver_=new BroadcastReceiver()
	        {
	            @Override
	            public void onReceive(Context c, Intent intent) 
	            {
	            	String action = intent.getAction();
	            	if(WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action))
	            	{
	            	checkConnection();
	            	showDefaultSSID(ap_text.getText().toString());
	            	}
	            	
	            	else if(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action))
	            	{
	            		changelist();
	            		showDefaultSSID(ap_text.getText().toString());
	            	}
	            	
	            }
	        }; 
	         showDefaultSSID(ap_text.getText().toString());
			 context.registerReceiver(defaultReceiver_, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
		     context.registerReceiver(defaultReceiver_, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
			
	        changelist();
	        
	    ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			
				InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		    	mgr.hideSoftInputFromWindow(ok.getWindowToken(), 0);
		    	
				get_ssid=choose_ssid.getEditableText().toString();
				
				if(get_ssid.matches(""))
				{
					
					if(mWifi.isConnected())
					{
						Toast.makeText(getApplicationContext(), curr_ssid, Toast.LENGTH_SHORT).show();
						ap_text.setText(curr_ssid);
						showDefaultSSID(curr_ssid);
					}
					
					else
					{
						ap_text.setText("");
						showDefaultSSID("");
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), get_ssid, Toast.LENGTH_SHORT).show();
					ap_text.setText(get_ssid);
					showDefaultSSID(get_ssid);
				}
			}
		});
		
		Log.d("Inside onCreateSSID()", "Size ::" + curr_ssid);
		
	}

    public void changelist()
    {
    	ArrayList<String> ssid = new ArrayList<String>();
    	for(int i = 0; i < ScanActivity.scanResultList.size(); i++){
	   		ssid.add(ScanActivity.scanResultList.get(i).SSID);
	   	}
	   	HashSet<String> hs = new HashSet<String>();
		hs.addAll(ssid);
		ssid.clear();
		ssid.addAll(hs);

		Collections.sort(ssid);
	    list_ssid = new String[ssid.size()];
	    list_ssid =  ssid.toArray(list_ssid);
		
   	
    
    Log.d("choosen ssid", "name::" + choose_ssid.getText().toString());
    choose_ssid.addTextChangedListener(this);
    choose_ssid.setAdapter(new ArrayAdapter<String>(this,R.layout.my_custom_dropdown, list_ssid));
    choose_ssid.setDropDownBackgroundResource(R.color.white);
    
    }
	 	
		public void checkConnection()
	 	{
	 		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	 		mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	 		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
			   wifiInfo = wifiManager.getConnectionInfo();
			   
			    // Do whatever
    			
			   if (mWifi.isConnected()) {
		     		
			 		connectedSSID.setText(wifiInfo.getSSID());
					
					  curr_ssid=wifiInfo.getSSID();
					  curr_bssid=wifiInfo.getBSSID();
					  title.setVisibility(View.VISIBLE);
					  blank.setVisibility(View.VISIBLE);
					  connectedSSID.setVisibility(View.VISIBLE);
					  txt1.setVisibility(View.INVISIBLE);
			 	
					  
					 
			   }  
    		
    		if(mWifi.isConnected()==false){
    			title.setVisibility(View.GONE);
    			blank.setVisibility(View.GONE);
    			connectedSSID.setVisibility(View.INVISIBLE);
    			txt1.setVisibility(View.VISIBLE);
    			 	}
        	
        		}
	 
	 	
		private void showDefaultSSID(String s) {
		// TODO Auto-generated method stub
		scanList_ = getScanResultParamDefault(s);
    	
        coreData_.scanNetList_ = scanList_;
		
        coreData_.listItem_ = scan_act.formatScanResults(scanList_,curr_bssid);
        Log.d("Inside showDefaultSSID", "After format");
        
        ArrayList<ListClass> listItem = coreData_.listItem_;		
    
        scanListView_.setAdapter(
    				new EfficientAdapter(getApplicationContext(),
    							listItem)); 
        
    		scanListView_.setOnItemClickListener(new OnItemClickListener(){

    			public void onItemClick(AdapterView<?> arg0, View arg1, 
    					int position, long arg3) {
    					
    				Log.d("Inside OnClickListener::", "Position Value ::" + position);
    				showConfigurationDefault(position);
    				
    			}
    			
    		});

	}
 	
		
		
		
 	private ArrayList<ScanResultParam> getScanResultParamDefault(String ssid) 
			throws NullPointerException{    	
    	
		
 		Log.d("Core", "Inside getScanResultParamDefault()");
		ScanResultParam scanResultParam;
		
		ScanResult scanResult = null;
		
		int f ;
		ArrayList<ScanResult> scanResultListFinal = new ArrayList<ScanResult>();
		
		
			for(int j = 0; j < ScanActivity.scanResultList.size(); j++){
				f=0;
				if(ssid.equals(ScanActivity.scanResultList.get(j).SSID)){
					f++;
					if(f==1)
					scanResultListFinal.add(ScanActivity.scanResultList.get(j));
				}
				
			}
		
	   	scanNetListDefault = new ArrayList<ScanResultParam>();
	   	Log.d("For Loop Final", "Inside Final List");    	
	   	for(int i=0;i<scanResultListFinal.size();i++)
	   	{	
	   		scanResultParam = new ScanResultParam();
			scanResult = scanResultListFinal.get(i);
	   		scanResultParam.setSsid(scanResult.SSID);
	   		scanResultParam.setBssid(scanResult.BSSID);
	   		scanResultParam.setFrequency(scanResult.frequency);
	   		scanResultParam.setSignalLevel(scanResult.level);
	   		scanResultParam.setCapabilities(scanResult.capabilities);
	   		scanNetListDefault.add(scanResultParam);  		
	   		
	   	}	   		   		   		
	   	
	   	return scanNetListDefault;
	}
 	
public void showConfigurationDefault(int position){
    	
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
		
		String nw_ssid=ap_text.getText().toString();
		if (nw_ssid==curr_ssid || nw_ssid=="") {
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
			listClassCurr.sub = String.valueOf(wifiInfo.getLinkSpeed())+" "+WifiInfo.LINK_SPEED_UNITS;
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
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
 	
}
