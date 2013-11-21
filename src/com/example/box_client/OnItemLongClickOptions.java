package com.example.box_client;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

public class OnItemLongClickOptions implements OnItemLongClickListener{
	
	/*
	 * Properties
	 */
	private Activity activity;
	
	public OnItemLongClickOptions (Activity activity) {
		this.activity = activity;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View arg1, int index,
			long arg3) {
		// TODO Auto-generated method stub
		activity.registerForContextMenu(adapterView);
		FileExplorer.ITEM_LONGCLICKED = index;
		return false;
	}

}
