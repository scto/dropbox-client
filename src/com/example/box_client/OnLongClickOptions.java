package com.example.box_client;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

public class OnLongClickOptions implements OnItemLongClickListener{
	
	/*
	 * Properties
	 */
	private Activity activity;
	
	public OnLongClickOptions (Activity activity) {
		this.activity = activity;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View arg1, int index,
			long arg3) {
		// TODO Auto-generated method stub
		activity.registerForContextMenu(adapterView);
		ExplorerActivity.ITEM_LONGCLICKED = index;
		return false;
	}

}
