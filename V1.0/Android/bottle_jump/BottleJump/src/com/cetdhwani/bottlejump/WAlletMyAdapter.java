
package com.cetdhwani.bottlejump;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.cetdhwani.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;





class MyWallet extends Fragment {
	static ListView list;
	 static ArrayList<String> couponList=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.abc_action_bar_decor, container, false);
         
        JSONObject data=new JSONObject();
        try {
			data.put("token", new Localstore(getActivity()).getdata("asdf"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        list=(ListView)rootView.findViewById(R.id.list);
       new SendData(data.toString(), "http://cetdhwani.com/bottlerun/wallet.php", 4, AfterGame.aftergame).execute();
       WAlletMyAdapter adapter=new WAlletMyAdapter(AfterGame.aftergame,couponList);
		list.setAdapter(adapter);
        
        return rootView;
    }
}

class WAlletMyAdapter extends BaseAdapter{
	Context con;
	List<String> coup;
    WAlletMyAdapter(Context context,List<String> coupons)
{
	con=context;
coup=coupons;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		try
		{
	return	coup.size();
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
                   R.layout.coupon_item, parent, false);

       } else {
          itemView = (RelativeLayout) convertView;
       }

                      
       TextView pos = (TextView)
          itemView.findViewById(R.id.coupon_code);                       
            
       pos.setText(coup.get(position));
       return itemView;
    }
	void received(List<String> coupons)
	{
		coup=coupons;
		notifyDataSetChanged();
	}
	
	
}

