package com.cetdhwani.bottlejump;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.cetdhwani.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



 


class MyAdapter extends BaseAdapter{
	Context con;
	List<leader> Leaders;
MyAdapter(Context context,List<leader> leaders)
{
	con=context;
Leaders=leaders;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		try
		{
	return	Leaders.size();
		}
		catch(Exception e)
		{
			return 0;
		}
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
    public View getView(int position, View convertView,
          ViewGroup parent) {                                           
       RelativeLayout itemView;
      LayoutInflater    mLayoutInflater = (LayoutInflater)con
                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       if (convertView == null) {                                        
          itemView = (RelativeLayout) mLayoutInflater.inflate(
                   R.layout.leader_item, parent, false);

       } else {
          itemView = (RelativeLayout) convertView;
       }

                      
       TextView pos = (TextView)
          itemView.findViewById(R.id.pos);                       
       TextView name = (TextView)
          itemView.findViewById(R.id.name);             
       TextView score = (TextView)
    	          itemView.findViewById(R.id.score);             
       pos.setText(Leaders.get(position).pos);
       name.setText(Leaders.get(position).name);
       score.setText(Leaders.get(position).score);
       return itemView;
    }
	void received(List<leader> leaders)
	{
		Leaders=leaders;
	
		notifyDataSetChanged();
	}
	
	
}
class leader
{
	String pos;
	String name;
	String score;
}