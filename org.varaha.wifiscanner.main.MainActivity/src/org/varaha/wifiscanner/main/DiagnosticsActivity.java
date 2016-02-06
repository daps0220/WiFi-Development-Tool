
package org.varaha.wifiscanner.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import android.content.DialogInterface.OnClickListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

@SuppressLint({ "HandlerLeak", "HandlerLeak" })
public class DiagnosticsActivity extends Activity  {
	Button pingButton;
	TextView pingText,connText;
	TextView pingPacket;
	EditText pingCommand, pingPacketNumber;
	String address;
	String packetNumber;
	Thread pingThread = null;
	ScrollView netDiagInfoScroll;
	ProgressDialog myProgressDialog;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			String string = bundle.getString("myKey");
			pingText.setText(string);
			netDiagInfoScroll.scrollBy(0, netDiagInfoScroll.getBottom());
			
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.netdiag);
		netDiagInfoScroll=(ScrollView) findViewById(R.id.infoScroll);
		pingButton = (Button) findViewById(R.id.bPing);
		pingPacketNumber = (EditText) findViewById(R.id.tPacketNumber);
		pingCommand = (EditText) findViewById(R.id.tPingCommand);
		pingText = (TextView) findViewById(R.id.tReturnResult);
		connText = (TextView) findViewById(R.id.tConnectionResult);
		
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (ni.isConnected() == false) {
			// There are no active networks.
			pingText.setVisibility(View.INVISIBLE);
			connText.setVisibility(View.VISIBLE);
			connText.setText("Connection is not Available");
			
		} else {
			pingButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			    	mgr.hideSoftInputFromWindow(pingButton.getWindowToken(), 0);
					
				  myProgressDialog =new ProgressDialog(DiagnosticsActivity.this);
				  myProgressDialog.setTitle("Please wait...");
				  myProgressDialog.setMessage("Loading...");
				  myProgressDialog.show();
				  					
				  pingText.setText("     ");
					address = pingCommand.getText().toString();
					packetNumber = pingPacketNumber.getText().toString();
					
					Runnable runnable = new Runnable() {
						public void run() {
							
							try {
									String pingCommand = "ping -c " + packetNumber + " ";
									Process process = Runtime.getRuntime().exec(
											pingCommand + address);
									BufferedReader reader = new BufferedReader(
											new InputStreamReader(process.getInputStream()));
									int i;
									char[] buffer = new char[8192];
									StringBuffer output = new StringBuffer();
									while ((i = reader.read(buffer)) > 0) {
										output.append(buffer, 0, i);
										String data = new String();
										data = output.toString();
										Message msg = handler.obtainMessage();
										myProgressDialog.dismiss();
										Bundle bundle = new Bundle();
										bundle.putString("myKey", data);
										msg.setData(bundle);
										handler.sendMessage(msg);
									
									}
									reader.close();
									
								} catch (IOException e) {
									e.printStackTrace();
								}
							
						}
					};
					Thread mythread = new Thread(runnable);
					mythread.start();

				}
			});
		}
}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
	    .setTitle("Exit")
	    .setMessage("Are you sure you want to exit?")
	    .setNegativeButton("No",null)
	    .setPositiveButton("Yes", new OnClickListener(){

			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
	    	
	    }).create().show();
		
	}
}