package org.varaha.wifiscanner.data;

public class ScanResultParam{
		
		private String ssid_;
		private String bssid_;
		private String capabilities_;
		private String security_;
		private String keyMgmt_;
		private String groupCiphers_;
		
		private int freq_;
		private int signalLevel_;
		
		
		public void setSsid(String ssid){
			this.ssid_ = ssid;
		}
		
		public String getSsid(){
			return this.ssid_;
		}
		
		public void setBssid(String bssid){
			this.bssid_ = bssid;
		}
		
		public String getBssid(){
			return this.bssid_;
		}
		
		public void setCapabilities(String capabilities){
			this.capabilities_ = capabilities;
		}
		
		public String getCapabilities(){
			return this.capabilities_;
		}
		
		public void setSecurity(String security){
			this.security_ = security;
		}
		
		public String getSecurity(){
			return this.security_;
		}
		
		public void setKeyMgmt(String key){
			this.keyMgmt_ = key;
		}
		
		public String getKeyMgmt(){
			return this.keyMgmt_;
		}
		
		public void setGroupCipher(String groupCipher){
			this.groupCiphers_ = groupCipher;
		}
		
		public String getGroupCipher(){
			return this.groupCiphers_;
		}
		
		public void setFrequency(int frequency){
			this.freq_ = frequency;
		}
		
		public int getFrequency(){
			return this.freq_;
		}
		
		public void setSignalLevel(int signalLevel){
			this.signalLevel_ = signalLevel;
		}
		
		public int getSignalLevel(){
			return this.signalLevel_;
		}
}