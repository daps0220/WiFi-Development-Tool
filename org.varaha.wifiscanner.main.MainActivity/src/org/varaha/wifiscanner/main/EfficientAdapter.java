package org.varaha.wifiscanner.main;

import java.util.ArrayList;

import org.varaha.wifiscanner.data.ListClass;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EfficientAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	int paddingInDp = 8;  // 12 dps
   int paddingInPx = (int) (paddingInDp + 0.5f);
    
	ArrayList<ListClass> setListItem = new ArrayList<ListClass>();
			
	public EfficientAdapter(Context context,
			ArrayList<ListClass> listItem){
		
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
					R.layout.custom_view, null);
			
			holder = new ViewHolder();
			holder.text1 = (TextView) convertView
					.findViewById(R.id.text1);
	
			holder.text2 = (TextView) convertView
					.findViewById(R.id.text2);
			
			holder.text3 = (TextView) convertView
					.findViewById(R.id.text3);
			
			holder.text4 = (TextView) convertView
					.findViewById(R.id.text4);
			
			holder.itemImage = (ImageView) convertView
					.findViewById(R.id.item_image);
			
			holder.text1.setPadding(paddingInPx,0,0,0);
			holder.text2.setPadding(paddingInPx,0,0,0);
			holder.text3.setPadding(paddingInPx,0,0,0);
			holder.text4.setPadding(paddingInPx,0,0,0);
			
			convertView.setTag(holder);
		} 
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.text1.setText(setListItem.get(position).title);
		holder.text2.setText(setListItem.get(position).sub);
		holder.text3.setText("rssi:  " + setListItem.get(position).Slevel);	
		holder.text4.setText(setListItem.get(position).status);
		
		String temp_status=setListItem.get(position).status;
		
		if(temp_status.matches("Connected"))
		{
			holder.text4.setText(setListItem.get(position).status);
			Log.d("Inside adapter", "status holder after"+setListItem.get(position).status);
		}
		else
		{
			holder.text4.setVisibility(View.GONE);
		}
		int l=Integer.parseInt(setListItem.get(position).Slevel);
		if(l<0 && l>=-50)
			holder.itemImage.setImageResource(R.drawable.signal_exellent);
		else if(l<-50 && l>=-63)
			holder.itemImage.setImageResource(R.drawable.signal_better);
		else if(l<-63 && l>=-75)
			holder.itemImage.setImageResource(R.drawable.signal_good);
		else if(l<-75 && l>=-87)
			holder.itemImage.setImageResource(R.drawable.signal_medium);
		else if(l<-87 && l>=-100)
			holder.itemImage.setImageResource(R.drawable.signal_low);
		
		
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
		TextView text3;
		TextView text4;
		ImageView itemImage;
	}
}

