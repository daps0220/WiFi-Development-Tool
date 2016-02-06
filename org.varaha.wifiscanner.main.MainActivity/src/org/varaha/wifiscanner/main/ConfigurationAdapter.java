package org.varaha.wifiscanner.main;

import java.util.ArrayList;

import org.varaha.wifiscanner.data.Configurationlist;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ConfigurationAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	int paddingInDp = 8;  // 12 dps
   int paddingInPx = (int) (paddingInDp + 0.5f);
    
	ArrayList<Configurationlist> setListItem = new ArrayList<Configurationlist>();
			
	public ConfigurationAdapter(Context context,
			ArrayList<Configurationlist> listItem){
		
		this.setListItem = listItem; 
		mInflater = LayoutInflater.from(context);
	
	}
	
	public int getCount() {
		
		return setListItem.size();
		
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, 
			View convertView, ViewGroup parent) {
		ViewHolder holder;
		
					
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.conf_list_view, null);
			
			holder = new ViewHolder();
			holder.text1 = (TextView) convertView
					.findViewById(R.id.text1);
	
			holder.text2 = (TextView) convertView
					.findViewById(R.id.text2);
			
			
			
			holder.text1.setPadding(paddingInPx,0,0,0);
			holder.text2.setPadding(paddingInPx,0,0,0);
			
			
			convertView.setTag(holder);
		} 
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.text1.setText(setListItem.get(position).title);
		holder.text2.setText(setListItem.get(position).sub);
		
		
		return convertView;
	}

	/**
	 * class to hold the TextViews to be used in the list. 
	 * @author trainee
	 *
	 */
	 class ViewHolder { 
		TextView text1;
		TextView text2;
		
	}
}
